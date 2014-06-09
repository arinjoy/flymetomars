package flymetomars.business.services;

import flymetomars.business.model.Person;

/**
 * Grandfathered archetypal basic custom exception for loose implementation
 * coupling withing the services package of the business layer for instances
 * 
 * Note:
 *          Decendants of this checked exception deal with existential Person
 *          model issues - such as existence when unexpected, or non-existence
 * 
 * @author Lawrence Colman
 */
public abstract class AbstractExistentialPersonException extends Exception {

    private final Person personConcerned;
    
    /**
     * Default constructor taking no arguments
     */
    public AbstractExistentialPersonException() {
        super();
        this.personConcerned=null;
    }
    
    /**
     * Primary public constructor accepting message and PErson model object cause
     * 
     * @param msg String detailing the reason(s) behind the throwing of exception
     * @param cause a Person domain model object to attribute with causing error
     */
    public AbstractExistentialPersonException(String msg, Person cause) {
        super(msg);
        this.personConcerned=cause;
    }

    /**
     * Property accessor to retrieve the Person model object which caused issue
     * 
     * @return either null (if not passed to constructor) or a Person model object
     */
    public Person getPersonConcerned() {
        return personConcerned;
    }
    
}
