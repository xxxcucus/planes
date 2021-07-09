import androidx.core.util.Pair;

import com.planes.common.Coordinate2D;
import com.planes.common.GuessPoint;
import com.planes.common.Orientation;
import com.planes.common.Plane;
import com.planes.common.PlanePointIterator;
import com.planes.common.Type;

import org.junit.Test;

import java.util.Arrays;
import java.util.Vector;

import static com.google.common.truth.Truth.assertThat;

public class PlaneGridTest {

    @Test
    public void PlaneGridTest_SaveSearchPlane() {

        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane pl1 = new Plane(rows / 2, 0, Orientation.NorthSouth);
        assertThat(pl1.isPositionValid(rows, cols)).isTrue();
        Plane pl2 = new Plane(0, 6, Orientation.EastWest);
        assertThat(pl2.isPositionValid(rows, cols)).isTrue();
        Plane pl3 = new Plane(6, 6, Orientation.EastWest);
        assertThat(pl3.isPositionValid(rows, cols)).isTrue();

        grid.savePlane(pl1);
        grid.savePlane(pl2);
        grid.savePlane(pl3);

        assertThat(grid.searchPlane(pl1) == 0).isTrue();
        assertThat(grid.searchPlane(pl2) == 1).isTrue();
        assertThat(grid.searchPlane(pl3) == 2).isTrue();

        assertThat(grid.searchPlane(0,0) >= 0).isFalse();

        assertThat(grid.searchPlane(pl1.row(), pl1.col()) == 0).isTrue();
        assertThat(grid.searchPlane(pl2.row(), pl2.col()) == 1).isTrue();
        assertThat(grid.searchPlane(pl3.row(), pl3.col()) == 2).isTrue();
    }

    @Test
    public void PlaneGridTest_RemoveSearchPlane() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane pl1 = new Plane(rows / 2, 0, Orientation.NorthSouth);
        assertThat(pl1.isPositionValid(rows, cols)).isTrue();
        Plane pl2 = new Plane(0, 6, Orientation.EastWest);
        assertThat(pl2.isPositionValid(rows, cols)).isTrue();
        Plane pl3 = new Plane(6, 6, Orientation.EastWest);
        assertThat(pl3.isPositionValid(rows, cols)).isTrue();

        grid.savePlane(pl1);
        grid.savePlane(pl2);
        grid.savePlane(pl3);


        Pair<Boolean, Plane> result = grid.removePlane(0);

        assertThat(result.first).isTrue();
        assertThat(result.second.equals(pl1)).isTrue();
        assertThat(grid.searchPlane(pl2) == 0).isTrue();
        assertThat(grid.searchPlane(pl3) == 1).isTrue();
        assertThat(grid.searchPlane(pl1) >= 0).isFalse();

