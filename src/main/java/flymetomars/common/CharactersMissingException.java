package flymetomars.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Custom exception representing circumstance of String missing required char
 * 
 * @author Lawrence Colman
 */
public class CharactersMissingException extends AbstractStringValidationException {

    private Set<Character> requiredChars=Collections.emptySet();
    
    /**
     * Default constuctor
     */
    public CharactersMissingException() {
        super();
    }
    
    /**
     * Message constuctor
     * 
     * @param message String message describing the circumstances for this issue
     */
     public CharactersMissingException(String message) {
         super(message);
     }
    
     /**
     * Violating-Value constructor
     * 
     * @param badString the violating String value that caused this Exception
     * @param message String message describing the circumstances for this issue
     */
    public CharactersMissingException(String badString, String message) {
        super(badString, message);
    }
     
    /**
     * Full Exception constructor taking bad-value, mandatory chars and message.
     * 
     * @param badString the violating String value that caused this Exception
     * @param reqChars String of characters which make up the violated rule(s).
     * @param message String message describing the circumstances for this issue
     */
     public CharactersMissingException(String badString, String reqChars, String message) {
         this(badString, message);
         this.requiredChars=new HashSet<Character>();
         for(Character c : reqChars.toCharArray()) {
             this.requiredChars.add(c);
         }
     }

     /**
     * Property accessor for set of Strings that represent mandatory characters
     * 
     * @return Set of String objects which make up the violated business rule(s).
     */
    public Set<Character> getRequiredChars() {
        return Collections.unmodifiableSet(requiredChars);
    }
    
}
