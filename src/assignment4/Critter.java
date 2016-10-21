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
package assignment4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.lang.reflect.Modifier;

/**
 * Abstract class that all objects in the Critter world extend. Also manages the 
 * Critter world.
 */
public abstract class Critter {
    private static String myPackage;
    private static List<Critter> population = new java.util.ArrayList<Critter>();
    private static List<Critter> babies = new java.util.ArrayList<Critter>();

    // Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }
    
    private static boolean fighting = false;
    
    private static java.util.Random rand = new java.util.Random();
	/**
	 * Generates a random integer from 0 to max - 1 based on rand.
	 * @param max One more than the maximum number possible from this function
	 * @return An int from 0 to max - 1
	 */
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

	/**
	 * Sets the seed for the random number generator used in getRandomInt.
	 * @param new_seed The number the seed will be set to
	 */
    public static void setSeed(long new_seed) {
        rand = new java.util.Random(new_seed);
    }
    
    
    /* a one-character long string that visually depicts your critter in the ASCII interface */
    public String toString() { return ""; }
    
    private int energy = 0;
    protected int getEnergy() { return energy; }
    
    private int x_coord;
    private int y_coord;
    
    private boolean walked;

    /**
     * Calls go with a value of 1 in a specified direction and subtracts energy cost
     * @param direction direction given
     */
    protected final void walk(int direction) {
        if(!walked){
            walked = true;
            go(direction, 1);
        }

        energy -= Params.walk_energy_cost;
    }

    /**
     * Calls go with a value of 2 in a specified direction and subtracts energy cost
     * @param direction direction given
     */
    protected final void run(int direction) {
        if(!walked){
            walked = true;
            go(direction, 2);
        }

        energy -= Params.run_energy_cost;
    }

    /**
     * Moves Critter in given direction a given distance, wrapping the map on all four sides.
     * @param direction given direction
     * @param distance given distance to go
     */
    private void go(int direction, int distance){
        int oldy = y_coord;
        int oldx = x_coord;
    	
    	if(direction > 4){
            y_coord += distance;

            y_coord = y_coord >= Params.world_height ? y_coord - Params.world_height : y_coord;
            
        } else if(direction > 0 && direction < 4){
            y_coord -= distance;

            y_coord = y_coord < 0 ? y_coord + Params.world_height : y_coord;
        }

        if(direction > 2 && direction < 6){
            x_coord -= distance;

            x_coord = x_coord < 0 ? x_coord + Params.world_width : x_coord;
        } else if(direction < 2 || direction > 6){
            x_coord += distance;

            x_coord = x_coord >= Params.world_width ? x_coord - Params.world_width : x_coord;
        }
        
        if(fighting && isTaken(x_coord, y_coord)){
        	x_coord = oldx;
        	y_coord = oldy;
        }

    }

    /**
     * Checks if position is occupied.
     * @param x x coordinate to check
     * @param y y coordinate to check
     * @return true if taken, false, otherwise
     */
    private static boolean isTaken(int x, int y){
    	for(Critter e : population){
    		if(e.x_coord == x && e.y_coord == y){
    			return true;
    		}
    	}
    	return false;
    }

	/**
	 * Takes a new instance of Critter and adds it to the list of babies if possible.
	 * If the parent has enough energy, the offspring is assigned some of its energy and a location.
	 * @param offspring The new instance of Critter to be added
	 * @param direction The direction in which this offspring will be placed
	 */
    protected final void reproduce(Critter offspring, int direction) {
        if(this.energy >= Params.min_reproduce_energy){
            offspring.energy = this.energy / 2;
            this.energy -= offspring.energy;
            offspring.x_coord = this.x_coord;
            offspring.y_coord = this.y_coord;
            offspring.go(direction, 1);
            babies.add(offspring);
        }
    }

