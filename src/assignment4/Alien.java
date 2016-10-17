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
 * Aliens have invaded the world of Critters. They want to abduct and study Earthlings
 * to prepare for their invasion. There are a lot of them on their spaceship (you
 * can add any number you'd like to the board after all) and all of them except the
 * leader on the ship are quite dumb, so they are relentlessly aggressive and run
 * around as much as possible. They won't fight each other, but they fight everything
 * else and try to keep holding patterns to search in.
 * They also don't reproduce unless they have a lot of extra energy.
 */

public class Alien extends Critter{

    private static int numAbducted = 0;
    private static int totalSent = 0;
    private int holdingPattern;
    private int patternStep = 0;
    private boolean abducting;

    public Alien(){
        totalSent++;
        abducting = false;
        holdingPattern = Critter.getRandomInt(3);
        patternStep = 0;
    }

    public String toString(){
        return "M";
    }

    public boolean fight(String other){
        if(abducting){
            abducting = false;
            numAbducted++;
        }

        if(!other.equals("M")){
            abducting = true;
            return true;
        } else {
            walk(Critter.getRandomInt(8));
            return false;
        }
    }

    public void doTimeStep(){
        if(abducting){
            abducting = false;
            numAbducted++;
        }

        switch(holdingPattern){
            case 0:
                run(patternStep);
                patternStep++;
                patternStep %= 8;
                break;
            case 1:
                run(patternStep / 2);
                patternStep++;
                patternStep %= 8;
                break;
            default:
                run(holdingPattern);
                patternStep++;
                if(patternStep > 30){
                    patternStep = 0;
                    holdingPattern++;
                    if(holdingPattern >=6){
                        holdingPattern = 2;
                    }
                }
                break;
        }

        if(getEnergy() >= Params.min_reproduce_energy * 2){
            reproduce(new Alien(), 2);
        }
    }

    public static void runStats(java.util.List<Critter> aliens){
        int dead = totalSent - aliens.size();

        int pattern1 = 0;
        int pattern2 = 0;
        int pattern3 = 0;

        for(Critter e : aliens){
            Alien alien = (Alien) e;
            if(alien.holdingPattern == 0) pattern1++;
            else if(alien.holdingPattern == 1) pattern2++;
            else pattern3++;
        }

        System.out.println(totalSent + " have been sent to attain victory.");
        System.out.println(dead + " have died in service to our great lord Boros.");
        System.out.println("Our warriors have procured specimens: " + numAbducted + " of them.");
        System.out.println("Holding patterns: 1->" + pattern1 + " 2->" + pattern2 + " 3->" + pattern3);
    }
    

}