import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

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
 * Tests for all the methods used in the Glossary.java class
 *
 * @author Varun Mangla
 */
public class GlossaryTest {

    // Tests of generateElements

    // routine test - testing only letters
    @Test
    public void generateElementsTest1() {
        String str = "abcdefffed";
        String strRequired = "abcdefffed";
        Set<Character> charSet = new Set1L<>();
        Set<Character> charSetRequired = new Set1L<>();
        charSetRequired.add('a');
        charSetRequired.add('b');
        charSetRequired.add('c');
        charSetRequired.add('d');
        charSetRequired.add('e');
        charSetRequired.add('f');
        Glossary.generateElements(str, charSet);
        assertEquals(charSetRequired, charSet);
        assertEquals(strRequired, str);
    }

    // testing special characters
    @Test
    public void generateElementsTest2() {
        String str = "abab*,:";
        String strRequired = "abab*,:";
        Set<Character> charSet = new Set1L<>();
        Set<Character> charSetRequired = new Set1L<>();
        charSetRequired.add('a');
        charSetRequired.add('b');
        charSetRequired.add('*');
        charSetRequired.add(',');
        charSetRequired.add(':');
        Glossary.generateElements(str, charSet);
        assertEquals(charSetRequired, charSet);
        assertEquals(strRequired, str);
    }

    /*
     * testing numbers, and quotation marks because they need the \ to go
     * through
     */
    @Test
    public void generateElementsTest3() {
        String str = "xy123\"";
        String strRequired = "xy123\"";
        Set<Character> charSet = new Set1L<>();
        Set<Character> charSetRequired = new Set1L<>();
        charSetRequired.add('x');
        charSetRequired.add('y');
        charSetRequired.add('1');
        charSetRequired.add('2');
        charSetRequired.add('3');
        charSetRequired.add('"');
        Glossary.generateElements(str, charSet);
        assertEquals(charSetRequired, charSet);
        assertEquals(strRequired, str);
    }

    // Tests of nextWordOrSeparator

    // routine test, position 0, returns a word
    @Test
    public void wordOrSeparatorTest1() {
        String text = "abc de   ,f";
        Set<Character> separators = new Set1L<>();
        separators.add(' ');
        separators.add('\t');
        separators.add(',');
        int pos = 0;
        String textRequired = "abc de   ,f";
        Set<Character> separatorsReq = new Set1L<>();
        separatorsReq.add(' ');
        separatorsReq.add('\t');
        separatorsReq.add(',');
        int posRequired = 0;
        String result = Glossary.nextWordOrSeparator(text, pos, separators);
        String required = "abc";
        assertEquals(textRequired, text);
        assertEquals(separatorsReq, separators);
        assertEquals(posRequired, pos);
        assertEquals(required, result);

    }

    // routine test, position 3, returns a separator string
    @Test
    public void wordOrSeparatorTest2() {
        String text = "abc de   ,f";
        Set<Character> separators = new Set1L<>();
        separators.add(' ');
        separators.add('\t');
        separators.add(',');
        final int pos = 3;
        String textRequired = "abc de   ,f";
        Set<Character> separatorsReq = new Set1L<>();
        separatorsReq.add(' ');
        separatorsReq.add('\t');
        separatorsReq.add(',');
        final int posRequired = 3;
        String result = Glossary.nextWordOrSeparator(text, pos, separators);
        String required = " ";
        assertEquals(textRequired, text);
        assertEquals(separatorsReq, separators);
        assertEquals(posRequired, pos);
        assertEquals(required, result);

    }

    // testing between a separator string, index = 7
    @Test
    public void wordOrSeparatorTest3() {
        String text = "abc de   ,f";
        Set<Character> separators = new Set1L<>();
        separators.add(' ');
        separators.add('\t');
        separators.add(',');
        final int pos = 7;
        String textRequired = "abc de   ,f";
        Set<Character> separatorsReq = new Set1L<>();
        separatorsReq.add(' ');
        separatorsReq.add('\t');
        separatorsReq.add(',');
        final int posRequired = 7;
        String result = Glossary.nextWordOrSeparator(text, pos, separators);
        String required = "  ,";
        assertEquals(textRequired, text);
        assertEquals(separatorsReq, separators);
        assertEquals(posRequired, pos);
        assertEquals(required, result);
    }

    // Tests for getTermsAndDefinitions

    // routine test - 2 terms and definitions - all definitions 1 line long
    @Test
    public void termsAndDefsTest1() {
        SimpleReader input = new SimpleReader1L("testInput1.txt");
        Map<String, String> map = new Map1L<>();
        Glossary.getTermsAndDefinitions(input, map);
        Map<String, String> mapRequired = new Map1L<>();
        mapRequired.add("word", "a string of characters");
        mapRequired.add("world", "a region or group of countries");
        assertEquals(mapRequired, map);
    }

