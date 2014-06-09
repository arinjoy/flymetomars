package flymetomars.common.datatransfer;

/**
 * Represents the functional-contract of having a persistable (serialisable)
 * unique identification code expressable via a long integer value.
 * 
 * @param <T> The type of entity represented for serialisation
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Apoorva Singh
 */
public interface SeriablizableEntity<T> extends Marshalable {

    /**
     * Property accessor for the long integer `id' of a generic entity
     * 
     * @return the unique long integer identifier of this entity within the db
     */
    Long getId();
    
    /**
     * Property mutator for the long integer `id' of a generic entity
     * 
     * @param id Long integer value to set the internal identifier to
     */
    void setId(Long id);
    
}
