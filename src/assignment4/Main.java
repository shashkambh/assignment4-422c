/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Shashank Kambhampati
 * skk834
 * 16445
 * Pranav Harathi
 * sh44674
 * 16460
 * Slip days used: 0
 * Fall 2016
 */
package assignment4; // cannot be in default package
import java.util.*;
import java.io.*;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    private static final String PROMPT = "critters>";
    static Scanner kb;  // scanner connected to keyboard input, or input file
    private static String inputFile;    // input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;  // if test specified, holds all console output
    private static String myPackage;    // package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;    // if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));          
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }

        /* Do not alter the code above for your submission. */
        /* Write your code below. */

        boolean done = false;
        
        while(!done){
            System.out.print(PROMPT);


            String in = kb.nextLine();

            String[] inputArgs = in.split("\\s+");
            boolean invalid = false;

            switch(inputArgs[0]){
                case "quit":
                    if(inputArgs.length == 1){
                        done = true;
                    } else {
                        invalid = true;
                    }
                    break;

                case "show":
                    if(inputArgs.length == 1){
                        Critter.displayWorld();
                    } else {
                        invalid = true;
                    }
                    break;

                case "step":
                    int numSteps = 0;
                    if(inputArgs.length == 1){
                        numSteps = 1;

                    } else if(inputArgs.length == 2){
                        try{
                            numSteps = Integer.parseInt(inputArgs[1]);
                        } catch(NumberFormatException e){
                            invalid = true;
                        }

                    } else {
                        invalid = true;
                    }

                    if(!invalid){
                        for(int i =0; i < numSteps; i++){
                            Critter.worldTimeStep();
                        }
                    }
                    break;

                case "seed":
                    if(inputArgs.length == 2){
                        try{
                            int seed = Integer.parseInt(inputArgs[1]);
                            Critter.setSeed(seed);
                        } catch(NumberFormatException e){
                            invalid = true;
                        }
                    } else {
                        invalid = true;
                    }
                    break;
                    
                case "make":
                    int numToMake = 0;
                    if(inputArgs.length == 3){
                        try{
                            numToMake = Integer.parseInt(inputArgs[2]);
                        } catch(NumberFormatException e){
                            invalid = true;
                        }
                    } else if(inputArgs.length == 2){
                        numToMake = 1;
                    } else {
                        invalid = true;
                    }

                    if(!invalid){
                        try{
                            for(int i = 0; i < numToMake; i++){
                                Critter.makeCritter(inputArgs[1]);
                            }
                        } catch(InvalidCritterException e){
                            invalid = true;
                        }
                    }
                    break;

                case "stats":
                    if(inputArgs.length == 2){
                        try{
                            java.util.List<Critter> statList = Critter.getInstances(inputArgs[1]);
							
							Class<?>[] params = {List.class};

							
							Class<? extends Critter> stats = Class.forName(myPackage + "." + inputArgs[1])
								.asSubclass(Critter.class);
							
                            stats.getMethod("runStats", params).invoke(null, statList);
                        } catch(Exception e){
                            invalid = true;
                        }
                    } else {
                        invalid = true;
                    }
                    break;
                
                default:
                    System.out.println("invalid command: " + in);
            }


            if(invalid){
                System.out.println("error processing: " + in);
            }
        }
        
        /* Write your code above */
        System.out.flush();

    }

}
