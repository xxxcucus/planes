import com.google.common.truth.Truth;
import com.planes.common.Coordinate2D;
import com.planes.common.Orientation;
import com.planes.common.Plane;
import com.planes.common.PlanePointIterator;

import org.junit.Test;

import java.util.HashMap;
import java.util.Vector;

import static com.google.common.truth.Truth.assertThat;


public class PlanePointIteratorTest {

    @Test
    public void PlanePointIterator_GenerateListNorthSouth() {

        Plane pl = new Plane(0, 0, Orientation.NorthSouth);
        PlanePointIterator ppi = new PlanePointIterator(pl);

        Vector<Coordinate2D> planePoints = new Vector<Coordinate2D>();
        Vector<Integer> yCoords = new Vector<Integer>();

        while(ppi.hasNext()) {
            Coordinate2D c = ppi.next();
            planePoints.add(c);
            yCoords.add(c.y());
        }

        assertThat(planePoints.size() == 10).isTrue();

        HashMap<Integer, Integer> histoY = computeHisto(yCoords);

        assertThat(histoY.get(0) == 1).isTrue();
        assertThat(histoY.get(1) == 5).isTrue();
        assertThat(histoY.get(2) == 1).isTrue();
        assertThat(histoY.get(3)== 3).isTrue();
    }

    @Test
    public void PlanePointIterator_GenerateListSouthNorth() {

        Plane pl = new Plane(0, 0, Orientation.SouthNorth);
        PlanePointIterator ppi = new PlanePointIterator(pl);

        Vector<Coordinate2D> planePoints = new Vector<Coordinate2D>();
        Vector<Integer> yCoords = new Vector<Integer>();

        while(ppi.hasNext()) {
            Coordinate2D c = ppi.next();
            planePoints.add(c);
            yCoords.add(c.y());
        }

        assertThat(planePoints.size() == 10).isTrue();

        HashMap<Integer, Integer> histoY = computeHisto(yCoords);

        assertThat(histoY.get(0) == 1).isTrue();
        assertThat(histoY.get(-1) == 5).isTrue();
        assertThat(histoY.get(-2) == 1).isTrue();
        assertThat(histoY.get(-3)== 3).isTrue();
    }

    @Test
    public void PlanePointIterator_GenerateListEastWest() {

        Plane pl = new Plane(0, 0, Orientation.EastWest);
        PlanePointIterator ppi = new PlanePointIterator(pl);

        Vector<Coordinate2D> planePoints = new Vector<Coordinate2D>();
        Vector<Integer> xCoords = new Vector<Integer>();

        while(ppi.hasNext()) {
            Coordinate2D c = ppi.next();
            planePoints.add(c);
            xCoords.add(c.x());
        }

        assertThat(planePoints.size() == 10).isTrue();

        HashMap<Integer, Integer> histoX = computeHisto(xCoords);

        assertThat(histoX.get(0) == 1).isTrue();
        assertThat(histoX.get(1) == 5).isTrue();
        assertThat(histoX.get(2) == 1).isTrue();
        assertThat(histoX.get(3)== 3).isTrue();
    }

    @Test
    public void PlanePointIterator_GenerateListWestEast() {

        Plane pl = new Plane(0, 0, Orientation.WestEast);
        PlanePointIterator ppi = new PlanePointIterator(pl);

        Vector<Coordinate2D> planePoints = new Vector<Coordinate2D>();
        Vector<Integer> xCoords = new Vector<Integer>();

        while(ppi.hasNext()) {
            Coordinate2D c = ppi.next();
            planePoints.add(c);
            xCoords.add(c.x());
        }

        assertThat(planePoints.size() == 10).isTrue();

        HashMap<Integer, Integer> histoX = computeHisto(xCoords);

        assertThat(histoX.get(0) == 1).isTrue();
        assertThat(histoX.get(-1) == 5).isTrue();
        assertThat(histoX.get(-2) == 1).isTrue();
        assertThat(histoX.get(-3)== 3).isTrue();
    }


    private HashMap<Integer, Integer> computeHisto(final Vector<Integer> values) {
        HashMap<Integer, Integer> retVal = new HashMap<Integer, Integer>();

        for (Integer val : values) {
            if (retVal.containsKey(val)) {
                retVal.put(val, retVal.get(val) + 1);
            } else {
                retVal.put(val, 1);
            }
        }

        return retVal;
    }
}
