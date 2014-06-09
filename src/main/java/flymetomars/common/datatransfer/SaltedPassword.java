package flymetomars.common.datatransfer;

/**
 * A representative class for the SaltedPassword belonging to a Person
 * 
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 */
public interface SaltedPassword extends SeriablizableEntity<SaltedPassword> {
    
    /**
     * Property accessor for the `digest' of a SaltedPassword
     * 
     * @return String representation of the hash digest value for this password
     */
    String getDigest();
    
    /**
     * Property mutator for the `digest' of a SaltedPassword
     * 
     * @param passwordHash String representation of hash digest value password
     */
    void setDigest(String passwordHash);
    
    /**
     * Property accessor for the `saltId' of a SaltedPassword
     * 
     * @return String hash value of salt string identifier referenced inside
     */
    String getSaltId();
    
    /**
     * Property mutator for the `saltId' of a SaltedPassword
     * 
     * @param saltId String hash value of salt string identifier to be used
     */
    void setSaltId(String saltId);
    
}
