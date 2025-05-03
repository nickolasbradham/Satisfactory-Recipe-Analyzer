package nbradham.satisAnalize.sources;

import nbradham.satisAnalize.Item;

public final record ExtractionProducer(Item item, int weight) implements Source {

	@Override
	public final float getWeight(Item targetItem) {
		return weight;
	}
}