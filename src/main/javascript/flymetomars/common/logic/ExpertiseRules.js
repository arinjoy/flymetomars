/*
* Expertise Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==StringValidator) { eval("1/0"); }

var ExpertiseRules = {

    validateName : function(name) {
        var result=StringValidator.validateStringLengthInRange(name, 5, 20);
        if(result[0]!=0) {
            result[1]="Expertise name must be between five and twenty characters";
            return result;
        }
        var accept=StringValidator.LATIN_ALPHABET + " '.,-()";
        result=StringValidator.validateStringContainsOnly(name, accept);
        if(result[0]!=0) {
            result[1]="Illegal character detected in Expertise name, please ";
            result[1]+="ensure that only the following characters are included:"
            result[1]+=accept;
            return result;
        }
        return new Array(0,"OK");
    },
    
    validateDescription : function(description) {
        var result=StringValidator.validateStringLengthInRange(description, 20, 120);
        if(result[0]!=0) {
            result[1]="Expertise Description is required to be at least twenty ";
            result[1]+="and at most one hunderd and twenty characters in length.";
            return result;
        }
        var blackbox="\u0000<>";
        result=StringValidator.validateStringContainsNo(description, blackbox);
        if(result[0]!=0) {
            result[1]="The text entered for Expertise description appears to ";
            result[1]+="contain a prohibbitted character.  Please ensure no ";
            result[1]+="HTML <tags> appear anywhere within the description.";
            return result;
        }
        return new Array(0,"OK");
    }
    
}