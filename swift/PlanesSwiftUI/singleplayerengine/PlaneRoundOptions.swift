
struct PlaneRoundOptions {
    
    var computerSkillLevel: SkillLevel = .advanced
    var showPlaneAfterKill: Bool = false
    
    mutating func reset() {
        computerSkillLevel = .advanced
        showPlaneAfterKill = false
    }
}
