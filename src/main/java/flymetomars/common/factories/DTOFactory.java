package flymetomars.common.factories;

/**
 * Factory pattern interface for the type-safe and implementation-independent
 * loosely-coupled instantiation of data transfer object (DTO) classes.
 * 
 * @author Lawrence Colman
 */
public interface DTOFactory {
    
    /**
     * Constructs and returns an instance of a specified interface for domain.
     * 
     * @param <T> type parameter for type-safely referencing desired interface
     * @param domainInterface the interface of which an instance is desired
     * @return returns an instance of an implementor of the requested interface
     * @throws ClassNotFoundException thrown when the requested interface is not
     *          mapped within the backing store of this factory implementation.
     */
    <T> T createDTO(Class<T> domainInterface) throws ClassNotFoundException;
}
