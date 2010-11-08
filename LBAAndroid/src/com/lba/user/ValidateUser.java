/**
 * 
 */
package com.lba.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author payalpatel
 *
 */
public class ValidateUser {
	/**
	 * isEmailValid: Validate email address using Java reg ex. This method
	 * checks if the input string is a valid email address.
	 * 
	 * @param email
	 *            String. Email address to validate
	 * @return boolean: true if email address is valid, false otherwise.
	 */

	public static boolean validateEmail(String email) {
		boolean isValid = false;

		/*
		 * Email format: A valid email address will have following format:
		 * [\\w\\.-]+: Begins with word characters, (may include periods and
		 * hypens).
		 * 
		 * @: It must have a '@' symbol after initial characters.
		 * ([\\w\\-]+\\.)+: '@' must follow by more alphanumeric characters (may
		 * include hypens.). This part must also have a "." to separate domain
		 * and subdomain names. [A-Z]{2,4}$ : Must end with two to four
		 * alaphabets. (This will allow domain names with 2, 3 and 4 characters
		 * e.g pa, com, net, wxyz)
		 * 
		 * Examples: Following email addresses will pass validation abc@xyz.net;
		 * ab.c@tx.gov
		 */

		// Initialize reg ex for email.
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		// Make the comparison case-insensitive.
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * isPhoneNumberValid: Validate phone number using Java reg ex. This method
	 * checks if the input string is a valid phone number.
	 * 
	 * @param email
	 *            String. Phone number to validate
	 * @return boolean: true if phone number is valid, false otherwise.
	 */
	public static boolean validaetPhoneNumber(String phoneNumber) {
		boolean isValid = false;
		/*
		 * Phone Number formats: (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn ^\\(? :
		 * May start with an option "(" . (\\d{3}): Followed by 3 digits. \\)? :
		 * May have an optional ")" [- ]? : May have an optional "-" after the
		 * first 3 digits or after optional ) character. (\\d{3}) : Followed by
		 * 3 digits. [- ]? : May have another optional "-" after numeric digits.
		 * (\\d{4})$ : ends with four digits.
		 * 
		 * Examples: Matches following <a href="http://mylife.com">phone
		 * numbers</a>: (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890
		 */
		// Initialize reg ex for phone number.
		String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
		CharSequence inputStr = phoneNumber;
		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	// validate first name
	public static boolean validateFirstName(String firstName) {
		return firstName.matches("[A-Z][a-zA-Z]*");
	} // end method validateFirstName

	// validate last name
	public static boolean validateLastName(String lastName) {
		return lastName.matches("[a-zA-z]+([ '-][a-zA-Z]+)*");
	} // end method validateLastName

	// validate address
	public static boolean validateAddress(String address) {
		return address.matches("\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)");
	} // end method validateAddress

	/**
	 * Validate username with regular expression
	 * 
	 * @param username
	 *            username for validation
	 * @return true valid username, false invalid username
	 * 
	 *         [a-z0-9_-] # Match characters and symbols in the list, a-z, 0-9,
	 *         underscore, hyphen {3,15} # Length at least 3 characters and
	 *         maximum length of 15
	 */
	public static boolean validateUserName(final String username) {

		final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();

	}

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password 
	 *         (?=.*\d) # must contains one digit from 0-9 
	 *         (?=.*[a-z]) # must contains one lowercase characters 
	 *         (?=.*[A-Z]) # must contains one uppercase characters 
	 *         (?=.*[@#$%]) # must contains one special symbols in the list 
	 *         "@#$%" . # match anything with previouscondition checking 
	 *         {6,20} # length at least 6 characters and
	 *         maximum of 20 )
	 */
	public static boolean validatePassword(final String password) {

		final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}
}
