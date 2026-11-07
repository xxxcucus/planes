//
//  GuessTypeTests.swift
//  PlanesSwiftUI
//
//  Created by Cristian Cucu on 01.11.26.
//

import Testing

struct GuessTypeTests {

    @Test func typeValues() async throws {
        #expect(GuessType.allCases.first == .miss)
        #expect(GuessType.allCases[1] == .hit)
        #expect(GuessType.allCases[2] == .dead)
    }
}
