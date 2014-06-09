package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import flymetomars.common.validation.StringValidator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * As a member of the model package, this class implements business logic.
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 */
public class Mission extends IdentifiableModel<Mission> implements TransferableModel<flymetomars.common.datatransfer.Mission> {


    private Date time;
    private String name;
    private Person captain;
    private Location location;
    private String description;
    private Set<Person> participantSet = new HashSet<Person>();
    private Set<Invitation> invitationSet = new HashSet<Invitation>();
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.Mission> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.Mission>(
            flymetomars.common.datatransfer.Mission.class, dtoFactory
    );

    /**
     * Default mission constructor which initializes the class variables
     */
    public Mission() {
        this.name = "";
        this.description = "";
        this.location = null;
        this.time = null;
    }
    
     /**
     * Copy constructor that takes a DTO to a Mission model object
     * 
     * @param dto Data Transfer Object representing a Mission 
     */
    public Mission(flymetomars.common.datatransfer.Mission dto) {
        this();
        this.copyFromDTO(dto);
    }
    private void copyFromDTO(flymetomars.common.datatransfer.Mission dto) {
        /*this.setName(dto.getName());
        this.setTime(dto.getTime());
        this.setLocation(new Location(dto.getLocation()));
        this.setDescription(dto.getDescription());*/
        String nam=null, desc=null;
        Date tim=null;
        flymetomars.common.datatransfer.Location locatio=null;
        /*
         * Declare local variables for each simple field, name, time, description
         * Use reflection to populate local variables from (non-final) DTO getters
         * Replace getter calls in local setter calls to use local variable refs
         */
        RuntimeException err=null;
        try {
            nam = (String)dto.getClass().getMethod("getName", new Class<?>[]{}).invoke(dto);
            tim = (Date)dto.getClass().getMethod("getTime", new Class<?>[]{}).invoke(dto);
            Method lm=dto.getClass().getMethod("getLocation", new Class<?>[]{});
            locatio = (flymetomars.common.datatransfer.Location)lm.invoke(dto);
            desc = (String)dto.getClass().getMethod("getDescription", new Class<?>[]{}).invoke(dto);
            this.getClass().getMethod("setName", new Class<?>[]{String.class}).invoke(this, nam);
            this.getClass().getMethod("setDescription", new Class<?>[]{String.class}).invoke(this, desc);
        } catch (IllegalAccessException e) { err=new UnsupportedOperationException(e); }    
        catch(IllegalArgumentException e) { err=new UnsupportedOperationException(e); }
        catch (SecurityException e) { err=new UnsupportedOperationException(e); }
        catch (NoSuchMethodException e) { err=new UnsupportedOperationException(e); }
        catch (InvocationTargetException e) { err=new UnsupportedOperationException(e); }
        if(null!=err) { throw err; }
        //set the things
        //this.setName(nam);
        this.setTime(tim);
        this.setLocation(new Location(locatio));
        //this.setDescription(desc);
        //business as usual
        Set<Invitation> invs=new HashSet<Invitation>();
        for(flymetomars.common.datatransfer.Invitation inv : dto.getInvitationSet()) {
            invs.add(new Invitation(inv));
        }
        this.invitationSet=invs;
        Set<Person> pers=new HashSet<Person>();
        for(flymetomars.common.datatransfer.Person per : dto.getParticipantSet()) {
            pers.add(new Person(per));
        }
        this.participantSet=pers;
        this.setCaptain(new Person(dto.getCaptain()));
        this.setId(dto.getId());
    }
    
