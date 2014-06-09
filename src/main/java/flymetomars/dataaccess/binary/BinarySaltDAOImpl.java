package flymetomars.dataaccess.binary;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Salt;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.SaltDAO;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javax.security.auth.RefreshFailedException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Binary based implementation of DAO operations pertaining to Salt domain objects
 * 
 * This class is a customised JavaBean designed to be resident in a Spring environment
 * 
 * @author Lawrence Colman
 */
public class BinarySaltDAOImpl extends AbstractBinaryDAOImpl implements SaltDAO, ApplicationContextAware {

    private final String filePath;
    private RandomAccessFile datastore;
    private static final Semaphore FILE_LOCK=new Semaphore(1);
    
    private static volatile boolean constructed=false;
    private static volatile SaltDAO instance;
    
    private static final int SLEEPER_INTERVAL=250;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    /**
     * Standard singleton pattern getInstance property accessor, suspends the
     * execution of the calling thread indefinitely until Sprint has instantiated
     * an instance of the class.
     * 
     * @return either the instance if constructed, or a sleeper-proxy wrapper
     */
    public static synchronized SaltDAO getInstance() {
        return constructed?instance:new SaltDAO() {  //sleeper-proxy wrapping
            private SaltDAO getStub() {while(!constructed) {try { Thread.sleep(SLEEPER_INTERVAL); }
                catch(InterruptedException ie) { throw new IllegalStateException(ie); }}
            return instance;}
            //<editor-fold defaultstate="collapsed" desc="self wrapping method overloads">
            @Override
            public Salt load(String saltKey) {return getStub().load(saltKey);}
            @Override
            public List<Salt> getAll() { return getStub().getAll(); }
            @Override
            public void save(Salt object) { getStub().save(object); }
            @Override
            public void update(Salt object) { getStub().update(object); }
            @Override
            public void saveOrUpdate(Salt object) { getStub().saveOrUpdate(object); }
            @Override
            public Salt delete(Salt object) { return getStub().delete(object); }
            //</editor-fold>
        };
    }
    
    private static final Map<String,String> memoryStage=new ConcurrentHashMap<String,String>();
    
    private long lastRehash=0;
    private static final long MAX_REHASH_FREQ=10000; //miliseconds
    
    private long lastFlush=0;
    private static final long MAX_FLUSH_FREQ=1000; //miliseconds
    
    private static final int HASH_LENGTH=16;
    
