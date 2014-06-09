package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SaltedPasswordEntity is responsible for representing strongly-typed and
 * strongly-coupled `SaltedPassword' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class SaltedPasswordEntity {

    private String digest;
    private String saltId;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    /**
     * Default constructor
     */
    public SaltedPasswordEntity() {}

    /**
     * SaltedPassword DTO to Entity class conversion utility method
     * 
     * @param pass SaltedPassword data transfer object (DTO) to convert to entity
     * @return SaltedPassword Entity instance representing the DTO passed to in
     */
    public static SaltedPasswordEntity fromDTO(SaltedPassword pass) {
        if(null==pass) { return (SaltedPasswordEntity)null; }
        SaltedPasswordEntity result = new SaltedPasswordEntity();
	result.setSaltId(pass.getSaltId());
        result.setDigest(pass.getDigest());
        result.setId(pass.getId());
        return result;
    }

    /**
     * SaltedPassword Entity transfer encoding conversion utility method
     * 
     * @return A SaltedPassword data transfer object (DTO) representing this Entity
     */
    public SaltedPassword toDTO() {
        SaltedPassword result;
        try {
            result=this.dtoFactory.createDTO(SaltedPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaltedPasswordEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setSaltId(this.getSaltId());
        result.setDigest(this.getDigest());
        result.setId(this.getId());
        return result;
    }
    
    /**
     * Convenience method to populate this SaltedPasswordEntity with values from another
     * 
     * @param pass The alternate SaltedPasswordEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public SaltedPasswordEntity copyValues(SaltedPasswordEntity pass) {
        this.setDigest(pass.getDigest());
        this.setId(pass.getId());
        return this;
    }

    public String getDigest() {
        return digest;
    }
    
    public void setDigest(String passwordHash) {
        this.digest = passwordHash;
    }

    public String getSaltId() {
        return saltId;
    }
    
    public void setSaltId(String saltKey) {
        this.saltId = saltKey;
    }
    
    /**
     * Property accessor for Entity objects encapsulating domain objects with ids
     * 
     * @return the unique Long Integer identifier code for this Entity instance
     */
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder(this.getClass().getCanonicalName());
        result.append('#');
        result.append(this.getId());
        result.append("-[");
        result.append(this.getDigest());
        result.append("]");
        return result.toString();
    }
    
}
