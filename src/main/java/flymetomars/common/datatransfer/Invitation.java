package flymetomars.common.datatransfer;

import java.util.Date;

/**
 * A representative class for an Invitation by one Person to another Person for
 * a particular Mission.  This Invitation may then be accepted, declined, etc.
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 */
public interface Invitation extends SeriablizableEntity<Invitation> {

    /**
     * The enumeration type for Invitation status
     */
    public enum InvitationStatus {

        SENT("sent"),
        CREATED("created"),
        ACCEPTED("accepted"),
        DECLINED("declined");
        private String name;

        private InvitationStatus(String name) {
            this.name = name;
        }

        /**
         * Basic toString override method for inner enumeration name retrieval
         * 
         * @return the String name of this particular enumeration value
         */
        @Override
        public String toString() {
            return this.name;
        }
    }

    
    /**
     * Property accessor for the `mission' component of an Invitation
     * 
     * @return Mission that the creator is inviting recipient to
     */
    Mission getMission();
    
    /**
     * Property mutator for the `mission' component of an Invitation
     * 
     * @param mission Mission intended for inviting the recipient to
     */
    void setMission(Mission mission);

    /**
     * Property accessor for the `creator' component of an Invitation
     * 
     * @return Person whom created the invitation
     */
    Person getCreator();
    
    /**
     * Property mutator for the `creator' component of an Invitation
     * 
     * @param creator Person whom created the invitation
     */
    void setCreator(Person creator);

    /**
     * Property accessor for the `status' component of an Invitation
     * 
     * @return InvitationStatus enumeration value for this Invitation's status
     */
    InvitationStatus getStatus();
    
    /**
     * Property mutator for the `status' component of an Invitation
     * 
     * @param status InvitationStatus enumeration value desired for this invite
     */
    void setStatus(InvitationStatus status);
    
    /**
     * Property accessor for the `lastUpdated' component of an Invitation
     *
     * @return Date intended to as the most recent update time of Invitation
     */
    Date getLastUpdated();
    
    /**
     * Property mutator for the `lastUpdated' component of an Invitation
     * 
     * @param lastUpdated Date intended to as most recent update of Invitation
     */
    void setLastUpdated(Date lastUpdated);
    
    /**
     * Property accessor for the `recipient' component of an Invitation
     * 
     * @return Person to whom this invitation is addressed and intended
     */
    Person getRecipient();
    
    /**
     * Property mutator for the `recipient' component of an Invitation
     * 
     * @param recipient Person desired to recieve this invitation
     */
    void setRecipient(Person recipient);

}
