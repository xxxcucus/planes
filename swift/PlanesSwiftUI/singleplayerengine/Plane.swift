
struct Plane : Equatable {
    
    var row : Int
    var col : Int
    var orient : Orientation
    
    init(row: Int, col: Int, orient: Orientation) {
        self.row = row
        self.col = col
        self.orient = orient
    }
    
    init() {
        self.init(row: 0, col: 0, orient: .northSouth)
    }

    init(qp: Coordinate2D, orient: Orientation) {
        self.init(row: qp.x, col: qp.y, orient: orient)
    }
    
    var head : Coordinate2D {
        Coordinate2D(x: row, y: col)
    }
    
    //translates a plane by a 2d translation vector
    func adding(qp: Coordinate2D) -> Plane {
        return Plane(row: self.row + qp.x, col: self.col + qp.y, orient: self.orient)
    }
    
    // Geometrical transformations
    // Clockwise rotation of planes
    mutating func rotate() {
        switch self.orient {
            case .northSouth:
                self.orient = .eastWest
            case .eastWest:
                self.orient = .southNorth
            case .southNorth:
                self.orient = .westEast
            case .westEast:
                self.orient = .northSouth
        }
    }
    
    //translation with given offset in a grid with row and col rows and columns
    //if the future head position is not valid do not translate
    mutating func translateWhenHeadPosValid(offsetX: Int, offsetY: Int, row: Int, col: Int) {
        guard (0..<row).contains(self.row + offsetX),
              (0..<col).contains(self.col + offsetY) else {
            return
        }
        self.row += offsetX
        self.col += offsetY
    }
    
    //other utility functions
    //tests whether a point is a plane's head
    func isHead(qp: Coordinate2D) -> Bool {
        qp == head
    }
    
    //checks if a certain point on the grid is on the plane
    func containsPoint(qp: Coordinate2D) -> Bool {
        let ppi = PlanePointIterator(pl: self)
        while (ppi.hasNext()) {
            let qp1 = ppi.next()
            if qp == qp1 {
                return true
            }
        }
        return false
    }
    
    //returns whether a plane position is valid
    // (the plane is completely contained inside the grid)
    // in a grid with row and col
    func isPositionValid(row: Int, col: Int) -> Bool {
        let ppi = PlanePointIterator(pl: self)
        while (ppi.hasNext()) {
            let qp = ppi.next()
            if !(0..<row).contains(qp.x) {
                return false
            }
            if !(0..<col).contains(qp.y) {
                return false
            }
        }
        return true
    }

    //displays the plane
    func toString() -> String {
        let orientationText: String
        switch self.orient {
            case .northSouth:
                orientationText = "NorthSouth"
            case .southNorth:
                orientationText = "SouthNorth"
            case .eastWest:
                orientationText = "EastWest"
            case .westEast:
                orientationText = "WestEast"
        }
        return "Plane head: \(self.row)-\(self.col) oriented: \(orientationText)"
    }
    
    // do not return the head
    func planePoints() -> [Coordinate2D] {
        let ppi = PlanePointIterator(pl: self)
        var retVal: [Coordinate2D] = []
        _ = ppi.next() // do not return the head
        while ppi.hasNext() {
            retVal.append(ppi.next())
        }
        return retVal
    }
}
