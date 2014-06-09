package flymetomars.dataaccess;

import flymetomars.common.NullArgumentException;

/**
 * Responsible for signifying that an operation that only deals with entity
 * objects that have been previously saved to a data-store (such as a database)
 * has received a entity object that has not yet been persisted to the data-store
 *
 * @author Lawrence Colman
 */
public class UnserialisedEntityException extends Exception {

    private String entity;

    /**
     * Thrown to indicate when an entity object has not been serialised but referenced in a DAO operation
     * 
     * @param entity the String object of the entity name
     * @param operation the String object to indicate the operation being performed
     * @param innerException an inner Exception to be furthered from this UnserialisedEntityException
     * @throws NullArgumentException if the entity name itself is null
     */
    public UnserialisedEntityException(String entity, String operation, Exception innerException) {
        this(entity + " entity supplied to " + (null==operation?"NULL":operation) + " was not a persisted instance!  Inner Error: [" + (null==innerException?"NULL":innerException.getMessage()) + ']');
        this.initCause(innerException);
        if(null==entity) {
            throw new NullArgumentException("entity value for UnserialisedEntityException cannot be null");
        }
        this.entity = entity;
    }

    /**
     * Default Exception constructor just taking message
     * 
     * @param message message for Throwable
     */
    public UnserialisedEntityException(String message) {
        super(message);
    }
    
    /**
     * Property accessor method for the entity name string passed in at construction
     * 
     * @return String name of entity as supplied to constructor
     */
    public String getEntity() {
        return this.entity;
    }
}
