package utilities;

import common.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by aaronbriel on 9/27/16.
 */
public class CommonUtils {

    private static Logger LOGGER = Logger.getLogger(CommonUtils.class.getName());

    /**
     * Returns an expiration year
     *
     * @param year The number of years to increment
     * @return Integer of calendar year
     */
    public int getExpYear(int year) {
        return Calendar.getInstance().get(Calendar.YEAR) + year;
    }


    /**
     * Returns the substring of a string between delimiters
     *
     * @param text       The text to pull the string from
     * @param delimiter1 The string preceding the string to extract
     * @param delimiter2 The string following the string to extract
     * @return String
     */
    public static String getSubstring(String text, String delimiter1, String delimiter2) {
        return text.substring(text.indexOf(delimiter1) + delimiter1.length(), text.indexOf(delimiter2));
    }


    /**
     * Creates a new file with fileName if it does not exist, then adds
     * or appends text to it
     *
     * @param fileName The file to create and/or append
     * @param text     The text to append to the file
     */
    public static void appendFile(String fileName, String text) {
        try {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            LOGGER.info("IOException: " + e.getMessage());
        }
    }


    /**
     * Checks if the strings in an array are sorted
     *
     * @param array      The array to check
     * @return boolean  Whether the array was sorted
     */
    public static boolean isArraySorted(String[] array) {
        String previous = "";
        for (final String current: array) {
            if (current.compareTo(previous) < 0)
                return false;
            previous = current;
        }
        return true;
    }


    /**
     * Checks if the strings in a list are sorted
     *
     * @param list      The list to check
     * @return boolean  Whether the array was sorted
     */
    public static boolean isListSorted(ArrayList<String> list) {
        String previous = "";
        for (final String current: list) {
            if (current.compareTo(previous) < 0)
                return false;
            previous = current;
        }
        return true;
    }

}