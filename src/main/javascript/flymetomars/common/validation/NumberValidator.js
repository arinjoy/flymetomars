/*
* Utility class of static validation method pertaining to numerical validations
* 
* @author Arinjoy Biswas
* @author Lawrence Colman
*/
var NumberValidator = {
    
    /*
    * Validates that an integer is zero or greater
    * 
    * @param pos The integer to validate as being positive or zero
    * @throws NumberNegativeException thrown when pos is negative
    */
    validatePositiveInteger : function(pos) {
        if(typeof(1)!=typeof(pos) || Math.round(pos)!=pos) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(pos<0) {
            return new Array(121, "Integer value must be greater than or equal to 0");
        }
        return new Array(0,"");
    },
    
    /*
    * Validates that a long integer is zero or greater
    * 
    * @param pos The long to validate as being positive or zero
    * @throws NumberNegativeException thrown when pos is negative
    */
    validatePositiveLong : function(pos) {
        var result = this.validatePositiveInteger(pos);
        if(result[0]==121) {
            result[1]="Long value must be greater than or equal to 0";
        }
        return result;
    },
    
    /*
    * Validates that a floating point number is zero or more
    * 
    * @param pos The float to validate as being positive or zero
    * @throws NumberNegativeException thrown when pos is negative
    */
    validatePositiveFloat : function(pos) {
        if(typeof(1.0)!=typeof(pos)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(pos<0) {
            return new Array(121, "Float value must be greater than or equal to 0");
        }
        return new Array(0,"");
    },
    
    /*
    * Validates that a double precision floating point number is zero or more
    * 
    * @param pos The double to validate as being positive or zero
    * @throws NumberNegativeException thrown when pos is negative
    */
    validatePositiveDouble : function(pos){
        result=this.validatePositiveFloat(pos);
        if(pos[0]==121) {
            result[1]="Double value must be greater than or equal to 0";
        }
        return result;
    },
    
    
    /*
    * Validates a number (number) as being within a specified numerical
    * range (bounded by minimum and maximum, naturally).  Bounds are inclusive.
    * 
    * @param number the number to validate against the range
    * @param minimum the lower bound of the permissible range (inclusive)
    * @param maximum the upper bound of the permissible range (inclusive)
    * @throws NumberTooSmallException thrown when number is less than minimum
    * @throws NumberTooLargeException thrown when number is more than maximum
    */
    validateIntegerInRange : function(number, minimum, maximum) {
        if(typeof(1)!=typeof(number) || typeof(1)!=typeof(minimum) || typeof(1)!=typeof(maximum)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(number<minimum) {
            return new Array(123, "given integer not within specified range");
        }
        if(number>maximum) {
            return new Array(122, "given integer not within specified range");
        }
        return new Array(0,"");
    }
    
}
