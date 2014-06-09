package flymetomars.business;

import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;

/**
 * Connection - custom type representing a component along the chain that links
 * together two Person objects within the FlyMetoMars system.  Each instance of
 * this class encapsulates either a Mission that has been shared by two Persons
 * or a Person that has participated within two Missions.
 * 
 * As a direct member of the business package this class represents a custom type
 * 
 * @author Lawrence Colman
 */
public abstract class Connection {
    
    /**
     * Instantiates a Connection object representative of a Mission link.
     * 
     * @param miss The Mission model object that is to be wrapped as a Connection
     * @return Returns a new Connection instance that represents a Mission
     */
    public static Connection makeConnectionFromMission(Mission miss) {
        return new MissionConnection(miss);
    }
    
    /**
     * Instantiates a Connection object representative of a participant Person link.
     * 
     * @param per the Person model object that us to be wrapped as a Connection
     * @return Returns a new Connection instance that represents a Person
     */
    public static Connection makeConnectionFromPerson(Person per) {
        return new PersonConnection(per);
    }
    
    /**
     * Property accessor for the type-unsafe retrieval of internal model object.
     * 
     * @return A type-unsafe reference to the internally wrapped model object.
     * 
     * Note:
     *          The return value of this method should always be cast back using
     *          the Class type that is returned by the accompanying getInnerType
     */
    public abstract Object getInnerModel();
    
    /**
     * Property accessor for the type-safe Class type reference represented within
     * 
     * @return The Class type that is internally represented within this
     *          Connection.  This will be either Mission or Person.
     *
     */
    public abstract Class<?> getInnerType();

    
    /**
     * Model object wrapping/boxing protected class for type-safe Connections.
     * This class wraps a Mission object as a Connection.
     */
    protected static final class MissionConnection extends Connection {

        private Mission inner;
        
        private MissionConnection(Mission miss) {
            if(null==miss) { throw new NullArgumentException(); }
            this.inner=miss;
        }

        @Override
        public Object getInnerModel() {
            return this.inner;
        }

        @Override
        public Class<?> getInnerType() {
            return Mission.class;
        }
    }
    
    
    /**
     * Model object wrapping/boxing protected class for type-safe Connections.
     * This class wraps a Person participant as a Connection.
     */
    protected static final class PersonConnection extends Connection {
        
        private Person inner;

        private PersonConnection(Person per) {
            if(null==per) { throw new NullArgumentException(); }
            this.inner=per;
        }

        @Override
        public Object getInnerModel() {
            return this.inner;
        }

        @Override
        public Class<?> getInnerType() {
            return Person.class;
        }
    }
    
}
