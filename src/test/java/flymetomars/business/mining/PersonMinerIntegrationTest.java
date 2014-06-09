package flymetomars.business.mining;

import flymetomars.business.SocialCircle;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test cases for PersonMiner class
 * 
 * @author Arinjoy Biswas
 */
public class PersonMinerIntegrationTest {
    
    private PersonMiner personMiner;
 //   private PersonDAO personDao;
    private EntityLoader loader;
    
    private Person p1,p2,p3,p4,p5,p6,p7,p8,p9,p10;
    private Mission m1,m2,m3,m4,m5;
    private Invitation i1,i2,i3,i4;
    
    
    @Before
    public void doBeforeEachTestCase() {        
                  
      personMiner = new PersonMiner(null);
            
        p1 = Mockito.mock(Person.class);
        Mockito.when(p1.getUserName()).thenReturn("p1_user");
        p2 = Mockito.mock(Person.class);
        Mockito.when(p2.getUserName()).thenReturn("p2_user");
        p3 = Mockito.mock(Person.class);
        Mockito.when(p3.getUserName()).thenReturn("p3_user");
        p4 = Mockito.mock(Person.class);
        Mockito.when(p4.getUserName()).thenReturn("p4_user");
        p5 = Mockito.mock(Person.class);
        Mockito.when(p5.getUserName()).thenReturn("p5_user");
        p6 = Mockito.mock(Person.class);
        Mockito.when(p6.getUserName()).thenReturn("p6_user");
        p7 = Mockito.mock(Person.class);
        Mockito.when(p7.getUserName()).thenReturn("p7_user");
        p8 = Mockito.mock(Person.class);
        Mockito.when(p8.getUserName()).thenReturn("p8_user");
        p9 = Mockito.mock(Person.class);
        Mockito.when(p9.getUserName()).thenReturn("p9_user");
        p10 = Mockito.mock(Person.class);
        Mockito.when(p10.getUserName()).thenReturn("p10_user");
        
        m1 = Mockito.mock(Mission.class);
        Mockito.when(m1.getName()).thenReturn("m1 mission");
        m2 = Mockito.mock(Mission.class);
        Mockito.when(m2.getName()).thenReturn("m2 mission");
        m3 = Mockito.mock(Mission.class);
        Mockito.when(m3.getName()).thenReturn("m3 mission");
        m4 = Mockito.mock(Mission.class);
        Mockito.when(m4.getName()).thenReturn("m4 mission");
        m5 = Mockito.mock(Mission.class);
        Mockito.when(m5.getName()).thenReturn("m5 mission");
           
        i1 = Mockito.mock(Invitation.class);
        i2 = Mockito.mock(Invitation.class);
        i3 = Mockito.mock(Invitation.class);
        i4 = Mockito.mock(Invitation.class);
    }
    
    /**
     * Test case for getting only the top celebrity
     */
    @Test
    public void testGetTopKCelebritiesOnlyTheTopSuccess(){
        
        List<Person> pList = new ArrayList<Person>();
        pList.add(p1);
        pList.add(p2);
        pList.add(p3);     
        
        // mocking the load behaviour from core to give all the peoople from database
        loader = Mockito.mock(EntityLoader.class);      
        Mockito.when(this.loader.loadAllPersons()).thenReturn(pList);      
        personMiner = new PersonMiner(loader);
        
        Set<Invitation> iSet1 = new HashSet<Invitation>();
        Set<Invitation> iSet2 = new HashSet<Invitation>();
        Set<Invitation> iSet3 = new HashSet<Invitation>();     
        iSet1.add(i1);
        iSet1.add(i2);
        iSet1.add(i3);
        iSet2.add(i1);
        iSet2.add(i4);
        iSet3.add(i4);
        Mockito.when(p1.getInvitationsReceived()).thenReturn(iSet1);
        Mockito.when(p2.getInvitationsReceived()).thenReturn(iSet2);
        Mockito.when(p3.getInvitationsReceived()).thenReturn(iSet3);

        List<Person> topCelebrities = personMiner.mineTopCelebrities(1);
        Assert.assertEquals(topCelebrities.size(),1);
        Assert.assertTrue(topCelebrities.contains(p1));
    }
      
