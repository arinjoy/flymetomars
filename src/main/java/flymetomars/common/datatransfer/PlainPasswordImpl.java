package flymetomars.common.datatransfer;

import flymetomars.common.NullArgumentException;

/**
 * A convenience class for representing the un-digested SaltedPassword of Person
 * 
 * @author Lawrence Colman
 * 
 * JavaDoc copied from interface
 */
public class PlainPasswordImpl implements PlainPassword {
    
    private String passwordText="";
    
    /**
     * Property mutator for the insecure (non-hash-digested) password (or
     * password attempt) text value being supplied for transference via this DTO
     * 
     * @param textPass The unencoded String of password characters  - no hashing
     * @throws NullArgumentException thrown when `textPass` is given as null ref
     */
    @Override
    public void setPasswordText(String textPass) {
        if(null==textPass) {
            throw new NullArgumentException("Password text cannot be null");
        }
        this.passwordText=textPass;
    }
    
    /**
     * Property accessor for the insecure (non-hash-digested) password (or
     * password attempt) text value supplied by user/client
     * 
     * @return String of unencoded, unhashed insecurely transferred password text
     */
    @Override
    public String getPasswordText() {
        return this.passwordText;
    }
    
}
