package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * 
 * As a member of the model package, this class implements business logic.
 * 
 * @author Lawrence Colman
 */
public class Salt extends EqualableModel<Salt> implements TransferableModel<flymetomars.common.datatransfer.Salt> {
    private String hashedSaltKey;
    private String obfuscatedSalt;
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    
    //<editor-fold defaultstate="collapsed" desc="obfuscation provider code">

    private MessageDigest obfuscationProvider=new MessageDigest("Base64") {
        private List<Byte> content=new ArrayList<Byte>();
        @Override
        public void engineUpdate(byte input) { content.add(input); }
        @Override
        public void engineUpdate(byte[] input, int offset, int len) {
            for(int i=offset;i<len;i++) { this.engineUpdate(input[i]); }
        }
        @Override
        public byte[] engineDigest() {
            byte[] input=new byte[this.content.size()];
            for(int i=0;i<content.size();i++) { input[i]=content.get(i); }
            byte[] result=Base64.encodeBase64(input);
            this.engineReset();
            return result;
        }
        @Override
        public void engineReset() { this.content=new ArrayList<Byte>(); }
    };
    
    //</editor-fold>
    
    /**
     * Default constructor for initializing the class variables
     */
    public Salt() {
        this.hashedSaltKey="";
        this.obfuscatedSalt="";
    }
    
     /**
     * Copy constructor that takes a DTO to a Salt object
     * 
     * @param dto Data Transfer Object representing a Salt 
     */
    public Salt(flymetomars.common.datatransfer.Salt dto) {
        this();
        if(null!=dto) {
            this.setHashedSaltKey(dto.getHashedSaltKey());
            this.setObfuscatedSalt(dto.getObfuscatedSalt());
        }
    }
    
     /**
     * To convert a SaltedPassword model object to a dto
     */
    public final flymetomars.common.datatransfer.Salt toDTO() {
        flymetomars.common.datatransfer.Salt result;
        try {
            result=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Salt.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Salt.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setHashedSaltKey(this.getHashedSaltKey());
        result.setObfuscatedSalt(this.getObfuscatedSalt());     
        return result;   
    }

    //<editor-fold desc="ObfuscatedSalt property">
    
    /**
     * Method to get the obfuscatedSalt of the salt
     * @return
     */
    public String getObfuscatedSalt() {
        return obfuscatedSalt;
    }
    
    /**
     * Method to set the obfuscatedSalt of the salt
     * @param obfuscatedSalt
     */
    public final void setObfuscatedSalt(String obfuscatedSalt) {
        if(null==obfuscatedSalt) { throw new NullArgumentException("salt cannot be null"); }
        this.obfuscatedSalt = obfuscatedSalt;
    }
    
    //</editor-fold>
    
    //<editor-fold desc="PureSalt property">
    
    /**
     * Method to get the pureSalt of the salt
     * @return
     */
    public String getPureSalt() {
        byte[] bytes=Base64.decodeBase64(this.obfuscatedSalt);
        StringBuilder result=new StringBuilder();
        for(byte b : bytes) { result.append((char)b); }
        return result.toString();
    }
    
    /**
     * Method to set the pureSalt of the salt
     * @param actualSaltData
     */
    public void setPureSalt(String actualSaltData) {
        if(null==actualSaltData) {
            throw new NullArgumentException("Cannot set salting data to null - try empty string");
        }
        byte[] buf = new byte[actualSaltData.length()];
        for(int b=0;b<actualSaltData.length();b++) {
            buf[b]=(byte)actualSaltData.toCharArray()[b];
        }
        this.obfuscationProvider.update(buf);
        StringBuilder salter=new StringBuilder();
        buf=this.obfuscationProvider.digest();
        for(byte by : buf) { salter.append((char)by); }
        this.obfuscatedSalt = salter.toString();
    }
    
    //</editor-fold>

    //<editor-fold desc="HashedSaltKey property">
    
    /**
     * Method to get the hashedSaltKey of the salt
     * @return
     */
    public String getHashedSaltKey() {
        return hashedSaltKey;
    }
    
    /**
     * Method to set the hashedSaltKey of the salt
     * @param hashedSaltKey
     */
    public final void setHashedSaltKey(String hashedSaltKey) {
        if(null==hashedSaltKey) { throw new NullArgumentException("Salt key cannot be null"); }
        if(hashedSaltKey.isEmpty()) { throw new IllegalArgumentException("Salt key cannot be empty"); }
        this.hashedSaltKey = hashedSaltKey;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="equalsModel and hashCode implementations">
    
    @Override
    public boolean equals(Object obj)  {
        if (null==obj) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        return this.equalsModel((Salt)obj);
    }
    
    @Override
    public boolean equalsModel(Salt o) {
        if(!this.getHashedSaltKey().equals(o.getHashedSaltKey())) { return false; }
        if(!this.getObfuscatedSalt().equals(o.getObfuscatedSalt())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 2+1,bit=(2*2*2*2)+((2*2*2)-1);
        hash = bit * hash + (this.hashedSaltKey != null ? this.hashedSaltKey.hashCode() : 0);
        hash = bit * hash + (this.obfuscatedSalt != null ? this.obfuscatedSalt.hashCode() : 0);
        return hash;
    }

    //</editor-fold>
    
}
