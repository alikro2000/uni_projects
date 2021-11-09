package Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NameUtils {
    static Random randomGenerator = new Random();
    private static boolean hasName = true;
    static List<String> namesRepo = new ArrayList<String>() {{
        add("The Jackdaw");
        add("Dawn of the Dead");
        add("Black Pearl");
        add("Pharaoh");
        add("HMS Edmond");
        add("Bourbon");
        add("Grand Cadeau");
        add("Mighty Uranus");
        add("Kraken");
        add("Queen Anne's Revenge");
    }};
    static int index = namesRepo.size() + 1;

    public static boolean getHasName() {
        return hasName;
    }

    /**
     * Non-repetitive names.
     *
     * @return A unique name or a number (if ran out of unique names).
     */
    public static String getNextName() {
        if (!hasName)
            return String.format("%d", index++);
        //else

        int index = randomGenerator.nextInt(namesRepo.size());
//        System.out.println(index);
        String result = namesRepo.remove(index);
        hasName = (namesRepo.size() != 0);
        return result;
    }
}
