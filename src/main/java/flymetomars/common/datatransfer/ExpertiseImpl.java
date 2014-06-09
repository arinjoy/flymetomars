package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Expertise.ExpertiseLevel;

/**
 * A representative class for an Expertise belonging to a particular Person
 * 
 * @author Apoorva Singh
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * 
 * JavaDoc copied from interface.
 */
public class ExpertiseImpl extends SeriablizableEntityImpl<Expertise> implements EqualableStructure<Expertise>, Expertise {
    
    private String name;
    private String description;
    private ExpertiseLevel level;
    private Person owner;
    
    /**
     * default constructor for Expertise DTO
     */
    public ExpertiseImpl(){}
    
    /**
     * Property accessor for the `name' of an Expertise
     * 
     * @return String name of the Expertise
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     *Property mutator for the `name' of an Expertise
     * 
     * @param name String name of the Expertise
     * @throws NullArgumentException thrown when given `name' is a null reference
     */
    @Override
    public void setName(String name) {
        if (null == name) {
            throw new NullArgumentException("Expertise name cannot be null");
        }
        this.name = name;
    }
    
    /**
     * Property accessor for the `description' of an Expertise
     * 
     * @return String description of the Expertise
     */
    @Override
    public String getDescription() {
        return description;
    }
   
    /**
     * Property mutator for the `description' of an Expertise
     * 
     * @param description String description of the Expertise
     * @throws NullArgumentException thrown when  `description' is a null ref
     */
    @Override
    public void setDescription(String description) {
        if(null==description) {
            throw new NullArgumentException("Description cannot be null");
        }
        this.description = description;
    }
    
    /**
     * Property accessor for the `level' of an Expertise taken from defined enum
     * 
     * @return ExpertiseLevel enumeration value for the level of this Expertise
     */
    @Override
    public ExpertiseLevel getLevel() {
        return level;
    }

    /**
     * Property mutator for the `level' of an Expertise taken from defined enum
     * 
     * @param level ExpertiseLevel enum value to set the level of this Expertise
     * @throws NullArgumentException thrown when `level' is a null reference
     */
    @Override
    public void setLevel(ExpertiseLevel level) {
        if (null == level) {
            throw new NullArgumentException("Expertise level field cannot be null");
        }
        this.level = level;
    }
    
    /**
     * Property mutator for the `HeldBy' Person property of an Expertise
     * 
     * @param p Person DTO to set as the 'holder' of this Expertise
     */
    @Override
    public void setHeldBy(Person p) {
        this.owner=p;
    }
    
    /**
     * Property accessor for the `HeldBy' Person property of an Expertise
     * 
     * @return Person DTO representing the "holder" of this particular Expertise
     */
    @Override
    public Person getHeldBy() {
        return owner;
    }
    
    /**
     * 
     * 
     * @param exp
     * @return 
     */
    @Override
    public boolean equalsDTO(Expertise exp) {
        if ((null==this.name&&null!=exp.getName())||!this.name.equals(exp.getName())) { return false; }
        if ((null==this.description&&null!=exp.getDescription())||!this.description.equals(exp.getDescription())) { return false; }
        if ((null==this.level&&null!=exp.getLevel())||!this.level.equals(exp.getLevel())) { return false; }
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
        int hash = 1+(2*2), bit=(((2*2)+2)*(((2*2)+1)*2))-1;
        hash = bit * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = bit * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = bit * hash + (this.level != null ? this.level.hashCode() : 0);
        return hash;
    }

    /**
     * Standard Java Object.equals(Object) method signature override for sorting
     * 
     * @param obj Object to be checked for equality with this object
     * @return boolean value indicating if the representations contained within
     * this object and obj are effectively equivalent (thus equal for sorting).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.equalsDTO((ExpertiseImpl)obj);
    }
    
}
