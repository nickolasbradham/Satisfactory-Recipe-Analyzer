package nbradham.satisAnalyze;

import java.util.ArrayList;
import java.util.Arrays;

record Recipe(String name, Object[][] inputs, Object[][] outputs, String machine, float time) {

	static Recipe parseRecipe(String string) {
		String[] parts = string.split("\t");
		return new Recipe(parts[0], getItems(parts[1]), getItems(parts[2]), parts[3], Float.parseFloat(parts[4]));
	}

	private static Object[][] getItems(String string) {
		ArrayList<Object[]> items = new ArrayList<>();
		for (String s : string.split(", ")) {
			int qSplit = s.indexOf(' ');
			items.add(new Object[] { s.substring(qSplit+1), Short.parseShort(s.substring(0, qSplit)) });
		}
		return items.toArray(new Object[0][2]);
	}

	@Override
	public final String toString() {
		return String.format("Recipe[name=%s, inputs=%s, outputs=%s, machine=%s, time=%.0f]", name,
				Arrays.deepToString(inputs), Arrays.deepToString(outputs), machine, time);
	}
}