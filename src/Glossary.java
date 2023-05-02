import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Program that takes a file input and creates a glossary for all the terms and
 * their definitions in the form of HTML files.
 *
 * @author Varun Mangla
 */
public final class Glossary {

    /**
     * Compare {@code String}s in alphabetical order.
     */
    public static class CompareStrings implements Comparator<String> {
        @Override
        public int compare(String first, String second) {
            return first.compareTo(second);
        }
    }

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary() {
        // no code needed here
    }

    /**
     * Takes all the characters in the given {@code String} and stores them in
     * the given {@code Set}.
     *
     * @param str
     *            the {@code String} from which the characters are taken
     * @param charSet
     *            the {@code Set} which is replaced
     * @replaces charSet
     * @ensures charSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> charSet) {
        assert str != null : "Violation of: str is not null";
        assert charSet != null : "Violation of: charSet is not null";

        charSet.clear();
        int i = 0;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (!charSet.contains(c)) {
                charSet.add(c);
            }
            i++;
        }

    }

    /**
     * Returns the first word or string of separators in the given
     * {@code String} starting from the given {@code position}.
     *
     * @param text
     *            the {@code String} which is examined for the word or
     *            separators
     * @param position
     *            the starting index for the word or separator
     * @param separators
     *            the {@code Set} of separator characters
     * @return The word or separator in {@code String}, starting at given index
     *         {@code position}
     * @requires 0 <= position < |text|
     * @ensures [nextWordOrSeparator is substring of text or nextWordOrSeparator
     *          is subset of separators]
     */
    public static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {

        // substring of the text, beginning at the given position
        String tempString = text.substring(position);
        String result = "";
        char first = tempString.charAt(0);
        // the first character has to be a part of the output
        result = result + first;

        /*
         * check from the second character in the substring, because the first
         * one is already in the output String
         */
        int i = 1;
        boolean cont = true; // boolean to act like a flag
        while (i < tempString.length() & cont) {
            if (separators.contains(first)) { // return the separator string
                if (separators.contains(tempString.charAt(i))) {
                    result = result + tempString.charAt(i);
                    i = i + 1;
                } else {
                    cont = false;
                }
            } else { // return the word
                if (separators.contains(tempString.charAt(i))) {
                    cont = false;
                } else {
                    result = result + tempString.charAt(i);
                    i = i + 1;
                }
            }
        }
        return result;

    }

    /**
     * Reads an input file which contains terms and their definitions, storing
     * the values in the given {@code Map}, replacing its contents.
     *
     * @param in
     *            the input stream from the input file
     * @param map
     *            the {@code Map} that gets replaced
     * @replaces map
     * @ensures [the {@code Map}'s Keys are the terms and its Values are the
     *          corresponding definitions]
     *
     */
    public static void getTermsAndDefinitions(SimpleReader in,
            Map<String, String> map) {

        while (!in.atEOS()) {
            String temp = in.nextLine();
            if (!temp.isEmpty()) {
                // the term will only take 1 line, because it is just one word
                String term = temp;
                temp = in.nextLine();
                /*
                 * the definition can take more than 1 line, so this statement
                 * only stores the first line of the definition (if there are
                 * more lines)
                 */
                String def = temp;
                // while loop to see if definition is still going on
                temp = in.nextLine();
                while (!temp.isEmpty()) {
                    def = def + " " + temp;
                    // space is to separate words
                    temp = in.nextLine();
                }
                map.add(term, def);
            }
        }
    }

    /**
     * Sorts all the terms in the given {@code Map} and stores them in
     * alphabetical order in a given {@code Queue}, replacing its contents.
     *
     * @param q
     *            The {@code Queue} to be replaced
     * @param map
     *            The {@code Map} containing the terms as its Keys
     * @param c
     *            The {@code Comparator} used to sort the terms
     * @replaces q
     * @requires |map| > 0
     * @ensures [The returned {@code Queue} contains all of {@code map}'s Keys
     *          in alphabetical order]
     */
    public static void sortTerms(Queue<String> q, Map<String, String> map,
            Comparator<String> c) {
        // this queue is to store all the terms from map, in no order
        Queue<String> temp = q.newInstance();
        for (Map.Pair<String, String> pair : map) {
            temp.enqueue(pair.key());
        }

        // now sort the queue's elements in order
        temp.sort(c);
        q.transferFrom(temp);

    }

    /**
     * Prints out all the HTML tags that come before the list of terms onto the
     * index page.
     *
     * @param out
     *            The output stream, going to the index HTML page
     * @updates out.content
     * @requires out.is_open
     * @ensures [out.content --> opening HTML tags of the index page]
     */
    public static void printIndexHeaderHTMLTags(SimpleWriter out) {

        /*
         * The index page will need a title, a header saying glossary and
         * another saying index, followed by a list of terms
         */
        out.println("<html>");
        out.println("  <head>"); //indentation is needed (I used 2 spaces here)
        out.println("    <title>Glossary</title>");
        out.println("  </head>"); //Closing tags
        out.println("  <body>");
        out.println("    <h2>Glossary</h2>");
        out.println("    <hr />");
        out.println("    <h3>Index</h3>");
        /*
         * the list of terms comes after this; these are all the header tags
         * before the list of terms
         */
    }

