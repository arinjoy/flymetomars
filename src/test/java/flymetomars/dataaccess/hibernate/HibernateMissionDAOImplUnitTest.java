package flymetomars.dataaccess.hibernate;

import flymetomars.common.NullArgumentException;
import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Location;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import flymetomars.dataaccess.UnserialisedEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({TransactionalTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class
})
@FixMethodOrder
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class HibernateMissionDAOImplUnitTest {

    @Autowired
    private MissionDAO missionDao;
    
    @Autowired
    private LocationDAO locationDao;
    
    @Autowired
    private InvitationDAO invitationDao;

    @Autowired
    private PersonDAO personDao;
    
    @Autowired
    private SaltedPasswordDAO passwordDao;
    
    private Invitation invite;
    private Mission mission;
    private Location location;
    private Person person;
    private Person captain;
    private SaltedPassword pass1;
    private SaltedPassword pass2;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    private boolean donePW=false;
    
    @Before
    public void setUp() throws ClassNotFoundException {
        try {
            this.mission = this.dtoFactory.createDTO(Mission.class);
            if(null==this.person){ this.person = this.dtoFactory.createDTO(Person.class); }
            if(null==this.captain){ this.captain = this.dtoFactory.createDTO(Person.class); }
            if(null==this.location){ this.location = this.dtoFactory.createDTO(Location.class); }
            if(null==this.pass1){ this.pass1 = this.dtoFactory.createDTO(SaltedPassword.class); }
            if(null==this.pass2){ this.pass2 = this.dtoFactory.createDTO(SaltedPassword.class); }
            this.invite = this.dtoFactory.createDTO(Invitation.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernateMissionDAOImplUnitTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            throw ex;
        }
        //location
        location.setFloor("H2.22");
        location.setStreetNo("221");
        location.setStreet("Dandenong Road");
        location.setRegion("Victoria");
        location.setPostcode("3031");
        location.setLandmark("Near Racecourse");
        location.setTown("Caulfield");
        location.setCountry("Australia");
        locationDao.save(location);
        //passwords
        if(!donePW) {
            pass1.setDigest("ba15d0542b59d81dd435c22946aeb338");  //I<3Mars!
            pass1.setSaltId("342436");
            passwordDao.save(pass1);
            pass2.setSaltId("656667");
            pass2.setDigest("35179a54ea587953021400eb0cd23201");
            passwordDao.save(pass2);
            this.donePW=true;
        }
        //person
        person.setPassword(pass1.getId());
        person.setFirstName("Apoorva");
        person.setLastName("Singh");
        person.setUserName("asin91");
        person.setEmail("ap.singh@test.com.au");
        //captain
        captain.setFirstName("James");
        captain.setLastName("Cook");
        captain.setEmail("james.cook@nasa.gov.com");
        captain.setUserName("jamme_21");
        captain.setPassword(pass2.getId());
        personDao.save(captain);
        //mission
        mission.setName("Unite the Federation");
        mission.setTime(new Date(new Date().getTime() + 10000L));
        //mission.setLocation(location);  //do not set mission.location!
        mission.setDescription("to boldly go where no one has gone before");
        //mission.setCaptain(person);  //do not set mission.captain!
        //invitation
        invite.setRecipient(person);
        invite.setCreator(person);
        //invite.setMission(mission);
        invite.setLastUpdated(new Date());
        invite.setStatus(Invitation.InvitationStatus.CREATED);
    }

    //<editor-fold desc="insert mision test cases">

    @Test
    @Transactional
    public void testInsertMissionSuccess() throws UnserialisedEntityException {
        mission.setLocation(location);
        mission.setCaptain(captain);
        missionDao.save(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(mis.getCaptain().getEmail(), mission.getCaptain().getEmail());
        Assert.assertEquals(mis.getName(), mission.getName());
        Assert.assertEquals(mis.getDescription(), mission.getDescription());
        Assert.assertEquals(mis.getTime(), mission.getTime());
        Assert.assertEquals(mis.getLocation(), mission.getLocation());
    }

    @Test
    @Transactional
    public void testInsertMissionWithParticipantSuccess() throws UnserialisedEntityException {
        Set<Person> participants = new HashSet<Person>();
        participants.add(person);
        mission.setParticipantSet(participants);
        mission.setLocation(location);
        personDao.save(person);
        mission.setCaptain(captain);
        missionDao.save(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(participants.size(), mis.getParticipantSet().size());
        Assert.assertEquals(mission.getParticipantSet().iterator().next().getFirstName(), mis.getParticipantSet().iterator().next().getFirstName());
    }

    @Test
    @Transactional
    public void testInsertMissionWithParticipantsSuccess() throws UnserialisedEntityException {
        Set<Person> participants = new HashSet<Person>();
        participants.add(person);
        participants.add(captain);
        mission.setParticipantSet(participants);
        mission.setLocation(location);
        personDao.save(person);
        mission.setCaptain(captain);
        missionDao.save(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(mission.getParticipantSet().size(), mis.getParticipantSet().size());
        int p=0;
        for(Person e : mission.getParticipantSet()) {
            for(Person r : mis.getParticipantSet()) {
                if(r.getId()==e.getId() && r.getEmail().equals(e.getEmail())) {
                    p++;
                    break;
                }
            }
        }
        Assert.assertEquals(participants.size(), p);
    }

    @Test
    @Transactional
    public void testInsertMissionWithoutInvitationSetSuccess() throws UnserialisedEntityException {
        personDao.save(person);
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(mis.getName(), mission.getName());
        Assert.assertEquals(mis.getDescription(), mission.getDescription());
        Assert.assertEquals(mis.getTime(), mission.getTime());
        Assert.assertEquals(mis.getLocation(), mission.getLocation());
        if(null!=mission.getInvitationSet()) {  //It is valid for the case of a
            Assert.assertTrue(mission.getInvitationSet().isEmpty());  //mission
        } else {  //with no InvitationSet propery set, to return either of;
            Assert.assertNull(mission.getInvitationSet());  //null, or a valid
        }  //(but empty) list object instance.
    }

    @Test
    @Transactional
    public void testInsertMissionWithInvitationSetSuccess() {
        mission.setLocation(location);
        personDao.save(person);
        mission.setCaptain(captain);
        Set<Invitation> invitations = new HashSet<Invitation>();
        mission.setInvitationSet(invitations);
        missionDao.save(mission);
        Mission mis = missionDao.getAll().get(0);
        Assert.assertNotNull(mis.getInvitationSet());
        Assert.assertTrue(mis.getInvitationSet().isEmpty());
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertMissionWithNullLocationFailure() {
        //mission.setLocation((Location)null);
        Assert.assertNull(mission.getLocation());
        mission.setCaptain(captain);
        missionDao.save(mission); // a mission without location set
        Assert.assertNull(mission.getLocation());
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertMissionWithNullCaptainFailure() {
        //mission.setCaptain((Person)null);
        Assert.assertNull(mission.getCaptain());
        mission.setLocation(location);
        missionDao.save(mission); // a mission without captain set
        Assert.assertNull(mission.getCaptain());
    }
    
    //</editor-fold>

    //<editor-fold desc="update mission test cases">

    @Test
    @Transactional
    public void testUpdateMissionSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission); //saving the mission
        mission=missionDao.getMissionByName(mission.getName());
        Assert.assertNotNull(mission);  //save worked
        mission.setName("Woderful Mars!"); //chainging the name
        mission.setDescription("The beautiful journey to Mars and the milky way");
        missionDao.update(mission); //updating the mission
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(mis.getName(), mission.getName());
        Assert.assertEquals(mis.getDescription(), mission.getDescription());
    }
    
    @Test
    @Transactional
    public void testUpdateMissionLocationSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission); //saving the mission       
        location.setCountry("Russia");
        location.setTown("Moscow");
        locationDao.update(location);
        Mission mis = missionDao.getMissionsByLocation(location).get(0);
        Assert.assertNotNull(mis.getLocation());
        Assert.assertEquals(location.getCountry(), mis.getLocation().getCountry());
        Assert.assertEquals(location.getTown(), mis.getLocation().getTown());
    }

    @Test
    @Transactional
    public void testUpdateMissionCaptainSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission);     
        personDao.save(person);
        mission.setCaptain(person);
        missionDao.update(mission); //updating the mission captain
        List<Mission> miss=missionDao.getMissionsByCaptain(mission.getCaptain());
        Assert.assertEquals(1, miss.size());
        Mission mis = miss.get(0); // getting the only mission with newly updated captain
        Assert.assertNotNull(mis);
        Assert.assertNotNull(mis.getCaptain());
        Assert.assertEquals(mis.getCaptain().getId(), mission.getCaptain().getId());
    }

    @Test
    @Transactional
    public void testUpdateMissionAddNewParticipantSuccess() throws UnserialisedEntityException {
        Set<Person> participants = new HashSet<Person>();
        participants.add(person);
        mission.setLocation(location);
        personDao.save(person);
        mission.setParticipantSet(participants);
        mission.setCaptain(captain);
        missionDao.save(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        participants = mis.getParticipantSet();
        captain=personDao.getPersonByUserName(captain.getUserName());
        participants.add(captain); //ading the new party
        mission.setParticipantSet(participants); //setting the new particiapnt set
        missionDao.update(mission);  //updating the mision        
        mis = missionDao.getMissionByName(mission.getName());
        Assert.assertEquals(2, mis.getParticipantSet().size());
    }

    @Test
    @Transactional
    public void testUpdateMissionAddInvitationSuccess() {
        mission.setLocation(location);
        personDao.save(person);
        mission.setCaptain(captain);
        missionDao.save(mission);
        Set<Invitation> invitations = new HashSet<Invitation>();
        invite.setMission(mission);
        invitations.add(invite);
        mission.setInvitationSet(invitations);
        invitationDao.save(invite);
        missionDao.update(mission); //update the mission
        Mission mis = missionDao.getAll().get(0);
        Assert.assertFalse(mis.getInvitationSet().isEmpty());
        Assert.assertEquals(1, mis.getInvitationSet().size());
        Invitation result=mis.getInvitationSet().iterator().next();
        Assert.assertEquals(invite.getId(),result.getId());
    }

    @Test
    @Transactional
    public void testUpdateMissionRemoveInvitationSuccess() throws UnserialisedEntityException {
        mission.setLocation(location);
        personDao.save(person);
        mission.setCaptain(person);
        missionDao.save(mission);
        //add invite (to be removed)
        Set<Invitation> invitations = new HashSet<Invitation>();
        invite.setMission(mission);
        invitations.add(invite);
        mission.setInvitationSet(invitations);
        invitationDao.save(invite);
        missionDao.update(mission);
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertFalse(mis.getInvitationSet().isEmpty());
        Assert.assertEquals(1, mis.getInvitationSet().size());
        //remove invite
        invitations = new HashSet<Invitation>();
        mission.setInvitationSet(invitations); //setting the invitation set for mission
        missionDao.update(mission); //update the mission
        Mission result = missionDao.getMissionByName(mission.getName());
        Assert.assertTrue(result.getInvitationSet().isEmpty());
        Assert.assertFalse(result.getInvitationSet().contains(invite)); //asserting invite is deleted
    }

    //</editor-fold>

    //<editor-fold desc="delete mission test cases">   

    @Test
    @Transactional
    public void testDeleteMissionSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission);
        missionDao.delete(mission); //deleting mission
        Mission mis = missionDao.getMissionByName(mission.getName());
        Assert.assertNull(mis);// ensuring the missin is dead now
    }
    
    //</editor-fold>

    //<editor-fold desc="get missions test cases">  
    @Test
    @Transactional
    public void testGetMissionsByCaptainSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        personDao.save(person);
        List<Mission> missions = missionDao.getMissionsByCaptain(person);
        Assert.assertNotNull(missions);
        for (Mission miss : missions) {
            Assert.assertEquals(miss.getLocation(), mission.getLocation());
            Assert.assertEquals(miss.getName(), mission.getName());
            Assert.assertEquals(miss.getDescription(), mission.getDescription());
            Assert.assertEquals(miss.getCaptain(), mission.getCaptain());
            for (int mi = 0; mi < miss.getInvitationSet().size(); Assert.assertTrue("Mission paramaters are not identical",
                    (new ArrayList<Invitation>(miss.getInvitationSet())).get(mi++).equals(mission.getInvitationSet().toArray()[mi - 1]))) {}
        }
    }

    @Test(expected = NullArgumentException.class)
    @Transactional
    public void testGetMissionsByNullCaptainFailure() throws UnserialisedEntityException {
        missionDao.getMissionsByCaptain(null);
        //should never execute, as above line throws NullArgumentException
        Assert.assertTrue("null captain argument to method did not throw error", false);
    }

    @Test
    @Transactional
    public void testGetMissionsByLocationSuccess() throws UnserialisedEntityException {
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission);
        List<Mission> missions = missionDao.getMissionsByLocation(location);
        try {
            Assert.assertNotNull(missions); //asserting the result set is not null
            Assert.assertEquals(missions.size(), 1); //asserting the resulting list has size 1
            for (Mission miss : missions) {
                Assert.assertEquals(location.getCountry(), miss.getLocation().getCountry());
                Assert.assertEquals(location.getTown(), miss.getLocation().getTown());
                Assert.assertEquals(location.getFloor(), miss.getLocation().getFloor());
            }
        } finally {
            missionDao.delete(mission);
        }
    }

    @Test(expected = NullArgumentException.class)
    @Transactional
    public void testGetMissionsByNullLocationFailure() throws UnserialisedEntityException {
        missionDao.getMissionsByLocation(null);
        //should never execute, as above line throws NullArgumentException
        Assert.assertTrue("null location argument to method did not throw error", false);
    }

    @Test
    @Transactional
    public void testGetMissionsByDateRangeSuccess() {
        mission.setTime(new Date(115, 12, 10));
        mission.setCaptain(captain);
        mission.setLocation(location);
        missionDao.save(mission);
        List<Mission> missions = missionDao.getMissionsByDateRange(new Date(115, 11, 1), new Date(115, 12, 31));
        try {
            Assert.assertNotNull(missions); //asserting the result set is not null
            Assert.assertEquals(1, missions.size()); //asserting the resulting list has size 1
            Mission miss=missions.get(0);
            Assert.assertEquals(miss.getName(), mission.getName());
            Assert.assertEquals(miss.getTime(), mission.getTime());
        } finally {
            missionDao.delete(mission);
        }
    }
    
    //</editor-fold>
 
}
