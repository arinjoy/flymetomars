package flymetomars.business.mining;

import flymetomars.business.model.Person;
import java.util.Set;

/**
 * HowFrontiers Interface - Represents mining service for metadata about the
 * "froniters" of a particular `target' Person.  This service deals with friends
 * and colleagues synonymously for the purposes of method naming and invocation.
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface HowFrontiers {
    
    /**
     * Retrieves a set of persons from database who are not a friend of
     * a given person upto (and including) a specified level of connections
     * 
     * @param target The person input 
     * @param maxLevels The specified level of connections
     * @param howMany The number of such new frontiers to be returned
     * @return A set of m persons that this person is not a friend with up to k connections
     */
    Set<Person> mineNewFroniters(Person target, int maxLevels, int howMany);
}
