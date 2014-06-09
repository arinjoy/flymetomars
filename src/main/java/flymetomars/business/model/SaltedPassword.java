package flymetomars.business.model;

import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
//import flymetomars.common.validation.StringValidator;
import flymetomars.common.NullArgumentException;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 * As a member of the model package, this class implements business logic.
 * 
 * @author Lawrence Colman
 */
public class SaltedPassword extends IdentifiableModel<SaltedPassword> implements TransferableModel<flymetomars.common.datatransfer.SaltedPassword> {
    
    private String actualPasswordHash;
    private Salt salt;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.PlainPassword> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.PlainPassword>(
            flymetomars.common.datatransfer.PlainPassword.class, dtoFactory
    );

    //<editor-fold defaultstate="collapsed" desc="hash provider fields">
    
    private static final String HASH_ALGORITHM="MD5";   
    private final MessageDigest hashProvider;
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hash utility method">
    private static final int HEX=16;
    private static final int SQ1=(HEX*HEX)-1;
    
    /**
     *
     * @param input
     * @return
     */
    protected String hash(String input) {
        byte[] pass=new byte[input.length()];
        for(int c=0;c<input.length();c++) {pass[c]=(byte)input.charAt(c);}
        this.hashProvider.update(pass);
        pass=this.hashProvider.digest();
        StringBuilder result=new StringBuilder();
        for(byte b : pass) {
        if((b&SQ1)<HEX){ result.append('0'); }
        result.append(Integer.toHexString(b&SQ1).toUpperCase());
        }
        return result.toString();
    }
    //</editor-fold>

    /**
     * Default constructor to initialize the class variables
     */
    public SaltedPassword() {
        //this.actualPasswordHash="";
        try {
            this.hashProvider=MessageDigest.getInstance(HASH_ALGORITHM);
        } catch(NoSuchAlgorithmException nsae) {
            throw new UnsupportedOperationException(nsae);
        }
    }
    
    /**
     * Copy constructor that takes a DTO to a SaltedPassword object
     * 
     * @param dto Data Transfer Object representing a SaltedPassword 
     */
    public SaltedPassword(flymetomars.common.datatransfer.SaltedPassword dto) {
        this();
        this.setDigest(dto.getDigest());
        this.setId(dto.getId());
    }
    
    /**
     * To convert a SaltedPassword model object to a dto
     */
    @Override
    public final flymetomars.common.datatransfer.SaltedPassword toDTO() {
        flymetomars.common.datatransfer.SaltedPassword result;
        try {
            result=this.dtoFactory.createDTO(flymetomars.common.datatransfer.SaltedPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaltedPassword.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setDigest(this.getDigest());
        if(null!=this.getSalt()) { result.setSaltId(this.getSalt().getHashedSaltKey()); }
        result.setId(this.getId());
        return result;   
    }
    //<editor-fold desc="Password property">
    
    /**
     * Method to check if the 'candidate' is a password
     * @param candidate
     * @return
     */
    public boolean isPassword(String candidate) {
        if(null==this.actualPasswordHash) {
            throw new IllegalStateException("Cannot validate supplied password against unpopulated digest");
        }
        if(null==candidate) { return false; }
        String salty="";
        if(null!=this.getSalt()) { salty=this.getSalt().getPureSalt(); }
        String hashConstruct=this.hash(candidate+salty);
        return this.getDigest().equals(hashConstruct);
    }

    /**
     * Method to set the password
     * @param password
     */
    public void setPassword(String password) {
        /* What the password validation arguments mean
         * Minimum Length is 8,
         * Required Uppercase letters:  0,
         * Required Lowercase letters:  0,
         * Required letters (any case): 1,
         * Required Numbers: 1,
         * Required Symbols: 1,  symbols are: !@#$%^&*
         * Whitespace Allowed: false.
         */
        /* Old Validation (non-JS):
        StringValidator.validatePasswordAgainstConstraints(password, 8, 0, 0, 1, 1, 1, false);
        StringValidator.validateStringLengthInRange(password, 8, 20);
        */
        flymetomars.common.datatransfer.PlainPassword dto;
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.PlainPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaltedPassword.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setPasswordText(password);
        logic.applyRules("Password","passwordText",dto);
        //digest logic: (not Rhino-ified)
        String salty=this.getSalt().getPureSalt();
        this.setDigest(this.hash(password+salty));
    }
    
    //</editor-fold>
    
    //<editor-fold desc="Digest property">
    
    /**
     * Method to get the password hash
     * @return
     */
    public String getDigest() {
        return actualPasswordHash;
    }
    
    /**
     * Method to set the password hash
     * @param passwordHash
     */
    public final void setDigest(String passwordHash) {
        if(null==passwordHash) { throw new NullArgumentException("password hash cannot be null"); }
        if(passwordHash.length()%2>0 || !passwordHash.matches("[0-9A-Z]+")) {  //test for hex encodeing
            throw new IllegalArgumentException("Supplied password hash argument is not a valid digest");
        }
        this.actualPasswordHash = passwordHash;
    }
    
    //</editor-fold>

    //<editor-fold desc="Salt property">
    
    /**
     * Method to get the salt
     * @return
     */
    public Salt getSalt() {
        return salt;
    }
    
    /**
     * Method to set the salt
     * @param salt
     */
    public void setSalt(Salt salt) {
        if(null==salt) { throw new NullArgumentException("Salt instance cannot be set to null"); }
        this.salt = salt;
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="equalsModel and hashCode implementations">
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        final SaltedPassword other = (SaltedPassword) obj;
        return this.equalsModel(other);
    }

    @Override
    public int hashCode() {
        int hash = 2+2+1,bit=hash*hash+(((hash*hash)+1)*2);
        hash = bit * hash + (this.actualPasswordHash != null ? this.actualPasswordHash.hashCode() : 0);
        hash = bit * hash + (this.salt != null ? this.salt.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equalsModel(SaltedPassword o) {
        return null==this.actualPasswordHash ? (null==o.actualPasswordHash) : this.actualPasswordHash.equals(o.actualPasswordHash);
    }
    
    //</editor-fold>
    
}
