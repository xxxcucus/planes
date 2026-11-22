
import Testing

@Test func areAllPointsTested() async throws {
    let pod = PlaneOrientationData()
    #expect(pod.areAllPointsChecked())
}

@Test func discard() async throws {
    let pl = Plane(row: 0, col: 0, orient: Orientation.northSouth)
    let pod = PlaneOrientationData(pl: pl, isDiscarded: false)

    let ppi = PlanePointIterator(pl: pl)
    _ = ppi.next()
    let pointOnPlane = ppi.next() //first point on plane after the head

    pod.update(gp: GuessPoint(row: pointOnPlane.x,col:  pointOnPlane.y, type: GuessType.miss))
    #expect(pod.discarded)
}

@Test func hitNotTestedList() async throws {
    let pl = Plane(row: 0, col: 0, orient: Orientation.northSouth)
    let pod = PlaneOrientationData(pl: pl, isDiscarded: false)

    let ppi = PlanePointIterator(pl: pl)
    _ = ppi.next()
    let pointOnPlane = ppi.next() //first point on plane after the head

    let notTestedBeforeCount = pod.pointsNotTested.count

    pod.update(gp: GuessPoint(row: pointOnPlane.x, col: pointOnPlane.y, type: GuessType.hit))
    #expect(!pod.discarded)
    let notTestedAfterCount = pod.pointsNotTested.count
    #expect(notTestedAfterCount + 1 == notTestedBeforeCount)
}
