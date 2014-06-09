package flymetomars.dataaccess;


import flymetomars.common.datatransfer.Invitation;
import flymetomars.common.datatransfer.Person;
import java.util.Date;
import java.util.List;

/**
 * 
 * 
 * @author Yuan-Fang Li
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface InvitationDAO extends EntityDAO<Invitation> {
    
    /**
     * Gives a list of invitations created by a particular person (creator)
     * 
     * @param creator the Person object which created the Invitations
     * @return list of Invitation where the creator field is `creator'
     * @throws NullArgumentException thrown when creator is a null ref
     * @throws UnserialisedEntityException thrown when creator is not persisted
     */
    List<Invitation> getInvitationsByCreator(Person creator) throws UnserialisedEntityException;
    
    /**
     * Pulls a list of invitations received by a particular person (recipient)
     * 
     * @param creator the Person object which received the Invitations
     * @return list of Invitation where the recipient field is `recipient'
     * @throws NullArgumentException thrown when recipient is a null ref
     * @throws UnserialisedEntityException thrown when recipient is not persisted
     */
    List<Invitation> getInvitationsByRecipient(Person recipient) throws UnserialisedEntityException;
    
    /**
     * Retrieves a list of Invitation model objects that have a lastUpdated
     * value which is less than the specified `when' argument.
     * 
     * @param when the Date before which returned Invitations must be
     * @return list of Invitation's updated  before a particular past Date
     * @throws NullArgumentException thrown when `when' is a null reference
     */
    List<Invitation> getInvitationsBeforeDate(Date when);
    
    /**
     * Returns a list of Invitation objects which all have a lastUpdated field
     * value of greater than the `when' Date argument.
     * 
     * @param when the Date after which returned Invitations must be
     * @return list of Invitation objects updated since a particular past Date
     * @throws NullArgumentException thrown when `when' is a null reference
     * @throws DateInFutureException thrown when `when' is actually after today
     */
    List<Invitation> getInvitationsSinceDate(Date when);
    

}
