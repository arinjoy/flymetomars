package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Expertise;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
import flymetomars.dataaccess.ExpertiseDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * 
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
@FixMethodOrder
public class AddExpertiseResourceSystemTest extends AbstractDataDependantResourceSystemTest {
    
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;
    
    @Autowired
    private ExpertiseDAO expertiseDao;
    
    
    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    private static int constructCount=0;
    public AddExpertiseResourceSystemTest() {
        super(constructCount++);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<AddExpertiseResourceSystemTest> me=AddExpertiseResourceSystemTest.class;
        testCount=countTests(me);
    }
    @Before
    public void preTestTicker() {
        this.setTestCounter(testCount);
        this.setLoader(loader);
        this.setDeleter(deleter);
        beforeTestTick();
    }
    @After
    public void postTestTicker() {
        afterTestTick();
    }
    @Override
    public void doBeforeFirstTest() {
        doSystemInit();
    }
    @Override
    public void doAfterLastTest() {
        doSystemWindup();
    }
    //</editor-fold>
       
    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception durig System Test Windup", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
    
    private static SaltedPassword saltedPass;
    private static Person person;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to remove existing persons", e);
            throw new IllegalStateException(e);
        }
        try {
            saltedPass=factory.createSaltedPassword(factory.createSalt(""));
            saver.saveSalt(saltedPass.getSalt());
            saltedPass.setPassword(VALID_PASSWORD);
            saver.saveSaltedPassword(saltedPass);
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to persist password errata", e);
            throw new IllegalStateException(e);
        }
        String port = getProperty("port.number");
        JWebUnit.setBaseUrl("http://localhost:" + port + "/flymetomars");
        JWebUnit.setScriptingEnabled(false);
        if(null==person) {
            //person
            person=factory.createPerson(saltedPass);
            person.setEmail("barfoogoo@example.new");
            person.setUserName("hoofoogoo");
            person.setFirstName("HooGoo");
            person.setLastName("FooGoo");
            //save
            try {
                person=saver.savePerson(person);
            } catch (DependencyException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Unable to persist person test data", e);
                throw new UnsupportedOperationException("System Test Infrastructure Error",e);
            }
        }
    }

    private static final String VALID_PASSWORD="Hahah3!45";
   
    /**
     * index > login > base > user > add expertise1 > expertise1
     */
    @Test
    public void testAddExpertiseSuccess() throws DependencyException {
        Expertise expertise=factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        flymetomars.common.datatransfer.Expertise expDTO = expertise.toDTO();
        expDTO.setHeldBy(person.toDTO());
        // saving the expertise1 first
        expertiseDao.save(expDTO);
        // getting the serilazable id from DTO
        expertise.setId(expDTO.getId());

        try {            
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  add expertise1
            JWebUnit.clickLinkWithText("Add Expertise");
            JWebUnit.gotoPage("/user/"+person.getUserName()+"/add_expertise");
            
            JWebUnit.setTextField("name", expertise.getName());
            JWebUnit.setTextField("description", expertise.getDescription());
            // selecting the level
            JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
  
            JWebUnit.clickButtonWithText("Add New Expertise");
            //verify expertise1 details page
            JWebUnit.gotoPage("/expertise/" + expertise.getId());
            JWebUnit.assertTextPresent("Expertise Details");
            JWebUnit.assertTextPresent("Name: " + expertise.getName());
            JWebUnit.assertTextPresent("Level: "+ expertise.getLevel().toString().toLowerCase());
            JWebUnit.assertTextPresent("Description: " + expertise.getDescription());
        } finally {
            expertiseDao.delete(expDTO);
        }
    }
    
    /**
     * index > login > base > user > add expertise1 > expertise1 > user page > verify expertise1 gained
     */
    @Test
    public void testAddExpertiseSuccessAndVerifyFromUserPage() throws DependencyException {
        Expertise expertise=factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);  
        
        flymetomars.common.datatransfer.Expertise expDTO = expertise.toDTO();
        expDTO.setHeldBy(person.toDTO());
        // saving the expertise1 first
        expertiseDao.save(expDTO);
        // getting the serilazable id from DTO
        expertise.setId(expDTO.getId());
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  add expertise1
            JWebUnit.clickLinkWithText("Add Expertise");
            JWebUnit.gotoPage("/user/"+person.getUserName()+"/add_expertise");
            
            JWebUnit.setTextField("name", expertise.getName());
            JWebUnit.setTextField("description", expertise.getDescription());
            // seleccting the Profession level
            JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
  
            JWebUnit.clickButtonWithText("Add New Expertise");
            //verify expertise1 details
            JWebUnit.gotoPage("/expertise/" + expertise.getId());
            JWebUnit.assertTextPresent("Expertise Details");
            JWebUnit.assertTextPresent("Name: " + expertise.getName());
            JWebUnit.assertTextPresent("Level: "+ expertise.getLevel().toString());
            JWebUnit.assertTextPresent("Description: " + expertise.getDescription());
            
            // go to the person's home page to see if the expertise1 has been gained by him
            JWebUnit.gotoPage("/user/" + person.getUserName());
            // make sure the added expertise1 has been gained
            JWebUnit.assertTextPresent("My Expertise:");
            JWebUnit.assertTextPresent(expertise.getName());
        } finally {
            expertiseDao.delete(expDTO);
        }
    }
    
     /**
     * index > login > base > user > add expertise1 > expertise1 > user page > verify expertise1 gained
     */
    @Test
    public void testAddTwoExpertiseAndVerifyBothFromUserPage() throws DependencyException {
        Expertise expertise1=factory.createExpertise();
        expertise1.setName("Mountain Biking");
        expertise1.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise1.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);  
        
        flymetomars.common.datatransfer.Expertise expDTO1 = expertise1.toDTO();
        expDTO1.setHeldBy(person.toDTO());
        // saving the expertise-1
        expertiseDao.save(expDTO1);
        // getting the serilazable id from DTO
        expertise1.setId(expDTO1.getId());
        
        Expertise expertise2=factory.createExpertise();
        expertise2.setName("Scuba Diving");
        expertise2.setDescription("Someone who an hold his breath for long time and know how to use oxygen cylinder and swim like fishes");
        expertise2.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.INTRODUCTORY);  
        
        flymetomars.common.datatransfer.Expertise expDTO2 = expertise1.toDTO();
        expDTO2.setHeldBy(person.toDTO());
        // saving the expertise-2
        expertiseDao.save(expDTO2);
        // getting the serilazable id from DTO
        expertise1.setId(expDTO2.getId());
        
        try {
            JWebUnit.beginAt("/login");
            JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
            JWebUnit.setTextField("j_username", person.getUserName());
            JWebUnit.setTextField("j_password", VALID_PASSWORD);
            JWebUnit.clickButtonWithText("Login");
            JWebUnit.gotoPage("/user/" + person.getUserName());
            JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
            JWebUnit.assertTextPresent("User Details");

            //  add expertise-1
            JWebUnit.clickLinkWithText("Add Expertise");
            JWebUnit.gotoPage("/user/"+person.getUserName()+"/add_expertise");
            
            JWebUnit.setTextField("name", expertise1.getName());
            JWebUnit.setTextField("description", expertise1.getDescription());
            // seleccting the Profession level
            JWebUnit.clickRadioOption("level", expertise1.getLevel().toString().toUpperCase());
  
            JWebUnit.clickButtonWithText("Add New Expertise");
            //verify expertise-1 details
            JWebUnit.gotoPage("/expertise/" + expertise1.getId());
            JWebUnit.assertTextPresent("Expertise Details");
            JWebUnit.assertTextPresent("Name: " + expertise1.getName());
            JWebUnit.assertTextPresent("Level: "+ expertise1.getLevel().toString());
            JWebUnit.assertTextPresent("Description: " + expertise1.getDescription());
            
            // back to home page to add one more expertise
            JWebUnit.gotoPage("/user/" + person.getUserName());
            
            //  add expertise-2
            JWebUnit.clickLinkWithText("Add Expertise");
            JWebUnit.gotoPage("/user/"+person.getUserName()+"/add_expertise");
            
            JWebUnit.setTextField("name", expertise2.getName());
            JWebUnit.setTextField("description", expertise2.getDescription());
            // seleccting the level
            JWebUnit.clickRadioOption("level", expertise2.getLevel().toString().toUpperCase());
  
            JWebUnit.clickButtonWithText("Add New Expertise");
            //verify expertise2 details
            JWebUnit.gotoPage("/expertise/" + expertise2.getId());
            JWebUnit.assertTextPresent("Expertise Details");
            JWebUnit.assertTextPresent("Name: " + expertise2.getName());
            JWebUnit.assertTextPresent("Level: "+ expertise2.getLevel().toString());
            JWebUnit.assertTextPresent("Description: " + expertise2.getDescription());
            
            // go to the person's home page to see if the expertise-1 and 2 have been gained by him
            JWebUnit.gotoPage("/user/" + person.getUserName());
           
            // make sure the added expertise-2 has been gained
            JWebUnit.assertTextPresent("My Expertise:");
            JWebUnit.assertTextPresent(expertise1.getName());
            JWebUnit.assertTextPresent(expertise2.getName());
        } finally {
            expertiseDao.delete(expDTO1);
            expertiseDao.delete(expDTO2);
        }
    }
    
    /**
     * index > login > base > user > add expertise --> then fails
     */
    @Test
    public void testAddExpertiseEmptyNameFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", "");
        JWebUnit.setTextField("description", expertise.getDescription());
        // seleccting the Profession level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Expertise name must be between five and twenty characters");
    }
  
    /**
     * index > login > base > user > add expertise
     */
    @Test
    public void testAddExpertiseTooShortNameFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);


        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", "Abc");
        JWebUnit.setTextField("description", expertise.getDescription());
        // selecting the level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Expertise name must be between five and twenty characters");
    }
    
    /**
     * index > login > base > user > add expertise
     */
    @Test
    public void testAddExpertiseNameInvalidCharacterFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", "Scuba_Diving");
        JWebUnit.setTextField("description", expertise.getDescription());
        // selecting the level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Illegal character detected in Expertise name, please ensure that only the following characters are included:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz '.,-()");
    }
    
    /**
     * index > login > base > user > add expertise
     */
    @Test
    public void testAddExpertiseTooLongNameFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", "Scuba Diving in the Ocean world");
        JWebUnit.setTextField("description", expertise.getDescription());
        // selecting the  level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Expertise name must be between five and twenty characters");
    }

    /**
     * index > login > base > user > add expertise
     */
    @Test
    public void testAddExpertiseEmptyDescriptionFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", expertise.getName());
        // selecting the level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Expertise Description is required to be at least twenty and at most one hunderd and twenty characters in length.");
    }

    /**
     * index > login > base > user > add expertise
     */
    @Test
    public void testAddExpertiseTooLongDescriptionFailure() throws DependencyException {
        Expertise expertise = factory.createExpertise();
        expertise.setName("Mountain Biking");
        expertise.setDescription("Someone who can do great bike ride over rough montain surface and jump from once rock to other");
        expertise.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.PROFESSIONAL);

        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password", VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");

        //  add expertise1
        JWebUnit.clickLinkWithText("Add Expertise");
        JWebUnit.gotoPage("/user/" + person.getUserName() + "/add_expertise");

        JWebUnit.setTextField("name", expertise.getName());
        JWebUnit.setTextField("description", "Someone who can play, sing, and cook well with the help of coean creatures and fishes but does not play greatly with them. Someone who can be great fried to fishes.");
        // selecting the level
        JWebUnit.clickRadioOption("level", expertise.getLevel().toString().toUpperCase());
        JWebUnit.clickButtonWithText("Add New Expertise");

        //verify error message
        JWebUnit.assertTextPresent("Expertise Description is required to be at least twenty and at most one hunderd and twenty characters in length.");

    }
   
}
