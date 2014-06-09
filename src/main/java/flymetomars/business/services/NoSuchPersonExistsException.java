package flymetomars.business.services;

/**
 * Basic custom exception for the loose-coupling of clients to business services
 * 
 * @author Lawrence Colman
 */
public class NoSuchPersonExistsException extends AbstractExistentialPersonException {

    /**
     * default constructor - for exception compatibility
     */
    public NoSuchPersonExistsException() {
        this(null,null);
    }
    
    /**
     * Primary public constructor accepting either of expected username, or email.
     * 
     * @param expectedUserName String username found to be in conflict, or null
     * @param expectedEmail String email found to be in conflict, or null
     */
    public NoSuchPersonExistsException(String expectedUserName, String expectedEmail) {
        super("Person not found attempt (".concat(
            null==expectedUserName?"Username=".concat(expectedUserName):
            null==expectedEmail?"NO DATA":"E-mail=".concat(expectedEmail)).concat(
        ") confliced with existing Person model."),null);
    }

}
