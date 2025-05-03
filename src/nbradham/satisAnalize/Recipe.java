package nbradham.satisAnalize;

import java.util.HashMap;

record Recipe(String name, HashMap<String, Float> inputs, String machine, HashMap<String, Float> outputs) {
}