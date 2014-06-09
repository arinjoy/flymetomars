package flymetomars.common.datatransfer;

/**
 * A representative class for an Expertise belonging to a particular Person
 * 
 * @author Apoorva Singh
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface Expertise extends SeriablizableEntity<Expertise> {
    
    /**
     * Public enumeration definition for the `level' of an Expertise
     */
    public enum ExpertiseLevel {

        AMATURE("amature"),
        INTRODUCTORY("introductory"),
        PROFESSIONAL("professional"),
        SEMINAL("seminal"),
        EMINENT("eminent");
        private String name;

        private ExpertiseLevel(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
    
    /**
     * Property accessor for the `name' of an Expertise
     * 
     * @return String name of the Expertise
     */
    String getName();
    
    /**
     *Property mutator for the `name' of an Expertise
     * 
     * @param name String name of the Expertise
     */
    void setName(String name);
    
    /**
     * Property accessor for the `description' of an Expertise
     * 
     * @return String description of the Expertise
     */
    String getDescription();
    
    /**
     * Property mutator for the `description' of an Expertise
     * 
     * @param description String description of the Expertise
     */
    void setDescription(String description);
    
    /**
     * Property accessor for the `level' of an Expertise taken from defined enum
     * 
     * @return ExpertiseLevel enumeration value for the level of this Expertise
     */
    Expertise.ExpertiseLevel getLevel();
    
    /**
     * Property mutator for the `level' of an Expertise taken from defined enum
     * 
     * @param level ExpertiseLevel enum value to set the level of this Expertise
     */
    void setLevel(ExpertiseLevel level);
    
    /**
     * Property mutator for the `HeldBy' Person property of an Expertise
     * 
     * @param p Person DTO to set as the 'holder' of this Expertise
     */
    void setHeldBy(Person p);
    
    /**
     * Property accessor for the `HeldBy' Person property of an Expertise
     * 
     * @return Person DTO representing the "holder" of this particular Expertise
     */
    Person getHeldBy();
    
}
