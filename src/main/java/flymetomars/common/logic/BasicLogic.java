package flymetomars.common.logic;

/**
 * Genericly-typed application interface for the execution of basic business
 * logic (BBL) against a domain model via a loosely-coupled data transfer object
 * 
 * @author Lawrence Colman
 * @param <D> Data Transfer Object (DTO) datatype of model to be checked
 */
public interface BasicLogic<D> {
    
    /**
     * Performs the application of basic business rules (bounds checking)
     * against a given data transfer object (DTO) of the `modelName' type
     * 
     * @param modelName Name of domain model type being checked against rules
     * @param fieldName String name of field with new value to be validated
     * @param dtoWithNewValue DTO object encapsulating new field value
     * @throws NullArgumentException thrown when any of the arguments passed to
     *          the method are null references for their respective types.
     * @throws ClassNotFoundException thrown when the supplied `modelName' does
     *          not relate to a registered validatable domain model class name
     */
    void applyRules(String modelName, String fieldName, D dtoWithNewValue);
    
}
