package nbradham.satisAnalize;

final class Item {

	static final Source NO_SOURCE = i -> Integer.MAX_VALUE;

	private final Option[] options;

	private Recipe[] consumers = new Recipe[0];

	Item() {
		options = new Option[] { new Option(new SourcePointer()) };
	}

	final Option[] getOptions() {
		return options;
	}

	final Source getPrimarySource() {
		return options[0].source;
	}

	final void setPrimarySource(final Source setSrc) {
		((SourcePointer) options[0].source).src = setSrc;
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
		public float getWeight(Item targetItem) {
			return src.getWeight(targetItem);
		}
	}

	private static final record Option(Source source) {
	}
}