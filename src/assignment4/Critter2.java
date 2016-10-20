/* EE422C Project 4 submission by
 * Shashank Kambhampati
 * skk834
 * 16445
 * Pranav Harathi
 * sh44674
 * 16460
 * Slip days used: 0
 * Fall 2016
 */
package assignment4;

/*
 * With all the chaos in the Critter world lately, everyone and their mother is
 * getting worried.  Especially the moms.  In fact, the mothers are so worried that
 * they will fight any other Critter they come across (except other mothers),
 * doing the most damage to Craig, who reminds them of their children.
 * They both run around frantically and reproduce quickly, recruiting other mothers
 * for their cause.  After a few fights, however, they have to sit down and rest,
 * so they don't move for a few time steps.
 *
 */

import java.util.ArrayList;

public class Critter2 extends Critter{

    private static int numMoms = 0;

    private boolean isTired;
    private int activeTurnsMax;
    private int tiredTurnsMax;
    private int numTurns;
    private int tiredTurns;
    private int momId;

    /**
     * Creates a Mom.
     */
    public Critter2(){
        isTired = false;
        activeTurnsMax = Critter.getRandomInt(10) + 3; // at least 3 steps, moms are strong
        tiredTurnsMax = activeTurnsMax / 2;
        momId = numMoms;
        numMoms++;
    }

    /**
     * String representation of a Mom for output to user.
     * @return A single letter representation of a Mom
     */
    public String toString(){
        return "2";
    }

    /**
     * Checks if a Mom will fight with another Critter.
     * @param other The string representation of the other critter
     * @return true if Mom is not tired (or Craig), false otherwise
     */
    public boolean fight(String other){
        if(isTired) {
            return other.equals("C");
        }
        return !other.equals("2");
    }

    /**
     * Performs one timestep for the Mom.
     */
    public void doTimeStep(){
        if(isTired) {
            tiredTurns++;
            if(tiredTurns > tiredTurnsMax) {
                tiredTurns = 0;
                isTired = false;
            }
        } else {
            run(Critter.getRandomInt(8));
            if(getEnergy() >= Params.min_reproduce_energy * 2){
                reproduce(new Critter2(), 2);
            }
            numTurns++;
            if(numTurns > activeTurnsMax) {
                numTurns = 0;
                isTired = true;
            }
        }
    }

    /**
     * Lists the stats of the Moms passed in from the general population.
     * @param moms A list of all Moms in the population right now
     */
    public static void runStats(java.util.List<Critter> moms){
        int active = 0;
        int tired = 0;

        ArrayList<Critter2> activeMoms = new ArrayList<>();
        for(Critter m : moms) {
            if(activeMoms.isEmpty()) {
                activeMoms.add((Critter2) m);
            } else {
                if(activeMoms.get(0).activeTurnsMax < ((Critter2) m).activeTurnsMax) {
                    activeMoms.clear();
                    activeMoms.add((Critter2) m);
                } else if(activeMoms.get(0).activeTurnsMax == ((Critter2) m).activeTurnsMax) {
                    activeMoms.add((Critter2) m);
                }
            }
            if(((Critter2) m).isTired) tired++;
            else active++;
        }
        System.out.print("Most active Moms by ID: ");
        for(Critter2 m: activeMoms) {
            System.out.print(m.momId + " ");
        }
        System.out.println();

        System.out.printf("There are %d total moms.\n", numMoms);
        System.out.printf("%d of them are active, and %d of them are resting.\n", active, tired);
    }
    

}
