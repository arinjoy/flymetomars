package flymetomars.dataaccess;

/**
 * The DAO ancestor interface for all model classes that implement the SeriablizableEntity interface.
 * 
 * @author Lawrence Colman
 */
public interface EntityDAO<T> extends DAO<T> {
    
    /**
     * Loat method to retrieve a entity by a given long integer identifier
     * 
     * @param id Long integer identifier of a given "T" DTO object from DAO
     * @return A SeriablizableEntity object of type "T" with the identifier `id'
     */
    T load(Long id);
    
}
