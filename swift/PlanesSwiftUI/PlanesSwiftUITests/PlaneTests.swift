
import Testing

@Test func add() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.eastWest)
    let pl_copy = pl
    
    let pl_add = pl.adding(qp: Coordinate2D(x: 1, y: 2))
    
    #expect(pl_add.head.x == 1)
    #expect(pl_add.head.y == 2)
    #expect(pl.orient == pl_copy.orient)
}

@Test func rotate() async throws {
    
    var pl = Plane(row: 0, col: 0, orient: Orientation.eastWest)
    let pl_copy = pl
    
    for _ in 0...3 {
        pl.rotate()
        #expect(pl.head == pl_copy.head)
    }
        
    #expect(pl == pl_copy)
}

@Test func translateWhenHeadPosValid() async throws {
    
    let rows = 10
    let cols = 10
    var pl = Plane(row: rows / 2, col: 0, orient: Orientation.northSouth)
    
    pl.translateWhenHeadPosValid(offsetX: -1, offsetY: -1, row: rows, col: cols)
    #expect(pl.head.x == rows / 2)
    #expect(pl.head.y == 0)
}

@Test func containsPoint() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.eastWest)
    let ppi = PlanePointIterator(pl: pl)
    while (ppi.hasNext()) {
        let point = ppi.next()
        #expect(pl.containsPoint(qp: point))
    }
    
    let pl1 = Plane(row: 0, col: 0, orient: Orientation.westEast)
    let ppi1 = PlanePointIterator(pl: pl1)
    while (ppi1.hasNext()) {
        let point = ppi1.next()
        if point == pl1.head {
            continue
        }
        #expect(!pl.containsPoint(qp: point))
    }
}

@Test func isPosValid() async throws {
    
    let rows = 10
    let cols = 10
    let pl = Plane(row: rows / 2, col: cols / 2, orient: Orientation.westEast)
    #expect(pl.isPositionValid(row: rows, col: cols))
    let pl1 = Plane(row: 1, col: cols / 2, orient: Orientation.northSouth)
    #expect(!pl1.isPositionValid(row: rows, col: cols))
}
