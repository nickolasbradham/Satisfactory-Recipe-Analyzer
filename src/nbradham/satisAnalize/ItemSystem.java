package nbradham.satisAnalize;

sealed interface ItemSystem permits ItemConsumer, ItemProducer {

	ItemSystem copy();
}