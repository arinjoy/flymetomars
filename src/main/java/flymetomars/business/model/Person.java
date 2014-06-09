package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.BasicLogic;
import flymetomars.common.logic.RhinoBasicLogicImpl;
//import flymetomars.common.validation.StringValidator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class represents all People (invitation recipients, mission
 * participants, spaceship captains, miscellaneous PETSS personnel, etc) that
 * may interact, partake or be in any way involved with any PETSS Mars flight or
 * considered for one (including members of the public registering their
 * interest).
 *
 * As a member of the model package, this class implements business logic.
 *
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class Person extends IdentifiableModel<Person> implements TransferableModel<flymetomars.common.datatransfer.Person> {
  
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private SaltedPassword password;
        
    private Set<Expertise> expertiseGained;
    private Set<Mission> missionsRegistered;
    private Set<Invitation> invitationsReceived;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    private BasicLogic<flymetomars.common.datatransfer.Person> logic=
        new RhinoBasicLogicImpl<flymetomars.common.datatransfer.Person>(
            flymetomars.common.datatransfer.Person.class, dtoFactory
    );
    
    /**
     * Default person constructor which initializes the class variables
     */
    public Person() {
        this.expertiseGained = Collections.emptySet();
        this.missionsRegistered = Collections.emptySet();
        this.invitationsReceived = Collections.emptySet();
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.userName = "";
        this.password = new SaltedPassword();
    }
    
        
     /**
     * Copy constructor that takes a DTO to a Mission model object
     * 
     * @param dto Data Transfer Object representing a Mission 
     */
     public Person(flymetomars.common.datatransfer.Person dto) {
        this();
        if(null==dto) { throw new NullArgumentException("Null DTO cannot be identified");}
        this.setFirstName(dto.getFirstName());
        this.setLastName(dto.getLastName());
        this.setUserName(dto.getUserName());
        if(null!=dto.getPassword()){
            SaltedPassword sp=new SaltedPassword();
            sp.setId(dto.getPassword());
            this.setPassword(sp);
        }
        this.setEmail(dto.getEmail());
        Set<Mission> missions = new HashSet<Mission>();
        for(flymetomars.common.datatransfer.Mission mis : dto.getMissionsRegistered()) {
            missions.add(new Mission(mis));
        }      
        this.missionsRegistered=missions;
        Set<Invitation> invs=new HashSet<Invitation>();
        for(flymetomars.common.datatransfer.Invitation inv : dto.getInvitationsReceived()) {
            invs.add(new Invitation(inv));
        }
        this.invitationsReceived=invs;
        Set<Expertise> exps=new HashSet<Expertise>();
        for(flymetomars.common.datatransfer.Expertise exp : dto.getExpertiseGained()) {
            exps.add(new Expertise(exp));
        }
        this.expertiseGained=exps;
        /*this*/super.setId(dto.getId());
     }
     
     /**
      * Utility function (private) to avoid complexity in public methods
      * 
      * @param condition Boolean condition to act upon if true
      * @param action Runnable action to take is condition is true
      */
    private void doif(boolean condition, Runnable action) {
	if(condition) { action.run(); }
    }
     
     /**
     * To convert a Person model object to a dto
     */
    @Override
     public final flymetomars.common.datatransfer.Person toDTO() {
        final flymetomars.common.datatransfer.Person result;
        try {
            result=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        if(null!=this.getFirstName()) { result.setFirstName(this.getFirstName()); }
        if(null!=this.getLastName()) { result.setLastName(this.getLastName()); }
        if(null!=this.getUserName()) { result.setUserName(this.getUserName()); }
        if(null!=this.getPassword()) { result.setPassword(this.getPassword().getId()); }
        if(null!=this.getEmail()) { result.setEmail(this.getEmail()); }
        final Long myId=this.getId();
        doif((null!=this.getId()), new Runnable() {
            @Override
            public void run() {result.setId(myId);}
        });
        Set<flymetomars.common.datatransfer.Mission> missions = new HashSet<flymetomars.common.datatransfer.Mission>();
        for(Mission m : this.getMissionsRegistered()) {
            flymetomars.common.datatransfer.Mission mDTO=m.toDTO(this);
            //mDTO.getParticipantSet().add(this.toDTO());
            missions.add(mDTO);
        }
        result.setMissionsRegistered(missions);
        Set<flymetomars.common.datatransfer.Invitation> invs = new HashSet<flymetomars.common.datatransfer.Invitation>();
        /*for(Invitation i : this.getInvitationsReceived()) {
            invs.add(i.toDTO(result));
        }*/
        result.setInvitationsReceived(invs);
        Set<flymetomars.common.datatransfer.Expertise> exps = new HashSet<flymetomars.common.datatransfer.Expertise>();
        /*for(Expertise e : this.getExpertiseGained()) {
            exps.add(e.toDTO());
        }*/
        result.setExpertiseGained(exps);
        return result;
    }
    /**
     * Method to get the first name of the person
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Method to set the first name of the person
     * @param firstName
     */
    public final void setFirstName(String firstName) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(firstName, 2, 25);
        StringValidator.validateStringContainsOnly(firstName, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + "-");
        */
        flymetomars.common.datatransfer.Person dto=null;//this.toDTO();
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Person.class.getCanonicalName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setFirstName(firstName);
        logic.applyRules(this.getClass().getSimpleName(),"firstName",dto);
        this.firstName = firstName;
    }

    /**
     * Method to get the last name of the person
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Method to set the last name of the person
     * @param lastName
     */
    public final void setLastName(String lastName) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(lastName, 2, 25);
        StringValidator.validateStringContainsOnly(lastName, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN + " -");
        StringValidator.validateStringContainsMinimumChars(lastName, 2, StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN);
        */
        flymetomars.common.datatransfer.Person dto=null;//this.toDTO();
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Person.class.getCanonicalName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setLastName(lastName);
        logic.applyRules(this.getClass().getSimpleName(),"lastName",dto);
        this.lastName = lastName;
    }

    /**
     * Method to get the user name of the person
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Method to set the user name of the person
     * @param userName
     */
    public final void setUserName(String userName) {
        /* Old Validation (non-JS):
        StringValidator.validateStringLengthInRange(userName, 6, 20);
        String acceptableCharacters = StringValidator.LATIN_ALPHABET;
        acceptableCharacters += StringValidator.NUMBERS + "._";
        StringValidator.validateStringContainsOnly(userName, acceptableCharacters);
        */
        flymetomars.common.datatransfer.Person dto=null;//this.toDTO();
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Person.class.getCanonicalName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setUserName(userName);
        logic.applyRules(this.getClass().getSimpleName(),"userName",dto);
        this.userName = userName;
    }
    
    /**
     * Method to get the salted password of the person
     * @return
     */
    public SaltedPassword getPassword() {
        return password;
    }

    /**
     * Method to set the salted password of the person
     * @param password
     */
    public final void setPassword(SaltedPassword password) {
        if(null==password) { throw new NullArgumentException("A Person's SaltedPassword cannot be null"); }
        this.password = password;
    }
    
    /**
     * 
     * @param pass
     * @return
     */
    public boolean isPassword(String pass) {  //utility wrapper accessor
        return this.getPassword().isPassword(pass);
    }
    
    /**
     *
     * @param pass
     */
    public void updatePassword(String pass) {  //utility wrapper accessor
        this.getPassword().setPassword(pass);
    }
    
    /**
     * Method to get the email id of the person
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Method to set the email id of the person
     * @param email
     */
    public final void setEmail(String email) {
        /* Old Validation (non-JS):
        StringValidator.validateEmail(email);
        StringValidator.validateStringLengthInRange(email, 8, 80);
        */
        flymetomars.common.datatransfer.Person dto=null;//this.toDTO();
        try {
            dto=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Person.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Person.class.getCanonicalName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw new IllegalStateException(ex);
        }
        dto.setEmail(email);
        logic.applyRules(this.getClass().getSimpleName(),"email",dto);
        this.email = email;
    }

    /**
     * Method to get the registered missions of the person
     * @return
     */
    public Set<Mission> getMissionsRegistered() {
        return missionsRegistered;
    }

    /**
     * Method to set the registered missions of the person
     * @param missionsRegistered
     */
    public void setMissionsRegistered(Set<Mission> missionsRegistered) {
        if (null == missionsRegistered) {
            throw new NullArgumentException("person mission set cannot be set to null");
        }
        for (Mission m : missionsRegistered) {
            if (null == m) {
                throw new NullArgumentException("person cannot register for a null mission");
            }
        }
        this.missionsRegistered = missionsRegistered;
    }

    /**
     * Method to get the invitations of the person
     * @return
     */
    public Set<Invitation> getInvitationsReceived() {
        return invitationsReceived;
    }

    /**
     * Method to set the invitations of the person
     * @param invitationsReceived
     */
    public void setInvitationsReceived(Set<Invitation> invitationsReceived) {
        if (null == invitationsReceived) {
            throw new NullArgumentException("Person invitation set cannot be set to null");
        }
        for (Invitation i : invitationsReceived) {
            if (null == i) {
                throw new NullArgumentException("Person cannot recieve a null Invitation");
            }
            if (null == i.getCreator()) {
                throw new NullArgumentException("Person Invitation cannot have null creator");
            }
            if (null == i.getRecipient()) {
                throw new NullArgumentException("Person Invitation cannot have null reciepient");
            }
            if (null!=i.getRecipient() && !this.equals(i.getRecipient())) {
                throw new IllegalArgumentException("Person cannot recieve an Invitation for which they are not the reciepient");
            }
            if (null == i.getMission()) {
                throw new NullArgumentException("Person Invitation cannot have null Mission component");
            }
        }
        this.invitationsReceived = invitationsReceived;
    }

    /**
     * Method to get the expertise of the person
     * @return
     */
    public Set<Expertise> getExpertiseGained() {
        return expertiseGained;
    }

    /**
     * Method to set the expertise of the person
     * @param expertiseGained
     */
    public void setExpertiseGained(Set<Expertise> expertiseGained) {
        if (null == expertiseGained) {
            throw new NullArgumentException("Person expertise set cannot be null");
        }
        for (Expertise exp : expertiseGained) {
            if(exp==null) {
                throw new NullArgumentException("An expertise cannot be null");
            }
        }
        if (!expertiseGained.isEmpty()) {
            flymetomars.common.datatransfer.Person dto=this.toDTO();
            Set<flymetomars.common.datatransfer.Expertise> exp=new HashSet<flymetomars.common.datatransfer.Expertise>();
            for(Expertise e : expertiseGained) {
                flymetomars.common.datatransfer.Expertise dummy=null;
                try {
                    dummy=this.dtoFactory.createDTO(flymetomars.common.datatransfer.Expertise.class);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Person.class.getCanonicalName()).log(Level.SEVERE, ex.getMessage(), ex);
                    throw new UnsupportedOperationException(ex);
                }
                dummy.setName(e.getName());
                dummy.setDescription(e.getDescription());
                dummy.setLevel(e.getLevel());
                dummy.setId(e.getId());
                exp.add(dummy);
            }
            dto.setExpertiseGained(exp);
            logic.applyRules(this.getClass().getSimpleName(),"expertiseGained",dto);
        }
        this.expertiseGained = expertiseGained;
    }
    
    @Override
    public boolean equals(Object obj) {
        try {
            return this.equalsModel((Person) obj);
        } catch (ClassCastException cc) {
            return super.equals(obj);
        }
    }

    @Override
    public int hashCode() {
        int hash = 2+1, bit=((2+2)*(((2*2)+1)*2))+hash;
        hash = bit * hash + (this.firstName != null ? this.firstName.hashCode() : 0);
        hash = bit * hash + (this.lastName != null ? this.lastName.hashCode() : 0);
        hash = bit * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = bit * hash + (this.userName != null ? this.userName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equalsModel(Person per) {
        if(!this.firstName.equals(per.getFirstName())) { return false; }
        if(!this.lastName.equals(per.getLastName())) { return false; }
        if(!this.email.equals(per.getEmail())) { return false; }
        if(!this.userName.equals(per.getUserName())) { return false; }
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        result.append(this.getClass().getName());
        result.append('#');
        result.append(this.getId());
        result.append(' ');
        result.append(this.getUserName());
        result.append(": \"");
        result.append(this.getFirstName());
        result.append(' ');
        result.append(this.getLastName());
        result.append(" - ");
        result.append(this.getEmail());
        result.append(" @");
        result.append(this.hashCode());
        return result.toString();
    }
    
}
