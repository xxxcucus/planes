import Testing

@Test func generateListNorthSouth() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.northSouth)
    let ppi = PlanePointIterator(pl: pl)
    var planePoints : [Coordinate2D] = []
    var yCoords : [Int] = []
    while (ppi.hasNext()) {
        let c = ppi.next()
        planePoints.append(c)
        yCoords.append(c.y)
    }
    
    #expect(planePoints.count == 10)
    let histoY = computeHisto(values: yCoords)
    #expect(histoY[0] == 1)
    #expect(histoY[1] == 5)
    #expect(histoY[2] == 1)
    #expect(histoY[3] == 3)
}

@Test func generateListSouthNorth() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.southNorth)
    let ppi = PlanePointIterator(pl: pl)
    var planePoints : [Coordinate2D] = []
    var yCoords : [Int] = []
    while (ppi.hasNext()) {
        let c = ppi.next()
        planePoints.append(c)
        yCoords.append(c.y)
    }
    
    #expect(planePoints.count == 10)
    let histoY = computeHisto(values: yCoords)
    #expect(histoY[0] == 1)
    #expect(histoY[-1] == 5)
    #expect(histoY[-2] == 1)
    #expect(histoY[-3] == 3)
}

@Test func generateListEastWest() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.eastWest)
    let ppi = PlanePointIterator(pl: pl)
    var planePoints : [Coordinate2D] = []
    var xCoords : [Int] = []
    while (ppi.hasNext()) {
        let c = ppi.next()
        planePoints.append(c)
        xCoords.append(c.x)
    }
    
    #expect(planePoints.count == 10)
    let histoX = computeHisto(values: xCoords)
    #expect(histoX[0] == 1)
    #expect(histoX[1] == 5)
    #expect(histoX[2] == 1)
    #expect(histoX[3] == 3)
}

@Test func generateListWestEast() async throws {
    
    let pl = Plane(row: 0, col: 0, orient: Orientation.westEast)
    let ppi = PlanePointIterator(pl: pl)
    var planePoints : [Coordinate2D] = []
    var xCoords : [Int] = []
    while (ppi.hasNext()) {
        let c = ppi.next()
        planePoints.append(c)
        xCoords.append(c.x)
    }
    
    #expect(planePoints.count == 10)
    let histoX = computeHisto(values: xCoords)
    #expect(histoX[0] == 1)
    #expect(histoX[-1] == 5)
    #expect(histoX[-2] == 1)
    #expect(histoX[-3] == 3)
}

private func computeHisto(values: [Int]) -> [Int : Int] {
    var retVal : [Int : Int] = [:]
    
    for value in values {
        retVal[value] = (retVal[value] ?? 0) + 1
    }
    return retVal
}
