package ca.jrvs.apps.practice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class LambdaStreamImpTest {


    private LambdaStreamExc lse;

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        lse = new LambdaStreamImp();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void createStrStream() {
        List<String> actual = lse.createStrStream("a", "b", "c")
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("a", "b", "c"), actual);
    }

    @Test
    public void toUpperCase() {
        List<String> actual = lse.toUpperCase("ab", "cD")
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("AB", "CD"), actual);
    }

    @Test
    public void filter() {
        // should remove strings containing "a"
        List<String> actual = lse.filter(
                        lse.createStrStream("a", "bb", "cat", "dog"), "a")
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("bb", "dog"), actual);
    }

    @Test
    public void createIntStream_fromArray() {
        List<Integer> actual = lse.toList(lse.createIntStream(new int[]{1, 2, 3}));
        assertEquals(Arrays.asList(1, 2, 3), actual);
    }

    @Test
    public void toList_genericStream() {
        List<String> actual = lse.toList(lse.createStrStream("x", "y"));
        assertEquals(Arrays.asList("x", "y"), actual);
    }

    @Test
    public void toList_intStream() {
        List<Integer> actual = lse.toList(IntStream.of(4, 5, 6));
        assertEquals(Arrays.asList(4, 5, 6), actual);
    }

    @Test
    public void createIntStream_rangeClosed() {
        List<Integer> actual = lse.toList(lse.createIntStream(2, 5));
        assertEquals(Arrays.asList(2, 3, 4, 5), actual);
    }

    @Test
    public void squareRootIntStream() {
        double[] actual = lse.squareRootIntStream(IntStream.of(0, 1, 4, 9)).toArray();
        assertArrayEquals(new double[]{0.0, 1.0, 2.0, 3.0}, actual, 1e-9);
    }

    @Test
    public void getOdd() {
        List<Integer> actual = lse.toList(lse.getOdd(IntStream.of(0, 1, 2, 3, 4, 5)));
        assertEquals(Arrays.asList(1, 3, 5), actual);
    }

    @Test
    public void getLambdaPrinter() {
        Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
        printer.accept("Message body");

        String expected = "start>Message body<end" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void printMessages() {
        String[] messages = {"a", "b", "c"};
        Consumer<String> printer = lse.getLambdaPrinter("msg:", "!");

        lse.printMessages(messages, printer);

        String expected =
                "msg:a!" + System.lineSeparator() +
                        "msg:b!" + System.lineSeparator() +
                        "msg:c!" + System.lineSeparator();

        assertEquals(expected, outContent.toString());
    }

    @Test
    public void printOdd() {
        Consumer<String> printer = lse.getLambdaPrinter("odd number:", "!");
        lse.printOdd(lse.createIntStream(0, 5), printer);

        String expected =
                "odd number:1!" + System.lineSeparator() +
                        "odd number:3!" + System.lineSeparator() +
                        "odd number:5!" + System.lineSeparator();

        assertEquals(expected, outContent.toString());
    }

    @Test
    public void flatNestedInt() {
        List<Integer> actual = lse.flatNestedInt(
                Arrays.asList(
                        Arrays.asList(1, 2),
                        Arrays.asList(3),
                        Arrays.asList(4, 5)
                ).stream()
        ).collect(Collectors.toList());

        assertEquals(Arrays.asList(1, 4, 9, 16, 25), actual);
    }

}
