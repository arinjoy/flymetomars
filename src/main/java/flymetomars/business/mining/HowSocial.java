package flymetomars.business.mining;

import flymetomars.business.SocialCircle;
import flymetomars.business.model.Person;
import java.util.List;

/**
 * HowSocial Interface - Represents mining service for calculating meatdata
 * concerning the associations of Persons who have participated in Missions
 * together.  This joint participation places the two in a "Social Circle".
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 */
public interface HowSocial {
    
    /**
     * Social Circle mining (a.k.a. Mission Groups) - provides for the mining of
     * metadata concerning the fellow Mission participants of a `target' Person.
     * 
     * @param target Person model object to retrieve SocialCircles of.
     * @return An arbitrarily ordered List of SocialCircle objects pertaining to
     *          the `target' Person.  Essentially all the participant sets of
     *          all of the Missions in which they themselves have participated.
     *          If the `target' Person has not participated in any Missions,
     *          then an empty set will be returned.
     * 
     * Note:
     *          By definition, each SocialCircle must include `target' within.
     */
    List<SocialCircle> mineSocialCircles(Person target);
    
    /**
     * Aggregative Social Circle mining - gives the maximumly-sized Social
     * Circle of a given `target' Person.  Essentially max(SocCirc(target)).
     * 
     * @param target Person model object to mine the biggest SocialCircle for.
     * @return A SocialCircle from the mined Social Circles of the `target'
     *          Person that is the largest SocialCircle found - calculated based
     *          upon the total number of unique Persons within each SocialCircle.
     *          Returns null when the `target' Person has no Mission participation
     * 
     * Note:
     *          By definition, the SocialCircle will include `target' within it.
     */
    SocialCircle mineMaxSocialCircle(Person target);
    
}
