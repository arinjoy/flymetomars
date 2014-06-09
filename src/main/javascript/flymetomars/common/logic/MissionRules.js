/*
* Mission Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==StringValidator) { eval("1/0"); }

var MissionRules = {

    validateName : function(name) {
        var result=StringValidator.validateStringLengthInRange(name, 5, 50);
        if(result[0]!=0) {
            result[1]="Mission name must be between five and fifty characters!";
            return result;
        }
        var ok=StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS;
        result=StringValidator.validateStringContainsOnly(name, " '_.,-()!"+ok);
        if(result[0]!=0) {
            result[1]="Illegal character detected in Mission name input value, ";
            result[1]+="please ensure that you only enter: letters, numbers, ";
            result[1]+="apostrophees, underscores, periods, commas, dashes, ";
            result[1]+="brackets and spaces. Thank you for your co-operation.";
            return result;
        }
        return new Array(0,"OK");
    },
    
    validateDescription : function(desc) {
        var result=StringValidator.validateStringLengthInRange(desc, 25, 300);
        if(result[0]!=0) {
            result[1]="Mission description must be at least twenty-five ";
            result[1]+="and at most three-hundred characters. Thank you.";
            return result;
        }
        var blackbox="\u0000<>";
        result=StringValidator.validateStringContainsNo(desc, blackbox);
        if(result[0]!=0) {
            result[1]="The text entered for Mission description appears to ";
            result[1]+="contain a prohibbitted character.  Please ensure no ";
            result[1]+="HTML <tags> appear anywhere within the description.";
            return result;
        }
        return new Array(0,"OK");
    }

}