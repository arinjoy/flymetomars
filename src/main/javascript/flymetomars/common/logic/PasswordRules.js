/*
* Password Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==StringValidator) { eval("1/0"); }

var PasswordRules = {
    
    validatePasswordText : function(password) {
        var result=StringValidator.validateStringLengthInRange(password, 8, 20);
        if(result[0]!=0) {
            result[1]="Invalid Password: length must be at least eight and no ";
            result[1]+="more than twenty characters in order to be valid. Thank you."
            return result;
        }
        result=StringValidator.validatePasswordAgainstConstraints(password, 8, 0, 0, 1, 1, 1, false);
        /* What the password validation arguments mean
         * Minimum Length is 8,
         * Required Uppercase letters:  0,
         * Required Lowercase letters:  0,
         * Required letters (any case): 1,
         * Required Numbers: 1,
         * Required Symbols: 1,  symbols are: !@#$%^&*
         * Whitespace Allowed: false.
         */
        if(result[0]>0 && result[1].indexOf("Invalid Password: ")<0) {
            result[1]="Invalid Password: Password must contain at least one alph";
            result[1]+="abetical character, one numeric character, and one symbo";
            result[1]+="l.  Password must not contain spaces. Valid Symbols are:";
            result[1]+="!@#$%^&*";
            return result;
        } else if(result[0]<0) { return result; }
        return new Array(0, "OK");
    }
    
}