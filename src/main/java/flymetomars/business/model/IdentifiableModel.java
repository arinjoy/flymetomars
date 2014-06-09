package flymetomars.business.model;

/**
 * Represents the behaviour of having an unique identifier long
 * integer value property for identification and serialisation.
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public abstract class IdentifiableModel<I extends EqualableModel> extends EqualableModel<I> {
    
    private Long id;

    /**
     * Property accessor method for retrieving the id
     * 
     * @return The long integer uniquely identifier this particular model object
     */
    public final Long getId() {
        return id;
    }

    /**
     * Property mutator method for altering the id
     * 
     * @param id Long integer for which to set the internal unique id to
     */
    public final void setId(Long id) {
        this.id = id;
    }
}
