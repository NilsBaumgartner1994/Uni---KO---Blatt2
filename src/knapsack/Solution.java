package knapsack;

/**
 * Solution of a integer or binary knapsack problem
 *
 * @author Stephan Beyer
 */
public class Solution extends GenericSolution<Integer> {
	public Solution(Instance instance) {
		super(instance);
	}

	/**
	 * Copy a solution (copy constructor)
	 */
	public Solution(Solution solution) {
		super(solution);
	}

	/**
	 * Assign a quantity to an item.
	 *
	 * @param item index of the item
	 * @param quantity quantity to be assigned
	 */
	@Override
	public void set(int item, Integer quantity) {
		assert sol.size() > item : "Item number " + item + " not found!";
		assert sol.get(item) != null : "Item " + item + " not initialized in solution.";
		// TODO: update solValue, solWeight, sol[item]
		// DONE
		if(quantity.intValue()<0){
			Logger.println("Quantity < 0 ! Abort !");
			throw new RuntimeException("Quantity < 0 ! Abort !");
		}
		if(item < 0){
			Logger.println("Item negative ! Abort !");
			throw new RuntimeException("item < 0 ! Abort !");
		}
		
		
		//Variablen holen
		int itemValue = this.instance.getValue(item);	//Effizienz des Items
		int itemWeight = this.instance.getWeight(item); //Kosten des Items
		int newQuantity = quantity.intValue();			//neue Anzahl der Items
		int oldAmount = this.get(item);					//alte Anzahl der Items

		//Effizienz differenz berechnen
		int oldValue = oldAmount * itemValue;	//alte Effizienz
		int newValue = newQuantity * itemValue; //neue Effizienz
		int diffValue = newValue-oldValue;		//differenz der Effizienz
		
		//Analog zur Effizienz mit Gewicht
		int oldWeight = oldAmount * itemWeight;
		int newWeight = newQuantity * itemWeight;
		int diffWeight = newWeight-oldWeight;
		
		//Update der neuen Variablen
		int newSolValue = this.solValue + diffValue;	//neue Effizienz
		int newSolWeight = this.solWeight + diffWeight; //neues Gewicht
		
		if( newSolValue < 0) {
			Logger.println("Negative SolValue ! ");
			return;
		}
		if (newSolWeight < 0){
			Logger.println("Negative SolWight !");
			return;
		}
		
		sol.set(item, quantity);	//setzen der Anzahl der Items
		this.solValue = newSolValue;
		this.solWeight = newSolWeight;
	}

	/**
	 * Check if the solution is feasible.
	 */
	@Override
	public boolean isFeasible() {
		int maxWeight = this.instance.getCapacity();
		return this.solWeight<=maxWeight;
	}

	/**
	 * Check if the solution is a binary solution
	 */
	@Override
	public boolean isBinary() {
		for (int quantity : sol) {
			if (quantity != 0 && quantity != 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Integer zero() {
		return 0;
	}
}
