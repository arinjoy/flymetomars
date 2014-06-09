package flymetomars.dataaccess;


import flymetomars.common.datatransfer.Location;
import flymetomars.common.datatransfer.Mission;
import flymetomars.common.datatransfer.Person;
import java.util.Date;
import java.util.List;


/**
 * 
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public interface MissionDAO extends EntityDAO<Mission> {
    
    /**
     * Retrieves a list of Mission objects where the captain property is equal
     * to the value of `captain' as passed-in as an argument to this method.
     * 
     * @param captain The Person instance to search for Missions captained by
     * @return List of Mission objects where `captian' is captain, or empty list
     * @throws NullArgumentException thrown when `captain' is a null Person ref
     * @throws UnserialisedEntityException thrown when the Person object passed
     *          in has not been serialised to the database for searching yet.
     */
     List<Mission> getMissionsByCaptain(Person captain) throws UnserialisedEntityException;
     
     /**
      * Retrieves the Mission object with a specific unique `name' field.
      * 
      * @param name the name that is to be looked up when retrieving the Mission
      * @return The single Mission instance that matches `name', or null if none found
      * @throws UnserialisedEntityException thrown when database is in an illegal state
      */
      Mission getMissionByName(String name) throws UnserialisedEntityException;
     /**
      * Returns a list of Mission instances from the data-store where the time
      * property of each Mission is in between a `begin' and `end' Date.
      * 
      * @param begin Date object representing lower bound (earlier in time).
      * @param end Date object representing upper bound (later in time).
      * @return List of Mission instances from data-store where the time field
      *         is after the `begin' Date value and before the `end' Date value.
      * @throws NullArgumentException thrown when either of the Date's is null
      */
     List<Mission> getMissionsByDateRange(Date begin, Date end);
     
     /**
      * Returns a list of Missions where the value of the Location field is `loc'.
      * 
      * @param loc The Location by which to lookup Missions.
      * @return A list of Missions found for `loc' (which may be empty)
      */
     List<Mission> getMissionsByLocation(Location loc) throws UnserialisedEntityException;
     
}
