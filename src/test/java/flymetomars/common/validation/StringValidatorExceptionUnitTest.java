package flymetomars.common.validation;

import org.junit.Test;

/**
 *
 * @author Lawrence Colman
 */
public class StringValidatorExceptionUnitTest {

    //<editor-fold desc="validate string not empty test cases">
    
    @Test
    public void testValidateStringNotEmptySuccess() {
        StringValidator.validateStringNotEmpty("not null");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringNotEmptyFailure() {
        StringValidator.validateStringNotEmpty("");
    }
    
    //</editor-fold>

    //<editor-fold desc="validate string length in range test cases">
    
    @Test
    public void testValidateStringLengthInRangeSuccess() {
        StringValidator.validateStringLengthInRange("yomomma", 6, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringLengthInRangeFailure() {
        StringValidator.validateStringLengthInRange("tutrle", 8, 12);
    }
    
    //</editor-fold>

    //<editor-fold desc="validate string length at least test cases">
    
    @Test
    public void testValidateStringLengthIsAtLeastSuccess() {
        StringValidator.validateStringLengthIsAtLeast("supercalafragalisticexpialadocious", 2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringLengthIsAtLeastFailure() {
        StringValidator.validateStringLengthIsAtLeast("Sam", 4);
    }
    
    //</editor-fold>

    //<editor-fold desc="validate string length most test cases">
    
    @Test
    public void testValidateStringLengthIsAtMostSuccess() {
        StringValidator.validateStringLengthIsAtMost("mynameis", 12);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringLengthIsAtMostFailure() {
        StringValidator.validateStringLengthIsAtMost("SlimShady", 8);
    }
    
    //</editor-fold>

    //<editor-fold desc="validate string contians only test cases">
    
    @Test
    public void testValidateStringContainsOnlySuccess() {
        StringValidator.validateStringContainsOnly("abc nbc and cbs!", "abcdns !");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringContainsOnlyFailure() {
        StringValidator.validateStringContainsOnly("abc nbc and cbs!", "abcdns!");
    }
    
    //</editor-fold>

    //<editor-fold desc="validate string contains no test cases">
    
    @Test
    public void testValidateStringContainsNoSuccess() {
        StringValidator.validateStringContainsNo("abcedfg", "ABC");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringContainsNoFailure() {
        StringValidator.validateStringContainsNo("text is not numb3r5", StringValidator.NUMBERS);
    }
    
    //</editor-fold>

    //<editor-fold desc="validate characters contained test cases">
    
    @Test
    public void testValidateStringCharsContainedSuccess() {
        StringValidator.validateStringCharsContained("Everybody was kung-foo-fighting", StringValidator.LATIN_ALPHABET+" -", ".,");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringCharsContainedFailure() {
        StringValidator.validateStringCharsContained("This is my rile, this is my gun...", " thismyrlegun", ",.");
    }
    
    //</editor-fold>

    //<editor-fold desc="validate minimum length test cases">
    
    @Test
    public void testValidateStringContainsMinimumCharsSuccess() {
        StringValidator.validateStringContainsMinimumChars("aeiou", 2, "abcde");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateStringContainsMinimumCharsFailure() {
        StringValidator.validateStringContainsMinimumChars("AEIOU", 7, "ABCDE");
    }
    
    //</editor-fold>

    //<editor-fold desc="validate password test cases">
    
    @Test
    public void testValidatePasswordAgainstConstraintsSuccess() {
        StringValidator.validatePasswordAgainstConstraints("P4ssw0rd!", 4, 1, 2, 4, 2, 1, true);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidatePasswordAgainstConstraintsFailure() {
        StringValidator.validatePasswordAgainstConstraints("P4ssw0rd!", 4, 2, 2, 4, 2, 1, false);
    }
    
    //</editor-fold>

    //<editor-fold desc="validate email test cases">
    
    @Test
    public void testValidateEmailSuccess() {
        StringValidator.validateEmail("my@email.org");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValidateEmailFailure() {
        StringValidator.validateEmail("This.email has_spaces-and+no atSymbol");
    }
    
    //</editor-fold>
}
