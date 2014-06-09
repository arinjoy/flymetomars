package flymetomars.business.model;

import flymetomars.common.NullArgumentException;
import org.junit.Assert;
import org.junit.Test;

/**
 * This is a unit test class that has test cases for the salt model
 * 
 * @author Lawrence Colman
 */
public class SaltUnitTest {

    //<editor-fold desc="ObfuscatedSalt Property Tests">
    
    @Test
    public void testGetObfuscatedSaltSuccess() {
        Salt instance = new Salt();
        instance.setPureSalt("ThisIs4passw0rd!");
        String expResult = "VGhpc0lzNHBhc3N3MHJkIQ==";
        String result = instance.getObfuscatedSalt();
        Assert.assertEquals(expResult,result);
    }
    
    @Test
    public void testGetObfuscatedSaltEmptyByDefaultSuccess() {
        Salt instance = new Salt();
        String result = instance.getObfuscatedSalt();
        Assert.assertNotNull(result);
        Assert.assertEquals("",result);
    }
    
    @Test
    public void testSetObfuscatedSaltSuccess() {
        String salty = "VGhpc0lzNHBhc3N3MHJkIQ==";
        Salt instance = new Salt();
        instance.setObfuscatedSalt(salty);
        Assert.assertEquals(salty,instance.getObfuscatedSalt());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetObfuscatedSaltNullFailure() {
        Salt instance = new Salt();
        instance.setObfuscatedSalt(null);
    }
    
    //</editor-fold>

    //<editor-fold desc="PureSalt Property Tests">
    
    @Test
    public void testGetPureSaltSuccess() {
        Salt instance = new Salt();
        instance.setObfuscatedSalt("VGhpc0lzNHBhc3N3MHJkIQ==");
        Assert.assertEquals("ThisIs4passw0rd!",instance.getPureSalt());
    }
    
    @Test
    public void testGetPureSaltEmptyByDefaultSuccess() {
        Salt instance = new Salt();
        Assert.assertEquals("",instance.getPureSalt());
    }
    
    @Test
    public void testSetPureSaltSuccess() {
        String actualSaltData = "ThisIs4passw0rd!";
        Salt instance = new Salt();
        instance.setPureSalt(actualSaltData);
        Assert.assertEquals(actualSaltData,instance.getPureSalt());
    }
    
    
    @Test(expected = NullArgumentException.class)
    public void testSetPureSaltNullFailure() {
        Salt instance = new Salt();
        instance.setPureSalt(null);
    }
    
    //</editor-fold>

    //<editor-fold desc="HashedSaltKey Property Tests">
    
    @Test
    public void testGetHashedSaltKeyEmptyByDefaultSuccess() {
        Salt instance = new Salt();
        Assert.assertEquals("",instance.getHashedSaltKey());
    }
    
    @Test
    public void testSetHashedSaltKeySuccess() {
        String hashedSaltKey = "53414C54";
        Salt instance = new Salt();
        instance.setHashedSaltKey(hashedSaltKey);
        Assert.assertEquals(hashedSaltKey,instance.getHashedSaltKey());
    }
    
    @Test(expected = NullArgumentException.class)
    public void testSetHashedSaltKeyNullFailure() {
        Salt instance = new Salt();
        instance.setHashedSaltKey(null);
    }
    
    //</editor-fold>
    
}