    // test with one term and longer definition - 3 lines
    @Test
    public void termsAndDefsTest2() {
        SimpleReader input = new SimpleReader1L("testInput2.txt");
        Map<String, String> map = new Map1L<>();
        Glossary.getTermsAndDefinitions(input, map);
        Map<String, String> mapRequired = new Map1L<>();
        String def = "a single distinct meaningful element of speech or "
                + "writing, used with others (or sometimes alone) to form a "
                + "sentence and typically shown with a space on either side "
                + "when written or printed.";
        mapRequired.add("word", def);
        assertEquals(mapRequired, map);
    }

    // Tests for sortTerms

    // routine test - 2 strings, easy to find order
    @Test
    public void sortTermsTest1() {
        Comparator<String> c = new Glossary.CompareStrings();
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        String term1 = "word";
        String def1 = "a string of characters";
        m.add(term1, def1);
        String term2 = "number";
        String def2 = "a quantity or amount";
        m.add(term2, def2);
        Glossary.sortTerms(q, m, c);
        Map<String, String> mReq = new Map1L<>();
        mReq.add(term1, def1);
        mReq.add(term2, def2);
        Queue<String> qReq = new Queue1L<>();
        qReq.enqueue(term2);
        qReq.enqueue(term1);
        assertEquals(qReq, q);
        assertEquals(mReq, m);
    }

    // 2 similar strings, harder to find the order
    @Test
    public void sortTermsTest2() {
        Comparator<String> c = new Glossary.CompareStrings();
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        String term1 = "word";
        String def1 = "a string of characters";
        m.add(term1, def1);
        String term2 = "world";
        String def2 = "a region or group of countries";
        m.add(term2, def2);
        Glossary.sortTerms(q, m, c);
        Map<String, String> mReq = new Map1L<>();
        mReq.add(term1, def1);
        mReq.add(term2, def2);
        Queue<String> qReq = new Queue1L<>();
        qReq.enqueue(term1);
        qReq.enqueue(term2);
        assertEquals(qReq, q);
        assertEquals(mReq, m);
    }

    // Tests for printIndexHeaderHTMLTags

    // this outputs the same thing regardless of output file
    @Test
    public void indexHeaderTest() {
        SimpleWriter out = new SimpleWriter1L("testOutput1.txt");
        SimpleReader in = new SimpleReader1L("testOutput1.txt");
        Glossary.printIndexHeaderHTMLTags(out);
        String input = "";
        while (!in.atEOS()) {
            input = input + in.nextLine();
        }
        String required = "<html>  <head>    <title>Glossary</title>"
                + "  </head>  <body>    <h2>Glossary</h2>    <hr />"
                + "    <h3>Index</h3>";
        assertEquals(required, input);

        out.close();
        in.close();
    }

    // Tests for printIndexTermsListAndClosingTags

    // routine test - 2 strings in the queue
    @Test
    public void printListAndTagsTest1() {
        SimpleWriter out = new SimpleWriter1L("testOutput2.txt");
        SimpleReader in = new SimpleReader1L("testOutput2.txt");
        Queue<String> q = new Queue1L<>();
        q.enqueue("number");
        q.enqueue("word");
        Glossary.printIndexTermsListAndClosingTags(q, out);
        Queue<String> qReq = new Queue1L<>();
        qReq.enqueue("number");
        qReq.enqueue("word");
        String input = "";
        while (!in.atEOS()) {
            input = input + in.nextLine();
        }
        String required = "    <ul>"
                + "      <li><a href=\"number.html\">number</a></li>"
                + "      <li><a href=\"word.html\">word</a></li>"
                + "    </ul>  </body></html>";
        assertEquals(required, input);
        assertEquals(qReq, q);

        out.close();
        in.close();
    }

    /*
     * test with the same elements in the queue, but flipped. This is just to
     * show that the method does work, however it isn't used in this way in the
     * program itself. In the program, the Queue object sent as an argument was
     * in alphabetical order from beforehand. The general form of the output
     * will stay the same, since the majority is just printed and there aren't
     * really any calculations
     */
    @Test
    public void printListAndTagsTest2() {
        SimpleWriter out = new SimpleWriter1L("testOutput3.txt");
        SimpleReader in = new SimpleReader1L("testOutput3.txt");
        Queue<String> q = new Queue1L<>();
        q.enqueue("word");
        q.enqueue("number");
        Glossary.printIndexTermsListAndClosingTags(q, out);
        Queue<String> qReq = new Queue1L<>();
        qReq.enqueue("word");
        qReq.enqueue("number");
        String input = "";
        while (!in.atEOS()) {
            input = input + in.nextLine();
        }
        String required = "    <ul>"
                + "      <li><a href=\"word.html\">word</a></li>"
                + "      <li><a href=\"number.html\">number</a></li>"
                + "    </ul>  </body></html>";
        assertEquals(required, input);
        assertEquals(qReq, q);

        out.close();
        in.close();
    }

