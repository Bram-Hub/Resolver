package classes;

/**
 * 
 * @author Jason Woods
 * 
 * An immutable Literal class representing a literal object consisting of
 * a character and a negation indicator.
 *
 */
public class Literal {
	
	private char c;
	private boolean negated;
	
	
	/**
	 * @param c_ The char to represent the Literal
	 * @param negated_ The boolean indicating whether or not
	 * the Literal is negated
	 * @modifies this
	 * @effects Sets this.c equal to c_ and this.negated equal
	 * to negated_
	 */
	public Literal(char c_, boolean negated_) {
		c = c_;
		negated = negated_;
	}
	
	
	/**
	 * @return this.c
	 */
	public char getChar() {
		return c;
	}
	
	
	/**
	 * @return this.negated
	 */
	public boolean isNegated() {
		return negated;
	}
	
	
	/**
	 * Prints a visual representation of this to System.out
	 */
	public void print() {
		if (negated) {
			System.out.print("~");
		}
		System.out.print(c);
	}
	
	
	/**
	 * @return The hashCode of this
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c;
		result = prime * result + (negated ? 1231 : 1237);
		return result;
	}

	
	/**
	 * @param obj The Object to compare to this
	 * @return true if obj equals this, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Literal other = (Literal) obj;
		if (c != other.c)
			return false;
		if (negated != other.negated)
			return false;
		return true;
	}
}
