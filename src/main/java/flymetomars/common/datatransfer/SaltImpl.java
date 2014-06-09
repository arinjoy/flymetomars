package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;

/**
 * A representative class for Salt values used within SaltedPassword objects
 * 
 * @author Lawrence Colman
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public class SaltImpl implements EqualableStructure<Salt>, Salt {
    
    private String hashedSaltKey;
    private String obfuscatedSalt;
    
    /**
     * default constructor
     */
    public SaltImpl() {}

    //<editor-fold desc="ObfuscatedSalt property">
    
    /**
     * Property accessor for the `ObfuscatedSalt' value of a Salt
     * 
     * @return String encoded inner salting string used for hash salting
     */
    @Override
    public String getObfuscatedSalt() {
        return obfuscatedSalt;
    }
    
    /**
     * Property mutator for the `ObfuscatedSalt' value of a Salt
     * 
     * @param obfuscatedSalt String to be stored for use in hash salting
     * @throws NullArgumentException thrown when `obfuscatedSalt' is a null value
     */
    @Override
    public void setObfuscatedSalt(String obfuscatedSalt) {
        if(null==obfuscatedSalt) { throw new NullArgumentException("salt cannot be null"); }
        this.obfuscatedSalt = obfuscatedSalt;
    }
    
    //</editor-fold>

    //<editor-fold desc="HashedSaltKey property">
    
    /**
     * Property accessor for the `HashedSaltKey' id of Salt instance
     * 
     * @return String hash value of specified salt string identifier
     */
    @Override
    public String getHashedSaltKey() {
        return hashedSaltKey;
    }
    
    /**
     * Property mutator for the `HashedSaltKey' id of Salt instance
     * 
     * @param hashedSaltKey String hash value of desired salt string identifier
     * @throws NullArgumentException thrown when `hashedSaltKey' is a null ref
     */
    @Override
    public void setHashedSaltKey(String hashedSaltKey) {
        if(null==hashedSaltKey) { throw new NullArgumentException("key cannot be null"); }
        this.hashedSaltKey = hashedSaltKey;
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="equalsDTO and hashCode implementations">

    /**
     * Standard Java Object.equals(Object) method signature override for sorting
     * 
     * @param obj Object to be checked for equality with this object
     * @return boolean value indicating if the representations contained within
     * this object and obj are effectively equivalent (thus equal for sorting).
     */
    @Override
    public boolean equals(Object obj)  {
        if (null==obj) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        return this.equalsDTO((SaltImpl)obj);
    }
    
    /**
     * 
     * 
     * @param o
     * @return 
     */
    @Override
    public boolean equalsDTO(Salt o) {
        if((null==this.getHashedSaltKey()&&null!=o.getHashedSaltKey())||!this.getHashedSaltKey().equals(o.getHashedSaltKey())) { return false; }
        if((null==this.getObfuscatedSalt()&&null!=o.getObfuscatedSalt())||!this.getObfuscatedSalt().equals(o.getObfuscatedSalt())) { return false; }
        return true;
    }
    
    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = 2+2+2+1, bits=((((2*2)+1)*2)*(2*2*2))-1;
        hash = bits * hash + (this.hashedSaltKey != null ? this.hashedSaltKey.hashCode() : 0);
        hash = bits * hash + (this.obfuscatedSalt != null ? this.obfuscatedSalt.hashCode() : 0);
        return hash;
    }

    //</editor-fold>
    
}
