package flymetomars.business;

import flymetomars.business.model.Person;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 * Basic test of the Set encapsulation SocialCircle custom type
 * 
 * @author Lawrence Colman
 */
public class SocialCircleUnitTest {
    
    private SocialCircle sc;
    
    @Before
    public void setUp() {
        this.sc=new SocialCircle(new HashSet<Person>());
    }

    /**
     * Test of size method, of class SocialCircle.
     */
    @Test
    public void testSizeSuccess() {
        Assert.assertEquals(0, this.sc.size());
    }

    /**
     * Test of isEmpty method, of class SocialCircle.
     */
    @Test
    public void testIsEmptySuccess() {
        Assert.assertTrue(this.sc.isEmpty());
    }

    /**
     * Test of contains method, of class SocialCircle.
     */
    @Test
    public void testContainsSuccess() {
        Person p=new Person();
        Assert.assertFalse(this.sc.contains(p));
    }

    /**
     * Test of iterator method, of class SocialCircle.
     */
    @Test(expected = NoSuchElementException.class)
    public void testIteratorFailure() {
        Iterator result = this.sc.iterator();
        Assert.assertNotNull(result);
        Assert.assertFalse(result.hasNext());
       result.next();  //throws exception
    }

    /**
     * Test of toArray method, of class SocialCircle.
     */
    @Test
    public void testToArrayDefaultSuccess() {
        Object[] expResult = new Person[0];
        Object[] result = this.sc.toArray();
        Assert.assertArrayEquals(expResult, result);
    }

    /**
     * Test of toArray method, of class SocialCircle.
     */
    @Test
    public void testToGenericTypeArraySuccess() {
        Person[] pa = new Person[0];
        Object[] expResult = new Person[0];
        Object[] result = this.sc.toArray(pa);
        Assert.assertArrayEquals(expResult, result);
    }

    /**
     * Test of add method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddFailure() {
        Assert.assertTrue(this.sc.add(new Person()));
        Assert.assertEquals(1, this.sc.size());
    }

    /**
     * Test of remove method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveFailure() {
        Assert.assertTrue(this.sc.add(new Person()));
        Assert.assertTrue(this.sc.remove(this.sc.iterator().next()));
    }

    /**
     * Test of containsAll method, of class SocialCircle.
     */
    @Test
    public void testContainsAllSuccess() {
        Person p = new Person();
        this.sc=new SocialCircle(new HashSet<Person>(Collections.singleton(p)));
        boolean result = this.sc.containsAll(Collections.singleton(p));
        Assert.assertTrue(result);
    }

    /**
     * Test of addAll method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddAllFailure() {
        Person p = new Person();
        boolean result = this.sc.addAll(Collections.singleton(p));
        Assert.assertTrue(result);
        Assert.assertFalse(this.sc.isEmpty());
    }

    /**
     * Test of retainAll method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRetainAllFailure() {
        Assert.assertTrue(this.sc.add(new Person()));
        Assert.assertFalse(this.sc.isEmpty());
        Person p = new Person();
        Assert.assertTrue(this.sc.add(p));
        Assert.assertEquals(2, this.sc.size());
        boolean result = this.sc.retainAll(Collections.singleton(p));
        Assert.assertTrue(result);
        Assert.assertEquals(1, this.sc.size());
    }

    /**
     * Test of removeAll method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAllFailure() {
        Assert.assertTrue(this.sc.add(new Person()));
        Assert.assertFalse(this.sc.isEmpty());
        Person p = new Person();
        Assert.assertTrue(this.sc.add(p));
        Assert.assertEquals(2, this.sc.size());
        boolean result = this.sc.removeAll(Collections.singleton(p));
        Assert.assertTrue(result);
        Assert.assertEquals(1, this.sc.size());
    }

    /**
     * Test of clear method, of class SocialCircle.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testClearFailure() {
        Assert.assertTrue(this.sc.add(new Person()));
        Assert.assertFalse(this.sc.isEmpty());
        Person p = new Person();
        Assert.assertTrue(this.sc.add(p));
        Assert.assertEquals(2, this.sc.size());
        this.sc.clear();
        Assert.assertTrue(this.sc.isEmpty());
        Assert.assertEquals(0, this.sc.size());
    }
}
