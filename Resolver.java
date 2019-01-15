package classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;


/**
 * 
 * @author Jason Woods
 * 
 * Resolver class that runs the process of resolution to test whether
 * or not an argument is valid based on the given premises and conclusion.
 *
 */
public class Resolver {

	private static HashMap<Literal, TreeSet<Clause>> clauseMap;
	private static Queue<Clause> queue;
	private static HashMap<Clause, ArrayList<Clause>> paths;
	private static HashMap<Clause, Integer> numberings;
	private static HashSet<Clause> printedClauses;
	private static int count;
	

	/**
	 * @param fileName The String representing the file to be read from
	 * @modifies this
	 * @effects calls the initializer function
	 */
	private static void fileLoader(String fileName) {
			
		try {
			
			//Prepare the file to be read
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferRead = new BufferedReader(fileReader);
			String line;
			List<Clause> clauseList = new ArrayList<Clause>();
			
			//Loop through each line of the file and create a Clause
			//for every line, storing it in a list
			while ((line = bufferRead.readLine()) != null) {
				Clause c = Parser.parse(line);
				clauseList.add(c);
			}
			bufferRead.close();
			
			//Call the initializer with the Clause list
			initializer(clauseList.iterator());
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * @param number The int signifying the number of clauses to read in
	 * @param bufferRead The open BufferedReader stream to read data from
	 * @modifies this
	 * @effects calls the initializer function
	 * @throws IOException
	 */
	private static void inputLoader(int number, BufferedReader bufferRead) throws IOException {
		
		List<Clause> clauseList = new ArrayList<Clause>();
		
		while (number > 0) {
			String line = bufferRead.readLine();
			Clause c = Parser.parse(line);
			clauseList.add(c);	
			number--;
		}
		
		initializer(clauseList.iterator());
		
	}
	
	
	/** 
	 * @param clauseItr The Iterator attached to a collection of Clause objects
	 * representing the premises and conclusion in CNF form
	 * @modifies this
	 * @effects Adds each Clause and its number to numberings, increments count for 
	 * each Clause, adds each Clause to queue, adds each Literal from each Clause
	 * to clauseMap
	 */
	private static void initializer(Iterator<Clause> clauseItr) {
		
		clauseMap = new HashMap<Literal, TreeSet<Clause>>();
		queue = new LinkedList<Clause>();
		paths = new HashMap<Clause, ArrayList<Clause>>();
		numberings = new HashMap<Clause, Integer>();
		printedClauses = new HashSet<Clause>();
		count = 1;
		
		//null check
		if (clauseItr == null) {
			return;
		}
		
		//Loop through all of the Clauses
		while (clauseItr.hasNext()) {
			Clause c = clauseItr.next();
			
			//Print each Clause with its number
			System.out.print(count + ". ");
			c.print();
			System.out.println();
			
			//Increment count and add to the appropriate data structures
			numberings.put(c, count);
			queue.add(c);
			addClauseToMap(c);
			count++;
		}
		
	}
	
	
	/**
	 * @param c The Clause used to populate clauseMap
	 * @modifies this
	 * @effects Adds each Literal from c to clauseMap and adds c
	 * to the corresponding set of Clauses that is the value for
	 * each Literal
	 * @return true if c was not already in the map and was 
	 * successfully added, false otherwise
	 */
	private static boolean addClauseToMap(Clause c) {
		
		//Loop through each Literal in c
		Iterator<Literal> itr = c.iterator();
		while (itr.hasNext()) {
			Literal l = itr.next();
			
			//Check to see if the Literal has already been made a key
			if (clauseMap.containsKey(l)) {
				
				//Try to add c to the TreeSet of Clauses corresponding to
				//the Literal
				TreeSet<Clause> t = clauseMap.get(l);
				if (!t.add(c)) {
					return false;
				}
				
			} else {
				
				//Initialize the TreeSet if it hasn't been made yet and
				//add c
				TreeSet<Clause> t = new TreeSet<Clause>();
				t.add(c);
				clauseMap.put(l, t);
			}
		}
		return true;
		
	}
	
	
	/**
	 * @param c The Clause to start printing at
	 * @modifies this
	 * @effects Adds the corresponding number to numberings and
	 * increments count for each Clause that is printed
	 */
	private static void printer(Clause c) {
		
		//Get the predecessors of c
		//If it has none, it is a premise, so return
		ArrayList<Clause> predecessors = paths.get(c);
		if (predecessors == null) {
			return;
		}
		
		//Recurse the left and right subtrees
		printer(predecessors.get(0));
		printer(predecessors.get(1));
		
		//Check to see if c has already been printed
		if (!printedClauses.contains(c)) {
			
			//Print c and its two predecessors
			//Add the number to numberings and increment count
			System.out.print(count + ". ");
			numberings.put(c, count);
			count++;
			c.print();
			System.out.println(" " + numberings.get(predecessors.get(0)) + "," + numberings.get(predecessors.get(1)));
			printedClauses.add(c);
		}
		
	}
	
	
	/**
	 * @modifies this
	 * @effects Repeatedly removes the head of queue until it is empty,
	 * adds to the tail of queue whenever a new resolvent is created,
	 * adds the two predecessors to paths for each resolvent
	 */
	private static void driver() {

		while (!queue.isEmpty()) {
		
			//Get the head Clause from queue and an iterator to its Literals
			Clause currentC = queue.remove();
			Iterator<Literal> literalItr = currentC.iterator();

			//Loop through each Literal
			while (literalItr.hasNext()) {
			
				//Look for the negated Literal in clauseMap, continue if it 
				//does not exist
				Literal currentL = literalItr.next();
				Literal negatedL = new Literal(currentL.getChar(), !currentL.isNegated());
				TreeSet<Clause> set = clauseMap.get(negatedL);
				if (set == null) {
					continue;
				}
				
				//Search through the set of Clauses corresponding to the negated Literal
				Iterator<Clause> setItr = set.iterator();
				while (setItr.hasNext()) {
					
					//Create a new resolvent from the current Clause and the Clause
					//from the set
					Clause checkC = setItr.next();
					Clause newClause = currentC.resolveClauses(checkC, currentL);

					//If the new Clause is a tautology, continue
					if (newClause == null) {
						continue;
						
					//If the new Clause is empty, a contradiction has been found
					} else if (newClause.isEmpty()) {
						
						//Add the predecessors to paths
						ArrayList<Clause> resolvedClauses = new ArrayList<Clause>();
						resolvedClauses.add(currentC);
						resolvedClauses.add(checkC);
						paths.put(newClause, resolvedClauses);
						
						//Print the path to the contradiction and return
						printer(newClause);
						System.out.println("Contradiction, therefore the conclusion is valid.\n");
						return;
						
					} else {
						
						//Try to add the clause to clauseMap
						if (addClauseToMap(newClause)) {
							
							//Add the path to the new Clause and add the new Clause
							//to the queue
							ArrayList<Clause> resolvedClauses = new ArrayList<Clause>();
							resolvedClauses.add(currentC);
							resolvedClauses.add(checkC);
							paths.put(newClause, resolvedClauses);
							queue.add(newClause);
						}
						break;
					}
				}
			}
		}
		
		//The argument is invalid if every possible resolvent is produced and no 
		//contradiction is found
		System.out.println("No contradiction found, therefore the argument is invalid.\n");
		
	}
		
	
	public static void main(String[] arg) {
		
		while (true) {
			
			System.out.println("Enter 'f' to read input from a file.");
			System.out.println("Enter 'i' to enter your own input directly.");
			System.out.println("Enter 'q' to quit.");
			
			String command = null;
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				
				command = bufferRead.readLine();
				
				if (command.equals("f")) {
					
					System.out.print("Enter the filename: ");
					String fileName = bufferRead.readLine();
					fileLoader(fileName);
										
				} else if (command.equals("i")) {
					
					System.out.print("Enter the number of clauses: ");
					String numberClauses = bufferRead.readLine();
					int number = Integer.parseInt(numberClauses);
					System.out.println("Now input the clauses, hitting the 'Enter' key after each one");
					inputLoader(number, bufferRead);
										
				} else if (command.equals("q")) {
					
					return;
					
				} else {
					
					System.out.println("Command unrecognized\n");
					continue;
					
				}
							
				driver();
				
			} catch (IOException e1) {
				System.out.println("Incorrect input\n");
			}	
		}
		
	}
	
}
