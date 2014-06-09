package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Salt;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SaltEntity is responsible for representing strongly-typed and
 * strongly-coupled `Salt' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class SaltEntity {

    private String hashedSaltKey;
    private String obfuscatedSalt;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    /**
     * Default constructor
     */
    public SaltEntity() {}

    /**
     * Salt DTO to Entity class conversion utility method
     * 
     * @param salt Salt data transfer object (DTO) to convert to Entity
     * @return A Salt Entity instance representing the DTO passed to method
     */
    public static SaltEntity fromDTO(Salt salt) {
        if(null==salt) { return (SaltEntity)null; }
        SaltEntity result = new SaltEntity();
        result.setHashedSaltKey(salt.getHashedSaltKey());
        result.setObfuscatedSalt(salt.getObfuscatedSalt());
        return result;
    }

    /**
     * Salt Entity transfer encoding conversion utility method
     * 
     * @return A Salt data transfer object (DTO) representing this Entity
     */
    public Salt toDTO() {
        Salt result;
        try {
            result=this.dtoFactory.createDTO(Salt.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaltEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setHashedSaltKey(this.getHashedSaltKey());
        result.setObfuscatedSalt(this.getObfuscatedSalt());
        return result;
    }
    
    /**
     * Convenience method to populate this SaltEntity with values from another
     * 
     * @param salt The alternate SaltEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public SaltEntity copyValues(SaltEntity salt) {
        this.setHashedSaltKey(salt.getHashedSaltKey());
        this.setObfuscatedSalt(salt.getObfuscatedSalt());
        return this;
    }

    public String getObfuscatedSalt() {
        return obfuscatedSalt;
    }
    
    public void setObfuscatedSalt(String obfuscatedSalt) {
        this.obfuscatedSalt = obfuscatedSalt;
    }
    
    public String getHashedSaltKey() {
        return hashedSaltKey;
    }
    
    public void setHashedSaltKey(String hashedSaltKey) {
        this.hashedSaltKey = hashedSaltKey;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder(this.getClass().getCanonicalName());
        result.append("#[");
        result.append(this.getHashedSaltKey());
        result.append(']');
        return result.toString();
    }
    
}