        grid.removePlane(pl2);
        assertThat(grid.searchPlane(pl2) >= 0).isFalse();
        assertThat(grid.searchPlane(pl3) == 0).isTrue();
    }

    @Test
    public void PlaneGridTest_isPointOnPlane() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);
        grid.setPlanePoints(new Vector<Coordinate2D>(Arrays.asList(new Coordinate2D[]{new Coordinate2D(0, 0), new Coordinate2D(0, 1)})));

        assertThat(grid.isPointOnPlane(0,0).first).isTrue();
        assertThat(grid.isPointOnPlane(0,0).second == 0).isTrue();

        assertThat(grid.isPointOnPlane(0,1).first).isTrue();
        assertThat(grid.isPointOnPlane(0,1).second == 1).isTrue();

        assertThat(grid.isPointOnPlane(0,2).first).isFalse();
    }

    @Test
    public void PlaneGridTest_computePlanePointListNoOverlapAllInsideGrid() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane[] pl_list = new Plane[]{new Plane(rows / 2, 0, Orientation.NorthSouth),
                new Plane(0, 6, Orientation.EastWest), new Plane(6, 6, Orientation.EastWest)};

        grid.savePlane(pl_list[0]);
        grid.savePlane(pl_list[1]);
        grid.savePlane(pl_list[2]);

        grid.computePlanePointsList();

        for (int i = 0; i < 3; i++) {
            PlanePointIterator ppi = new PlanePointIterator(pl_list[i]);
            while (ppi.hasNext()) {
                Coordinate2D point = ppi.next();
                assertThat(grid.isPointOnPlane(point.x(), point.y()).first).isTrue();
            }
        }
    }

    @Test
    public void PlaneGridTest_computePlanePointListOutsideGrid() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane[] pl_list = new Plane[]{new Plane(rows / 2, 0, Orientation.NorthSouth),
                new Plane(0, 6, Orientation.WestEast), new Plane(6, 6, Orientation.EastWest)};

        grid.savePlane(pl_list[0]);
        grid.savePlane(pl_list[1]);
        grid.savePlane(pl_list[2]);

        grid.computePlanePointsList();

        assertThat(grid.isPlaneOutsideGrid()).isTrue();
    }

    @Test
    public void PlaneGridTest_computePlanePointListPlanesOverlap() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane[] pl_list = new Plane[]{new Plane(rows / 2, 0, Orientation.NorthSouth),
                new Plane(0, 6, Orientation.EastWest), new Plane(6, 6, Orientation.WestEast)};

        grid.savePlane(pl_list[0]);
        grid.savePlane(pl_list[1]);
        grid.savePlane(pl_list[2]);

        assertThat(grid.computePlanePointsList()).isFalse();
    }

    @Test
    public void PlaneGridTest_generateAnnotation() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        assertThat(grid.generateAnnotation(0, false)).isEqualTo(1);
        assertThat(grid.generateAnnotation(0, true)).isEqualTo(2);
    }

    @Test
    public void PlaneGridTest_getGuessResult() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane[] pl_list = new Plane[]{new Plane(rows / 2, 0, Orientation.NorthSouth),
                new Plane(0, 6, Orientation.EastWest), new Plane(6, 6, Orientation.EastWest)};

        grid.savePlane(pl_list[0]);
        grid.savePlane(pl_list[1]);
        grid.savePlane(pl_list[2]);

        grid.computePlanePointsList();

        assertThat(grid.getGuessResult(new Coordinate2D(rows / 2, 0))).isEqualTo(Type.Dead);
        assertThat(grid.getGuessResult(new Coordinate2D(rows / 2, 1))).isEqualTo(Type.Hit);
        assertThat(grid.getGuessResult(new Coordinate2D(rows / 2 + 1, 0))).isEqualTo(Type.Miss);
    }

    @Test
    public void PlaneGridTest_decodeAnnotation() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        assertThat(grid.decodeAnnotation(1)).isEqualTo(new Vector<Integer>(Arrays.asList(new Integer[]{0})));
        assertThat(grid.decodeAnnotation(2)).isEqualTo(new Vector<Integer>(Arrays.asList(new Integer[]{-1})));
        assertThat(grid.decodeAnnotation(5)).isEqualTo(new Vector<Integer>(Arrays.asList(new Integer[]{0, 1})));
        assertThat(grid.decodeAnnotation(9)).isEqualTo(new Vector<Integer>(Arrays.asList(new Integer[]{0, -2})));
    }

    @Test
    public void PlaneGridTest_addGuessPoints() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        grid.addGuess(new GuessPoint(0, 0,Type.Hit));
        Vector<GuessPoint> guesses = grid.getGuesses();
        assertThat(guesses).containsExactly(new GuessPoint(0, 0, Type.Hit));
    }

    @Test
    public void PlaneGridTest_getPlanePoints() {
        int rows = 10;
        int cols = 10;

        PlaneGridStubNoPlanes grid = new PlaneGridStubNoPlanes(rows, cols, 3, false);

        Plane[] pl_list = new Plane[]{new Plane(rows / 2, 0, Orientation.NorthSouth),
                new Plane(0, 6, Orientation.EastWest), new Plane(6, 6, Orientation.EastWest)};

        grid.savePlane(pl_list[0]);
        grid.savePlane(pl_list[1]);
        grid.savePlane(pl_list[2]);

        Pair<Boolean, Vector<Coordinate2D>> planePointsResult = grid.getPlanePoints(-1);
        assertThat(planePointsResult.second.size() == 0).isTrue();
        assertThat(planePointsResult.first).isFalse();

        Pair<Boolean, Vector<Coordinate2D>> planePointsResult1 = grid.getPlanePoints(0);
        assertThat(planePointsResult1.second.size() == 9).isTrue();
        assertThat(planePointsResult1.first).isTrue();
    }

}