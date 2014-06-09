package flymetomars.business.mining;

import flymetomars.business.SocialCircle;
import flymetomars.business.core.EntityLoader;
import flymetomars.business.model.Mission;
import flymetomars.business.model.Person;
import flymetomars.common.NullArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Person mining features and logic for determining new (mined) relationships,
 * facts, notes or other data & meta data pertaining to Person model objects.
 * 
 * As an implementation within mining package - this class performs data mining.
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public class PersonMiner implements HowFriendly, HowPopular, HowSocial, HowFrontiers, HowPowerful/*, HowConnected*/ {
    
    private  EntityLoader loader;

    /**
     * Constructor method - accepts the dependency injected EntityLoader object
     * 
     * @param load EntityLoader instance for used for the Top-K buddies as well
     * as 
     * 
     * Note:
     *          If not executing either of:
     *              * getTheMostPowerfulBrokersOfAllSocialCircles
     *              * mineNewFroniters
     *          then `load' may be passed as null without any issue.
     */
    public PersonMiner(EntityLoader load) {
        this.loader = load;
                
    }
    
    
    /**
     * Return the top-k celebrities after getting all the persons from
     * database 
     * @param k is the size of the set to be returned
     */
    @Override
    public List<Person> mineTopCelebrities(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("The numner celebrities to be retuend cannot be zero");
        }
        List<Person> persons = loader.loadAllPersons();
        if (persons.isEmpty()) { return Collections.emptyList(); }
        // Sort persons according to the received invitation numbers
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p2.getInvitationsReceived().size() - p1.getInvitationsReceived().size(); //descending order
            }
        });
        // Get the top k persons with the most invitations received
        return Collections.unmodifiableList(persons.subList(0, k));
    }
     
     /**
     * Return all the colleagues of a this person who have been or will be
     * meeting in at least one of the registered missions of this person
     * 
     * @param person is the input person
     * @return 
     */
    @Override
    public Set<Person> mineColleagues(Person person) {
        if(person == null){
            throw new NullArgumentException("The Person whose colleagues are being searched cannot be null");
        }
        Set<Person> result=new HashSet<Person>();
        for(Mission m : person.getMissionsRegistered()) {
            result.addAll(m.getParticipantSet());
        }
        result.remove(person);
        return result;
    }
    
     /**
     * Get the set of top-k buddies for a given person whom
     * this person interacts most
     *
     * @param person the person of the buddies to be returned.
     * @param k  the size of the set to be returned.
     */
    @Override
    public List<Person> mineTopFriends(Person person, int k) {
        if(person == null){
            throw new NullArgumentException("The Person whose top-k buddies are being searched cannot be null");
        }
        if(k<=0){
            throw new IllegalArgumentException("The number of buddies to be returned must be moe than zero");
        }
        //Get the missions this person has registered
        Set<Mission> missions = person.getMissionsRegistered();
        //get the friends set of this person
        Set<Person> friends = this.mineColleagues(person);
        if(friends.isEmpty()){
            throw new IllegalArgumentException("The top-k buddies cannot be searched when no buddy exists at all");
        }
        //Convert set to list (to get the order)
        List<Person> relatedPersons = new ArrayList<Person>(friends);
        //New a HashMap for Person and Integer
        final Map<Person, Integer> map = new HashMap<Person, Integer>();
        //Find the most registred person in the missions and link the no. of attendance with this person
        for (int i = 0; i < relatedPersons.size(); i++) {
            int j = 0;
            for (Mission m : missions) {
                if (m.getParticipantSet().contains(relatedPersons.get(i))) {
                    j++;
                }
            }
            map.put(relatedPersons.get(i), j);
        }
        List<Person> sorted = new ArrayList<Person>(map.keySet());
        Collections.sort(sorted, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return (map.get(p2) - map.get(p1));
            }
        });
        if(k<sorted.size()) {
          return sorted.subList(0, k);

        }
        else {
            return sorted;
        }
    }

    /**
     * Retrieves the colleagues/friends  of all the colleagues of a person
     * @param person whose colleagues of colleagues is searched 
     * @return a set of person
     */ 
    @Override
    public Set<Person> mineFriendsOfFriends(Person person) {
        if(person == null){
            throw new NullArgumentException("The Person whose colleagues of colleaagues are being searched cannot be null");
        }
        Set<Person> result=new HashSet<Person>();
        Set<Person> col=this.mineColleagues(person);
        for(Person p : col) {
            result.addAll(this.mineColleagues(p));
        }
        col.add(person);
        result.removeAll(col);
        return result;
    }

    /**
     * Retrieves all of the "social circles" (a.k.a. Mission Groups) of a
     * given person model object.
     * @param person Person model object to use for mining Mission Groups
     * @return List of "social circles" (MissionGroups) for Person `person'
     */
    @Override
    public List<SocialCircle> mineSocialCircles(Person person) { /*BROKEN LOGIC*/
        if(person == null){
            throw new NullArgumentException("The Person whose all social circles are being searched cannot be null");
        }
        List<SocialCircle> result=new ArrayList<SocialCircle>();
        for(Mission circle : person.getMissionsRegistered()) {
            result.add(new SocialCircle(Collections.unmodifiableSet(new HashSet<Person>(circle.getParticipantSet()))));
        }
        return Collections.unmodifiableList(result);
    }
    
    /**
     * Implements the MaxSocialCircles mining query that returns the social circle
     * with the maximum size
     * @param person Person model object to use in mining largest Mission Group
     * @return set of person in the max social circle
     */
    @Override
    public SocialCircle mineMaxSocialCircle(Person person) {
        if(person == null){
            throw new NullArgumentException("The Person whose Max soical circle is being searched cannot be null");
        }
        List<SocialCircle> socialCircles = this.mineSocialCircles(person);
        Set<Person> result=Collections.emptySet();
        for(Set<Person> group : socialCircles) {
            if(group.size() > result.size()) {
                result=group;
            }
        }
        return new SocialCircle(Collections.unmodifiableSet(result));
    }
    
    /**
     * Get the set of persons who connect two mostly non-interacting social circles
     * @param m1 participants of mission m1, i.e. a social circle
     * @param m2 participants of another social circle, i.e. another social circle
     * @return the set of the persons who connect these mostly non-interacting social circles
     * if the number of interactions is less than a threshold value, i.e. m1_size X m2_size / 2
     */
    //@Override
    public Set<Person> getPowerBrokersOfTwoMissionSocialCircles(Mission m1, Mission m2) {
        if (m1 == null || m2 == null) {
            throw new NullArgumentException("Input missions of social circles cannot be null");
        }
        //the result to be returned
        Set<Person> powerBrokers = new HashSet<Person>();

        Set<Person> socialCircle1 = m1.getParticipantSet();
        Set<Person> socialCircle2 = m2.getParticipantSet();
        
        Set<Person> common = new HashSet(m1.getParticipantSet());
        common.retainAll(m2.getParticipantSet());
        if(common.size()>0){
             throw new IllegalArgumentException("The missions social circles are already interacting"); 
        }
        
        //the threshold value of the interactions of two social circles
        int thresholdValue = socialCircle1.size() * socialCircle2.size() / 2;

        //the total no of interactions between two social circles
        int totalInteraction = 0;
        // iterarte through the person of two social circles and finding a match
        for (Person p1 : socialCircle1) {
            int matchNo = 0;
            for (Person p2 : socialCircle2) {
                Set commonMissions = new HashSet(p1.getMissionsRegistered());
                commonMissions.retainAll(p2.getMissionsRegistered());
                if (!commonMissions.isEmpty()) {
                    matchNo++;
                    powerBrokers.add(p1);
                    powerBrokers.add(p2);
                }
            }
            totalInteraction += matchNo;
        }
        // if the total number of interactions is non-zero and less than the threshold
        if (totalInteraction < thresholdValue && totalInteraction > 0) {
            return powerBrokers;
        } else { // return empty set of power brokers
            powerBrokers = Collections.emptySet();
            return powerBrokers;
        }
    }
    
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
    @Override
    public Set<Person> getTheMostPowerfulBrokersOfAllSocialCircles() {
        //to store the two most non-interacting missions
        List<Mission> twoMostNonInteracting = new ArrayList<Mission>();
        //loading all the exixsting missions by calling the core layer
        List<Mission> allMissions = loader.loadAllMissions();
        //if  less that two missions found then return empty set
        if(allMissions.size()<2) {
            return Collections.emptySet();
        }
        //assume the first two missions are mostly non-interacting
        twoMostNonInteracting.add(allMissions.get(0));
        twoMostNonInteracting.add(allMissions.get(1));
        //check for each pair of misssions and find the interactions
        Set<Person> socialCircleIntersection = new HashSet<Person>();
        for(Mission m1 : allMissions) {
            for (Mission m2 : allMissions) {
                if (!m1.equals(m2)) {
                    socialCircleIntersection.addAll(m1.getParticipantSet());
                    socialCircleIntersection.retainAll(m2.getParticipantSet());
                    //if intersection is empty then they are non-interacting
                    if (socialCircleIntersection.isEmpty()) {
                        if (twoMostNonInteracting.get(0).getParticipantSet().size() < m1.getParticipantSet().size() && twoMostNonInteracting.get(1).getParticipantSet().size() < m2.getParticipantSet().size()) {
                            //if participant size of these missions are bigger that the one stored before
                            //then set these two missions as the most non-interacting
                            twoMostNonInteracting.clear();
                            twoMostNonInteracting.add(m1);
                            twoMostNonInteracting.add(m2);
                        }
                        if (twoMostNonInteracting.get(1).getParticipantSet().size() < m1.getParticipantSet().size() && twoMostNonInteracting.get(0).getParticipantSet().size() < m2.getParticipantSet().size()) {
                            //if participant size of these missions are bigger that the one stored before
                            //then set these two missions as the most non-interacting
                            twoMostNonInteracting.clear();
                            twoMostNonInteracting.add(m1);
                            twoMostNonInteracting.add(m2);
                        }
                    }
                }
            }
        }
        //get the most powerful brokers by invoking the other method for these two missions
        return this.getPowerBrokersOfTwoMissionSocialCircles(twoMostNonInteracting.get(0), twoMostNonInteracting.get(1));
    }
    
    /**
     * Retrieves a set of persons from database who are not a friend of
     * a given person upto (and including) a specified level of connections
     * 
     * @param person The person input 
     * @param k The specified level of connections
     * @param m The number of such new frontiers to be returned
     * @return A set of m persons that this person is not a friend with up to k connections
     */
    @Override
    public Set<Person> mineNewFroniters(Person person, int k, int m) {
        if(person==null){
           throw new NullArgumentException("Input person whose new fronters are searched cannot be null"); 
        }
        if(k<=0){
            throw new IllegalArgumentException("The number of connection levels must be non zero");
        }
        if(m<=0){
            throw new IllegalArgumentException("The number of new frontiers to be retrieved must be non zero");
        }

        Set<Person> newFrontiers = new HashSet<Person>();
        
        // find the list of all the existing persons in the database via core
        List<Person> persons = loader.loadAllPersons();
        //if at least 2 people exist
        if(persons.size()<=2) {
            return newFrontiers;
        }
        // all the persons that can be found within k level of connections
        Set<Person> friendsWithKConnections = new HashSet<Person>();
        
        // find the input person's friends. i.e. level of connection is 1
        Set<Person> friendsLevel1 = this.mineColleagues(person);
        
        // add first evel friends to friendsWithKConnections
        friendsWithKConnections.addAll(friendsLevel1);
        // frinds found in the intermediate levels
        Set<Person> currentLevelFriends;
        // 1st level has already been mined, now mine the remaining k-1 levels of friends
        for (int i = 1; i <= k - 1; i++) {
            currentLevelFriends = Collections.emptySet();
            for (Person p : friendsWithKConnections) {
                // get the friends of friends at the current level
                currentLevelFriends = this.mineColleagues(p);        
            }
            friendsWithKConnections.addAll(currentLevelFriends);
        }
        //create a set of all existing persons who are not friends up to k-level
        // person iteslf is not a friend
        persons.remove(person);
        // remove the set of friends upto k-level friends just mined
        persons.removeAll(friendsWithKConnections);
        
        List<Person>  nonFriends = persons;
        int c;
        // if the number of new froniters is more than it was intended for
        // then only return the first m person of that set,  otherwise retun what ever found
        if (m <= nonFriends.size()) {
            c = m;
        } else {
            c = nonFriends.size();
        }
        for (int i = 0; i < c; i++) {
            newFrontiers.add(nonFriends.get(i));
        }
        return newFrontiers;
    }

}
