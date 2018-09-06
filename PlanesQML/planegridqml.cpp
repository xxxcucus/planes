#include "planegridqml.h"
#include "coordinate2d.h"
#include <QDebug>
#include <QColor>

PlaneGridQML::PlaneGridQML(PlaneGameQML* planeGame, PlaneGrid* planeGrid): m_PlaneGrid(planeGrid), m_PlaneGame(planeGame) {
    //connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
    connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SLOT(verifyPlanePositionValid()));
    if (m_PlaneGrid->isComputer()) {
        connect(this, SIGNAL(guessMade(const GuessPoint&)), m_PlaneGame, SIGNAL(guessMade(const GuessPoint&)));
        m_SelectedPlane = -1;
    }
    if (!m_PlaneGrid->isComputer()) {
        connect(m_PlaneGame, SIGNAL(computerMoveGenerated(const GuessPoint&)), this, SLOT(showComputerMove(const GuessPoint&)));
    }
    m_LineSize = m_PlaneGrid->getColNo() + 2 * m_Padding;
    m_NoLines = m_PlaneGrid->getRowNo() + 2 * m_Padding;
}

QColor PlaneGridQML::getPlanePointColor(int idx, bool& isPlaneHead) const
{
   int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneGrid->getPlaneNo();
   int annotation = m_PlaneGrid->getPlanePointAnnotation(idx);
   isPlaneHead = false;
   std::vector<int> planesIdx = m_PlaneGrid->decodeAnnotation(annotation);
   if (planesIdx.size() > 1)    //point belongs to more planes mark it with red
       return m_PlaneIntersectionColor;
   if (planesIdx.size() == 1) {
       if ((planesIdx[0] >= 0 && m_SelectedPlane != planesIdx[0]) ||
               (planesIdx[0] < 0 && m_SelectedPlane != -planesIdx[0] - 1)) {
           if (planesIdx[0] < 0) {
               isPlaneHead = true;
               return m_PlaneHeadColor;
           } else {
               int grayCol = m_MinPlaneBodyColor + planesIdx[0] * colorStep;
               return QColor(grayCol, grayCol, grayCol);
           }
       } else {
           if (planesIdx[0] < 0)
               isPlaneHead = true;
           return m_SelectedPlaneColor;
       }
   }
   qDebug() << "Error: point belongs to no plane " << annotation;
   return m_InvalidPointColor;
}

QHash<int, QByteArray> PlaneGridQML::roleNames() const {
    QHash<int, QByteArray> roles;
    roles[PlaneRole] = "planeData";
    roles[PlaneColorRole] = "planeColorData";
    roles[GuessRole] = "guessData";
    roles[BoardRole] = "boardData";
    return roles;
}

int PlaneGridQML::rowCount(const QModelIndex &parent) const {
    Q_UNUSED(parent)
    return m_LineSize * m_NoLines;
}

