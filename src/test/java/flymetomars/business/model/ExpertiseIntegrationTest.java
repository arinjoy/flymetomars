package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the expertise model
 * 
 * @author Apoorva Singh
 * @author Lawrence Colman
 */
public class ExpertiseIntegrationTest {
 
    //<editor-fold desc="name test cases">
    
    @Test(expected = NullArgumentException.class)
    public void testSetNameNullFailure() {
        Expertise e = new Expertise();
        e.setName(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameLenghtLessThanMinFailure() {
        Expertise e = new Expertise();
        e.setName("Abc");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameLengthGreaterThanMaxFailure() {
        Expertise e = new Expertise();
        e.setName("This expertise name is longer than 20 chars");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetNameContainsIlegalCharacterFailure() {
        Expertise e = new Expertise();
        e.setName("Junio?r");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="description test cases">
    
    @Test(expected = NullArgumentException.class)
    public void testSetDescriptionNullFailure() {
        Expertise e = new Expertise();
        e.setDescription(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionLenghtLessThanMinFailure() {
        Expertise e = new Expertise();
        e.setDescription("Talker");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetdescriptionLengthGreaterThanMaxFailure() {
        Expertise e = new Expertise();
        e.setDescription("This description name is longer than 120 chars. It has a lot of irrelevant details. It should definitely be shortened by a large amount.");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDescriptionContainsIlegalCharacterFailure() {
        Expertise e = new Expertise();
        e.setDescription("Can fly and speak <script> languages");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="level test cases">
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetLevelBadValueFailure() {
        Expertise e = new Expertise();
        e.setLevel(flymetomars.common.datatransfer.Expertise.ExpertiseLevel.valueOf("xyzyx"));
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetLevelNullFailure() {
        Expertise e = new Expertise();
        e.setLevel(null);
    }
    
    //</editor-fold>
    
}
