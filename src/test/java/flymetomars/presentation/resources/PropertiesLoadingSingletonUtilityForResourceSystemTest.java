package flymetomars.presentation.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is not (despite the name suffix and directory-tree position) a test class
 * 
 * This class provides a statically initialised (and ergo thread-safe) utility
 * for other (Actual) System-Tests to load Java properties from the applications
 * app.properties file - such as the all-important port-number in use by Jetty.
 * 
 * @author Lawrence Colman
 */
public class PropertiesLoadingSingletonUtilityForResourceSystemTest {
    
    private static final String PROP_FILE="app.properties";
    
    private static final Logger LOG=Logger.getLogger(
        PropertiesLoadingSingletonUtilityForResourceSystemTest.class.getCanonicalName()
    );
    
    private final Properties appProps=new Properties();
    private volatile boolean loadSuccess=false;
    private final Semaphore loadPerm=new Semaphore(1);
    
    private static final PropertiesLoadingSingletonUtilityForResourceSystemTest instance=
        new PropertiesLoadingSingletonUtilityForResourceSystemTest()
    ;
    
    private PropertiesLoadingSingletonUtilityForResourceSystemTest() {
        log("Properties Utility Singleton Initializing...");
        StringBuilder wind=null;
        try {
            load();
        } catch(UnsupportedOperationException e) {
            wind=new StringBuilder("Error Occurred Loading ");
            wind.append(PROP_FILE);
            wind.append("File!  Will Retry at first retrieve invocation.");
        }
        if(null==wind) {
            wind=new StringBuilder("Finished loading properties from ");
            wind.append(PROP_FILE);
            wind.append("File - Loaded ");
            wind.append(Integer.toString(appProps.size()));
            wind.append(" property/ies in total.");
        }
        log(wind.toString());
    }
    
    private synchronized void load() {
        if(loadSuccess) {
            throw new IllegalStateException();
        }
        try {
            loadPerm.acquire();
        } catch(InterruptedException ie) {
            throw new UnsupportedOperationException("Concurrency Error Loading Proprties",ie);
        }
        InputStream resource = getClass().getClassLoader().getResourceAsStream(PROP_FILE);
        try {
            appProps.load(new InputStreamReader(resource));
            this.loadSuccess=true;
            StringBuilder load=new StringBuilder("Successfully loaded ");
            load.append(Integer.toString(appProps.size()));
            load.append(" property/ies from ");
            load.append(PROP_FILE);
            load.append(" file.");
            log(load.toString());
        } catch (IOException ioe) {
            throw new UnsupportedOperationException("Fatal Error Loading Proprties File!",ioe);
        } finally {
            loadPerm.release();
        }
    }
    
    private static void log(String msg) {
        LOG.log(Level.INFO, msg);
    }
    
    public final String getPropery(String name) {
        if(!loadSuccess) { load(); }
        return appProps.getProperty(name);
    }
    
    public static String retrieve(String prop) {
        return instance.getPropery(prop);
    }
    
}
