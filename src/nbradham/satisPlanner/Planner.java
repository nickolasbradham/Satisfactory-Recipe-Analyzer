package nbradham.satisPlanner;

import java.util.Arrays;
import java.util.HashMap;
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
		HashMap<String, Recipe[]> rsByOut = new HashMap<>();
		while (s.hasNextLine()) {
			Recipe r = new Recipe(s.next(), parseList(s.next()), s.next(), parseList(s.next()));
			r.outs.keySet().forEach(o -> {
				Recipe[] rs = rsByOut.get(o);
				if (rs == null)
					rsByOut.put(o, new Recipe[] { r });
				else {
					int l = rs.length;
					rs = Arrays.copyOf(rs, l + 1);
					rs[l] = r;
					rsByOut.put(o, rs);
				}
			});
		}
		s.close();
		System.out.println("Recipies by input:");
		rsByOut.forEach((k, v) -> System.out.printf("\t%27s: %s%n", k, Arrays.toString(v)));
		s = new Scanner(Planner.class.getResourceAsStream("/machines.tsv")).useDelimiter("\t|\r\n");
		s.nextLine();
		HashMap<String, Short> machines = new HashMap<>();
		while (s.hasNextLine())
			machines.put(s.next(), s.nextShort());
		s.close();
		System.out.printf("Machines: %s%n", machines);
		s = new Scanner(Planner.class.getResourceAsStream("/rates.tsv")).useDelimiter("\t|\r\n");
		s.nextLine();
		HashMap<String, Integer> rates = new HashMap<>();
		while (s.hasNextLine())
			rates.put(s.next(), s.nextInt());
		s.close();
		System.out.printf("Rates: %s%n", rates);
	}

	private static final record Recipe(String name, HashMap<String, Float> ins, String machine,
			HashMap<String, Float> outs) {
	}
}