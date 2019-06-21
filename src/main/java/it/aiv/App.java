package it.aiv;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	int rows,cols,steps,seed,border;
    	Boolean horizontalBlanking;
    	
    	if(args.length!=6)
    	{
    		System.out.println("Wrong amount of args, using default args");
    		rows=80;
    		cols=80;
    		steps=200;
    		seed=2525252;
    		border=1;
    		horizontalBlanking=false;
    	}
    	else
    	{
    		rows=Integer.parseInt(args[0]);
    		cols=Integer.parseInt(args[1]);
    		steps=Integer.parseInt(args[2]);
    		seed=Integer.parseInt(args[3]);
    		border=Integer.parseInt(args[4]);
    		horizontalBlanking=Boolean.parseBoolean(args[5]);
    	}
    	
    	Dungen dun=new Dungen(rows,cols,steps,seed,border,horizontalBlanking);
    	
    	int[][] RandomizedMap = dun.randomizeMap();
    	System.out.println("Randomized map:");
    	Dungen.printDungeon(RandomizedMap, false);
    	
    	int[][] GeneratedMap = dun.build();
    	System.out.println("Generated map:");
    	Dungen.printDungeon(GeneratedMap, false);
    	
    	int[][] RoomsMap=dun.findRooms();
    	
    	dun.printRooms();
    	System.out.println("Rooms map:");
    	Dungen.printDungeon(RoomsMap, true);
    	
    	int[][] FloodedMap=dun.floodMap();
    	System.out.println("Flooded map:");
    	Dungen.printDungeon(FloodedMap, false);
    	
    	int[][] CroppedMap =dun.cropMap();
    	System.out.println("Cropped map:");
    	Dungen.printDungeon(CroppedMap, false);
    	
    }

}
