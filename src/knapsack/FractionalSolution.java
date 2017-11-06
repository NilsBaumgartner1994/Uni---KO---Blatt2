package knapsack;

/**
 * Solution of a fractional knapsack problem
 *
 * @author Stephan Beyer
 */
public class FractionalSolution extends GenericSolution<Double> {
	private double epsilon = 1e-6;

	public FractionalSolution(Instance instance) {
		super(instance);
	}

	/**
	 * Copy a solution (copy constructor)
	 */
	public FractionalSolution(FractionalSolution solution) {
		super(solution);
	}

	@Override
	public void set(int item, Double quantity) {
		assert sol.size() > item : "Item number " + item + " not found!";
		assert sol.get(item) != null : "Item " + item + " not initialized in solution.";
		// TODO: insert correct solution of excerise sheet 1 here
		
		if(quantity.intValue()<0){
			Logger.println("Quantity < 0 ! Abort !");
			throw new RuntimeException("Quantity < 0 ! Abort !");
		}
		if(item < 0){
			Logger.println("Item negative ! Abort !");
			throw new RuntimeException("item < 0 ! Abort !");
		}
		
		
		//Variablen holen
		double itemValue = this.instance.getValue(item);	//Effizienz des Items
		double itemWeight = this.instance.getWeight(item); //Kosten des Items
		double newQuantity = quantity.doubleValue();			//neue Anzahl der Items
		double oldAmount = this.get(item);					//alte Anzahl der Items

		//Effizienz differenz berechnen
		double oldValue = oldAmount * itemValue;	//alte Effizienz
		double newValue = newQuantity * itemValue; //neue Effizienz
		double diffValue = newValue-oldValue;		//differenz der Effizienz
		
		//Analog zur Effizienz mit Gewicht
		double oldWeight = oldAmount * itemWeight;
		double newWeight = newQuantity * itemWeight;
		double diffWeight = newWeight-oldWeight;
		
		//Update der neuen Variablen
		double newSolValue = this.solValue + diffValue;	//neue Effizienz
		double newSolWeight = this.solWeight + diffWeight; //neues Gewicht
		
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

	@Override
	public boolean isFeasible() {
		// TODO: insert correct solution of excerise sheet 1 here
		int maxWeight = this.instance.getCapacity();
		return this.solWeight<=maxWeight;
	}

	@Override
	public boolean isBinary() {
		for (double quantity : sol) {
			if (quantity > epsilon
			 && quantity < 1 - epsilon) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected Double zero() {
		return 0.0;
	}
}
