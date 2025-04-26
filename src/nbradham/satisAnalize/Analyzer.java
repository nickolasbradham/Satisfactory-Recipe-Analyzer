package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

final class Analyzer {

	private final HashMap<String, Item> items = new HashMap<>();

	private final void start() throws FileNotFoundException {
		Scanner scan = createScanner("recipes.tsv");
		while (scan.hasNext()) {
			Recipe r = new Recipe(scan.next(), parseItems(scan.next()), scan.next(), parseItems(scan.next()));
			r.inputs.keySet().forEach(i -> {
				final int l = i.consumers.length;
				Recipe[] recs = Arrays.copyOf(i.consumers, l + 1);
				recs[l] = r;
				i.consumers = recs;
			});
		}
		scan.close();
		scan = createScanner("rates.tsv");
		int max = 0;
		Node head = null, last = null;
		final HashSet<Item> queSet = new HashSet<>();
		while (scan.hasNext()) {
			final Item item = items.get(scan.next());
			final int tMax = item.weight = scan.nextInt();
			if (tMax > max)
				max = tMax;
			if (!queSet.contains(item)) {
				queSet.add(item);
				final Node n = new Node(item);
				if (head == null)
					last = head = n;
				else
					last = last.next = n;
			}
		}
		Node n = head;
		while (n != null) {
			n.item.weight = max / n.item.weight;
			n = n.next;
		}
	}

	private final HashMap<Item, Float> parseItems(final String parse) {
		final HashMap<Item, Float> map = new HashMap<>();
		for (final String split : parse.split(", ")) {
			final int x = split.indexOf('x');
			if (x != -1) {
				final String name = split.substring(x + 1);
				Item i = items.get(name);
				if (i == null)
					items.put(name, i = new Item());
				map.put(i, Float.parseFloat(split.substring(0, x)));
			}
		}
		return map;
	}

	private static final Scanner createScanner(final String filename) throws FileNotFoundException {
		return new Scanner(new File(filename)).useDelimiter("\t|\r*\n").skip(".*\r*\n");
	}

	public static final void main(final String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}

	private static final record Recipe(String name, HashMap<Item, Float> inputs, String machine,
			HashMap<Item, Float> outputs) {
	}

	private static final class Node {
		private final Item item;
		private Node next;

		private Node(Item setItem) {
			item = setItem;
		}
	}

	private static final class Item {
		private Recipe[] consumers = new Recipe[0];
		private int weight = -1;
	}
}