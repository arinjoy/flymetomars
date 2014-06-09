package flymetomars.common.validation;

import flymetomars.common.NumberNegativeException;
import flymetomars.common.NumberTooLargeException;
import flymetomars.common.NumberTooSmallException;

/**
 * Utility class of static validation method pertaining to numerical validations
 * 
 * @author Arinjoy Biswas
 * @author Lawrence Colman
 */
public final class NumberValidator {
    
    private NumberValidator() {}
    
    /**
     * Validates that an integer is zero or greater
     * 
     * @param pos The integer to validate as being positive or zero
     * @throws NumberNegativeException thrown when pos is negative
     */
    public static void validatePositiveInteger(int pos) {
        if(pos<0) {
            throw new NumberNegativeException(pos, "Integer value must be greater than or equal to 0");
        }
    }
    
    /**
     * Validates that a long integer is zero or greater
     * 
     * @param pos The long to validate as being positive or zero
     * @throws NumberNegativeException thrown when pos is negative
     */
    public static void validatePositiveLong(long pos) {
        if(pos<0) {
            throw new NumberNegativeException(pos, "Long value must be greater than or equal to 0");
        }
    }
    
    /**
     * Validates that a floating point number is zero or more
     * 
     * @param pos The float to validate as being positive or zero
     * @throws NumberNegativeException thrown when pos is negative
     */
    public static void validatePositiveFloat(float pos) {
        if(pos<0) {
            throw new NumberNegativeException(new Double(pos), "Float value must be greater than or equal to 0");
        }
    }
    
    /**
     * Validates that a double precision floating point number is zero or more
     * 
     * @param pos The double to validate as being positive or zero
     * @throws NumberNegativeException thrown when pos is negative
     */
    public static void validatePositiveDouble(double pos) {
        if(pos<0) {
            throw new NumberNegativeException(pos, "Double value must be greater than or equal to 0");
        }
    }
    
    /**
     * Validates a number (number) as being within a specified numerical
     * range (bounded by minimum and maximum, naturally).  Bounds are inclusive.
     * 
     * @param number the number to validate against the range
     * @param minimum the lower bound of the permissible range (inclusive)
     * @param maximum the upper bound of the permissible range (inclusive)
     * @throws NumberTooSmallException thrown when number is less than minimum
     * @throws NumberTooLargeException thrown when number is more than maximum
     */
    public static void validateIntegerInRange(int number, int minimum, int maximum) {
        if(number<minimum) {
            throw new NumberTooSmallException(minimum, number, "given integer not within specified range");
        }
        if(number>maximum) {
            throw new NumberTooLargeException(maximum, number, "given integer not within specified range");
        }
    }
    
}
