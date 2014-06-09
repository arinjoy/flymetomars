package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;
import java.util.Date;
import flymetomars.common.datatransfer.Invitation.InvitationStatus;

/**
 * A representative class for an Invitation by one Person to another Person for
 * a particular Mission.  This Invitation may then be accepted, declined, etc.
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public class InvitationImpl extends SeriablizableEntityImpl<Invitation> implements EqualableStructure<Invitation>, Invitation {

    private Mission mission;
    private Person creator;
    private Person recipient;
    private Date lastUpdated;
    private InvitationStatus status;

    /**
     * default constructor
     */
    public InvitationImpl() {}

    
    /**
     * Property accessor for the `mission' component of an Invitation
     * 
     * @return Mission that the creator is inviting recipient to
     */
    @Override
    public Mission getMission() {
        return mission;
    }

    /**
     * Property mutator for the `mission' component of an Invitation
     * 
     * @param mission Mission intended for inviting the recipient to
     */
    @Override
    public void setMission(Mission mission) {
        if (null == mission) {
            throw new NullArgumentException("Invitation mission does not permit null");
        }
        this.mission = mission;
    }

    /**
     * Property accessor for the `creator' component of an Invitation
     * 
     * @return Person whom created the invitation
     */
    @Override
    public Person getCreator() {
        return creator;
    }

    /**
     * Property mutator for the `creator' component of an Invitation
     * 
     * @param creator Person whom created the invitation
     */
    @Override
    public void setCreator(Person creator) {
        if (null == creator) {
            throw new NullArgumentException("Invitation cannot have null creator");
        }
        this.creator = creator;
    }

    /**
     * Property accessor for the `status' component of an Invitation
     * 
     * @return InvitationStatus enumeration value for this Invitation's status
     */
    @Override
    public InvitationStatus getStatus() {
        return status;
    }
    
    /**
     * Property mutator for the `status' component of an Invitation
     * 
     * @param status InvitationStatus enumeration value desired for this invite
     */
    @Override
    public void setStatus(InvitationStatus status) {
        if (null == status) {
            throw new NullArgumentException("Invitation status field cannot be null");
        }
        this.status = status;
    }

    /**
     * Property accessor for the `lastUpdated' component of an Invitation
     *
     * @return Date intended to as the most recent update time of Invitation
     */
    @Override
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Property mutator for the `lastUpdated' component of an Invitation
     * 
     * @param lastUpdated Date intended to as most recent update of Invitation
     */
    @Override
    public void setLastUpdated(Date lastUpdated) {
        if (null == lastUpdated) {
            throw new NullArgumentException("Invitation last updated field cannot be null");
        }
        this.lastUpdated = lastUpdated;
    }

    /**
     * Property accessor for the `recipient' component of an Invitation
     * 
     * @return Person to whom this invitation is addressed and intended
     */
    @Override
    public Person getRecipient() {
        return recipient;
    }

    /**
     * Property mutator for the `recipient' component of an Invitation
     * 
     * @param recipient Person desired to recieve this invitation
     */
    @Override
    public void setRecipient(Person recipient) {
        if (null == recipient) {
            throw new NullArgumentException("Invitation cannot have null recipient field");
        }
        this.recipient = recipient;
    }

    /**
     * 
     * 
     * @param inv
     * @return 
     */
    @Override
    public boolean equalsDTO(Invitation inv) {
        if(!this.status.equals(inv.getStatus())) { return false; }
        if(!this.lastUpdated.equals(inv.getLastUpdated())) { return false; }
        if(!this.mission.equals(inv.getMission())) { return false; }
        if(!this.creator.equals(inv.getCreator())) { return false; }
        if(!this.recipient.equals(inv.getRecipient())) { return false; }
        return true;
    }
    
    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = (2*2)+2+1, bits=hash+2+(2*2*2);
        hash = bits * hash + (this.mission != null ? this.mission.hashCode() : 0);
        hash = bits * hash + (this.creator != null ? this.creator.hashCode() : 0);
        hash = bits * hash + (this.recipient != null ? this.recipient.hashCode() : 0);
        hash = bits * hash + (this.lastUpdated != null ? this.lastUpdated.hashCode() : 0);
        hash = bits * hash + (this.status != null ? this.status.hashCode() : 0);
        return hash;
    }

    /**
     * Standard Java Object.equals(Object) method signature override for sorting
     * 
     * @param obj Object to be checked for equality with this object
     * @return boolean value indicating if the representations contained within
     * this object and obj are effectively equivalent (thus equal for sorting).
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.equalsDTO((Invitation)obj);
    }
}
