package flymetomars.dataaccess;

import flymetomars.common.datatransfer.Person;

/**
 * 
 * @author Yuan-Fang Li
 * @author Lawrence Colman
 */

public interface PersonDAO extends EntityDAO<Person> {
    
    /**
     * Returns a single Person object given his email address
     * @param email is the String object of the email address
     * @return a Person object matching the email 
     */
    Person getPersonByEmail(String email);
    
    /**
     * Returns a single Person object given his user name
     * @param userName is the String object of the user name
     * @return a person object matching the user name
     */ 
    Person getPersonByUserName(String userName);   
}