    /**
     * Creates links for the list of terms in the given {@code Queue} and prints
     * them onto the index page in HTML layout. Also prints the closing tags for
     * the index page
     *
     * @param q
     *            The {@code Queue} containing all the terms
     * @param out
     *            The output stream, going to the index HTML page
     * @requires out.is_open
     * @updates out.content
     * @ensures [out.content will contain a list of terms, all with a hyperlink,
     *          and the closing body and html tags for the index page]
     */
    public static void printIndexTermsListAndClosingTags(Queue<String> q,
            SimpleWriter out) {

        out.println("    <ul>"); //start of list, this is required
        // keep track of all the terms in q
        Queue<String> temp = q.newInstance();
        while (q.length() > 0) { //iterate through all terms
            String currentTerm = q.dequeue();
            String link = "<li><a href=\"" + currentTerm + ".html\">";
            link = link + currentTerm + "</a></li>";
            out.println("      " + link); // 6 spaces
            temp.enqueue(currentTerm);
        }
        out.println("    </ul>");
        out.println("  </body>");
        out.println("</html>");
        q.transferFrom(temp);
    }

    /**
     * Creates HTML pages for each term in the given {@code Queue}, along with
     * their definitions from the given {@code Map}, adding links to other pages
     * if the definition contains another term in the {@code Queue}.
     *
     * @param q
     *            {@code Queue} containing all the terms in alphabetical order
     * @param map
     *            {@code Map} containing all terms and their definitions as its
     *            Keys and Values
     * @param folder
     *            name of folder where these pages will be stored
     * @param separators
     *            {@code Set} of separator characters
     * @ensures [all terms have their own HTML page with appropriate tags]
     */
    public static void createTermHTMLPages(Queue<String> q,
            Map<String, String> map, String folder, Set<Character> separators) {

        // keep track of all the terms in q
        Queue<String> temp = q.newInstance();
        while (q.length() > 0) { //iterate through all terms
            String currentTerm = q.dequeue();
            String fileName = folder + "/" + currentTerm + ".html";
            // need to create an output stream for each of these terms
            SimpleWriter out = new SimpleWriter1L(fileName);
            out.println("<html>");
            out.println("  <head>");
            out.println("    <title>" + currentTerm + "</title>");
            out.println("  </head>"); //Closing tags
            out.println("  <body>");
            // heading has to be bold, in italics, and red
            out.print("    <h2><b><i><font color=\"red\">");
            out.println(currentTerm + "</font></i></b></h2>");
            out.print("    <blockquote>");
            String def = map.value(currentTerm);
            /*
             * have to check if this definition contains another term. If it
             * does, then that term has to be hyperlinked within the outputted
             * definition
             */
            int position = 0;
            while (position < def.length()) {
                String wordOrSep = nextWordOrSeparator(def, position,
                        separators);
                if (map.hasKey(wordOrSep)) {
                    /*
                     * definition contains another term
                     */
                    String beforeTerm = def.substring(0, position);
                    String linkToTerm = "<a href=\"" + wordOrSep + ".html\">";
                    linkToTerm = linkToTerm + wordOrSep + "</a>"; //closing tag
                    String afterLink = def
                            .substring(position + wordOrSep.length());
                    def = beforeTerm + linkToTerm + afterLink;
                }
                position = position + wordOrSep.length();
            }
            out.println(def + "</blockquote>");
            out.println("    <hr />");
            out.println("    <p>Return to <a href=\"index.html\">index</a><p>");
            out.println("  </body>");
            out.println("</html>");
            temp.enqueue(currentTerm);
            // close output stream
            out.close();
        }
        q.transferFrom(temp);
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        /*
         * in and out are to get the input file from the user of the program,
         * while input and output are related to the files
         */
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        out.print("Enter input file name: ");
        String fileName = in.nextLine();
        out.print("Enter output folder name: ");
        String folderName = in.nextLine();

        SimpleReader input = new SimpleReader1L(fileName);
        /*
         * output folder already created, and is called output in Glossary
         * folder
         */
        SimpleWriter output = new SimpleWriter1L(folderName + "/index.html");

        /*
         * Create the set of separator characters, used by the
         * nextWordOrSeparator method
         */
        final String separators = " \t, ";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separators, separatorSet); //set generated

        /*
         * Create a Map object for the terms and their own separate definition
         * which corresponds to the term, using the method, getting each with
         * the method getTermAndDefinition
         */
        Map<String, String> wordsAndDefs = new Map1L<>();
        getTermsAndDefinitions(input, wordsAndDefs); // all in map

        // sort the terms
        Queue<String> allTerms = new Queue1L<>();
        Comparator<String> order = new CompareStrings();
        sortTerms(allTerms, wordsAndDefs, order); // sorted

        // Output the HTMLs

        // Main glossary index page
        /*
         * create links for the index page, each term on the index page will be
         * a hyperlink leading to their own individual page
         */
        printIndexHeaderHTMLTags(output);
        printIndexTermsListAndClosingTags(allTerms, output);

        // Individual term pages
        createTermHTMLPages(allTerms, wordsAndDefs, folderName, separatorSet);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
        input.close();
        output.close();
    }

}
