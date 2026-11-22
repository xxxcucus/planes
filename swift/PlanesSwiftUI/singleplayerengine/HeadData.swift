
class HeadData {
    // position of the head
    var headRow: Int
    var headCol: Int

    // the correct plane orientation if decided
    var correctOrient: Int

    // statistics about the 4 positions with this head
    var options: [PlaneOrientationData]

    init(row: Int, col: Int, headRow: Int, headCol: Int) {
        self.headRow = headRow
        self.headCol = headCol
        self.correctOrient = -1
        self.options = []

        for (_, orientation) in Orientation.allCases.enumerated() {
            let pl = Plane(row: headRow, col: headCol, orient: orientation)
            // create the four planes for each head position
            let option = PlaneOrientationData(pl: pl, isDiscarded: false)
            if !pl.isPositionValid(row: row, col: col) {
                option.discarded = true
            }
            self.options.append(option)
        }
    }

    // update the current data with a guess
    // return true if a plane is confirmed
    func update(gp: GuessPoint) -> Bool {
        // if the head data is already conclusive ignore
        if correctOrient != -1 {
            return true
        }

        // update the four plane positions with this new data
        for i in 0...3 {
            options[i].update(gp: gp)
        }

        // verify if we checked all points of a plane
        for i in 0...3 {
            if !options[i].discarded && options[i].areAllPointsChecked() {
                correctOrient = i
                return true
            }
        }

        // verify if 3 of the 4 possible orientations are discarded
        var count = 0
        var good_orientation = -1
        for i in 0...3 {
            if options[i].discarded {
                count += 1
            } else {
                good_orientation = i
            }
        }

        // if there are exactly 3 wrong orientations
        if count == 3 {
            correctOrient = good_orientation
            return true
        }

        return false
    }
}
