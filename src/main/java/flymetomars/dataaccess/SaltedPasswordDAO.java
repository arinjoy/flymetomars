package flymetomars.dataaccess;


import flymetomars.common.datatransfer.Salt;
import flymetomars.common.datatransfer.SaltedPassword;
import java.util.List;

/**
 * Data Access Object (DAO) Interface for the "Salted Password" domain object.
 * 
 * @author Lawrence Colman
 */
public interface SaltedPasswordDAO extends EntityDAO<SaltedPassword>, DAO<SaltedPassword> {

    /**
     * Retrieves a collection of SaltedPassword domain representative objects
     * that all share in common within themselves a single Salt value
     * 
     * @param shared the Salt object to fetch the associated passwords of
     * @return List of SaltedPassword instances that 
     */
    List<SaltedPassword> getSaltedPasswordsSharingSameSalt(Salt shared);

}
