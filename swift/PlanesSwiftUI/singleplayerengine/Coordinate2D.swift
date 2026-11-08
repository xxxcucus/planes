
struct Coordinate2D : Equatable {

    var x: Int
    var y: Int
    
    func adding(_ other: Coordinate2D) -> Coordinate2D {
        Coordinate2D(x: x + other.x, y: y + other.y)
    }
}
