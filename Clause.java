package classes;

import java.util.*;

/**
 * 
 * @author Jason Woods
 * 
 * An immutable Clause class representing a disjunction of Literals.
 *
 */
public class Clause implements Iterable<Literal>, Comparable<Clause> {
	
	private HashSet<Literal> literals;
	
	
	/** @param literals_ list of literals to make up clause
	 *  @modifies this
	 *  @effects adds each literal to literals
	 */
	public Clause(List<Literal> literals_) {
		literals = new HashSet<Literal>();
		for (Literal l : literals_) {
			literals.add(l);
		}
	}
	
	
	/** @return iterator to literals
	 */
	public Iterator<Literal> iterator() {
		return literals.iterator();
	}
	
	
	/** @param l literal to check if it is contained
	 *  in literals
	 *  @return true if the literal is in literals,
	 *  false otherwise
	 */
	public boolean containsLiteral(Literal l) {
		return literals.contains(l);
	}
	
	
	/** @return true if literals is empty, false
	 *  otherwise
	 */
	public boolean isEmpty() {
		return literals.isEmpty();
	}
	
	
	/** @return true if the clause contains two literals
	 * 	of the same character but opposite signs, false
	 * 	otherwise
	 */
	public boolean isTautology() {
		Iterator<Literal> itr = iterator();
		while (itr.hasNext()) {
			Literal temp = itr.next();
			Literal newLiteral = new Literal(temp.getChar(), !temp.isNegated());
			if (literals.contains(newLiteral)) {
				return true;
			}
		}
		return false;
	}
	
	
	/** @param c The Clause to merge with this
	 * 	@param l1 The Literal to not copy from this into the return Clause
	 * 	@param l2 The Literal to not copy from c into the return Clause
	 *  @return New Clause containing the literals from this and c
	 */
	private Clause merge(Clause c, Literal l1, Literal l2) {
		ArrayList<Literal> list = new ArrayList<Literal>();
		Iterator<Literal> litItr = this.iterator();
		while (litItr.hasNext()) {
			Literal l = litItr.next();
			if (l.equals(l1)) {
				continue;
			}
			list.add(l);
		}
		litItr = c.iterator();
		while (litItr.hasNext()) {
			Literal l = litItr.next();
			if (l.equals(l2)) {
				continue;
			}
			list.add(l);
		}
		return new Clause(list);
	}
	
	
	/** @param c The Clause to resolve with
	 *  @param l The literal to be removed from this
	 *  @return New Clause which is composed of the literals
	 *  from this and c, excluding l and its negation
	 */
	public Clause resolveClauses(Clause c, Literal l) {
		Literal negatedL = new Literal(l.getChar(), !l.isNegated());
		Clause newC = this.merge(c, l, negatedL);
		if (!newC.isTautology()) {
			return newC;
		} else {
			return null;
		}
	}
	
	
	
	/** @return size of clause
	 */
	public int getSize() {
		return literals.size();
	}	
	
	
	/** @param c The Clause to compare to this
	 *  @return 1 if c should come before this, -1 if c should
	 *  come after this, 0 if the two clauses are equal
	 */
	public int compareTo(Clause c) {
		if (this.getSize() > c.getSize()) {
			return 1;
		} else if (this.getSize() < c.getSize()){
			return -1;
		} else {
			if (this.equals(c)) {
				return 0;
			}
			return -1;
		}
	}
	
	
	/** @return The hashCode of this
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((literals == null) ? 0 : literals.hashCode());
		return result;
	}


	/** @param obj The Object to compare to this
	 *  @return true if obj is equal to this, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if (literals == null) {
			if (other.literals != null)
				return false;
		} else if (!literals.equals(other.literals))
			return false;
		return true;
	}


	/** Prints a visual representation of this to System.out
	 */
	public void print() {
		System.out.print("{");
		Iterator<Literal> itr = literals.iterator();
		while (itr.hasNext()) {
			Literal l = itr.next();
			l.print();
			if (itr.hasNext()) {
				System.out.print(", ");
			}
		}
		System.out.print("}");
		
	}

}
