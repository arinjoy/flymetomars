package flymetomars.business.services;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.handling.PasswordHandler;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.FMTMValidationException;
import flymetomars.common.InvalidPasswordException;
import flymetomars.common.NullArgumentException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service Interface Implementation for processing new user registrations
 * 
 * As a component defined within the business.services package - this class
 * exposes a (unauthenticated & unauthorized) guest-accessible API to the web.
 * 
 * @author Lawrence Colman
 */
public class RegisterServiceImpl implements RegisterService {
    
    private EntityLoader loader;
    private EntitySaver saver;
    private EntityFactory factory;
    private PasswordHandler handler;
    
    /**
     * Public constructor for service
     * 
     * @param loader The core package entity-loader dependency to be injected
     * @param saver The core package entity-saver dependency to be injected
     * @param factory The core package entity-factory dependency to be injected
     * @param handler Handling package PAsswordHandler dependency to be injected
     */
    public RegisterServiceImpl(
        EntityLoader loader,
        EntitySaver saver,
        EntityFactory factory,
        PasswordHandler handler
    ) {
        this.loader=loader;
        this.saver=saver;
        this.factory=factory;
        this.handler=handler;
    }
    
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
    @Override
    public Person register(
        String desiredUsername,
        String suppliedEmail,
        String givenPassword,
        String confirmPassword,
        String firstName,
        String lastName
    ) throws PersonAlreadyExistsException, DependencyException {
        //begin by validating inputs
        eif((null==confirmPassword),
            new NullArgumentException("Password confirmation cannot be null")
        );
        //check that the givenPassword matches it's confirmation value
        eif((!confirmPassword.equals(givenPassword)),
            new IllegalArgumentException("Password confirmation mismatch")
        );
        //check that username is not in conflict
        Person conflict=this.loader.loadPersonByUserName(desiredUsername);
        if(null!=conflict) {
            throw new PersonAlreadyExistsException(desiredUsername,null,conflict);
        }
        //check that email address is not in conflict
        conflict=this.loader.loadPersonByEmail(suppliedEmail);
        if(null!=conflict) {
            throw new PersonAlreadyExistsException(null,suppliedEmail,conflict);
        }
        //retrieve or create (auto-magic) a Salt for Person
        Salt salt=handler.randomSalt();
        //configure (and validate) given password
        SaltedPassword pass=this.factory.createSaltedPassword(salt);
        FMTMValidationException e=null;
        try {  //may throw any one of a number of exceptions
            pass.setPassword(givenPassword);
        } catch(FMTMValidationException ve) {e=ve;}
        catch (IllegalArgumentException iae) {
            throw new InvalidPasswordException("Illegal Argument for Password: ",iae);
        } if(null!=e) { throw e; }
        //create the Person model
        Person result=this.factory.createPerson(pass);
        //configure model with argument data
        result.setUserName(desiredUsername);
        result.setEmail(suppliedEmail);
        result.setFirstName(firstName);
        result.setLastName(lastName);
        //begin saving models (in reverse order of dependency)
        try { this.saver.saveSalt(salt); } catch (DependencyException ex) {}
        try {
            this.saver.saveSaltedPassword(pass);
        } catch (DependencyException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.WARNING, "Error registering Parson password", ex);
            throw ex;
        }
        try {
            this.saver.savePerson(result);
        } catch (DependencyException ex) {
            Logger.getLogger(RegisterService.class.getName()).log(Level.WARNING, "Error registering Person", ex);
            throw ex;
        }
        //finally return model
        return result;
    } private static void eif(boolean c, RuntimeException e) {if(c){throw e;}}

}
