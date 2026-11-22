
class PlaneOrientationData {
    // the position of the plane
    private var plane: Plane

    // whether this orientation was discarded
    var discarded: Bool

    // points on this plane that were not tested
    // if discarded is false it means that all the tested points were hits
    var pointsNotTested: [Coordinate2D]

    // default initializer
    init() {
        self.plane = Plane(row: 0, col: 0, orient: Orientation.allCases[0])
        self.discarded = true
        self.pointsNotTested = []
    }

    // parameterized initializer
    init(pl: Plane, isDiscarded: Bool) {
        self.plane = pl
        self.discarded = isDiscarded
        self.pointsNotTested = []

        let ppi = PlanePointIterator(pl: plane)
        _ = ppi.next()
        while ppi.hasNext() {
            pointsNotTested.append(ppi.next())
        }
    }

    // update the info about this plane with another guess point
    // a guess point is a pair (position, guess result)
    func update(gp: GuessPoint) {
        // if plane is discarded return
        if discarded {
            return
        }

        // find the guess point in the list of points not tested
        guard let idx = pointsNotTested.firstIndex(of: Coordinate2D(x: gp.row, y: gp.col))
        else {
            return
        }

        // if point found
        // if dead and point is the plane's head remove the head from the list of untested points
        // TODO: the head point is not in the list so this is useless
        if gp.type == .dead && plane.isHead(qp: Coordinate2D(x: gp.row, y: gp.col)) {
            pointsNotTested.remove(at: idx)
            return
        }

        // if miss or dead discard plane
        if gp.type == .miss || gp.type == .dead {
            discarded = true
        }

        // if hit take point out of the list of points not tested
        if gp.type == .hit {
            pointsNotTested.remove(at: idx)
        }
    }

    // verifies if all the points in the current orientation were already checked
    func areAllPointsChecked() -> Bool {
        return pointsNotTested.isEmpty
    }
}
