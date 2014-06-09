package flymetomars.business.handling;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.FMTMValidationException;
import flymetomars.common.InvalidPasswordException;
import flymetomars.common.NullArgumentException;
import java.util.ArrayList;
import java.util.List;

/**
 * PasswordHandler Complex Business Logic implementation Class for Invitations
 * 
 * As a handling package a member  this class implements complex business logic
 * 
 * @author Lawrence Colman
 * 
 * Note:
 *         When completed the methods of this business logic implementation will
 *         leave the supplied model objects in a consistent (i.e. non-transient)
 *         state - however they will contain non-persisted changes.
 *         To make the requisite durable changes a following call to one of the
 *         appropriate "core" entity operators (i.e. EntitySaver, EntityUpdater,
 *         etc) will be required in order to persist the changes made to storage
 */
public class PasswordHandler implements PasswordService {
    
    /**
     * Configurable business threshold for determining the security maximum
     * number of SaltedPassword domain model objects that may share a Salt
     */
    private static final int MAX_SHARING_SAME_SALT=5;
    
    private EntityLoader loader;
    
    /**
     * Public constructor accepting dependency injected SaltedPasswordDAO instance
     * 
     * @param load EntityLoader instance to take as injected dependency
     * 
     * Note:
     *          The `load' argument may be safely passed as null provided that
     *          the "levelSalt" and "randomSalt" methods are not called.
     */
    public PasswordHandler(EntityLoader load) {
        this.loader=load;
    }

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
    @Override
    public void changePassword(Person person, String oldPass, String newPass) {
        if(null==person) {
            throw new NullArgumentException("Cannot change password of null Person");
        }
        if(null==oldPass) {
            throw new NullArgumentException("Person cannot be authenticated to null");
        }
        if(null==newPass) {
            throw new NullArgumentException("Cannot change password of Person to null");
        }
        if(null==person.getPassword().getSalt()) {
            throw new IllegalArgumentException(new NullArgumentException(
                "Cannot authenticate Person against a SaltedPassword without Salt"
            ));
        }
        if(!person.isPassword(oldPass)) {
            throw new IllegalArgumentException("Incorrect password supplied!");
        }
        try {
            person.getPassword().setPassword(newPass);
        } catch (FMTMValidationException e) {
            throw new InvalidPasswordException("Bad new password value given",e);
        }
    }
    
    /**
     * Adjusts the Salt value is use for a particular given Person domain model 
     * objects SaltedPAssword depending upon the *level* of use of the Salt
     * value contained within it.  Salts that are being used by at least
     * MAX_SHARING_SAME_SALT number of SaltedPasswords will be changed (via the
     * changeSalt method of this handler) to randomly selected Salt values that
     * are in use by at least one less than the MAX_SHARING_SAME_SALT threshold.
     * 
     * @param person Person model object for whom to perform the salt leveling of
     * @param currentPassword String of the current plain text password value
     * @return flag value indicating whether the associated Salt was changed
     * @throws NullArgumentException thrown when any arguments are given as null
     * @throws IllegalArgumentException thrown when currentPassword is incorrect
     * @throws UnsupportedOperationException when backing store is unavailiable
     */
    public boolean levelSalt(Person person, String currentPassword) {
        if(null==person) {
            throw new NullArgumentException("Cannot change password of null Person");
        }
        if(null==currentPassword) {
            throw new NullArgumentException("Person cannot be authenticated to null");
        }
        if(null==person.getPassword().getSalt()) {
            throw new IllegalArgumentException(new NullArgumentException(
                "Cannot authenticate Person against a SaltedPassword without Salt"
            ));
        }
        if(!person.isPassword(currentPassword)) {
            throw new IllegalArgumentException("Incorrect password supplied!");
        }
        Salt salt = person.getPassword().getSalt();
        List<SaltedPassword> spl=loader.loadAllSaltedPasswordsSharingSalt(salt);
        if(spl.size() > MAX_SHARING_SAME_SALT) {
            this.changeSalt(person,currentPassword,this.randomSalt());
            return true;
        }
        return false;
    }
    
