package nbradham.satisAnalyze;

import java.io.FileNotFoundException;

final class Analyzer {

	private final RecipeHandler recipeHandler;

	private Analyzer() throws FileNotFoundException {
		recipeHandler = new RecipeHandler();
	}

	private final void start() {
		//TODO continue.
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}
}