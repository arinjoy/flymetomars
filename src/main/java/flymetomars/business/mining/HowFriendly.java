package flymetomars.business.mining;

import flymetomars.business.model.Person;
import java.util.List;
import java.util.Set;

/**
 * HowFriendly Interface - Represents mining service for data pertaining to the
 * "friends" of a particular `target' Person.  This service deals with friends
 * and colleagues synonymously for the purposes of method naming and invocation.
 * 
 * As an interface in the mining package - this represents a data mining service
 * 
 * @author Lawrence Colman
 */
public interface HowFriendly {

    /**
     * Colleagues mining (a.k.a. Friends) for a given `target' Person model
     * object - this method will give a set of mission co-participants.
     * 
     * @param target The Person model object to mine the Mission associates of.
     * @return Set of Persons whom have shared a Mission with `target' Person.
     * 
     * Note:    This method's return value inherently excludes `target' themself
     */
    Set<Person> mineColleagues(Person target);  //friends

    /**
     * Extended Colleagues mining (a.k.a. Friends of Friends) - establishes a
     * set of the colleagues of a given `target' Person's "friends".  This
     * excludes any such Persons that the `target' Person has participated with
     * in at least one Mission previously (i.e. does not include direct friends).
     * 
     * @param target The Person model object to mine the Mission associates of.
     * @return Set of Persons that have been on a Mission with friends of `target'.
     */
    Set<Person> mineFriendsOfFriends(Person target);

    /**
     * Popular Colleagues mining (a.k.a. Buddies) - returns an ordered list of
     * the top `howMany' Friends of a supplied `target' Person as per ordering
     * via the number of shared Mission participation between the two Persons.
     * 
     * @param target The Person model object to mine the frequent colleagues of.
     * @param howMany The number of popular-colleagues to include in return list
     * @return Sequence of the top `howMany' "Friends" that this person most
     *          often shares in Mission participation with.
     */
    List<Person> mineTopFriends(Person target, int howMany);  //buddies

}
