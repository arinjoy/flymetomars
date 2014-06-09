package flymetomars.dataaccess.entities;

import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PersonEntity is responsible for representing strongly-typed and
 * strongly-coupled `Person' data within the Data Access Layer (DAL).
 * 
 *  As a member of the Entity package, this class provides a persistable POJO for the Hibernate Mapper
 * 
 * @author Lawrence Colman
 */
public class PersonEntity {

    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private /*SaltedPasswordEntity*/Long password;
    private Set<ExpertiseEntity> expertiseGained;
    private Set<MissionEntity> missionsRegistered;
    private Set<InvitationEntity> invitationsReceived;
    private Long id;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();

    /**
     * Default constructor
     */
    public PersonEntity() {}

    /**
     * Person DTO to Entity class conversion utility method
     * 
     * @param who Person data transfer object (DTO) to convert to Entity
     * @return A Person Entity instance representing the DTO passed to method
     */
    public static PersonEntity fromDTO(Person who) {
        if(null==who) { return (PersonEntity)null; }
        PersonEntity result = new PersonEntity();
        result.setFirstName(who.getFirstName());
        result.setLastName(who.getLastName());
        result.setUserName(who.getUserName());
        result.setPassword(who.getPassword());
        result.setEmail(who.getEmail());
        result.setMissionsRegistered(AbstractEntityUtil.toEntitySet(who.getMissionsRegistered(),MissionEntity.class));
        result.setInvitationsReceived(AbstractEntityUtil.toEntitySet(who.getInvitationsReceived(),InvitationEntity.class));
        result.setExpertiseGained(AbstractEntityUtil.toEntitySet(who.getExpertiseGained(),ExpertiseEntity.class));
        result.setId(who.getId());
        return result;
    }

    /**
     * Person Entity transfer encoding conversion utility method
     * 
     * @return A Person data transfer object (DTO) representing this Entity
     */
    public Person toDTO() {
        Person result;
        try {
            result=this.dtoFactory.createDTO(Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PersonEntity.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new UnsupportedOperationException(ex);
        }
        if(null!=this.getFirstName()) { result.setFirstName(this.getFirstName()); }
        if(null!=this.getLastName()) { result.setLastName(this.getLastName()); }
        if(null!=this.getUserName()) { result.setUserName(this.getUserName()); }
        //if(null!=this.getPassword()) { result.setPassword(this.getPassword()==null?null:this.getPassword().toDTO()); }
        if(null!=this.getPassword()) { result.setPassword(this.getPassword()); }
        if(null!=this.getEmail()) { result.setEmail(this.getEmail()); }
        result.setMissionsRegistered(AbstractEntityUtil.fromEntitySet(this.getMissionsRegistered(),Mission.class));
        result.setInvitationsReceived(AbstractEntityUtil.fromEntitySet(this.getInvitationsReceived(),Invitation.class));
        result.setExpertiseGained(AbstractEntityUtil.fromEntitySet(this.getExpertiseGained(),Expertise.class));
        if(null!=this.getId()) { result.setId(this.getId()); }
        return result;
    }

    /**
     * Convenience method to populate this PersonEntity with values from another
     * 
     * @param who The alternate PersonEntity instance to copy the values of
     * @return a reference to "this" current entity - scoped method syntax
     */
    public PersonEntity copyValues(PersonEntity who) {
        this.setFirstName(who.getFirstName());
        this.setLastName(who.getLastName());
        this.setUserName(who.getUserName());
        this.setPassword(who.getPassword());
        this.setEmail(who.getEmail());
        this.setMissionsRegistered(who.getMissionsRegistered());
        this.setInvitationsReceived(who.getInvitationsReceived());
        this.setExpertiseGained(who.getExpertiseGained());
        this.setId(who.getId());
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
       this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public /*SaltedPasswordEntity*/Long getPassword() {
        return password;
    }
    
    public void setPassword(/*SaltedPasswordEntity*/Long password) {
        this.password = password;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<MissionEntity> getMissionsRegistered() {
        return missionsRegistered;
    }

    public void setMissionsRegistered(Set<MissionEntity> missionsRegistered) {
        this.missionsRegistered = missionsRegistered;
    }

    public Set<InvitationEntity> getInvitationsReceived() {
        return invitationsReceived;
    }

    public void setInvitationsReceived(Set<InvitationEntity> invitationsReceived) {
        this.invitationsReceived = invitationsReceived;
    }

    public Set<ExpertiseEntity> getExpertiseGained() {
        return expertiseGained;
    }

    public void setExpertiseGained(Set<ExpertiseEntity> expertiseGained) {
        this.expertiseGained = expertiseGained;
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
        result.append(this.firstName);
        result.append(' ');
        result.append(this.lastName);
        result.append(" @ ");
        result.append(this.getEmail());
        result.append(')');
        return result.toString();
    }

}
