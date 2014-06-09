package flymetomars.business.mining;

import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import java.util.List;

/**
 * HowUnpopular Interface - Represents mining service for data concerning the
 * Mission participation of a given `target' person in relation to their social
 * group (i.e. the union of all of their social circles - a.k.a colleagues).
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 * @author Arinjoy Biswas
 */
public interface HowUnpopular {
    
    /**
     * Retrieves an ordered list of `howMany' Mission model objects that the
     * `target' Person has not been invited to, but their colleagues have been.
     * 
     * @param target Person to mine for Missions missed out on that friends aren't
     * @param howMany The number of "sour grapes" missions to return in the list
     * @return Ordered List of `howMany' Missions that `target' is sour about
     */
    List<Mission> mineSourGrapes(Person target, int howMany);
    
}
