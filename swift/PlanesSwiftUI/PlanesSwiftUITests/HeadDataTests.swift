import Testing

@Test func init1() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    _ = hd.update(gp: GuessPoint(row: 5, col: 5, type: GuessType.dead))
}

@Test func updateCorrectOrientSet() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    hd.correctOrient = 0
    #expect(hd.update(gp: GuessPoint(row: 0, col: 0)))
}

@Test func updateOneOrientationCompletAndNotDiscarded() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    let pod = PlaneOrientationData()
    //already correct orientation found
    pod.discarded = false
    hd.options[0] = pod
    #expect(hd.update(gp: GuessPoint(row: 0, col: 0)))
    #expect(0 == hd.correctOrient)
}

@Test func updateThreeOrientationsDiscardedAndOneNotDiscarded() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    hd.options[0].discarded = true
    hd.options[1].discarded = true
    hd.options[2].discarded = true
    hd.options[3].discarded = false
    #expect(hd.update(gp: GuessPoint(row: 0, col: 0)))
    #expect(3 == hd.correctOrient)
}

@Test func updateFourOrientationsDiscarded() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    hd.options[0].discarded = true
    hd.options[1].discarded = true
    hd.options[2].discarded = true
    hd.options[3].discarded = true
    #expect(!hd.update(gp: GuessPoint(row: 0, col: 0)))
}


@Test func updateTwoOrientationsDiscardedAndTwoNotDiscarded() async throws {
    let hd = HeadData(row: 10, col: 10, headRow: 5, headCol: 5)
    hd.options[0].discarded = true
    hd.options[1].discarded = true
    hd.options[2].discarded = false
    hd.options[3].discarded = false
    #expect(!hd.update(gp: GuessPoint(row: 0, col: 0)))
}
