package flymetomars.business.mining;

import flymetomars.business.model.Person;
import java.util.Date;
import java.util.List;

/**
 * HowOpportune Interface - Represents mining service for data pertaining to the
 * Mission participation of a given `target' person within a given time frame.
 * ("time frame" is specified as a particular `startDate' and `endDate'.)
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public interface HowOpportune {
   
    /**
     * Retrieves an ordered list of `howMany' Date objects that represent the
     * best days for a given `target' Person to go on Missions to ensure maximal
     * friend/colleague potential in their mission participation.
     * 
     * @param target the target Person about which to mine for most new friends
     * @param startDate Date representing begin time of given time frame
     * @param endDate Date representing finish time of given time frame
     * @param howMany Integer number of Opportune Dates to return in order
     * @return An ordered sequence of Date objects of the most opportune times
     */
    List<Date> mineOpportuneTimes(Person target, Date startDate, Date endDate, int howMany);
    
}
