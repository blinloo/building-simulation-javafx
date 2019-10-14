package gui.building;

import javafx.geometry.Point2D;

public abstract class Thing
{
    private Point2D topCorner, bottomCorner;
    private double width, height;
    private boolean state;
    private boolean solid; // can things move through or be placed on the object
    protected String type;
    protected String roomType;

    /**
     * Constructor for abstract thing class, sets position, width, height and state
     * @param p top left corner point
     * @param w width
     * @param h height
     * @param s boolean state, solid or not
     */
    protected Thing(Point2D p, double w, double h, boolean s) {
        topCorner = p;
        width = w;
        height = h;
        Point2D tempP = new Point2D(topCorner.getX() + width - 1, topCorner.getY() - height + 1);
        bottomCorner = tempP;
            // calculates bottom point based on width and height
        solid = s;
    }

    /**
     * gives the point at top left of the object
     * @return point of top left corner
     */
    public Point2D getTopCorner() {
        return topCorner;
    }

    /**
     * gives the point at bottom right of the object (useless for width:1, height:1 objects)
     * @return point of bottom right corner
     */
    public Point2D getBottomCorner() {
        return bottomCorner;
    }

    /**
     * gives the width of the object
     * @return width
     */
    public double getWidth() {
        return width;
    }

    /**
     * gives the height of the object
     * @return height
     */
    public double getHeight() {
        return height;
    }

    /**
     * gives the state of an object, i.e. whether other objects can be placed on it
     * @return boolean, true for solid, false for not
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * gives the type of object, e.g. chair, bed, light, ect.
     * @return type of object
     */
    public String getType() {
        return type;
    }

    /**
     * gives the room type that the object will be placed in, e.g. kitchen or bathroom
     * @return type of room
     */
    public String getRoomType() {
        return roomType;
    }

    /**
     * sets state of the object, only applicable to lights
     * @param st new state of the light, true on, false off
     */
    public void setState(boolean st) {
        state = st;
    }

    /**
     * gets state of the object, only applicable to lights
     * @return state of the light, true on, false off
     */
    public boolean getState() {
        return state;
    }
}
