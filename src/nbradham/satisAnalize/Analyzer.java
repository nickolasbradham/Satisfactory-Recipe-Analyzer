package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

final class Analyzer {

	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("rates.tsv")).useDelimiter("\t|\r*\n");
		scan.skip(".*\n");
		int max = Integer.MIN_VALUE;
		HashMap<String, Item> items = new HashMap<>();
		Queue<Item> updateQueue = new LinkedList<>();
		while (scan.hasNext()) {
			String name = scan.next();
			int weight = scan.nextInt();
			Item i = new Item(weight);
			items.put(name, i);
			if (weight > max)
				max = weight;
			updateQueue.offer(i);
		}
		scan.close();
		for (Item i : items.values())
			i.weight = max / i.weight;
		System.out.println(items);
		scan = new Scanner(new File("recipes.tsv"));

		scan.close();
	}

	private static final class Item {

		private int weight;

		public Item(int initialWeight) {
			weight = initialWeight;
		}

		@Override
		public final String toString() {
			return String.valueOf(weight);
		}
	}
}