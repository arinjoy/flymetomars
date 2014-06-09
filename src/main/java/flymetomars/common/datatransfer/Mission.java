package flymetomars.common.datatransfer;

import java.util.Date;
import java.util.Set;

/**
 * A representative class for Mission's (to MarS) within FlyMeToMars system.
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 * @author Apoorva Singh
 */
public interface Mission extends SeriablizableEntity<Mission> {
    
    /**
     * Property accessor for `name' of Mission
     * 
     * @return String representing the associated name of this Mission
     */
    String getName();
    
    /**
     * Property mutator for `name' of Mission
     *
     * @param name String to represent the name of this Mission
     */
    void setName(String name);

    /**
     * Property accessor for `time' of Mission
     *
     * @return Date object representing this Missions begin/launch time
     */
    Date getTime();
    
    /**
     * Property mutator for `time' of Mission
     *
     * @param time Date object to store as record for Mission timing
     */
    void setTime(Date time);

    /**
     * Property accessor for `location' of Mission
     *
     * @return Location represented at the site of this Mission
     */
    Location getLocation();
    
    /**
     * Property mutator for `location' of Mission
     * 
     * @param location Location at which to represent the Mission as being at
     */
    void setLocation(Location location);

    /**
     * Property accessor for `description' of Mission
     *
     * @return String that describes this Mission and its activities
     */
    String getDescription();
    
    /**
     * Property mutator for `description' of Mission
     * 
     * @param description String describing this Mission and its activities
     */
    void setDescription(String description);

    /**
     * Property accessor for set if Invitations to this Mission
     * 
     * @return Set of Invitation objects that reference this Mission
     */
    Set<Invitation> getInvitationSet();
    
    /**
     * Property mutator for set if Invitations to this Mission
     *
     * @param invitationSet Set of Invitation objects that reference this Mission
     */
    void setInvitationSet(Set<Invitation> invitationSet);

    /**
     * Property accessor for set of Persons participating
     * 
     * @return Set of Person recorded as taking part within the Mission
     */
    Set<Person> getParticipantSet();
    
    /**
     * Property mutator for set of Persons participating
     * 
     * @param participantSet Set of Person to record as taking part in Mission
     */
    void setParticipantSet(Set<Person> participantSet);

    /**
     * Property accessor for `captain' of Mission
     *
     * @return Person associated as the pilot and leader of this Mission
     */
    Person getCaptain();
    
    /**
     * Property mutator for `captain' of Mission
     * 
     * @param captain Person to associate as the pilot and leader of the Mission
     */
    void setCaptain(Person captain);
    
}
