package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * A representative class for Mission's (to MarS) within FlyMeToMars system.
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public class MissionImpl extends SeriablizableEntityImpl<Mission> implements EqualableStructure<Mission>, Mission {

    private Date time;
    private String name;
    private Person captain;
    private Location location;
    private String description;
    private Set<Person> participantSet;
    private Set<Invitation> invitationSet;

    /**
     * default constructor
     */
    public MissionImpl() {}

    /**
     * Property accessor for `name' of Mission
     * 
     * @return String representing the associated name of this Mission
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Property mutator for `name' of Mission
     *
     * @param name String to represent the name of this Mission
     */
    @Override
    public void setName(String name) {
         if(null==name) { throw new NullArgumentException("A mission name cannot be set null"); }
        this.name = name;
    }

    /**
     * Property accessor for `time' of Mission
     *
     * @return Date object representing this Missions begin/launch time
     */
    @Override
    public final Date getTime() {
        return time;
    }

    /**
     * Property mutator for `time' of Mission
     *
     * @param time Date object to store as record for Mission timing
     */
    @Override
    public void setTime(Date time) {
        if(null==time) { throw new NullArgumentException("A mission time cannot be set null"); }
        this.time = time;
    }

    /**
     * Property accessor for `location' of Mission
     *
     * @return Location represented at the site of this Mission
     */
    @Override
    public final Location getLocation() {
        return location;
    }

    /**
     * Property mutator for `location' of Mission
     * 
     * @param location Location at which to represent the Mission as being at
     */
    @Override
    public void setLocation(Location location) {
        if (null == location) {
            throw new NullArgumentException("cannot set location to null");
        }
        this.location = location;
    }

    /**
     * Property accessor for `description' of Mission
     *
     * @return String that describes this Mission and its activities
     */
    @Override
    public final String getDescription() {
        return description;
    }

    /**
     * Property mutator for `description' of Mission
     * 
     * @param description String describing this Mission and its activities
     */
    @Override
    public void setDescription(String description) {
        if(null==description) { throw new NullArgumentException("A mission description cannot be set null"); }
        this.description = description;
    }

    /**
     * Property accessor for set if Invitations to this Mission
     * 
     * @return Set of Invitation objects that reference this Mission
     */
    @Override
    public final Set<Invitation> getInvitationSet() {
        return invitationSet;
    }

    /**
     * Property mutator for set if Invitations to this Mission
     *
     * @param invitationSet Set of Invitation objects that reference this Mission
     */
    @Override
    public void setInvitationSet(Set<Invitation> invitationSet) {
        if (null == invitationSet) {
            //throw new NullArgumentException("cannot set invitations to null");
            this.invitationSet=Collections.emptySet();
            return;
        }
        this.invitationSet = invitationSet;
    }

    /**
     * Property accessor for set of Persons participating
     * 
     * @return Set of Person recorded as taking part within the Mission
     */
    @Override
    public final Set<Person> getParticipantSet() {
        return participantSet;
    }

    /**
     * Property mutator for set of Persons participating
     * 
     * @param participantSet Set of Person to record as taking part in Mission
     */
    @Override
    public void setParticipantSet(Set<Person> participantSet) {
        if (null == participantSet) {
            //throw new NullArgumentException("cannot set participants to null");
            this.participantSet=Collections.emptySet();
            return;
        }
        this.participantSet = participantSet;
    }

    /**
     * Property accessor for `captain' of Mission
     *
     * @return Person associated as the pilot and leader of this Mission
     */
    @Override
    public final Person getCaptain() {
        return captain;
    }

    /**
     * Property mutator for `captain' of Mission
     * 
     * @param captain Person to associate as the pilot and leader of the Mission
     */
    @Override
    public void setCaptain(Person captain) {
        if (null == captain) {
            throw new NullArgumentException("Mission cannot have a null captain");
        }
        this.captain = captain;
    }
    
    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = 2+1,bits=(2*2*2*2*2*2)+2+2+1+((2+2+1)*2*2);
        hash = bits * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = bits * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = bits * hash + (this.captain != null ? this.captain.hashCode() : 0);
        hash = bits * hash + (this.location != null ? this.location.hashCode() : 0);
        hash = bits * hash + (this.description != null ? this.description.hashCode() : 0);
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
        return this.equalsDTO((Mission)obj);
    }
    
    /**
     * 
     * 
     * @param miss
     * @return 
     */
    @Override
    public boolean equalsDTO(Mission miss) {
        if (!this.name.equals(miss.getName())) { return false; }
        if (!this.description.equals(miss.getDescription())) { return false; }
        if (!this.location.equals(miss.getLocation())) { return false; }
        if (!this.time.equals(miss.getTime())) { return false; }
        if (!this.captain.equals(miss.getCaptain())) { return false; }
        return true;
    }

}