     /**
     * To convert a Mission model object to a dto
     * 
     * @return
     */
    @Override
    public final flymetomars.common.datatransfer.Mission toDTO() {
        return this.toDTO(null);
        } final flymetomars.common.datatransfer.Mission toDTO(Person ignore) {
        flymetomars.common.datatransfer.Mission result;
        try {
            result=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Mission.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Mission.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        result.setName(this.getName());
        if(null!=this.getTime()) { result.setTime(this.getTime()); }
        if(null!=this.getLocation()) { result.setLocation(this.getLocation().toDTO()); }
        result.setDescription(this.getDescription());
        Set<flymetomars.common.datatransfer.Invitation> invs = new HashSet<flymetomars.common.datatransfer.Invitation>();
        for (Invitation i : this.getInvitationSet()) {
            invs.add(i.toDTO());
        }
        result.setInvitationSet(invs);
        Set<flymetomars.common.datatransfer.Person> pers = new HashSet<flymetomars.common.datatransfer.Person>();
        for (Person p : this.getParticipantSet()) {
            if(null!=ignore) {//begin circular reference avoidance dodge
                if(null!=ignore.getId()) {
                    if(null!=p.getId() && p.getId().equals(ignore.getId())) { continue; }
                } else {
                    if(p.equals(ignore)) { continue; }
                }
            }//end circular reference avoidance dodge
            pers.add(p.toDTO());
        }
        result.setParticipantSet(pers);
        if(null!=this.getCaptain()) { result.setCaptain(this.getCaptain().toDTO()); }
        result.setId(this.getId());
        return result;
    }

    /**
     * Method to get the name of the mission
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name of the mission
     * 
     * @param name
     */
    public final void setName(String name) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(name, 5, 50);
        StringValidator.validateStringContainsOnly(name, " '_.,-()!" + StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS);
        */
        flymetomars.common.datatransfer.Mission dto=this.toDTO(null);
        dto.setName(name);
        logic.applyRules(this.getClass().getSimpleName(),"name",dto);
        this.name = name;
    }

    /**
     * Method to get the time of the mission
     * 
     * @return
     */
    public Date getTime() {
        return time;
    }

    /**
     * Method to set the time of the mission
     * 
     * @param time
     */
    public final void setTime(Date time) {
        this.time = time;
    }

    /**
     * Method to get the location of the mission
     * 
     * @return
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Method to set the location of the mission
     * 
     * @param location
     */
    public final void setLocation(Location location) {
        if (null == location) {
            throw new NullArgumentException("cannot set location to null");
        }
        this.location = location;
    }

    /**
     * Method to get the description of the mission
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to set the description of the mission
     * 
     * @param description
     */
    public final void setDescription(String description) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(description, 25, 300);
        StringValidator.validateStringContainsNo(description, Character.toString((char) 0) + "<>");  //blackbox
        */
        flymetomars.common.datatransfer.Mission dto=this.toDTO(null);
        dto.setDescription(description);
        logic.applyRules(this.getClass().getSimpleName(),"description",dto);
        this.description = description;
    }

    /**
     * Method to get the set of invitations of the mission
     * 
     * @return
     */
    public Set<Invitation> getInvitationSet() {
        return invitationSet;
    }

    /**
     * Method to set the invitations of the mission
     * 
     * @param invitationSet
     */
    public void setInvitationSet(Set<Invitation> invitationSet) {
        if (null == invitationSet) {
            throw new NullArgumentException("cannot set invitations to null");
        }
        this.invitationSet = invitationSet;
    }

    /**
     * Method to get the participants of the mission
     * 
     * @return
     */
    public Set<Person> getParticipantSet() {
        return participantSet;
    }

    /**
     * Method to set the participants of the mission
     * 
     * @param participantSet
     */
    public void setParticipantSet(Set<Person> participantSet) {
        if (null == participantSet) {
            throw new NullArgumentException("cannot set participants to null");
        }
        for (Person participant : participantSet) {
            if (null == participant) {
                throw new NullArgumentException("Mission cannot have null participant in set");
            }
        }
        this.participantSet = participantSet;
    }

    /**
     * Method to get the captain of the mission
     * 
     * @return
     */
    public Person getCaptain() {
        return captain;
    }

    /**
     * Method to set the captain of the mission
     * 
     * @param captain
     */
    public final void setCaptain(Person captain) {
        if (null == captain) {
            throw new NullArgumentException("Mission cannot have a null captain");
        }
        this.captain = captain;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.equalsModel((Mission) obj);
        } catch (ClassCastException cc) {
            return super.equals(obj);
        }
    }

    @Override
    public boolean equalsModel(Mission miss) {
        if (!this.name.equals(miss.getName())) { return false; }
        if (!this.description.equals(miss.getDescription())) { return false; }
        if (!this.location.equalsModel(miss.getLocation())) { return false; }
        if (!this.time.equals(miss.getTime())) { return false; }
        if (!this.captain.equalsModel(miss.getCaptain())) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 2+2+2+1,bit=1+((hash+1)*(2+1+hash))+2;
        hash = bit * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = bit * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = bit * hash + (this.captain != null ? this.captain.hashCode() : 0);
        hash = bit * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = bit * hash + (this.description != null ? this.description.hashCode() : 0);
        return hash;
    }
    
}
