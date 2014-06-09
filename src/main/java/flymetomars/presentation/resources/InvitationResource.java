package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.InvitationHandler;
import org.restlet.resource.Get;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Person;
import flymetomars.common.datatransfer.Invitation.InvitationStatus;
import flymetomars.presentation.webservices.util.FormHandler;
import org.restlet.Request;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.resource.Post;

/**
 * 
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class InvitationResource extends FreemarkerResource {
    private static final int HOME_INVITATION_INDEX = 2;

    private Invitation invitation;
    private FormHandler formHandler;
    private EntityUpdater entityUpdater;
    private InvitationHandler invitationHandler;
    
    @Override
    public void doInit() throws ResourceException {
        dataModel = new HashMap<String, Object>();
        exceptionHandler.setDataModel(dataModel);
        setContentTemplateName("invitation.html.ftl");
        String invId = getRequestedInvitationName(getRequest());
        try {
            invitation = entityLoader.loadInvitationById(Long.parseLong(invId));
            //if(null==invitation) { throw new DataAccessException("dummy"){}; }
            super.doInit();
        } catch (DataAccessException e) {
            final String msg = "Error loading invitation: " + invId;
            exceptionHandler.displayErrorOnScreen(msg);
        }
    }

    private String getRequestedInvitationName(Request request) {
        List<String> segments = request.getResourceRef().getSegments();
        return segments.get(HOME_INVITATION_INDEX);
    }

    @Override
    protected boolean authorizeGet() {
        return true;
    }

    @Override
    @Get("html")
    public Representation doAuthenticatedGet(Variant variant) throws ResourceException {
        dataModel.put("authenticatedUser",authenticatedUser);
        if (null == invitation) {
            exceptionHandler.displayErrorOnScreen("Cannot find invitation with id: " + getRequestedInvitationName(this.getRequest()));
        } else {
            dataModel.put("invitation", invitation);
            // find the receipient of this invitation
            Person recipient = invitation.getRecipient();
            dataModel.put("recipient", recipient);
        }
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
        if(null==invitation) {
            errorMap.put("errorMessage", "Invalid or non-existant invitation requested for status update");
        }
        if (errorMap.isEmpty()) {
            String choice=formHandler.getFirstFieldValue("newStatus");
            if("ACCEPTED".equals(choice.toUpperCase())) {
                try {
                    invitationHandler.acceptInvitation(invitation, invitation.getRecipient());
                } catch (IllegalArgumentException e) {
                        errorMap.put("errorMessage", e.getMessage());
                    }
                //invitation.setStatus(InvitationStatus.ACCEPTED);
                //invitation.getMission().getParticipantSet().add(authenticatedUser);
                //authenticatedUser.getMissionsRegistered().add(invitation.getMission());
            } else if("DECLINED".equals(choice.toUpperCase())) {
                try {
                    invitationHandler.rejectInvitation(invitation, invitation.getRecipient());
                } catch (IllegalArgumentException e) {
                        errorMap.put("errorMessage", e.getMessage());
                    }
                //invitation.setStatus(InvitationStatus.DECLINED);
                //invitation.getMission().getParticipantSet().remove(authenticatedUser);
                //authenticatedUser.getMissionsRegistered().remove(invitation.getMission());
            } else if("SENT".equals(choice.toUpperCase())) {
                if(!invitation.getStatus().equals(InvitationStatus.CREATED)) {
                    errorMap.put("errorMessage", "Invitation has akready been sent!");
                } else {
                    try {
                        invitationHandler.sendInvitation(invitation, invitation.getRecipient());
                    } catch (IllegalArgumentException e) {
                        errorMap.put("errorMessage", e.getMessage());
                    }
                    //invitation.setStatus(InvitationStatus.SENT);
                }
            } else {
                errorMap.put("errorMessage", "Invalid invitation operation recieved: " + choice);
            }
            if (errorMap.isEmpty()) {
                try {
                    //invitation.setRecipient(entityUpdater.updatePerson(invitation.getRecipient()));
                    //invitation.setMission(entityUpdater.updateMission(invitation.getMission()));
                    invitation=entityUpdater.updateInvitation(invitation);
                } catch (DependencyException ex) {
                    String msg="Unable to " + choice.toLowerCase() +" invitation";
                    Logger.getLogger(InvitationResource.class.getName()).log(Level.CONFIG, msg, ex);
                    errorMap.put("errorMessage", new StringBuilder(msg).append(": ").append(ex.getMessage()).toString());
                }
            }
        }
        // add error messages to the screen
        if (!errorMap.isEmpty()) {
            dataModel.putAll(errorMap);
        }
        return get(variant);
    }

    public void setEntityUpdater(EntityUpdater entityUpdater) {
        this.entityUpdater = entityUpdater;
    }

    public void setFormHandler(FormHandler formHandler) {
        this.formHandler = formHandler;
    }
    
    public void setInvitationHandler(InvitationHandler invitationHandler) {
        this.invitationHandler = invitationHandler;
    }
    
}
