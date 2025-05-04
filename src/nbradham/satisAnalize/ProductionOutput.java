package nbradham.satisAnalize;

final class ProductionOutput implements ItemConsumer {

	private final String[] items;

	ProductionOutput(String outputItem) {
		items = new String[] { outputItem };
	}

	@Override
	public ItemSystem copy() {
		return new ProductionOutput(items[0]);
	}

	@Override
	public float getConsumeRate() {
		return 1;
	}

	@Override
	public String[] getInputs() {
		return items;
	}
}