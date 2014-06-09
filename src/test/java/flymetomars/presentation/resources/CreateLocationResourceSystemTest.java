package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.model.Location;
import flymetomars.business.model.Person;
import flymetomars.business.model.SaltedPassword;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.jwebunit.junit.JWebUnit;
import org.junit.After;
import org.junit.Assert;
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
public class CreateLocationResourceSystemTest extends AbstractDataDependantResourceSystemTest {
    
    @Autowired
    private EntityLoader loader;
    
    @Autowired
    private EntitySaver saver;
    
    @Autowired
    private EntityDeleter deleter;
    
    @Autowired
    private EntityFactory factory;
    
    
    //<editor-fold defaultstate="collapsed" desc="AbstractDataDependantResourceSystemTest plugin code">
    private static int constructCount=0;
    public CreateLocationResourceSystemTest() {
        super(constructCount++);
    }
    private static int testCount;
    @BeforeClass
    public static void getTestCount() {
        Class<CreateLocationResourceSystemTest> me=CreateLocationResourceSystemTest.class;
        testCount=countTests(me);
    }
    @Before
    public void preTestTicker() {
        this.setTestCounter(testCount);
        this.setLoader(loader);
        this.setDeleter(deleter);
        makeLocation();
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
    
    @After
    public void tearDown() throws DependencyException {
        removeMissions();
        removeLocations();
    }
    
    //@AfterInstance (effectivly)
    public void doSystemWindup() {
        try {
            removeMissions();
            removeLocations();
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception durig System Test Windup", ex);
            throw new UnsupportedOperationException(ex);
        }
    }
    
    private SaltedPassword saltedPass;
    private static Person person;
    
    //@BeforeInstance (effectivly)
    public void doSystemInit() {
        try {
            removeMissions();
            removeLocations();
            removePeople();
            removePasswords();
            removeSalts();
        } catch (DependencyException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Test Infrastructure Error; unable to remove existing missions", e);
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
            person.setEmail("barfoo@example.new");
            person.setUserName("foobar");
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
        makeLocation();
    }

    private static final String VALID_PASSWORD="Hahah3!45";
    
    private Location location;  //just placeholder for form-fill
    
    private void makeLocation() {
        //location
        location=factory.createLocation();
        location.setFloor("H7.24");
        location.setStreetNo("900");
        location.setStreet("Dandenong Road");
        location.setRegion("Victoria");
        location.setPostcode("3037");
        location.setLandmark("By Station");
        location.setTown("Caulfield");
        location.setCountry("Australia");
    }
    
    public void loadLocationIdAsSaved(List<Location> former) {
        if(null==location.getId() && null!=former) {
            List<Location> present=loader.loadAllLocations();
            for(Location there : present) {
                if(former.contains(there)) { continue; }
                location=there;
                break;
            }
        }
    }
    
    /**
     * private utility method for simplification of creation verification
     */
    private void verifyLocationPageIsThereNow() {
        JWebUnit.gotoPage("/location/" + location.getId());
        JWebUnit.assertTextPresent("Location Details");
        JWebUnit.assertTextPresent("Floor: " + location.getFloor());
        JWebUnit.assertTextPresent("Street No: " + location.getStreetNo());
        JWebUnit.assertTextPresent("Street: " + location.getStreet());
        JWebUnit.assertTextPresent("Landmark: " + location.getLandmark());
        JWebUnit.assertTextPresent("Town/City: " + location.getTown());
        JWebUnit.assertTextPresent("Region: " + location.getRegion());
        JWebUnit.assertTextPresent("Postcode: " + location.getPostcode());
        JWebUnit.assertTextPresent("Country: " + location.getCountry());
    }
    
    /**
     * index > login > base > user > create mission > create location > location
     */
    @Test
    public void testCreateLocationFromMissionCreatePage() {
        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password",VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("Welcome, " + person.getFirstName() + " " + person.getLastName());
        JWebUnit.assertTextPresent("User Details");
         // go to create mission page
        JWebUnit.clickLinkWithText("Create Mission");
        //  go to create location page
        JWebUnit.clickLinkWithText("Create Location");
        JWebUnit.gotoPage("/location/create");
        //form-fill
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        int before=loader.loadAllLocations().size();
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //ensure database presence
        Assert.assertTrue(loader.loadAllLocations().size()>before);
        //verify location details    
        verifyLocationPageIsThereNow();
    }
    
    /**
     * private utility method for simplification of related test cases
     */
    private void loginAndGoToCreateLocation() {
        JWebUnit.beginAt("/login");
        JWebUnit.assertTextPresent("Login with your Fly me to Mars username and password");
        JWebUnit.setTextField("j_username", person.getUserName());
        JWebUnit.setTextField("j_password",VALID_PASSWORD);
        JWebUnit.clickButtonWithText("Login");
        JWebUnit.gotoPage("/user/" + person.getUserName());
        JWebUnit.assertTextPresent("User Details");
        JWebUnit.clickLinkWithText("Create Location");
        JWebUnit.gotoPage("/location/create");
        JWebUnit.assertTextPresent("Create location");
    }

    /**
     * index > login > base > user > create location > location
     */
    @Test
    public void testCreateLocationFromUserHomePage() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify location details    
        verifyLocationPageIsThereNow();
    }
    
    /**
     * index > login > base > user > create mission > create location > location
     */
    @Test
    public void testCreateLocationWithMinimalInformationSuccess() {
        loginAndGoToCreateLocation();
        //JWebUnit.setTextField("floor", location.getFloor());
        //JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        //JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        //JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify location details    
        JWebUnit.gotoPage("/location/" + location.getId());
        JWebUnit.assertTextPresent("Location Details");
        //JWebUnit.assertTextPresent("Floor: " + location.getFloor());
        //JWebUnit.assertTextPresent("Street No: " + location.getStreetNo());
        JWebUnit.assertTextPresent("Street: " + location.getStreet());
        //JWebUnit.assertTextPresent("Landmark: " + location.getLandmark());
        JWebUnit.assertTextPresent("Town/City: " + location.getTown());
        JWebUnit.assertTextPresent("Region: " + location.getRegion());
        //JWebUnit.assertTextPresent("Postcode: " + location.getPostcode());
        JWebUnit.assertTextPresent("Country: " + location.getCountry());
    }
    
    /**
     * index > login > base > user > create location 
     * Empty street and error comes
     */
    @Test
    public void testCreateLocationEmptyStreetFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        //JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location name
        JWebUnit.assertTextPresent("Location \"Street\" address value must be between five and fourty characters in length. Thank you.");
    }
    
    
    /**
     * index > login > base > user > create location 
     * Empty region and error comes
     */
    @Test
    public void testCreateLocationEmptyRegionFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        //JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location region
        JWebUnit.assertTextPresent("Location region must be between two and thirty characters");
    }
    
    /**
     * index > login > base > user > create location 
     * Empty town and error comes
     */
    @Test
    public void testCreateLocationEmptyTownFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        //JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location name
        JWebUnit.assertTextPresent("\"Town\" component of Location specification must be between two and thirty characters in length. Thank you");
    }
    
    /**
     * index > login > base > user > create location 
     * Empty country and error comes
     */
    @Test
    public void testCreateLocationEmptyCountryFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        //JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location name
        JWebUnit.assertTextPresent("\"Country\" article of Location data must be between two and thirty-two characters in length. Thank you.");
    }
    
    /**
     * index > login > base > user > create location 
     * Floor value is too long
     */
    @Test
    public void testCreateLocationSetFloorGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", "The floor just above Ground floor");
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location name
        JWebUnit.assertTextPresent("Location \"Floor\" component (if specified) must be between one and twelve characters in length. Thanks.");
    }
    
    /**
     * index > login > base > user > create location 
     * Floor value contains invalid character
     */
    @Test
    public void testCreateLocationSetFloorContainsInvalidCharFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", "level 2*1");
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location 
        JWebUnit.assertTextPresent("Please ensure Location \"Floor\" value contains only letters, numbers, spaces, dashes, dots, and colons. Thank you.");
    }
    
    /**
     * index > login > base > user > create location 
     * Street No. is too long
     */
    @Test
    public void testCreateLocationSetStreetNoGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", "This street no is longer than 10 chars");
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location name
        JWebUnit.assertTextPresent("Location street number field must can be at most ten characters");
    }
    
    /**
     * index > login > base > user > create location 
     * Street is greater than max
     */
    @Test
    public void testCreateLocationSetStreetGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", "Morrabin south where little lane turns around Dandenong road area");
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("Location \"Street\" address value must be between five and fourty characters in length. Thank you.");
    }
        
    /**
     * index > login > base > user > create location 
     * town value is too long
     */
    @Test
    public void testCreateLocationSetTownGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", "Geelong is a town with length greater than 30 chars");
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("\"Town\" component of Location specification must be between two and thirty characters in length. Thank you");
    }
    
    /**
     * index > login > base > user > create location
     * country value is too long
     */
    @Test
    public void testCreateLocationSetCountryGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode", location.getPostcode());
        JWebUnit.setTextField("country", "New Zealand is a country that is longer than 32 chars");
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("\"Country\" article of Location data must be between two and thirty-two characters in length. Thank you.");
    }
    
    /**
     * index > login > base > user > create location 
     * post code value is too long
     */
    @Test
    public void testCreateLocationSetPostCodeGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode","0123456789-0123456789");
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("Postcode (if any) must be between one & twelve characters");
    }
    
     /**
      * index > login > base > user > create location 
      * region value is too long
      */
    @Test
    public void testCreateLocationSetRegionGreaterThanMaxFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", "This region is longer than 30 chars.");
        JWebUnit.setTextField("postcode",location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("Location region must be between two and thirty characters");
    }
    
     /**
      * index > login > base > user > create location 
      * street contains illegal character
      */
    @Test
    public void testCreateLocationStreetContainsInvalidCharFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", "Dande@3ngs#road");
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode",location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("Location Street field may only contain; letters, numbers, extended latin alphabet symbols, dots, dashes, spaces apostrophes, and commas. All other characters disallowed.");
    }
    
     /**
      * index > login > base > user > create location 
      * town contains illegal character
      */
    @Test
    public void testCreateLocationTownContainsInvalidCharFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", "Melbour%ne");
        JWebUnit.setTextField("region", location.getRegion());
        JWebUnit.setTextField("postcode",location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location
        JWebUnit.assertTextPresent("Value given for the \"town\" component of Location is invalid as it contains banned characters.");
    }
    
     /**
      * index > login > base > user > create location 
      * region contains illegal character
      */
    @Test
    public void testCreateLocationRegionContainsInvalidCharFailure() {
        loginAndGoToCreateLocation();
        JWebUnit.setTextField("floor", location.getFloor());
        JWebUnit.setTextField("streetNo", location.getStreetNo());
        JWebUnit.setTextField("street", location.getStreet());
        JWebUnit.setTextField("landmark", location.getLandmark());
        JWebUnit.setTextField("town", location.getTown());
        JWebUnit.setTextField("region", "New South! Wales");
        JWebUnit.setTextField("postcode",location.getPostcode());
        JWebUnit.setTextField("country", location.getCountry());
        List<Location> former=loader.loadAllLocations();
        JWebUnit.clickButtonWithText("Create New Location");
        loadLocationIdAsSaved(former);
        //verify error message of location region
        JWebUnit.assertTextPresent("Entered value for Location region component contains invalid characters, permissible characters are letters, extended latin alphabetical characters, spaces, dashes, commas, and appostrophees. Thank you.");
    }
   
}
