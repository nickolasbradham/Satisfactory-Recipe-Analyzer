package nbradham.satisAnalize;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;

final class Analyzer {

	private static final String DELIM = "\t|\r*\n";

	private final Queue<String> procQue = new LinkedList<>();
	private final HashSet<String> qHash = new HashSet<>();

	private void start() throws InterruptedException, IOException {
		Scanner scan = new Scanner(Analyzer.class.getResourceAsStream("/recipes.tsv")).useDelimiter(DELIM);
		scan.nextLine();
		HashMap<String, Recipe[]> recipesByIn = new HashMap<>();
		HashMap<String, Float> weights = new HashMap<>();
		while (scan.hasNextLine()) {
			Recipe recipe = new Recipe(scan.next(), parseList(scan.next()), scan.next(), parseList(scan.next()));
			scan.next();
			recipe.ins.keySet().forEach(o -> {
				Recipe[] recipes = recipesByIn.get(o);
				if (recipes == null)
					recipesByIn.put(o, new Recipe[] { recipe });
				else {
					int l = recipes.length;
					recipes = Arrays.copyOf(recipes, l + 1);
					recipes[l] = recipe;
					recipesByIn.put(o, recipes);
				}
			});
			if (recipe.ins.isEmpty())
				recipe.outs.keySet().forEach(item -> {
					forceToQueue(item);
					weights.put(item, Float.MIN_NORMAL);
				});
		}
		scan.close();
		System.out.println("Recipies by input:");
		recipesByIn.forEach((key, val) -> System.out.printf("\t%27s: %s%n", key, Arrays.toString(val)));
		scan = new Scanner(Analyzer.class.getResourceAsStream("/machines.tsv")).useDelimiter(DELIM);
		scan.nextLine();
		HashMap<String, Short> machPow = new HashMap<>();
		while (scan.hasNextLine()) {
			String in = scan.next();
			System.out.printf("Parsing %s...%n", in);
			machPow.put(in, scan.nextShort());
		}
		scan.close();
		System.out.printf("Machines: %s%n", machPow);
		scan = new Scanner(Analyzer.class.getResourceAsStream("/rates.tsv")).useDelimiter(DELIM);
		scan.nextLine();
		while (scan.hasNextLine()) {
			String item = scan.next();
			int rate = scan.nextInt();
			forceToQueue(item);
			weights.put(item, 10000f / rate);
		}
		scan.close();
		System.out.printf("Weights: %s%n", weights);
		HashMap<String, Recipe> bestRec = new HashMap<>();
		String calcI;
		while ((calcI = procQue.poll()) != null) {
			qHash.remove(calcI);
			System.out.printf("Calculating %s...%n", calcI);
			Recipe[] recipes = recipesByIn.get(calcI);
			if (recipes != null)
				rLoop: for (Recipe recipe : recipes) {
					float sum = 0;
					for (Entry<String, Float> ent : recipe.ins.entrySet()) {
						Float iweight = weights.get(ent.getKey());
						if (iweight == null)
							continue rLoop;
						sum += iweight * ent.getValue();
					}
					System.out.printf("%f: %s%n", sum, recipe);
					for (Entry<String, Float> ent : recipe.outs.entrySet()) {
						String item = ent.getKey();
						float iWeight = sum / ent.getValue();
						Float curWeight = weights.get(item);
						if ((curWeight == null || (iWeight < curWeight || (iWeight == curWeight
								&& machPow.get(recipe.machine) < machPow.get(bestRec.get(item).machine))))) {
							bestRec.put(item, recipe);
							if (qHash.add(item))
								procQue.offer(item);
							System.out.printf("Best Recipes updated (%s [%f -> %f]):%n", item,
									weights.put(item, iWeight), iWeight);
							bestRec.forEach((k, v) -> System.out.printf("%27s (%f): %s%n", k, weights.get(k), v));
						}
					}
					System.out.printf("Weights: %s%n", weights);
				}
		}
		PrintWriter writer = new PrintWriter(new FileWriter("Best Recipes.txt"));
		bestRec.forEach((k, v) -> writer.printf("%27s: %40s (%s > %s > %s)%n", k, v.name, v.ins, v.machine, v.outs));
		writer.close();
	}

	private void forceToQueue(String item) {
		procQue.offer(item);
		qHash.add(item);
	}

	private static HashMap<String, Float> parseList(String s) {
		HashMap<String, Float> map = new HashMap<>();
		for (String split : s.split(", ")) {
			System.out.printf("Parsing \"%s\"...%n", split);
			int i = split.indexOf('x');
			if (i != -1)
				map.put(split.substring(i + 1), Float.parseFloat(split.substring(0, i)));
		}
		return map;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		new Analyzer().start();
	}

	private static final record Recipe(String name, HashMap<String, Float> ins, String machine,
			HashMap<String, Float> outs) {
	}
}