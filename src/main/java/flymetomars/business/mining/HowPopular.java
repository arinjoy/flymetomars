package flymetomars.business.mining;

import flymetomars.business.model.Person;
import java.util.List;

/**
 * HowPopular Interface - Represents mining service for retrieving data about
 * the popularity of Persons within the FlyMeToMars registration system.  This
 * service is dealing mainly with 'celebrities' - popular Person model objects.
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 */
public interface HowPopular {
    
    /**
     * Celebrity Mining (a.k.a. Popularity Contest) - provides way to retrieve
     * the most "popular" people in terms on Mission-Invites for the system.
     * The 'Top' celebrity is the Person with the most Invitations received.
     * 
     * @param howMany The number of celebrities to return within the ordered list
     * @return Ordered list (most prominent first) of `howMany' celebrity Persons
     */
    List<Person> mineTopCelebrities(int howMany);
    
}
