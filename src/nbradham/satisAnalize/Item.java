package nbradham.satisAnalize;

import java.util.Arrays;

import nbradham.satisAnalize.sources.Source;
import nbradham.satisAnalize.sources.SourcePointer;

public final class Item {

	private final Source[] options;

	private Recipe[] consumers = new Recipe[0];

	Item() {
		options = new Source[] { new SourcePointer() };
	}

	final Source[] getOptions() {
		return options;
	}

	final Source getPrimarySource() {
		return ((SourcePointer) options[0]).getSource();
	}

	final void setPrimarySource(final Source setSrc) {
		((SourcePointer) options[0]).setSource(setSrc);
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