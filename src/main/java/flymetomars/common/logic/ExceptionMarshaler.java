package flymetomars.common.logic;

import flymetomars.common.CharactersMissingException;
import flymetomars.common.DateInFutureException;
import flymetomars.common.DateInPastException;
import flymetomars.common.DateOutOfSequenceException;
import flymetomars.common.FMTMValidationException;
import flymetomars.common.InvalidEmailException;
import flymetomars.common.InvalidPasswordException;
import flymetomars.common.NullArgumentException;
import flymetomars.common.NumberNegativeException;
import flymetomars.common.NumberTooLargeException;
import flymetomars.common.NumberTooSmallException;
import flymetomars.common.ProhibitedCharacterException;
import flymetomars.common.StringTooLongException;
import flymetomars.common.StringTooShortException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.NativeArray;

/**
 * Type-safe Exception instance marshaling class for Java to JavaScript (i.e.
 * Mozilla Rhino engine) language bridge - instantiates Exceptions from serials
 * 
 * @author Lawrence Colman
 */
public final class ExceptionMarshaler {

    private ExceptionMarshaler() {} //prevents external instantiation

    private static final Map<String,Class<? extends RuntimeException>> CODEX=new HashMap<String,Class<? extends RuntimeException>>();
    static {
        CODEX.put("-5365630128856068164", IllegalArgumentException.class);
        CODEX.put("-1", NullArgumentException.class);
        CODEX.put("1", FMTMValidationException.class);
        //codex.put("11", AbstractStringValidationException.class);
        CODEX.put("111", InvalidPasswordException.class);
        CODEX.put("112", InvalidEmailException.class);
        CODEX.put("113", StringTooShortException.class);
        CODEX.put("114", StringTooLongException.class);
        CODEX.put("115", ProhibitedCharacterException.class);
        CODEX.put("116", CharactersMissingException.class);
        //codex.put("12", AbstractNumberValidationException.class);
        CODEX.put("121", NumberNegativeException.class);
        CODEX.put("122", NumberTooLargeException.class);
        CODEX.put("123", NumberTooSmallException.class);
        //codex.put("13", AbstractDateValidationException.class);
        CODEX.put("131", DateInPastException.class);
        CODEX.put("132", DateInFutureException.class);
        CODEX.put("133", DateOutOfSequenceException.class);
    }
    
    /**
     * Intermediary function used by the unmarshal method for looking up the 
     * serialised identifier codes of the various JavaScript exceptions that are
     * passed in to the method.  Can be useful for getting the correct runtime
     * type of the object that is to be returned by a subsequent call to the
     * unmarshal method - as these instances will be of the "Class" returned.
     * 
     * @param sid
     * @return 
     */
    public static Class<? extends RuntimeException> retreive(Long sid) {
        return CODEX.get(sid.toString());
    }
    
    /**
     * 
     * 
     * @param <E>
     * @param noa
     * @return RuntimeException sub-type instance if appropriate, or null if not
     * @throws NullPointerException thrown when supplied object is null, or the
     *          first object contained within the object as an array is null
     * @throws NullArgumentException thrown when given exception marshaling id
     *          does not represent a known exception so backing map returns null
     * @throws ClassCastException thrown when supplied object is not an Apache
     *          Rhino `NativeArray' JavaScript Array Object.
     * @throws IllegalArgumentException thrown when the supplied object is an
     *          Apache Rhino `NativeArray' Object - but is of the wrong length,
     *          or contains two objects other than a Number (double) and String
     * @throws UnsupportedOperationException thrown when invocation of exception
     *          itself throws an Exception to be handled higher up
     * @throws IllegalStateException thrown when internally mapped exception is
     *          incorrect in some fashion.  Such as not having a constructor
     *          that only takes a message, being abstract, being a private class
     *          or having a private constructor, or not being decendant from
     *          RuntimeException as per the type-contract of this method.
     */
    public static <E extends RuntimeException> E unmarshal(Object noa) {
        NativeArray a=(NativeArray)noa;  //throws ClassCastException
        eif(2!=a.size(),
            new IllegalArgumentException(
                "Supplied array is of incorrect size; ".concat(
                    Integer.toString(a.size())
                )
            )
        );
        E result;
        try {
            Long seid;
            try {
                Number eid=(Number)a.get(0);  //throws ClassCastException
                seid=eid.longValue();  //throws NullPointerException
            } catch(ClassCastException cce) {
                throw new IllegalArgumentException(
                    "Problem in unmarshaling exception identifier", cce
                );
            }
            if(seid==0) { return null; }  //No Exception to Lookup and Construct
            Class<E> ex;
            try {
                ex=(Class<E>)retreive(seid);
            } catch(ClassCastException cce) {
                throw new IllegalStateException("Incorrect Exception Type", cce);
            }
            eif((null==ex),
                new NullArgumentException("Unable to unmarshal null exception")
            );
            Constructor<E> con;
            try {
                con=ex.getConstructor(String.class);
            } catch(NoSuchMethodException nsme) {
                throw new IllegalStateException("Unable to invoke exception contructor", nsme);
            }
            CharSequence msg;
            try {
                msg=(CharSequence)a.get(1);  //throws ClassCastException
            } catch(ClassCastException cce) {
                throw new IllegalArgumentException("Argument not valid String", cce);
            }
            StringBuilder str=new StringBuilder(msg);
            result=(E)con.newInstance(str.toString());  //throws ClassCastException
        } catch(ClassCastException cce) {
            throw new IllegalStateException("Internal Exception Type Mismatch", cce);
        } catch(InvocationTargetException ite) {
            throw new UnsupportedOperationException(ite.getTargetException().getMessage(),ite);
        } catch (IllegalAccessException iae) {
            throw new IllegalStateException("Exception constructor not accessible", iae);
        } catch(InstantiationException ie) {
            throw new IllegalStateException("Unable to instanciate exception", ie);
        }
        return result;
    } private static void eif(boolean condition, RuntimeException e) {
        if(condition) { throw e; }
    }
    
}
