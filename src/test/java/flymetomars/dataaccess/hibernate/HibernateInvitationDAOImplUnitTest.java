package flymetomars.dataaccess.hibernate;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
public class HibernateInvitationDAOImplUnitTest {

    @Autowired
    private InvitationDAO invitationDao;
    
    @Autowired
    private PersonDAO personDao;
    
    @Autowired
    private SaltedPasswordDAO passwordDao;
    
    @Autowired
    private MissionDAO missionDao;

    @Autowired
    private LocationDAO locationDao;
    
    private Invitation invite;
    
    private Mission mission;
    
    private Person participant;
    
    private SaltedPassword password1;
    
    private Person captain;
    
    private SaltedPassword password2;
    
    private Location location;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
    
    protected void testInit() {if(password1!=null&&password2!=null) {return;}
        try {
            password1 = this.dtoFactory.createDTO(SaltedPassword.class);
            password2 = this.dtoFactory.createDTO(SaltedPassword.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernateInvitationDAOImplUnitTest.class.getName()).log(Level.SEVERE, "unable to make passwords", ex);
            throw new IllegalStateException(ex);
        }
        password1.setSaltId("1234");
        password1.setDigest("c68f25857abda7a85d2b8e8295c411dc"); //ax%ssk1A
        passwordDao.save(password1);
        password2.setSaltId("ABCD");
        password2.setDigest("876f5be86a17016b4aefccea186f8ed3"); //jsj_32aB%
        passwordDao.save(password2);
    }
    
