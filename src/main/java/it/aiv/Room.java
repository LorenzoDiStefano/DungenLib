package it.aiv;

import java.util.Comparator;


public class Room implements Comparable<Room>
{
	int _id;
	int _size;
	
	int FirstCellX,FirstCellY;
	
	public int get_id() 
	{
		return _id;
	}

	public int get_size() 
	{
		return _size;
	}

	public static class RoomCustomComparator implements Comparator<Room> 
	{
	    public int compare(Room o1, Room o2) 
	    {
	        return o1.compareTo(o2);
	    }
	}
	
	public Room(int id, int size,int x, int y) 
	{
		_id=id;
		_size=size;
		FirstCellX=x;
		FirstCellY=y;
	}

	public int compareTo(Room o) 
	{
        if(this._size>o._size)
        	return -1;
        else if(this._size<o._size)
        	return 1;
        else
        	return 0;
	}
}
