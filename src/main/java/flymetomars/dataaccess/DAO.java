package flymetomars.dataaccess;

import java.util.List;

/**
 * Top-level data access object interface for loosely-coupled DAO definitions
 * 
 * @param <T> Genericised type reference for the data transfer object (DTO) type
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public interface DAO<T> {

    /**
     * Retrieves a sequence of all recorded DTO objects of a given domain type
     * 
     * @return List of "T" domain model entity objects encoded into loose DTOs
     */
    List<T> getAll();

    /**
     * Persists a given DTO through the data access layer to underlaying store
     * 
     * @param object DTO object of type "T" to be persisted to the store via DAL
     */
    void save(T object);

    /**
     * Persists changes made to an existent domain object via altered DTO object
     * 
     * @param object DTO object representing changes made to the domain instance
     */
    void update(T object);

    /**
     * Performs a consistent persist operation regardless of object state in store
     * 
     * @param object DTO representing object that is to be either saved or updated
     */
    void saveOrUpdate(T object);

    /**
     * Purges (deletes) a given domain model article from the backing store
     * 
     * @param object DTO representing the persisted object to purge from store
     * @return the DTO object of type "T" that was passed in to the delete method
     */
    T delete(T object);

}
