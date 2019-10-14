package gui.building;

import javafx.geometry.Point2D;
import java.util.ArrayList;

public class Person
{
    private Point2D P; //location
    private ArrayList<Point2D> pDests; //destination points for body to follow

    Person(Point2D L)
    {
        P = L;
        pDests = new ArrayList<Point2D>();
    }

    public void setPosition(Point2D L)
    {
        P = L;
    }

    /**
     * gives current position of the person
     * @return
     */
    public Point2D getPos()
    {
        return P;
    }

    /**
     * Add a destination to the array list
     * @param p destination point
     */
    public void addDest(Point2D p)
    {
        pDests.add(p);
    }

    /**
     * Remove all destinations from the array list if not empty
     */
    public void clearDest()
    {
        if (pDests.size() > 0)
            pDests.clear();
    }

    /**
     * checks if a person is still moving and moves them if they are
     * @return true if still moving, false if not
     */
    public boolean movePerson()
    {
        double dx, dy;
        if (pDests.size() > 0) {
            dx = pDests.get(0).getX() - getPos().getX();
            dy = pDests.get(0).getY() - getPos().getY();

            if ((dx == 0) && (dy == 0)) {
                pDests.remove(0);
                if (pDests.size() == 0) {
                    return false;
                } else
                    return true;
            } else {
                if (dx > 1)
                    dx = 1;
                else if (dx < -1)
                    dx = -1;
                if (dy > 0)
                    dy = 1;
                else if (dy < 0)
                    dy = -1;
                //Makes sure it only moves one space in each direction

                P = (P.add(dx, dy));
                return true;
            }
        }
        return false;
    }

    /**
     * gives information, in a readable way, about the person
     * @return person information in a string
     */
    public String toString()
    {
        String personText = "Person at " + P.getX() + ", " + P.getY();
        return personText;
    }
}