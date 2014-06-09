package flymetomars.business.services;

import flymetomars.business.handling.PasswordService;
import flymetomars.business.core.EntityDeleter;
import flymetomars.business.core.EntityFactory;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.core.EntitySaver;
import flymetomars.business.core.EntityUpdater;
import flymetomars.business.handling.PasswordHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Locator pattern implementation via static utility class for the
 * provision of Thread-local constant loosely-coupled service implementations
 * 
 * @author Lawrence Colman
 */
public final class ServiceLocator {

    //<editor-fold defaultstate="collapsed" desc="private constructor">
    /**
     * private constructor - to make class effectively uninstanciable
     */
    private ServiceLocator() {}
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="hard-coded interface mappings">
    
    private static final Set<Class<?>> SUPPORTED=
        Collections.unmodifiableSet(new HashSet<Class<?>>(Arrays.asList(
            new Class<?>[]{
                AuthService.class,
                PasswordService.class,
                RegisterService.class
            }
        )))
    ;
    
    private static <Q> void initialiseForThread(
        Class<Q> serviceInterface,
        EntityLoader loader,
        EntitySaver saver,
        EntityUpdater updater,
        EntityDeleter deleter,
        EntityFactory factory
    ) {
        if(!supports(serviceInterface)) { throw new IllegalStateException(); }
        if(has(serviceInterface)) { throw new ConcurrentModificationException(); }
        if(serviceInterface.equals(AuthService.class)) {  //AuthServiceImpl
            AuthService instance=new AuthServiceImpl(
                loader,saver,updater,((PasswordHandler)
                    fetch(PasswordService.class,loader,saver,updater,deleter,factory))
            );
            load(serviceInterface).put(Thread.currentThread(), (Q)instance);
        } else
        if(serviceInterface.equals(PasswordService.class)) {  //PasswordHandler
            PasswordService instance=new PasswordHandler(loader);
            load(serviceInterface).put(Thread.currentThread(), (Q)instance);
        } else
        if(serviceInterface.equals(RegisterService.class)) {  //RegisterServiceImpl
            RegisterService instance=new RegisterServiceImpl(
                loader,saver,factory,((PasswordHandler)
                fetch(PasswordService.class,loader,saver,updater,deleter,factory))
            );
            load(serviceInterface).put(Thread.currentThread(), (Q)instance);
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="thread-safe static collections">
    private static final Map<Thread,AuthService> AUTHS=
            new ConcurrentHashMap<Thread,AuthService>()
            ;
    private static final Map<Thread,PasswordService> PASSES=
            new ConcurrentHashMap<Thread,PasswordService>()
            ;
    private static final Map<Thread,RegisterService> REGS=
            new ConcurrentHashMap<Thread,RegisterService>()
            ;
    //</editor-fold>
    
    /**
     * Retrieves (and instanciates if not already done so) thread-local copies of
     * given Service Interface and handler types for use by higher layer logic.
     * 
     * @param <S> The Reflective Class type argument matching desired service
     * @param serviceWanted Loosely-coupled service interface Class<> type to get
     * @param loader Core-component EntityLoader instance to use of initialisations
     * @param saver Core-component EntitySaver instance to use of initialisations
     * @param updater  Core-component EntityUpdater instance to use of initialisations
     * @param deleter  Core-component EntityDeleter instance to use of initialisations
     * @param factory Core-component EntityFactory instance to use of initialisations
     * @return A loosely-coupled instanciated thread-local instance of `serviceWanted'
     */
    public static <S> S fetch(
        Class<S> serviceWanted,
        EntityLoader loader,
        EntitySaver saver,
        EntityUpdater updater,
        EntityDeleter deleter,
        EntityFactory factory
    ) {
        if(!supports(serviceWanted)) {
            throw new IllegalArgumentException("Unable to load service class",
                    new ClassNotFoundException(serviceWanted.getCanonicalName())
            );
        }
        while(!has(serviceWanted)) {
            initialiseForThread(serviceWanted,loader,saver,updater,deleter,factory);
        }
        return load(serviceWanted).get(Thread.currentThread());
    }
    
    //<editor-fold desc="private support methods">
    
    /**
     * Returns flag value indication if this ServiceLocator supports a Class
     *
     * @param serviceClass the Class service interface to check for support of
     * @return boolean flag value indicating support of supplied Class
     */
    private static boolean supports(Class<?> serviceClass) {
        return SUPPORTED.contains(serviceClass);
    }
    
    /**
     * Returns flag indicating thread-local initialisation status for a mapping
     *
     * @param serviceClass Reference to Service Interface Definition
     * @return boolean flag value indicating whether or not an instance of the
     *          `serviceClass' type has been initalised for the current thread
     */
    private static boolean has(Class<?> serviceClass) {
        return load(serviceClass).containsKey(Thread.currentThread());
    }
    
    /**
     * Returns the appropriate Map collection for the given service interface
     *
     * @param <I> Interface Class to load Mapping data structure for
     * @param serviceMapping Reference to Service Interface Definition
     * @return appropriate Map collection for the given service interface
     * @throws UnsupportedOperationException thrown when serviceMapping is not supported
     */
    private static <I> Map<Thread,I> load(Class<I> serviceMapping) {
        if(serviceMapping.equals(AuthService.class)) {
            return (Map<Thread,I>)AUTHS;
        } else
            if(serviceMapping.equals(PasswordService.class)) {
                return (Map<Thread,I>)PASSES;
            } else
                if(serviceMapping.equals(RegisterService.class)) {
                    return (Map<Thread,I>)REGS;
                } else {
                    throw new UnsupportedOperationException(
                            new ClassNotFoundException(serviceMapping.getCanonicalName())
                            );
                }
    }
    
    //</editor-fold>
    
}
