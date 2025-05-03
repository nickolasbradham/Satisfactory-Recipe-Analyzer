package nbradham.satisAnalize.sources;

import nbradham.satisAnalize.Item;

public final class ExtractionProducer extends AbstractSource {

	public ExtractionProducer(final Item item, final float weight) {
		weights.put(item, weight);
	}
}