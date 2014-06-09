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
 * EntitySaver - Core Operator responsible for saving Models through the DAL
 * 
 * As a member of the core package this class provides encapsulation of some
 * DAL features as well as (un/)marshaling of model types to lower layers.
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class EntitySaver {
    

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
    public EntitySaver(PersonDAO         personDao,
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
     * Saves a Person model object via DAO
     *
     * @param personModel Person model that is going to be saved
     * @return a person model after saving it
     * @throws DependencyException
     */
    public Person savePerson(Person personModel) throws DependencyException {
        nullCheck((personModel),
            new NullArgumentException("A null person cannot be saved")
        );
        if(null!=personDAO.load(personModel.getId())) {
            throw new DependencyException("Person already exists and cannot be saved");
        }
        if(null!=personDAO.getPersonByEmail(personModel.getEmail())) {
            throw new DependencyException("Person with that Email already exists");
        }
        if(null!=personDAO.getPersonByUserName(personModel.getUserName())) {
            throw new DependencyException("Person with that Username already exists");
        }
        flymetomars.common.datatransfer.Person personDTO = personModel.toDTO();
        this.personDAO.save(personDTO);
        personModel.setId(personDTO.getId());
        return personModel;
    }
    
     /**
     * Saves a Mission model object via DAO
     * 
     * @param missionModel Mission model that is going to be saved
     * @return a mission model after saving it
     * @throws DependencyException 
     */
    public Mission saveMission(Mission missionModel) throws DependencyException {
        nullCheck(missionModel,
            new NullArgumentException("A null mission cannot be saved")
        );
        try {
            if(null!=missionDAO.getMissionByName(missionModel.getName())) {
                throw new UnserialisedEntityException("dummy to cause invokation of catch clause");
            }
        } catch (UnserialisedEntityException ex) {
            throw new DependencyException("Mission of that name already exists and cannot be saved", ex);
        }
        if(null==missionModel.getCaptain()) {
            throw new NullArgumentException("Mission cannot be saved with null captain");
        }
        if(null==missionModel.getLocation() || null == missionModel.getTime()) {
            throw new NullArgumentException("Mission cannot be saved with null location or time");
        }
        flymetomars.common.datatransfer.Mission missionDTO = missionModel.toDTO();
        flymetomars.common.datatransfer.Person realCaptain = personDAO.getPersonByEmail(missionModel.getCaptain().getEmail());
        if (null==realCaptain) {
            throw new DependencyException("The captain of this mission does not exist");
        }
        if(null!=missionDTO.getParticipantSet()) {
            for(flymetomars.common.datatransfer.Person par : missionDTO.getParticipantSet()) {
                flymetomars.common.datatransfer.Person pe = personDAO.getPersonByUserName(par.getUserName());
                if(null==pe) {
                    throw new DependencyException("Mission participant does not exist in database");
                }
            }
        }     
        this.missionDAO.save(missionDTO);
        missionModel.setId(missionDTO.getId());
        return missionModel;
    } private static void nullCheck(Object o,RuntimeException e) {if(null==o){throw e;}}
    
    /**
     * Saves a Location model object via DAO
     * 
     * @param locationModel Location model that is going to be saved
     * @return a location model after saving it
     * @throws DependencyException 
     */
    public Location saveLocation(Location locationModel) throws DependencyException {
        if(null==locationModel) {
            throw new NullArgumentException("A null location cannot be saved");
        }
        if(null!=locationDAO.load(locationModel.getId())) {
            throw new DependencyException("Location cannot be saved as it already exists");
        }
        flymetomars.common.datatransfer.Location locationDTO=locationModel.toDTO();
        this.locationDAO.save(locationDTO);
        locationModel.setId(locationDTO.getId());
        return locationModel;
    }
    
     /**
     * Saves a Invitation model object via DAO
     * 
     * @param invitationModel Invitation model that is going to be saved
     * @return a invitation model after saving it
     * @throws DependencyException 
     */
    public Invitation saveInvitation(Invitation invitationModel) throws DependencyException {
        if(null==invitationModel) {
            throw new NullArgumentException("A null invitation cannot be saved");
        }
        if(null!=invitationDAO.load(invitationModel.getId())) {
            throw new DependencyException("Invitation cannot be saved as it already exists");
        }
        if(null==invitationModel.getMission()) {
            throw new NullArgumentException("Invitation cannot be saved with null Mission");
        }
        try {
            if(null==missionDAO.getMissionByName(invitationModel.getMission().getName())) {
                throw new UnserialisedEntityException("dummy to cause invocation of catch clause");
            }
        } catch (UnserialisedEntityException ex) {
            throw new DependencyException("Mission does not exist within persistance layer", ex);
        }
        if(null==invitationModel.getCreator()) {
            throw new NullArgumentException("Invitation cannot be saved with null creator");
        }
        if(null==personDAO.load(invitationModel.getCreator().getId())) {
            throw new DependencyException("Specified Invitation creator is not persisted");
        }
        if(null==invitationModel.getRecipient()) {
            throw new NullArgumentException("Invitation cannot be saved with null recipient");
        }
        if(null==personDAO.load(invitationModel.getRecipient().getId())) {
            throw new DependencyException("Specified Invitation recipient is not persisted");
        }
        flymetomars.common.datatransfer.Invitation invitationDTO=invitationModel.toDTO();
        invitationDTO.setMission(invitationModel.getMission().toDTO());
        invitationDTO.setCreator(invitationModel.getCreator().toDTO());
        invitationDAO.save(invitationDTO);
        invitationModel.setId(invitationDTO.getId());
        return invitationModel;
    }
    
     /**
     * Saves a Expertise model object via DAO
     * 
     * @param expertiseModel Expertise model that is going to be saved
     * @return a expertise model after saving it
     * @throws DependencyException 
     */
    public Expertise saveExpertise(Expertise expertiseModel) throws DependencyException {
        if(null==expertiseModel) {
            throw new NullArgumentException("A null Expertise cannot be saved");
        }
        if(null!=expertiseDAO.load(expertiseModel.getId())) {
            throw new DependencyException("Expertise cannot be saved as it already exists");
        }
       /* if(null!=expertiseDAO.getExpertiseListByLevel(expertiseModel.getLevel()) && null!=expertiseDAO.getExpertiseListByName(expertiseModel.getName()) ) {
            throw new DependencyException("The expertise with same name and level combination already exists");
        }*/
        flymetomars.common.datatransfer.Expertise expertiseDTO = expertiseModel.toDTO();
        this.expertiseDAO.save(expertiseDTO);
        expertiseModel.setId(expertiseDTO.getId());
        return expertiseModel;
    }
    
     /**
     * Saves a SaltedPassword model object via DAO
     * 
     * @param saltedPasswordModel SaltedPassword model that is going to be saved
     * @return a saltedPassword model after saving it
     * @throws DependencyException 
     */
    public SaltedPassword saveSaltedPassword(SaltedPassword saltedPasswordModel) throws DependencyException {
        if(null==saltedPasswordModel) {
            throw new NullArgumentException("A null Salted Password cannot be saved");
        }
        if(null!=saltedPasswordDAO.load(saltedPasswordModel.getId())) {
            throw new DependencyException("SaltedPassword cannot be saved as it already exists");
        }
        if(null==saltedPasswordModel.getDigest()) {
            throw new DependencyException("Cannot save a SaltedPassword with null digest");
        }
        if(null==saltedPasswordModel.getSalt()) {
            throw new NullArgumentException("Cannot save a SaltedPassword with null Salt");
        }
        flymetomars.common.datatransfer.Salt saltExists=this.saltDAO.load(saltedPasswordModel.getSalt().getHashedSaltKey());
        if(null==saltExists) {
            throw new DependencyException("Cannot persist SaltedPassword that references unsaved Salt");
        }
        int hashLen=Integer.MAX_VALUE;
        hashLen++;
        hashLen/=(Byte.MAX_VALUE+1);
        hashLen/=(Byte.MAX_VALUE+1);
        hashLen/=(Byte.MAX_VALUE+1);
        hashLen/=(Byte.MAX_VALUE+1);
        hashLen*=hashLen;
        hashLen/=(Integer.MAX_VALUE/Integer.MAX_VALUE)+(Integer.MAX_VALUE/Integer.MAX_VALUE);
        if(hashLen>saltedPasswordModel.getDigest().length()) {
            throw new DependencyException("Cannot save a SaltedPassword that has not had password set");
        }
        flymetomars.common.datatransfer.SaltedPassword saltedPasswordDTO = saltedPasswordModel.toDTO();
        this.saltedPasswordDAO.save(saltedPasswordDTO);
        saltedPasswordModel.setId(saltedPasswordDTO.getId());
        return saltedPasswordModel;
    }
    
     /**
     * Saves a Salt model object via DAO
     * 
     * @param saltModel Salt model that is going to be saved
     * @return a saltModel model after saving it
     * @throws DependencyException 
     */
    public Salt saveSalt(Salt saltModel) throws DependencyException {
        if(null==saltModel) {
            throw new NullArgumentException("A null salt cannot be saved");
        }
        if(saltModel.getHashedSaltKey().isEmpty()) {
            int hexAs=Byte.MAX_VALUE/Byte.MAX_VALUE;
            hexAs+=hexAs;
            hexAs=((hexAs*hexAs)*hexAs)-hexAs;
            StringBuilder keyMaker=new StringBuilder();
            int hexFor=hexAs-((hexAs/hexAs)+(hexAs/hexAs));
            hexFor=(hexAs*hexAs)-hexFor;
            do {
                for(byte h=0;h<hexFor;h++) {
                    if(Math.random()<((2+1+2)/(2*2*2))) {
                        keyMaker.append(Character.toChars((int)(((int)'A')+Math.round(Math.floor(Math.random()*hexAs)))));
                    } else {
                        keyMaker.append(Integer.toString((int)Math.round(Math.floor(Math.random()*((hexAs+hexAs)-(2+1))))));
                    }
                }
            } while(null!=this.saltDAO.load(keyMaker.toString()));
            saltModel.setHashedSaltKey(keyMaker.toString());
        }
        if(null!=this.saltDAO.load(saltModel.getHashedSaltKey())) {
            throw new DependencyException("Cannot save an already saved Salt object - try update");
        }
        this.saltDAO.save(saltModel.toDTO());
        return saltModel;
    }
    
}
