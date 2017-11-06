package knapsack;

/**
 * Solution of a integer or binary knapsack problem
 *
 * @author Nils Baumgartner
 */
public class BinarySolver implements SolverInterface<Solution> {
	
	public Instance instance; //merken der Instanz
	public static Solution best; //merken der besten L�sung

	@Override
	public Solution solve(Instance instance) {
		this.instance = instance;
		// TODO Auto-generated method stub
		
		//Dieses Solver l�st Probleme f�r Beliebig gro�e Items und Rucks�cke, bzw. besitzt deren Grenzen
		//Loesungen = 2^Objekte
		//da es 5000 Objekte sein k�nnen und 2^5000 >> 2^64 m�ssen wir incrementell durchlaufen
		
		Solution toTest = new Solution(instance); //wir beginne damit, nichts mitzunehmen
		best = new Solution(toTest); //erstelle 0 L�sung als Bestes
		
		//wir fangen an mit L�sungsvektor 000000...
		//und enden bei L�sungsvektor 111111...
		//sprich wir inkrementieren bis dahin, da man die Bitfolge als Zahl sehen kann
		
		while(isIncrementableSolution(toTest)) { // solange wir inkrementieren k�nnen
			//Logger.print(toTest.toString());
			if(isBetterSolution(toTest)) { // pr�fen wir ob es eine bessere L�sung gibt
				best = new Solution(toTest); // und merken diese ggf.
				Logger.println("New Best Solution: "+best.toString()+" with a Weight: "+best.getWeight()+" and Value: "+best.getValue());
			}		
			toTest = incrementBinarySolution(toTest); //danach inkrementieren einen Schritt
		}
		//Nun sind alle bis auf die letzte Lösung ausprobiert
		//d.h. die letzte Lösung fehlt
		//Logger.print(toTest.toString());
		if(isBetterSolution(toTest)) { // pr�fen wir ob es eine bessere L�sung gibt
			best = new Solution(toTest); // und merken diese ggf.
		}
		//oder sind mit allen L�sungen durch
	
		return best; //und geben die beste L�sung zur�ck
	}
	
	/**
	 * Test Mac vs PC special characters
	 * on keyboard ü
	 * via shortcut ü
	 */
	
	/**
	 * P�ft ob eine L�sung inkrementierbar ist
	 * @param toIncrement L�sung zum pr�fen
	 * @return true falls inkrementierbar
	 */
	private static boolean isIncrementableSolution(Solution toIncrement) {
		int bits = toIncrement.getIntegerArray().length; // hole anzahl an Bitstellen
		for(int i = 0; i<bits ; i++) { // wenn es in irgendeiner Stelle
			if(toIncrement.get(i)==0) { // eine 0 gibt, ist es noch nicht das maximum
				return true; //sprich es ist inkrementierbar
			}
		}
		return false; //wenn alle Stellen belegt ==> nicht inkrementierbar
	}
	
	
	/**
	 * Inkrementiere die Objekte wie eine bin�re Zahl
	 * @param toIncrement L�sung von der die n�chste gesucht wird
	 * @return N�chst h�hre L�sung
	 */
	private static Solution incrementBinarySolution(Solution toIncrement) {
		Solution solution = new Solution(toIncrement);	//Erstelle Kopie
		int bits = solution.getIntegerArray().length; // hole anzahl an Bitstellen
		
		//bin�res inkrementieren von https://stackoverflow.com/questions/7327619/how-to-increment-bits-correctly-in-an-array
		boolean carry = true;
	    for (int i = (bits - 1); i >= 0; i--) {
	        if (carry) {
	            if (solution.get(i) == 0) {
	                solution.set(i, 1);
	                carry = false;
	            }
	            else {
	                solution.set(i, 0);
	                carry = true;
	            }
	        }
	    }
		
	    // Gebe inkrementierte L�sung zur�ck
	    return solution;
	}
	
	/**
	 * �berpr�ft ob L�sung besser als bekannte beste L�sung
	 * @param toTestzu �berpr�fende neue L�sung
	 * @return boolean true wenn die L�sung besser ist
	 */
	private static boolean isBetterSolution(Solution toTest) {
		//Security
		if(!toTest.isBinary()){
			Logger.println(" Solution not Binary ! ");
			return false;
		}
		if(!toTest.isFeasible()){	
			//Logger.println(" Solution not feasible ! ");
			return false;
		}
				
		if(toTest.getValue()>best.getValue()) { //pr�fen obs besser ist
			Logger.println(" is a better Solution found a better Value "+toTest.getValue()+" and Weight: "+toTest.getWeight()+"! <---------------------");
			return true;
		}
		else {
			//Logger.println(" is less good than old best ! ");
			return false;
		}
	}
	
}
