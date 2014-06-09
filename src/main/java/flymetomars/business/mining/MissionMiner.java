package flymetomars.business.mining;

import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Invitation;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;

/**
 * Mission mining features and logic for determining new (mined) relationships,
 * facts, notes or other data & meta data pertaining to Mission model objects.
 *
 * As an implementation within mining package - this class performs data mining.
 *
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class MissionMiner implements HowUnpopular, HowOpportune {

    private EntityLoader loader;

    /**
     * Constructor method - accepts the dependency injected EntityLoader object
     * 
     * @param load EntityLoader instance for use from getAllTheMissionsInDateRange
     * 
     * Note:
     *          If not executing either of:
     *              * mineOpportuneTimes
     *              * getAllTheMissionsInDateRange
     *          then `load' may be passed as null without any issue.
     */
    public MissionMiner(EntityLoader load) {
        this.loader = load;

    }
    
    /**
     * eif - Exception-if-condition.
     * 
     * Small utility method for easier null/integrity checks within implementation
     * code without increasing McCabe's Cyclomatic Copmplexity (MCC) score.
     */
    private static void eif(boolean condition, RuntimeException err) { 
        if(condition) { throw err; }
    }

    /**
     * Retrieve the opportune times for a person such that given a time frame
     * return a set of top-k dates to ensure maximal friend potential
     *
     * @param person The Input Person model object
     * @param startDate the start date of the time frame
     * @param endDate the end date of the time frame
     * @param k the number of dates to be returned
     * @return the top-k dates
     * @throws NullArgumentException if any of the reference-type arguments are null
     * @throws IllegalArgumentException when `k' is zero or negative
     */
    @Override
    public List<Date> mineOpportuneTimes(Person person, Date startDate, Date endDate, int k) {
        eif((null==person),
            new NullArgumentException("The opportune times cannot be searched for a null person")
        );
        eif((k <= 0),
            new IllegalArgumentException("The number of opportunes time to be returned must at least 1")
        );
        eif((null == startDate),
            new NullArgumentException("The opportune times end date cannot be null")
        );
        eif((null == endDate),
            new NullArgumentException("The opportune times end date cannot be null")
        );
        eif(startDate.getTime() > endDate.getTime(),
            new IllegalArgumentException("The start date cannot be ahead of end date")
        );
        // get the friends who will meet before the start date
        Set<Person> alreadyFriends = this.getFriendsWhoWillMeetBeforeDate(person, startDate);
        // get all the missions in that time frame
        List<Mission> missionsInDateRange = this.getAllTheMissionsInDateRange(startDate, endDate);
        // if no freinds will be met before start date or no missions between that range
        // then return empty list of dates
        if (alreadyFriends.isEmpty() || missionsInDateRange.isEmpty()) {
            return Collections.emptyList();
        }
        // list of all the potential dates when the friends will me met again
        // i.e. the the participation potential of same friends again
        List<Date> potentialMissionDates = new ArrayList<Date>();
        // copy the mission dates in a list
        for (Mission m : missionsInDateRange) {
            if (!potentialMissionDates.contains(m.getTime())) {
                potentialMissionDates.add(m.getTime());
            }
        }
        // integer array to store the potential value of date that
        // a friend is likely to participate again
        int[] potentialDegree = new int[potentialMissionDates.size()];
        // for each mission in that date range find all the participants
        // and see how many of them already are friends
        for (Mission m : missionsInDateRange) {
            Bag participants = new HashBag(m.getParticipantSet());
            int count = 0;
            //count indicates the number of re-particpating by a friend
            for (Person p : alreadyFriends) {
                count+=Math.min(1, participants.getCount(p));
            }
            // finding the date of that mission and store its potenital value
            int position = potentialMissionDates.indexOf(m.getTime());
            potentialDegree[position] += count;
        }
        // if the number of top dates being asked is more than the actual list date possible
        // then return all such dates
        if (k > potentialMissionDates.size()) {
            return potentialMissionDates;
        }
        //otherwise, return only the top-k after sorting them bassed on the poteitial values
        Map<Date, Integer> map = new HashMap<Date, Integer>();
        for (Date d : potentialMissionDates) {
            int position = potentialMissionDates.indexOf(d.getTime());
            map.put(d, potentialDegree[position]);
        }
        //Sort the result
        List<Map.Entry<Date, Integer>> sorted = new ArrayList<Map.Entry<Date, Integer>>(map.entrySet());
        Collections.sort(sorted, new Comparator<Map.Entry<Date, Integer>>() {
            @Override
            public int compare(Map.Entry<Date, Integer> o1, Map.Entry<Date, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        List<Date> result = new ArrayList<Date>();  // top-k dates to be returned
        for (int i = 0; i < k; i++) {
            result.add(sorted.get(i).getKey());
        }
        return result;
    }

    /**
     * Find the set of missions for this person and filter them before a given
     * date
     *
     * @param person The input person
     * @param givenDate the date input
     * @return The list of all friends who will meet in any mission before this
     * date
     */
    public Set<Person> getFriendsWhoWillMeetBeforeDate(Person person, Date givenDate) {
        if (null == person) {
            throw new NullArgumentException("The person input cannot be null");
        }
        if (null == givenDate) {
            throw new NullArgumentException("The given date cannot be null");
        }
        Set<Mission> aMissionSet = new HashSet<Mission>();
        for (Mission m : person.getMissionsRegistered()) {
            if (m.getTime().before(givenDate)) {
                aMissionSet.add(m);
            }
        }
        Set<Person> metFriends = new HashSet<Person>();
        // get the friends who will meet him before the date
        for (Mission m1 : aMissionSet) {
            for (Person p : m1.getParticipantSet()) {
                metFriends.add(p);
            }
        }
        // delete the person himslef from the set of friends
        metFriends.remove(person);
        return metFriends;
    }

    /**
     * Gives the list of all the missions in a given date range
     *
     * @param startDate
     * @param endDate
     */
    public List<Mission> getAllTheMissionsInDateRange(Date startDate, Date endDate) {
        if (null == startDate) {
            throw new NullArgumentException("The input start date cannot be null");
        }
        if (null == startDate) {
            throw new NullArgumentException("The input end date cannot be null");
        }
        if (startDate.getTime() > endDate.getTime()) {
            throw new IllegalArgumentException("The start date cannot be ahead of end date");
        }

        // loading all the exisitng missions from dataaccess layer by calling the core 
        List<Mission> missionList = loader.loadAllMissions();
        List<Mission> missionsInDateRange = new ArrayList<Mission>();
        // filter the list of all the missions within the time frame fiven
        for (Mission m : missionList) {
            if (m.getTime().after(startDate)
                    && m.getTime().before(endDate)) {
                missionsInDateRange.add(m);
            }
        }
        return missionsInDateRange;
    }

    /**
     * Get the missions that have sent invitations to this person's friends
     * while not to this person, i.e. the SourGrapes of this person
     *
     * @param target the Person of who has some sour grapes
     * @param howMany the size of the set to be returned
     * @return the set of the missions that did not invite this person when
     * inviting his/her friends
     */
    @Override
    public List<Mission> mineSourGrapes(Person person, int k) {
        if (null == person) {
            throw new NullArgumentException("The sour grapes cannot be searched for a null person");
        }
        if (k <= 0) {
            throw new IllegalArgumentException("The number of sour grapes to be returned must at least 1");
        }
        Date now = new Date();
        //build a set of "upcoming" missions that target has invites to
        Set<Mission> invitedMissions = new HashSet<Mission>();
        for (Invitation invRec : person.getInvitationsReceived()) {
            Mission miss = invRec.getMission();
            if (miss.getTime().after(now)) {
                invitedMissions.add(miss);
            }
        }
        //find upcoming missions which friends have recieved invitations for
        final Map<Mission, Integer> friendInviteCount = new HashMap<Mission, Integer>();
        for (Person friend : (new PersonMiner(null)).mineColleagues(person)) {
            for (Invitation fInv : friend.getInvitationsReceived()) {
                Mission mis = fInv.getMission();
                if (mis.getTime().before(now)) {
                    continue;
                }  //skip missions in past
                if (!friendInviteCount.containsKey(mis)) {
                    friendInviteCount.put(mis, 0);
                }
                friendInviteCount.put(mis, friendInviteCount.get(mis) + 1);
            }
        }
        //calculare disjoint set for the missions that target is sour about
        Set<Mission> missedOutMissions = friendInviteCount.keySet();
        missedOutMissions.removeAll(invitedMissions);
        //populate result set in any ad-hoc order from set
        List<Mission> result = new LinkedList<Mission>(missedOutMissions);
        //sort the result set by the number of invitations recieved by friends
        Collections.sort(result, new Comparator<Mission>() {
            @Override
            public int compare(Mission m1, Mission m2) {
                return (friendInviteCount.get(m2) - friendInviteCount.get(m1));
            }
        });
        if (result.size() < k) {
            return Collections.unmodifiableList(result);
        }
        return Collections.unmodifiableList(result.subList(0, k));
    }
    
}
