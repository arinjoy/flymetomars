package flymetomars.presentation.resources;

import flymetomars.business.mining.MissionMiner;
import flymetomars.business.mining.PersonMiner;
import flymetomars.business.model.Expertise;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.presentation.webservices.auth.UserAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;

/**
 * 
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class UserResource extends FreemarkerResource {
    
    private static final int HOME_USER_INDEX = 2;

    private PersonMiner personMiner;
    private MissionMiner missionMiner;

    private Person user;

    @Override
    public void doInit() throws ResourceException {
        super.doInit();
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("user.html.ftl");
        String requestedUserName = getRequestedUserName(getRequest());
        try {
            user=entityLoader.loadPersonByUserName(requestedUserName);
            super.doInit();
        } catch (DataAccessException e) {
            final String msg = "Error loading user: " + requestedUserName;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedUserName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_USER_INDEX);
    }


    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        if (null == user) {
            exceptionHandler.displayErrorOnScreen(
                "Cannot find user with name: " + getRequestedUserName(this.getRequest()));
        }
        dataModel.put("authenticatedUsername", authenticatedUser.getUserName());
        dataModel.put("requestedUser", user);

        // put in expertise gained
        logger.debug("Put in expertise gained");
        Set<Expertise> gainedExpertise=user.getExpertiseGained();
        dataModel.put("expertiseGained", gainedExpertise);
        
        // put in missions created
        logger.debug("Put in missions created.");
        //Set<Mission> missionsCreated = missionDao.getMissionsByCreator(user);
        List<Mission> missionsByCreator=entityLoader.loadMissionsByCaptain(user);
        dataModel.put("missionsCreated", missionsByCreator);

        // put in invitations created
        logger.debug("Put in invitations created.");
        List<Invitation> invitationsByCreator = entityLoader.loadInvitationsByCreator(user);
        /*new ArrayList<Invitation>();
        for(Invitation i : entityLoader.loadInvitationsByCreator(user)) {
            i.setCreator(user);
            i.setRecipient(entityLoader.loadInvitationById(i.getId().longValue()).getRecipient());
        }*/
        dataModel.put("invitationsCreated", invitationsByCreator);
       
        // put in missions registered
        logger.debug("Put in missions registered.");
        Set<Mission> registeredMissions = user.getMissionsRegistered();
        dataModel.put("missionsRegistered", registeredMissions);

        // put in invitations received with status (rejected, accepted, not_decided)
        logger.debug("Put in invitations received.");
        Set<Invitation> invitationsReceived = user.getInvitationsReceived();
        for(Invitation i : invitationsReceived) {
            i.setCreator(entityLoader.loadInvitationById(i.getId().longValue()).getCreator());
        }
        dataModel.put("invitationsReceived", invitationsReceived);
        
        logger.debug("Put in buddies");
        List<Person> buddies = Collections.emptyList();
        try {
            buddies=personMiner.mineTopFriends(user,3);
        } catch (IllegalArgumentException iae) {
            String msg="The top-k buddies cannot be searched when no buddy exists at all";
            if(!iae.getMessage().equals(msg)) { throw iae; }
        }
        //personDao.evictAll(buddies);
        dataModel.put("buddies", buddies);
        
        logger.debug("Put in Celebrities");
         List<Person> celebrities = Collections.emptyList();
         try {
             celebrities = personMiner.mineTopCelebrities(3);
         } catch (IndexOutOfBoundsException ioobe) {}
        //personDao.evictAll(celebrities);
        dataModel.put("celebrities", celebrities); 

        logger.debug("Get all friends");
        Set<Person> friends = personMiner.mineColleagues(user);
       // personDao.evictAll(friends);
        dataModel.put("friends", friends);
        
        logger.debug("Get Popular people");
        Person popularPerson = personMiner.mineTopCelebrities(1).get(0);
       // personDao.evict(popularPerson);
        dataModel.put("popularPerson", popularPerson);

        logger.debug("Get social circle");
        Set<Person> socialCircle = personMiner.mineMaxSocialCircle(user);
        //personDao.evictAll(socialCircle);
        dataModel.put("socialCircle", socialCircle);

        logger.debug("Get Sour grapes");
        List<Mission> sourGrapes = missionMiner.mineSourGrapes(user,3);
        dataModel.put("sourGrapes", sourGrapes);
        
        return super.doAuthenticatedGet(variant);
    }

    @Override
    protected boolean authorizeGet() {
        return null == user || manager.decide(authenticatedUser, user, UserAction.READ);
    }

    public void setPersonMiner(PersonMiner personMiner) {
        this.personMiner = personMiner;
    }
    
    public void setMissionMiner(MissionMiner missionMiner) {
        this.missionMiner = missionMiner;
    }
    
}
