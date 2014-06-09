package flymetomars.business.services;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.PasswordHandler;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.NullArgumentException;

/**
 * Service Interface Implementation for processing existing user authentication
 * 
 * As a component defined within the business.services package - this class
 * exposes a (unauthenticated & unauthorized) guest-accessible API to the web.
 * 
 * @author Lawrence Colman
 * @author Apoorva Singh
 */
public class AuthServiceImpl implements AuthService {
    
    private EntityLoader loader;
    private EntitySaver saver;
    private EntityUpdater updater;
    private PasswordHandler handle;
    
    /**
     * Basic public constructor - dependency only upon core loading operator
     * 
     * @param loader the core package entity-loader utility class instance
     * @throws NullArgumentException thrown when loader is passed in as null
     * 
     * Note:
     *          A call to this constructor is semantically equivalent to doing
     *          AuthService(loader, null, null, null) - resulting in the leveling
     *          functionality of this class being unavailiabile for usage.
     */
    public AuthServiceImpl(EntityLoader loader) {
        this(loader, null, null, null);
    }
    
    /**
     * Full public constructor - enabling the auto-magical performance of salt
     * leveling at authentication time for each Person model's SaltedPassword.
     * 
     * @param loader the core package entity-loader utility class instance
     * @param saver core package entity-saver utility class instance, or null
     * @param updater core package entity-updater utility class instance, or null
     * @param handler handling package password-handler dependency, or null
     * @throws NullArgumentException thrown when loader is passed in as null, or
     *          if one the saver and handler are null but not the other
     * 
     * Note:
     *          Each of the saver, updater  and handler arguments may be given to
     *          this constructor as null references - which will result in the
     *          auto-disabling of the salt-leveling functionality for auths.
     *          If both a valid saver and handler are given, then the instance
     *          initialised by this constructor will automatically perform
     *          salt leveling upon each successful call to "authenticate".
     */
    public AuthServiceImpl(EntityLoader loader, EntitySaver saver, EntityUpdater updater, PasswordHandler handler) {
        if(null==loader) {
            throw new NullArgumentException("Required dependency injected as null");
        }
        boolean anyNull=(null==saver || null==updater || null==handler);
        boolean oneNot=(null!=saver || null!=updater || null!=handler);
        eif((anyNull && oneNot),
            new NullArgumentException(
                "Inconsistency in dependency injections - "+
                    "please pass last 3 arguments as all either null or instances"
            )
        );
        if(null==saver && null==handler && null==updater) {
            this.autoLeveling=false;
        } else {
            this.saver=saver;
            this.updater=updater;
            this.handle=handler;
        }
        this.loader = loader;
    } private static void eif(boolean c, RuntimeException e) {if(c){throw e;}}
    
    /**
     * Sheltered (package-private constructor) - for self-injection of handler
     * 
     * @param loader the core package entity-loader utility class instance
     * @param saver the core package entity-saver utility class instance
     * @param updater core package entity-updater utility class instance
     * @throws NullArgumentException thrown when either arg is passed in as null
     */
    /*package-private*/ AuthServiceImpl(EntityLoader loader, EntitySaver saver, EntityUpdater updater) {
        this(loader, saver, updater, new PasswordHandler(loader));
    }
    
    private boolean autoLeveling=true;
    
    /**
     * Internal state checking property accessor for determining whether this
     * instance is or is not performing automatic salt leveling upon passwords.
     * 
     * @return boolean flag indicating if instance is performing salt leveling
     */
    @Override
    public boolean isLevelling() {
        return this.autoLeveling;
    }
    
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
    @Override
    public boolean authenticate(String username, String password) throws NoSuchPersonExistsException {
        if(null==username) {
            throw new NullArgumentException("Cannot authenticate a null username");
        }
        if(null==password) {
            throw new NullArgumentException("Cannot authenticate a null password");
        }
        Person model=loader.loadPersonByUserName(username);
        if(null==model) {
            throw new NoSuchPersonExistsException(username,null);
        }
        SaltedPassword pass=model.getPassword();
        if(null==pass) { return false; }
        if(pass.isPassword(password)) { //password is correct
            if(autoLeveling) {
                this.handle.levelSalt(model, password);
                pass=model.getPassword();
                try { this.saver.saveSalt(pass.getSalt()); }
                catch (DependencyException ex) {}  //already saved
                try { this.updater.updateSaltedPassword(pass); }
                catch (DependencyException de) {
                    throw new IllegalStateException("Error persisting leveled password", de);
                }
            }
            return true;
        } else { return false; }
    }

}
