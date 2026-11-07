//
//  PlaneRoundOptions.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 07.11.26.
//

struct PlaneRoundOptions {
    
    var computerSkillLevel: SkillLevel = .advanced
    var showPlaneAfterKill: Bool = false
    
    mutating func reset() {
        computerSkillLevel = .advanced
        showPlaneAfterKill = false
    }
}
