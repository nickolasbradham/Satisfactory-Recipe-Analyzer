package nbradham.satisAnalize;

import java.util.Arrays;
import java.util.HashMap;

import nbradham.satisAnalize.sources.Source;
import nbradham.satisAnalize.sources.SourcePointer;

public final class Item {

	private final HashMap<Item, Source> optionsByByprod = new HashMap<>();

	private Recipe[] consumers = new Recipe[0];

	Item() {
		optionsByByprod.put(null, new SourcePointer());
	}

	final Source getPrimarySource() {
		return ((SourcePointer) optionsByByprod.get(null)).getSource();
	}

	final void setPrimarySource(final Source setSrc) {
		((SourcePointer) optionsByByprod.get(null)).setSource(setSrc);
	}

	final Recipe[] getConsumers() {
		return consumers;
	}

	final void addConsumer(final Recipe recipe) {
		final int l = consumers.length;
		final Recipe[] recs = Arrays.copyOf(consumers, l + 1);
		recs[l] = recipe;
		consumers = recs;
	}
}