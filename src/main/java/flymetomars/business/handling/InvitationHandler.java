package flymetomars.business.handling;

import flymetomars.business.model.Invitation;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Invitation.InvitationStatus;
import java.util.Calendar;
import java.util.Date;

/**
 * InvitationHandler Complex Business Logic implementation Class for Invitations
 * 
 * As a handling package a member  this class implements complex business logic
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
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
public class InvitationHandler {
    
    //<editor-fold defaultstate="collapsed" desc="Date utility code">
    private Calendar calUtil=Calendar.getInstance();
    
    protected Date now() {
        return calUtil.getTime();
    }
    //</editor-fold>

    /**
     * Check the business logic for sending an invitation
     * 
     * @param invitation Invitation model object to add/send for a person
     * @param person The person model object who will be sent the invitation
     * @throws NullArgumentException thrown when either argument is given a null
     * @throws IllegalArgumentException thrown when the supplied `invitation' is
     *          not correctly addressed to the supplied `recipient' Person
     */
    public void sendInvitation(Invitation invitation, Person recipient) {
        if(null==invitation) {
            throw new NullArgumentException("Cannot sendInvitation that is null");
        }
        if(null==recipient) {
            throw new NullArgumentException("Cannot sendInvitation to null null recipient");
        }
        if(null==invitation.getRecipient() || null==invitation.getRecipient().getId() || invitation.getRecipient().getId().equals(recipient.getId())) {
            invitation.setRecipient(recipient);
            recipient.getInvitationsReceived().add(invitation);
            invitation.setStatus(InvitationStatus.SENT);
            invitation.setLastUpdated(now());
        } else {
            throw new IllegalArgumentException("Invitation not addressed to person:".concat(recipient.toString()));
        }
    }

    /**
     * Check the business logic for accepting an invitation
     * 
     * @param invitation Invitation model object to accept for a person
     * @param person The person model object who will be accept the invitation
     * @throws NullArgumentException thrown when either argument is given a null
     * @throws IllegalArgumentException thrown when the supplied `invitation' is
     *          not correctly addressed to the supplied `recipient' Person
     */
    public void acceptInvitation(Invitation invitation, Person person) {
        if(null==invitation) {
            throw new NullArgumentException("Cannot acceptInvitation that is null");
        }
        if(null==person) {
            throw new NullArgumentException("Cannot acceptInvitation for null prson");
        }
        /*if (!person.getInvitationsReceived().contains(invitation)) {
            throw new IllegalArgumentException("Invitation not addressed to person:".concat(person.toString()));
        }*/
        invitation.getMission().getParticipantSet().add(person);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setLastUpdated(Calendar.getInstance().getTime());
    }

     /**
     * Check the business logic for rejecting an invitation
     * 
     * @param invitation Invitation model object to reject for a person
     * @param person The person model object who will be reject the invitation
     * @throws NullArgumentException thrown when either argument is given a null
     * @throws IllegalArgumentException thrown when the supplied `invitation' is
     *          not correctly addressed to the supplied `recipient' Person
     */
    public void rejectInvitation(Invitation invitation, Person person) {
        if(null==invitation) {
            throw new NullArgumentException("Cannot rejectInvitation that is null");
        }
        if(null==person) {
            throw new NullArgumentException("Cannot rejectInvitation for null prson");
        }
        /*if (!person.getInvitationsReceived().contains(invitation)) {
            throw new IllegalArgumentException("Invitation not meant for the person: ".concat(person.toString()));
        }*/
        invitation.getMission().getParticipantSet().remove(person);
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setLastUpdated(now());
    }
    
}
