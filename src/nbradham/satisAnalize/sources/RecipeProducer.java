package nbradham.satisAnalize.sources;

import java.util.HashMap;
import java.util.Map.Entry;

import nbradham.satisAnalize.Item;
import nbradham.satisAnalize.Recipe;

public final class RecipeProducer extends AbstractSource {

	private final Recipe recipe;
	private final HashMap<Item, Source> sources;

	public RecipeProducer(final Recipe setRecipe, final HashMap<Item, Source> setSources) {
		recipe = setRecipe;
		sources = setSources;
	}

	@Override
	public final HashMap<Item, Float> calcOutWeights() {
		int sum = 0;
		final HashMap<Item, Float> inputs = recipe.inputs();
		for (final Entry<Item, Source> entry : sources.entrySet()) {
			final Item item = entry.getKey();
			sum += sources.get(item).getWeight(item) * inputs.get(item);
		}
		final int fSum = sum;
		recipe.outputs().forEach((i, r) -> weights.put(i, fSum / r));
		return weights;
	}
}