    // Tests for createTermHTMLPages

    // routine tests - no terms in the definition
    @Test
    public void createTermPageTest1() {
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        String term1 = "number";
        String term2 = "word";
        String def1 = "a quantity or amount";
        String def2 = "a string of characters";
        q.enqueue(term1);
        q.enqueue(term2);
        m.add(term1, def1);
        m.add(term2, def2);
        String folder = "data";
        Set<Character> separators = new Set1L<>();
        separators.add(' ');
        separators.add('\t');
        separators.add(',');
        Glossary.createTermHTMLPages(q, m, folder, separators);
        Queue<String> qReq = new Queue1L<>();
        Map<String, String> mReq = new Map1L<>();
        qReq.enqueue(term1);
        qReq.enqueue(term2);
        mReq.add(term1, def1);
        mReq.add(term2, def2);
        SimpleReader inTerm1 = new SimpleReader1L("data/number.html");
        SimpleReader inTerm2 = new SimpleReader1L("data/word.html");
        String input1 = "";
        while (!inTerm1.atEOS()) {
            input1 = input1 + inTerm1.nextLine();
        }
        String input2 = "";
        while (!inTerm2.atEOS()) {
            input2 = input2 + inTerm2.nextLine();
        }
        String required1 = "<html>  <head>    <title>number</title>  </head>"
                + "  <body>"
                + "    <h2><b><i><font color=\"red\">number</font></i></b></h2>"
                + "    <blockquote>a quantity or amount</blockquote>"
                + "    <hr />    <p>Return to <a href=\"index.html\">index</a><p>"
                + "  </body></html>";

        String required2 = "<html>  <head>    <title>word</title>  </head>"
                + "  <body>"
                + "    <h2><b><i><font color=\"red\">word</font></i></b></h2>"
                + "    <blockquote>a string of characters</blockquote>"
                + "    <hr />    <p>Return to <a href=\"index.html\">index</a><p>"
                + "  </body></html>";

        assertEquals(required1, input1);
        assertEquals(required2, input2);
        assertEquals(qReq, q);
        assertEquals(mReq, m);

        inTerm1.close();
        inTerm2.close();
    }

    // one term shows up in the other's definition
    @Test
    public void createTermPageTest2() {
        Queue<String> q = new Queue1L<>();
        Map<String, String> m = new Map1L<>();
        String term1 = "letters";
        String term2 = "word";
        String def1 = "what make up a word";
        String def2 = "a string of characters";
        q.enqueue(term1);
        q.enqueue(term2);
        m.add(term1, def1);
        m.add(term2, def2);
        String folder = "data";
        Set<Character> separators = new Set1L<>();
        separators.add(' ');
        separators.add('\t');
        separators.add(',');
        Glossary.createTermHTMLPages(q, m, folder, separators);
        Queue<String> qReq = new Queue1L<>();
        Map<String, String> mReq = new Map1L<>();
        qReq.enqueue(term1);
        qReq.enqueue(term2);
        mReq.add(term1, def1);
        mReq.add(term2, def2);
        SimpleReader inTerm1 = new SimpleReader1L("data/letters.html");
        SimpleReader inTerm2 = new SimpleReader1L("data/word.html");
        String input1 = "";
        while (!inTerm1.atEOS()) {
            input1 = input1 + inTerm1.nextLine();
        }
        String input2 = "";
        while (!inTerm2.atEOS()) {
            input2 = input2 + inTerm2.nextLine();
        }
        String required1 = "<html>  <head>    <title>letters</title>  </head>"
                + "  <body>"
                + "    <h2><b><i><font color=\"red\">letters</font></i></b></h2>"
                + "    <blockquote>what make up a <a href=\"word.html\">word"
                + "</a></blockquote>    <hr />    <p>Return to "
                + "<a href=\"index.html\">index</a><p>" + "  </body></html>";

        String required2 = "<html>  <head>    <title>word</title>  </head>"
                + "  <body>"
                + "    <h2><b><i><font color=\"red\">word</font></i></b></h2>"
                + "    <blockquote>a string of characters</blockquote>"
                + "    <hr />    <p>Return to <a href=\"index.html\">index</a><p>"
                + "  </body></html>";

        assertEquals(required1, input1);
        assertEquals(required2, input2);
        assertEquals(qReq, q);
        assertEquals(mReq, m);

        inTerm1.close();
        inTerm2.close();
    }

}
