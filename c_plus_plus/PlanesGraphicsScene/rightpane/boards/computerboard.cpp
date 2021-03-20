#include "computerboard.h"
#include "playareagridsquare.h"

ComputerBoard::ComputerBoard(PlaneGrid& grid, int squareWidth) : GenericBoard(grid, squareWidth) {
    connect(m_Scene, SIGNAL(gridSquareClicked(int, int)), this, SLOT(gridSquareClicked(int, int)));
    m_SelectedPlane = -1;
}

void ComputerBoard::gridSquareClicked(int row, int col)
{
    if (m_CurStage != GameStages::Game)
        return;
    if (row < m_PaddingEditingBoard || col < m_PaddingEditingBoard)
        return;
    if (row >= m_Grid.getRowNo() + m_PaddingEditingBoard)
        return;
    if (col >= m_Grid.getColNo() + m_PaddingEditingBoard)
        return;

    PlanesCommonTools::Coordinate2D qp(col - m_PaddingEditingBoard, row - m_PaddingEditingBoard);
    GuessPoint::Type tp = m_Grid.getGuessResult(qp);

    //qDebug() << "Guess " << tp;

    //the m_grid object returns whether is a miss, hit or dead
    //with this data builda guess point object
    GuessPoint gp(qp.x(), qp.y(), tp);

    //verify if the guess point is not already in the list
    //emit a signal that the guess has been made
    if(m_GuessList.indexOf(gp)==-1)
    {
        m_GuessList.append(gp);
        //to not let the user draw while the computer is thinking
//        m_currentMode = Editor;
        emit guessMade(gp);
        hidePlanes();
        displayPlanes();
        displayGuesses();
    }
}

void ComputerBoard::endRound(bool isPlayerWinner, bool isDraw, bool isSinglePlayer) {
    GenericBoard::endRound(isPlayerWinner, isDraw, isSinglePlayer);
    ///show the computer planes at the end of the round
    for (auto sit : m_SceneItems) {
        PlayAreaGridSquare* pags = dynamic_cast<PlayAreaGridSquare*>(sit.second);
        if (pags)
            pags->showPlane(true);
    }
}

void ComputerBoard::generateBoardItems() {
    GenericBoard::generateBoardItems();
    m_SelectedPlane = -1;

    ///do not display the planes in the computer grid
    for (auto sit : m_SceneItems) {
        PlayAreaGridSquare* pags = dynamic_cast<PlayAreaGridSquare*>(sit.second);
        if (pags)
            pags->showPlane(false);
    }
}
