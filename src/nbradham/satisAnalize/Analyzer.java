package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

final class Analyzer {

	private final HashMap<String, Item> items = new HashMap<>();
	Node head = null, last = null;

	private final void start() throws FileNotFoundException {
		Scanner scan = createScanner("rates.tsv");
		final HashMap<Item, Integer> rates = new HashMap<>();
		int max = 0;
		while (scan.hasNext()) {
			final int rate;
			rates.put(getItem(scan.next()), rate = scan.nextInt());
			if (rate > max)
				max = rate;
		}
		final int fMax = max;
		rates.forEach((i, r) -> enqueue(new ExtractionProducer(i, fMax / r)));
		scan = createScanner("recipes.tsv");
		while (scan.hasNext()) {
			Recipe recipe = new Recipe(scan.next(), parseItems(scan.next()), scan.next(), parseItems(scan.next()));
			recipe.inputs().keySet().forEach(i -> {
				final Recipe[] consumers = i.getConsumers();
				final int l = consumers.length;
				Recipe[] recs = Arrays.copyOf(consumers, l + 1);
				recs[l] = recipe;
				i.setConsumers(recs);
			});
			if (recipe.inputs().size() == 0)
				recipe.outputs().keySet().forEach(i -> enqueue(new RecipeProducer(recipe, new HashMap<>())));
		}
		while (head != null) {
			// TODO continue.
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

	private final void enqueue(Source item) {
		final Node n = new Node(item);
		if (head == null)
			last = head = n;
		else
			last = last.next = n;
	}

	private static final Scanner createScanner(final String filename) throws FileNotFoundException {
		return new Scanner(new File(filename)).useDelimiter("\t|\r*\n").skip(".*\r*\n");
	}

	public static final void main(final String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}

	private static final class Node {
		private final Source src;
		private Node next;

		private Node(Source source) {
			src = source;
		}
	}
}