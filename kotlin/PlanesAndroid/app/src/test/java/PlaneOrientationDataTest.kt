package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.PlaneOrientationData
import com.planes.single_player_engine.Plane
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.Type
import com.planes.single_player_engine.PlanePointIterator
import com.planes.single_player_engine.GuessPoint
import org.junit.Test

class PlaneOrientationDataTest {
    @Test
    fun PlaneOrientationData_AreAllPointsTested() {
        val pod = PlaneOrientationData()
        Truth.assertThat(pod.areAllPointsChecked()).isTrue()
    }

    fun PlaneOrientationData_Discard() {
        val pl = Plane(0, 0, Orientation.NorthSouth)
        var pod = PlaneOrientationData(pl, false)

        var ppi = PlanePointIterator(pl)
        ppi.next()
        val pointOnPlane = ppi.next() //first point on plane after the head

        pod.update(GuessPoint(pointOnPlane.x(), pointOnPlane.y(), Type.Miss))
        Truth.assertThat(pod.m_discarded).isTrue()
    }
    fun PlaneOrientationData_HitNotTestedList() {
        val pl = Plane(0, 0, Orientation.NorthSouth)
        var pod = PlaneOrientationData(pl, false)

        var ppi = PlanePointIterator(pl);
        ppi.next();
        val pointOnPlane = ppi.next(); //first point on plane after the head

        val notTestedBeforeCount = pod.m_pointsNotTested.size;

        pod.update(GuessPoint(pointOnPlane.x(), pointOnPlane.y(), Type.Hit))
        Truth.assertThat(!pod.m_discarded).isFalse()
        val notTestedAfterCount = pod.m_pointsNotTested.size
        Truth.assertThat(notTestedAfterCount + 1 == notTestedBeforeCount).isTrue()
    }
}