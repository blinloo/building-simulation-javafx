package gui.building;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Room
{
    private Point2D
            p1, //top left corner of room
            p2, //bottom right corner of room
            pd; //coordinates of door

    private int
            width,
            height;

    private String type;

    private ArrayList<Thing> thingsInRoom;

    Room(String S) //Constructor, splits the input string for coordinates
    {
        String[] coordsStrings = S.split(" ");
        int[] coords = {0, 0, 0, 0, 0, 0, 0};

        for (int i = 0; i < coordsStrings.length; i++)
        {
            coords[i] = Integer.valueOf(coordsStrings[i]);
            //converts sting input into integer coordinate values
        }
        p1 = new Point2D(coords[0], coords[1]);
        p2 = new Point2D(coords[2], coords[3]);
        pd = new Point2D(coords[4], coords[5]);

        width = Math.abs((int)Math.round(p1.getX() - p2.getX()))+1;
        height = Math.abs((int)Math.round(p1.getY() - p2.getY()))+1;
        //defines all coordinate values

        int roomType = ThreadLocalRandom.current().nextInt(0, 3);
        switch (roomType) { //picks random room type to be
            case 0:
                type = "bathroom";
                break;
            case 1:
                type = "kitchen";
                break;
            case 2:
                type = "bedroom";
        }

        createThings();
    }

    Point2D getP1()
    {
        return p1; //returns top left point of room
    }

    Point2D getP2() {
        return p2;
    }

    Point2D getDoor() {
        return pd; //returns door point
    }

    ArrayList<Thing> getThingsInRoom()
    {
        return thingsInRoom;
    }

    public String getType()
    {
        return type;
    }

    /**
     * Converts the room into a readable string format, including all objects in the room
     * @return room display string
     */
    public String toString() //returns the coordinates as a string
    {
        String roomText = "\n";
        String prevThing;
        int count = 0;
        roomText += type + " from (" + p1.getX() + ", " + p1.getY() + ") " +
                "to (" + p2.getX() + ", " + p2.getY() + "). \n" +
                "door at (" + pd.getX() + ", " + pd.getY() + ")";
        prevThing = thingsInRoom.get(0).getType();
        for (Thing t: thingsInRoom)
        {
            if (prevThing != t.getType()) {
                roomText += "\n"+ prevThing + " count: " + count;
            } else
                count++;
            prevThing = t.getType();
        }
        return roomText;
    }

    /**
     * checks whether a point is within the confines of the room
     * @param p point to check
     * @return true is point is within room, false if not
     */
    public boolean isInRoom(Point2D p)
    {
        if (((p1.getX() < p.getX()) && (p2.getX() > p.getX())) && ((p1.getY() < p.getY()) && (p2.getY() > p.getY()))) //don't check whether blocking door here, recursion
            //Checks whether point is within coordinates of the room (Walls take up a space)
            return true;
        else
            return false;
    }

    /**
     * checks if a point will collide with solid objects in the room
     * @param p point to check
     * @return true if point is on a solid object false is not
     */
    public boolean isOnSolidThing(Point2D p)
    {
        boolean solid = false;
        for (Thing t : thingsInRoom)
        {
            if (t.isSolid()) {
                //goes through all x values that the thing takes up
                for (double xCheck = t.getTopCorner().getX(); xCheck > t.getBottomCorner().getX(); xCheck++)
                {
                    if (p.getX() == xCheck) { solid = true; }
                }
                for (double yCheck = t.getTopCorner().getY(); yCheck < t.getBottomCorner().getY(); yCheck--)
                {
                    if (p.getY() == yCheck) { solid = true; }
                }
            }
        }
        return solid;
    }

    /**
     * generates a random point in the room
     * @return the random point
     */
    public Point2D getRandomPointInRoom()
    {
        int x, y;
        x = ThreadLocalRandom.current().nextInt((int)Math.round(p1.getX()) + 1, (int)Math.round(p2.getX())-1);
        y = ThreadLocalRandom.current().nextInt((int)Math.round(p1.getY()) + 1, (int)Math.round(p2.getY())-1);
        return new Point2D(x, y);
    }

    /**
     * Selects a random point on the wall of the room
     * @return random point in wall
     */
    public Point2D getRandomWall()
    {
        int x, y;
        int[] xs = {(int)Math.round(p1.getX()), (int)Math.round(p2.getX())}; //sets x wall value
        int[] ys = {(int)Math.round(p1.getY()), (int)Math.round(p2.getY())}; //sets y wall values
        do {
            //ensures either the x or y value is along a wall of the room
            if (ThreadLocalRandom.current().nextInt(0, 2) == 0) {
                x = ThreadLocalRandom.current().nextInt((int) Math.round(p1.getX()) + 1, (int) Math.round(p2.getX()) - 1);
                y = ys[ThreadLocalRandom.current().nextInt(0, 2)];
            } else {
                x = xs[ThreadLocalRandom.current().nextInt(0, 2)];
                y = ThreadLocalRandom.current().nextInt((int) Math.round(p1.getY()) + 1, (int) Math.round(p2.getY()) - 1);
            }
        } while (new Point2D(x, y).equals(pd)); //exclude the door
        return new Point2D(x, y);
    }

    /**
     * Adds a new object to the room as specified by the user
     * @param type type of object to add
     * @param size whether object is small(1) or big(2)
     * @return true if adding object was successful, false if not
     */
    public boolean addThing(String type, int size)
    {
        int prevThings = thingsInRoom.size();
        if (size == 1)
            createSmallThing(type);
        else
            createBigThing(type, 2, 1);
        if (prevThings < thingsInRoom.size())
            return true;
        else
            return false;
    }

    /**
     * Creates an object of specified width and height in the room
     * @param w width of the thing
     * @param h height of the object
     */
    public void createBigThing(String type, int w, int h)
    {
        boolean validPos = false;
        Point2D p;
        int count = 1;
        do {
            p = getRandomPointInRoom();
            if (isFree(p) && isFree(new Point2D(p.getX()+(w-1), p.getY()+(h-1))))
            {
                validPos = true;
            }
            count++; //avoid chance of infinite looping
        } while ((count > 49) || (!validPos));
        if (count < 50)
        {
            switch (type) {
                case "table":
                    thingsInRoom.add(new Table(p, w, h));
                    break;
                case "bed":
                    thingsInRoom.add(new Bed(p, w, h));
                    break;
            }

        }
    }

    /**
    * Creates an width 1, height 1 object in a room
     * @param type type of object being created
    */
    public void createSmallThing(String type)
    {
        boolean validPos = false;
        Point2D p;
        int count = 1;
        do {
            p = getRandomPointInRoom();
            if (isFree(p))  // ensures not on top of other objects
            {
                validPos = true;
            }
            count++; //avoid chance of infinite looping
        } while ((!validPos) || (count > 49));
        if (count < 50)
        {
            switch (type) {
                case "chair": thingsInRoom.add(new Chair(p)); break;
                case "furnace": thingsInRoom.add(new Furnace(p)); break;
                case "toilet": thingsInRoom.add(new Toilet(p)); break;
                case "sink": thingsInRoom.add(new Sink(p)); break;
                case "bedsideTable": thingsInRoom.add(new BedsideTable(p)); break;
            }
        }
    }

    /**
     * Creates a window in the wall of the room
     */
    public void createWindow()
    {
        boolean validPos = false;
        int count = 1;
        Point2D p;
        do {
            p = getRandomWall();
            if (!isOnSolidThing(p))  // ensures not on top of other objects
            {
                validPos = true;
            }
            count++; //avoid chance of infinite looping
        } while ((!validPos) || (count > 49));
        if (count < 50)
        {
            thingsInRoom.add(new Window(p));
        }
    }

    /**
     * Adds objects to the room in their specified room type and in limited random amounts
     */
    private void createThings()
    {
        thingsInRoom = new ArrayList<>();



        if (type == "kitchen") { //if room type is kitchen
            int numTables = ThreadLocalRandom.current().nextInt(0, 2);
            int numChairs = ThreadLocalRandom.current().nextInt(0, 5);
            for (int tct = 0; tct < numTables; tct++) //adds tables to room
            {
                createBigThing("table", 2, 1);
            }
            for (int cct = 0; cct < numChairs; cct++) //adds chairs to room
            {
                createSmallThing("chair");
            }
            createSmallThing("furnace");
        }

        if (type == "bathroom") {
            createSmallThing("toilet");
            createSmallThing("sink");
        }

        if (type == "bedroom") {
            int numBeds = ThreadLocalRandom.current().nextInt(1, 3);
            for (int bct = 0; bct < numBeds; bct++) //adds beds to room
            {
                createBigThing("bed", 2, 1);
                createSmallThing("bedsideTable");
            }
        }

        if (type != "bathroom") {
            int numWindows = ThreadLocalRandom.current().nextInt(0, 8);
            for (int wct = 0; wct < numWindows; wct++) //adds windows to room
            {
                createWindow();
            }
        }

        thingsInRoom.add(new Light(new Point2D (p1.getX() + width/2, p1.getY() + height/2)));
    }

    /**
     * checks if a point is free from obstruction
     * @param tp point to check
     * @return true is point is free, false if not
     */
    private boolean isFree(Point2D tp)
    {
        boolean check = false;
        {
            if ((tp.getX() > p1.getX()) && (tp.getX() < p2.getX()) && (tp.getY() < p2.getY()) && (tp.getY() > p1.getY()) && !isOnSolidThing(tp))
                check = true;
        }
        return check;
    }

    /**
     * gives a point just inside or outside of the door
     * @param inOut if -1 get point inside room, if 1 get outside
     * @return the point either inside or outside the door
     */
    public Point2D getDoorPoint(int inOut)
    {
        Point2D outputP = new Point2D(0,0);
        if (inOut == -1) //point inside door of room
        {
            if (isInRoom(new Point2D(pd.getX(), pd.getY() - 1)))
                outputP = new Point2D(pd.getX(), pd.getY() - 1);
            if (isInRoom(new Point2D(pd.getX(), pd.getY() + 1)))
                outputP = new Point2D(pd.getX(), pd.getY() + 1);
            if (isInRoom(new Point2D(pd.getX() - 1, pd.getY())))
                outputP = new Point2D(pd.getX() - 1, pd.getY());
            if (isInRoom(new Point2D(pd.getX() + 1, pd.getY())))
                outputP = new Point2D(pd.getX() + 1, pd.getY());
        }
        if (inOut == 1) //point outside door of room
        {
            if (!isInRoom(new Point2D(pd.getX(), pd.getY() - 1)))
                outputP = new Point2D(pd.getX(), pd.getY() - 1);
            if (!isInRoom(new Point2D(pd.getX(), pd.getY() + 1)))
                outputP = new Point2D(pd.getX(), pd.getY() + 1);
            if (!isInRoom(new Point2D(pd.getX() - 1, pd.getY())))
                outputP = new Point2D(pd.getX() - 1, pd.getY());
            if (!isInRoom(new Point2D(pd.getX() + 1, pd.getY())))
                outputP = new Point2D(pd.getX() + 1, pd.getY());
        }
        return outputP;
    }

    public static void main(String[] args)
    {
        // testing
        Room r;
        r = new Room("0 0 5 5 0 2");
        System.out.println(r.toString());
        Point2D p = new Point2D(3, 4); //var = JOptionPane.showInputDialog(null, "Type stuff: ");
        if (r.isInRoom(p))
            System.out.println("(" + p.getX() + ", " + p.getY() + ") is within the room");
        else
            System.out.println("(" + p.getX() + ", " + p.getY() + ") NOPE");
    }
}