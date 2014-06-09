package flymetomars.business.model;

import flymetomars.common.CharactersMissingException;
import flymetomars.common.NullArgumentException;
import flymetomars.common.StringTooLongException;
import flymetomars.common.StringTooShortException;
import org.junit.Test;

/**
 *
 * Integration test (via Rhino-enabled model implementation) of the Location
 * domain model object for the fly me to mars application
 * 
 * @author Apoorva Singh
 * @author Lawrence Colman
 */
public class LocationIntegrationTest {
    
    //<editor-fold desc="floor test cases">

    @Test
    public void testSetFloorValidSuccess() {
        Location l = new Location();
        l.setFloor("Thirteenth");
    }
    
    @Test
    public void testSetFloorNullSuccess() {
        Location l = new Location();
        l.setFloor(null);
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetFloorEmptyFailure() {
        Location l = new Location();
        l.setFloor("");
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetFloorLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setFloor("The floor just above Ground floor");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetFloorContainsIlegalCharacterFailure() {
        Location l = new Location();
        l.setFloor("Level 2*1");
    }
    
    //</editor-fold>
     
    //<editor-fold desc="street number test cases">
    
    @Test
    public void testSetStreetNoValidSuccess() {
        Location l = new Location();
        l.setStreetNo("1014-1019");
    }
    
    @Test
    public void testSetStreetNoNullSuccess() {
        Location l = new Location();
        l.setStreetNo(null);
    }
     
    @Test(expected = StringTooShortException.class)
     public void testSetStreetNoEmptyFailure() {
         Location l = new Location();
         l.setStreetNo("");
     }
    
    @Test(expected = StringTooLongException.class)
     public void testSetStreetNoLengthGreaterThanMaxFailure() {
         Location l = new Location();
         l.setStreetNo("This street no is longer than 10 chars");
     }
     
     @Test(expected = CharactersMissingException.class)
     public void testSetStreetNoContainsIlegalCharacterFailure() {
         Location l = new Location();
         l.setStreetNo("2?21");
     }
     
     //</editor-fold>
     
    //<editor-fold desc="street test cases">

    @Test
    public void testSetStreetValidSuccess() {
        Location l = new Location();
        l.setStreet("Randwick Cresent");
    }
     
    @Test(expected = NullArgumentException.class)
    public void testSetStreetNullFailure() {
        Location l = new Location();
        l.setStreet(null);
    }
     
     @Test(expected = StringTooLongException.class)
     public void testSetStreetLengthGreaterThanMaxFailure() {
         Location l = new Location();
         l.setStreet("Moorabin South where little lane turns around at Dandenong Road");
     }
     
     @Test(expected = StringTooShortException.class)
     public void testSetStreetLengthLessThanMinFailure() {
         Location l = new Location();
         l.setStreet("Moo");
     }
     
     @Test(expected = StringTooShortException.class)
     public void testSetStreetEmptyFailure() {
         Location l = new Location();
         l.setStreet("");
     }
     
     @Test(expected = CharactersMissingException.class)
     public void testSetStreetContainsIlegalCharacterFailure() {
         Location l = new Location();
         l.setStreet("Danden@ong Road");
     }
     
     //</editor-fold>
  
    //<editor-fold desc="landmark test cases">

    @Test
    public void testSetLandmarkValidSuccess() {
        Location l = new Location();
        l.setLandmark("Down by the wharf");
    }
     
    @Test
    public void testSetLandmarkNullSuccess() {
        Location l = new Location();
        l.setLandmark(null);
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetLandmarkLengthLessThanMinFailure() {
        Location l = new Location();
        l.setLandmark("");
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetLandmarkLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setLandmark("This landmark is longer than 30 chars.");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetLandmarkContainsIllegalCharacterFailure() {
        Location l = new Location();
        l.setLandmark("Near Racecourse??");
    }
    
    //</editor-fold>
     
    //<editor-fold desc="town test cases">

    @Test
    public void testSetTownValidSuccess() {
        Location l = new Location();
        l.setTown("Launceston");
     }
    
    @Test(expected = NullArgumentException.class)
    public void testSetTownNullFailure() {
        Location l = new Location();
        l.setTown(null);
     }
    
    @Test(expected = StringTooShortException.class)
    public void testSetTownLengthLessThanMinFailure() {
        Location l = new Location();
        l.setTown("G");
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetTownLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setTown("Geelong is a town with lenght greater than 30 chars");
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetTownEmptyFailure() {
        Location l = new Location();
        l.setTown("");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetTownContainsNumberFailure() {
        Location l = new Location();
        l.setTown("Melbou2gh1");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetTownContainsIllegalCharacterFailure() {
        Location l = new Location();
        l.setTown("Melbou%ne");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="region test cases">

    @Test
    public void testSetRegionValidSuccess() {
        Location l = new Location();
        l.setRegion("Tasmania");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetRegionNullFailure() {
        Location l = new Location();
        l.setRegion(null);
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetRegionLengthLessThanMinFailure() {
        Location l = new Location();
        l.setRegion("V");
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetRegionLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setRegion("This region is longer than 30 chars.");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetRegionContainsIllegalCharacterFailure() {
        Location l = new Location();
        l.setRegion("Victoria!");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="postcode test cases">

    @Test
    public void testSetPostcodeValidSuccess() {
        Location l = new Location();
        l.setPostcode("3002");
    }
    
    @Test
    public void testSetPostcodeNullSuccess() {
        Location l = new Location();
        l.setPostcode(null);
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetPostcodeLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setPostcode("0123456789-0123456789");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetPostcodeContainsIllegalCharacterFailure() {
        Location l = new Location();
        l.setPostcode("30_31'");
    }
    
    //</editor-fold>
    
    //<editor-fold desc="country test cases">
    
    @Test
    public void testSetCountryValidSuccess() {
        Location l = new Location();
        l.setCountry("New Zealand");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetCountryNullFailure() {
        Location l = new Location();
        l.setCountry(null);
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetCountryLengthLessThanMinFailure() {
        Location l = new Location();
        l.setCountry("N");
    }
    
    @Test(expected = StringTooLongException.class)
    public void testSetCountryLengthGreaterThanMaxFailure() {
        Location l = new Location();
        l.setCountry("New Zealand is a country that is longer than 32 chars");
    }
    
    @Test(expected = StringTooShortException.class)
    public void testSetCountryEmptyFailure() {
        Location l = new Location();
        l.setCountry("");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetCountryContainsNumberFailure() {
        Location l = new Location();
        l.setCountry("Aus2ralia");
    }
    
    @Test(expected = CharactersMissingException.class)
    public void testSetCountryContainsIllegalCharacterFailure() {
        Location l = new Location();
        l.setCountry("Aus.tr-alia");
    }
    
    //</editor-fold>
    
}
