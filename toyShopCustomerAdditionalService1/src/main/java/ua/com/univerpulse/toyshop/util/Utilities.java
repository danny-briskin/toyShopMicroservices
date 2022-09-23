package ua.com.univerpulse.toyshop.util;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Danny Briskin (DBriskin@qaconsultants.com)
 * for toyshopmicroservices project.
 */
public class Utilities {
    private static final char[] CONSONANTS = {'q', 'w', 'r', 't', 'p', 's', 'd', 'f', 'g', 'h'
            , 'k', 'j', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
    private static final char[] VOWELS = {'e', 'y', 'u', 'i', 'o', 'a'};

    /**
     * Returns random "readable" name of given length. Starting from capital consonant
     *
     * @param length length
     * @return name
     */
    public static String createName(int length) {
        String[] resultName = new String[length];
        for (int i = 0; i < length; i = i + 2) {
            resultName[i] = RandomStringUtils.random(1, CONSONANTS);
        }
        for (int i = 1; i < length; i = i + 2) {
            resultName[i] = RandomStringUtils.random(1, VOWELS);
        }
        return StringUtils.capitalize(String.join("", resultName));
    }
}
