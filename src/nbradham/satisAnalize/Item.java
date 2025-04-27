package nbradham.satisAnalize;

import java.util.HashMap;

final class Item {

	static final Source NO_SOURCE = () -> Integer.MAX_VALUE;

	private final HashMap<Item, Source> options = new HashMap<>();

	private Recipe[] consumers = new Recipe[0];

	Item() {
		options.put(null, new SourcePointer());
	}

	final HashMap<Item, Source> getOptions() {
		return options;
	}

	final Source getPrimarySource() {
		return options.get(null);
	}

	final void setPrimarySource(final Source setSrc) {
		((SourcePointer) options.get(null)).src = setSrc;
	}

	final Recipe[] getConsumers() {
		return consumers;
	}

	final void setConsumers(final Recipe[] setConsumers) {
		consumers = setConsumers;
	}

	private static final class SourcePointer implements Source {

		private Source src = NO_SOURCE;

		@Override
		public int getWeight() {
			return src.getWeight();
		}
	}
}