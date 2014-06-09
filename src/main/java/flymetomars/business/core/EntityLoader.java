package flymetomars.business.core;

import flymetomars.business.model.Expertise;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import flymetomars.common.NullArgumentException;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * EntitySaver - Core Operator responsible for retrieving saved Models from DAL
 * 
 * As a member of the core package this class provides encapsulation of some
 * DAL features as well as (un/)marshaling of model types to lower layers.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntityLoader {

    private InvitationDAO invitationDAO;
    private MissionDAO missionDAO;
    private LocationDAO locationDAO;
    private PersonDAO personDAO;
    private ExpertiseDAO expertiseDAO;
    private SaltedPasswordDAO saltedPasswordDAO;
    private SaltDAO saltDAO;

    /**
     * Public constructor taking dependency-injected DAO refs as dependencies
     * 
     * @param personDao PersonDAO instance for the core operations pertaining to Person models
     * @param invitationDao InvitationDAO instance for the core operations pertaining to Invitation models
     * @param missionDao MissionDAO instance for the core operations pertaining to Mission model
     * @param locationDao LocationDAO instance for the core operations pertaining to Location models
     * @param expertiseDao ExpertiseDAO instance for the core operations pertaining to Expertise models
     * @param passwordDao SaltedPasswordDAO instance for the core operations pertaining to SaltedPassword models
     * @param saltDao SaltDAO instance for the core operations pertaining to Salt models
     */
    public EntityLoader(PersonDAO         personDao,
                        InvitationDAO     invitationDao,
                        MissionDAO        missionDao,
                        LocationDAO       locationDao,
                        ExpertiseDAO      expertiseDao,
                        SaltedPasswordDAO passwordDao,
                        SaltDAO           saltDao
    ) {
        this.personDAO = personDao;
        this.invitationDAO = invitationDao;
        this.missionDAO = missionDao;
        this.locationDAO = locationDao;
        this.expertiseDAO = expertiseDao;
        this.saltedPasswordDAO = passwordDao;
        this.saltDAO = saltDao;
    }

    /**
    * Loads all the Person from the data access layer
    */
    public List<Person> loadAllPersons() {
        List<Person> persons = new ArrayList<Person>();
        for (flymetomars.common.datatransfer.Person p : this.personDAO.getAll()) {
            Person person=new Person(p);
            //begin dodgy inner loop invitation population handling
            if(person.getInvitationsReceived().isEmpty()) {  //has to be done here
                Set<Invitation> inv=new HashSet<Invitation>();
                try {  //to prevent circular reference loop in toDTO
                    for(flymetomars.common.datatransfer.Invitation i : this.invitationDAO.getInvitationsByRecipient(p)) {
                        Invitation invy=new Invitation(i);
                        invy.setRecipient(person);
                        invy.setCreator(new Person(invitationDAO.load(i.getId()).getCreator()));
                        inv.add(invy);
                    }
                } catch (UnserialisedEntityException ex) {
                    Logger.getLogger(EntityLoader.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NullArgumentException pq) { if(!pq.getMessage().startsWith("specified recipient")) { throw pq;} }
                person.setInvitationsReceived(inv);
            }
            //end dodgy
            persons.add(person); 
        }
        return persons;
    }
    
    /**
    * Loads a Person from the data access layer by his email-id
    */
    public Person loadPersonByEmail(String emailAddr) {
        if(null==emailAddr) {
            throw new NullArgumentException("A null e-mail cannot be searched");
        }
        flymetomars.common.datatransfer.Person dto = this.personDAO.getPersonByEmail(emailAddr);
        if(null==dto) { return null; }
        Person p=new Person(dto);
        if(null!=p) { p=loadPass(dto, p); }
        return p;
    }
    
    /**
     * private helper method for loading SaltedPAssword DTO data into Person models
     */
    private Person loadPass(flymetomars.common.datatransfer.Person dto, Person p) {
        if(null!=dto.getPassword()) {
            Long pid=dto.getPassword();
            flymetomars.common.datatransfer.SaltedPassword pass = this.saltedPasswordDAO.load(pid);
            if(null==pass) { return p; }
            p.setPassword(new SaltedPassword(pass));
            String sid=pass.getSaltId();
            if (null!=sid) {
                Salt salt=new Salt(this.saltDAO.load(sid));
                if(null!=salt) { p.getPassword().setSalt(salt); }
            }
        }
        return p;
    }
    
    /**
    * Loads a Person from the data access layer by his email-id
    */
    public Person loadPersonByUserName(String username) {
        if(null==username) {
            throw new NullArgumentException("A null user name cannot be searched");
        }
        flymetomars.common.datatransfer.Person dto = this.personDAO.getPersonByUserName(username);
        if(null==dto) { return null; }
        Person p=new Person(dto);
        if(null!=p) { p=loadPass(dto, p); }
        return p;
    }
    
    /**
    * Loads all the Missions from the data access layer
    */
    public List<Mission> loadAllMissions() {
        List<Mission> missions = new ArrayList<Mission>();
        for (flymetomars.common.datatransfer.Mission m : this.missionDAO.getAll()) {
            missions.add(new Mission(m));
        }
        return missions;
    }
    
    /**
     * Loads a specific Mission model by it's unique identifier code number
     * 
     * @param id the unique identifier code number to retrieve Mission for
     * @return Mission instance matching the supplied id, or null none found
     */
    public Mission loadMissionById(long id) {
        flymetomars.common.datatransfer.Mission m;
        m=this.missionDAO.load(id);
        if(null==m) { return null; }
        Mission result=new Mission(m);
        if(result.getInvitationSet().isEmpty()) {
            try {
                Set<Invitation> invites=new HashSet<Invitation>();
                for(flymetomars.common.datatransfer.Invitation inv : invitationDAO.getInvitationsByCreator(m.getCaptain())) {
                    if(!inv.getMission().getId().equals(id)) { continue; }
                    Invitation i=new Invitation();
                    i.setId(inv.getId());
                    invites.add(i);
                }
                result.setInvitationSet(invites);
            } catch (UnserialisedEntityException ex) {
                Logger.getLogger(EntityLoader.class.getName()).log(Level.INFO, "Unable to retreve invitation set for mission", ex);
            }
        }
        return result;
    }
    
     /**
     * Loads a specific Expertise model by it's unique identifier code number
     * 
     * @param id the unique identifier code number to retrieve Expertise for
     * @return Expertise instance matching the supplied id, or null none found
     */
    public Expertise loadExpertiseById(long id) {
        flymetomars.common.datatransfer.Expertise e;
        e=this.expertiseDAO.load(id);
        if(null==e) { return null; }
        return new Expertise(e);
    }

    /**
     * Loads a specific Invitation model by it's unique identifier code number
     * 
     * @param id the unique identifier code number to retrieve Invitation for
     * @return Invitation instance matching the supplied id, or null none found
     */
    public Invitation loadInvitationById(Long id) {
        if(null==id) {
            throw new NullArgumentException("Cannot load Invitation with null id");
        }
        flymetomars.common.datatransfer.Invitation inv=this.invitationDAO.load(id);
        if(null==inv) { return null; }
        return new Invitation(inv);
    }
    
    /**
     * Loads a specific Location model by it's unique identifier code number
     * 
     * @param id the unique identifier code number to retrieve Location for
     * @return Location instance matching the supplied id, or null none found
     */
    public Location loadLocationById(long id) {
        flymetomars.common.datatransfer.Location l;
        l=this.locationDAO.load(id);
        if(null==l) { return null; }
        return new Location(l);
    }
    
    /**
    * Loads all the Locations from the data access layer
    */
    public List<Location> loadAllLocations() {
        List<Location> locations = new ArrayList<Location>();
        for (flymetomars.common.datatransfer.Location i : this.locationDAO.getAll()) {
            locations.add(new Location(i));
        }
        return locations;
    }
    
    /**
     * Loads the list of Invitations of created by a person
     * 
     * @param owner
     * @return 
     */
    public List<Invitation> loadInvitationsByCreator(Person owner) {
        if (null == owner) {
            throw new NullArgumentException("Cannot load Invitation set by null Person");
        }
        try {
            List<Invitation> invitations = new ArrayList<Invitation>();
            for (flymetomars.common.datatransfer.Invitation i : this.invitationDAO.getInvitationsByCreator(owner.toDTO())) {
                Invitation inv=new Invitation(i);
                inv.setCreator(owner);
                invitations.add(inv);
            }
            return invitations;
        } catch (UnserialisedEntityException ex) {
            throw new IllegalArgumentException("Cannot load invitations for transient Person", ex);
        }
    }
    
    /**
     * Loads the list of missions of a person who is captain for all these missions
     * 
     * @param captain Person model object for whom to load the list of piloted Missions
     * @return List of Mission model object which all share the common `captain' as captain
     */
    public List<Mission> loadMissionsByCaptain(Person captain) {
        if (null == captain) {
            throw new NullArgumentException("Cannot load missions of null Captain");
        }
        try {
            List<Mission> missions = new ArrayList<Mission>();
            for (flymetomars.common.datatransfer.Mission m : this.missionDAO.getMissionsByCaptain(captain.toDTO())) {
                missions.add(new Mission(m));
            }
            return missions;
        } catch (UnserialisedEntityException ex) {
            throw new IllegalArgumentException("Cannot load missions for transient Person", ex);
        }
    }
    
    /**
    * Loads all the Expertise from the data access layer
    */
    public List<Expertise> loadAllExpertise() {
        List<Expertise> exps = new ArrayList<Expertise>();
        for (flymetomars.common.datatransfer.Expertise i : this.expertiseDAO.getAll()) {
            exps.add(new Expertise(i));
        }
        return exps;
    }
    
    /**
    * Loads all the SaltedPasswords from the data access layer
    */
    public List<SaltedPassword> loadAllSaltedPasswords() {
        List<SaltedPassword> passwords = new ArrayList<SaltedPassword>();
        for (flymetomars.common.datatransfer.SaltedPassword s : this.saltedPasswordDAO.getAll()) {
            SaltedPassword p=new SaltedPassword(s);
            p.setSalt(new Salt(this.saltDAO.load(s.getSaltId())));
            passwords.add(p);
        }
        return passwords;
    }
    
    /**
    * Loads all the SaltedPasswords that reference the given `salt' value
    * 
    * @param salt Salt model object instance to use in search
    */
    public List<SaltedPassword> loadAllSaltedPasswordsSharingSalt(Salt salt) {
        flymetomars.common.datatransfer.Salt saltDTO=salt.toDTO();
        List<SaltedPassword> passwords = new ArrayList<SaltedPassword>();
        List<flymetomars.common.datatransfer.SaltedPassword> results;
        results=this.saltedPasswordDAO.getSaltedPasswordsSharingSameSalt(saltDTO);
        for (flymetomars.common.datatransfer.SaltedPassword s : results) {
            SaltedPassword p=new SaltedPassword(s);
            p.setSalt(new Salt(this.saltDAO.load(s.getSaltId())));
            passwords.add(p);
        }
        return passwords;
    }
    
    /**
    * Loads all the Salts from the data access layer
    */
    public List<Salt> loadAllSalts() {
        List<Salt> salts = new ArrayList<Salt>();
        for (flymetomars.common.datatransfer.Salt s : this.saltDAO.getAll()) {
            salts.add(new Salt(s));
        }
        return salts;
    }
    
}
