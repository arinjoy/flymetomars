package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the saltedPassword model
 * 
 * @author Lawrence Colman
 */
public class SaltedPasswordIntegrationTest {

    @Test
    public void testHasNullDigestByDefaultSuccess() {
        SaltedPassword sp = new SaltedPassword();
        Assert.assertNull(sp.getDigest());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDigestEmptyFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setDigest("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordEmptyFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("");
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetDigestNullFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setDigest(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDigestNotAHashFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setDigest("Ajoy_36#Bis%joy");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetDigestUnevenLengthFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("FFABC");
    }
    
    @Test
    public void testSetPasswordSuccess() {        
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("Ajoy_36#Bis%joy");
        Assert.assertTrue(pass.isPassword("Ajoy_36#Bis%joy"));
    }
    
    @Test(expected = NullPointerException.class)
    public void testSetPasswordNullSaltFailure() {        
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setPassword("Ajoy_36#Bis%joy");  //should throw exception
        pass.setSalt(salt); //setting salt AFTER setPassword - will never happen
    }
    
    @Test
    public void testIsPasswordNullSuccess() {
        SaltedPassword sp = new SaltedPassword();
        sp.setDigest("5F4DCC3B5AA765D61D8327DEB882CF99");
        Assert.assertFalse(sp.isPassword(null));
    }
	
    @Test(expected = NullArgumentException.class)
    public void testSetPasswordNullFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword(null);
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordTooShortFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("Ab1!");  //4 chars
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordTooLongFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("aSIN91g6_dash#hash$4rinj0y+s3cure-hash!");  //40 chars
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNoLettersFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("^$1-2-3#*");  //no letters char
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNoSpecialCharacterFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("abc123DEF");  //no special char
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithNoNumberFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("abJKfn#Kl");  //no numeric char
    }
	
    @Test(expected = IllegalArgumentException.class)
    public void testSetPasswordWithWhiteSpaceFailure() {
        SaltedPassword sp = new SaltedPassword();
        sp.setPassword("abc DEF %12h$	");
    }
    
    @Test
    public void testSetPasswordWithExtendedLatinSuccess() {   
        SaltedPassword pass=new SaltedPassword();
        Salt salt=new Salt();
        pass.setSalt(salt);  //the setSalt call must happen BEFORE setPassword.
        pass.setPassword("@3I0u_ÃËÌÓÛ");
    }
    
    @Test
    public void testSetPasswordActuallyUsesSaltSuccess() {
        String unsaltedHash="50DB60E9CACCD249A541381646940E21";
        String plainText="Ajoy_36#Bis%joy";
        SaltedPassword sp = new SaltedPassword();
        Salt seaWater=new Salt();
        seaWater.setObfuscatedSalt("");
        seaWater.setHashedSaltKey("534541");  //SEA
        sp.setSalt(seaWater);
        sp.setPassword(plainText);
        //actual test:
        Assert.assertTrue(sp.isPassword(plainText));
        Assert.assertEquals(unsaltedHash, sp.getDigest());  //no salting
        seaWater.setPureSalt("My body lays over the ocean, my body lays over the sea");
        sp.setPassword(plainText);  //with salting....
        Assert.assertTrue(sp.isPassword(plainText));  //must still work
        Assert.assertFalse(sp.getDigest().equals(unsaltedHash));
        Assert.assertTrue(sp.getDigest().equals("54B16DE2DFF63984B3DA7AFF7471A19E"));
    }
    
}
