package flymetomars.business.core;

import flymetomars.business.model.Expertise;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;


/**
 * Factory pattern implementation for contractually instantiating Model types.
 * 
 * As a member of the core package this class provides encapsulation of some
 * DAL features as well as (un/)marshaling of model types to lower layers.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * 
 * Note:
 *          While this class does not directly encapsulate the DAL, the methods
 *          that it provides for model configuration for creation are pivotal
 *          to all clients of the core package which mutate data (i.e. handlers)
 */
public class EntityFactory {
    
    /**
     * Convenience helper utility method for the creation of Expertise models
     * 
     * @return Expertise model instance, as initialised by default constructor
     */
    public Expertise createExpertise() {
        return new Expertise();
    }

    /**
     * Convenience helper utility method for the creation of Invitation models
     * 
     * @param creator Person model to record as being the creator of Invitation
     * @param mission Mission that this invitation is being created to invite
     * @return Invitation model instance, initialised with creator and mission
     */
    public Invitation createInvitation(Person creator, Mission mission) {
        Invitation invitation = new Invitation();
        invitation.setCreator(creator);
        invitation.setMission(mission);
        return invitation;
    }
    
    /**
     * Convenience helper utility method for the creation of Location models
     * 
     * @return Location model instance, as initialised by default constructor
     */
    public Location createLocation() {
        return new Location();
    }
    
    /**
     * Convenience helper utility method for the creation of Mission models
     * 
     * @param creator Person model to record as being the captain of Mission
     * @param location Location at which this Mission is taking place
     * @return Mission model instance, initialised with creator and location
     */
    public Mission createMission(Person creator, Location location) {
        Mission mission = new Mission();
        mission.setCaptain(creator);
        mission.setLocation(location);
        return mission;
    }
    
    /**
     * Convenience helper utility method for the creation of Person models
     * 
     * @param password Password model instance to use as auth(n) for this Person
     * @return Person model instance, initialised with given SaltedPassword
     */
    public Person createPerson(SaltedPassword password) {
        Person person = new Person();
        person.setPassword(password);
        return person;
    }
    
    /**
     * Convenience helper utility method for the creation of Salt models
     * 
     * @param saltingText String of the given (unencoded) text to use for salt
     * @return Salt model instance, initialised to encapsulate saltingText value
     */
    public Salt createSalt(String saltingText) {
        Salt salt=new Salt();
        salt.setPureSalt(saltingText);
        return salt;
    }
    
    /**
     * Convenience helper utility method for the creation of SaltedPassword models
     * 
     * @param saltToUse Salt object representing salt to use for this password
     * @return SaltedPassword model instance, initialised with given Salt model
     */
    public SaltedPassword createSaltedPassword(Salt saltToUse) {
        SaltedPassword password=new SaltedPassword();
        password.setSalt(saltToUse);
        return password;
    }
    
}
