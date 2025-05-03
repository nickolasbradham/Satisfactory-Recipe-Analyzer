package nbradham.satisAnalize.sources;

import nbradham.satisAnalize.Item;

@FunctionalInterface
public interface Source {

	float getWeight(Item targetOutput);
}