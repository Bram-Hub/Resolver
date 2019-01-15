package classes;

import java.util.ArrayList;

/**
 * 
 * @author Jason Woods
 * 
 * A parser to read a line of comma-separated values into a Clause object,
 * where each value represents a Literal within the Clause.
 *
 */
public class Parser {

	
	/**
	 * @param input The string to parse in the form of <char, char, ....>
	 * @return The Clause created from input
	 */
	public static Clause parse(String input) {
		
		//Check if the input is null or of the wrong format
		if (input == null || input.length() == 0) {
			System.out.println("Invalid input.");
			return null;
		}
		
		//Split the line at the commas to get each literal
		String[] literals = input.split(",");
		
		ArrayList<Literal> literalList = new ArrayList<Literal>();
		
		for (String literal : literals) {
			
			//Remove the whitespace
			literal = literal.replaceAll(" ","");
			Literal l = null;
			
			//Positive Literal
			if (literal.length() == 1) {
				
				//Make sure the char is a letter
				if (Character.isLetter(literal.charAt(0))) {
					char c = Character.toUpperCase(literal.charAt(0));
					l = new Literal(c, false);
				}
				
			//Negative Literal
			} else if (literal.length() == 2) {
				
				//Make sure the first char is a negation symbol
				if (literal.charAt(0) == '~') {
					
					//Make sure the second char is a letter
					if (Character.isLetter(literal.charAt(1))) {
						char c = Character.toUpperCase(literal.charAt(1));
						l = new Literal(c, true);
					}
				}
			}
			
			//Add the Literal to the list
			if (l != null) {
				literalList.add(l);
				
			//If the Literal was never created, there was invalid input somewhere
			} else {
				System.out.println("Invalid input.");
				return null;
			}
		}
		
		//Create the new Clause and return it
		return new Clause(literalList);
	}
	
}
