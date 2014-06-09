package flymetomars.business.mining;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class MissionMinerIntegrationTest {
    
    private Person p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10;
    private Mission m0,m1,m2,m3,m4,m5,m6;
    private Invitation i0,i1,i2,i3,i4,i5,i6,i7,i8,i9,i10,i11,i12;
    private Date d1,d2,d3,d4,d5,d6;

    private MissionMiner missionMiner;
    private EntityLoader loader;
    

    
    @Before
    public void doBeforeEachTestCase() {              
        missionMiner = new MissionMiner(null);
        p0 = Mockito.mock(Person.class);
        Mockito.when(p0.getUserName()).thenReturn("p0_user");
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
        m0 = Mockito.mock(Mission.class);
        Mockito.when(m0.getName()).thenReturn("m0 mission");
        Mockito.when(m0.getTime()).thenReturn(new Date());
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
        m6 = Mockito.mock(Mission.class);
        Mockito.when(m6.getName()).thenReturn("m6 mission");
       
        Calendar c = Calendar.getInstance();
        
        c.set(2014, 4, 1);
        d1 = c.getTime();
        Mockito.when(m1.getTime()).thenReturn(d1);
        
        c.set(2014, 4, 5);
        d2 = c.getTime();
        Mockito.when(m2.getTime()).thenReturn(d2);
        
        c.set(2014, 4, 10);
        d3 = c.getTime();
        Mockito.when(m3.getTime()).thenReturn(d3);
        
        c.set(2014, 4, 15);
        d4 = c.getTime();
        Mockito.when(m4.getTime()).thenReturn(d4);
        
        c.set(2014, 4, 20);
        d5 = c.getTime();
        Mockito.when(m5.getTime()).thenReturn(d5);
        
        c.set(2014, 4, 20);
        d6 = c.getTime();
        Mockito.when(m6.getTime()).thenReturn(d6);
        
        i0 = Mockito.mock(Invitation.class);
        i1 = Mockito.mock(Invitation.class);
        i2 = Mockito.mock(Invitation.class);
        i3 = Mockito.mock(Invitation.class);
        i4 = Mockito.mock(Invitation.class);
        i5 = Mockito.mock(Invitation.class);
        i6 = Mockito.mock(Invitation.class);
        i7 = Mockito.mock(Invitation.class);
        i8 = Mockito.mock(Invitation.class);
        i9 = Mockito.mock(Invitation.class);
        i10 = Mockito.mock(Invitation.class);
        i11 = Mockito.mock(Invitation.class);
        i12 = Mockito.mock(Invitation.class);        
    }
    
    /**
     * Testing of sour grapes when it succeeds
     */
    @Test
    public void testGetSourGrapesWhenAllFutureMissionsSuccess() {
        // P0 the person as input

        Mission mp1 = Mockito.mock(Mission.class);
        Mockito.when(mp1.getName()).thenReturn("mp1");
        Mission mp2 = Mockito.mock(Mission.class);
        Mockito.when(mp2.getName()).thenReturn("mp2");
        Mission mp3 = Mockito.mock(Mission.class);
        Mockito.when(mp3.getName()).thenReturn("mp3");
        Mission mp4 = Mockito.mock(Mission.class);
        Mockito.when(mp4.getName()).thenReturn("mp4");
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(mp1);
        mSet.add(mp2);
        mSet.add(mp3);
        mSet.add(mp4);
        Mockito.when(p0.getMissionsRegistered()).thenReturn(mSet);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p0);
        pSet1.add(p1);
        Mockito.when(mp1.getParticipantSet()).thenReturn(pSet1);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p0);
        pSet2.add(p2);
        Mockito.when(mp2.getParticipantSet()).thenReturn(pSet2);
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p0);
        pSet3.add(p3);
        Mockito.when(mp3.getParticipantSet()).thenReturn(pSet3);
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p0);
        pSet4.add(p4);
        Mockito.when(mp4.getParticipantSet()).thenReturn(pSet4);

        Calendar cal = Calendar.getInstance();
        cal.set(2014, 4, 29);
        Mockito.when(m1.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2014, 5, 29);
        Mockito.when(m2.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2015, 5, 13);
        Mockito.when(m3.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2014, 8, 22);
        Mockito.when(m4.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2014, 5, 21);
        Mockito.when(m5.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
   
        Mockito.when(i0.getMission()).thenReturn(m0);  
        
        Mockito.when(i1.getMission()).thenReturn(m1);
        Mockito.when(i2.getMission()).thenReturn(m1);
        Mockito.when(i3.getMission()).thenReturn(m1);
        Mockito.when(i4.getMission()).thenReturn(m5);      
        Mockito.when(i5.getMission()).thenReturn(m2);
        Mockito.when(i6.getMission()).thenReturn(m2);
        Mockito.when(i7.getMission()).thenReturn(m2);
        
        Mockito.when(i8.getMission()).thenReturn(m3);
        Mockito.when(i9.getMission()).thenReturn(m3);
        
        Mockito.when(i10.getMission()).thenReturn(m4);
        Mockito.when(i11.getMission()).thenReturn(m5);
        Mockito.when(i12.getMission()).thenReturn(m5);

        //invitations received by p0
        Set<Invitation> iSet0 = new HashSet<Invitation>();
        iSet0.add(i0);
        //invitations received by p0's friends
        Set<Invitation> iSet1 = new HashSet<Invitation>();
        iSet1.add(i1);
      //  iSet1.add(i5);
        iSet1.add(i9);
        Set<Invitation> iSet2 = new HashSet<Invitation>();
        iSet2.add(i2);
       // iSet2.add(i6);
        iSet2.add(i5);
        iSet2.add(i10);
        Set<Invitation> iSet3 = new HashSet<Invitation>();
        iSet3.add(i3);
        iSet3.add(i11);
        Set<Invitation> iSet4 = new HashSet<Invitation>();
        iSet4.add(i4);
        iSet4.add(i8);
        iSet4.add(i12);

        Mockito.when(p0.getInvitationsReceived()).thenReturn(iSet0);
        Mockito.when(p1.getInvitationsReceived()).thenReturn(iSet1);
        Mockito.when(p2.getInvitationsReceived()).thenReturn(iSet2);
        Mockito.when(p3.getInvitationsReceived()).thenReturn(iSet3);
        Mockito.when(p4.getInvitationsReceived()).thenReturn(iSet4);

        List<Mission> result = missionMiner.mineSourGrapes(p0, 3);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(m1));
        Assert.assertTrue(result.contains(m5));
        Assert.assertTrue(result.contains(m3));       
    }
    
    /**
     * Testing of sour grapes when it succeeds
     */
     
    @Test
    public void testGetSourGrapestWhenOnePastMissionSuccess() {
        // P0 the person as input

        Mission mp1 = Mockito.mock(Mission.class);
        Mockito.when(mp1.getName()).thenReturn("mp1");
        Mission mp2 = Mockito.mock(Mission.class);
        Mockito.when(mp2.getName()).thenReturn("mp2");
        Mission mp3 = Mockito.mock(Mission.class);
        Mockito.when(mp3.getName()).thenReturn("mp3");
        Mission mp4 = Mockito.mock(Mission.class);
        Mockito.when(mp4.getName()).thenReturn("mp4");
        Set<Mission> mSet = new HashSet<Mission>();
        mSet.add(mp1);
        mSet.add(mp2);
        mSet.add(mp3);
        mSet.add(mp4);
        Mockito.when(p0.getMissionsRegistered()).thenReturn(mSet);

        Set<Person> pSet1 = new HashSet<Person>();
        pSet1.add(p0);
        pSet1.add(p1);
        Mockito.when(mp1.getParticipantSet()).thenReturn(pSet1);
        Set<Person> pSet2 = new HashSet<Person>();
        pSet2.add(p0);
        pSet2.add(p2);
        Mockito.when(mp2.getParticipantSet()).thenReturn(pSet2);
        Set<Person> pSet3 = new HashSet<Person>();
        pSet3.add(p0);
        pSet3.add(p3);
        Mockito.when(mp3.getParticipantSet()).thenReturn(pSet3);
        Set<Person> pSet4 = new HashSet<Person>();
        pSet4.add(p0);
        pSet4.add(p4);
        Mockito.when(mp4.getParticipantSet()).thenReturn(pSet4);

        Calendar cal = Calendar.getInstance();
        cal.set(2014, 2, 15);
        Mockito.when(m1.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2014, 5, 29);
        Mockito.when(m2.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2015, 5, 13);
        Mockito.when(m3.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2015, 8, 22);
        Mockito.when(m4.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        cal.set(2012, 5, 21);
        Mockito.when(m5.getTime()).thenReturn(new Date(cal.getTimeInMillis()));
        
        Mockito.when(i0.getMission()).thenReturn(m0);
        
        Mockito.when(i1.getMission()).thenReturn(m1);
        Mockito.when(i2.getMission()).thenReturn(m1);
        Mockito.when(i3.getMission()).thenReturn(m1);
        Mockito.when(i4.getMission()).thenReturn(m5);
        
        Mockito.when(i5.getMission()).thenReturn(m2);
        Mockito.when(i6.getMission()).thenReturn(m2);
        Mockito.when(i7.getMission()).thenReturn(m2);
        
        Mockito.when(i8.getMission()).thenReturn(m3);
        Mockito.when(i9.getMission()).thenReturn(m3);
        
        Mockito.when(i10.getMission()).thenReturn(m4);
        Mockito.when(i11.getMission()).thenReturn(m5);
        Mockito.when(i12.getMission()).thenReturn(m5);

        //invitations received by p0
        Set<Invitation> iSet0 = new HashSet<Invitation>();
        iSet0.add(i0);
        //invitations received by p0's friends
        Set<Invitation> iSet1 = new HashSet<Invitation>();
        iSet1.add(i1);
        iSet1.add(i9);
        Set<Invitation> iSet2 = new HashSet<Invitation>();
        iSet2.add(i2);
        iSet2.add(i5);
        iSet2.add(i10);
        Set<Invitation> iSet3 = new HashSet<Invitation>();
        iSet3.add(i3);
        iSet3.add(i11);
        Set<Invitation> iSet4 = new HashSet<Invitation>();
        iSet4.add(i4);
        iSet4.add(i8);
        iSet4.add(i12);

        Mockito.when(p0.getInvitationsReceived()).thenReturn(iSet0);
        Mockito.when(p1.getInvitationsReceived()).thenReturn(iSet1);
        Mockito.when(p2.getInvitationsReceived()).thenReturn(iSet2);
        Mockito.when(p3.getInvitationsReceived()).thenReturn(iSet3);
        Mockito.when(p4.getInvitationsReceived()).thenReturn(iSet4);

        List<Mission> result = missionMiner.mineSourGrapes(p0, 7);
        // asked for 7 sour grapes.. there are actually 4 of them except m5 because it has older date
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(m1));
        Assert.assertTrue(result.contains(m2));
        Assert.assertTrue(result.contains(m3));
        Assert.assertTrue(result.contains(m4));       
    }
    
    /**
     * Testing of getSourGrpaes when null is given as input
     */
    @Test(expected=NullArgumentException.class)
    public void testGetSourGrapesEmptyPersonInputFailure() {
        
        List<Mission> result = missionMiner.mineSourGrapes(null, 3); 
    }
    
     /**
     * Testing of getSourGrpaes when zero number of missions are being asked
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetSourGrapesZeroResultsAskedFailure() {
        
        List<Mission> result = missionMiner.mineSourGrapes(p0, 0); 
    }
    
    
    /**
     * Testing of all the friends who will be met before a date
     */
    @Test
    public void testGetFriendsWhoWillBeMetBeforeDateSuccess() {             
        Set<Mission> mP1 = new HashSet<Mission>();
        mP1.add(m1);
        mP1.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mP1);
        
        Set<Mission> mP2 = new HashSet<Mission>();
        mP2.add(m1);
        mP2.add(m3);
        mP2.add(m5);
        mP2.add(m6);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mP2);
        
        Set<Mission> mP3 = new HashSet<Mission>();
        mP3.add(m2);
        mP3.add(m4);
        mP3.add(m6);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mP3);
        
        Set<Mission> mP4 = new HashSet<Mission>();
        mP4.add(m2);
        mP4.add(m6);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mP4);
        
        Set<Mission> mP5 = new HashSet<Mission>();
        mP5.add(m3);
        mP5.add(m4);
        mP5.add(m5);
        mP5.add(m6);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mP5);
        
        Set<Person> pM1 = new HashSet<Person>();
        pM1.add(p1);
        pM1.add(p2);
        Mockito.when(m1.getParticipantSet()).thenReturn(pM1);
        
        Set<Person> pM2 = new HashSet<Person>();
        pM2.add(p1);
        pM2.add(p3);
        pM2.add(p4);
        Mockito.when(m2.getParticipantSet()).thenReturn(pM2);
        
        Set<Person> pM3 = new HashSet<Person>();
        pM3.add(p2);
        pM3.add(p5);
        Mockito.when(m3.getParticipantSet()).thenReturn(pM3);
                
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        // each of these mission already have dates set in the @Before method
        
        // mock the behaviour of core to give all the missions from database
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 4, 8);
        Date givenDate = cal.getTime();
        
        Set<Person> result = missionMiner.getFriendsWhoWillMeetBeforeDate(p1, givenDate);
        
        // only p2, p3, and p4 will be met before 8/4/2014 via some missions
        // but p5 is met after that date via m3 as it occurs on 13/5/2015
        Assert.assertEquals(result.size(), 3);
        Assert.assertTrue(result.contains(p2));
        Assert.assertTrue(result.contains(p3));
        Assert.assertTrue(result.contains(p4));
    }
    
    /**
     * Testing of Get mission within a date range
     */
    @Test
    public void testGetAllMissionsInDateRangeSuccess() {
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);

        // mock the behaviour of core to give all the missions from database
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        missionMiner = new MissionMiner(loader);

        // @Before method has all th dates d1 to d6 defined for m1 to m6.
        // d5 and d6 are the same date.
        Calendar c = Calendar.getInstance();
        c.set(2014, 4, 2);
        Date start = c.getTime();
        c.set(2014, 4, 25);
        Date end = c.getTime();

        List<Mission> dates = missionMiner.getAllTheMissionsInDateRange(start, end);
        // m2, m3, m4, m5, m6 would be returned
        Assert.assertEquals(dates.size(), 5);
        // make sure m1 is not returned in the list
        Assert.assertFalse(dates.contains(m1));
    }
    
    /**
     * Testing for mineOpportuneTimes success
     */
    @Test
    public void testGetOpportuneTimesSuccess() {             
        Set<Mission> mP1 = new HashSet<Mission>();
        mP1.add(m1);
        mP1.add(m2);
        Mockito.when(p1.getMissionsRegistered()).thenReturn(mP1);
        Set<Mission> mP2 = new HashSet<Mission>();
        mP2.add(m1);
        mP2.add(m3);
        mP2.add(m5);
        mP2.add(m6);
        Mockito.when(p2.getMissionsRegistered()).thenReturn(mP2);
        Set<Mission> mP3 = new HashSet<Mission>();
        mP3.add(m2);
        mP3.add(m4);
        mP3.add(m6);
        Mockito.when(p3.getMissionsRegistered()).thenReturn(mP3);
        Set<Mission> mP4 = new HashSet<Mission>();
        mP4.add(m2);
        mP4.add(m6);
        Mockito.when(p4.getMissionsRegistered()).thenReturn(mP4);
        Set<Mission> mP5 = new HashSet<Mission>();
        mP5.add(m3);
        mP5.add(m4);
        mP5.add(m5);
        mP5.add(m6);
        Mockito.when(p5.getMissionsRegistered()).thenReturn(mP5);
        Set<Person> pM1 = new HashSet<Person>();
        pM1.add(p1);
        pM1.add(p2);
        Mockito.when(m1.getParticipantSet()).thenReturn(pM1);
        Set<Person> pM2 = new HashSet<Person>();
        pM2.add(p1);
        pM2.add(p3);
        pM2.add(p4);
        Mockito.when(m2.getParticipantSet()).thenReturn(pM2);
        Set<Person> pM3 = new HashSet<Person>();
        pM3.add(p2);
        pM3.add(p5);
        Mockito.when(m3.getParticipantSet()).thenReturn(pM3);
        Set<Person> pM4 = new HashSet<Person>();
        pM4.add(p3);
        pM4.add(p5);
        Mockito.when(m4.getParticipantSet()).thenReturn(pM4);
        Set<Person> pM5 = new HashSet<Person>();
        pM5.add(p2);
        pM5.add(p5);
        Mockito.when(m5.getParticipantSet()).thenReturn(pM5);
        Set<Person> pM6 = new HashSet<Person>();
        pM6.add(p2);
        pM6.add(p3);
        pM6.add(p4);
        pM6.add(p5);
        Mockito.when(m6.getParticipantSet()).thenReturn(pM6);
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);
        // each of these mission already have dates set in the @Before method
        // mock the behaviour of core to give all the missions from database
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 4, 8);
        Date startDate = cal.getTime();
        cal.set(2014, 4, 28);
        Date endDate = cal.getTime();
        List<Date> result = missionMiner.mineOpportuneTimes(p1, startDate, endDate, 5);
        Assert.assertEquals(result.size(), 3);
        Assert.assertTrue(result.contains(d3));
        Assert.assertTrue(result.contains(d4));
        Assert.assertTrue(result.contains(d5));
    }
    
    /**
     * Testing for mineOpportuneTimes when null person is given as input
     */
    @Test(expected=NullArgumentException.class)
    public void testGetOpportuneTimesEmptyPersonInputFailure() {
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);     
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 4, 3);
        Date startDate = cal.getTime();  
        cal.set(2014, 5, 7);
        Date endDate = cal.getTime();
        missionMiner.mineOpportuneTimes(null, startDate, endDate, 5);
    }
    
    /**
     * Testing for mineOpportuneTimes when number of dates to be returned is zero
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetOpportuneTimesZeroResultsAskedFailure() {
        
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);     
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 5, 13);
        Date startDate = cal.getTime();  
        cal.set(2014, 6, 20);
        Date endDate = cal.getTime();
        
        List<Date> result = missionMiner.mineOpportuneTimes(p1, startDate, endDate, 0);
    }
    
    /**
     * Testing for mineOpportuneTimes when the input start date is null
     */
    @Test(expected=NullArgumentException.class)
    public void testGetOpportuneTimesStarDateNullFailure() {
        
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);     
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        Date startDate =  null;
        cal.set(2014, 4, 28);
        Date endDate = cal.getTime();
        
        List<Date> result = missionMiner.mineOpportuneTimes(p0, startDate, endDate, 5);
    }
    
     /**
     * Testing for mineOpportuneTimes when the input end date is null
     */
    @Test(expected=NullArgumentException.class)
    public void testGetOpportuneTimesEndDateNullFailure() {
        
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);     
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 5, 29);
        Date startDate =  cal.getTime();
        Date endDate = null;
        
        List<Date> result = missionMiner.mineOpportuneTimes(p0, startDate, endDate, 5);
    }
    
     /**
     * Testing for mineOpportuneTimes when the input end date is null
     */
    @Test(expected=IllegalArgumentException.class)
    public void testGetOpportuneTimesInputDatesInconsistentFailure() {
        
        List<Mission> allMissions = new ArrayList<Mission>();
        allMissions.add(m1);
        allMissions.add(m2);
        allMissions.add(m3);
        allMissions.add(m4);
        allMissions.add(m5);
        allMissions.add(m6);     
        loader = Mockito.mock(EntityLoader.class);
        Mockito.when(this.loader.loadAllMissions()).thenReturn(allMissions);
        
        missionMiner = new MissionMiner(loader);
        Calendar cal = Calendar.getInstance();
        cal.set(2014, 4, 28);
        Date startDate =  cal.getTime();
        cal.set(2013, 12, 11);
        Date endDate = cal.getTime();
        
        List<Date> result = missionMiner.mineOpportuneTimes(p0, startDate, endDate, 5);
    }   
    
}
