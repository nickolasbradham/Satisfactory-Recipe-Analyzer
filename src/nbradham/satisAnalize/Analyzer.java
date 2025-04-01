package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

final class Analyzer {

	private static final HashMap<String, Item> ITEMS = new HashMap<>();

	private final void start() throws FileNotFoundException {
		Scanner scan = createScanner("rates.tsv");
		int max = Integer.MIN_VALUE;
		Queue<Item> updateQueue = new LinkedList<>();
		while (scan.hasNext()) {
			String name = scan.next();
			int weight = scan.nextInt();
			Item i = new Item(weight);
			ITEMS.put(name, i);
			if (weight > max)
				max = weight;
			updateQueue.offer(i);
		}
		scan.close();
		for (Item i : ITEMS.values())
			i.weight = max / i.weight;
		System.out.printf("Raw Resources: %s%n", ITEMS);
		scan = createScanner("recipes.tsv");
		while (scan.hasNext()) {
			Recipe r = new Recipe(scan.next(), parseItems(scan.next()), scan.next(), parseItems(scan.next()));
			if (r.input.size() == 0)
				r.output.keySet().forEach(k -> k.weight = 0);
			else
				r.input.keySet().forEach(k -> {
					int l = k.consumers.length;
					k.consumers = Arrays.copyOf(k.consumers, l + 1);
					k.consumers[l] = r;
				});
		}
		scan.close();
		System.out.println("Items:");
		ITEMS.forEach((k, v) -> System.out.printf("\t%s: %s%n", k, v));
	}

	private final HashMap<Item, Float> parseItems(String str) {
		HashMap<Item, Float> items = new HashMap<>();
		if (str.length() != 0)
			for (String s : str.split(", ")) {
				int x = s.indexOf('x');
				String name = s.substring(x + 1);
				Item i = ITEMS.get(name);
				if (i == null)
					ITEMS.put(name, i = new Item());
				items.put(i, Float.parseFloat(s.substring(0, x)));
			}
		return items;
	}

	private static Scanner createScanner(String filename) throws FileNotFoundException {
		return new Scanner(new File(filename)).useDelimiter("\t|\r*\n").skip(".*\r*\n");
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}

	private static final class Item {

		private Recipe[] consumers = new Recipe[0];
		private int weight;

		public Item() {
			this(Integer.MAX_VALUE);
		}

		private Item(int initialWeight) {
			weight = initialWeight;
		}

		@Override
		public final String toString() {
			String[] recipes = new String[consumers.length];
			for (byte i = 0; i != consumers.length; ++i)
				recipes[i] = consumers[i].name;
			return String.format("(W:%d, C:%s)", weight, Arrays.toString(recipes));
		}
	}

	private static final record Recipe(String name, HashMap<Item, Float> input, String machine,
			HashMap<Item, Float> output) {
	}
}