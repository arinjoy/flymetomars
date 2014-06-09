package flymetomars.dataaccess;

import flymetomars.common.datatransfer.Expertise;
import flymetomars.common.datatransfer.Expertise.ExpertiseLevel;
import java.util.List;

/**
 * Specific data access object loosely-coupled interface definition for Expertise
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface ExpertiseDAO extends EntityDAO<Expertise> {
    
    /**
     * Retrieves a sequence of Expertise domain objects matching their name
     * 
     * @param name String `name' of Expertise objects to be returned if matching
     * @return List of Expertise entity instances matching the specified `name'
     */
    List<Expertise> getExpertiseListByName(String name);
    
    /**
     * Retrieves a sequence of Expertise domain objects of a given level
     * 
     * @param level ExpertiseLevel given to retrieve Expertise entity objects by
     * @return List of Expertise entity instances matching the specified `level'
     */
    List<Expertise> getExpertiseListByLevel(ExpertiseLevel level);
    
}
