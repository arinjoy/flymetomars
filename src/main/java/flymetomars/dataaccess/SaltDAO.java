package flymetomars.dataaccess;

import flymetomars.common.datatransfer.Salt;

/**
 * Specific data access object loosely-coupled interface definition for Salt
 * 
 * @author Lawrence Colman
 */
public interface SaltDAO extends DAO<Salt> {
    
    /**
     * Entity loading method for String identified Salt objects from DAL
     * 
     * @param saltKey the unique String key code used to identify a Salt instance
     * @return Salt DTO object representing Salt entity answering to id `saltKey'
     */
    Salt load(String saltKey);
}
