package nbradham.satisAnalyze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

final class Analyzer {

	private final RecipeHandler recipeHandler;
	private final ProducerRegistry producerRegistry = new ProducerRegistry();

	private Analyzer() throws FileNotFoundException {
		recipeHandler = new RecipeHandler();
	}

	private final void start() throws FileNotFoundException {
		loadRawProducers();
	}

	private void loadRawProducers() throws FileNotFoundException {
		Scanner scan = new Scanner(new FileReader("resources.tsv")).useDelimiter("\t|\n");
		scan.nextLine();
		ArrayList<Object[]> maxRates = new ArrayList<>();
		int rate, max = -1;
		while (scan.hasNextLine()) {
			maxRates.add(new Object[] { scan.next(), rate = scan.nextInt() });
			max = Math.max(max, rate);
			scan.nextLine();
		}
		scan.close();
		for (Object[] i : maxRates)
			producerRegistry.register(new RawProducer((String) i[0], (int) i[1] == -1 ? 0 : max / (int) i[1]));
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Analyzer().start();
	}
}