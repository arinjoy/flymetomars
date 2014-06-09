package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.InvitationHandler;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Person;
import flymetomars.common.datatransfer.Invitation.InvitationStatus;
import flymetomars.presentation.webservices.auth.UserAction;
import flymetomars.presentation.webservices.util.FormHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public class CreateInvitationResource extends FreemarkerResource {
    
    private static final int HOME_MISSION_INDEX = 2;

    private FormHandler formHandler;
    private EntityFactory entityFactory;
    private EntitySaver entitySaver;
    private EntityUpdater entityUpdater;
    private InvitationHandler invitationHandler;

    private Mission mission;

    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("create_invitation.html.ftl");
        dataModel.put("title", "Create invitation");
        String missionId = getRequestedMissionName(getRequest());
        try {
            mission = entityLoader.loadMissionById(Long.parseLong(missionId));
            super.doInit();
        } catch (DataAccessException e) {
            final String msg = "Error loading user: " + missionId;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedMissionName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_MISSION_INDEX);
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel.put("authenticatedUser", authenticatedUser);
        String init = getRequest().getResourceRef().getQueryAsForm().getFirstValue("init");
        if (init != null && init.equals("true")) {
            formHandler.resetAllFields();
        }
        dataModel.put("mission", mission);
        dataModel.put("receipientValue", formHandler.getFirstFieldValue("receipient"));
        dataModel.put("toSend", formHandler.getFirstFieldValue("toSendValue"));
        return super.doAuthenticatedGet(variant);
    }

    @Override
    @Post("html")
    protected Representation doAuthenticatedPost(Representation entity, Variant variant) throws ResourceException {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        final boolean validFormValues = formHandler.saveFieldValues(entity);
        if (!validFormValues) {
            exceptionHandler.displayErrorOnScreen("Invalid invitation information.");
            final Map<String, List<String>> inputErrors = formHandler.getInputErrors();
            for (String key : inputErrors.keySet()) {
                errorMap.put(key + "Error", inputErrors.get(key).get(0));
            }
        }
        if(null==authenticatedUser) { errorMap.put("errorMessage", "Authentication issue"); }
        if (errorMap.isEmpty()) {
            errorMap.putAll(createAndSaveInvitation(formHandler.getFirstFieldValue("receipient"),
                    formHandler.getFirstFieldValue("toSend")));
            if (errorMap.size()==1 && errorMap.containsKey("id")) {
                getResponse().redirectPermanent(getRequest().getRootRef().toString() + "/invitation/" + errorMap.get("id"));
                errorMap.remove("id");
            }
        }
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            dataModel.putAll(errorMap);
        }
        return get(variant);
    }

    private Map<String, String> createAndSaveInvitation(String receipientName, String status) {
        Map<String, String> errorMap = new LinkedHashMap<String, String>();
        Person recipient = this.entityLoader.loadPersonByUserName(receipientName);
        if(recipient==null) {
             errorMap.put("errorMessage", "Recipient does not exist. Enter a valid recipient username!");
            return errorMap;
        }
        if(recipient.getUserName().equals(authenticatedUser.getUserName())) {
            errorMap.put("errorMessage", "You cannot invite to yourself! Please send invite to someone else.");
            return errorMap;
        }
        Invitation invitation;  Person creator=authenticatedUser;
        if(null==authenticatedUser) { creator=mission.getCaptain(); }
        invitation = entityFactory.createInvitation(creator, mission);     
        invitation.setRecipient(recipient);
        InvitationStatus invitationStatus = InvitationStatus.valueOf(status);
        if (invitationStatus.equals(InvitationStatus.SENT)) {
            invitationHandler.sendInvitation(invitation, recipient);
        }
        invitation.setStatus(invitationStatus);
        invitation.setLastUpdated(Calendar.getInstance().getTime());
        try {
            invitation=entitySaver.saveInvitation(invitation);
            /*if (invitationStatus.equals(InvitationStatus.SENT)) {
                entityUpdater.updatePerson(recipient);
            }*/
            //entityUpdater.updateMission(mission);
            if(null==invitation.getId()) { throw new IllegalStateException("ID not allocated internally for invite"); }
        } catch (DependencyException ex) {
             errorMap.put("errorMessage", "Error saving invitation: " + ex.getMessage());
        } catch (RuntimeException e) {
            errorMap.put("errorMessage", "Error saving invitation: " + e.getMessage());
        }
        if(errorMap.isEmpty()) { errorMap.put("id", invitation.getId().toString()); }
        return errorMap;
    }

    @Override
    protected boolean authorizeGet() {
        return manager.decide(authenticatedUser, Invitation.class, mission, UserAction.CREATE);
    }

    public void setFormHandler(FormHandler formHandler) {
        this.formHandler = formHandler;
    }

    public void setEntityFactory(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    public void setEntitySaver(EntitySaver entitySaver) {
        this.entitySaver = entitySaver;
    }

    public void setInvitationHandler(InvitationHandler invitationHandler) {
        this.invitationHandler = invitationHandler;
    }

    public void setEntityUpdater(EntityUpdater entityUpdater) {
        this.entityUpdater = entityUpdater;
    }
    
}
