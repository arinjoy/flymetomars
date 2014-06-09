package flymetomars.dataaccess;

import flymetomars.common.datatransfer.Location;
import java.util.List;

/**
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface LocationDAO extends EntityDAO<Location> {
    /**
     * Gives a list of locations in a particular country
     * @param country the String object of country name to search
     * @return list of Location objects matching the country name
     */
    List<Location> getLocationsByCountry(String country);
    
    /**
     * Gives a list of locations within a particular town
     * @param town the String object of the town to search
     * @return list of Location objects matching the town name
     */
    List<Location> getLocationsByTown(String town);
}
