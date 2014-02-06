package com.hjwylde.uni.swen221.assignment04.tests;

import org.junit.Assert;
import org.junit.Test;

import com.hjwylde.uni.swen221.assignment04.shapes.Canvas;
import com.hjwylde.uni.swen221.assignment04.shapes.parser.Interpreter;

/*
 * Code for Assignment 4, SWEN 221
 * Name: Henry J. Wylde
 * Usercode: wyldehenr
 * ID: 300224283
 */

public class InterpreterTests {
    
    @Test
    public void invalidSyntaxTests() {
        // This test makes sure that the interpreter throws an appropriate error message
        String[] inputs = {
            "x = [2,2,4,4]\nfill y #0000ff\n",
            "x = [2.012,2,4,4]\nfill x #0000ff\n",
            "x= [4,4,-2,2]\nfill x #010203\n", "x= [4,4,-2,2]\nfill x\n",
            "x= [4,4,2,2]\ndraw z\n",
            "x = [2,0,5,5]\ny = [0,2,4,5]\ny =  y-z\nfill y #ff0000\n",
            "x = [2,0,-5,5]\ny = [0,2,4,5]\ny =  y-x\nfill y #ff0000\n",
            "x = [2,0,+-5,5]\ny = [0,2,4,5]\ny =  y-x\nfill y #ff0000\n",
            "x = [2,00500.0,5,5]\ny = [0,2,4,5]\ny =  y-x\nfill y #ff0000\n",
            "x = [2,0,-+5,5]\ny = [0,2,4,5]\ny =  y-z\nfill y #ff0000\n",
            "x = [2,0,5,5]\ny = [0,2,4,5]\ny =  (y-x\nfill y #ff0000\n",
            "x = [2,0,5]\nfill x #ff0000\n",
            "x += [2,0,5,5]\nfill x #ff0000\n",
            "x = [2,0,5,5]\ny = [0,2,4,5]\ny =  y-x\ndraw y #ff000"
        };
        
        for (int i = 0; i != inputs.length; ++i) {
            String input = inputs[i];
            try {
                new Interpreter(input).run();
                Assert.fail("Input " + i + " should have given an error!");
            } catch (IllegalArgumentException e) {} catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Exception occurred: " + e.getMessage());
            }
        }
    }
    
    /**
     * Some simple tests that shape difference is working. You will want to add
     * more of your own tests!
     */
    @Test
    public void validDifferenceTests() {
        String[][] inputs = {
            {
                "x = [2,0,5,5]\ny = [0,2,4,5]\ny = y - x\ndraw y #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ffffff#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,5]\ny = y- x\ndraw y #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ffffff#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,5]\ny = y -x\ndraw y #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ffffff#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,5]\ny =  y-x\ndraw y #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ffffff#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [+2,-0,+5,5]\nx = [0,2,4,5]-x\ndraw x #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ffffff#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,5]\ny =  y-x\nfill y #ff0000\n",
                "#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ffffff#ffffff\n#ff0000#ff0000#ff0000#ff0000\n#ff0000#ff0000#ff0000#ff0000\n"
            },
            {
                "x = [0,0,2,2]\ny = [1,1,2,2]\nz=[0,1,   2, 1] fill \n   y\n&\nz+   x  #ff0000",
                "#ff0000#ff0000\n#ff0000#ff0000\n"
            }
        };
        
        InterpreterTests.testValidInputs(inputs);
    }
    
    /**
     * Some simple tests that the draw command is working. You will want to add
     * more of your own tests!
     */
    @Test
    public void validDrawTests() {
        String[][] inputs = {
            {
                "x = [2,2,4,4]\ndraw x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#ffffff#ffffff#0000ff\n#ffffff#ffffff#0000ff#ffffff#ffffff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x = [2,2,4,4]\ndraw x #010203\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#010203#010203#010203#010203\n#ffffff#ffffff#010203#ffffff#ffffff#010203\n#ffffff#ffffff#010203#ffffff#ffffff#010203\n#ffffff#ffffff#010203#010203#010203#010203\n"
            },
            {
                "draw [2,2,4,4] #010203\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#010203#010203#010203#010203\n#ffffff#ffffff#010203#ffffff#ffffff#010203\n#ffffff#ffffff#010203#ffffff#ffffff#010203\n#ffffff#ffffff#010203#010203#010203#010203\n"
            },
            {
                "x = [2,2,4,4]\ndraw x #0000ff\ny = [0,0,3,3]\ndraw y #010203\n",
                "#010203#010203#010203#ffffff#ffffff#ffffff\n#010203#ffffff#010203#ffffff#ffffff#ffffff\n#010203#010203#010203#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#ffffff#ffffff#0000ff\n#ffffff#ffffff#0000ff#ffffff#ffffff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
        };
        
        InterpreterTests.testValidInputs(inputs);
    }
    
    /**
     * Some simple tests that the fill command is working. You will want to add
     * more of your own tests!
     */
    @Test
    public void validFillTests() {
        String[][] inputs = {
            {
                "x = [2,2,4,4]\nfill x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x= [2,2,4,4]\nfill x #010203\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#010203#010203#010203#010203\n#ffffff#ffffff#010203#010203#010203#010203\n#ffffff#ffffff#010203#010203#010203#010203\n#ffffff#ffffff#010203#010203#010203#010203\n"
            },
            {
                "x =[2,2,4,4]\nfill x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "fill [2,2,4,4] #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x=[2,2,4,4]\nfill x#0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x=[2 ,2,4,4]\nfill x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x=[2, 2,4,4]\nfill x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            },
            {
                "x=[2, 2, 4, 4]\nfill x #0000ff\n",
                "#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#ffffff#ffffff#ffffff#ffffff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n#ffffff#ffffff#0000ff#0000ff#0000ff#0000ff\n"
            }
        };
        InterpreterTests.testValidInputs(inputs);
    }
    
    /**
     * Some simple tests that shape intersection is working. You will want to add
     * more of your own tests!
     */
    @Test
    public void validIntersectionTests() {
        String[][] inputs = {
            {
                "x = [2,0,5,5]\ny = [0,2,4,4]\ndraw x #00ff00\ndraw y #0000ff\ny = y & x\ndraw y #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,4]\ndraw x #00ff00\ndraw y #0000ff\ny = y& x\ndraw y #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,4]\ndraw x #00ff00\ndraw y #0000ff\ny = y &x\ndraw y #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,4]\ndraw x #00ff00\ndraw y #0000ff\ny =y&x\ndraw y #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            },
            {
                "x = [2,0,5,5]\ndraw x #00ff00\nx = [0,2,4,4]\ndraw x #0000ff\nx =x&[2,0,5,5]\ndraw x #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            },
            {
                "x = [2,0,5,5]\ny = [0,2,4,4]\ndraw x #00ff00\ndraw y #0000ff\ny = y & x\nfill y #ff0000",
                "#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#ffffff#00ff00\n#0000ff#0000ff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#ffffff#ffffff#00ff00\n#0000ff#ffffff#ff0000#ff0000#00ff00#00ff00#00ff00\n#0000ff#0000ff#0000ff#0000ff#ffffff#ffffff#ffffff\n"
            }
        };
        
        InterpreterTests.testValidInputs(inputs);
    }
    
    /**
     * Some simple tests that shape union is working. You will want to add
     * more of your own tests!
     */
    @Test
    public void validUnionTests() {
        String[][] inputs = {
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = y + x\ndraw y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = y+ x\ndraw y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = y +x\ndraw y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = y+x\ndraw y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = (y + x)\ndraw y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\nx = ((((([0,0,3,3])) + ((x)))))\ndraw x #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#ffffff#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#ffffff#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#ffffff#ffffff#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
            {
                "x = [2,2,4,4]\ny = [0,0,3,3]\ny = y + x\nfill y #00ff00\n",
                "#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#00ff00#ffffff#ffffff#ffffff\n#00ff00#00ff00#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n#ffffff#ffffff#00ff00#00ff00#00ff00#00ff00\n"
            },
        };
        
        InterpreterTests.testValidInputs(inputs);
    }
    
    private static void testValidInputs(String[][] inputs) {
        for (int i = 0; i != inputs.length; ++i) {
            String[] input = inputs[i];
            try {
                Canvas canvas = new Interpreter(input[0]).run();
                String output = canvas.toString();
                if (!input[1].equals(output)) {
                    System.out.println("Incorrect output on input " + i + " : "
                        + input[0] + "\nExpected output:\n" + input[1]
                        + "\nReceived output:\n" + canvas.toString() + "\n\n");
                    Assert.fail("Incorrect output on input " + i + " : "
                        + input[0] + "\nExpected output:\n" + input[1]
                        + "\nReceived output:\n" + canvas.toString() + "\n\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Exception occurred: " + e.getMessage()
                    + " on input: " + input[0]);
            }
        }
    }
}
