/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Shashank Kambhampati
 * skk834
 * 16445
 * Pranav Harathi
 * sh44674
 * 16460
 * Slip days used: <0>
 * Fall 2016
 */
package assignment4;

import java.util.List;
import java.lang.reflect.Modifier;

/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	
	protected final void walk(int direction) {
		go(direction, 1);
	}
	
	protected final void run(int direction) {
		go(direction, 2);
	}

	private void go(int direction, int distance){
		if(direction > 4){
			y_coord += distance;

			y_coord = y_coord >= Params.world_height ? y_coord - Params.world_height : y_coord;
			
		} else if(direction > 0 && direction < 4){
			y_coord -= distance;

			y_coord = y_coord < 0 ? y_coord + Params.world_height : y_coord;
		}

		if(direction > 2 && direction < 6){
			x_coord -= distance;

			x_coord = x_coord >= Params.world_width ? x_coord - Params.world_width : x_coord;
		} else if(direction < 2 || direction > 6){
			x_coord += distance;

			x_coord = x_coord < 0 ? x_coord + Params.world_width : x_coord;
		}

	}
	
	protected final void reproduce(Critter offspring, int direction) {
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
			toMake = Class.forName(critter_class_name).asSubclass(Critter.class);
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
			search = Class.forName(critter_class_name).asSubclass(Critter.class);
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
	
	public static void worldTimeStep() {
		// TODO conflict resolution, add new Critters
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
		
		for(int i = population.size() - 1; i >= 0; i--){
			Critter toCheck = population.get(i);
			toCheck.energy -= Params.rest_energy_cost;
			if(toCheck.energy <= 0){
				population.remove(i);
			}
		}
	}
	
	public static void displayWorld() {
		String[][] grid = new String[Params.world_height][Params.world_width];
		for(Critter c : population) {
			grid[c.x_coord][c.y_coord] = c.toString();
		}
		// print grid
		for(int r = 0; r < grid.length + 2; r++) {
			for(int c = 0; c < grid[r].length + 2; c++) {

				if(c == 0) {
					if(r == 0 || r == grid.length + 1) System.out.print("+");
					else System.out.print("|");
					continue;
				} else if(c == grid.length + 1) {
					if(r == 0 || r == grid.length + 1) System.out.print("+");
					else System.out.print("|");
					continue;
				}

				if(r == 0 || r == grid.length + 1) {
					System.out.print("-");
					continue;
				}

				System.out.print(grid[r][c]);
			}
		}
		System.gc(); // just in case
	}
}
