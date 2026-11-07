//
//  Orientation.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 30.10.26.
//

enum Orientation: Int, CaseIterable {
    case northSouth = 0
    case southNorth = 1
    case westEast = 2
    case eastWest = 3
    
    static func fromInt(_ value: Int) -> Orientation {
        return Orientation(rawValue: value) ?? .northSouth
    }
}