    /**
     * Constructs a new singleton BinarySaltDaoImpl instance for saving and loading salts.
     * 
     * @param dataFilePath a String file-path to the binary data-file for use as
     *          the backing store to load and save the salt data to
     * @throws InstantiationException thrown when an instance of the class already
     *          exists and construction would violate the singleton contract.
     * @throws IOException thrown to indicate an error in opening the supplied
     *          `dataFilePath' upon the file-system.
     */
    public BinarySaltDAOImpl(String dataFilePath) throws InstantiationException, IOException, BackingStoreException {
        /*if(constructed || null!=application) {
            throw new InstantiationException("BinarySaltDaoImpl is a singleton and cannot be constructed more than once");
        }*/
        this.application=null;
        this.filePath=dataFilePath;
        try {
            this.rehash();
        } catch(RefreshFailedException rfe) {
            this.log("Exception occured during initial hashing of the specified datafile:", Level.SEVERE);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="data store management and interaction">
    
    /**
     * Causes the internal binary file to be re-read and populate the in-memory datastore.
     * 
     * @throws RefreshFailedException thrown when either the rehash fails, or cannot be completed.
     */
    @Override
    public void rehashData() throws RefreshFailedException {
        long now=(new Date()).getTime();
        if(this.lastRehash-now>MAX_REHASH_FREQ) {
            String e=null;
            try {
                this.rehash();
            } catch (IOException ex) {
                e=ex.getMessage();
            }
            if(null!=e) { throw new RefreshFailedException(e); }
            this.lastRehash=now;
        } else {
            throw new RefreshFailedException("The requested rehash could not "+
                    "be fulfilled immediatly due to frequency-overrun."
            );
        }
    }
    
    /**
     * Executes a file-based refresh of in-memory data state.  Always performed
     * at first load/startup of singletonian instance, as well as for durability.
     * 
     * @throws RefreshFailedException thrown when either the rehash fails, or cannot be completed.
     */
    private synchronized void rehash() throws RefreshFailedException, IOException {
        try{
            FILE_LOCK.acquire();
            this.datastore=openDatastore(this.filePath);
        } catch(SecurityException se) {
            throw new IOException(se);
        } catch(InterruptedException ie) {
            throw new IllegalStateException(ie);
        }
        this.log("(Re)-loading password salt information from file: "+this.filePath);
        memoryStage.clear();
        String t=null;
        try {
            if(this.datastore.length()>0) {  //we have existing records
                for(;this.datastore.getFilePointer()<this.datastore.length();) {  //record loop
                    if(this.datastore.length()-this.datastore.getFilePointer()<HASH_LENGTH) {
                        throw new RefreshFailedException("Binary file length mismatch occurred!");
                    }
                    //digest read-in:
                    byte[] digest=new byte[HASH_LENGTH];
                    try {
                        this.datastore.read(digest);
                    } catch(IOException ioe) {
                        t="IO error occurred during digest read-in: ".concat(ioe.getMessage());
                        throw ioe;
                    }
                    //salt read-in:
                    StringBuilder salty=new StringBuilder();
                    byte c;
                    while(this.datastore.length()-this.datastore.getFilePointer()>0) {  //characters left before EOF
                        c=this.datastore.readByte();  //exception is caught by outer try/catch
                        if(c==0) { break; }
                        salty.append(c);
                    }
                    //in-memory table population:
                    String hash=hashHex(digest);
                    if(memoryStage.containsKey(hash)) {
                        throw new RefreshFailedException("Duplicate key found in datastore!");
                    }
                    memoryStage.put(hash, salty.toString());
                }
            }
        } catch(IOException ioe) {
            t="File operation caused IO error: ".concat(ioe.getMessage());
        } finally {
            this.datastore.close();
            FILE_LOCK.release();
        }
        if(t!=null){ throw new RefreshFailedException(t); }
    }
    
    /**
     * Public accessible wrapper implementation for the
     * underlying functionality within rehash method
     * 
     * @throws IOException thrown when a filesystem issue is encountered on the data store
     */
    @Override
    public void flushToDatastore() throws IOException {
        long now=(new Date()).getTime();
        if(this.lastFlush-now>MAX_FLUSH_FREQ) {
            this.flush();
            this.lastFlush=now;
        }
    }
    
    /**
     * Implementation detail of the algorithm/s for persisting Salt data to disk
     * in binary form from the in-memory resident non-durable data structures
     * 
     * @throws IOException thrown when a filesystem issue is encountered on the data store
     */
    private synchronized void flush() throws IOException {
        try{
            FILE_LOCK.acquire();
            this.datastore=openDatastore(this.filePath);
        } catch(SecurityException se) {
            throw new IOException(se);
        } catch(InterruptedException ie) {
            throw new IllegalStateException(ie);
        }
        this.log("Serializing password salt information to file: "+this.filePath);
        if(memoryStage.size()>0) { //we have records to persist
            byte[][] hashKeys=new byte[memoryStage.size()][HASH_LENGTH];
            StringBuilder buf=new StringBuilder();
            long totalSaltLength=0L;
            int[] lengthOfSalts=new int[memoryStage.size()];
            int i=0;
            for(String key : memoryStage.keySet()) {  //record read loop
                String value=memoryStage.get(key);
                byte[] digest=hashNum(key);
                hashKeys[i]=digest;
                lengthOfSalts[i]=value.length();
                totalSaltLength+=lengthOfSalts[i];
                buf.append(value);
                i+=1;
            }
            //intermediate record loop:
            StringReader saltValues=new StringReader(buf.toString());
            buf=null;  //enable GC
            long dataFileSize=totalSaltLength+(this.memoryStage.size()*(HASH_LENGTH+1));
            byte[] serialisedDataStream=new byte[(int)dataFileSize];  //LARGE!
            int fp=0;
            for(int saltIndex=0;saltIndex<lengthOfSalts.length;saltIndex++) {
                for(byte b : hashKeys[saltIndex]) { serialisedDataStream[fp++]=b; }  //add hash
                for(int n=lengthOfSalts[saltIndex];n>0;n--) {
                    serialisedDataStream[fp++]=(byte)saltValues.read();  //add salt
                }
                serialisedDataStream[fp++]=0;  //null record terminator
            }
            //datastore write-out:
            this.datastore.seek(0);
            this.datastore.write(serialisedDataStream);
            this.datastore.close();
            FILE_LOCK.release();
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hexidecimal hashcode representation utility code">
    
    private static final char[] HEX=new char[]{0,1,2,3,4,5,6,7,8,9,'A','B','C','D','E','F'};
    
    private static String hashHex(byte[] hash) {
        StringBuilder result=new StringBuilder();
        for(byte num : hash) {
            int sixteens=num/HEX.length;
            int ones=num%HEX.length;
            result.append(HEX[sixteens]);
            result.append(HEX[ones]);
        }
        return result.toString();
    }
    
    private static byte[] hashNum(String asciiHash) {
        if(asciiHash.length()%2==1) {
            throw new IllegalArgumentException("ASCII encoded hex hash digest length mismatch!");
        }
        byte[] result=new byte[asciiHash.length()/2];
        for(int c=0;c<asciiHash.length();c+=2) {
            String hexValue=asciiHash.substring(c, c+2);
            boolean foundOne=false;
            for(int hexChar=0;hexChar<HEX.length;hexChar++) {
                if(hexValue.toCharArray()[0]==HEX[hexChar]) {
                    result[((int)(c/2))]=(byte)(hexChar*HEX.length);
                    if(foundOne) {
                        break;
                    } else {
                        foundOne=true;
                    }
                }
                if(hexValue.toCharArray()[1]==HEX[hexChar]) {
                    result[((int)(c/2))]=(byte)hexChar;
                    if(foundOne) {
                        break;
                    } else {
                        foundOne=true;
                    }
                }
            }
        }
        return result;
    }
    
    //</editor-fold>

    //<editor-fold desc="SaltDAO interface implementation">
    
    @Override
    /**
     * 
     * 
     * @throws NullArgumentException
     */
    public Salt load(String saltKey) {
        if(null==saltKey) { throw new NullArgumentException("cannot load Salt from null"); }
        String salt=memoryStage.get(saltKey);
        if(null==salt) { return null; }
        Salt result;
        try {
            result = this.dtoFactory.createDTO(Salt.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BinarySaltDAOImpl.class.getName()).log(Level.SEVERE, "unable to load DTO for Salt model", ex);
            throw new IllegalStateException(ex);
        }
        result.setHashedSaltKey(saltKey);
        result.setObfuscatedSalt(salt);
        return result;
    }
    
    @Override
    /**
     * 
     */
    public List<Salt> getAll() {
        List<Salt> result=new ArrayList<Salt>();
        for(String key : memoryStage.keySet()) {
            result.add(this.load(key));
        }
        return result;
    }
    
    @Override
    /**
     * 
     * 
     * @throws NullArgumentException
     * @throws RejectedExecutionException
     * @throws IllegalStateException
     */
    public void save(Salt entry) {
        if(null==entry) { throw new NullArgumentException("cannot save null Salt"); }
        if(memoryStage.containsKey(entry.getHashedSaltKey())) {
            throw new RejectedExecutionException("Cannot save an already persisted Salt instance");
        }
        if(null==entry.getHashedSaltKey()) {
            throw new NullArgumentException("cannot save Salt with null ID");
        }
        memoryStage.put(entry.getHashedSaltKey(), entry.getObfuscatedSalt());
        try { this.flushToDatastore(); }
        catch(IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }
    
    @Override
    /**
     * 
     * 
     * @throws NullArgumentException
     * @throws RejectedExecutionException
     * @throws IllegalStateException
     */
    public void update(Salt entry) {
        if(null==entry) { throw new NullArgumentException("cannot update null Salt"); }
        if(!memoryStage.containsKey(entry.getHashedSaltKey())) {
            throw new RejectedExecutionException("Cannot update a transient Salt object");
        }
        memoryStage.put(entry.getHashedSaltKey(), entry.getObfuscatedSalt());
        try { this.flushToDatastore(); }
        catch(IOException ioe) {
            throw new IllegalStateException(ioe);
        }
    }
    
    @Override
    /**
     * 
     * 
     * @throws NullArgumentException
     * @throws RejectedExecutionException
     * @throws IllegalStateException
     */
    public void saveOrUpdate(Salt entry) {
        try {
            this.save(entry);
        } catch(RejectedExecutionException ree) {
            this.update(entry);
        }
    }
    
    @Override
    /**
     * 
     * 
     * @throws NullArgumentException
     * @throws UnsupportedOperationException
     */
    public Salt delete(Salt theSalt) {
        if(null==theSalt) { throw new NullArgumentException("cannot delete null Salt"); }
        if(!memoryStage.containsKey(theSalt.getHashedSaltKey())) {
            throw new UnsupportedOperationException("Cannot delete a non-existent Salt instance");
        }
        memoryStage.remove(theSalt.getHashedSaltKey());
        return theSalt;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Spring Framework JavaBean cruft">
    private ApplicationContext application;
    
    @Override
    /**
     * Implementation method required for ApplicationContextAware interface.
     * Neccessery as bean needs to know about itself for singleton instance.
     * 
     * @throws BeansException thrown when Spring cannot retrieve a bean instance
     */
    public void setApplicationContext(ApplicationContext ac) {
        this.application=ac;
        constructed=true;
        BinarySaltDAOImpl.instance=this.application.getBean(BinarySaltDAOImpl.class);
    }
    //</editor-fold>
    
}
