package flymetomars.common.validation;

import flymetomars.common.CharactersMissingException;
import flymetomars.common.InvalidEmailException;
import flymetomars.common.InvalidPasswordException;
import flymetomars.common.NullArgumentException;
import flymetomars.common.NumberTooLargeException;
import flymetomars.common.NumberTooSmallException;
import flymetomars.common.ProhibitedCharacterException;
import flymetomars.common.StringTooLongException;
import flymetomars.common.StringTooShortException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Utility class containing several methods for validating String information
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public final class StringValidator {
    
    private StringValidator() {}
    
    /**
     * Validates that input is not blank ("") or null.
     * 
     * @param input the string to be checked against
     * @throws NullArgumentException thrown when input is null
     * @throws StringTooShortException thrown when input is zero-length (empty)
     */
    public static void validateStringNotEmpty(String input) {
       if(null == input) {
           throw new NullArgumentException("String field cannot be null");
       }
       if(input.isEmpty()) {
            throw new StringTooShortException(input,0,"field cannot be empty");
       }
    }
    
    
    /**
     * Validates that a given string (str) has length within a particular range
     * 
     * @param str the string to check the length of
     * @param min minimum length for str
     * @param max maximum length for str
     * @throws StringTooShortException thrown when str is empty, or below min
     * @throws StringTooLongException thrown when the length of str is over max
     * @throws NullArgumentException thrown when str is null
     * @throws IllegalArgumentException thrown when min and max are out of synch
     * @throws NumberNegativeException thrown when either min or max go negative
     */
    public static void validateStringLengthInRange(String str, int min, int max) {
        if(null==str) {
            throw new NullArgumentException("field cannot be set to null");
        }
        NumberValidator.validatePositiveInteger(min);
        if(min>0) { validateStringNotEmpty(str); }
        NumberValidator.validatePositiveInteger(max);
        if(max < min) {
            throw new IllegalArgumentException("Min value must be less than Max value");
        }
        try {
            NumberValidator.validateIntegerInRange(str.length(),min,max);
        } catch(NumberTooSmallException nts) {
            throw new StringTooShortException(str,min,"String must be longer",nts);
        } catch(NumberTooLargeException ntl) {
            throw new StringTooLongException(str,max,"String must be shorter",ntl);
        }
    }
    
    /**
     * Validates that a given string (str) has at least minLength length 
     * 
     * @param str the string to check the length of
     * @param minLength minimum length for str
     * @throws StringTooShortException thrown when either str is null, or empty,
     *          or also thrown when str is below the specified minLength
     */
    public static void validateStringLengthIsAtLeast(String str, int minLength) {
        validateStringLengthInRange(str,minLength,Integer.MAX_VALUE);
    }
    
    /**
     * Validates that a given string (str) has at most maxLength length 
     * 
     * @param str the string to check the length of
     * @param maxLength maximum length for str
     * @throws StringTooLongException thrown when either str is null, or empty,
     * or also thrown when str is longer than the specified maxLength
     */
    public static void validateStringLengthIsAtMost(String str, int maxLength) {
        validateStringLengthInRange(str,0,maxLength);
    }

    
    /**
     * Helper constant defined to contain all lowercase letters of the standard
     * (western) 26-character latin alphabet.  Useful for constructing allowed
     * and denied parameters for passing into validateStringCharsContained.
     */
    public static final String LOWERCASE_LETTERS="abcdefghijklmnopqrstuvwxyz";
    
    /**
     * Helper constant defined to contain all uppercase letters of the standard
     * (western) 26-character latin alphabet.  Useful for constructing allowed
     * and denied parameters for passing into validateStringCharsContained.
     */
    public static final String UPPERCASE_LETTERS="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    /**
     * Helper constant defined to contain all upper and lower case letters of
     * the latin alphabet.  Handy for constructing allowed and denied parameters
     * for validateStringContainsOnly and validateStringContainsNo methods.
     */
    public static final String LATIN_ALPHABET=LOWERCASE_LETTERS+UPPERCASE_LETTERS;
    
    /**
     * Helper constant defined to contain all upper and lower case letters of
     * the extended 8-bit portion of the higher ASCII representation of the
     * extended latin alphabet.  Used in constructing allowed/denied parameters
     * for validateStringContainsNo and validateStringContainsOnly methods.
     */
    public static final String EXTENDED_LATIN="\u0094\u00a4\u00bd\u00be\u00c6\u00c7\u00d0\u00d2\u00d3\u00d4\u00d5\u00d7\u00de\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0093\u00d1\u00d6\u00d8\u00ed\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f\u00a0\u00a1\u00a2";
    
    /**
     * Validates that a given string (text) only contains characters from an
     * allowed set of characters specified as a String.
     * 
     * Performs a case sensitive  comparison by default, beware mixed case.
     * Skips validation, without reporting error, when text is a null reference.
     * 
     * @param text the String to be validated for permitted and prohibited chars
     * @param allowed a String of all of the allowable characters for text.
     * @throws CharactersMissingException thrown when text contains chars not in allowed
     * @throws NullArgumentException thrown only when allowed is null, text may be null
     */
    public static void validateStringContainsOnly(String text, String allowed) {
        if(null == text) { return; }
        if(null == allowed) {
            throw new NullArgumentException("allowed string cannot be null ref");
        }
        for(char c : text.toCharArray()) {
            if(!allowed.contains(Character.toString(c))) {
                StringBuilder msg=new StringBuilder("Disallowed character detected: ");
                msg.append(c);
                throw new CharactersMissingException(text, allowed, msg.toString());
            }
        }
    }
    
    /**
     * Validates that a given string (text) contains no denied characters.
     * Performs a case sensitive  comparison by default, beware mixed case.
     * Skips validation, without reporting error, when text is a null reference.
     * 
     * @param text the String to be validated for permitted and prohibited chars
     * @param denied a String of all of the denied characters for text.
     * @throws ProhibitedCharacterException thrown when text contains chars in denied
     * @throws NullArgumentException thrown only when denied is null, text may be null
     */
    public static void validateStringContainsNo(String text, String denied) {
        if(null == text) { return; }
        if(null == denied) {
            throw new NullArgumentException("denied string cannot be null ref");
        }
        for(char c : denied.toCharArray()) {
            if(text.contains(Character.toString(c))) {
                StringBuilder msg=new StringBuilder("Prohibited character detected: ");
                msg.append(c);
                throw new ProhibitedCharacterException(text, denied, msg.toString());
            }
        }
    }
    
    /**
     * Validates that a given string (text) only contains only characters from
     * allowed set, and that text does not contain any character from denied set.
     * Performs a case sensitive  comparison by default, beware mixed case.
     * Skips validation, without reporting error, when text is an empty string.
     * Takes exception when text is a null reference, or allowed or denied are null.
     * 
     * @param text the String to be validated for permitted and prohibited chars
     * @param allowed a String of all of the allowable characters for text.
     * @param denied a String of all of the denied characters for text.
     * @throws ProhibitedCharacterException thrown when text contains characters from denied set
     * @throws CharactersMissingException thrown when text contains one or more characters not in allowed set
     * @throws NullArgumentException thrown when any of the arguments if null.
     */
    public static void validateStringCharsContained(String text, String allowed, String denied) {
        if(null!=text && text.isEmpty()) { return; }
        validateStringNotEmpty(text);
        validateStringContainsOnly(text,allowed);
        validateStringContainsNo(text,denied);
    }
    
    
    /**
     * Validates that a given string (item) contains a minimum amount (minCount)
     * of a certain collection of characters (chars).
     * 
     * @param item the string to check
     * @param minCount the number required
     * @param chars the characters that meet the requirement
     * @throws CharactersMissingException thrown when item does not contain at
     *          least minCount number of characters from chars.
     * @throws NullArgumentException thrown when chars is given as a null ref
     */
    public static void validateStringContainsMinimumChars(String item, int minimumCount, String chars) {
        int minCount=minimumCount;  //"Parameter Assignment" issue in sonar - resolved
        validateStringNotEmpty(item);
        if(null==chars) {
            throw new NullArgumentException("required character set cannot be null");
        }
        for(char i : item.toCharArray()) {
            if(chars.contains(Character.toString(i))) { minCount-=1; }
        }
        if(minCount>0) {
            throw new CharactersMissingException(item, chars,
                "field does not contain enough specific characters"
            );
        }
    }
    
    
    /**
     * Helper constant defined to contain all ten Hindu-Arabic numeric digits.
     * Useful for constructing allowed and denied parameters
     * for validateStringContainsOnly and validateStringContainsNo methods.
     */
    public static final String NUMBERS="0123456789";
    
    /**
     * Private class fields used within the validatePasswordAgainstConstraints
     * method.  Values of NEWLINES and WHITESPACE are used for the whitespace
     * validation internal to the method (see validatePasswordAgainstConstraints)
     * and SYMBOLS is used for the symbol validation.
     */
    private static final String NEWLINES="\r\n";
    private static final String WHITESPACE=" \t";
    private static final String SYMBOLS="!@#$%^&*";  //formerly included: ~+=\\|/[]{}()'\"<>
    
    /**
     * Validates that passwd is at least length characters long, has at least
     * upper Uppercase letters, and at least numbers number characters, with at
     * least symbols symbol characters, and checks for spaces according to whitespace.
     * Passwd values that are equal to an empty string, or are empty according
     * to the isEmpty method will cause an IllegalArgumentException regardless
     * of the value given in the length parameter.  Negative length parameter
     * values are simply ignored, as are lengths equal to zero.
     * Will not validate a passwd string that contains any newline characters,
     * regardless of the value of whitespace - which concerns in-line space only.
     * If whitespace is set to true, then whitespace characters (tabs, spaces)
     * are allowed, otherwise they are prohibited and will cause exception.
     *
     * @param passwd the paintext password string to validate against
     * @param length minimum length that password must be in order to validate
     * @param upper minimum count of uppercase letters to be present in passwd
     * @param lower minimum count of lowercase letters to be present in passwd
     * @param letter minimum count of all letters which must be present in passwd
     * @param numbers minimum count of numeric characters needed in passwd
     * @param symbols minimum count of symbolic characters required in passwd
     * @param whitespace flag indicating whether spaces,tabs,etc are allowed.
     * @throws InvalidPasswordException thrown when passwd does not meet conditions
     * @throws NullArgumentException thrown when passwd parameter is passed as null
     * @throws StringTooShortException thrown when passwd is shorter than length
     */
    public static void validatePasswordAgainstConstraints(String passwd, int length, int upper, int lower, int letter, int numbers, int symbols, boolean whitespace) {
        if(null == passwd) {  //we don't accept null as a valid password
            throw new NullArgumentException("password field cannot be null");
        }
        if(length>0) {  //we have a minimum length to validate
            validateStringLengthIsAtLeast(passwd,length);
        } else {  //otherwise just ensure not empty
            validateStringNotEmpty(passwd);
        }
        try {
            //uppercase letters:
            validateStringContainsMinimumChars(passwd,upper,UPPERCASE_LETTERS);
            //lowercase letters:
            validateStringContainsMinimumChars(passwd,upper,LOWERCASE_LETTERS);
            //any case letters:
            validateStringContainsMinimumChars(passwd,letter,UPPERCASE_LETTERS+LOWERCASE_LETTERS);
            //numeric characters:
            validateStringContainsMinimumChars(passwd,numbers,NUMBERS);
            //symbolic characters:
            validateStringContainsMinimumChars(passwd,symbols,SYMBOLS);
            //whitespace (and newlines):
            validateStringContainsNo(passwd,NEWLINES);
            if(!whitespace) {  //in-line whitespace not permitted
                validateStringContainsNo(passwd,WHITESPACE);
            }
        } catch(CharactersMissingException ex) {
            throw new InvalidPasswordException(passwd,
                "Invalid Password: needs more character diversity", ex
            );
        } catch(ProhibitedCharacterException ex) {
            throw new InvalidPasswordException(passwd,
                "Invalid Password: contains characters not permissable for password"
            ,ex);
        }
    }
    

    /**
     * Private class field used within the validateEmail method.
     * This regular expression is used to determine validity of E-mail addresses.
     */
    private static final String EMAIL_REGEX="^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
    
    /**
     * Validate that an E-mail address (email) follows format conventions
     * @param email the E-mail address to validate against
     * @throws InvalidEmailException thrown when email is invalid
     * @throws NullArgumentException thrown if email is supplied as null
     */
    public static void validateEmail(String email) {
        if(null==email) {  //will not validate a null E-mail
            throw new NullArgumentException("E-mail field cannot be null");
        }
        // Compile the refular expression into a pattern:
        Pattern p = Pattern.compile(EMAIL_REGEX);
        // Match the given string with the pattern:
        Matcher m = p.matcher(email);
        if(!m.matches()) { //Invalid!
            throw new InvalidEmailException(email, "E-mail address is not valid");
        }
    }
    
}
