package flymetomars.business.handling;

import flymetomars.business.model.Person;

/**
 * Service Interface Definition for processing existing user change of password
 * 
 * As an interface defined within the business.services package - this interface
 * exposes a (unauthenticated & unauthorized) guest-accessible API to the web.
 * 
 * @author Lawrence Colman
 */
public interface PasswordService {

    /**
     * Complex business logic pertaining to the changing of a users password
     * 
     * @param person Person model object for whom to change the SaltedPassword of
     * @param oldPass String of the unencoded plain text current password value
     * @param newPass String containing the desired new password value for Person
     * @throws NullArgumentException thrown when any arguments are given as null
     * @throws IllegalArgumentException thrown when the supplied `oldPass' is
     *          not the actual password text value currently belonging to person
     * @throws InvalidPasswordException thrown when the supplied `newPass' String
     *          is not a valid password as per the simple business logic rules
     */
    void changePassword(Person person, String oldPass, String newPass);
    
}
