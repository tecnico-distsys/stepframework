package step.test;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *  This class contains text file utility methods.
 */
public class TextFileUtil {

    //
    //  lineOf
    //

    /**
     *  Returns the line number within the file
     *  of the first occurrence of the specified substring.
     */
    public static int lineOf(String file, String str) throws IOException {
        return lineOf(file, str, 0);
    }

    /**
     *  Returns the line number within the file
     *  of the first occurrence of the specified substring,
     *  starting at the specified line.
     */
    public static int lineOf(String file, String str, int fromLine) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = null;
            int lineNr = 0;

            while((line = br.readLine()) != null) {

                if(lineNr >= fromLine) {
                    int strIndex = line.indexOf(str);
                    if(strIndex != -1)
                        return lineNr;
                }

                lineNr++;
            }
        } finally {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }

        // return not found
        return -1;
    }

    /**
     *  Returns the line number within the file
     *  of the first occurrence of the specified substring.
     */
    public static int lineOfPattern(String file, String patternText) throws IOException {
        return lineOfPattern(file, patternText, 0);
    }


    /**
     *  Returns the line number within the file
     *  of the first match of the specified pattern,
     *  starting at the specified line.
     */
    public static int lineOfPattern(String file, String patternText, int fromLine) throws IOException {
        Pattern pattern = Pattern.compile(patternText);

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = null;
            int lineNr = 0;

            while((line = br.readLine()) != null) {
                if(lineNr >= fromLine) {
                    boolean matches = pattern.matcher(line).matches();
                    if(matches)
                        return lineNr;
                }

                lineNr++;
            }
        } finally {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }

        // return not found
        return -1;
    }


    //
    //  lastLineOf
    //

    /**
     *  Returns the line number within the file
     *  of the last occurrence of the specified substring.
     */
    public static int lastLineOf(String file, String str) throws IOException {
        return lastLineOf(file, str, 0);
    }

    /**
     *  Returns the line number within the file
     *  of the last occurrence of the specified substring,
     *  starting at the specified line.
     */
    public static int lastLineOf(String file, String str, int fromLine) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;

        int foundAtLineNr = -1;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = null;
            int lineNr = 0;

            while((line = br.readLine()) != null) {

                if(lineNr >= fromLine) {
                    int strIndex = line.indexOf(str);
                    if(strIndex != -1)
                        foundAtLineNr = lineNr;
                }

                lineNr++;
            }
        } finally {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }

        // return last found line number
        return foundAtLineNr;
    }

    /**
     *  Returns the line number within the file
     *  of the last match of the specified pattern.
     */
    public static int lastLineOfPattern(String file, String patternText) throws IOException {
        return lastLineOfPattern(file, patternText, 0);
    }

    /**
     *  Returns the line number within the file
     *  of the last match of the specified pattern,
     *  starting at the specified line.
     */
    public static int lastLineOfPattern(String file, String patternText, int fromLine) throws IOException {
        Pattern pattern = Pattern.compile(patternText);

        FileReader fr = null;
        BufferedReader br = null;

        int foundAtLineNr = -1;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);

            String line = null;
            int lineNr = 0;

            while((line = br.readLine()) != null) {

                if(lineNr >= fromLine) {
                    boolean matches = pattern.matcher(line).matches();
                    if(matches)
                        foundAtLineNr = lineNr;
                }

                lineNr++;
            }
        } finally {
            if(br != null)
                br.close();
            if(fr != null)
                fr.close();
        }

        // return last found line number
        return foundAtLineNr;
    }


    /**
     *  Returns the total number of lines within the file.
     */
    public static int lineCount(String file) throws IOException {
        BufferedReader reader = null;
        int lines = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            while (reader.readLine() != null)
                lines++;
        } finally {
            if(reader != null)
                reader.close();
        }
        return lines;
    }

    /**
     *  Returns the total number of lines within the file that match the pattern.
     */
    public static int lineCountPattern(String file, String patternText) throws IOException {
        Pattern pattern = Pattern.compile(patternText);

        BufferedReader reader = null;
        int lineCounter = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = null;
            while((line = reader.readLine()) != null) {
                boolean matches = pattern.matcher(line).matches();
                if(matches) {
                    lineCounter++;
                }
            }
        } finally {
            if(reader != null)
                reader.close();
        }
        return lineCounter;
    }

}
