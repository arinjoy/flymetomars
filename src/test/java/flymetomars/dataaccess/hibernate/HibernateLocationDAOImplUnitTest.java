package flymetomars.dataaccess.hibernate;


import flymetomars.common.datatransfer.Location;
import flymetomars.common.factories.DTOFactory;
import flymetomars.common.factories.DTOFactoryImpl;
import flymetomars.dataaccess.LocationDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({TransactionalTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@FixMethodOrder
public class HibernateLocationDAOImplUnitTest {
    
    @Autowired
    private LocationDAO locationDao;

    private Location location;
    
    private DTOFactory dtoFactory=DTOFactoryImpl.getInstance();
       
    @Before
    public void setUp() {
        try {
            this.location=this.dtoFactory.createDTO(Location.class);
        } catch (ClassNotFoundException cnfe) {
            Logger.getLogger(HibernateLocationDAOImplUnitTest.class.getName()).log(Level.SEVERE, cnfe.getMessage(), cnfe);
        }
        this.location.setFloor("H2.22");
        this.location.setStreetNo("221");
        this.location.setStreet("Dandenong Road");
        this.location.setRegion("Victoria");
        this.location.setPostcode("3031");
        this.location.setLandmark("Near Racecourse");
        this.location.setTown("Caulfield");
        this.location.setCountry("Australia");
    }
    
    @Test
    @Transactional
    public void testInsertLocationSuccess() {
        try {
            locationDao.save(location);
            Location loc = locationDao.getLocationsByCountry("Australia").get(0);
            Assert.assertEquals(loc.getFloor(),location.getFloor());
            Assert.assertEquals(loc.getStreetNo(),location.getStreetNo());
            Assert.assertEquals(loc.getStreet(),location.getStreet());
            Assert.assertEquals(loc.getLandmark(),location.getLandmark());
            Assert.assertEquals(loc.getPostcode(),location.getPostcode());
            Assert.assertEquals(loc.getTown(),location.getTown());
            Assert.assertEquals(loc.getCountry(),location.getCountry());
        } finally {
            locationDao.delete(location);
       }
    }
    
    @Test
    @Transactional
    public void testUpdateLocationSuccess() {
        locationDao.save(location);
        location.setCountry("India");
        location.setTown("Kolkata");
        locationDao.update(location);
        Location loc = locationDao.getLocationsByCountry("India").get(0);
        Assert.assertEquals(loc.getFloor(),location.getFloor());
        Assert.assertEquals(loc.getStreetNo(),location.getStreetNo());
        Assert.assertEquals(loc.getStreet(),location.getStreet());
        Assert.assertEquals(loc.getLandmark(),location.getLandmark());
        Assert.assertEquals(loc.getPostcode(),location.getPostcode());
        Assert.assertEquals(loc.getTown(),location.getTown());
        Assert.assertEquals(loc.getCountry(),location.getCountry());
    }
    
    @Test
    @Transactional
    public void testDeleteLocationSuccess() {
        locationDao.save(location); //saving the location
        if(location!=null){
            Location dead = locationDao.delete(location); //deleting the location
            Assert.assertEquals(location, dead);
            List<Location> result=locationDao.getLocationsByTown(location.getTown());
            Assert.assertFalse(result.contains(location)); //assert that its dead/null now
        }
    }
    
    @Test
    @Transactional
    public void testFindLocationsByCountryWhichDoesNotExistSuccess() {
        List<Location> list = locationDao.getLocationsByCountry("Abra Ka Dabra");
            Assert.assertEquals(list.size(),0);
    }
    
    @Test
    @Transactional
    public void testFindLocationsByTownWhichDoesNotExistSuccess() {
        List<Location> list = locationDao.getLocationsByCountry("ZYZABX");
            Assert.assertEquals(list.size(),0);
    }

    @Test
    @Transactional
    public void testFindLocationsByCountrySuccess() {
        try{
            locationDao.save(location);
            List<Location> list = locationDao.getLocationsByCountry(location.getCountry());
            Assert.assertEquals(list.size(),1);
            Assert.assertEquals(location,list.get(0));
        } finally {
            locationDao.delete(location);
        }
    }
    
    @Test
    @Transactional
    public void testFindLocationsByTownSuccess() {
        try{
            locationDao.save(location);
            List<Location> list = locationDao.getLocationsByTown(location.getTown());
            Assert.assertEquals(list.size(),1);
            Assert.assertEquals(location,list.get(0));
        } finally {
            locationDao.delete(location);
        }
    }
    
}
