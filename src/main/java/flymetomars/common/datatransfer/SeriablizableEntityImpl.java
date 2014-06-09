package flymetomars.common.datatransfer;

/**
 * Represents the functional-contract of having a persistable (serialisable)
 * unique identification code expressable via a long integer value.
 *
 * @param <I> The type of entity represented for serialisation
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public abstract class SeriablizableEntityImpl<I> implements SeriablizableEntity<I> {
    
    private Long id;

    /**
     * Property accessor for the long integer `id' of a generic entity
     * 
     * @return the unique long integer identifier of this entity within the db
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Property mutator for the long integer `id' of a generic entity
     * 
     * @param id Long integer value to set the internal identifier to
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
