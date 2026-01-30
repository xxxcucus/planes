#include "genericboard.h"
#include "playareagridsquare.h"
#include "planeiterators.h"
#include "planepointiterator.h"
#include "coordinate2d.h"

#include <QPropertyAnimation>
#include <QDebug>


GenericBoard::~GenericBoard()
{
    if (m_PropertyAnimation != nullptr)
        delete m_PropertyAnimation;
    if (m_RoundEndsAnimatedText != nullptr)
        delete m_RoundEndsAnimatedText;
    delete m_RoundEndsAnimatedText;
    delete m_View;
    delete m_Scene;
}


GenericBoard::GenericBoard(PlaneGrid& grid, int squareWidth) : m_Grid(grid), m_SquareWidth(squareWidth)
{
    m_Scene = new CustomGraphicsScene();
    m_View = new CustomGraphicsView(m_Scene);
    m_View->setSceneSize((m_Grid.getColNo() + 2 * m_PaddingEditingBoard) * m_SquareWidth,
                         (m_Grid.getRowNo() + 2 * m_PaddingEditingBoard) * m_SquareWidth);

}

void GenericBoard::reset()
{
    clearBoard();
    m_CurStage = GameStages::BoardEditing;
    generateBoardItems();
    displayPlanes();
    m_RoundEndsAnimatedText = nullptr;
}

///@todo: deleted the items from m_SceneItems
void GenericBoard::clearBoard()
{
    m_SceneItems.clear();
    m_Scene->clear();
    m_GuessList.clear();
}

void GenericBoard::generateBoardItems()
{
    ///generate player scene items
    int rows = m_Grid.getRowNo() + 2 * m_PaddingEditingBoard;
    int cols = m_Grid.getColNo() + 2 * m_PaddingEditingBoard;

    for (int i = 0; i < rows; i++)
        for (int j = 0; j < cols; j++) {
            if (i < m_PaddingEditingBoard || abs(i - rows) <= m_PaddingEditingBoard
                    || j < m_PaddingEditingBoard || abs (j - cols) <= m_PaddingEditingBoard) {
                GridSquare* br = new GridSquare(i, j, m_SquareWidth);
                m_Scene->addItem(br);
                br->setPos(j * m_SquareWidth, i * m_SquareWidth);
                m_SceneItems[std::make_pair(i, j)] = br;
            } else {
                PlayAreaGridSquare* pabr = new PlayAreaGridSquare(i, j, m_SquareWidth);
                m_Scene->addItem(pabr);
                pabr->setPos(j * m_SquareWidth, i * m_SquareWidth);
                m_SceneItems[std::make_pair(i,j)] = pabr;
            }
        }
}

///@todo: deal with overlapping planes
void GenericBoard::displayPlanes() {
    int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_Grid.getPlaneNo();
    for (int i = 0; i < m_Grid.getPlaneNo(); i++) {
        Plane pl;
        int greyLevel = m_MinPlaneBodyColor + i * colorStep;
        if (!m_Grid.getPlane(i, pl))
            continue;
        if (i != m_SelectedPlane)
            showPlane(pl, QColor(greyLevel, greyLevel, greyLevel));
        else
            showSelectedPlane(pl);
        //() << "Display plane " << i << " " << pl.row() << " " << pl.col() << " " << pl.orientation();
    }
}

void GenericBoard::hidePlanes()
{
    int rows = m_Grid.getRowNo() + 2 * m_PaddingEditingBoard;
    int cols = m_Grid.getColNo() + 2 * m_PaddingEditingBoard;
    for (int i = 0; i < rows; i++)
        for (int j = 0; j < cols; j++)
            m_SceneItems[std::make_pair(i, j)]->clearPlaneOptions();
}

void GenericBoard::refreshPlanes()
{
    reset();
    hidePlanes();
    displayPlanes();    
}


///shows the guesses on the grid
void GenericBoard::displayGuesses() {
    for (GuessPoint gp : m_Grid.getGuesses()) {
        showGuessPoint(gp);
    }
}

