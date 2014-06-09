package flymetomars.common.datatransfer;

/**
 * A representative class for Salt values used within SaltedPassword objects
 * 
 * @author Lawrence Colman
 * @author Apoorva Singh
 */
public interface Salt extends Marshalable {

    /**
     * Property accessor for the `ObfuscatedSalt' value of a Salt
     * 
     * @return String encoded inner salting string used for hash salting
     */
    String getObfuscatedSalt();
    
    /**
     * Property mutator for the `ObfuscatedSalt' value of a Salt
     * 
     * @param obfuscatedSalt String to be stored for use in hash salting
     */
    void setObfuscatedSalt(String obfuscatedSalt);
    
    /**
     * Property accessor for the `HashedSaltKey' id of Salt instance
     * 
     * @return String hash value of specified salt string identifier
     */
    String getHashedSaltKey();
    
    /**
     * Property mutator for the `HashedSaltKey' id of Salt instance
     * 
     * @param hashedSaltKey String hash value of desired salt string identifier
     */
    void setHashedSaltKey(String hashedSaltKey);
    
}
