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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * EntityDeleter - Core Operator responsible for deleting business models
 * 
 * As a member of the core package this class provides encapsulation of some
 * DAL features as well as (un/)marshaling of model types to lower layers.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntityDeleter {
    

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
    public EntityDeleter(PersonDAO         personDao,
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
     * Delete the given person only if the person does not have any mission or invitation created by her.
     * Otherwise throw a DePendencyException.
     *
     * @param person the person to be deleted.
     * @return the deleted person model object
     * @throws DependencyException thrown when the person cannot be deleted due to a dependency.
     */
    public Person deletePerson(Person personModel) throws DependencyException {     
        eif((null==personModel),
            new NullArgumentException("A null person cannot be deleted")
        );
        if(null==personDAO.getPersonByEmail(personModel.getEmail())) {
            throw new DependencyException("Person does not exist and cannot be deleted");
        }
        List<flymetomars.common.datatransfer.Mission> missionsByCaptain=null;
        List<flymetomars.common.datatransfer.Invitation> invsByCreator=null;
        List<flymetomars.common.datatransfer.Invitation> invsByRecipient=null;
        try {
            missionsByCaptain = missionDAO.getMissionsByCaptain(personModel.toDTO());
            try {
                invsByCreator = invitationDAO.getInvitationsByCreator(personModel.toDTO());
            } catch (NullArgumentException p) { invsByCreator=Collections.emptyList(); }
            try {
                invsByRecipient = invitationDAO.getInvitationsByRecipient(personModel.toDTO());
            } catch (NullArgumentException q) { invsByRecipient=Collections.emptyList(); }
        } catch (UnserialisedEntityException ex) {
            Logger.getLogger(EntityDeleter.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new DependencyException("Cannot save mission with unsaved elements in join references", ex);
        }
        if(!personModel.getMissionsRegistered().isEmpty()) {
            throw new DependencyException("Cannot delete Person who is participating in any Missions");
        }
        if (null == missionsByCaptain || missionsByCaptain.isEmpty()) {
            if (null == invsByCreator || invsByCreator.isEmpty()) {
                if (null == invsByRecipient || invsByRecipient.isEmpty()) {
                    flymetomars.common.datatransfer.Person personDTO = personModel.toDTO();
                    this.personDAO.delete(personDTO);
                } else {
                    throw new DependencyException("Person cannot be deleted as he is recipient of an invitation");
                }
            } else {
                throw new DependencyException("Person cannot be deleted as he is a creator of an invitation");
            }
        } else {
            throw new DependencyException("Person cannot be deleted as he is captain of a mission");
        }
        return personModel;
    }

    private static void eif(boolean condition, RuntimeException e) {if(condition){ throw e; }}
    
    /**
     * Delete the given mission only if the mission does not have any person registered.
     * Otherwise throw a DependencyException.
     *
     * @param missionModel The Mission model object to be deleted
     * @return the deleted mission
     * @throws DependencyException thrown when the mission cannot be deleted due to a dependency.
     */
    public Mission deleteMission(Mission missionModel) throws DependencyException {
        if(null==missionModel) {
            throw new NullArgumentException("A null mission cannot be deleted");
        }
        flymetomars.common.datatransfer.Mission missionExist = missionDAO.load(missionModel.getId());
        if(null==missionExist) {
            throw new DependencyException("Cannot delete Mission that does not exist");
        }
        if(!missionModel.getParticipantSet().isEmpty()||missionExist.getParticipantSet().size()>missionModel.getParticipantSet().size()) {
            throw new DependencyException("Cannot delete Mission with non-empty participant set");
        }
        if(!missionModel.getInvitationSet().isEmpty()||missionExist.getInvitationSet().size()>missionModel.getInvitationSet().size()) {
            throw new DependencyException("Cannot delete Mission with Invitations still pending to users");
        }
        this.missionDAO.delete(missionExist);
        return missionModel;
    }

    /**
     * Delete the given invitation only if the invitation has not been accepted.
     * Otherwise throw a DependencyException.
     *
     * @param invitationModel The Invitation model to be deleted
     * @return the deleted invitation model object
     * @throws DependencyException thrown when the invitation cannot be deleted due to a dependency.
     */
    public Invitation deleteInvitation(Invitation invitationModel) throws DependencyException {
       if(null==invitationModel) {
            throw new NullArgumentException("A null invitation cannot be deleted");
        }
        if(null==invitationDAO.load(invitationModel.getId())) {
            throw new DependencyException("Invitation cannot be deleted until it is saved");
        }
        flymetomars.common.datatransfer.Invitation invitationDTO = invitationModel.toDTO();
        this.invitationDAO.delete(invitationDTO);
        return invitationModel;
    }
    
    /**
     * Deletes a location model object from the database
     *
     * @param locationModel The Location model to be deleted
     * @return the deleted Location model object
     * @throws Depenedency exception if this location has mission
     */
    public Location deleteLocation(Location locationModel) throws DependencyException {
       if(null==locationModel) {
            throw new NullArgumentException("A null location cannot be deleted");
       }
       if(null==locationDAO.load(locationModel.getId())) {
            throw new DependencyException("Location cannot be deleted until it is saved");
       }
       try {
            List<flymetomars.common.datatransfer.Mission> refmiss = this.missionDAO.getMissionsByLocation(locationModel.toDTO());
           if(null!=refmiss && !refmiss.isEmpty()) {
               throw new UnserialisedEntityException("dummy to cause invocation of catch clause");
           }
       } catch(UnserialisedEntityException ex) {
           throw new DependencyException("A Location where Mission occurs cannot be deleted", ex);
       }
        flymetomars.common.datatransfer.Location locationDTO = locationModel.toDTO();
        this.locationDAO.delete(locationDTO);
        return locationModel;
    }
    
     /**
     * Deletes an Expertise model object from the database
     *
     * @param expertiseModel The Expertise model to be deleted
     * @return the deleted Expertise model object
     * @throws Depenedency exception if the expertise is not saved already
     */
    public Expertise deleteExpertise(Expertise expertiseModel) throws DependencyException {
        if (null == expertiseModel) {
            throw new NullArgumentException("A null expertise cannot be deleted");
        }
        if (null == expertiseDAO.load(expertiseModel.getId())) {
            throw new DependencyException("Expertise cannot be deleted until it is saved");
        }
        flymetomars.common.datatransfer.Expertise expertiseDTO = expertiseModel.toDTO();
        this.expertiseDAO.delete(expertiseDTO);
        return expertiseModel;
    }
    
     /**
     * Deletes an SaltedPassword model object from the database
     *
     * @param saltedPasswordModel The SaltedPassword model to be deleted
     * @return the deleted SaltedPassword model object
     * @throws Depenedency exception if this SaltedPassword belong to some person
     */
    public SaltedPassword deleteSaltedPassword(SaltedPassword saltedPasswordModel) throws DependencyException {
        if(null==saltedPasswordModel) {
            throw new NullArgumentException("A null saltedPassword cannot be deleted");
        }
        if(null==saltedPasswordModel.getId() || null==saltedPasswordDAO.load(saltedPasswordModel.getId())) {
            throw new DependencyException("SaltedPassword cannot be deleted if it does not exist");
        }
        final flymetomars.common.datatransfer.SaltedPassword spDTO=saltedPasswordModel.toDTO();
        List<flymetomars.common.datatransfer.Person> all=new ArrayList<flymetomars.common.datatransfer.Person>(personDAO.getAll());
        final Set<flymetomars.common.datatransfer.Person> owners=new HashSet<flymetomars.common.datatransfer.Person>();
        Collections.sort(all, new Comparator<flymetomars.common.datatransfer.Person> () {
            @Override
            public int compare(flymetomars.common.datatransfer.Person p1, flymetomars.common.datatransfer.Person p2) {
                if(null!=p1 && null!=p1.getPassword() && spDTO.getId().equals(p1.getPassword())) { owners.add(p1); }
                if(null!=p2 && null!=p2.getPassword() && spDTO.getId().equals(p2.getPassword())) { owners.add(p2); }
                return 0;
            }
        });
        if(!owners.isEmpty()) {
            throw new DependencyException("Cannot delete SaltedPassword still referenced by a Person");
        }
        this.saltedPasswordDAO.delete(spDTO);
        return saltedPasswordModel;
    }
    
     /**
     * Deletes an Salt model object from the database
     *
     * @param saltModel The Salt model to be deleted
     * @return the deleted Salt model object
     * @throws Depenedency exception if this Salt belong to some password
     */
    public Salt deleteSalt(Salt saltModel) throws DependencyException {
       if(null==saltModel) {
            throw new NullArgumentException("A null salt cannot be deleted");
        }
        if(null==this.saltDAO.load(saltModel.getHashedSaltKey())) {
            throw new DependencyException("Cannot delete an unsaved Salt object - try save first");
        }
        flymetomars.common.datatransfer.Salt saltDTO = saltModel.toDTO();
        if(!this.saltedPasswordDAO.getSaltedPasswordsSharingSameSalt(saltDTO).isEmpty()) {
            throw new DependencyException("Cannot delete a Salt object with refering passwords");
        }
        this.saltDAO.delete(saltDTO);
        return saltModel;
    }
    
}
