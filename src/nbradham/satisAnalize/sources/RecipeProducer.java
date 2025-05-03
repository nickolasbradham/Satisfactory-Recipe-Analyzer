package nbradham.satisAnalize.sources;

import java.util.HashMap;
import java.util.Map.Entry;

import nbradham.satisAnalize.Item;
import nbradham.satisAnalize.Recipe;

public final record RecipeProducer(Recipe recipe, HashMap<Item, Source> sources) implements Source {

	@Override
	public final float getWeight(final Item targetItem) {
		int sum = 0;
		final HashMap<Item, Float> inputs = recipe.inputs();
		for (final Entry<Item, Source> entry : sources.entrySet()) {
			final Item item = entry.getKey();
			sum += sources.get(item).getWeight(item) * inputs.get(item);
		}
		return sum / recipe.outputs().get(targetItem);
	}
}