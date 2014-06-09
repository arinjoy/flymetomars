package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * InvitationEntity is responsible for representing strongly-typed and
 * strongly-coupled `Invitation' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class InvitationEntity {

    private MissionEntity mission;
    private PersonEntity creator;
    private PersonEntity recipient;
    private Date lastUpdated;
    private Invitation.InvitationStatus status;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    /**
     * Default constructor
     */
    public InvitationEntity() {}

    /**
     * Invitation DTO to Entity class conversion utility method
     * 
     * @param inv Invitation data transfer object (DTO) to convert to Entity
     * @return An Invitation Entity instance representing the DTO passed to method
     */
    public static InvitationEntity fromDTO(Invitation inv) { return fromDTO(inv, false); }
    protected static InvitationEntity fromDTO(Invitation inv, boolean circularWithMission) {
        if(null==inv) { return (InvitationEntity)null; }
        InvitationEntity result = new InvitationEntity();
        result.setMission(MissionEntity.fromDTO(inv.getMission(),true));  //don't load circulars
        if(circularWithMission){ result.getMission().setInvitationSet(Collections.singleton(result)); }  //resolve circulars
        result.setCreator(PersonEntity.fromDTO(inv.getCreator()));
        result.setStatus(inv.getStatus());
        result.setLastUpdated(inv.getLastUpdated());
        result.setRecipient(PersonEntity.fromDTO(inv.getRecipient()));
        result.setId(inv.getId());
        return result;
    }

    /**
     * Invitation Entity transfer encoding conversion utility method
     * 
     * @return An Invitation data transfer object (DTO) representing this Entity
     */
    public Invitation toDTO() { return this.toDTO(true,true); }
    public Invitation toDTO(boolean skipCap, boolean skipRec) {
        Invitation result;
        try {
            result=this.dtoFactory.createDTO(Invitation.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InvitationEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setMission(this.getMission()==null?null:this.getMission().toDTO());
        if(!skipCap) { result.setCreator(this.getCreator()==null?null:this.getCreator().toDTO()); }
        result.setStatus(this.getStatus());
        result.setLastUpdated(this.getLastUpdated());
        if(!skipRec) { result.setRecipient(this.getRecipient()==null?null:this.getRecipient().toDTO()); }
        result.setId(this.getId());
        return result;
    }
    
    /**
     * Convenience method to populate this InvitationEntity with values from another
     * 
     * @param inv The alternate InvitationEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public InvitationEntity copyValues(InvitationEntity inv) {
        this.setMission(inv.getMission());
        this.setCreator(inv.getCreator());
        this.setStatus(inv.getStatus());
        this.setLastUpdated(inv.getLastUpdated());
        this.setRecipient(inv.getRecipient());
        this.setId(inv.getId());
        return this;
    }

    public MissionEntity getMission() {
        return mission;
    }

    public void setMission(MissionEntity mission) {
        this.mission = mission;
    }

    public PersonEntity getCreator() {
        return creator;
    }

    public void setCreator(PersonEntity creator) {
        this.creator = creator;
    }

    public Invitation.InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(Invitation.InvitationStatus status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public PersonEntity getRecipient() {
        return recipient;
    }

    public void setRecipient(PersonEntity recipient) {
        this.recipient = recipient;
    }

    /**
     * Property accessor for Entity objects encapsulating domain objects with ids
     * 
     * @return the unique Long Integer identifier code for this Entity instance
     */
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id=id;
    }

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder(this.getClass().getCanonicalName());
        result.append('#');
        result.append(this.getId());
        return result.toString();
    }
    
}
