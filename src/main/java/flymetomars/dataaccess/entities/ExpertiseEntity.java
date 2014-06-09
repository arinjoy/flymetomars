package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ExpertiseEntity is responsible for representing strongly-typed and
 * strongly-coupled `Expertise' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class ExpertiseEntity {
    
    private String name;
    private String description;
    private Expertise.ExpertiseLevel level;
    private PersonEntity person;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    /**
     * Default constructor
     */
    public ExpertiseEntity() {}
    
    /**
     * Expertise DTO to Entity class conversion utility method
     * 
     * @param exp Expertise data transfer object (DTO) to convert to Entity
     * @return An Expertise Entity instance representing the DTO passed to method
     */
    public static ExpertiseEntity fromDTO(Expertise exp) {
        if(null==exp) { return (ExpertiseEntity)null; }
        ExpertiseEntity result = new ExpertiseEntity();
        result.setName(exp.getName());
        result.setDescription(exp.getDescription());
        result.setLevel(exp.getLevel());
        result.setHeldBy(PersonEntity.fromDTO(exp.getHeldBy()));
        result.setId(exp.getId());
        return result;
    }
    
    /**
     * Expertise Entity transfer encoding conversion utility method
     * 
     * @return An Expertise data transfer object (DTO) representing this Entity
     */
    public Expertise toDTO() { return this.toDTO(true); }
    public Expertise toDTO(boolean skipHeldBy) {
        Expertise result;
        try {
            result=this.dtoFactory.createDTO(Expertise.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ExpertiseEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setName(this.getName());
        result.setDescription(this.getDescription());
        result.setLevel(this.getLevel());
        if(!skipHeldBy) { result.setHeldBy(this.getHeldBy()==null?null:this.getHeldBy().toDTO()); }
        result.setId(this.getId());
        return result;
    }
    
    /**
     * Convenience method to populate this ExpertiseEntity with values from another
     * 
     * @param salt The alternate ExpertiseEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public ExpertiseEntity copyValues(ExpertiseEntity exp) {
        this.setName(exp.getName());
        this.setDescription(exp.getDescription());
        this.setLevel(exp.getLevel());
        this.setHeldBy(exp.getHeldBy());
        this.setId(exp.getId());
        return this;
    }
   
    public String getName(){
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
   
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Expertise.ExpertiseLevel getLevel() {
        return level;
    }

    public void setLevel(Expertise.ExpertiseLevel level) {
        this.level = level;
    }

    public void setHeldBy(PersonEntity owner) {
        this.person=owner;
    }

    public PersonEntity getHeldBy() {
        return person;
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
        result.append("-(");
        result.append(this.getName());
        result.append("/");
        result.append(this.getLevel().toString());
        result.append(")");
        return result.toString();
    }
    
}
