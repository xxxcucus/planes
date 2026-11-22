import Testing

@Test func values() async throws {
    let point = Coordinate2D(x: 0, y: 0)
    let pipi = PlaneIntersectingPointIterator(point: point)
    var count = 0
    while (pipi.hasNext()) {
        let pl = pipi.next()
        #expect(pl.containsPoint(qp: point))
        count += 1
    }
    #expect(count > 0)
}
