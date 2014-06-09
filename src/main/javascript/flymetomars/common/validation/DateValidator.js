/*
Utility class containing several methods for validating Date information
Written by Lawrence Colman, with code contributions from Arinjoy Biswas.
*/
var DateValidator = {
    
    /*
    * Validates that the given date is not a null reference
    * 
    * @param date the Date instance that must not be null
    * @throws NullArgumentException thrown when date is null
    */
    validateDateNotNull : function(date) {
        if(typeof(this)!=typeof(date)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(null == date) {
            return new Array(-1, "Date field must not be null");
        }
        return new Array(0,"");
    },
    
    
   /*
   * Validates that future is greater than now
   * 
   * @param future the date object to be checked
   * @throws DateInPastException thrown to indicate future is in the past
   * @throws NullArgumentException thrown when `future' date is null
   */
    validateFutureDate : function(future) {
        var nullCheck=this.validateDateNotNull(future);
        if(nullCheck[0]<0) { return nullCheck; }
        var now = new Date();
        if(future <= now) {
            return new Array(131, "Date must be later than now");
        }
        return new Array(0, "");
    },
    
   /*
   * Validates that past is before the current date
   * 
   * @param past the date object to be checked
   * @throws DateInFutureException thrown to indicate past is in the future
   * @throws NullArgumentException thrown when `past' date is null
   */
    validatePastDate : function(past) {
        var nullCheck=this.validateDateNotNull(past);
        if(nullCheck[0]!=0) { return nullCheck; }
        var now = new Date();
        if(past > now) {
            return new Array(132, "Date must be before present");
        }
        return new Array(0, "");
    },
    
    
   /*
   * Validates that a past Date (before) is before another date (after)
   * 
   * @param before the Date object to be verified as being before after
   * @param after the Date object to be verified as being after before
   * @throws DateOutOfSequenceException thrown to indicate before is ahead of after
   * @throws NullArgumentException thrown when either date argument is null
   */
    validateDateSequence : function(before, after) {
        var nullCheck=this.validateDateNotNull(before);
        if(nullCheck[0]<0) { return nullCheck; }
        nullCheck=this.validateDateNotNull(after);
        if(nullCheck[0]<0) { return nullCheck; }
        if(before > after) {
            return new Array(133, "supplied after Date is before previous Date");
        }
        return new Array(0, "");
    },
    
    /*
    * Validates that a Date (when) is in between before and after Dates
    * 
    * @param when the date instance to be validated as being within the range
    * @param before the Date object representing the lower bound
    * @param after the Date object representing the upper bound
    * @throws DateOutOfSequenceException thrown to indicate when is not in range
    * @throws NullArgumentException thrown when any date argument is null
    */
    validateDateInRange : function(when, before, after) {
        var nullCheck=this.validateDateNotNull(when);
        if(nullCheck[0]<0) { return nullCheck; }
        nullCheck=this.validateDateNotNull(before);
        if(nullCheck[0]<0) { return nullCheck; }
        nullCheck=this.validateDateNotNull(after);
        if(nullCheck[0]<0) { return nullCheck; }
        var seq=this.validateDateSequence(before,after);
        if(seq[0]!=0) { return seq; }
        seq=this.validateDateSequence(before,when);
        if(seq[0]!=0) { return seq; }
        seq=this.validateDateSequence(when,after);
        if(seq[0]!=0) { return seq; }
        return new Array(0, "");
    }
    
}