void GenericBoard::showPlane(const Plane &pl, const QColor& color)
{
    PlanesCommonTools::Coordinate2D head = pl.head();
    auto headGridSquareIndex = std::make_pair(head.x() + m_PaddingEditingBoard, head.y() + m_PaddingEditingBoard);
    m_SceneItems[headGridSquareIndex]->setType(GridSquare::Type::PlaneHead);
    PlanePointIterator ppi(pl);
    ///ignore the plane head
    ppi.next();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D pt = ppi.next();
        auto pointGridSquareIndex = std::make_pair(pt.x() + m_PaddingEditingBoard, pt.y() + m_PaddingEditingBoard);
        m_SceneItems[pointGridSquareIndex]->setType(GridSquare::Type::Plane);
        m_SceneItems[pointGridSquareIndex]->setColor(color);
    }
}

void GenericBoard::showSelectedPlane(const Plane &pl)
{
    PlanesCommonTools::Coordinate2D head = pl.head();
    auto headGridSquareIndex = std::make_pair(head.x() + m_PaddingEditingBoard, head.y() + m_PaddingEditingBoard);
    m_SceneItems[headGridSquareIndex]->setSelected(true);
    PlanePointIterator ppi(pl);
    ///ignore the plane head
    ppi.next();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D pt = ppi.next();
        auto pointGridSquareIndex = std::make_pair(pt.x() + m_PaddingEditingBoard, pt.y() + m_PaddingEditingBoard);
        m_SceneItems[pointGridSquareIndex]->setSelected(true);
    }
}

void GenericBoard::updateEditorBoard()
{
    hidePlanes();
    if (m_Grid.doPlanesOverlap() || m_Grid.isPlaneOutsideGrid())
        emit planePositionNotValid(true);
    else
        emit planePositionNotValid(false);
    displayPlanes();
}

void GenericBoard::showMove(const GuessPoint& gp)
{
    m_GuessList.push_back(gp);
    hidePlanes();
    displayPlanes();
    displayGuesses();
}

void GenericBoard::showGuessPoint(const GuessPoint &gp)
{
    //qDebug() << "show guess point " << gp.m_row << " " << gp.m_col;
    GridSquare::GameStatus st = GridSquare::GameStatus::TestedNotPlane;
    if (gp.isHit())
        st = GridSquare::GameStatus::PlaneGuessed;
    if (gp.isDead())
        st = GridSquare::GameStatus::PlaneHeadGuessed;
    ///not sure about -1 here
    PlayAreaGridSquare* pags = dynamic_cast<PlayAreaGridSquare*>(m_SceneItems[std::make_pair(gp.m_row + m_PaddingEditingBoard, gp.m_col + m_PaddingEditingBoard)]);
    if (!pags) {
        //qDebug() << "Dynamic cast did not succeed !";
        return;
    }
    pags->showGuesses(true);
    pags->setGameStatus(st);
}

void GenericBoard::endRound(bool isPlayerWinner, bool isDraw, bool isSinglePlayer) {
    QString winnerText = isPlayerWinner ? "Player wins!" : isSinglePlayer ? "Computer wins!" : "Opponent wins!";
	if (isDraw)
		winnerText = "Draw!";

    m_RoundEndsAnimatedText = new AnimatedTextItem(winnerText);
    m_RoundEndsAnimatedText->setFont(QFont("Timer", 20, QFont::Bold));
    m_RoundEndsAnimatedText->setDefaultTextColor(Qt::red);
    m_CurStage = GameStages::GameNotStarted;
    m_Scene->addItem(m_RoundEndsAnimatedText);
    //qDebug() << "end round";
    if (m_PropertyAnimation != nullptr)
        delete m_PropertyAnimation;
    m_PropertyAnimation = new QPropertyAnimation(m_RoundEndsAnimatedText, "m_ScenePosition");
    QFont f = m_RoundEndsAnimatedText->font();
    QFontMetrics fm(f);
    m_PropertyAnimation->setDuration(1000);
    m_PropertyAnimation->setStartValue(QPoint((m_Grid.getColNo() + m_PaddingEditingBoard * 2)  * m_SquareWidth / 2 - fm.boundingRect(winnerText).width() / 2, (m_Grid.getRowNo() +  m_PaddingEditingBoard) * m_SquareWidth));
    m_PropertyAnimation->setEndValue(QPoint((m_Grid.getColNo() + m_PaddingEditingBoard * 2) * m_SquareWidth / 2 - fm.boundingRect(winnerText).width() / 2,  m_SquareWidth / 2));
    m_PropertyAnimation->start();
}

void GenericBoard::setSelectedPlaneIndex(int idx) {
    m_SelectedPlane = idx;
    hidePlanes();
    displayPlanes();
    displayGuesses();
}
