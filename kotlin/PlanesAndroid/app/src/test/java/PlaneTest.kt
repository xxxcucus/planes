import com.google.common.truth.Truth
import com.planes_multiplayer.single_player_engine.Coordinate2D
import com.planes_multiplayer.single_player_engine.Orientation
import com.planes_multiplayer.single_player_engine.Plane
import com.planes_multiplayer.single_player_engine.PlanePointIterator
import org.junit.Test

class PlaneTest {
    @Test
    fun Plane_add() {
        val pl = Plane(0, 0, Orientation.EastWest)
        val pl_copy = pl.clone() as Plane
        val pl_add = pl.add(Coordinate2D(1, 2))
        Truth.assertThat(pl_add.head().x() == 1).isTrue()
        Truth.assertThat(pl_add.head().y() == 2).isTrue()
        Truth.assertThat(pl.orientation() === pl_copy.orientation()).isTrue()
    }

    @Test
    fun Plane_rotate() {
        val pl = Plane(0, 0, Orientation.EastWest)
        val pl_copy = pl.clone() as Plane
        for (i in 0..3) {
            pl.rotate()
            Truth.assertThat(pl.head().equals(pl_copy.head())).isTrue()
        }
        Truth.assertThat(pl.equals(pl_copy))
    }

    @Test
    fun Plane_translateWhenHeadPosValid() {
        val rows = 10
        val cols = 10
        val pl = Plane(rows / 2, 0, Orientation.NorthSouth)
        pl.translateWhenHeadPosValid(-1, -1, rows, cols)
        Truth.assertThat(pl.head()).isEqualTo(Coordinate2D(rows / 2, 0))
    }

    @Test
    fun Plane_containsPoint() {
        val pl = Plane(0, 0, Orientation.EastWest)
        val ppi = PlanePointIterator(pl)
        while (ppi.hasNext()) {
            val point = ppi.next()
            Truth.assertThat(pl.containsPoint(point)).isTrue()
        }
        val pl1 = Plane(0, 0, Orientation.WestEast)
        val ppi1 = PlanePointIterator(pl1)
        while (ppi1.hasNext()) {
            val point = ppi1.next()
            if (point.equals(pl1.head())) continue
            Truth.assertThat(pl.containsPoint(point)).isFalse()
        }
    }

    @Test
    fun Plane_isPositionValid() {
        val rows = 10
        val cols = 10
        val pl = Plane(rows / 2, cols / 2, Orientation.WestEast)
        Truth.assertThat(pl.isPositionValid(rows, cols)).isTrue()
        val pl1 = Plane(1, cols / 2, Orientation.NorthSouth)
        Truth.assertThat(pl1.isPositionValid(rows, cols)).isFalse()
    }
}