package spy.g5;

import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

import spy.sim.Point;
import spy.sim.Record;
import spy.sim.CellStatus;
import spy.sim.Simulator;
import spy.sim.Observation;


public class Player implements spy.sim.Player {
    
    private ArrayList<ArrayList<Record>> records; // 2-dim list of cells on map (personal records)
    private int id;
    private Point loc; // Current location on map

    private ArrayList<ArrayList<Record>> landInfo; // similar to 'records' but global for dry land claims
    private ArrayList<ArrayList<Record>> mudInfo; // similar to 'records' but global for muddy land claims
    
    public void init(int n, int id, int t, Point startingPos, List<Point> waterCells, boolean isSpy)
    {
        this.id = id;
        this.records = new ArrayList<ArrayList<Record>>();
        for (int i = 0; i < 100; i++)
        {
            ArrayList<Record> row = new ArrayList<Record>();
            for (int j = 0; j < 100; j++)
            {
                row.add(null);
            }
	    System.out.println(row);
            this.records.add(row);
        }
    }
    
    public void observe(Point loc, HashMap<Point, CellStatus> statuses)
    {
        this.loc = loc;
	System.out.println("Called observe function =========");
        for (Map.Entry<Point, CellStatus> entry : statuses.entrySet())
        {
            Point p = entry.getKey();
            CellStatus status = entry.getValue();
            Record record = records.get(p.x).get(p.y);

	    System.out.println(p + " " + status + " " );
            if (record == null || record.getC() != status.getC() || record.getPT() != status.getPT())
            {
                ArrayList<Observation> observations = new ArrayList<Observation>();
                record = new Record(p, status.getC(), status.getPT(), observations);
                records.get(p.x).set(p.y, record);
            }
            record.getObservations().add(new Observation(this.id, Simulator.getElapsedT()));
        }
    }
    
    public List<Record> sendRecords(int id)
    {
        System.out.println("Called sendRecords ======");	  
        ArrayList<Record> toSend = new ArrayList<Record>();
        for (ArrayList<Record> row : records)
        {
            for (Record record : row)
            {
                if (record != null)
                {
                    toSend.add(record);
                }
            }
        }
        return toSend;
    }
    
    public void receiveRecords(int id, List<Record> records)
    {
	System.out.println("Called receiveRecords Command ========");
    }
    
    public List<Point> proposePath()
    {
        return null;
    }
    
    public List<Integer> getVotes(HashMap<Integer, List<Point>> paths)
    {
        for (Map.Entry<Integer, List<Point>> entry : paths.entrySet())
        {
            ArrayList<Integer> toReturn = new ArrayList<Integer>();
            toReturn.add(entry.getKey());
            return toReturn;
        }
        return null;
    }
    
    public void receiveResults(HashMap<Integer, Integer> results)
    {
       	System.out.println("Called receiveResults Command =======");
    }
    
    public Point getMove()
    {
	System.out.println("Called getMove Command =======");
        Random rand = new Random();
        int x = rand.nextInt(2) * 2 - 1;
        int y = rand.nextInt(2 + Math.abs(x)) * (2 - Math.abs(x)) - 1;
	System.out.println("Coordinates :" + x + " " + y);
        return new Point(x, y);
    }
}