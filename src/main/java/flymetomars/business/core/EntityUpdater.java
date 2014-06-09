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

/**
 * EntityUpdater - Core Operator responsible for updating models with DAL
 * 
 * As a member of the core package this class provides encapsulation of some
 * DAL features as well as (un/)marshaling of model types to lower layers.
 *
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 */
public class EntityUpdater {
    

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
    public EntityUpdater(PersonDAO         personDao,
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
     * Updates a Person model object
     *
     * @param person the person to be updated
     * @return the updated person model object
     */
    public Person updatePerson(Person personModel) throws DependencyException {
        if (null == personModel) {
            throw new NullArgumentException("A null person cannot be updated");
        }
        flymetomars.common.datatransfer.Person existingPerson=personDAO.load(personModel.getId());
        if(null==existingPerson) {
            throw new DependencyException("Person does not exist and cannot be updated");
        }
        //if changing email address to different value check new value not taken
        if (!existingPerson.getEmail().equals(personModel.getEmail()) && personDAO.getPersonByEmail(personModel.getEmail()) != null) {
            throw new DependencyException("Duplicate email address already exists for another Person");
        }
        //if changing username to different value check new value not taken
        if (!existingPerson.getUserName().equals(personModel.getUserName()) && personDAO.getPersonByUserName(personModel.getUserName()) != null) {
            throw new DependencyException("Duplicate username already exists for another Person");
        }
        this.personDAO.update(personModel.toDTO());
        return personModel;
    }

    /**
     * Updates a Mission model object
     *
     * @param missionModel The Mission model object to be updated
     * @return the deleted mission
     */
    public Mission updateMission(Mission missionModel) throws DependencyException {
        if(null==missionModel) {
            throw new NullArgumentException("A null mission cannot be updated");
        }
        if(null==missionModel.getId()) {
            throw new NullArgumentException("Cannot update a mission with a null id");
        }
        try {
            if(null==missionDAO.getMissionByName(missionModel.getName())) {
                throw new DependencyException("Mission does not exist in store thus cannot be updated");
            }
            if(null==personDAO.getPersonByUserName(missionModel.getCaptain().getUserName())) {
                throw new DependencyException("Requested Mission captain update refers to unsaved Person");
            }
            if(null==locationDAO.load(missionModel.getLocation().getId())) {
                throw new DependencyException("Requested Mission location update refers to unsaved Location");
            }
            if(null==missionModel.getTime()) {
                throw new NullArgumentException("Mission cannot be updated to have null time");
            }
            flymetomars.common.datatransfer.Mission missNamed = missionDAO.getMissionByName(missionModel.getName());
            if(!missNamed.getId().equals(missionModel.getId())) {
                throw new DependencyException("Mission cannot be updated with that name as it is taken");
            }
        } catch (UnserialisedEntityException ex) {
            throw new DependencyException("Mission entity passed in has not been persisted and so cannot be updated", ex);
        }
        this.missionDAO.update(missionModel.toDTO());
        return missionModel;

    }

    /**
     * Updates a Invitation model object
     *
     * @param invitationModel The Invitation model to be updated
     * @return the updated invitation model object
     */
    public Invitation updateInvitation(Invitation invitationModel) throws DependencyException {
       if(null==invitationModel) {
            throw new NullArgumentException("A null invitation cannot be updated");
        }
        if(null==invitationDAO.load(invitationModel.getId())) {
            throw new DependencyException("Invitation cannot be updated as it does not exist");
        }
        try {
            if(null==missionDAO.getMissionByName(invitationModel.getMission().getName())) {
                throw new UnserialisedEntityException("dummy to cause invokation of catch clause");
            }
        } catch (UnserialisedEntityException ex) {
            throw new DependencyException("Mission does not exist within persistance layer", ex);
        }
        if(null==personDAO.load(invitationModel.getCreator().getId())) {
            throw new DependencyException("Specified Invitation creator is not persisted");
        }
        if(null==personDAO.load(invitationModel.getRecipient().getId())) {
            throw new DependencyException("Specified Invitation recipient is not persisted");
        }
        flymetomars.common.datatransfer.Invitation invitationDTO = invitationModel.toDTO();
        this.invitationDAO.update(invitationDTO);
        return invitationModel;
    }
    
    /**
     * Updates a Location model object in the database
     *
     * @param locationModel The Location model to be updated
     * @return the deleted Location model object
     * @throws Depenedency exception if this location has mission
     */
    public Location updateLocation(Location locationModel) throws DependencyException {
       if(null==locationModel) {
            throw new NullArgumentException("A null location cannot be updated");
        }
        try {
            if(null==missionDAO.getMissionsByLocation(locationModel.toDTO())) {
                throw new UnserialisedEntityException("dummy to cause invocation of catch clause");
            }
        } catch (UnserialisedEntityException ex) {
            throw new DependencyException("This location is a Missionless orphan and cannot be updated", ex);
        }
        flymetomars.common.datatransfer.Location locationDTO = locationModel.toDTO();
        this.locationDAO.update(locationDTO);
        return locationModel;
    }
    
     /**
     * Updates an Expertise model object in the database
     *
     * @param expertiseModel The Expertise model to be updated
     * @return the updated Expertise model object
     * @throws Depenedency exception if tit tries to held by from a valid person to a null
     * person
     */
    public Expertise updateExpertise(Expertise expertiseModel) throws DependencyException {
       if(null==expertiseModel) {
            throw new NullArgumentException("A null expertise cannot be updated");
        }
        if (null == expertiseDAO.load(expertiseModel.getId())) {
            throw new DependencyException("Expertise cannot be updated until it is saved");
        }
        flymetomars.common.datatransfer.Expertise expertiseDTO = expertiseModel.toDTO();
        this.expertiseDAO.update(expertiseDTO);
        return expertiseModel;
    }
    
     /**
     * Updates a SaltedPassword model object in the database
     *
     * @param saltedPasswordModel The SaltedPassword model to be updated
     * @return the updated SaltedPassword model object
     * @throws Depenedency exception if this SaltedPassword belong to some person
     */
    public SaltedPassword updateSaltedPassword(SaltedPassword saltedPasswordModel) throws DependencyException {
       if(null==saltedPasswordModel) {
            throw new NullArgumentException("A null saltedPassword cannot be updated");
        }
        if(null==saltedPasswordModel.getId() || null==saltedPasswordDAO.load(saltedPasswordModel.getId())) {
            throw new DependencyException("SaltedPassword cannot be updated if it does not exist");
        }
        flymetomars.common.datatransfer.Salt saltExists=this.saltDAO.load(saltedPasswordModel.getSalt().getHashedSaltKey());
        if(null==saltExists) {
            throw new DependencyException("Cannot update a SaltedPassword to reference an unsaved Salt");
        }
        flymetomars.common.datatransfer.SaltedPassword saltedPasswordDTO = saltedPasswordModel.toDTO();
        this.saltedPasswordDAO.update(saltedPasswordDTO);
        return saltedPasswordModel;
    }
    
     /**
     * Updates a Salt model object in the binary file
     *
     * @param saltModel The Salt model to be updated
     * @return the updated Salt model object
     * @throws Depenedency exception if this Salt belong to some password
     */
    public Salt updateSalt(Salt saltModel) throws DependencyException {
       if(null==saltModel) {
            throw new NullArgumentException("A null salt cannot be deleted");
        }
        if(null!=this.saltDAO.load(saltModel.getHashedSaltKey())) {
            throw new DependencyException("Cannot update an unsaved Salt object - try save first");
        }
        this.saltDAO.update(saltModel.toDTO());
        return saltModel;
    }
}
