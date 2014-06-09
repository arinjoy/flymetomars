/*
* Person Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==StringValidator) { eval("1/0"); }

var PersonRules = {
    
    validateFirstName : function(firstName){
        var result=StringValidator.validateStringLengthInRange(firstName, 2, 25);
        if(result[0]!=0) {
            result[1]="First name of a Person cannot be empty, and must be between"
            result[1]+=" two and twenty-five characters in length.  Thank you.";
            return result;
        }
        var ok=StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN+"-";
        result=StringValidator.validateStringContainsOnly(firstName, ok);
        if(result[0]!=0) {
            result[1]="Illegal characters present!  Permitted characters for the";
            result[1]+=" first name of a Person are upper and lower case letters,";
            result[1]+=" hyphen symbols and extended latin alphabet characters.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateLastName : function(lastName){
        var result=StringValidator.validateStringLengthInRange(lastName, 2, 25);
        if(result[0]!=0) {
            result[1]="Last name of a Person cannot be empty, and must be between"
            result[1]+=" two and twenty-five characters in length.  Thank you.";
            return result;
        }
        var ok=StringValidator.LATIN_ALPHABET+StringValidator.EXTENDED_LATIN+" -";
        result=StringValidator.validateStringContainsOnly(lastName, ok);
        if(result[0]!=0) {
            result[1]="Illegal characters present!  Permitted characters for the";
            result[1]+=" surname of a Person are upper and lower case letters, ";
            result[1]+="hyphen symbols and extended latin alphabet characters.";
            return result;
        }
        ok=StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN;
        result=StringValidator.validateStringContainsMinimumChars(lastName, 2, ok);
        if(result[0]!=0) {
            result[1]="Invalid surname specification detected!  Please ensure ";
            result[1]+="that at least two of the characters specified are from";
            result[1]+=" either the standard or extended latin alphabets. Thanks"
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateUserName : function(userName){
        var result=StringValidator.validateStringLengthInRange(userName, 6, 20);
        if(result[0]!=0) {
            result[1]="User name must be at least six and no more than twenty";
            result[1]+=" characters in length.  Thank you for your compliance.";
            return result;
        }
        var acceptable = StringValidator.LATIN_ALPHABET;
        acceptable += StringValidator.NUMBERS + "._";
        result=StringValidator.validateStringContainsOnly(userName, acceptable);
        if(result[0]!=0) {
            result[1]="Illegal characters present in user name!  Please ensure";
            result[1]+=" that only the following characters are present: "+acceptable;
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateEmail : function(email){
        var result=StringValidator.validateStringLengthInRange(email, 8, 80);
        if(result[0]!=0) {
            result[1]="Invalid E-mail address: length must be at least eight and";
            result[1]+="less than eighty characters to be valid. Thank you."
            return result;
        }
        result=StringValidator.validateEmail(email);
        if(result[0]!=0) {
            result[1]="Given E-mail address does not appear to be valid, please";
            result[1]+=" check the value entered and try again.  Thank you.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateExpertiseGained : function(expertiseGained) {
        var result=NumberValidator.validateIntegerInRange(expertiseGained.length,0,10);
        if(result[0]!=0) {
            result[1]="Person cannot have more than ten expertise";
            return result;
        }
        return new Array(0, "OK");
    }
    
}