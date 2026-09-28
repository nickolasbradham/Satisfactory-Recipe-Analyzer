package nbradham.satisAnalyze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

final class RecipeHandler {

	private static final HashMap<String, ArrayList<Recipe>> INDEX_BY_INPUT = new HashMap<>();

	RecipeHandler() throws FileNotFoundException {
		loadRecipes();
	}

	private final void loadRecipes() throws FileNotFoundException {
		Scanner scan = new Scanner(new FileReader("recipes.tsv"));
		scan.nextLine();
		while (scan.hasNextLine()) {
			Recipe recipe = Recipe.parseRecipe(scan.nextLine());
			for (Object[] ins : recipe.inputs()) {
				ArrayList<Recipe> arr = INDEX_BY_INPUT.get((String) ins[0]);
				if (arr == null)
					INDEX_BY_INPUT.put((String) ins[0], arr = new ArrayList<Recipe>());
				arr.add(recipe);
			}
		}
		scan.close();
		INDEX_BY_INPUT.forEach((k,v)->System.out.printf("%-22s %s%n", k, v.stream().map(r->r.name()).collect(Collectors.joining(", "))));
	}
}