    @Before
    public void doBeforeEachTestCase() {this.testInit();
        try {
            mission = this.dtoFactory.createDTO(Mission.class);
            participant = this.dtoFactory.createDTO(Person.class);
            captain = this.dtoFactory.createDTO(Person.class);
            location = this.dtoFactory.createDTO(Location.class);
            invite = this.dtoFactory.createDTO(Invitation.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HibernateInvitationDAOImplUnitTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        //make location
        location.setCountry("Australia");
        location.setCountry("Australia");
        location.setFloor("H2.22");
        location.setStreetNo("221");
        location.setStreet("Dandenong Road");
        location.setRegion("Victoria");
        location.setPostcode("3031");
        location.setLandmark("Near Racecourse");
        location.setTown("Caulfield");
        locationDao.save(location);
        //make captain
        captain.setEmail("arin.biswa@icloud.com");
        captain.setUserName("Arin_bis21");
        captain.setFirstName("Arinjoy");
        captain.setLastName("Biswas");
        captain.setPassword(password2.getId());
        personDao.save(captain);
        //make mission
        mission.setName("Awesome");
        mission.setDescription("This is a mission to Mars!");
        mission.setLocation(location);
        mission.setCaptain(captain);
        mission.setTime(new Date());
        //make participant
        participant.setEmail("ram.sham@hotmail.com");
        participant.setUserName("aama_hari2");
        participant.setFirstName("Butun");
        participant.setLastName("Mitra");
        participant.setPassword(password1.getId());
    }
    
    
    //<editor-fold desc="insert test cases">
    
   
    @Test
    @Transactional
    public void testInsertInvitationSuccess() {
        personDao.save(participant);
        missionDao.save(mission);
        invite.setMission(mission);
        invite.setCreator(captain);
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invitationDao.save(invite);   //inserting the invitation         
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationWithoutMissionFailure() {
        invite.setCreator(participant);
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.SENT);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invitationDao.save(invite);
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationWithoutCreatorFailure() {
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.SENT);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invite.setMission(mission);
        missionDao.save(mission);
        invitationDao.save(invite);
    } 
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationWithoutRecipientFailure() {
        invite.setCreator(participant);
        invite.setStatus(Invitation.InvitationStatus.SENT);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invite.setMission(mission);
        missionDao.save(mission);
        invitationDao.save(invite);
    }
    //</editor-fold>
    
    //<editor-fold desc="creator test cases">
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationEmptyCreatorFailure(){
        invite.setMission(mission);
        invite.setRecipient(participant);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        missionDao.save(mission);
        invitationDao.save(invite);
    }
    
    @Test
    @Transactional
    public void testGetInvitationsByCreatorSuccess() throws UnserialisedEntityException {
        Set<Person> ppl = Collections.singleton(participant);
        mission.setParticipantSet(ppl);
        personDao.save(participant);
        mission.setCaptain(captain);
        missionDao.save(mission);
        invite.setMission(mission);
        invite.setCreator(captain);
        invite.setRecipient(participant);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invitationDao.save(invite);
        //actual test:
        List<Invitation> list = invitationDao.getInvitationsByCreator(captain);
        Assert.assertEquals(1,list.size());
        Invitation result=list.get(0);
        Assert.assertEquals(result.getId(),invite.getId());
        result=invitationDao.load(invite.getId());
        Person creator=result.getCreator();
        Assert.assertEquals(captain.getId(),creator.getId());
        Assert.assertEquals(captain.getFirstName(),creator.getFirstName());
        Assert.assertEquals(captain.getLastName(),creator.getLastName());
    }
    
    @Test
    @Transactional
    public void testGetInvitationsByCreatorNoneExistSuccess() throws UnserialisedEntityException {
        personDao.save(participant);
        try {
            List<Invitation> list = invitationDao.getInvitationsByCreator(participant);
            Assert.assertEquals(0, list.size());
        } finally {
            if(null!=participant && null!=participant.getId()) { personDao.delete(participant); }
        }
    }
    
    //</editor-fold>
    
    //<editor-fold desc="retrieve by date test cases">
    
    @Test
    @Transactional
    public void testGetInvitationsSinceDateSuccess() {
        Set<Person> ppl = Collections.singleton(participant);
        invite.setLastUpdated(new Date(new Date().getTime()-1000));
        personDao.save(participant);
        mission.setParticipantSet(ppl);
        invite.setRecipient(participant);
        mission.setCaptain(participant);
        missionDao.save(mission);
        invite.setMission(mission);
        invite.setCreator(participant);
        invitationDao.save(invite);
        //actual test:
        List<Invitation> list = invitationDao.getInvitationsSinceDate(new Date(new Date().getTime()-30000));
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(invite.getLastUpdated(), list.get(0).getLastUpdated());
    }
    
    @Test
    @Transactional
    public void testGetInvitationsBeforeDateSuccess() {
        Set<Person> ppl = Collections.singleton(participant);
        invite.setLastUpdated(new Date(new Date().getTime()-10000));
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        personDao.save(participant);
        mission.setParticipantSet(ppl);
        missionDao.save(mission);
        invite.setMission(mission);
        invite.setRecipient(participant);
        invite.setCreator(participant);
        invitationDao.save(invite);
        //actual test:
        List<Invitation> list = invitationDao.getInvitationsBeforeDate(new Date(new Date().getTime()-8000));
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(invite.getLastUpdated(), list.get(0).getLastUpdated());
    }
    
    //</editor-fold>

    //<editor-fold desc="recipient test cases">
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationEmptyRecipientFailure(){
        invite.setMission(mission);
        try {
            missionDao.save(mission);
        } catch (DataIntegrityViolationException dive) {
            return;  //will cause test failure as no exception
        }
        invitationDao.save(invite);
    }
    
    @Test
    @Transactional
    public void testGetInvitationsByRecipientSuccess() throws UnserialisedEntityException {
        Set<Person> ppl = Collections.singleton(participant);
        mission.setParticipantSet(ppl);
        invite.setMission(mission);
        invite.setMission(mission);
        invite.setCreator(participant);
        invite.setRecipient(participant);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        personDao.save(participant);
        missionDao.save(mission);
        invitationDao.save(invite);
        //actual test:
        List<Invitation> list = invitationDao.getInvitationsByRecipient(participant);
        Assert.assertEquals(1,list.size());
        Invitation result=list.get(0);
        Assert.assertEquals(invite.getId(),result.getId());
        result=invitationDao.load(result.getId());
        Person recp=result.getRecipient();
        Assert.assertEquals(participant.getId(),recp.getId());
        Assert.assertEquals(participant.getFirstName(),recp.getFirstName());
        Assert.assertEquals(participant.getLastName(),recp.getLastName());
    }
    
    @Test
    @Transactional
    public void testGetInvitationsByRecipientDoesNotExistSuccess() throws UnserialisedEntityException {
        personDao.save(participant);
        List<Invitation> list = invitationDao.getInvitationsByRecipient(participant);
        Assert.assertEquals(0, list.size());
    }
    
    //</editor-fold>

    //<editor-fold desc="last updated test cases">
    
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testInsertInvitationWithNullLastUpdatedFaliure() {
        personDao.save(participant);
        invite.setCreator(participant);
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        //invite.setLastUpdated(null);
        missionDao.save(mission);
        invite.setMission(mission);
        invitationDao.save(invite);
    }
    
    //</editor-fold>
    
    //<editor-fold desc="update test cases">
    
    @Test
    @Transactional
    public void testUpdateInvitationRecipientSuccess() {
        invite.setMission(mission);
        invite.setCreator(participant);
        invite.setRecipient(participant);
        personDao.save(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        missionDao.save(mission);
        invitationDao.save(invite); //saving the complete invitation
        invite.setRecipient(captain);
        invitationDao.update(invite);
        List<Invitation> invitations = invitationDao.getAll(); //getting the invitation back from database
        Assert.assertEquals(1, invitations.size());// ensuring the invitation list has one element and it is person 2    
        Invitation inv=invitations.get(0);
        Assert.assertEquals(invite.getId(),inv.getId());
        inv=invitationDao.load(inv.getId());
        Person result=inv.getRecipient();
        Assert.assertEquals(result.getFirstName(), captain.getFirstName());
        Assert.assertEquals(result.getLastName(), captain.getLastName());
        Assert.assertEquals(result.getUserName(), captain.getUserName());
        Assert.assertEquals(result.getEmail(), captain.getEmail());
    }
    
    @Test
    @Transactional
    public void testUpdateInvitationCreatorSuccess() {
        invite.setMission(mission);
        invite.setCreator(participant);
        invite.setRecipient(participant);
        personDao.save(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        missionDao.save(mission);
        invitationDao.save(invite); //saving the complete invitation
        invite.setCreator(captain);
        invitationDao.update(invite);
        List<Invitation> result=invitationDao.getAll();
        Assert.assertEquals(1, result.size());
        Invitation inv=result.get(0);
        Assert.assertEquals(invite.getId(), inv.getId());
        inv=invitationDao.load(inv.getId());
        Assert.assertEquals(invite.getCreator().getId(), inv.getCreator().getId());
        Assert.assertEquals(invite.getCreator().getFirstName(), inv.getCreator().getFirstName());
        Assert.assertEquals(invite.getCreator().getLastName(), inv.getCreator().getLastName());
        Assert.assertEquals(invite.getCreator().getEmail(), inv.getCreator().getEmail());
    }
    
    @Test
    @Transactional
    public void testUpdateInvitationStatusSuccess() throws UnserialisedEntityException {
        invite.setCreator(participant);
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        personDao.save(participant);
        missionDao.save(mission);
        invite.setMission(mission);
        invitationDao.save(invite); //saving the complete invitation
        invite.setStatus(Invitation.InvitationStatus.SENT);
        invitationDao.update(invite);
        List<Invitation> invitations = invitationDao.getInvitationsByRecipient(invite.getRecipient()); //getting the invitation back from database
        Assert.assertEquals(invitations.size(),1);// ensuring the invitation list has one element and it is person 2    
        Assert.assertEquals(invitations.get(0).getMission().getId(), mission.getId());
        Assert.assertEquals(invitations.get(0).getStatus(), invite.getStatus());
    }
    
    //</editor-fold>
    
    //<editor-fold desc="delete test cases">
    @Test
    @Transactional
    public void testDeleteInvitationSuccess() throws UnserialisedEntityException {
        personDao.save(participant);
        missionDao.save(mission);
        invite.setMission(mission);
        invite.setCreator(participant);
        invite.setRecipient(participant);
        invite.setStatus(Invitation.InvitationStatus.CREATED);
        invite.setLastUpdated(Calendar.getInstance().getTime());
        invitationDao.save(invite); //saving the comlete invitation
        invitationDao.delete(invite);
        List<Invitation> invitations = invitationDao.getInvitationsByRecipient(participant); //getting the invitation back from database
        Assert.assertEquals(invitations.size(),0);// ensuring the invitation list has no element     
    }
    
    //</editor-fold>
    
}
