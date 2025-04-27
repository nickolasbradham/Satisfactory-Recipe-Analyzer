package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

final class Analyzer {

	private static final Source SRC_ZERO = () -> 0;

	private final HashMap<String, Item> items = new HashMap<>();
	private final HashSet<Item> queSet = new HashSet<>();
	Node head = null, last = null;

	private final void start() throws FileNotFoundException {
		Scanner scan = createScanner("rates.tsv");
		int max = 0;
		while (scan.hasNext()) {
			final Item item = getItem(scan.next());
			final int rate = scan.nextInt();
			item.setPrimarySource(() -> rate);
			if (rate > max)
				max = rate;
			enqueue(item);
		}
		Node n = head;
		while (n != null) {
			final int calc = max / n.item.getPrimarySource().getWeight();
			n.item.setPrimarySource(() -> calc);
			n = n.next;
		}
		scan = createScanner("recipes.tsv");
		while (scan.hasNext()) {
			Recipe r = new Recipe(scan.next(), parseItems(scan.next()), scan.next(), parseItems(scan.next()));
			r.inputs().keySet().forEach(i -> {
				final Recipe[] consumers = i.getConsumers();
				final int l = consumers.length;
				Recipe[] recs = Arrays.copyOf(consumers, l + 1);
				recs[l] = r;
				i.setConsumers(recs);
			});
			if (r.inputs().size() == 0)
				r.outputs().keySet().forEach(i -> {
					i.setPrimarySource(SRC_ZERO);
					enqueue(i);
				});
		}
		while (head != null) {
			head.item.getOptions().forEach((k, v) -> {
				for (Recipe r : head.item.getConsumers()) {
					// TODO Hmmm...
				}
			});
			head = head.next;
		}
	}

	private final HashMap<Item, Float> parseItems(final String parse) {
		final HashMap<Item, Float> map = new HashMap<>();
		if (!parse.isBlank())
			for (final String split : parse.split(", ")) {
				final int x = split.indexOf('x');
				map.put(getItem(split.substring(x + 1)), Float.parseFloat(split.substring(0, x)));
			}
		return map;
	}

	private final Item getItem(String name) {
		Item i = items.get(name);
		if (i == null)
			items.put(name, i = new Item());
		return i;
	}

	private final void enqueue(Item item) {
		if (!queSet.contains(item)) {
			queSet.add(item);
			final Node n = new Node(item);
			if (head == null)
				last = head = n;
			else
				last = last.next = n;
		}
	}

	private static final Scanner createScanner(final String filename) throws FileNotFoundException {
		return new Scanner(new File(filename)).useDelimiter("\t|\r*\n").skip(".*\r*\n");
	}

	public static final void main(final String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}

	private static final class Node {
		private final Item item;
		private Node next;

		private Node(Item setItem) {
			item = setItem;
		}
	}
}