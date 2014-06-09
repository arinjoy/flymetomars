package flymetomars.business.services;

import flymetomars.business.model.Person;

/**
 * Basic custom exception for the loose-coupling of clients to business services
 * 
 * @author Lawrence Colman
 */
public class PersonAlreadyExistsException extends AbstractExistentialPersonException {

    /**
     * default constructor - for exception compatibility
     */
    public PersonAlreadyExistsException() {
        this(null,null,null);
    }
    
    /**
     * Primary public constructor accepting either of conflicting username, or
     * conflicting email address - as well as person model which is conflicting.
     * 
     * @param conflictingUserName String username found to be in conflict, or null
     * @param conflictingEmail String email found to be in conflict, or null
     * @param existingModel Person model object that already responds to unique
     */
    public PersonAlreadyExistsException(String conflictingUserName, String conflictingEmail, Person existingModel) {
        super("Person registration attempt (".concat(
            null==conflictingUserName?"Username=".concat(conflictingUserName):
            null==conflictingEmail?"NO DATA":"E-mail=".concat(conflictingEmail)).concat(
        ") confliced with existing Person model."),existingModel);
    }

}
