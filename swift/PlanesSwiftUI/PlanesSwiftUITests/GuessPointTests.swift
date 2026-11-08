
import Testing

struct GuessPointTests {
    
    @Test func setType() async throws {
        var gp1 = GuessPoint(row: 1, col: 1)
        let gp2 = GuessPoint(row: 1, col: 1, type: GuessType.dead)
        gp1.type = GuessType.dead
        #expect(gp1 == gp2)
    }
    
    @Test func clone() async throws {
        let gp1 = GuessPoint(row: 1, col: 1, type: GuessType.dead)
        let gp2 = gp1
        #expect(gp1 == gp2)
    }
    
    
}
