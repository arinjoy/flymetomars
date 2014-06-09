package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
//import flymetomars.common.validation.DateValidator;
import flymetomars.common.validation.StringValidator;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * A business logic model for the expertise of a person
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */
public class Invitation extends IdentifiableModel<Invitation> implements TransferableModel<flymetomars.common.datatransfer.Invitation> {

    private Mission mission;
    private Person creator;
    private Person recipient;
    private Date lastUpdated;
    private flymetomars.common.datatransfer.Invitation.InvitationStatus status;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.Invitation> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.Invitation>(
            flymetomars.common.datatransfer.Invitation.class, dtoFactory
    );

    /**
     * The default invitation constructor
     */
    public Invitation() {
        this.lastUpdated = new Date();
        this.status = flymetomars.common.datatransfer.Invitation.InvitationStatus.CREATED;
    }
    
     /**
     * Copy constructor that takes a DTO to a Invitation model object
     * 
     * @param dto Data Transfer Object representing a Invitation
     */
    public Invitation(flymetomars.common.datatransfer.Invitation dto) {
        this();
        if(null!=dto.getMission()) { this.setMission(new Mission(dto.getMission())); }
        if(null!=dto.getCreator()) { this.setCreator(new Person(dto.getCreator())); }
        this.setStatus(dto.getStatus());
        this.setLastUpdated(dto.getLastUpdated());
        if(null!=dto.getRecipient()) { this.setRecipient(new Person(dto.getRecipient())); }
        this.setId(dto.getId());
    }
    
    @Override
    public final flymetomars.common.datatransfer.Invitation toDTO() { return this.toDTO(null); }
    flymetomars.common.datatransfer.Invitation toDTO(flymetomars.common.datatransfer.Person re) {
        flymetomars.common.datatransfer.Invitation result=this.iDTO();
        if(null!=this.getMission()) { result.setMission(this.getMission().toDTO()); }
        if(null!=this.getCreator() && this.getCreator()==this.getRecipient()) {
            if(null!=re) { result.setCreator(re); }
        } else {
            if(null!=this.getCreator()) { result.setCreator(this.getCreator().toDTO()); }
        }
        result.setStatus(this.getStatus());
        result.setLastUpdated(this.getLastUpdated());
        if(null==re && null!=this.getRecipient()) {
            result.setRecipient(this.getRecipient().toDTO());
        } else if (null!=re) { result.setRecipient(re); }
        if(null!=this.getId()) { result.setId(this.getId()); }
        return result;
    } private flymetomars.common.datatransfer.Invitation iDTO() {
        try {
            return this.dtoFactory.createDTO(flymetomars.common.datatransfer.Invitation.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Invitation.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Method to get the mission name of the invitation
     * @return
     */
    public Mission getMission() {
        return mission;
    }

    /**
     * Method to set the mission name of the invitation
     * @param mission
     */
    public final void setMission(Mission mission) {
        if (null == mission) {
            throw new NullArgumentException("Invitation mission does not permit null");
        }
        StringValidator.validateStringNotEmpty(mission.getName()); //mission name cannot be empty
        if (null == mission.getCaptain()) {
            throw new NullArgumentException("Invitation Mission cannot have null captain");
        }
        this.mission = mission;
    }

    /**
     * Method to get the creator of the invitation
     * @return
     */
    public Person getCreator() {
        return creator;
    }

    /**
     * Method to set the creator of the invitation
     * @param creator
     */
    public final void setCreator(Person creator) {
        if (null == creator) {
            throw new NullArgumentException("Invitation cannot have null creator");
        }
        this.creator = creator;
    }

    /**
     * Method to get the invitation status of the invitation
     * @return
     */
    public flymetomars.common.datatransfer.Invitation.InvitationStatus getStatus() {
        return status;
    }

    /**
     * Method to set the invitation status of the invitation
     * @param status
     */
    public final void setStatus(flymetomars.common.datatransfer.Invitation.InvitationStatus status) {
        if (null == status) {
            throw new NullArgumentException("Invitation status field cannot be null");
        }
        this.status = status;
    }

    /**
     * Method to get the last update date of the invitation
     * @return
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Method to set the last update date of the invitation
     * @param lastUpdated
     */
    public final void setLastUpdated(Date lastUpdated) {
        /* Old Validation (non-JS):
        DateValidator.validatePastDate(lastUpdated); //validating whether updated date was in past or not
        */
        flymetomars.common.datatransfer.Invitation dto;
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Invitation.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaltedPassword.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setLastUpdated(lastUpdated);
        logic.applyRules(this.getClass().getSimpleName(),"lastUpdated",dto);
        this.lastUpdated = lastUpdated;
    }

    /**
     * Method to get the recipient of the invitation
     * @return
     */
    public Person getRecipient() {
        return recipient;
    }

    /**
     * Method to set the recipient of the invitation
     * @param recipient
     */
    public final void setRecipient(Person recipient) {
        if (null == recipient) {
            throw new NullArgumentException("Invitation cannot have null recipient field");
        }
        this.recipient = recipient;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.equalsModel((Invitation) obj);
        } catch (ClassCastException cc) {
            return super.equals(obj);
        }
    }

    @Override
    public boolean equalsModel(Invitation inv) {
        if(!this.status.equals(inv.getStatus())) { return false; }
        if(!this.lastUpdated.equals(inv.getLastUpdated())) { return false; }
        if(!this.mission.equalsModel(inv.getMission())) { return false; }
        if(!this.creator.equalsModel(inv.getCreator())) { return false; }
        if(!this.recipient.equalsModel(inv.getRecipient())) { return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 2+1+2+2,bits=(2+2+2+2+2)+hash;
        hash = bits * hash + (this.mission != null ? this.mission.hashCode() : 0);
        hash = bits * hash + (this.creator != null ? this.creator.hashCode() : 0);
        hash = bits * hash + (this.recipient != null ? this.recipient.hashCode() : 0);
        hash = bits * hash + (this.lastUpdated != null ? this.lastUpdated.hashCode() : 0);
        hash = bits * hash + (this.status != null ? this.status.hashCode() : 0);
        return hash;
    }
}