    public abstract void doTimeStep();
    public abstract boolean fight(String opponent);
    
    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
     * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
     * an Exception.)
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void makeCritter(String critter_class_name) throws InvalidCritterException {
        Class<? extends Critter> toMake;
        Critter toMakeInstance;
        
        try{
            toMake = Class.forName(myPackage + "." + critter_class_name).asSubclass(Critter.class);
        } catch(Throwable e){
            throw new InvalidCritterException(critter_class_name);
        }

        if(!Modifier.isAbstract(toMake.getModifiers())){
            try{
                toMakeInstance = toMake.newInstance();
            } catch(Exception e){
                throw new InvalidCritterException(critter_class_name);
            }
            
            toMakeInstance.energy = Params.start_energy;
            toMakeInstance.x_coord = getRandomInt(Params.world_width);
            toMakeInstance.y_coord = getRandomInt(Params.world_height);

            population.add(toMakeInstance);
        } else {
            throw new InvalidCritterException(critter_class_name);
        }
    }
    
    /**
     * Gets a list of critters of a specific type.
     * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        List<Critter> result = new java.util.ArrayList<Critter>();

        Class<? extends Critter> search;
        
        try{
            search = Class.forName(myPackage + "." + critter_class_name).asSubclass(Critter.class);
        } catch(Throwable e){
            throw new InvalidCritterException(critter_class_name);
        }

        for(Critter e : population){
            if(search.isInstance(e)){
                result.add(e);
            }
        }
        
        return result;
    }
    
    /**
     * Prints out how many Critters of each type there are on the board.
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            Integer old_count = critter_count.get(crit_string);
            if (old_count == null) {
                critter_count.put(crit_string,  1);
            } else {
                critter_count.put(crit_string, old_count.intValue() + 1);
            }
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();       
    }
    
    /* the TestCritter class allows some critters to "cheat". If you want to 
     * create tests of your Critter model, you can create subclasses of this class
     * and then use the setter functions contained here. 
     * 
     * NOTE: you must make sure that the setter functions work with your implementation
     * of Critter. That means, if you're recording the positions of your critters
     * using some sort of external grid or some other data structure in addition
     * to the x_coord and y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {
        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }
        
        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }
        
        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }
        
        protected int getX_coord() {
            return super.x_coord;
        }
        
        protected int getY_coord() {
            return super.y_coord;
        }
        

        /*
         * This method getPopulation has to be modified by you if you are not using the population
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }
        
        /*
         * This method getBabies has to be modified by you if you are not using the babies
         * ArrayList that has been provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.  Babies should be added to the general population 
         * at either the beginning OR the end of every timestep.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        population.clear();
        babies.clear();
    }
    
	/**
	 * Moves the world and all Critters in it forward by a timestep.
	 */
    public static void worldTimeStep() {
        /* Order of timeStep:
           doTimeSteps for all Critters
           Do fights for any conflicts
           updateRestEnergy for all Critters
           generate algae
           move babies to general population
         */
        for(Critter toStep : population){
            toStep.doTimeStep();
        }
        
        fighting = true;
        ArrayList<List<Critter>> conflicts = identifyConflicts();
        resolveConflicts(conflicts);
        fighting = false;
        
        for(int i = population.size() - 1; i >= 0; i--){
            Critter toCheck = population.get(i);
            toCheck.energy -= Params.rest_energy_cost;
            if(toCheck.energy <= 0){
                population.remove(i);
            }
            toCheck.walked = false;
        }

        for(int i = 0; i < Params.refresh_algae_count; i++){
			try{
				makeCritter("Algae");
			} catch(InvalidCritterException e){System.out.println("Something went very wrong.");}
        }

		population.addAll(babies);
		babies.clear();
    }

    /**
     * Given a list of Critter pairs, runs their fight methods
     * @param conflicts list of pairs of Critters on the same position
     */
    private static void resolveConflicts(ArrayList<List<Critter>> conflicts) {
        for(List<Critter> conflict : conflicts) {
            Critter critterA = conflict.get(0);
            Critter critterB = conflict.get(1);

			if(population.contains(critterA) && population.contains(critterB)){
				
				boolean Afight = critterA.fight(critterB.toString());
				boolean Bfight = critterB.fight(critterA.toString());
				boolean noFight = false;

				if(critterA.energy <= 0){
					population.remove(critterA);
					noFight = true;
				} 
				if(critterB.energy <= 0){
					population.remove(critterB);
					noFight = true;
				}
				
				if(!noFight && critterA.x_coord == critterB.x_coord && critterA.y_coord == critterB.y_coord){

					int AfightNum = Afight ? getRandomInt(critterA.energy) : 0;
					int BfightNum = Bfight ? getRandomInt(critterB.energy) : 0;
					Critter winner = AfightNum > BfightNum ? critterA : critterB;
					Critter loser = winner == critterA ? critterB : critterA;
					winner.energy += loser.energy / 2;
					population.remove(loser);
				}
			}
        }
    }

	/**
	 * Creates a list of all Critters occupying the same space
	 */
    private static ArrayList<List<Critter>> identifyConflicts() {
        HashMap<String, Critter> occupied = new HashMap<>();
        ArrayList<List<Critter>> conflicts = new ArrayList<>();
        for(Critter c : population) {
            String coords = String.format("%d %d", c.x_coord, c.y_coord);
            if(occupied.containsKey(coords)) {
                ArrayList<Critter> conflictPair = new ArrayList<>();
                conflictPair.add(c);
                conflictPair.add(occupied.get(coords));
                conflicts.add(conflictPair);
            } else {
                occupied.put(coords, c);
            }
        }
        return conflicts;
    }
    
	/**
	 * Prints a string grid of the world to standard output.
	 */
    public static void displayWorld() {
        String[][] grid = new String[Params.world_height][Params.world_width];
        
        for(Critter c : population) {
            grid[c.y_coord][c.x_coord] = c.toString();
        }
        // print grid
        for(int r = 0; r < grid.length + 2; r++) {
            for(int c = 0; c < grid[0].length + 2; c++) {
                
                if(c == 0) {
                    if(r == 0 || r == grid.length + 1) System.out.print("+");
                    else System.out.print("|");
                    continue;
                } else if(c == grid[0].length + 1) {
                    if(r == 0 || r == grid.length + 1) System.out.print("+");
                    else System.out.print("|");
                    continue;
                } else {
                    if(r == 0 || r == grid.length + 1) {
                        System.out.print("-");
                        continue;
                    } else {
                        if(grid[r - 1][c - 1] == null) {
                            System.out.print(" ");
                        } else {
                            System.out.print(grid[r - 1][c - 1]);
                        }
                    }
                }

                
            }
            System.out.println();
        }
        System.gc(); // just in case
    }
}
