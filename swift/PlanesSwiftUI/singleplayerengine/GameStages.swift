//
//  GameStages.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 02.11.26.
//

enum GameStages: Int {
    case gameNotStarted = 0
    case boardEditing = 1
    case game = 2
    case waitForOpponentPlanePositions = 3
    case waitForOpponentMoves = 4
    case sendRemainingMoves = 5
}