    /**
     * Implements the complex business logic require to change the Salt in use
     * for a given SaltedPAssword from one model instance to another, including
     * the non-trivial requirement of rehashing the digest of the SaltedPassword
     * 
     * @param person Person model object for whom to change the salt value for
     * @param currentPass String of the current plain text password value
     * @param newSalt The new Salt model object to make use of for given `person'
     * @throws NullArgumentException thrown when any arguments are given as null
     * @throws IllegalArgumentException thrown when currentPassword is incorrect
     * 
     * Note:  This method is NOT thread-safe
     */
    public void changeSalt(Person person, String currentPass, Salt newSalt) {
        if(null==person) {
            throw new NullArgumentException("Cannot change password of null Person");
        }
        if(null==currentPass) {
            throw new NullArgumentException("Person cannot be authenticated to null");
        }
        if(null==newSalt) {
            throw new NullArgumentException("Password cannot be changed to null Salt");
        }
        if(null==person.getPassword().getSalt()) {
            throw new IllegalArgumentException(new NullArgumentException(
                "Cannot authenticate Person against a SaltedPassword without Salt"
            ));
        }
        if(!person.isPassword(currentPass)) {
            throw new IllegalArgumentException("Incorrect password supplied!");
        }
        if(!newSalt.equalsModel(person.getPassword().getSalt())) {
            SaltedPassword pass=person.getPassword();
            pass.setSalt(newSalt);
            pass.setPassword(currentPass);
            person.setPassword(pass);
        }
    }
    
    /**
     * Returns a random Salt object, either loaded from the DAL or newly created
     * 
     * @return Salt model instance representing Salt object avaliable for use
     * @throws UnsupportedOperationException when backing store is unavaliable
     * 
     * Note:
     *          The Salt instance returned is guaranteed to (at the time of the
     *          calling of this method) be less than MAX_SHARING_SAME_SALT in
     *          terms of current usage from SaltedPasswords.  However, the Salt
     *          returned is NOT guaranteed to have been persisted to the DAL.
     *          Best practice would be to call a `SaveOrUpdate' DAO method upon
     *          the model object returned (marshaled via a DTO naturally).
     */
    public Salt randomSalt() {
        List<Salt> potential=new ArrayList<Salt>();
        for(Salt salt : this.loader.loadAllSalts()) {
            if(loader.loadAllSaltedPasswordsSharingSalt(salt).size() < MAX_SHARING_SAME_SALT) {
                potential.add(salt);
            }
        }
        if(potential.isEmpty()) {  //have to make a new Salt
            Salt result=new Salt();
            result.setPureSalt(this.getQuote(Math.random(),Math.random(),Math.random()));
            return result;
        }
        return potential.get((int)Math.round(Math.floor(Math.random()*potential.size())));
    }
    
    private String getQuote(double seed1, double seed2, double seed3) {
        String[] start=new String[] {
            "A noble spirit", "One in the hand",
            "They always did say", "You never can tell",
            "Once upon a time", "When I was young"
        };
        String[] middle=new String[] {
            "embiggins", "enhabbits", "enlightens",
            "enjoys", "enchants", "arouses", "allowes",
            "pleases", "posterizes", "forbids", "permits",
            "enhances", "destroys", "focuses", "when you",
            "that I", "for true love", "who will", "who won't",
            "who might", "who can't", "who should", "that they",
            "I used to", "we had to", "there was a", "is better"
        };
        String[] end=new String[] {
            "a magical castle", "the smallest man", "he who helps himself",
            "call home", "like best", "down in New Orleans", "big dragon"
        };
        StringBuilder result=new StringBuilder();
        result.append(start[(int)Math.round(Math.floor(seed1*start.length))]);
        result.append(' ');
        result.append(middle[(int)Math.round(Math.floor(seed2*middle.length))]);
        result.append(' ');
        result.append(end[(int)Math.round(Math.floor(seed3*end.length))]);
        return result.toString();
    }
    
}
