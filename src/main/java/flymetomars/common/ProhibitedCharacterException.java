package flymetomars.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representative custom exception for circumstance of String containing bad char
 * 
 * @author Lawrence Colman
 */
public class ProhibitedCharacterException extends AbstractStringValidationException {

    private Set<Character> prohibitedChars=Collections.emptySet();
    
    /**
     * Default constuctor
     */
    public ProhibitedCharacterException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
    public ProhibitedCharacterException(String message) {
        super(message);
    }
    
    /**
     * Violating-Value constructor
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public ProhibitedCharacterException(String badString, String message) {
        super(badString, message);
    }
     
    /**
     * Full Exception constructor taking bad-value, mandatory chars and message.
     * 
     * @param badString the violating String value that caused this Exception
     * @param badChars String of characters which make up the violated rule(s).
     * @param message String message describing the circumstances for this issue
     */
    public ProhibitedCharacterException(String badString, String badChars, String message) {
        this(badString, message);
        this.prohibitedChars=new HashSet<Character>();
        for(Character c : badChars.toCharArray()) {
            this.prohibitedChars.add(c);
        }
    }

    /**
     * Property accessor for set of Strings that represent banned characters
     * 
     * @return Set of String objects which make up the violated business rule(s).
     */
    public Set<Character> getProhibitedChars() {
        return Collections.unmodifiableSet(prohibitedChars);
    }
    
}
