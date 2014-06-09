/*
* Location Basic Business Rules Application - JavaScript Edition
* 
* As a JavaScript language member of the logic package - this file implements
* basic business rules logic against a specific domain object model type.
* 
* @author Lawrence Colman
*/

if(null==StringValidator) { eval("1/0"); }

var LocationRules = {
    
    validateFloor : function(floor) {
        var result=StringValidator.validateStringLengthInRange(floor, 1, 12);
        if(result[0]!=0) {
            result[1]="Location \"Floor\" component (if specified) must be ";
            result[1]+="between one and twelve characters in length. Thanks.";
            return result;
        }
        var permissible=StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS + " -.:";
        result=StringValidator.validateStringContainsOnly(floor, permissible);
        if(result[0]!=0) {
            msg="Please ensure Location \"Floor\" value contains only letters, ";
            msg+="numbers, spaces, dashes, dots, and colons. Thank you.";
            result[1]=msg;
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateStreetNo : function(streetNo) {
        var result=StringValidator.validateStringLengthInRange(streetNo, 1, 10);
        if(result[0]!=0) {
            result[1]="Location street number field must can be at most ten characters";
            return result;
        }
        var ok=StringValidator.LATIN_ALPHABET + StringValidator.EXTENDED_LATIN;
        ok+=StringValidator.NUMBERS + " -./"
        result=StringValidator.validateStringContainsOnly(streetNo, ok);
        if(result[0]!=0) {
            result[1]="Location street number field can only contain the follow";
            result[1]+="ing characters: \""+ok+"\" (excluding quote symbols)."
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateStreet : function(street) {
        var result=StringValidator.validateStringLengthInRange(street, 5, 40);
        if(result[0]!=0) {
            result[1]="Location \"Street\" address value must be between five ";
            result[1]+="and fourty characters in length. Thank you.";
            return result;
        }
        var acceptable=StringValidator.LATIN_ALPHABET + " -.',";
        acceptable+=StringValidator.EXTENDED_LATIN + StringValidator.NUMBERS;
        result=StringValidator.validateStringContainsOnly(street, acceptable);
        if(result[0]!=0) {
            result[1]="Location Street field may only contain; letters, numbers,";
            result[1]+=" extended latin alphabet symbols, dots, dashes, spaces ";
            result[1]+="apostrophes, and commas.  All other characters disallowed.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateLandmark : function(landmark) {
        var result=StringValidator.validateStringLengthInRange(landmark, 1, 30);
        if(result[0]!=0) {
            result[1]="Landmark given for Location does not meet field length ";
            result[1]+="requirements; ensure value is at most thirty characters"
            return result;
        }
        var acceptable=StringValidator.LATIN_ALPHABET + StringValidator.NUMBERS;
        acceptable+=StringValidator.EXTENDED_LATIN + " -,."
        result=StringValidator.validateStringContainsOnly(landmark, acceptable);
        if(result[0]!=0) {
            result[1]="Landmark value input for Location contains characters that";
            result[1]+=" have been prohibited from use within a Landmark value.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateTown : function(town) {
        var result=StringValidator.validateStringLengthInRange(town, 2, 30);
        if(result[0]!=0) {
            result[1]="\"Town\" component of Location specification must be ";
            result[1]+="between two and thirty characters in length. Thank you";
            return result;
        }
        var okchars=StringValidator.LATIN_ALPHABET;
        okchars+=StringValidator.EXTENDED_LATIN + " -'."
        result=StringValidator.validateStringContainsOnly(town, okchars);
        if(result[0]!=0) {
            result[1]="Value given for the \"town\" component of Location is ";
            result[1]+="invalid as it contains banned characters. Permitted ";
            result[1]+="characters are: \""+okchars+"\" (without quotes).";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateRegion : function(region) {
        var result=StringValidator.validateStringLengthInRange(region, 2, 30);
        if(result[0]!=0) {
            result[1]="Location region must be between two and thirty characters";
            return result;
        }
        var permitted=StringValidator.LATIN_ALPHABET;
        permitted+=StringValidator.EXTENDED_LATIN + " -,'"
        result=StringValidator.validateStringContainsOnly(region, permitted);
        if(result[0]!=0) {
            result[1]="Entered value for Location region component contains inv";
            result[1]+="alid characters, permissible characters are letters, ext";
            result[1]+="ended latin alphabetical characters, spaces, dashes, ";
            result[1]+="commas, and appostrophees.  Thank you.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validatePostcode : function(pcode) {
        var result=StringValidator.validateStringLengthInRange(pcode, 1, 12);
        if(result[0]!=0) {
            result[1]="Postcode (if any) must be between one & twelve characters";
            return result;
        }
        var allowed=StringValidator.LATIN_ALPHABET+StringValidator.NUMBERS+"-";
        result=StringValidator.validateStringContainsOnly(pcode, allowed);
        if(result[0]!=0) {
            result[1]="Location \"Postcode\" component contains invalid charac";
            result[1]+="yers, only letters, numbers and hyphens are permitted.";
            return result;
        }
        return new Array(0, "OK");
    },
    
    validateCountry : function(country) {
        var result=StringValidator.validateStringLengthInRange(country, 2, 32);
        if(result[0]!=0) {
            result[1]="\"Country\" article of Location data must be between two";
            result[1]+=" and thirty-two characters in length. Thank you."
            return result;
        }
        var allowed=StringValidator.LATIN_ALPHABET;
        allowed+=StringValidator.EXTENDED_LATIN + " (-)";
        result=StringValidator.validateStringContainsOnly(country, allowed);
        if(result[0]!=0) {
            result[1]="Country entered for Location appears to contain some in";
            result[1]+="valid characters.  Please ensure only letters, extended";
            result[1]+=" latin alphabetical characters, spaces, braces, and hyp";
            result[1]+="ens are used for the country value.  Thank you";
            return result;
        }
        return new Array(0, "OK");
    }
    
}