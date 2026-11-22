

//The computer is trying to guess where the player's planes are
//For every position on the table and each of the 4 plane orientations
//it keeps a PlaneOrientationData structure that
//contains all the knowledge that the computer has
//about the corresponding plane
//For each computer guess there can be 3 results
//Miss - the guess is not on a plane
//Hit - the guess is on a plane but it is not on the plane's head
//Dead - the guess is the plane's head
//describes the data that is available about a given plane position
//This structure keeps the information about the position of the head of the planes

class ComputerLogic {
    // gets the number of rows / defines the grid size
    private var rowNo: Int
    // get the number of cols
    private var colNo: Int

    // maximum number of choices
    private var m_maxChoiceNo: Int

    // gets the number of planes / number of planes that need to be guessed
    private var m_planeNo: Int

    // list of already guessed planes
    private var m_guessedPlaneList: [Plane]

    // list of available data for each head in m_guessHeadList
    var m_headDataList: [HeadData]

    // gets the list of guesses / list of guesses made until this moment
    private var m_listGuesses: [GuessPoint]

    // list of extended guesses; when the position of a plane is decided
    // all the points on this plane are considered as misses
    var m_extendedListGuesses: [GuessPoint]

    // gets the choices / the list of choices
    // choice -2 means that a guess has already been made
    // choice is -1 means that plane position is there impossible
    // choice 0 means no data about the choice is available
    // choice = k means that k data exist that support this choice
    var choicesArray: [Int]

    // array keeping the number of points with positive m_choice influenced by a given point
    // contains:
    // -1 when a guess has been made at this point, the position is impossible, or there is already data about this point
    // a positive number showing how many points are influenced by this point
    private var m_zero_choices: [Int]

    // put here so that we calculate the list of indices only once
    // this iterator gives all the planes that intersect the
    // point (0,0) on the grid
    private var m_pipi: PlaneIntersectingPointIterator

    init(rowNo: Int, colNo: Int, planeno: Int) {
        self.rowNo = rowNo
        self.colNo = colNo
        self.m_maxChoiceNo = rowNo * colNo * 4
        self.m_planeNo = planeno

        self.choicesArray = Array(repeating: -1, count: m_maxChoiceNo)
        self.m_zero_choices = Array(repeating: -1, count: m_maxChoiceNo / 4)

        self.m_guessedPlaneList = []
        self.m_headDataList = []
        self.m_listGuesses = []
        self.m_extendedListGuesses = []
        self.m_pipi = PlaneIntersectingPointIterator(point: Coordinate2D(x: 0, y: 0))

        // initializes the table of choices and the head data
        reset()
        // initializes the iterator that generates all the planes that pass through (0,0)
        m_pipi.reset()
    }
    
    func reset() {
        // initializes -1 for impossible choice (invalid plane position)
        // with 0 for possible choice
        for i in 0..<m_maxChoiceNo {
            let pl = mapIndexToPlane(idx: i)
            choicesArray[i] = pl.isPositionValid(row: rowNo, col: colNo) ? 0 : -1
        }

        // clears various lists in the ComputerLogic object
        m_guessedPlaneList.removeAll()
        // m_guessedHeadList.removeAll()
        m_listGuesses.removeAll()
        m_extendedListGuesses.removeAll()
        m_headDataList.removeAll()
    }

    //computes the position in the m_choices array of a given plane
    func mapPlaneToIndex(pl: Plane) -> Int {
        return (pl.col * rowNo + pl.row) * 4 + pl.orient.rawValue
    }

    //computes the plane corresponding to a given position in the choices array
    func mapIndexToPlane(idx: Int) -> Plane {
        let o = Orientation.allCases[idx % 4]
        let temp = idx / 4
        let row = temp % rowNo
        let col = temp / rowNo
        return Plane(row: row, col: col, orient: o)
    }

    //computes the Coordinate2D corresponding to the head of the plane corresponding to the idx
    private func mapIndexToQPoint(idx: Int) -> Coordinate2D {
        let temp = idx / 4
        let row = temp % rowNo
        let col = temp / rowNo
        return Coordinate2D(x: row, y: col)
    }
}
