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
 * Cows are peaceful grazing animals that like to follow a pattern.
 * They simply feed off of Algae and move in a nice box pattern. 
 * Poor guys, they never fight back.
 */

public class Critter4 extends Critter {

    private int patternSize;
    private int pathLength;
    private int pathCtr;
    private int direction;

    public Critter4(){
        patternSize = Critter.getRandomInt(6) + 3; // has to be at least three
        pathLength = (patternSize*patternSize) - ((patternSize - 2)*(patternSize - 2));
        direction = 0; // ->|<-| ...
        pathCtr = 1;
    }

    public String toString(){
        return "4";
    }

    public boolean fight(String other){
        return other.equals("@");
    }

    public void doTimeStep(){
        walk(direction);
        pathCtr++;
        if(pathCtr > pathLength) pathCtr = 0;
        if((pathCtr / patternSize) == 0) {
            direction = 0;
        } else if((pathCtr / patternSize) == 1) {
            direction = 6;
        } else if((pathCtr / patternSize) == 2) {
            direction = 4;
        } else if((pathCtr / patternSize) == 3) {
            direction = 2;
        }
    }


    public static void runStats(java.util.List<Critter> cows){
        
    }

}
