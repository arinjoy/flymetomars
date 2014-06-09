package flymetomars.common.datatransfer;

/**
 * A convenience class for representing the un-digested SaltedPassword of Person
 * 
 * @author Lawrence Colman
 */
public interface PlainPassword extends Marshalable {

    /**
     * Property mutator for the insecure (non-hash-digested) password (or
     * password attempt) text value being supplied for transference via this DTO
     * 
     * @param textPass The unencoded String of password characters  - no hashing
     * @throws NullArgumentException thrown when `textPass` is given as null ref
     */
    void setPasswordText(String textPass);
    
    /**
     * Property accessor for the insecure (non-hash-digested) password (or
     * password attempt) text value supplied by user/client
     * 
     * @return String of unencoded, unhashed insecurely transferred password text
     */
    String getPasswordText();
    
}
