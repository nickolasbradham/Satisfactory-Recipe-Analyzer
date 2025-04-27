package nbradham.satisAnalize;

import java.util.HashMap;
import java.util.Map.Entry;

final record RecipeProducer(Recipe recipe, HashMap<Item, Source> sources) implements Source {

	@Override
	public float getWeight(Item targetItem) {
		int sum = 0;
		for (Entry<Item, Source> entry : sources.entrySet()) {
			Item item = entry.getKey();
			sum += sources.get(item).getWeight(item) * recipe.inputs().get(item);
		}
		return sum / recipe.outputs().get(targetItem);
	}
}