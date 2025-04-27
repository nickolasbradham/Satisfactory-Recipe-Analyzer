package nbradham.satisAnalize;

final record ExtractionProducer(Item item, int weight) implements Source {

	@Override
	public float getWeight(Item targetItem) {
		return weight;
	}
}