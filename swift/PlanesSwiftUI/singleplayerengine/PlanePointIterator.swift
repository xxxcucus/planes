class PlanePointIterator: VectorIterator<Coordinate2D> {
    private let plane: Plane

    init(pl: Plane) {
        self.plane = pl
        super.init()
        generateList()
    }

    private func generateList() {
        let pointsNorthSouth: [Coordinate2D] = [
            Coordinate2D(x:0, y:0), Coordinate2D(x:0, y:1), Coordinate2D(x:-1, y:1),
            Coordinate2D(x:1, y:1), Coordinate2D(x:-2, y:1), Coordinate2D(x:2, y:1),
            Coordinate2D(x:0, y:2), Coordinate2D(x:0, y:3), Coordinate2D(x:-1, y:3),
            Coordinate2D(x:1, y:3)
        ]
        let pointsSouthNorth: [Coordinate2D] = [
            Coordinate2D(x:0, y:0), Coordinate2D(x:0, y:-1), Coordinate2D(x:-1, y:-1),
            Coordinate2D(x:1, y:-1), Coordinate2D(x:-2, y:-1), Coordinate2D(x:2, y:-1),
            Coordinate2D(x:0, y:-2), Coordinate2D(x:0, y:-3), Coordinate2D(x:-1, y:-3),
            Coordinate2D(x:1, y:-3)
        ]
        let pointsEastWest: [Coordinate2D] = [
            Coordinate2D(x:0, y:0), Coordinate2D(x:1, y:0), Coordinate2D(x:1, y:-1),
            Coordinate2D(x:1, y:1), Coordinate2D(x:1, y:-2), Coordinate2D(x:1, y:2),
            Coordinate2D(x:2, y:0), Coordinate2D(x:3, y:0), Coordinate2D(x:3, y:-1),
            Coordinate2D(x:3, y:1)
        ]
        let pointsWestEast: [Coordinate2D] = [
            Coordinate2D(x:0, y:0), Coordinate2D(x:-1, y:0), Coordinate2D(x:-1, y:-1),
            Coordinate2D(x:-1, y:1), Coordinate2D(x:-1, y:-2), Coordinate2D(x:-1, y:2),
            Coordinate2D(x:-2, y:0), Coordinate2D(x:-3, y:0), Coordinate2D(x:-3, y:1),
            Coordinate2D(x:-3, y:-1)
        ]

        let size = 10
        for i in 0..<size {
            switch plane.orient {
                case .northSouth:
                    m_internalList.append(pointsNorthSouth[i].adding(plane.head))
                case .southNorth:
                    m_internalList.append(pointsSouthNorth[i].adding(plane.head))
                case .westEast:
                    m_internalList.append(pointsWestEast[i].adding(plane.head))
                case .eastWest:
                    m_internalList.append(pointsEastWest[i].adding(plane.head))
            }
        }
    }
}

