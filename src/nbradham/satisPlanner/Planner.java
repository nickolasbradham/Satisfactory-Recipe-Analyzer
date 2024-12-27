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
		for (String os : s.split(", ")) {
			System.out.printf("Parsing \"%s\"...%n", os);
			int x = os.indexOf('x');
			if (x != -1)
				map.put(os.substring(x + 1), Float.parseFloat(os.substring(0, x)));
		}
		return map;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(Planner.class.getResourceAsStream("/recipes.tsv")).useDelimiter("\t|\r\n");
		s.nextLine();
		HashMap<String, Recipe[]> rsByIn = new HashMap<>();
		while (s.hasNextLine()) {
			Recipe r = new Recipe(s.next(), parseList(s.next()), s.next(), parseList(s.next()));
			r.ins.keySet().forEach(o -> {
				Recipe[] rs = rsByIn.get(o);
				if (rs == null)
					rsByIn.put(o, new Recipe[] { r });
				else {
					int l = rs.length;
					rs = Arrays.copyOf(rs, l + 1);
					rs[l] = r;
					rsByIn.put(o, rs);
				}
			});
		}
		s.close();
		System.out.println("Recipies by input:");
		rsByIn.forEach((k, v) -> System.out.printf("\t%27s: %s%n", k, Arrays.toString(v)));
		s = new Scanner(Planner.class.getResourceAsStream("/machines.tsv")).useDelimiter("\t|\n");
		s.nextLine();
		HashMap<String, Short> machines = new HashMap<>();
		while (s.hasNextLine()) {
			String st = s.next();
			machines.put(st, s.nextShort());
		}
		s.close();
		System.out.printf("Machines: %s%n", machines);
		s = new Scanner(Planner.class.getResourceAsStream("/rates.tsv")).useDelimiter("\t|\n");
		s.nextLine();
		HashMap<String, Integer> rates = new HashMap<>();
		Queue<String> q = new LinkedList<>();
		HashSet<String> qHash = new HashSet<>();
		HashMap<String, Float> iWeight = new HashMap<>();
		while (s.hasNextLine()) {
			String i = s.next();
			int r = s.nextInt();
			rates.put(i, r);
			qHash.add(i);
			q.offer(i);
			iWeight.put(i, 10000f / r);
		}
		s.close();
		System.out.printf("Rates: %s%nWeights: %s%n", rates, iWeight);
		HashMap<String, Recipe> bestRec = new HashMap<>();
		while (!q.isEmpty()) {
			String calcI = q.poll();
			qHash.remove(calcI);
			rLoop: for (Recipe r : rsByIn.get(calcI)) {
				float sum = 0;
				for (Entry<String, Float> e : r.ins.entrySet()) {
					Float iw = iWeight.get(e.getKey());
					if (iw == null)
						continue rLoop;
					sum += iw * e.getValue();
				}
				System.out.printf("%f: %s%n", sum, r);
				for (Entry<String, Float> e : r.outs.entrySet()) {
					String it = e.getKey();
					float iw = sum / e.getValue();
					Float c = iWeight.get(it);
					if ((c == null
							|| (iw < c || (iw == c && machines.get(r.machine) < machines.get(bestRec.get(it).machine))))
							&& !rates.containsKey(it)) {
						iWeight.put(it, iw);
						bestRec.put(it, r);
						System.out.println("Best Recipes updated:");
						bestRec.forEach((k, v) -> System.out.printf("%26s (%f): %s%n", k, iWeight.get(k), v));
					}
				}
				System.out.printf("Weights: %s%n", iWeight);
			}
		}
	}

	private static final record Recipe(String name, HashMap<String, Float> ins, String machine,
			HashMap<String, Float> outs) {
	}
}