if(null!=NumberValidator) { eval("1/0"); }

/*
* Utility class containing several methods for validating String information
* 
* @author Arinjoy Biswas
* @author Lawrence Colman
*/
var StringValidator = {
   
    /*
    * Validates that input is not blank ("") or null.
    * 
    * @param input the string to be checked against
    * @throws NullArgumentException thrown when input is null
    * @throws StringTooShortException thrown when input is zero-length (empty)
    */
    validateStringNotEmpty : function(input) {
        if(typeof('')!=typeof(input)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
       if(null == input) {
           return new Array(-1, "String field cannot be null");
       }
       if(input=="") {
            return new Array(113,"field cannot be empty");
       }
       return new Array(0,"");
    },
    
    
    /*
    * Validates that a given string (str) has length within a particular range
    * 
    * @param str the string to check the length of
    * @param min minimum length for str
    * @param max maximum length for str
    * @throws StringTooShortException thrown when str is empty, or below min
    * @throws StringTooLongException thrown when the length of str is over max
    * @throws NullArgumentException thrown when str is null
    * @throws IllegalArgumentException thrown when min and max are out of synch
    * @throws NumberNegativeException thrown when either min or max go negative
    */
    validateStringLengthInRange : function(str,min,max) {
        if(typeof('')!=typeof(str) || typeof(1)!=typeof(min) || typeof(1)!=typeof(max)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(null==str) {
            return new Array(-1,"field cannot be set to null");
        }
        var result=NumberValidator.validatePositiveInteger(min);
        if(result[0]!=0) { return result; }
        if(min>0) {
            result=this.validateStringNotEmpty(str);
            if(result[0]!=0) { return result; }
        }
        result=NumberValidator.validatePositiveInteger(max);
        if(result[0]!=0) { return result; }
        if(max < min) {
            return new Array(-5365630128856068164, "Min value must be less than Max value");
        }
        result=NumberValidator.validateIntegerInRange(str.length,min,max);
        if(result[0]==123) {
            return new Array(113,"String must be longer");
        }
        if(result[0]==122) {
            return new Array(114,"String must be shorter");
        }
        return new Array(0,"");
    },
    
    /*
    * Validates that a given string (str) has at least minLength length 
    * 
    * @param str the string to check the length of
    * @param minLength minimum length for str
    * @throws StringTooShortException thrown when either str is null, or empty,
    *          or also thrown when str is below the specified minLength
    */
    validateStringLengthIsAtLeast : function(str, minLength) {
        return this.validateStringLengthInRange(str,minLength,Math.pow(2,31)-1);
    },
    
    /*
    * Validates that a given string (str) has at most maxLength length 
    * 
    * @param str the string to check the length of
    * @param maxLength maximum length for str
    * @throws StringTooLongException thrown when either str is null, or empty,
    * or also thrown when str is longer than the specified maxLength
    */
    validateStringLengthIsAtMost : function(str, maxLength) {
        return this.validateStringLengthInRange(str,0,maxLength);
    },

    
    /*
    * Helper constant defined to contain all lowercase letters of the standard
    * (western) 26-character latin alphabet.  Useful for constructing allowed
    * and denied parameters for passing into validateStringCharsContained.
    */
    LOWERCASE_LETTERS : "abcdefghijklmnopqrstuvwxyz",
    
    /*
    * Helper constant defined to contain all uppercase letters of the standard
    * (western) 26-character latin alphabet.  Useful for constructing allowed
    * and denied parameters for passing into validateStringCharsContained.
    */
    UPPERCASE_LETTERS : "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
    
    /*
    * Helper constant defined to contain all upper and lower case letters of
    * the latin alphabet.  Handy for constructing allowed and denied parameters
    * for validateStringContainsOnly and validateStringContainsNo methods.
    */
    LATIN_ALPHABET : null,  //has to be defined externally (ala C++)
    
    /*
    * Helper constant defined to contain all upper and lower case letters of
    * the extended 8-bit portion of the higher ASCII representation of the
    * extended latin alphabet.  Used in constructing allowed/denied parameters
    * for validateStringContainsNo and validateStringContainsOnly methods.
    */
    EXTENDED_LATIN : "\u0094\u00a4\u00bd\u00be\u00c6\u00c7\u00d0\u00d2\u00d3\u00d4\u00d5\u00d7\u00de\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u0080\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008a\u008b\u008c\u008d\u008e\u008f\u0090\u0093\u00d1\u00d6\u00d8\u00ed\u0095\u0096\u0097\u0098\u0099\u009a\u009b\u009c\u009d\u009e\u009f\u00a0\u00a1\u00a2",

    /*
    * Validates that a given string (text) only contains characters from an
    * allowed set of characters specified as a String.
    * 
    * Performs a case sensitive  comparison by default, beware mixed case.
    * Skips validation, without reporting error, when text is a null reference.
    * 
    * @param text the String to be validated for permitted and prohibited chars
    * @param allowed a String of all of the allowable characters for text.
    * @throws CharactersMissingException thrown when text contains chars not in allowed
    * @throws NullArgumentException thrown only when allowed is null, text may be null
    */
    validateStringContainsOnly : function(text, allowed) {
        if(null == text) { return new Array(0,""); }
        if(null == allowed || allowed=="") {
            return new Array(-1, "allowed string cannot be null ref");
        }
        //String.prototype.contains=new function(n){return this.indexOf(n)>=0;}
        for(var c=0;c<text.length;c++) {
            var t=text.charAt(c);
            if(allowed.indexOf(t)<0) {
                return new Array(116, "Disallowed character detected: "+t+"[@"+c+']');
            }
        }
        return new Array(0,"");
    },
    
    /*
    * Validates that a given string (text) contains no denied characters.
    * Performs a case sensitive  comparison by default, beware mixed case.
    * Skips validation, without reporting error, when text is a null reference.
    * 
    * @param text the String to be validated for permitted and prohibited chars
    * @param denied a String of all of the denied characters for text.
    * @throws ProhibitedCharacterException thrown when text contains chars in denied
    * @throws NullArgumentException thrown only when denied is null, text may be null
    */
    validateStringContainsNo : function(text, denied) {
        if(null == text) { return new Array(0,""); }
        if(null == denied) {
            return new Array(-1, "denied string cannot be null ref");
        }
        for(var c=0;c<denied.length;c++) {
            if(text.indexOf(denied.charAt(c))>=0) {
                return new Array(115, "Prohibited character detected: "+c);
            }
        }
        return new Array(0,"");
    },
    
    /*
    * Validates that a given string (text) only contains only characters from
    * allowed set, and that text does not contain any character from denied set.
    * Performs a case sensitive  comparison by default, beware mixed case.
    * Skips validation, without reporting error, when text is an empty string.
    * Takes exception when text is a null reference, or allowed or denied are null.
    * 
    * @param text the String to be validated for permitted and prohibited chars
    * @param allowed a String of all of the allowable characters for text.
    * @param denied a String of all of the denied characters for text.
    * @throws ProhibitedCharacterException thrown when text contains characters from denied set
    * @throws CharactersMissingException thrown when text contains one or more characters not in allowed set
    * @throws NullArgumentException thrown when any of the arguments if null.
    */
    validateStringCharsContained : function(text, allowed, denied) {
        var result=this.validateStringNotEmpty(text);
        if(result[0]!=0) { return result; }
        result=this.validateStringContainsOnly(text,allowed);
        if(result[0]!=0) { return result; }
        result=this.validateStringContainsNo(text,denied);
        if(result[0]!=0) { return result; }
        return new Array(0,"");
    },
    
    
    /*
    * Validates that a given string (item) contains a minimum amount (minCount)
    * of a certain collection of characters (chars).
    * 
    * @param item the string to check
    * @param minCount the number required
    * @param chars the characters that meet the requirement
    * @throws CharactersMissingException thrown when item does not contain at
    *          least minCount number of characters from chars.
    * @throws NullArgumentException thrown when chars is given as a null ref
    */
    validateStringContainsMinimumChars : function(item, minCount, chars) {
        var result=this.validateStringNotEmpty(item);
        if(result[0]!=0) { return result; }
        if(null==chars) {
            return new Array(-1 ,"required character set cannot be null");
        }
        for(var i=0;i<item.length;i++) {
            if(chars.indexOf(item.charAt(i))>=0) { minCount=minCount-1; }
        }
        if(minCount>0) {
            return new Array(116,
                "field does not contain enough specific characters"
            );
        }
        return new Array(0,"");
    },
    
    
    /*
    * Helper constant defined to contain all ten Hindu-Arabic numeric digits.
    * Useful for constructing allowed and denied parameters
    * for validateStringContainsOnly and validateStringContainsNo methods.
    */
    NUMBERS : "0123456789",
    
    /*
    * Private class fields used within the validatePasswordAgainstConstraints
    * method.  Values of NEWLINES and WHITESPACE are used for the whitespace
    * validation internal to the method (see validatePasswordAgainstConstraints)
    * and SYMBOLS is used for the symbol validation.
    */
    NEWLINES : "\r\n",
    WHITESPACE : " \t",
    SYMBOLS :"!@#$%^&*",  //formerly included: ~+=\\|/[]{}()'\"<>
    
    /*
    * Validates that passwd is at least length characters long, has at least
    * upper Uppercase letters, and at least numbers number characters, with at
    * least symbols symbol characters, and checks for spaces according to whitespace.
    * Passwd values that are equal to an empty string, or are empty according
    * to the isEmpty method will cause an IllegalArgumentException regardless
    * of the value given in the length parameter.  Negative length parameter
    * values are simply ignored, as are lengths equal to zero.
    * Will not validate a passwd string that contains any newline characters,
    * regardless of the value of whitespace - which concerns in-line space only.
    * If whitespace is set to true, then whitespace characters (tabs, spaces)
    * are allowed, otherwise they are prohibited and will cause exception.
    *
    * @param passwd the paintext password string to validate against
    * @param length minimum length that password must be in order to validate
    * @param upper minimum count of uppercase letters to be present in passwd
    * @param lower minimum count of lowercase letters to be present in passwd
    * @param letter minimum count of all letters which must be present in passwd
    * @param numbers minimum count of numeric characters needed in passwd
    * @param symbols minimum count of symbolic characters required in passwd
    * @param whitespace flag indicating whether spaces,tabs,etc are allowed.
    * @throws InvalidPasswordException thrown when passwd does not meet conditions
    * @throws NullArgumentException thrown when passwd parameter is passed as null
    * @throws StringTooShortException thrown when passwd is shorter than length
    */
    validatePasswordAgainstConstraints : function(passwd, length, upper, lower, letter, numbers, symbols, whitespace) {
        if(typeof(whitespace) != typeof(true)) {
            return new Array(-5365630128856068164,
                "Illegal Argument passed to function"
            );
        }
        if(null == passwd) {  //we don't accept null as a valid password
            return new Array(-1, "password field cannot be null");
        }
        var result;
        if(length>0) {  //we have a minimum length to validate
            result=this.validateStringLengthIsAtLeast(passwd,length);
            if(result[0]!=0) { return result; }
        } else {  //otherwise just ensure not empty
            result=this.validateStringNotEmpty(passwd);
            if(result[0]!=0) { return result; }
        }
        //uppercase letters:
        result=this.validateStringContainsMinimumChars(passwd,upper,StringValidator.UPPERCASE_LETTERS);
        if(result[0]!=0) { return result; }
        //lowercase letters:
        result=this.validateStringContainsMinimumChars(passwd,upper,StringValidator.LOWERCASE_LETTERS);
        if(result[0]!=0) { return result; }
        //any case letters:
        result=this.validateStringContainsMinimumChars(passwd,letter,StringValidator.LATIN_ALPHABET);
        if(result[0]!=0) { return result; }
        //numeric characters:
        result=this.validateStringContainsMinimumChars(passwd,numbers,StringValidator.NUMBERS);
        if(result[0]!=0) { return result; }
        //symbolic characters:
        result=this.validateStringContainsMinimumChars(passwd,symbols,StringValidator.SYMBOLS);
        if(result[0]!=0) { return result; }
        //whitespace (and newlines):
        result=this.validateStringContainsNo(passwd,StringValidator.NEWLINES);
        if(result[0]!=0) { return result; }
        if(!whitespace) {  //in-line whitespace not permitted
            result=this.validateStringContainsNo(passwd,StringValidator.WHITESPACE);
            if(result[0]!=0) { return result; }
        }
        if(result[0]==116) {
            return new Array(111,
                "Invalid Password: needs more character diversity"
            );
        }
        if(result[0]==115) {
            return new Array(111,
                "Invalid Password: contains characters not permissable for password"
            );
        }
        return new Array(0,"");
    },
    

    /*
    * Private class field used within the validateEmail method.
    * This regular expression is used to determine validity of E-mail addresses.
    */
    EMAIL_REGEX : "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$",
    
    /*
    * Validate that an E-mail address (email) follows format conventions
    * @param email the E-mail address to validate against
    * @throws InvalidEmailException thrown when email is invalid
    * @throws NullArgumentException thrown if email is supplied as null
    */
    validateEmail : function(email) {
        if(null==email) {  //will not validate a null E-mail
            return new Array(-1, "E-mail field cannot be null");
        }
        // Compile the refular expression into a pattern:
        re = new RegExp(this.EMAIL_REGEX);
        // Match the given string with the pattern:
        if(!re.test(email)) { //Invalid!
            return new Array(112, "E-mail address is not valid");
        }
        return new Array(0,"");
    }
    
}

StringValidator.LATIN_ALPHABET=StringValidator.UPPERCASE_LETTERS;
StringValidator.LATIN_ALPHABET+=StringValidator.LOWERCASE_LETTERS;