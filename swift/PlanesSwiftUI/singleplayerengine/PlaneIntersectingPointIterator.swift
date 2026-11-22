
class PlaneIntersectingPointIterator: VectorIterator<Plane> {
    private let point: Coordinate2D

    init(point: Coordinate2D) {
        self.point = point
        super.init()
        generateList()
    }

    private func generateList() {
        m_internalList.removeAll()
        
        for i in (-5 + point.x)..<(6 + point.x) {
            for j in (-5 + point.y)..<(6 + point.y) {
                for orientation in Orientation.allCases {
                    let pl = Plane(row: i, col: j, orient: orientation)
                    
                    if pl.containsPoint(qp: point) {
                        m_internalList.append(pl)
                    }
                }
            }
        }

    }
}

