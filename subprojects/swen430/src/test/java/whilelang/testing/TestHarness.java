package whilelang.testing;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class TestHarness {

    private static final String JASM_JAR = "../../lib/jasm-v0.1.1.jar".replace('/',
            File.separatorChar);
    private static final String JX86_JAR = "../../lib/jx86-v0.1.0.jar".replace('/',
            File.separatorChar);

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
    public TestHarness(String srcPath, String outputPath, String outputExtension) {
        this.srcPath = srcPath.replace('/', File.separatorChar);
        this.outputPath = outputPath.replace('/', File.separatorChar);
        this.outputExtension = outputExtension;

        // TEMP: Just for while the tests are beign run in gradle...
        this.srcPath = "src/test/resources/".replace('/', File.separatorChar) + srcPath;
        this.outputPath = "src/test/resources/".replace('/', File.separatorChar) + outputPath;
    }

    protected void runInterpreterTest(String name) {
        String output = runJava(srcPath, "whilelang.Main", name + ".while");
        compare(output, outputPath + File.separatorChar + name + "." + outputExtension);
    }

    protected void runClassFileTest(String name) {
        // First, we need to compiler the class
        runJava(srcPath, "whilelang.Main", "-jvm", name + ".while");

        // Second, we need to run it on the JVM
        String output = runJava(srcPath, name);
        compare(output, outputPath + File.separatorChar + name + "." + outputExtension);
    }

    protected static String runJava(String path, String... args) {
        try {
            // We need to have
            String
                    classpath =
                    "." + File.pathSeparator + "../../src/" + File.pathSeparator + JASM_JAR
                            + File.pathSeparator + JX86_JAR;
            classpath = classpath.replace('/', File.separatorChar);
            // TEMP: For running it in gradle
            classpath = classpath + File.pathSeparator + "../../../../../build/classes/main".replace('/', File.separatorChar);

            String tmp = "java -cp " + classpath;
            for (String arg : args) {
                tmp += " " + arg;
            }
            Process p = Runtime.getRuntime().exec(tmp, null, new File(path));

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

    /**
     * Compare the output of executing java on the test case with a reference file.
     *
     * @param output This provides the output from executing java on the test case.
     * @param referenceFile The full path to the reference file. This should use the appropriate
     * separator char for the host operating system.
     */
    protected static void compare(String output, String referenceFile) {
        try {
            BufferedReader outReader = new BufferedReader(new StringReader(output));
            BufferedReader refReader = new BufferedReader(new FileReader(new File(referenceFile)));

            while (refReader.ready() && outReader.ready()) {
                String a = refReader.readLine();
                String b = outReader.readLine();

                if (a.equals(b)) {
                    continue;
                } else {
                    System.err.println(" > " + a);
                    System.err.println(" < " + b);
                    throw new Error("Output doesn't match reference");
                }
            }

            String l1 = outReader.readLine();
            String l2 = refReader.readLine();
            if (l1 == null && l2 == null) {
                return;
            }
            do {
                l1 = outReader.readLine();
                l2 = refReader.readLine();
                if (l1 != null) {
                    System.err.println(" < " + l1);
                } else if (l2 != null) {
                    System.err.println(" > " + l2);
                }
            } while (l1 != null && l2 != null);

            fail("Files do not match");
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    static public class StreamGrabber extends Thread {

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
