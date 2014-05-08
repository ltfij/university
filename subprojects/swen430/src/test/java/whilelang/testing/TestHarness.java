package whilelang.testing;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public abstract class TestHarness {

    private static final String C_RUNTIME_LIBRARY =
            "../../../../../src/main/java/whilelang/runtime/runtime.c";

    protected String srcPath; // path to source files
    protected String outputPath; // path to output files
    protected String outputExtension; // the extension of output files

    /**
     * Construct a test harness object.
     *
     * @param srcPath The path to the source files to be tested
     * @param outputPath The path to the sample output files to compare against.
     * @param outputExtension The extension of output files
     */
    protected TestHarness(String srcPath, String outputPath, String outputExtension) {
        this.srcPath = srcPath.replace('/', File.separatorChar);
        this.outputPath = outputPath.replace('/', File.separatorChar);
        this.outputExtension = outputExtension;

        // TEMP: Just for while the tests are being run in Gradle...
        this.srcPath = "src/test/resources/".replace('/', File.separatorChar) + srcPath;
        this.outputPath = "src/test/resources/".replace('/', File.separatorChar) + outputPath;
    }

    protected void runClassFileTest(String name) {
        // First, we need to compile the class
        runJava(srcPath, "whilelang.Main", "-jvm", "-verbose", name + ".while");

        // Second, we need to run it on the JVM
        String output = runJava(srcPath, name);
        compare(output, outputPath + File.separatorChar + name + "." + outputExtension);
    }

    protected void runInterpreterTest(String name) {
        String output = runJava(srcPath, "whilelang.Main", "-verbose", name + ".while");
        compare(output, outputPath + File.separatorChar + name + "." + outputExtension);
    }

    protected void runX86Test(String name) {
        try {
            // First, we need to generate the assembly file
            runJava(srcPath, "whilelang.Main", "-verbose", "-x86", name + ".while");

            // Second, we need to compile the assembly file with gcc
            compileWithGcc(srcPath, name, name + ".s", C_RUNTIME_LIBRARY);

            // Third, execute the compiled file
            String output = runNative(srcPath, name);
            compare(output, outputPath + File.separatorChar + name + "." + outputExtension);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Problem running compiled test");
        }
    }

    /**
     * Compare the output of executing java on the test case with a reference file.
     *
     * @param output This provides the output from executing java on the test case.
     * @param referenceFile The full path to the reference file. This should use the appropriate
     * separator char for the host operating system.
     */
    private static void compare(String output, String referenceFile) {
        try {
            BufferedReader outReader = new BufferedReader(new StringReader(output));
            BufferedReader refReader = new BufferedReader(new FileReader(new File(referenceFile)));

            String line;

            StringBuilder outSb = new StringBuilder();
            while ((line = outReader.readLine()) != null) {
                outSb.append(line);
                outSb.append("\n");
            }
            StringBuilder refSb = new StringBuilder();
            while ((line = refReader.readLine()) != null) {
                refSb.append(line);
                refSb.append("\n");
            }

            if (!outSb.toString().equals(refSb.toString())) {
                for (String a : refSb.toString().split("\n")) {
                    System.err.println(" > " + a);
                }
                for (String b : outSb.toString().split("\n")) {
                    System.err.println(" < " + b);
                }

                throw new Error("Output doesn't match reference");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    private void compileWithGcc(String dir, String target, String... files) {
        try {
            String tmp = "gcc -g -Wno-format -o " + dir + File.separatorChar + target;
            for (String f : files) {
                tmp += " " + dir + File.separatorChar + f;
            }
            Process p = Runtime.getRuntime().exec(tmp);
            StringBuffer syserr = new StringBuffer();
            StringBuffer sysout = new StringBuffer();
            new StreamGrabber(p.getErrorStream(), syserr);
            new StreamGrabber(p.getInputStream(), sysout);
            int exitCode = p.waitFor();
            System.err.println(syserr); // propagate anything from the error
            // stream
            if (exitCode != 0) {
                System.err.println("============================================================");
                System.err.println(tmp);
                System.err.println("============================================================");
                fail("Problem running gcc to compile test");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Problem running gcc to compile test");
        }
    }

    private String runJava(String path, String... args) {
        try {
            // Compiled project files
            String classpath = "build/classes/main".replace('/', File.separatorChar);
            classpath += File.pathSeparator + "../../../../../build/classes/main";
            // JASM library
            classpath += File.pathSeparator + "lib/jasm-0.1.4.jar";
            classpath += File.pathSeparator + "../../../../../lib/jasm-0.1.4.jar";
            // JX86 library
            classpath += File.pathSeparator + "lib/jx86-0.1.2.jar";
            classpath += File.pathSeparator + "../../../../../lib/jx86-0.1.2.jar";
            // Current directory (for finding the compiled .class files)
            classpath += File.pathSeparator + ".";

            String tmp = "java -cp " + classpath;
            for (String arg : args) {
                tmp += " " + arg;
            }

            Process p = Runtime.getRuntime().exec(tmp, new String[0], new File(path));

            StringBuffer syserr = new StringBuffer();
            StringBuffer sysout = new StringBuffer();
            new StreamGrabber(p.getErrorStream(), syserr);
            new StreamGrabber(p.getInputStream(), sysout);
            int exitCode = p.waitFor();
            System.err.println(syserr); // propagate anything from the error stream
            if (exitCode != 0) {
                System.err.println("============================================================");
                System.err.println(tmp);
                System.err.println("============================================================");
                System.err.println(syserr);
                return null;
            } else {
                return sysout.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Problem running compiled test");
        }

        return null;
    }

    private String runNative(String dir, String executable) {
/*        Process p = Runtime.getRuntime().exec(dir + File.separatorChar + executable);
        StringBuffer syserr = new StringBuffer();
        StringBuffer sysout = new StringBuffer();
        new StreamGrabber(p.getErrorStream(), syserr);
        new StreamGrabber(p.getInputStream(), sysout);
        int exitCode = p.waitFor();*/
        try {
            Process p = Runtime.getRuntime().exec(dir + File.separatorChar + executable);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String input = "";
            String error = "";

            String temp = null;

            while ((temp = stdInput.readLine()) != null) {
                input += temp += "\n";
            }

            while ((temp = stdError.readLine()) != null) {
                error += temp += "\n";
            }

            p.waitFor();

            System.err.println("============================================================");
            System.err.println(dir + File.separatorChar + executable);
            System.err.println("============================================================");
            System.err.println(error); // propagate anything from the error
            // stream
            return input.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Problem running native executable");
            return null;
        }
    }

    private static class StreamGrabber extends Thread {

        private InputStream input;
        private StringBuffer buffer;

        public StreamGrabber(InputStream input, StringBuffer buffer) {
            this.input = input;
            this.buffer = buffer;
            start();
        }

        public void run() {
            try {
                int nextChar;
                // keep reading!!
                while ((nextChar = input.read()) != -1) {
                    buffer.append((char) nextChar);
                }
            } catch (IOException ioe) {
            }
        }
    }
}
