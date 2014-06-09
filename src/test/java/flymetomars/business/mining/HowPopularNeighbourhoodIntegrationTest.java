package flymetomars.business.mining;

import com.google.gson.Gson;
import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.InvitationHandler;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.common.logic.DTOMarshaler;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({TransactionalTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class
})
@FixMethodOrder
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class HowPopularNeighbourhoodIntegrationTest {
    
    //<editor-fold defaultstate="collapsed" desc="DAO dependencies for Loader">
    @Autowired
    private PersonDAO personDao;
    @Autowired
    private MissionDAO missionDao;
    @Autowired
    private InvitationDAO invitationDao;
    @Autowired
    private LocationDAO locationDao;
    @Autowired
    private ExpertiseDAO expertiseDao;
    @Autowired
    private SaltedPasswordDAO passwordDao;
    @Autowired
    private SaltDAO saltDao;
    //</editor-fold>
    
    //Loader dependency for Miner
    private EntityLoader loader;
    private HowPopular miner;  //the Miner = component under test

    //Saver utility for SetUp
    private EntitySaver saver;
    
    //Update utility for SetUp
    private EntityUpdater updater;
    
    private static final Gson json=new Gson();
    
    //Marshalers for SetUp
    private DTOMarshaler<flymetomars.common.datatransfer.Person> personMarshaler;
    
    private void ensureFields() {
        if(null==personMarshaler) {
            Class<flymetomars.common.datatransfer.Person> cl=flymetomars.common.datatransfer.Person.class;
            personMarshaler=new DTOMarshaler<flymetomars.common.datatransfer.Person>(cl,json,DTOFactoryImpl.getInstance());
        }
        if(null==loader) { loader=new EntityLoader(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao); }
        if(null==saver) { saver=new EntitySaver(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao); }
        if(null==updater) { updater=new EntityUpdater(personDao,invitationDao,missionDao,locationDao,expertiseDao,passwordDao,saltDao); }
        if(null==miner) { miner = new PersonMiner(loader); }
    }
    
    @Before
    public void setUp() throws DependencyException {
        ensureFields();
        EntityFactory factory=new EntityFactory();
        Salt staffSalt=factory.createSalt("");
        Salt studentSalt=factory.createSalt("super-cali-frag-ali-istic-expe-ala-doc-ious");
        staffSalt=saver.saveSalt(staffSalt);   studentSalt=saver.saveSalt(studentSalt);
        //Yuan-Fang
        Person yuanfang=factory.createPerson(factory.createSaltedPassword(staffSalt));
        yuanfang.setFirstName("Yuan-Fang");  yuanfang.setLastName("Li");  yuanfang.setUserName("yuanli");
        yuanfang.setEmail("y.li@monash.org");  yuanfang.getPassword().setPassword("$up3rS3cur3!");
        saver.saveSaltedPassword(yuanfang.getPassword());  yuanfang=saver.savePerson(yuanfang);
        //Lawrence
        Person lawrence=new Person(personMarshaler.unmarshal("{\"firstName\":\"Lawrence\",\"lastName\":\"Colman\",\"email\":\"lawrence.colman@acm.org\",\"userName\":\"lcolman\",\"expertiseGained\":[],\"missionsRegistered\":[],\"invitationsReceived\":[]}"));
        lawrence.setPassword(factory.createSaltedPassword(studentSalt));  lawrence.getPassword().setPassword("4w350m3!");
        saver.saveSaltedPassword(lawrence.getPassword());  lawrence=saver.savePerson(lawrence);
        //Jason
        Person jason=new Person(personMarshaler.unmarshal("{\"firstName\":\"Kefeng\",\"lastName\":\"Xuan\",\"email\":\"kxuan@monash.net\",\"userName\":\"jasonx\",\"expertiseGained\":[],\"missionsRegistered\":[],\"invitationsReceived\":[]}"));
        jason.setPassword(factory.createSaltedPassword(staffSalt));  jason.getPassword().setPassword("7u70rc0@1");
        saver.saveSaltedPassword(jason.getPassword());  jason=saver.savePerson(jason);
        //Apoorva
        Person apoorva=new Person(personMarshaler.unmarshal("{\"firstName\":\"Apoorva\",\"lastName\":\"Singh\",\"email\":\"asingh@monash.com\",\"userName\":\"apoorv\",\"expertiseGained\":[],\"missionsRegistered\":[],\"invitationsReceived\":[]}"));
        apoorva.setPassword(factory.createSaltedPassword(studentSalt));  apoorva.getPassword().setPassword("th3Dud3e!");
        saver.saveSaltedPassword(apoorva.getPassword());  apoorva=saver.savePerson(apoorva);
        //Arinjoy
        Person arinjoy=new Person(personMarshaler.unmarshal("{\"firstName\":\"Arinjoy\",\"lastName\":\"Biswas\",\"email\":\"abis@monash.org\",\"userName\":\"codegod\",\"expertiseGained\":[],\"missionsRegistered\":[],\"invitationsReceived\":[]}"));
        arinjoy.setPassword(factory.createSaltedPassword(studentSalt));  arinjoy.getPassword().setPassword("_=(0d3-g0D!");
        saver.saveSaltedPassword(arinjoy.getPassword());  arinjoy=saver.savePerson(arinjoy);
        //missions
        Location loc=new Location();  loc.setCountry("Australia");  loc.setRegion("Victoria");  loc.setTown("Caulfield");
        loc.setPostcode("3175");  loc.setStreet("Dendenong Road");  loc.setStreetNo("900");  loc.setFloor("7th");
        Mission fit5171=factory.createMission(yuanfang, loc);  fit5171.setName("Software Testing");
        fit5171.setDescription("The only realt software engineering subject in town");
        Mission fit5030=factory.createMission(apoorva, loc);  fit5030.setName("Web Services");
        fit5030.setDescription("SOAP + Java = best friends!");
        Mission fit5168=factory.createMission(arinjoy, loc);  fit5168.setName("XML Techniques");
        fit5168.setDescription("XML can be useful... occasionally");
        loc.setLandmark("Through the door to the right");  saver.saveLocation(loc);
        fit5168.setTime(new Date());  saver.saveMission(fit5168);
        fit5030.setTime(new Date(Date.UTC(2013, 5, 17, 11, 59, 00)));  saver.saveMission(fit5030);
        fit5171.setTime(new Date(Date.UTC(2013, 5, 1, 11, 55, 59)));  saver.saveMission(fit5171);
        //invitations
        InvitationHandler handler=new InvitationHandler();
        handler.sendInvitation(factory.createInvitation(yuanfang, fit5171), jason);
        handler.sendInvitation(factory.createInvitation(jason, fit5171), lawrence);
        handler.sendInvitation(factory.createInvitation(lawrence, fit5171), apoorva);
        handler.sendInvitation(factory.createInvitation(apoorva, fit5171), arinjoy);
        handler.sendInvitation(factory.createInvitation(arinjoy, fit5168), lawrence);
        handler.sendInvitation(factory.createInvitation(lawrence, fit5030), apoorva);
        handler.sendInvitation(factory.createInvitation(arinjoy, fit5030), apoorva);
        for(Person pi : new Person[]{yuanfang,jason,lawrence,arinjoy,apoorva}) {
            for(Invitation inv : pi.getInvitationsReceived()) {
                saver.saveInvitation(inv);
            }
            //updater.updatePerson(pi);
        }
    }

    /**
     * Test of mineTopCelebrities method, of class HowPopular.
     */
    @Test
    public void testMineTopCelebritiesSuccess() {
        int k=2;
        List<Person> result = miner.mineTopCelebrities(k);
        Assert.assertEquals(k, result.size());
        Assert.assertEquals("Apoorva" ,result.get(0).getFirstName());
        Assert.assertEquals("Lawrence" ,result.get(1).getFirstName());
    }
    
}
