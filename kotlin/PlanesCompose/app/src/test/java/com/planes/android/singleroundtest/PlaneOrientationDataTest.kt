package com.planes.android.singleroundtest
import com.google.common.truth.Truth
import com.planes.singleplayerengine.PlaneOrientationData
import com.planes.singleplayerengine.Plane
import com.planes.singleplayerengine.Orientation
import com.planes.singleplayerengine.Type
import com.planes.singleplayerengine.PlanePointIterator
import com.planes.singleplayerengine.GuessPoint
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