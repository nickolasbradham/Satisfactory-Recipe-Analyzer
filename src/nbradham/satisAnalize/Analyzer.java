package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
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
			Item i = new Item(name, weight);
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
				r.output.keySet().forEach(k -> {
					k.weight = 0;
					k.best = r;
				});
			else
				r.input.keySet().forEach(k -> {
					int l = k.consumers.length;
					k.consumers = Arrays.copyOf(k.consumers, l + 1);
					k.consumers[l] = r;
				});
			r.output.keySet().forEach(i -> {
				if (i.weight == Float.MAX_VALUE)
					i.best = r;
			});
		}
		scan.close();
		System.out.println("Items:");
		ITEMS.forEach((k, v) -> System.out.printf("\t%s: %s%n", k, v));
		while (updateQueue.size() != 0) {
			consumers: for (Recipe rec : updateQueue.poll().consumers) {
				System.out.printf("Checking %s...%n", rec.name);
				float sum = 0;
				for (Entry<Item, Float> e : rec.input.entrySet()) {
					float w = e.getKey().weight;
					if (w == Float.MAX_VALUE)
						continue consumers;
					sum += w * e.getValue();
				}
				System.out.println("\tUpdating outputs...");
				for (Entry<Item, Float> e : rec.output.entrySet()) {
					float w = sum / e.getValue();
					Item i = e.getKey();
					if (i.weight > w) {
						System.out.printf("\t\t%s: %s, %.3f -> %.3f%n", i.name, i.best.name, i.weight, i.weight = w);
						i.best = rec;
						updateQueue.offer(i);
					}
				}
			}
		}
		System.out.println("Best Recipes:");
		ITEMS.forEach((k, v) -> System.out.printf("%s\t%s%n", k, v.best.name));
	}

	private final HashMap<Item, Float> parseItems(String str) {
		HashMap<Item, Float> items = new HashMap<>();
		if (str.length() != 0)
			for (String s : str.split(", ")) {
				int x = s.indexOf('x');
				String name = s.substring(x + 1);
				Item i = ITEMS.get(name);
				if (i == null)
					ITEMS.put(name, i = new Item(name));
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

		private static final Recipe NULL_RECIPE = new Recipe(null, null, null, null);

		private final String name;

		private Recipe[] consumers = new Recipe[0];
		private Recipe best = NULL_RECIPE;
		private float weight;

		public Item(String setName) {
			this(setName, Float.MAX_VALUE);
		}

		private Item(String setName, float initialWeight) {
			name = setName;
			weight = initialWeight;
		}

		@Override
		public final String toString() {
			String[] recipes = new String[consumers.length];
			for (byte i = 0; i != consumers.length; ++i)
				recipes[i] = consumers[i].name;
			return String.format("(W: %.3f, B: %s, C: %s)", weight, best.name, Arrays.toString(recipes));
		}
	}

	private static final record Recipe(String name, HashMap<Item, Float> input, String machine,
			HashMap<Item, Float> output) {
	}
}