QVariant PlaneGridQML::data(const QModelIndex &index, int role) const {
    //qDebug() << index.row() << " " << index.column();

    if (role != PlaneRole && role != GuessRole && role != BoardRole && role != PlaneColorRole)
        return QVariant();

    // Check boundaries
    if(index.row() < 0 || rowCount() <= index.row()) {
        qDebug() << "Warning: " << index.row() << ", " << index.column();
        return QVariant();
    }

    int row = index.row() / m_LineSize;
    int col = index.row() % m_LineSize;
    //qDebug() << "row " << row << " col " << col;
    int idxInPlanePointList = -1;
    int idxR = row - m_Padding;
    int idxC = col - m_Padding;


    bool isOnPlane = m_PlaneGrid->isPointOnPlane(idxR, idxC, idxInPlanePointList);
    bool isPlaneHead = false;

    if (role == PlaneColorRole) {
        QColor retCol(0, 0, 0);
        if (!isOnPlane) {
            retCol = m_BoardColor;
            if (row < m_Padding || row >= m_PlaneGrid->getRowNo() + m_Padding)
                retCol = m_PaddingColor;
            if (col < m_Padding || col >= m_PlaneGrid->getColNo() + m_Padding)
                retCol = m_PaddingColor;
        } else {
            retCol = getPlanePointColor(idxInPlanePointList, isPlaneHead);
        }
        //qDebug() << "BlaBla " << idxR << " " << idxC << " " << retCol.red() << " " << retCol.green() << " " << retCol.blue();
        return retCol;
    }

    if (role == BoardRole) {
        int retVal = BoardDescription::Board;
        if (row < m_Padding || row >= m_PlaneGrid->getRowNo() + m_Padding)
            retVal = BoardDescription::Padding;
        if (col < m_Padding || col >= m_PlaneGrid->getColNo() + m_Padding)
            retVal = BoardDescription::Padding;
        return retVal;
    }

    if (role == PlaneRole) {
        int retVal = PlaneDescription::NoPlane;
        if (isOnPlane && !isPlaneHead)
            retVal = PlaneDescription::Plane;
        if (isPlaneHead)
            retVal = PlaneDescription::PlaneHead;
        return retVal;
    }

    if (role == GuessRole) {
        GuessPoint::Type retVal;
        if (wasGuessMade(idxR, idxC, retVal))
            return int(retVal);
        else
            return QVariant();
    }

    return QVariant();
}

void PlaneGridQML::verifyPlanePositionValid() {
    if (m_PlaneGrid->doPlanesOverlap() || m_PlaneGrid->isPlaneOutsideGrid())
        emit planePositionNotValid(true);
    else
        emit planePositionNotValid(false);
}

void PlaneGridQML::computerBoardClick(int index) {
    //qDebug() << index;

    ///@todo:
    /// calculate row and col
    /// add m_GuessList as member variable to planegridqml

    if (m_CurStage != GameStages::Game)
        return;

    beginResetModel();
    ///@todo: see method data() above
    int row = index / m_LineSize;
    int col = index % m_LineSize;

    if (row < m_Padding || col < m_Padding)
        return;
    if (row >= m_PlaneGrid->getRowNo() + m_Padding)
        return;
    if (col >= m_PlaneGrid->getColNo() + m_Padding)
        return;

    PlanesCommonTools::Coordinate2D qp(row - m_Padding, col - m_Padding);
    GuessPoint::Type tp = m_PlaneGrid->getGuessResult(qp);

    qDebug() << "Guess " << tp;

    //the m_grid object returns whether is a miss, hit or dead
    //with this data builda guess point object
    GuessPoint gp(qp.x(), qp.y(), tp);

    //verify if the guess point is not already in the list
    //emit a signal that the guess has been made
    if (m_GuessList.indexOf(gp) == -1)
    {
        m_GuessList.append(gp);
        m_GuessMap[std::make_pair(qp.x(), qp.y())] = tp;
        //to not let the user draw while the computer is thinking
//        m_currentMode = Editor;
        emit guessMade(gp);
    }
    endResetModel();
}

void PlaneGridQML::showComputerMove(const GuessPoint& gp) {
    beginResetModel();
    if (m_GuessList.indexOf(gp) == -1)
    {
        m_GuessList.append(gp);
        m_GuessMap[std::make_pair(gp.m_row, gp.m_col)] = gp.m_type;
    }
    endResetModel();
}

void PlaneGridQML::doneEditing() {
    if (m_CurStage == GameStages::BoardEditing)
        m_CurStage = GameStages::Game;
    else
        qDebug() << "Board editing done received, but not in the right state";
    beginResetModel();
    m_SelectedPlane = -1;
    endResetModel();
    //emit planesPointsChanged();
}

bool PlaneGridQML::wasGuessMade(int row, int col, GuessPoint::Type &guessRes) const {
    auto p = std::make_pair(row, col);
    auto it = m_GuessMap.find(p);
    if (it != m_GuessMap.end()) {
        guessRes = it->second;
        return true;
    }
    return false;
}
