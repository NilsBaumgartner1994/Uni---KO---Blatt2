package knapsack;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

/**
 * Solution of a fractional knapsack problem
 *
 * @author Nils Baumgartner
 */
public class HeuristicBinarySolver implements SolverInterface<Solution> {

	/**
	 * HeuristicBinarySolver löst ein gegebenes Binäres R.Problem mittels einer Heuristik
	 * @param instance Die Problem Instanz
	 * @return Solution gibt eine binäre Lösung zurück, welche nicht die schlechteste ist.
	 */
	@Override
	public Solution solve(Instance instance) {
		// TODO Auto-generated method stub
		
		/**
		 * 1. Gehe alle Objekte durch und berechne den Nutzen Quotienten. O(n)
		 * 2. Sortiere Objekte nach fallenden Quotienten. O(n*log n)
		*/
		List<SortableItemValueWeightItem> itemsSortet = getSortedItemValueWeightItem(instance);
		
		/**
		 * 3. Erstelle L�sungsvektor mit 0en initialisiert. O(1)
		 */
		Solution solution = new Solution(instance); //wir beginne damit, nichts mitzunehmen
		//erstelle 0 L�sung

		/**
		 * 4. Gehe alle sortierten Objekte nacheinander durch und f�r jedes: O(n)
		 */
		for(SortableItemValueWeightItem item: itemsSortet) {
			/**
			 * 5. Berechne ob das Element noch in den restlichen Platz im Rucksack
			 * passt und nehme diesen ggf. mit. O(1)
			 */
			int quantity = getItemQuantityFromRemindingSpace(item.itemNumber, solution, instance);
			solution.set(item.itemNumber, quantity);
			
			/**
			 * 6. Pr�fe ob Rucksack voll ? O(1)
			 * 		Wenn ja, dann Terminiere und gebe L�sungsvektor aus O(1) 
			 * 		Wenn nein, nehme das n�chste Element und wiederhole 4. O(1)	
			*/
			
			//Gebe Fortschritt auf der Konsole aus
			Logger.println("Weight: "+solution.getWeight()+" / "+instance.getCapacity());
			if(solution.getWeight()==instance.getCapacity()) { //falls kein Platz mehr vorhanden
				return solution; //gebe Lösung aus
			}
		}
		
		return solution; //In jedem Fall gebe eine Lösung aus
	}
	
	/**
	 * Berechne ob das Objekt in den Rucksack mitgenommen werden kann
	 * @param itemNumber die Itemnummer
	 * @param solution die L�sung, welche vlt. bereits gef�llt ist
	 * @param instance die Instacne/Problem, welche das Zul�ssige gesammt gewicht h�lt
	 * @return Ergebnis ist 0 oder 1
	 */
	public int getItemQuantityFromRemindingSpace(int itemNumber, Solution solution,Instance instance) {
		double remindingSpace = instance.getCapacity()-solution.getWeight(); //berechne restlichen Platz im Rucksack
		double weightOfItem = instance.getWeight(itemNumber); //hole Gewicht des Items
		double amountOfItem = remindingSpace/weightOfItem; //Berechne wie oft das Item reinpassen w�rde
		int trimmed = (int) trimAmount(amountOfItem, 0, 1); //begrenze das Item auf Intervall 0,1 und begrenze es auf 0 oder 1
		//Gebe restlichen Platz auf der Konsole aus
		Logger.print("RemindingSpace: "+remindingSpace+" | weightOfItem: "+weightOfItem+" | amoundOfItem: "+amountOfItem+" | trimmed: "+trimmed+" | ");
		return trimmed;
	}
	
	/**
	 * Schneidet eine Zahl ab auf das minimum oder maximum, falls es au�erhalb der Grenzen liegt
	 * @param amount Die Zahl selber
	 * @param min das Minimum
	 * @param max das Maximum
	 * @return eine Zahl zwischen Min und Max, falls die Zahl dazwischen liegt, sonst die Grenze
	 */
	public double trimAmount(double amount, double min ,double max) {
		return Math.min(Math.max(amount, min), max); //W�hle die kleinere zahl aus zwischen (W�hle das Maximum aus Zahl und minimum) und dem maximum
	}
	
	/**
	 * Gibt eine Sortierte Liste der Items aus, nach fallenden Nutzen/Kosten Quotienten
	 * @param instance die Probleminstanz, von welcher die Items bezogen werden
	 * @return List<SortableItemValueWeightItem> nach fallender Nutzen/Kosten
	 */
	private List<SortableItemValueWeightItem> getSortedItemValueWeightItem(Instance instance){
		List<SortableItemValueWeightItem> list = getListItemValueWeightItems(instance); //hole eine Liste mit Items
		list.sort(null); //sortiere diese
		return list;
	}
	
	/**
	 * Gibt eine List der Items zur�ck
	 * @param instance Die Probleminstanz
	 * @return List<SortableItemValueWeightItem>
	 */
	private List<SortableItemValueWeightItem> getListItemValueWeightItems(Instance instance){
		List<SortableItemValueWeightItem> list = new ArrayList<SortableItemValueWeightItem>(); // neue Liste
		
		int amountItems = instance.getSize();
		for(int i = 0; i<amountItems; i++) { //durchlaufe alle Items
			//Erstelle neues <SortableItemValueWeightItem> und f�ge der Liste hinzu
			SortableItemValueWeightItem element = new SortableItemValueWeightItem(i, instance.getValue(i), instance.getWeight(i));
			list.add(element);
		}
		
		return list;
	}
	
	/**
	 * Dies ist eine kleine private Hilfsklasse, mit welcher ich die Items sortieren m�chte
	 * ohne viel Auffwand. Die Klasse ist klein und denke ich selbsterkl�rend. Sie soll halt
	 * nur eben ein Item nach Nutzen/Kosten sortieren k�nnen.
	 * @author nilsb
	 *
	 */
	private class SortableItemValueWeightItem implements Comparable {
		
		private int itemNumber;	//die Item Nummer
		private double valueperweight; //das Nutzen Kosten Verh�ltnis
		
		//einfacher Konstruktor
		private SortableItemValueWeightItem(int itemNumber, double value, double weight) {
			this.itemNumber = itemNumber;
			this.valueperweight = value/weight; //Berechne Nutzen/Kosten Quotienten
		}

		/**
		 * CompareTo Methode, geeignet f�r SortableItemValueWeightItem
		 * @param Object obj sollte vom Typ SortableItemValueWeightItem sein
		 * @return -1 falls dieses Objekt einen h�heren Nutzen/Kosten Faktor hat
		 * @return 1 wenn dieser kleiner ist
		 * @return 0 sonst
		 */
		@Override
		public int compareTo(Object obj) {
			if(obj instanceof SortableItemValueWeightItem) { //minimale Security
				SortableItemValueWeightItem item = (SortableItemValueWeightItem) obj;
				if(this.valueperweight>item.valueperweight) { //entweder bist du gr��er
					return -1;
				}
				else if(this.valueperweight<item.valueperweight) { //oder kleiner
					return 1;
				}
				return 0; //sonst gleich
			}
			else {
				return 0;
			}
		}
		
		/**
		 * Einfach toString Methode f�r Debugging
		 */
		@Override
		public String toString() {
			return "["+this.itemNumber+";"+this.valueperweight+"]";
		}
		
	}
}
