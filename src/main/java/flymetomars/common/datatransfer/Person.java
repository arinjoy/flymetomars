package flymetomars.common.datatransfer;

import java.util.Set;

/**
 * This class represents Person domain objects for inter-layer transfer
 *
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * @author Apoorva Singh
 */
public interface Person extends SeriablizableEntity<Person> {

    /**
     * Property accessor for the `firstName' portion of a Person
     * 
     * @return String representing stored first name of Person
     */
    String getFirstName();
    
    /**
     * Property mutator for the `firstName' portion of a Person
     * 
     * @param firstName String to represent as Person's first name
     */
    void setFirstName(String firstName);

    /**
     * Property accessor for the `lastName' portion of a Person
     * 
     * @return String representing stored last name of Person
     */
    String getLastName();
    
    /**
     * Property mutator for the `lastName' portion of a Person
     * 
     * @param lastName String to represent as Person's last name
     */
    void setLastName(String lastName);

    /**
     * Property accessor for the `userName' portion of a Person
     * 
     * @return String representing stored user name of Person
     */
    String getUserName();
    
    /**
     * Property mutator for the `userName' portion of a Person
     * 
     * @param userName String to represent as Person's user name
     */
    void setUserName(String userName);
    
    /**
     * Property mutator for the `password' portion of a Person
     * 
     * @return SaltedPassword object representing hashed and salted password data
     */
    /*SaltedPassword*/Long getPassword();
    
    /**
     * Property accessor for the `password' portion of a Person
     * 
     * @param password SaltedPassword object for the salted hash of Person password
     */
    void setPassword(/*SaltedPassword*/Long password);
    
    /**
     * Property accessor for the `email' portion of a Person
     * 
     * @return String representing stored email address of Person
     */
    String getEmail();
    
    /**
     * Property mutator for the `email' portion of a Person
     * 
     * @param email String to represent as Person's email address
     */
    void setEmail(String email);

    /**
     * Property accessor for the contained set of `Mission' objects
     * 
     * @return Set of Missions participated in by Person
     */
    Set<Mission> getMissionsRegistered();
    
    /**
     * Property mutator for the contained set of `Mission' objects
     * 
     * @param missionsRegistered Set of Missions participated in by Person
     */
    void setMissionsRegistered(Set<Mission> missionsRegistered);

    /**
     * Property accessor for the contained set of `Invitation' objects
     * 
     * @return Set of Invitations received by Person
     */
    Set<Invitation> getInvitationsReceived();
    
    /**
     * Property mutator for the contained set of `Invitation' objects
     * 
     * @param invitationsReceived Set of Invitations received by Person
     */
    void setInvitationsReceived(Set<Invitation> invitationsReceived);

    /**
     * Property accessor for the contained set of `Expertise' objects
     * 
     * @return Set of Expertise belonging to this Person
     */
    Set<Expertise> getExpertiseGained();
    
    /**
     * Property mutator for the contained set of `Expertise' objects
     * 
     * @param expertiseGained Set of Expertise to represent as belonging to Person
     */
    void setExpertiseGained(Set<Expertise> expertiseGained);
    
}