    /**
     * Test case for getting the top-3 celebrities
     */  
    @Test
    public void testGetTopKCelebritiesOnlyTopThreeSuccess(){
        
        List<Person> pList = new ArrayList<Person>();
        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);
                
        loader = Mockito.mock(EntityLoader.class);     
        Mockito.when(this.loader.loadAllPersons()).thenReturn(pList);       
        personMiner = new PersonMiner(loader);

        Set<Invitation> iSet1 = Mockito.mock(HashSet.class);
        Mockito.when(iSet1.size()).thenReturn(15);
        Set<Invitation> iSet2 = Mockito.mock(HashSet.class);
        Mockito.when(iSet2.size()).thenReturn(10);
        Set<Invitation> iSet3 = Mockito.mock(HashSet.class);
        Mockito.when(iSet3.size()).thenReturn(5);
        Set<Invitation> iSet4 = Mockito.mock(HashSet.class);
        Mockito.when(iSet3.size()).thenReturn(1);

        Mockito.when(p1.getInvitationsReceived()).thenReturn(iSet1);
        Mockito.when(p2.getInvitationsReceived()).thenReturn(iSet2);
        Mockito.when(p3.getInvitationsReceived()).thenReturn(iSet3);
        Mockito.when(p4.getInvitationsReceived()).thenReturn(iSet4);

