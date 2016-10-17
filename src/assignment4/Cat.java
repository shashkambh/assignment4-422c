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

import java.util.*;

/*
 * This Cat is extremely capricious and decides almost everything based on the
 * random number generator. However, because Craig already uses the letter C, it
 * hates Craigs and will fight them at any opportunity. The more fights a Cat has
 * won, the more aggressive it is.
 * It will also reproduce somewhat frequently and run around every other turn.
 */
public class Cat extends Critter{

    private static int numCats = 0;
    private int catId;
    private int numFights;
    private int turnsAlive;
    private boolean running;

    /**
     * Sole constructor. Creates a new Cat
     */
    public Cat(){
        catId = numCats;
        numCats++;
        numFights = 0;
        turnsAlive++;
        running = (Critter.getRandomInt(2) == 0);
    }
    
    /**
     * String representation of a Cat for output to user.
     * @return A single letter representation of a Cat
     */
    public String toString(){
        return "A";
    }

    /**
     * Checks if a Cat will fight with another Critter.
     * @param other The string representation of the other critter
     * @return true if the cat will fight, false otherwise
     */
    public boolean fight(String other){
        boolean willFight = false;

        if(other.equals("@") || other.equals("C")){
            willFight = true;
        } else {
            if(running || Critter.getRandomInt(2 + numFights / 2) != 0){
                willFight = true;
            } else {
                walk(Critter.getRandomInt(8));
            }
        }

		if(willFight) numFights++;
		
        return willFight;
    }

    /**
     * Preforms one timestep for the Cat.
     */
    public void doTimeStep(){
        running = !running;

        if(running){
            run(Critter.getRandomInt(8));
        } else {
			if(getEnergy() >= Params.min_reproduce_energy){
				Cat child = new Cat();
				reproduce(child, Critter.getRandomInt(8));
			}
        }

        turnsAlive++;
        
    }

    /**
     * Lists the stats of the Cats passed in from the general population.
     * @param cats A list of all cats in the population right now
     */
    public static void runStats(List<Critter> cats){
        int numNoFights = 0;

        int maxTurns = 0;
		ArrayList<Integer> elders = new ArrayList<>();

        int maxFights = 0;
        int bestFighter = 0;
        
        
        for(Critter e : cats){
            Cat cat = (Cat) e;

            if(cat.numFights == 0){
                numNoFights++;
            } else if(cat.numFights >= maxFights){
                maxFights = cat.numFights;
                bestFighter = cat.catId;
            }

            if(cat.turnsAlive > maxTurns){
				elders.clear();
                maxTurns = cat.turnsAlive;
				elders.add(cat.catId);
            } else if(cat.turnsAlive == maxTurns){
				elders.add(cat.catId);
			}
        }

        System.out.println("There have been " + numCats + " cats in total.");
        System.out.println("Of those, " + cats.size() + " cats are currently alive.");
        System.out.println(cats.size() - numNoFights + " have survived a fight.");
		if(maxFights != 0){
			System.out.println("Cat number " + bestFighter + " is the best fighter.");
		}

		if(elders.size() == 1){
			System.out.println("Cat number " + elders.get(0) + " rules the cats, having lived for " + maxTurns + " step(s).");
		} else if(elders.size() > 1){
			System.out.print("Cats number ");

			for(int i = 0; i < elders.size() - 1; i++){
				System.out.print(elders.get(i) + " ");
			}

			System.out.println("and " + elders.get(elders.size()) + " rule the cats, having lived for " + maxTurns + " step(s).");
		}

    }

}
