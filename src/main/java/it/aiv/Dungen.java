package it.aiv;

import java.io.Console;
import java.util.*;

public class Dungen 
{
	int _rows,_cols,_steps,_seed;
	int[][] _dungenMap;
	int _border;
	Boolean _horizontalBlanking;

	int[][] _roomMap;
	ArrayList<Room> _rooms;
	
	public Dungen(int rows, int cols,int steps,int seed,int border,Boolean blanking) 
	{
		_horizontalBlanking=blanking;
		_border=border;
		_rows=rows;
		_cols=cols;
		_steps=steps;
		_seed=seed;
		_dungenMap=new int[rows][cols];		
	}
	
	public int[][] floodMap()
	{
		if(_rooms==null)
			findRooms();
		
		_rooms.sort(new Room.RoomCustomComparator());
		
		for (int i = 1; i < _rooms.size(); i++) 
		{
			Room roomToFill=_rooms.get(i);
			int cellValueToFill =roomToFill.get_id();
			floodRoom(cellValueToFill, roomToFill.FirstCellX, roomToFill.FirstCellY);
		}
		
		return _dungenMap;
	}
	
	public void floodRoom(int roomToFill,int x,int y)
	{
		if(_roomMap[x][y]!=roomToFill)
			return;
		
		_roomMap[x][y]=1;
		_dungenMap[x][y]=1;
		
		floodRoom(roomToFill,x,y+1);
		floodRoom(roomToFill,x,y-1);
		floodRoom(roomToFill,x-1,y);
		floodRoom(roomToFill,x+1,y);
		
		return;
	}		
	
	public int[][] findRooms()
	{
		_roomMap = new int[_dungenMap.length][];
		for(int i = 0; i < _dungenMap.length; i++)
			_roomMap[i] = _dungenMap[i].clone();
		
		int roomIndex=2;
		_rooms=new ArrayList<Room>();		
		
		for (int i = 0; i<_rows; i++) 
		{
			for (int j = 0; j<_cols; j++) 
			{
				if(_roomMap[i][j]==0)
				{			
					int roomSize = recognizeRoom(roomIndex, i, j);
					_rooms.add(new Room(roomIndex, roomSize,i,j));
					roomIndex+=1;								
				}
			}
		}	
		return _roomMap;
			
	}
	
	private int recognizeRoom(int roomIndex,int x,int y)
	{
		if(_roomMap[x][y]>0)
			return 0;
		
		_roomMap[x][y]=roomIndex;
		
		int upCell= recognizeRoom(roomIndex,x,y+1);
		int downCell= recognizeRoom(roomIndex,x,y-1);
		int rightCell= recognizeRoom(roomIndex,x-1,y);
		int leftCell= recognizeRoom(roomIndex,x+1,y);
		
		return upCell+downCell+rightCell+leftCell+1;
	}
	
	public int[][] build()
	{
		for (int c = 0; c < _steps; c++) 
        {
			int[][] nextMatrix= new int[_rows][_cols];		
			
			for (int i = 0; i < _rows; i++) 
			{

				for (int j = 0; j < _cols; j++) 
				{
					if(i==0 || j==0 || i==_rows-1 || j==_cols-1)
					{
						nextMatrix[i][j]=_dungenMap[i][j];
					}
					else
					{
						int cellValue=_dungenMap[i][j];
						int wallsAround=countWallsAroundCell(i, j);
						
						if(cellValue==0)
						{
							if(wallsAround>4)
							{
								nextMatrix[i][j]=1;	
							}
							else
							{
								nextMatrix[i][j]=0;	
							}
								
						}
						else
						{
							if(wallsAround<4)
							{
								nextMatrix[i][j]=0;	
							}
							else
							{
								nextMatrix[i][j]=1;	
							}
						}					
					}
				}			
			}			
			_dungenMap=nextMatrix;
		}
		
		return _dungenMap;
	}
	
	private void horizontalBlanking()
	{
		int middle=_rows/2;
		int range=1;
		
		for (int i = middle-range; i<=middle+range; i++) 
		{
			for (int j = 1; j<_cols-1; j++) 
			{
				_dungenMap[i][j]=0;
			}
		}
	}
	
	public int[][] cropMap()
	{
		int top=_rows,bottom=0;
		int left=_cols,right=0;
		
		for (int i = 0; i < _rows; i++) 
		{
		    for (int j = 0; j < _cols; j++) 		    
		    {
		    	int cellValue=_dungenMap[i][j];
		    	
		    	if(cellValue==0)
		    	{
		    		if(i<top)
		    		{
		    			top=i;
		    		}
		    		
		    		if(i>bottom)
		    		{
		    			bottom=i;
		    		}	
		    		
		    		if(j<left)
		    		{
		    			left=j;
		    		}
		    		
		    		if(j>right)
		    		{
		    			right=j;
		    		}
		    	}
		    }
		}
		
		int borderThickness=_border*2;
		//adding 1 since the cordinates starts from 0
		int croppedMapCols=right - left + 1 + borderThickness;
		int croppedMapRows=bottom - top + 1 + borderThickness;

		int[][] croppedMap=new int[croppedMapRows][croppedMapCols];
		
		for (int i = 0; i<croppedMapRows; i++) 
		{
			for (int j = 0; j<croppedMapCols; j++) 
			{
				croppedMap[i][j]=1;
			}
		}	

		for (int i = 0; i<croppedMapRows-borderThickness; i++) 
		{
			for (int j = 0; j<croppedMapCols-borderThickness; j++) 
			{
				croppedMap[i+_border][j+_border]=_dungenMap[i+top][j+left];
			}
		}
		return croppedMap;
	}
	
	private int countWallsAroundCell(int x,int y)
	{
		int range=1,wallCount=0;
		
		for (int i = x-range; i<=x+range; i++) 
		{
			for (int j = y-range; j<=y+range; j++) 
			{
				if(i==x&&y==j)
					continue;
				if(_dungenMap[i][j]==1)
					wallCount+=1;
			}
		}
		return wallCount;
	}
	
	public int[][] randomizeMap() 
	{
		if(_rooms!=null)
			_rooms=null;
		
		Random randomGenerator=new Random(_seed);
		
        for (int i = 0; i <_rows ; i++) 
        {
			for (int j = 0; j < _cols; j++) 
			{
				if(i==0 || j==0 || i==_rows-1 || j==_cols-1)
				{
					_dungenMap[i][j]=1;
				}
				else
					_dungenMap[i][j]= randomGenerator.nextInt(2);	
			}
		}
        
        if(_horizontalBlanking)
        	horizontalBlanking();
        return _dungenMap;
	}
	
	public void printRooms()
	{
		for (Room room : _rooms) 
		{
			System.out.println("Found room id: "+room.get_id()+" size: "+room.get_size());
		}
	}
	
	public static void printDungeon(int[][] dungeon, boolean debug)
	{
		for (int i = 0; i < dungeon.length; i++) {
		    for (int j = 0; j < dungeon[i].length; j++) 		    
		    {
		    	if(debug)
		    	{
		    		System.out.print(dungeon[i][j] + " ");
		    		continue;
		    	}
		    	//░▓ 176-178
		    	if(dungeon[i][j]==1)
		    	{
		    		System.out.print("▓▓");
		    	}
		    	else
		    	{
		    		System.out.print("░░");
		    	}
		    	
		    }
		    System.out.println();
		}
	}
}