        List<Person> results = personMiner.mineTopCelebrities(2);
        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains(p1));
        Assert.assertTrue(results.contains(p2));

        List<Person> results2 = personMiner.mineTopCelebrities(3);
        Assert.assertEquals(3, results2.size());
        Assert.assertTrue(results2.contains(p1));
        Assert.assertTrue(results2.contains(p2));
        Assert.assertTrue(results2.contains(p3));
        Assert.assertEquals(p1, results2.get(0));
        Assert.assertEquals(p2, results2.get(1));
        Assert.assertEquals(p3, results2.get(2));
    }
    
    /**
     * Test case for getting the top-2 celebrities
     */  
    @Test
    public void testGetTopKCelebritiesOnlyTopTwoSuccess(){
        
        List<Person> pList = new ArrayList<Person>();
        pList.add(p1);
        pList.add(p2);
        pList.add(p3);
        pList.add(p4);
                
        loader = Mockito.mock(EntityLoader.class);      
        Mockito.when(this.loader.loadAllPersons()).thenReturn(pList);    
        personMiner = new PersonMiner(loader);

        Set<Invitation> iSet1 = Mockito.mock(HashSet.class);
        Mockito.when(iSet1.size()).thenReturn(15);
        Set<Invitation> iSet2 = Mockito.mock(HashSet.class);
        Mockito.when(iSet2.size()).thenReturn(10);
        Set<Invitation> iSet3 = Mockito.mock(HashSet.class);
        Mockito.when(iSet3.size()).thenReturn(5);
        Set<Invitation> iSet4 = Mockito.mock(HashSet.class);
        Mockito.when(iSet3.size()).thenReturn(1);

        Mockito.when(p1.getInvitationsReceived()).thenReturn(iSet1);
        Mockito.when(p2.getInvitationsReceived()).thenReturn(iSet2);
        Mockito.when(p3.getInvitationsReceived()).thenReturn(iSet3);
        Mockito.when(p4.getInvitationsReceived()).thenReturn(iSet4);

        List<Person> results = personMiner.mineTopCelebrities(2);
        Assert.assertEquals(2, results.size());
        Assert.assertTrue(results.contains(p1));
        Assert.assertTrue(results.contains(p2));
        Assert.assertEquals(p1, results.get(0));
        Assert.assertEquals(p2, results.get(1));
    }
    
     /**
     * Test case for getting top-2 celebrities when there are NO
     * celebrities at all
     */
    @Test
    public void testGetTopKCelebritiesWitoutAnyExistingPersonSuccess() {
        
        List<Person> pList = Collections.emptyList();
                    
        loader = Mockito.mock(EntityLoader.class);      
        Mockito.when(this.loader.loadAllPersons()).thenReturn(pList);       
        personMiner = new PersonMiner(loader);
       
        List<Person> topCelebrities = personMiner.mineTopCelebrities(2);
        Assert.assertEquals(topCelebrities.size(),0);
    }
    
    /**
     * Test case for getting the top-0 celebrities which will fail
     */  
    @Test(expected=IllegalArgumentException.class)
    public void testGetTopKCelebritiesWhenZeroResultsAskedFailure() {
        
        List<Person> topCelebrities = personMiner.mineTopCelebrities(0);
        
    }
     /**
     * Test case for getting the direct colleagues of a null person
     */
     @Test(expected=NullArgumentException.class)
     public void testGetColleaguesEmptyPersonInputFailure() {  
        // the direct colleagues of a null person are being searhched
        Set<Person> result = personMiner.mineColleagues(null);
    }
     
     /**
     * Test case for getting the direct colleagues of person where he
     * has no direct colleagues
     */
     @Test
     public void testGetColleaguesNoSuchColleaguesSuccess() {  
         
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m3);
        // person p1 registered two missions m1 & m3
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet1);
        
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet2);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p2);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        Set<Person> result = personMiner.mineColleagues(p1);
        Assert.assertEquals(0, result.size());
    } 
     
    /**
     * Test case for getting the Colleagues of Colleagues of a person
     */
     @Test
     public void testGetColleaguesOfColleaguesSuccess() {
   
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        // person p1 registered two missions m1 & m2
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p5);
        // mission m1 has p1,p2,p5 as participants
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Mission> p2MSet = new HashSet<Mission>();
        p2MSet.add(m1);
        p2MSet.add(m3);
        // person p2 is regsitered for m1, and m3
        Mockito.when(p2.getMissionsRegistered()).thenReturn(p2MSet);
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p6);
        pSet3.add(p7);
        // mission m3 has p2, p6 and p7
         Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p3);
        pSet2.add(p4);
        // mission m2 has p1,p3,p4 as participants
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        Set<Mission> p4MSet = new HashSet<Mission>();
        p2MSet.add(m2);
        p2MSet.add(m4);
        // person p2 is regsitered for m1, and m3
        Mockito.when(p4.getMissionsRegistered()).thenReturn(p4MSet);
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p4);
        pSet4.add(p7);
        pSet4.add(p8);
        // mission m4 has p4, p7 and p8
         Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);
          
        // the colleagues and their colleagues are being searhched
        Set<Person> result = personMiner.mineFriendsOfFriends(p1);
        Assert.assertEquals(3,result.size());
        Assert.assertTrue(result.contains(p6));
        Assert.assertTrue(result.contains(p7));
        Assert.assertTrue(result.contains(p8));        
    }
     
     /**
     * Test case for getting the Colleagues of Colleagues of a person
     * when each colleagues has no more new colleagues other than themselves
     */
     @Test
     public void testGetColleaguesOfColleaguesWhenNoFutherColleaguesSuccess() {
   
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        // person p1 registered two missions m1 & m2
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p5);
        // m1 has p1, p2, p5
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p3);
        pSet2.add(p4);
        // mission m2 has p1,p3,p4 as participants
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        // the colleagues and their colleagues are being searched
        Set<Person> result = personMiner.mineFriendsOfFriends(p1);
        // none of p2, p3, p3, p4 has any further colleagues
        Assert.assertEquals(0,result.size());       
    }
      
    /* Test case for getTopKBuddies, i.e. the top-1, i.e. the best friend
     * of a person
     */ 
    @Test
    public void testGetTopKBuddiesOnlyTheTopSuccess(){
       Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        mSet.add(m3);
        mSet.add(m4);
        // p1 is the person whose top-k buddies will be searched for
        // p1 registers for m1,m2,m3,m4
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        //p2,p3,p4,p5 are all related persons/friends of p1

        //participants are being set for m1,m2,m3,m4
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        pSet1.add(p4);
        //m1 has p1,p2,p3,p4
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p2);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p6);
        //m2 has p1,p2,p3,p4,p6
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p1);
        pSet3.add(p2);
        pSet3.add(p3);
        //m3 has p1,p2,p3
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);

        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p1);
        pSet4.add(p2);
        pSet4.add(p4);
        pSet4.add(p5);
        pSet4.add(p6);
        // m4 has p1,p2,p5,p6
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);

        // top-1 buddy of p1 is being searched
        List<Person> buddies = personMiner.mineTopFriends(p1, 1);
        Assert.assertEquals(1, buddies.size());
        Assert.assertTrue(buddies.contains(p2));
    }
    
    /* Test case for getTopKBuddies, i.e. the top-3 friends
     * of a person
     */ 
    @Test
    public void testGetTopKBuddiesOnlyTheTopThreeSuccess(){
       Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        mSet.add(m3);
        mSet.add(m4);
        // p1 is the person whose top-k buddies will be searched for
        // p1 registers for m1,m2,m3,m4
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        //p2,p3,p4,p5 are all related persons/friends of p1

        //participants are being set for m1,m2,m3,m4
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        pSet1.add(p4);
        //m1 has p1,p2,p3,p4
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p2);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p6);
        //m2 has p1,p2,p3,p4,p6
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p1);
        pSet3.add(p2);
        pSet3.add(p3);
        //m3 has p1,p2,p3
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);

        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p1);
        pSet4.add(p2);
        pSet4.add(p4);
        pSet4.add(p5);
        pSet4.add(p6);
        // m4 has p1,p2,p5,p6
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);

        // top-3 buddies of p1 is being searched
        List<Person> buddies = personMiner.mineTopFriends(p1, 3);
        Assert.assertEquals(3, buddies.size());
        // make sure only p2,p3,p4 are top-3 friends
        Assert.assertTrue(buddies.contains(p2));
        Assert.assertTrue(buddies.contains(p3));
        Assert.assertTrue(buddies.contains(p4));
        // make sure p2 comes as the top of the list
        Assert.assertEquals(buddies.get(0),p2);
        // make sure p5 and p6 are not on the top-3 friends
        Assert.assertFalse(buddies.contains(p5));
        Assert.assertFalse(buddies.contains(p6));
    }
    
     /* Test case for getTopKBuddies, i.e. the top-1, i.e. the best friend
     * of a person
     */ 
    @Test
    public void testGetTopKBuddiesWhenTooManyAskedSuccess(){
       Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        mSet.add(m3);
        mSet.add(m4);
        // p1 is the person whose top-k buddies will be searched for
        // p1 registers for m1,m2,m3,m4
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        //p2,p3,p4,p5 are all related persons/friends of p1

        //participants are being set for m1,m2,m3,m4
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        pSet1.add(p4);
        //m1 has p1,p2,p3,p4
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p2);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p6);
        //m2 has p1,p2,p3,p4,p6
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p1);
        pSet3.add(p2);
        pSet3.add(p3);
        //m3 has p1,p2,p3
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);

        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p1);
        pSet4.add(p2);
        pSet4.add(p4);
        pSet4.add(p5);
        pSet4.add(p6);
        // m4 has p1,p2,p5,p6
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);

        // top-10 buddy of p1 is being searched
        List<Person> buddies = personMiner.mineTopFriends(p1, 10);
        Assert.assertEquals(5, buddies.size());
        Assert.assertTrue(buddies.contains(p2));
        Assert.assertTrue(buddies.contains(p2));
        Assert.assertTrue(buddies.contains(p3));
        Assert.assertTrue(buddies.contains(p5));
        Assert.assertTrue(buddies.contains(p6));
        
    }
    
     /**
     * Test case for getting the top-2 buddies where a person
     * does not have nay buddy/colleagues at all
     */
     @Test(expected=IllegalArgumentException.class)
     public void testGetTopKBuddiesWhenNoBuddyExistsFailure() {  
         
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m3);
        // person p1 registered two missions m1 & m3
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet1);
        
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet2);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p2);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        List<Person> result = personMiner.mineTopFriends(p1, 2);
        Assert.assertEquals(0, result.size());
    } 
    
    /**
     * Test case for top-K buddies when null person is searched
     */
     @Test(expected=NullArgumentException.class)
     public void testGetTopKBuddiesEmptyPersonInputFailure() {  
                
        List<Person> result = personMiner.mineTopFriends(null, 2);
     } 
     
     /**
     * Test case for top-K buddies when zero buddies are searched
     */
     @Test(expected=IllegalArgumentException.class)
     public void testGetTopKBuddieWhenZeroBuddiesAskedFailure() {  
                
        List<Person> result = personMiner.mineTopFriends(p1, 0);
     } 
       
    /**
     * Test case for for finding the list of social circles of
     * a person.
     */
    @Test
    public void testGetPersonAllSocialCirclesSuccess(){
        // p1 is the input
        //the missions p1 has attended are m1 and m2
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p4);
        // m1 has p1, p2, p4
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        pSet2.add(p6);
        pSet2.add(p7);
        // m2 has p1,p3,p4,p5,p6,p7
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p3);
        pSet3.add(p4);
        // m3 has p2,p3,p4
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);       
        // the following makes the situation: p2,p3,p4 all registered for m3       
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        mSet1.add(m3);
        // p2 has missions m1 and m3
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet1);       
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        mSet2.add(m3);
        // p3 has missions m2 and m3
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet2);       
        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m2);
        mSet3.add(m3);
        // p4 has missions m2 and m3
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet3);       
        List<SocialCircle> result = personMiner.mineSocialCircles(p1);
        // two misson groups p1 has
        Assert.assertEquals(2,result.size());
        Assert.assertTrue((result.get(0).size() == m1.getParticipantSet().size() && result.get(1).size()== m2.getParticipantSet().size()) || (result.get(0).size() == m2.getParticipantSet().size() || result.get(1).size()==m1.getParticipantSet().size()));
    }
    
    /**
     * Testing of all the social circles of person where he has no such social circles
     */
    @Test
    public void testGetPersonAllSocialCirclesWhenHeHasNoFriendsExistSuccess(){
        // p1 is the input
        //the missions p1 has attended are m1 and m2
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
             
        List<SocialCircle> result = personMiner.mineSocialCircles(p1);
        // two misson groups p1 has but both have only himself the person as he has no friends
        Assert.assertEquals(2,result.size());
        Assert.assertTrue(result.get(0).size()==1 && result.get(1).size()==1);
    }
    
    @Test (expected = NullArgumentException.class)
    public void testGetPersonAllSocialCirclesNullPersonInputFailure() {
        List<SocialCircle> result = personMiner.mineSocialCircles(null);
    }
    
    /**
     * Test case for for finding the maximum social circle of
     * a person.
     */
    @Test
    public void testGetMaxPersonMissionSocialCircleSuccess(){
        // p1 is the input
        //the missions p1 has attended are m1 and m2
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p4);
        // m1 has p1, p2, p4
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);

        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        pSet2.add(p6);
        pSet2.add(p7);
        // m2 has p1,p3,p4,p5,p6,p7
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p3);
        pSet3.add(p4);
        // m3 has p2,p3,p4
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);
        
        // the following makes the situation: p2,p3,p4 all registered for m3
        
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        mSet1.add(m3);
        // p2 has missions m1 and m3
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet1);
        
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        mSet2.add(m3);
        // p3 has missions m2 and m3
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet2);
        
        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m2);
        mSet3.add(m3);
        // p4 has missions m2 and m3
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet3);
        
        Set<Person> result = personMiner.mineMaxSocialCircle(p1);
        Assert.assertEquals(6,result.size());
        Assert.assertTrue(result.contains(p1));
        Assert.assertTrue(result.contains(p3));
        Assert.assertTrue(result.contains(p4));
        Assert.assertTrue(result.contains(p5));
        Assert.assertTrue(result.contains(p6));
        Assert.assertTrue(result.contains(p7));
    }
    
    /**
     * Testing of the max social circle of person where he has no such social circles
     */
    @Test
    public void testGetPersonMaxSocialCircleWhenHeHasNoFriendsSuccess(){
        // p1 is the input
        //the missions p1 has attended are m1 and m2
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(m1);
        mSet.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet);
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p1);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
             
        Set<Person> result = personMiner.mineMaxSocialCircle(p1);
        // two misson groups p1 has but both have only himself the person as he has no friends
        // max social circle will be related to one of the missions m1 and m2
        Assert.assertEquals(1,result.size());
        // no matter what both will have size 1 i.e. himself
        Assert.assertTrue(result.size()==1 || result.size()==1);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testGetPersonMaxSocialCircleNullPersonInputFailure() {
        Set<Person> result = personMiner.mineMaxSocialCircle(null);
    }
    
   /**
    * Testing of power brokers of two social circles 
    */
    @Test
    public void testGetPowerBrokersOfTwoMissionSocialCirclesSuccess(){
        
        // m1 and m2 are missions for input social circles
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet2);

        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m1);
        mSet3.add(m3);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet3);

        Set<Mission> mSet4 = new HashSet<Mission>();
        mSet4.add(m2);
        mSet4.add(m3);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet4);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        // interactions between these 2 social circles
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p4);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);

        // m1 has p1, p2
        // m2 has p3, p4, p5 
        // thresold 2 x 3 /2 = 3
        // p2 belongs to both m1 and m2 via 1 connection
        // p4 belongs to both m1 and m2 via 1 connection
        // thus total interactions = 1+1 = 2 < 3 (i.e. threshold)
        // p2 qand p4 should be power broker   
        Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(m1,m2);
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(p2));
        Assert.assertTrue(result.contains(p4));
    }
    
    /**
    * Testing of power brokers of two social circles  when no such power broker exists
    */
    @Test
    public void testGetPowerBrokersOfTwoMissionSocialCirclesNoPowerBrokerSuccess(){
        
        // m1 and m2 are missions for input social circles
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet2);

        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m1);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet3);

        Set<Mission> mSet4 = new HashSet<Mission>();
        mSet4.add(m2);
        mSet4.add(m3);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet4);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        // interactions between these 2 social circles
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p4);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);
 
        Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(m1,m2);
        Assert.assertEquals(0, result.size());
    } 
    
    @Test(expected = NullArgumentException.class)
    public void testGetPowerBrokersOfTwoMissionSocialCirclesNullInput1Failure() {
    Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(null,m2);
    }
    
    @Test(expected = NullArgumentException.class)
    public void testGetPowerBrokersOfTwoMissionSocialCirclesNullInput2Failure() {
    Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(m1,null);
    }
    
    /**
    * Testing of the most powerful brokers of all social circles
    */
    @Test
    public void testGetTheMostPowerfulBrokersOfAllSocialCircles(){
        
        // m1 and m2 are missions which are most non-interacting
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet2);

        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m1);
        mSet3.add(m3);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet3);

        Set<Mission> mSet4 = new HashSet<Mission>();
        mSet4.add(m2);
        mSet4.add(m3);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet4);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);

        // interactions between these 2 social circles
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p2);
        pSet3.add(p4);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);

        // m1 has p1, p2
        // m2 has p3, p4, p5 
        // thresold 2 x 3 /2 = 3
        // p2 belongs to both m1 and m2 via 1 connection
        // p4 belongs to both m1 and m2 via 1 connection
        // thus total interactions = 1+1 = 2 < 3 (i.e. threshold)
        // p2 qand p4 should be power broker   
        
        List<Mission> allMissionList = new ArrayList<Mission>();
        allMissionList.add(m1);
        allMissionList.add(m2);
        
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p1);
        pSet4.add(p2);
        pSet4.add(p3);
        pSet4.add(p6);
        pSet4.add(p8);
        pSet4.add(p10);
        Set<Person> pSet5 = new HashSet<Person>();
        pSet5.add(p1);
        pSet5.add(p2);
        pSet4.add(p3);
        pSet5.add(p9);
        pSet5.add(p10);
        pSet5.add(p7);  
        pSet5.add(p8); 
        // m4 and m5 are certainly most non interacting with m1 and m2
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);
        Mockito.when(m5.getParticipantSet()).thenReturn(pSet5);
        
        allMissionList.add(m3);
        allMissionList.add(m4);
        allMissionList.add(m5);
         
        loader = Mockito.mock(EntityLoader.class);     
        // all the missions from the database are m1,m2,m3,m4,m5
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissionList);       
        personMiner = new PersonMiner(loader);
            
        Set<Person> result = personMiner.getTheMostPowerfulBrokersOfAllSocialCircles();
        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(p2));
        Assert.assertTrue(result.contains(p4));
    }
    
    /**
    * Testing of the most powerful brokers of all social circles when no missions,
    * i.e. social circles exist
    */
    @Test
    public void testGetTheMostPowerfulBrokersWhenNoCircleExistSuccess() {
         List<Mission> allMissionList = new ArrayList<Mission>();
         
         loader = Mockito.mock(EntityLoader.class);     
        // all the missions from the database are empty
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissionList);       
        personMiner = new PersonMiner(loader);
            
        Set<Person> result = personMiner.getTheMostPowerfulBrokersOfAllSocialCircles();
    }
    
    /**
    * Testing of power brokers of two social circles that are directly interacting
    */
    @Test(expected = IllegalArgumentException.class)
    public void testGetPowerBrokersOfTwoMissionSocialCirclesAlreadyInteractingFailure(){
        
        // m1 and m2 are missions for input social circles
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet1);

        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m2);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet2);
        
        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m2);
        mSet3.add(m1);
        // p2 belongs in both m1 and m2
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet3);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p2);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        // m1 and m2 have directly interacting person p2
        Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(m1,m2);
    }
    
    /**
    * Testing of power brokers of two social circles where one of the mission 
    * is null
    */
    @Test(expected = NullArgumentException.class)
    public void testGetPowerBrokersOfTwoMissionSocialCirclesEmptyMissionInputFailure(){
        Set<Person> result = personMiner.getPowerBrokersOfTwoMissionSocialCircles(null, m2);
    }
    
    /**
     * Testing of new frontiers of a person when the number of new frontiers is
     * coincidentally the size that being asked for
     */
    @Test
    public void testGetNewFrontiersWhenAllSearchedSuccess() {
        
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);
        
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m1);
        mSet2.add(m2);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet2);
        
        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m1);
        mSet3.add(m2);
        mSet3.add(m3);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet3);
        
        Set<Mission> mSet4 = new HashSet<Mission>();
        mSet4.add(m2);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet4);
        
        Set<Mission> mSet5 = new HashSet<Mission>();
        mSet5.add(m2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet5);
        
        Set<Mission> mSet6 = new HashSet<Mission>();
        mSet6.add(m3);
        Mockito.when(p6.getMissionsRegistered()).thenReturn(mSet6);
        
        Set<Mission> eP7 = new HashSet<Mission>();
        eP7.add(m4);
        Mockito.when(p7.getMissionsRegistered()).thenReturn(eP7);
        
        Set<Mission> mSet8 = new HashSet<Mission>();
        mSet8.add(m4);
        Mockito.when(p8.getMissionsRegistered()).thenReturn(mSet8);
        
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p2);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p3);
        pSet3.add(p6);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);
        
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p7);
        pSet4.add(p8);
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);
        
        List<Person> allPersons = new ArrayList<Person>();
        allPersons.add(p1);
        allPersons.add(p2);
        allPersons.add(p3);
        allPersons.add(p4);
        allPersons.add(p5);
        allPersons.add(p6);
        allPersons.add(p7);
        allPersons.add(p8);
        allPersons.add(p9);
        
        loader = Mockito.mock(EntityLoader.class);  
        // mcoking the behaviour from core to give all the people from database via dao
        Mockito.when(this.loader.loadAllPersons()).thenReturn(allPersons); 
        personMiner = new PersonMiner(loader);
        // setting 2 level of connections and 3 such frontiers asked
        Set<Person> result = personMiner.mineNewFroniters(p1, 2, 3);
        // with three level of connections only p2 and p3 will match
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(p7));
        Assert.assertTrue(result.contains(p8));
    }
    
     /**
     * Testing of new frontiers of a person when only a some of all the new frontiers are
     * being searched
     */
    @Test
    public void testGetNewFrontiersWhenAPartOfAllSearchedSuccess() {
        
        Set<Mission> mSet1 = new HashSet<Mission>();
        mSet1.add(m1);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mSet1);
        
        Set<Mission> mSet2 = new HashSet<Mission>();
        mSet2.add(m1);
        mSet2.add(m2);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mSet2);
        
        Set<Mission> mSet3 = new HashSet<Mission>();
        mSet3.add(m1);
        mSet3.add(m2);
        mSet3.add(m3);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mSet3);
        
        Set<Mission> mSet4 = new HashSet<Mission>();
        mSet4.add(m2);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mSet4);
        
        Set<Mission> mSet5 = new HashSet<Mission>();
        mSet5.add(m2);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mSet5);
        
        Set<Mission> mSet6 = new HashSet<Mission>();
        mSet6.add(m3);
        Mockito.when(p6.getMissionsRegistered()).thenReturn(mSet6);
        
        Set<Mission> eP7 = new HashSet<Mission>();
        eP7.add(m4);
        Mockito.when(p7.getMissionsRegistered()).thenReturn(eP7);
        
        Set<Mission> mSet8 = new HashSet<Mission>();
        mSet8.add(m4);
        Mockito.when(p8.getMissionsRegistered()).thenReturn(mSet8);
        
        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p1);
        pSet1.add(p2);
        pSet1.add(p3);
        Mockito.when(m1.getParticipantSet()).thenReturn(pSet1);
        
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p2);
        pSet2.add(p3);
        pSet2.add(p4);
        pSet2.add(p5);
        Mockito.when(m2.getParticipantSet()).thenReturn(pSet2);
        
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p3);
        pSet3.add(p6);
        Mockito.when(m3.getParticipantSet()).thenReturn(pSet3);
        
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p7);
        pSet4.add(p8);
        Mockito.when(m4.getParticipantSet()).thenReturn(pSet4);
        
        List<Person> allPersons = new ArrayList<Person>();
        allPersons.add(p1);
        allPersons.add(p2);
        allPersons.add(p3);
        allPersons.add(p4);
        allPersons.add(p5);
        allPersons.add(p6);
        allPersons.add(p7);
        allPersons.add(p8);
        allPersons.add(p9);
        
        loader = Mockito.mock(EntityLoader.class); 
        // mocking the behaviour from core to give all the people from database via dao
        Mockito.when(this.loader.loadAllPersons()).thenReturn(allPersons);
        personMiner = new PersonMiner(loader);
        // setting 3 level of connections and 1 such frontiers asked
        Set<Person> result = personMiner.mineNewFroniters(p1, 3, 1);
      
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(result.contains(p6) || result.contains(p7)||result.contains(p8));
    }
    
    @Test (expected = NullArgumentException.class)
    public void testGetNewFrontiersInpuNullPersonFailure() {
        loader = Mockito.mock(EntityLoader.class); 
        // mocking the behaviour from core to give all the people from database via dao
        Mockito.when(this.loader.loadAllPersons()).thenReturn(null);
        personMiner = new PersonMiner(loader);
        // setting 3 level of connections and 1 such frontiers asked but target person is null
        Set<Person> result = personMiner.mineNewFroniters(null, 3, 1);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetNewFrontiersNegativeConnectionLevelInputFailure() {
        loader = Mockito.mock(EntityLoader.class); 
        // mocking the behaviour from core to give all the people from database via dao
        Mockito.when(this.loader.loadAllPersons()).thenReturn(null);
        personMiner = new PersonMiner(loader);
        // setting -1 level of connections and 1 such frontiers asked
        Set<Person> result = personMiner.mineNewFroniters(new Person(), -1, 1);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testGetNewFrontiersNegativeResultsAskedKFailure() {
        loader = Mockito.mock(EntityLoader.class); 
        // mocking the behaviour from core to give all the people from database via dao
        Mockito.when(this.loader.loadAllPersons()).thenReturn(null);
        personMiner = new PersonMiner(loader);
        // setting 2 levels of connections and -1 such frontiers asked
        Set<Person> result = personMiner.mineNewFroniters(new Person(), 2, -1);
    }

}
