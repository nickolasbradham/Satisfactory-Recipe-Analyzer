package nbradham.satisAnalize.sources;

import nbradham.satisAnalize.Item;

sealed public interface Source permits AbstractSource, SourcePointer {
	float getWeight(Item targetOutput);
}