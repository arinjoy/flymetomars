package flymetomars.business.services;

/**
 * Service Interface Definition for processing existing user authentication
 * 
 * As an interface defined within the business.services package - this interface
 * exposes a (unauthenticated & unauthorized) guest-accessible API to the web.
 * 
 * @author Lawrence Colman
 */
public interface AuthService {
    
    /**
     * Internal state checking property accessor for determining whether this
     * instance is or is not performing automatic salt leveling upon passwords.
     * 
     * @return boolean flag indicating if instance is performing salt leveling
     */
    boolean isLevelling();
    
    /**
     * Authenticates a given person by username with a given password
     * 
     * @param username String username belonging to person
     * @param password String password belonging to person
     * @return boolean flag value indicating authentication success
     * @throws NullArgumentException thrown when either argument is a null ref
     * @throws IllegalStateException when leveling enabled and unable to persist
     * @throws NoSuchPersonExistsException thrown when person matching username
     *          cannot be found within the given persistance mechanism (DB)
     */
    boolean authenticate(String username, String password) throws NoSuchPersonExistsException;

}
