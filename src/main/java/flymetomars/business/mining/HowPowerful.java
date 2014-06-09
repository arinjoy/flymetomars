package flymetomars.business.mining;

import flymetomars.business.model.Person;
import java.util.Set;

/**
 * HowPowerful Interface - Represents mining service for data pertaining to
 * "power" of Person domain objects.  Power is said to have been accumulated if
 * a Person connects many disparate social groups together.
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 */
public interface HowPowerful {
    
    /**
     * Get the most powerful brokers by checking each pair of Missions and
     * mining the two which are most non-interacting. "Most non-interacting" has
     * the meaning that their intersection of participants size are zero but also
     * their sizes are big enough to be become the two most non interacting
     * social circle among all. After finding those two missions, it invokes the
     * getPowerBrokersOfTwoMissionSocialCircles() get the power brokers of those
     * two missions
     * 
     * @return The most powerful brokers of all the existing social circles
     */
    Set<Person> getTheMostPowerfulBrokersOfAllSocialCircles();
    
}
