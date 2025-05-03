package nbradham.satisAnalize;

import java.util.HashMap;

public record Recipe(String name, HashMap<Item, Float> inputs, String machine, HashMap<Item, Float> outputs) {
}