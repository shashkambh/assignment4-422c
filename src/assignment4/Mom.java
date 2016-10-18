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
 * 'P' for parent, since Aliens are M
 */

public class Mom extends Critter{

    private boolean isTired;
    private int activeTurnsMax;
    private int tiredTurnsMax;
    private int numTurns;
    private int tiredTurns;

    public Mom(){
        isTired = false;
        activeTurnsMax = Critter.getRandomInt(10) + 3; // at least 3 steps, moms are strong
        tiredTurnsMax = activeTurnsMax / 2;
    }

    public String toString(){
        return "P";
    }

    public boolean fight(String other){
        if(isTired) {
            return other.equals("C");
        }
        return !other.equals("P");
    }

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
                reproduce(new Alien(), 2);
            }
            numTurns++;
            if(numTurns > activeTurnsMax) {
                numTurns = 0;
                isTired = true;
            }
        }
    }


    public static void runStats(java.util.List<Critter> moms){

    }
    

}
