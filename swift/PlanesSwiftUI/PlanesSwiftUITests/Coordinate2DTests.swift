
import Testing

struct Coordinate2DTests {
    
    @Test func coordinate2DX() async throws {
        let c1 = Coordinate2D(x: 1, y: 1)
        #expect(c1.x == 1)
    }
    
    @Test func coordinate2DY() async throws {
        let c1 = Coordinate2D(x: 1, y: 1)
        #expect(c1.y == 1)
    }
    
    @Test func coordinate2DAdd() async throws {
        let c1 = Coordinate2D(x: 1, y: 0)
        let c2 = Coordinate2D(x: 2, y: 4)
        let c3 = Coordinate2D(x: 3, y: 4)
        #expect(c1.adding(c2) == c3)
    }
}
