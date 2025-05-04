package nbradham.satisAnalize;

non-sealed interface ItemConsumer extends ItemSystem{

	float getConsumeRate();
	
	String[] getInputs();
}