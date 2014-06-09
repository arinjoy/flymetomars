package flymetomars.presentation.resources;

import flymetomars.business.core.DependencyException;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Location;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.business.model.Salt;
import flymetomars.business.model.SaltedPassword;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;

/**
 * This is not (despite the name suffix and directory-tree position) a test class
 * 
 * This class provides various transactional cleanup and initialisation runtimes
 * that prove fruitful for re-use within various resource system test classes.
 * 
 * @author Lawrence Colman
 */
public abstract class AbstractDataDependantResourceSystemTest {

    private EntityLoader loader;
    private EntityDeleter deleter;
    
    protected final String getProperty(String name) {
        return PropertiesLoadingSingletonUtilityForResourceSystemTest.retrieve(name);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Property mutators in light of JUnit dependency injection unavailiability for construction">
    protected void setLoader(EntityLoader loader) {
        this.loader = loader;
    }
    
    protected void setDeleter(EntityDeleter deleter) {
        this.deleter = deleter;
    }
    //</editor-fold>

    private void ensurePropertiesPopulated() {
        if(null==loader || null==deleter) {
            throw new IllegalStateException(
                "AbstractDataDependantResourceSystemTest usage error; please " +
                    "ensure that you have called both the setLoader and set" +
                    "Deleter methods with valid references prior to invocation"+
                    " of any other instance methods within this clas!",
            new NullPointerException(null==loader?"loader":"deleter"));
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Logging cruft boilerplate utility code">
    private final Logger logger=Logger.getLogger(this.getClass().getCanonicalName());
    
    private final void log(String msg) {this.log(Level.INFO,msg);}
    private final void log(Level lev, String msg) {
        this.logger.log(lev, msg);
    }
    private final void log(String msg, Throwable t) {
        this.logger.log(Level.SEVERE, msg, t);
    }
    //</editor-fold>
    
    public static int countTests(Class<?> clientTest) {
        int testCount=0;
        for(Method m :clientTest.getMethods()) {
            if(null!=m.getAnnotation(Test.class)) { testCount++; }
        }
        StringBuilder msg=new StringBuilder("Counted ");
        msg.append(Integer.toString(testCount));
        msg.append(" @Test JUnit annotations within ");
        msg.append(clientTest.getCanonicalName());
        Logger.getLogger(clientTest.getName()).log(Level.INFO, msg.toString());
        return testCount;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Methods and fields for managing instance setUp and Shutdown">
    private int testsRun=0;
    protected AbstractDataDependantResourceSystemTest(int testsSoFar) {
        this.testsRun=testsSoFar;
    }
    private void incTestsRun(int v){this.testsRun=testsRun+v;}
    protected boolean isTestsRun(int o){return this.testsRun==o;}
    protected final void beforeTestTick() {
        if(isTestsRun(0)) {
            doBeforeFirstTest();
        } else {
            ensurePropertiesPopulated();
        }
        incTestsRun(1);
    }
    
    private int testCount=-1;
    
    protected final void afterTestTick() {
        if(isTestsRun(testCount)) {
            doAfterLastTest();
        } else if(testsRun>testCount) {
            throw new IllegalStateException("Cardinality of run counter or test counter incorrect");
        }
    }
    
    private volatile boolean setTestCount=false;
    
    protected void setTestCounter(int howMany) {
        if(setTestCount) { throw new UnsupportedOperationException("Already set count property"); }
        this.testCount=howMany;
        setTestCount=true;
    }
    //</editor-fold>
    
    protected abstract void doBeforeFirstTest();
    protected abstract void doAfterLastTest();
    
    public final void removePeople() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(Person p : loader.loadAllPersons()) {
            try {
                deleter.deletePerson(p);
            } catch (DependencyException ex) {
                this.log("Error deleting Person for test clean-up", ex);
                if(null==de) { de=ex; }  //ensures deletes still attempted on others
            }
        }
        if(null!=de) { throw de; }
    }
 
    public final void removePasswords() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(SaltedPassword sp : loader.loadAllSaltedPasswords()) {
            try {
                deleter.deleteSaltedPassword(sp);
            } catch (DependencyException ex) {
                if(null==de) { de=ex; }  //ensures deletes still attempted on others
            }
        }
        if(null!=de) { throw de; }
    }
    
    public final void removeSalts() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(Salt s : loader.loadAllSalts()) {
            try {
                deleter.deleteSalt(s);
            } catch (DependencyException ex) {
                if(null==de) { de=ex; }  //ensures deletes still attempted on others
            }
        }
        if(null!=de) { throw de; }
    }
    
    public final void removeInvites() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(Person p : loader.loadAllPersons()) {
            for(Invitation i : loader.loadInvitationsByCreator(p)) {
                try {
                    deleter.deleteInvitation(i);
                } catch (DependencyException ex) {
                    this.log("Error deleting Invitation for test clean-up", ex);
                    if(null==de) { de=ex; }  //ensures deletes still attempted on others
                }
            }
        }
        if(null!=de) { throw de; }
    }
    
    public final void removeMissions() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(Mission m : loader.loadAllMissions()) {
            try {
                deleter.deleteMission(m);
            } catch (DependencyException ex) {
                this.log("Error deleting Mission for test clean-up", ex);
                if(null==de) { de=ex; }  //ensures deletes still attempted on others
            }
        }
        if(null!=de) { throw de; }
    }
    
    public final void removeLocations() throws DependencyException {
        ensurePropertiesPopulated();
        DependencyException de=null;
        for(Location l : loader.loadAllLocations()) {
            try {
                deleter.deleteLocation(l);
            } catch (DependencyException ex) {
                this.log("Error deleting Location for test clean-up", ex);
                if(null==de) { de=ex; }  //ensures deletes still attempted on others
            }
        }
        if(null!=de) { throw de; }
    }
    
}
