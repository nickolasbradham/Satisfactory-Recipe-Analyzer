package nbradham.satisAnalize;

import java.util.Arrays;

import nbradham.satisAnalize.sources.Source;

public final class Item {

	static final Source NO_SOURCE = i -> Integer.MAX_VALUE;

	private final Source[] options;

	private Recipe[] consumers = new Recipe[0];

	Item() {
		options = new Source[] { new SourcePointer() };
	}

	final Source[] getOptions() {
		return options;
	}

	final Source getPrimarySource() {
		return ((SourcePointer) options[0]).src;
	}

	final void setPrimarySource(final Source setSrc) {
		((SourcePointer) options[0]).src = setSrc;
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

	private static final class SourcePointer implements Source {

		private Source src = NO_SOURCE;

		@Override
		public final float getWeight(Item targetItem) {
			return src.getWeight(targetItem);
		}
	}
}