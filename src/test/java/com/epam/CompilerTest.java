package com.epam;

import com.epam.compiler.Compiler;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompilerTest {

    public void compilerTest(String sourceCode, String expectedOutput,
                             Boolean enableOptimization) throws IOException {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             PrintStream printStream = new PrintStream(stream)) {

            System.setOut(printStream);

            Compiler compiler = new Compiler(enableOptimization);
            compiler.compile(sourceCode);
            compiler.launchProgram();

            assertEquals(stream.toString(), expectedOutput);
        }
    }

    @Test
    void test1() throws IOException {
        compilerTest("++-><[-][+].", String.valueOf((char) 0), false);
    }

    @Test
    void test2() throws IOException {
        compilerTest("++-><[-][+].", String.valueOf((char) 0), true);
    }

    @Test
    void test3() throws IOException {
        compilerTest(Common.readAllLines("hw.bf"), "Hello World!", false);
    }

    @Test
    void test4() throws IOException {
        compilerTest(Common.readAllLines("hw.bf"), "Hello World!", true);
    }

    @Test
    void test5() throws IOException {
        compilerTest(Common.readAllLines("big.bf"), String.valueOf((char) 202), false);
    }

    @Test
    void test6() throws IOException {
        compilerTest(Common.readAllLines("big.bf"), String.valueOf((char) 202), true);
    }
}
