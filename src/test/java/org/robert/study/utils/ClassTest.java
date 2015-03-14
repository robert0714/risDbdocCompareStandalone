package org.robert.study.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class ClassTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String subjectString="6-4-RLDFS001B.doc";
		try {
			Pattern regex = Pattern.compile("6-4-RLDFS[\\w1-9][\\w1-9][\\w1-9]B.doc");
			Matcher regexMatcher = regex.matcher(subjectString);
			while (regexMatcher.find()) {
				// matched text: regexMatcher.group()
				// match start: regexMatcher.start()
				// match end: regexMatcher.end()
				System.out.println(regexMatcher.group());
				System.out.println(regexMatcher.start());
				System.out.println(regexMatcher.end());
			} 
		} catch (PatternSyntaxException ex) {
			// Syntax error in the regular expression
		}


	}

}
