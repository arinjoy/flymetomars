package flymetomars.business.services;

import flymetomars.business.core.DependencyException;
import flymetomars.business.model.Person;

/**
 * Service Interface Definition for processing new user registrations
 * 
 * As an interface defined within the business.services package - this interface
 * defines a (unauthenticated & unauthorized) guest-accessible API to the web.
 * 
 * @author Lawrence Colman
 */
public interface RegisterService {
    
    /**
     * Registers and saves a given Person within the persistance layer as per
     * given argument values for the attributes of a Person model object.
     * 
     * @param desiredUsername String unique userName to use for the Person object
     * @param suppliedEmail String E-mail address to configure Person as having
     * @param givenPassword String password text to configure password to be
     * @param confirmPassword String password value to use as confirmation
     * @param firstName String firstname to configure Person as having
     * @param lastName String surname to configure Person as having
     * @return A fully-persisted Person model instance reflecting given values
     * @throws PersonAlreadyExistsException throws when username or email is taken
     * @throws NullArgumentException thrown when any argument is given as null
     * @throws StringTooShortException thrown when some argument is too short
     * @throws StringTooLongException thrown when some argument is too long
     * @throws InvalidPasswordException thrown when given password is illegal
     * @throws IllegalArgumentException thrown when the `givenPassword' and the
     *              `confirmPassword' argument values do not match eachother
     * @throws IllegalStateException thrown when an internal validator error occurs
     * @throws DependencyException thrown when an internal persistance error occurs
     */
    Person register(
        String desiredUsername,
        String suppliedEmail,
        String givenPassword,
        String confirmPassword,
        String firstName,
        String lastName
    ) throws PersonAlreadyExistsException, DependencyException;

}
