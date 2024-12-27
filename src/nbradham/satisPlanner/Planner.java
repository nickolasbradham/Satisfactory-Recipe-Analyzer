package nbradham.satisPlanner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;

final class Planner {
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

	public static void main(String[] args) {
		Scanner scan = new Scanner(Planner.class.getResourceAsStream("/recipes.tsv")).useDelimiter("\t|\r\n");
		scan.nextLine();
		HashMap<String, Recipe[]> recipesByIn = new HashMap<>();
		while (scan.hasNextLine()) {
			Recipe recipe = new Recipe(scan.next(), parseList(scan.next()), scan.next(), parseList(scan.next()));
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
		}
		scan.close();
		System.out.println("Recipies by input:");
		recipesByIn.forEach((key, val) -> System.out.printf("\t%27s: %s%n", key, Arrays.toString(val)));
		scan = new Scanner(Planner.class.getResourceAsStream("/machines.tsv")).useDelimiter("\t|\n");
		scan.nextLine();
		HashMap<String, Short> machPow = new HashMap<>();
		while (scan.hasNextLine())
			machPow.put(scan.next(), scan.nextShort());
		scan.close();
		System.out.printf("Machines: %s%n", machPow);
		scan = new Scanner(Planner.class.getResourceAsStream("/rates.tsv")).useDelimiter("\t|\n");
		scan.nextLine();
		HashMap<String, Integer> rates = new HashMap<>();
		Queue<String> procQue = new LinkedList<>();
		HashSet<String> qHash = new HashSet<>();
		HashMap<String, Float> weights = new HashMap<>();
		while (scan.hasNextLine()) {
			String item = scan.next();
			int rate = scan.nextInt();
			rates.put(item, rate);
			qHash.add(item);
			procQue.offer(item);
			weights.put(item, 10000f / rate);
		}
		scan.close();
		System.out.printf("Rates: %s%nWeights: %s%n", rates, weights);
		HashMap<String, Recipe> bestRec = new HashMap<>();
		while (!procQue.isEmpty()) {
			String calcI = procQue.poll();
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
						float iweight = sum / ent.getValue();
						Float curWeight = weights.get(item);
						if ((curWeight == null || (iweight < curWeight || (iweight == curWeight
								&& machPow.get(recipe.machine) < machPow.get(bestRec.get(item).machine))))
								&& !rates.containsKey(item)) {
							weights.put(item, iweight);
							bestRec.put(item, recipe);
							if (qHash.add(item))
								procQue.offer(item);
							System.out.printf("Best Recipes updated (%s):%n", item);
							bestRec.forEach((k, v) -> System.out.printf("%26s (%f): %s%n", k, weights.get(k), v));
						}
					}
					System.out.printf("Weights: %s%n", weights);
				}
		}
	}

	private static final record Recipe(String name, HashMap<String, Float> ins, String machine,
			HashMap<String, Float> outs) {
	}
}