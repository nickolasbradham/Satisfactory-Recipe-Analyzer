package nbradham.satisAnalize.sources;

import java.util.HashMap;

import nbradham.satisAnalize.Item;

public abstract sealed class AbstractSource implements Source permits ExtractionProducer, RecipeProducer {

	protected static final HashMap<Item, Float> weights = new HashMap<>();

	@Override
	public final float getWeight(Item targetOutput) {
		return weights.get(targetOutput);
	}

	public HashMap<Item, Float> calcOutWeights() {
		return weights;
	}
}