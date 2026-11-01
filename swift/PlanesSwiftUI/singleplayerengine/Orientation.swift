//
//  Orientation.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 30.10.26.
//

enum Orientation: Int {
    case NorthSouth = 0
    case SouthNorth = 1
    case WestEast = 2
    case EastWest = 3
    
    static func fromInt(_ value: Int) -> Orientation {
        return Orientation(rawValue: value) ?? .NorthSouth
    }
}

