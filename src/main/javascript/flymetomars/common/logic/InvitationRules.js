/*
* Invitation Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==DateValidator) { eval("1/0"); }

var InvitationRules = {
    
    validateLastUpdated : function(lastUpdated) {
        var result=DateValidator.validatePastDate(new Date(lastUpdated));
        if(result[0]!=0) {
            result[1]="Invitation Last Updated: "+result[1];
            return result;
        }
        return new Array(0, "OK");
    }
    
}