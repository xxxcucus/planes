//
//  GuessTypeTests.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 01.11.26.
//

import Testing

struct GuessTypeTests {

    @Test func typeValues() async throws {
        #expect(GuessType.allCases.first == .Miss)
        #expect(GuessType.allCases[1] == .Hit)
        #expect(GuessType.allCases[2] == .Dead)
    }
}
