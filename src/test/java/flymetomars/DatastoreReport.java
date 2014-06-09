package flymetomars;

import flymetomars.dataaccess.DAO;
import flymetomars.dataaccess.ExpertiseDAO;
import flymetomars.dataaccess.InvitationDAO;
import flymetomars.dataaccess.LocationDAO;
import flymetomars.dataaccess.MissionDAO;
import flymetomars.dataaccess.PersonDAO;
import flymetomars.dataaccess.SaltDAO;
import flymetomars.dataaccess.SaltedPasswordDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.impl.SessionFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Despite the annotations, this class is not a test - but rather a testing unit
 * composable into the larger testing programs (for both integration as well as
 * system tests) which reports the Hibernate DDL generation configuration option
 * and reports tallies for each of the data access layer DAOs from database.
 * 
 * @author Lawrence Colman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-wiring.xml"})
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class
})
public class DatastoreReport extends Object {
    
    //<editor-fold defaultstate="collapsed" desc="DAO injections">
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
    
    @Autowired
    private SessionFactoryImpl hibernate;
    
    //<editor-fold defaultstate="collapsed" desc="Utility method to retrieve DAO names from Hibernate proxies">
    private <M> String establishResponcibility(DAO<M> dao) {
        final Class<?>[] cls=new Class<?>[] {
            PersonDAO.class,
            MissionDAO.class,
            InvitationDAO.class,
            LocationDAO.class,
            ExpertiseDAO.class,
            SaltedPasswordDAO.class,
            SaltDAO.class
        };
        for(Class<?> dt : cls) {
            try { dt.cast(dao); } catch(ClassCastException e) { continue; }
            return dt.getSimpleName();
        }
        return "[UNKNOWN]";
    }
    //</editor-fold>
    
    @Test
    public void reportExistingData() {
        final DAO<?>[] daos=new DAO<?>[] {
            personDao,
            missionDao,
            invitationDao,
            locationDao,
            expertiseDao,
            passwordDao,
            saltDao
        };
        StringBuilder report=new StringBuilder("Data entry tallies for FlyMeToMars data access objects:");
        for(DAO dao : daos) {
            report.append("\n");
            report.append(establishResponcibility(dao));
            report.append(" reports:  ");
            report.append(Integer.toString(dao.getAll().size()));
            report.append(" entities present.");
        }
        Logger.getLogger(getClass().getCanonicalName()).log(Level.INFO, report.toString());
    }
    
    @Test
    public void reportHibernateConfig() {
        StringBuilder rep=new StringBuilder("Hibernate Configuration for DLL auto generation is: ");
        rep.append(this.hibernate.getProperties().getProperty("hibernate.hbm2ddl.auto"));
        Logger.getLogger(getClass().getCanonicalName()).log(Level.INFO, rep.toString());
    }
    
}
