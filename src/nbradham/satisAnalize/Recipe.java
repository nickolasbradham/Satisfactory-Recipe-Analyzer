package nbradham.satisAnalize;

import java.util.HashMap;

record Recipe(String name, HashMap<Item, Float> inputs, String machine, HashMap<Item, Float> outputs) {
}