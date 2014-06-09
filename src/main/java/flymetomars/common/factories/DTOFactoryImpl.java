package flymetomars.common.factories;

import flymetomars.common.datatransfer.SaltedPassword;
import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.ExpertiseImpl;
import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.InvitationImpl;
import flymetomars.common.datatransfer.Location;
import flymetomars.common.datatransfer.LocationImpl;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.MissionImpl;
import flymetomars.common.datatransfer.Person;
import flymetomars.common.datatransfer.PersonImpl;
import flymetomars.common.datatransfer.PlainPassword;
import flymetomars.common.datatransfer.PlainPasswordImpl;
import flymetomars.common.datatransfer.Salt;
import flymetomars.common.datatransfer.SaltImpl;
import flymetomars.common.datatransfer.SaltedPasswordImpl;
import java.util.HashMap;
import java.util.Map;
import javax.management.ReflectionException;

/**
 * This class implements the implementation logic for a type-safe DTO instance
 * factory.  It is itself strongly-coupled to the *-DAOImpl classes via its
 * constructor - however the core pattern interface is separately defined (in
 * the DTOFactory interface), and the essential underlying HashMap pattern of
 * instance lookup is largely defined in AbstractDTOFactory (separate from any
 * DTO implementation concerns).
 * 
 * @author Lawrence Colman
 */
public final class DTOFactoryImpl extends AbstractDTOFactory implements DTOFactory {

    private final Map<Class,InterfaceImplementationMapping> typeMapping;
    
    private static DTOFactoryImpl obj=new DTOFactoryImpl();
        
    /**
     * Default constructor for the reference implementation of the DTOFactory.
     * Includes a hard-coded mapping of domain object data transfer objects.
     */
    private DTOFactoryImpl() {
        this.typeMapping=new HashMap<Class,InterfaceImplementationMapping>();
        this.typeMapping.put(Invitation.class, new InterfaceImplementationMapping(Invitation.class,InvitationImpl.class));
        this.typeMapping.put(Mission.class, new InterfaceImplementationMapping(Mission.class,MissionImpl.class));
        this.typeMapping.put(Location.class, new InterfaceImplementationMapping(Location.class,LocationImpl.class));
        this.typeMapping.put(Person.class, new InterfaceImplementationMapping(Person.class,PersonImpl.class));
        this.typeMapping.put(Expertise.class, new InterfaceImplementationMapping(Expertise.class,ExpertiseImpl.class));
        this.typeMapping.put(SaltedPassword.class, new InterfaceImplementationMapping(SaltedPassword.class,SaltedPasswordImpl.class));
        this.typeMapping.put(Salt.class, new InterfaceImplementationMapping(Salt.class,SaltImpl.class));
        this.typeMapping.put(PlainPassword.class, new InterfaceImplementationMapping(PlainPassword.class,PlainPasswordImpl.class));
    }
    
    /**
     * singleton instance accessor method
     * @return the singletonian instance of this class
     */
    public static DTOFactory getInstance() { return DTOFactoryImpl.obj; }
    
    @Override
    /**
     * Implementation of DTOFactory interface createDTO method, to create DTO instances.
     * @param domainInterface the strongly-typed interface reference for instantiation.
     * @returns An instance of the specified DTO interface, if available within mapping.
     * @throws ClassNotFoundException thrown when the interface is not found in mapping
     */
    public <T> T createDTO(Class<T> domainInterface) throws ClassNotFoundException {
        if(!this.typeMapping.keySet().contains(domainInterface)) {
            throw new ClassNotFoundException(domainInterface.getCanonicalName());
        }
        T result;
        Class<T> typ=this.typeMapping.get(domainInterface).getSafeConcrete();
        try {
            result=typ.newInstance();
        } catch(InstantiationException ie) {
            throw new ClassNotFoundException(ie.getMessage(), ie);
        } catch(IllegalAccessException iae) {
            throw new UnsupportedOperationException(new ReflectionException(iae));
        }
        return result;
        
    }

}
