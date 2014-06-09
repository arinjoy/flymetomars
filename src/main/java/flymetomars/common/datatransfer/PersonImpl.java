package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;
import java.util.Collections;
import java.util.Set;

/**
 * This class represents Person domain objects for inter-layer transfer
 *
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 * @author Apoorva Singh
 * 
 * JavaDoc copied from interface.
 */
public class PersonImpl extends SeriablizableEntityImpl<Person> implements EqualableStructure<Person>, Person {
  
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private /*SaltedPassword*/Long password;
        
    private Set<Expertise> expertiseGained;
    private Set<Mission> missionsRegistered;
    private Set<Invitation> invitationsReceived;
    
    /**
     * default constructor
     */
    public PersonImpl() {}

    /**
     * Property accessor for the `firstName' portion of a Person
     * 
     * @return String representing stored first name of Person
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * Property mutator for the `firstName' portion of a Person
     * 
     * @param firstName String to represent as Person's first name
     */
    @Override
    public void setFirstName(String firstName) {
       if(null==firstName) { throw new NullArgumentException("A Person's first name cannot be null"); } 
       this.firstName = firstName;
    }

    /**
     * Property accessor for the `lastName' portion of a Person
     * 
     * @return String representing stored last name of Person
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * Property mutator for the `lastName' portion of a Person
     * 
     * @param lastName String to represent as Person's last name
     */
    @Override
    public void setLastName(String lastName) {
        if(null==lastName) { throw new NullArgumentException("A Person's last name cannot be null"); }
        this.lastName = lastName;
    }

    /**
     * Property accessor for the `userName' portion of a Person
     * 
     * @return String representing stored user name of Person
     */
    @Override
    public String getUserName() {
        return userName;
    }

    /**
     * Property mutator for the `userName' portion of a Person
     * 
     * @param userName String to represent as Person's user name
     */
    @Override
    public void setUserName(String userName) {
        if(null==userName) { throw new NullArgumentException("A Person's user name cannot be null"); }
        this.userName = userName;
    }
    
    /**
     * Property mutator for the `password' portion of a Person
     * 
     * @return SaltedPassword object representing hashed and salted password data
     */
    @Override
    public /*SaltedPassword*/Long getPassword() {
        return password;
    }
    
    /**
     * Property accessor for the `password' portion of a Person
     * 
     * @param password SaltedPassword object for the salted hash of Person password
     */
    @Override
    public void setPassword(/*SaltedPassword*/Long password) {
        if(null==password) { throw new NullArgumentException("A Person's SaltedPassword cannot be null"); }
        this.password = password;
    }
    
    /**
     * Property accessor for the `email' portion of a Person
     * 
     * @return String representing stored email address of Person
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * Property mutator for the `email' portion of a Person
     * 
     * @param email String to represent as Person's email address
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Property accessor for the contained set of `Mission' objects
     * 
     * @return Set of Missions participated in by Person
     */
    @Override
    public Set<Mission> getMissionsRegistered() {
        return missionsRegistered;
    }

    /**
     * Property mutator for the contained set of `Mission' objects
     * 
     * @param missionsRegistered Set of Missions participated in by Person
     */
    @Override
    public void setMissionsRegistered(Set<Mission> missionsRegistered) {
        if (null == missionsRegistered) {
            //throw new NullArgumentException("person mission set cannot be set to null");
            this.missionsRegistered=Collections.emptySet();
            return;
        }
        this.missionsRegistered = missionsRegistered;
    }

    /**
     * Property accessor for the contained set of `Invitation' objects
     * 
     * @return Set of Invitations received by Person
     */
    @Override
    public Set<Invitation> getInvitationsReceived() {
        return invitationsReceived;
    }

    /**
     * Property mutator for the contained set of `Invitation' objects
     * 
     * @param invitationsReceived Set of Invitations received by Person
     */
    @Override
    public void setInvitationsReceived(Set<Invitation> invitationsReceived) {
        if (null == invitationsReceived) {
            //throw new NullArgumentException("Person invitation set cannot be set to null");
            this.invitationsReceived=Collections.emptySet();
            return;
        }
        this.invitationsReceived = invitationsReceived;
    }

    /**
     * Property accessor for the contained set of `Expertise' objects
     * 
     * @return Set of Expertise belonging to this Person
     */
    @Override
    public Set<Expertise> getExpertiseGained() {
        return expertiseGained;
    }

    /**
     * Property mutator for the contained set of `Expertise' objects
     * 
     * @param expertiseGained Set of Expertise to represent as belonging to Person
     */
    @Override
    public void setExpertiseGained(Set<Expertise> expertiseGained) {
        if (null == expertiseGained) {
            //throw new NullArgumentException("Person expertise set cannot be null");
            this.expertiseGained=Collections.emptySet();
            return;
        }
        this.expertiseGained = expertiseGained;
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
        final Person other = (Person)obj;
        return this.equalsDTO(other);
    }

    /**
     * Standard Java hashCode overide method implementation
     * 
     * @return Unique (within object-typed scope) integer deterministically
     *           generated to reflect the inner-contents represented by object
     */
    @Override
    public int hashCode() {
        int hash = 2+1, bits=((2*2)*2)+1;
        hash = 1+(bits*2*2) * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
        hash = 1+(bits*2*2) * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
        hash = 1+(bits*2*2) * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 1+(bits*2*2) * hash + (this.userName != null ? this.userName.hashCode() : 0);
        return hash;
    }

    /**
     * 
     * 
     * @param per
     * @return 
     */
    @Override
    public boolean equalsDTO(Person per) {
        if(this.firstName != null ? this.firstName.equals(per.getFirstName()) : per.getFirstName() == null) { return false; }
        if(this.lastName != null ? this.lastName.equals(per.getLastName()) : per.getLastName() == null) { return false; }
        if(this.email != null ? this.email.equals(per.getEmail()) : per.getEmail() == null) { return false; }
        if(this.userName != null ? this.userName.equals(per.getUserName()) : per.getUserName() == null) { return false; }
        return true;
    }
    
}
