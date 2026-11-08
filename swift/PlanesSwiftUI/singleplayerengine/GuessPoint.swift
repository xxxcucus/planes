
struct GuessPoint : Equatable {
    var row: Int
    var col: Int
    var type: GuessType

    init(row: Int, col: Int, type: GuessType = .miss) {
        self.row = row
        self.col = col
        self.type = type
    }
    
    // Proprietăți calculate (Computed Properties)
    var isDead: Bool { type == .dead }
    var isHit: Bool { type == .hit }
    var isMiss: Bool { type == .miss }
}
