package nbradham.satisPlanner;

import java.util.HashMap;
import java.util.Scanner;

final class Planner {

	public static void main(String[] args) {
		Scanner scan = new Scanner(Planner.class.getResourceAsStream("/recipes.tsv")).useDelimiter("\t|\r\n");
		scan.nextLine();
		HashMap<String, Recipe> rByIn = new HashMap<>();
		while(scan.hasNext()) {
			
		}
		scan.close();
	}
	
	private static final record Recipe(String name, Object[][] ins, String machine, Object[][] outs) {}
}