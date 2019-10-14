package gui.building;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class Building
{
    private
    int xSize, ySize; //
    private ArrayList<Room> allRooms; //array of all rooms in the building
    private String createS; //original input string

    private ArrayList<Person> bodies; //array of all people in the building

    Building(String S)
    {
        setBuilding(S);
    }

    /**
     * Sets the building values to those specified in the parameters
     * Also initialises useful variables
     * @param bS building create string
     */
    public void setBuilding(String bS)
    {
        createS = bS;
        allRooms = new ArrayList<Room>();
        bodies = new ArrayList<Person>();

        String[] buildingStrings = bS.split(";");
        String[] buildingSizeString = buildingStrings[0].split(" ");
        xSize = Integer.valueOf(buildingSizeString[0]);
        ySize = Integer.valueOf(buildingSizeString[1]);

        for (int i = 1; i < buildingStrings.length; i++)
        {
            allRooms.add(new Room(buildingStrings[i]));
        }

        addPerson(1);
        updateLights();
    }

    public int getXSize()
    {
        return xSize;
    }

    public int getYSize()
    {
        return ySize;
    }

    public int getNoRooms()
    {
        return allRooms.size();
    }

    public Room getRoom(int index)
    {
        return allRooms.get(index);
    }

    public String getCreateString()
    {
        return createS;
    }

    public int getPersonCount() {
        return bodies.size();
    }

    public Person getPerson(int index) {
        return bodies.get(index);
    }

    /**
     * Selects a room from all the rooms in the building at random
     * @return random room object
     */
    private Room getRdmRoom()
    {
        int randRoomIndex = ThreadLocalRandom.current().nextInt(0, allRooms.size()); //gets random room in building
        Room random;// = new Room("0 0 5 5 0 2");
        random = allRooms.get(randRoomIndex);
        return random;
    }

    /**
     * Checks which room the given body is located in and returns -1 if they are not in a room
     * @param body Person object to check location of
     * @return index of room that body is located in or -1
     */
    private int checkRoomOfPerson(Person body)
    {
        int roomNum = -1;
        for (int i = 0; i < allRooms.size(); i++)
        {
            if (allRooms.get(i).isInRoom(body.getPos())) //not  in room
                roomNum = i;
        }
        return roomNum;
    }

    /**
     * Adds Person entities to the array stored in the building
     * @param num defines how many to add
     */
    public void addPerson(int num){
        for (int i = 0; i < num; i++) {
            bodies.add(new Person(getRandPos()));
        }
    }

    /**
     * Gives a random position in a random room for the person to travel to
     * @return random point in a room
     */
    private Point2D getRandPos() {
        int randRoomIndex = ThreadLocalRandom.current().nextInt(0, allRooms.size()); //gets random room in building
        Point2D bodyPos = allRooms.get(randRoomIndex).getRandomPointInRoom(); //sets position of person
        while (allRooms.get(randRoomIndex).isOnSolidThing(bodyPos)) {
            bodyPos = allRooms.get(randRoomIndex).getRandomPointInRoom(); //sets position of person
        }
        return bodyPos;
    }

    /**
     * Checks whether there is a person in the each room and turns the appropriate lights on if there is
     */
    public void updateLights() {
        ArrayList<Integer> personRoomIndexes = new ArrayList<>();
        for (int pi = 0; pi < bodies.size(); pi++) {
            personRoomIndexes.add(checkRoomOfPerson(bodies.get(pi))); //list of all rooms containing people
        }
        for (int ri = 0; ri < allRooms.size(); ri++) { //steps through all rooms
            for (Thing t: getRoom(ri).getThingsInRoom()) { //steps though all things
                if (t.getType() == "light") { //selects only lights
                    t.setState(false); //turns light off
                    for (int i = 0; i < personRoomIndexes.size(); i++) {
                        if (personRoomIndexes.get(i) == ri) //if the current room index matches a room
                            t.setState(true);               // index with a person in, turns the light on
                    }
                }
            }
        }
    }

    /**
     * converts the building into a reading string format using all methods from contained classes
     * @return a string with the building in a readable format
     */
    public String toString()
    {
        String buildingText = "Building size: " + xSize + ", " + ySize + "\n";
        int index = 0;
        for (Room r : allRooms)
        {
            index++;
            buildingText += "\n" + index + "." + r.toString() + "\n";
        }

        if (bodies.size() > 0) {
            for (Person b : bodies) {
                int bodyRoom = checkRoomOfPerson(b);
                buildingText += "\nperson in room " + bodyRoom + " at (" + b.getPos().getX() + ", " + b.getPos().getY() + " )";
            }
        }
        buildingText += "\n";

        return buildingText;
    }

    /**
     *
     * @param bodyIndex index of current body attempting to move
     * @return true if body still has points to travel to, false if not
     */
    boolean moveP(int bodyIndex){
        return bodies.get(bodyIndex).movePerson();
    }

    /**
     * Adds points for the body to travel to, first leaving it's current room then moving to a random one
     * @param bodyIndex index of current body attempting to move
     */
    void addDest(int bodyIndex) {
        Person body = bodies.get(bodyIndex);
        body.clearDest();

        Room bodyRoom, nextRoom;// = new Room("0 0 5 5 0 2");
        if (checkRoomOfPerson(body) != -1) //if person is in a room
        {
            bodyRoom = allRooms.get(checkRoomOfPerson(body));
            body.addDest(bodyRoom.getDoorPoint(-1)); //adds point inside door
            body.addDest(bodyRoom.getDoorPoint(1)); //adds point outside door
        }

        nextRoom = this.getRdmRoom(); //gets random room
        body.addDest(nextRoom.getDoorPoint(1)); //adds point outside door of that room
        body.addDest(nextRoom.getDoorPoint(-1)); //adds point inside door
        body.addDest(nextRoom.getRandomPointInRoom()); //adds a random point in that room
    }

    public static void main(String[] args)
    {
        // testing
        Building b = new Building("11 11;0 0 5 5 3 5;7 0 10 10 7 6;0 7 5 10 2 7");
        System.out.println(b.toString());
    }
}
