package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.Date;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MissionEntity is responsible for representing strongly-typed and
 * strongly-coupled `Mission' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class MissionEntity {

    private Date time;
    private String name;
    private PersonEntity captain;
    private LocationEntity location;
    private String description;
    private Set<PersonEntity> participantSet;
    private Set<InvitationEntity> invitationSet;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    /**
     * Default constructor
     */
    public MissionEntity() {}

    /**
     * Mission DTO to Entity class conversion utility method
     * 
     * @param miss Mission data transfer object (DTO) to convert to Entity
     * @return A Mission Entity instance representing the DTO passed to method
     */
    public static MissionEntity fromDTO(Mission miss) { return fromDTO(miss,false); }
    protected static MissionEntity fromDTO(Mission miss, boolean circularWithInvitation) {
        if(null==miss) { return (MissionEntity)null; }
        MissionEntity result = new MissionEntity();
        result.setName(miss.getName());
        result.setTime(miss.getTime());
        result.setLocation(LocationEntity.fromDTO(miss.getLocation()));
        result.setDescription(miss.getDescription());
        if(!circularWithInvitation){ result.setInvitationSet(AbstractEntityUtil.toEntitySet(miss.getInvitationSet(),InvitationEntity.class)); }
        result.setParticipantSet(AbstractEntityUtil.toEntitySet(miss.getParticipantSet(),PersonEntity.class));
        result.setCaptain(PersonEntity.fromDTO(miss.getCaptain()));
        result.setId(miss.getId());
        return result;
    }

    /**
     * Mission Entity transfer encoding conversion utility method
     * 
     * @return A Mission data transfer object (DTO) representing this Entity
     */
    public Mission toDTO() {
        Mission result;
        try {
            result=this.dtoFactory.createDTO(Mission.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MissionEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setName(this.getName());
        result.setTime(this.getTime());
        result.setLocation(this.getLocation()==null?null:this.getLocation().toDTO());
        result.setDescription(this.getDescription());
        result.setInvitationSet(null==this.getInvitationSet()?null:AbstractEntityUtil.fromEntitySet(this.getInvitationSet(),Invitation.class));
        result.setParticipantSet(AbstractEntityUtil.fromEntitySet(this.getParticipantSet(),Person.class));
        result.setCaptain(this.getCaptain()==null?null:this.getCaptain().toDTO());
        result.setId(this.getId());
        return result;
    }
    
    /**
     * Convenience method to populate this MissionEntity with values from another
     * 
     * @param miss The alternate MissionEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public MissionEntity copyValues(MissionEntity miss) {
        this.setName(miss.getName());
        this.setTime(miss.getTime());
        this.setLocation(miss.getLocation());
        this.setDescription(miss.getDescription());
        this.setInvitationSet(miss.getInvitationSet());
        this.setParticipantSet(miss.getParticipantSet());
        this.setCaptain(miss.getCaptain());
        this.setId(miss.getId());
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InvitationEntity> getInvitationSet() {
        return invitationSet;
    }

    public void setInvitationSet(Set<InvitationEntity> invitationSet) {
        this.invitationSet = invitationSet;
    }

    public Set<PersonEntity> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(Set<PersonEntity> participantSet) {
        this.participantSet = participantSet;
    }

    public PersonEntity getCaptain() {
        return captain;
    }

    public void setCaptain(PersonEntity captain) {
        this.captain = captain;
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
        result.append("-(");
        result.append(this.getName());
        result.append(")");
        return result.toString();
    }
    
}
