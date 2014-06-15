// This file is part of the WhileLang Compiler (wlc).
//
// The WhileLang Compiler is free software; you can redistribute
// it and/or modify it under the terms of the GNU General Public
// License as published by the Free Software Foundation; either
// version 3 of the License, or (at your option) any later version.
//
// The WhileLang Compiler is distributed in the hope that it
// will be useful, but WITHOUT ANY WARRANTY; without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE. See the GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public
// License along with the WhileLang Compiler. If not, see
// <http://www.gnu.org/licenses/>
//
// Copyright 2013, David James Pearce.

package whilelang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import jasm.io.ClassFileWriter;
import jasm.lang.ClassFile;
import jasm.util.DeadCodeElimination;
import jx86.io.AsmFileWriter;
import jx86.lang.Target;
import jx86.lang.X86File;
import whilelang.io.Lexer;
import whilelang.io.Parser;
import whilelang.io.While2ClassTranslator;
import whilelang.io.X86FileWriter;
import whilelang.lang.WhileFile;
import whilelang.pipelines.DefiniteAssignment;
import whilelang.pipelines.TypeChecker;
import whilelang.util.SyntaxError;

public class Main {

    public static PrintStream errout;

    static {
        try {
            errout = new PrintStream(System.err, true, "UTF8");
        } catch (Exception e) {
            errout = System.err;
        }
    }

    public static void main(String[] args) throws Exception {
        run(args);
    }

    public static boolean run(String[] args) {
        boolean verbose = false;
        int fileArgsBegin = 0;
        Mode mode = Mode.interpret;

        for (int i = 0; i != args.length; ++i) {
            if (args[i].startsWith("-")) {
                String arg = args[i];
                if (arg.equals("-help")) {
                    usage();
                    System.exit(0);
                } else if (arg.equals("-version")) {
                    System.out.println("While Language Compiler (wlc)");
                    System.exit(0);
                } else if (arg.equals("-verbose")) {
                    verbose = true;
                } else if (arg.equals("-jvm")) {
                    mode = Mode.jvm;
                } else if (arg.equals("-x86")) {
                    mode = Mode.x86;
                } else {
                    throw new RuntimeException("Unknown option: " + args[i]);
                }

                fileArgsBegin = i + 1;
            }
        }

        if (fileArgsBegin == args.length) {
            usage();
            return false;
        }

        try {
            String filename = args[fileArgsBegin];
            File srcFile = new File(filename);

            // First, lex and parse the source file
            Lexer lexer = new Lexer(srcFile.getPath());
            Parser parser = new Parser(srcFile.getPath(), lexer.scan());
            WhileFile ast = parser.read();

            // Second, we'd want to perform some kind of type checking here.
            new DefiniteAssignment().check(ast);
            new TypeChecker().check(ast);

            // Third, we'd want to run the interpreter or compile the file.
            switch (mode) {
                case interpret:
                    new Interpreter().run(ast);
                    break;
                case jvm:
                    File classFile = new File(filename.substring(0, filename.lastIndexOf('.'))
                            + ".class");
                    FileOutputStream fos = new FileOutputStream(classFile);

                    System.out.println("Compiling to JVM Bytecode...");

                    ClassFile cf = new While2ClassTranslator().translate(ast);

                    new DeadCodeElimination().apply(cf);

                    ClassFileWriter cfw = new ClassFileWriter(fos);
                    cfw.write(cf);

                    fos.close();

                    break;
                case x86:
                    System.out.println("Compiling to X86 Assembly Language...");

                    // First, determine output filename
                    File asFile = new File(filename.substring(0, filename.lastIndexOf('.')) + ".s");

                    // Second, build the x86 file
                    Target target = Target.LINUX_X86_64;
                    X86File xf = new X86FileWriter(target).build(ast);

                    // Third, write that file in GAS compatible assembly language
                    AsmFileWriter afw = new AsmFileWriter(asFile);
                    afw.write(xf);
                    afw.close();

                    break;
            }
        } catch (SyntaxError e) {
            if (e.filename() != null) {
                e.outputSourceError(System.out);
            } else {
                System.err.println("syntax error (" + e.getMessage() + ").");
            }

            if (verbose) {
                e.printStackTrace(errout);
            }

            return false;
        } catch (Exception e) {
            errout.println("Error: " + e.getMessage());
            if (verbose) {
                e.printStackTrace(errout);
            }
            return false;
        }

        return true;
    }

    /**
     * Print out information regarding command-line arguments
     */
    public static void usage() {
        String[][] info = {{"version", "Print version information"},
                {"verbose", "Print detailed information on what the compiler is doing"},
                {"jvm", "Generate JVM Bytecode"}, {"x86", "Generate x86 Assembly Language"}};

        System.out.println("usage: wlc <options> <source-files>");
        System.out.println("Options:");

        // first, work out gap information
        int gap = 0;

        for (String[] p : info) {
            gap = Math.max(gap, p[0].length() + 5);
        }

        // now, print the information
        for (String[] p : info) {
            System.out.print("  -" + p[0]);
            int rest = gap - p[0].length();
            for (int i = 0; i != rest; ++i) {
                System.out.print(" ");
            }
            System.out.println(p[1]);
        }
    }

    private static enum Mode {interpret, jvm, x86}
}
