package nbradham.satisAnalize;

@FunctionalInterface
interface Source {

	float getWeight(Item targetOutput);
}