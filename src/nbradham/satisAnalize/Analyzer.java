package nbradham.satisAnalize;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

final class Analyzer {

	private static final HashMap<String, Float> parseItems(String parse) {
		final HashMap<String, Float> map = new HashMap<>();
		if (!parse.isEmpty())
			for (String split : parse.split(", ")) {
				final int x = split.indexOf('x');
				map.put(split.substring(x + 1), Float.parseFloat(split.substring(0, x)));
			}
		return map;
	}

	private static final Scanner createScanner(String filepath) throws FileNotFoundException {
		return new Scanner(new File(filepath)).useDelimiter("\t|\r*\n").skip(".*\r*\n");
	}

	public static final void main(final String[] args) throws FileNotFoundException {
		Scanner scan = createScanner("recipes.tsv");
		final HashMap<String, ArrayList<Recipe>> recipesByOut = new HashMap<>();
		while (scan.hasNext()) {
			final Recipe recipe = new Recipe(scan.next(), parseItems(scan.next()), scan.next(),
					parseItems(scan.next()));
			recipe.outputs().keySet().forEach(out -> {
				ArrayList<Recipe> recipes = recipesByOut.get(out);
				if (recipes == null)
					recipesByOut.put(out, recipes = new ArrayList<>());
				recipes.add(recipe);
			});
		}
		scan.close();
		scan = createScanner("rates.tsv");
		final HashMap<String, Integer> weights = new HashMap<>();
		int max = Integer.MIN_VALUE;
		while (scan.hasNext()) {
			final int scanWeight;
			weights.put(scan.next(), scanWeight = scan.nextInt());
			if (scanWeight > max)
				max = scanWeight;
		}
		final int finMax = max;
		weights.forEach((item, rate) -> weights.put(item, finMax / rate));
		recipesByOut.forEach((out, recipes) -> {
			final ArrayList<ItemSystem> itemSystems = new ArrayList<>();
			final HashMap<String, HashSet<ItemConsumer>> consumersByItem = new HashMap<>();
			final HashMap<String, HashSet<ItemProducer>> producersByItem = new HashMap<>(),
					byproducersByItem = new HashMap<>();
			final ItemConsumer output = new ProductionOutput(out);
			itemSystems.add(output);
			final HashSet<ItemConsumer> outConsume = new HashSet<>();
			outConsume.add(output);
			consumersByItem.put(out, outConsume);
			recipes.forEach(recipe -> {
				final ArrayList<ItemSystem> nextItemSystems = new ArrayList<>();
				final HashMap<String, HashSet<ItemConsumer>> nextConsumersByItem = new HashMap<>();
				consumersByItem.keySet().forEach(item -> nextConsumersByItem.put(item, new HashSet<>()));
				final HashMap<String, HashSet<ItemProducer>> nextProducersByItem = new HashMap<>(),
						nextByproducersByItem = new HashMap<>();
				producersByItem.keySet().forEach(item -> nextProducersByItem.put(item, new HashSet<>()));
				byproducersByItem.keySet().forEach(item -> nextByproducersByItem.put(item, new HashSet<>()));
				itemSystems.forEach(sys -> {
					ItemSystem copy = sys.copy();
					nextItemSystems.add(copy);
					if (sys instanceof ItemConsumer)
						for (String in : ((ItemConsumer) sys).getInputs())
							nextConsumersByItem.get(in).add((ItemConsumer) sys);
					if (sys instanceof ItemProducer)
						for (String prod : ((ItemProducer) sys).getOutputs())
							if (producersByItem.get(prod).contains(sys))
								nextProducersByItem.get(prod).add((ItemProducer) copy);
							else
								nextByproducersByItem.get(prod).add((ItemProducer) copy);
				});
			});
		});
	}
}