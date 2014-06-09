package flymetomars.business.mining;

import flymetomars.business.Connection;
import flymetomars.common.datatransfer.Person;
import java.util.List;

/**
 * HowConnected Interface - Represents mining service for retrieving data about
 * how any two given Person model objects are connected within the system.
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 */
public interface HowConnected {
    
    /**
     * Connection Mining (ala LinkedIn graph diagrams) - allows the determinance
     * of an ordered sequence of Connections between two Person objects, such
     * that if followed, the sequence leads from a `start' Person to an `'end'.
     * 
     * Connections can be either shared Missions between people, or A Person
     * that has participated in two missions - each creates a conceptual link.
     * 
     * @param start Person to begin searching for a Connection from
     * @param end Person to search for a connection to from `start'
     * @return An ordered sequence of Connection objects that connect from the
     *          `start' Person to the `end' Person - or null if no Connection is
     *          possible.  Returns an empty List if start.equals(end) is true.
     */
    List<Connection> mineConnections(Person start, Person end);
    
}
