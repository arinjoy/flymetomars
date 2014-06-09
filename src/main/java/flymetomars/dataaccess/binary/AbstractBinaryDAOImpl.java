package flymetomars.dataaccess.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.RefreshFailedException;

/**
 * Implementation helper class for Binary based DAO Implementation classes
 * 
 * @author Lawrence Colman
 */
/*package-private*/ abstract class AbstractBinaryDAOImpl {
       
    //<editor-fold defaultstate="collapsed" desc="bean status logging utilities">
    
    /**
     * Utility overload for main log method `INFO' Level events
     * 
     * @param message String describing event to submit for Logger input
     */
    protected void log(String message) { this.log(message,Level.INFO); }
    
    /**
     * Submits an issue or event to the Logger
     * 
     * @param message String describing issue
     * @param status The Level of the issue being logged
     */
    protected void log(String message, Level status) {
        Logger.getLogger(this.getClass().getName()).log(status, message);
        /*if(null==status) { throw new NullArgumentException("null status passed to internal loggin method"); }
        while(status.length()<5) { status=status+' '; }
        message=null==message?"<NULL> message recieved":message;
        StringBuilder msg=new StringBuilder();
        msg.append((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS ")).format(new Date()));
        msg.append(status);
        msg.append(" [");
        msg.append(this.getClass().getCanonicalName());
        msg.append("] - ");
        msg.append(message);
        System.err.println(msg.toString());*/
    }
    
    //</editor-fold>
    
    //<editor-fold desc="data store management and interaction">
    
    /**
     * Causes the internal binary file to be re-read and populate the in-memory datastore.
     * 
     * @throws RefreshFailedException thrown when either the rehash fails, or cannot be completed.
     */
    public abstract void rehashData() throws RefreshFailedException;
    
    /**
     * Performs file open operation system call for establishing data store.
     * 
     * @throws IOException thrown when a filesystem issue is encountered on the data store
     */
    protected static RandomAccessFile openDatastore(String filePath) throws IOException {
        RandomAccessFile datastore=null;
        do {
            try {
                datastore=new RandomAccessFile(filePath,"rwd");
                break;
            } catch(FileNotFoundException fnfe) {
                Logger.getLogger(AbstractBinaryDAOImpl.class.getName()).log(Level.WARNING,
                    "Specified datafile not found on filesystem... Attempting to create",fnfe
                );
                (new File(filePath)).createNewFile();
                continue;
            }
        } while(null==datastore);
        return datastore;
    }
    
    /**
     * Privatised implementation interface for the underlying
     * functionality behind openDatastore method
     * 
     * @throws IOException thrown when a filesystem issue is encountered on the data store
     */
    protected abstract void flushToDatastore() throws IOException;

    //</editor-fold>
    
}
