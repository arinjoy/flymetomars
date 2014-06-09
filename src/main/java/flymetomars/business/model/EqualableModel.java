package flymetomars.business.model;

/**
 * Represents the functional-contract of having the ability to be type-safely
 * compared to instances of a particular classification.  This differs from the
 * compareTo method and associated interface - in that this interface represents
 * a boolean compare via an equalsModel() method rather than a sortable int result.
 * 
 * @author Lawrence Colman
 */
public abstract class EqualableModel<E extends EqualableModel> {
    
    /**
     * Compares for simple boolean equality between this object and another.
     * 
     * @param struct the object to compare this against for equality
     * @return boolean value indicating if the comparison yielded no differences
     */
    public abstract boolean equalsModel(E struct);
    
}
