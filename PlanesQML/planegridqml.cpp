#include "planegridqml.h"
#include <QDebug>
#include <QColor>

PlaneGridQML::PlaneGridQML(PlaneGameQML* planeGame, PlaneGrid* planeGrid): m_PlaneGame(planeGame), m_PlaneGrid(planeGrid) {
    //connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
    connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SLOT(verifyPlanePositionValid()));
    if (m_PlaneGrid->isComputer()) {
        connect(this, SIGNAL(guessMade(const GuessPoint&)), m_PlaneGame, SIGNAL(guessMade(const GuessPoint&)));
        m_SelectedPlane = -1;
    }
    m_LineSize = m_PlaneGrid->getColNo() + 2 * m_Padding;
    m_NoLines = m_PlaneGrid->getRowNo() + 2 * m_Padding;
}

QColor PlaneGridQML::getPlanePointColor(int idx) const
{
   ///@todo: mark the head of the planes with green
   int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneGrid->getPlaneNo();
   int annotation = m_PlaneGrid->getPlanePointAnnotation(idx);
   std::vector<int> planesIdx = m_PlaneGrid->decodeAnnotation(annotation);
   if (planesIdx.size() > 1)    //point belongs to more planes mark it with red
       return m_PlaneIntersectionColor;
   if (planesIdx.size() == 1) {
       if (m_SelectedPlane != planesIdx[0]) {
           int grayCol = m_MinPlaneBodyColor + planesIdx[0] * colorStep;
           return QColor(grayCol, grayCol, grayCol);
       } else {
           return m_SelectedPlaneColor;
       }
   }
   qDebug() << "Error: point belongs to no plane";
   return m_InvalidPointColor;
}

QHash<int, QByteArray> PlaneGridQML::roleNames() const {
    QHash<int, QByteArray> roles;
    roles[ColorRole] = "colorRGB";
    return roles;
}

int PlaneGridQML::rowCount(const QModelIndex &parent) const {
    Q_UNUSED(parent)
    return m_LineSize * m_NoLines;
}

QVariant PlaneGridQML::data(const QModelIndex &index, int role) const {
    //qDebug() << index.row() << " " << index.column();

    if (role != ColorRole)
        return QVariant();

    // Check boudaries
    if(index.row() < 0 || rowCount() <= index.row()) {
        qDebug() << "Warning: " << index.row() << ", " << index.column();
        return QVariant();
    }

    int row = index.row() / m_LineSize;
    int col = index.row() % m_LineSize;

    qDebug() << "row " << row << " col " << col;

    int idxInPlanePointList = -1;
    int idxR = row - m_Padding;
    int idxC = col - m_Padding;
    bool isOnPlane = m_PlaneGrid->isPointOnPlane(idxR, idxC, idxInPlanePointList);

    QColor retCol(0, 0, 0);
    if (!isOnPlane) {
        retCol = m_BoardColor;
        if (row < m_Padding || row >= m_PlaneGrid->getRowNo() + m_Padding)
            retCol = m_PaddingColor;
        if (col < m_Padding || col >= m_PlaneGrid->getColNo() + m_Padding)
            retCol = m_PaddingColor;
    } else {
        retCol = getPlanePointColor(idxInPlanePointList);
    }

    return retCol;
}

void PlaneGridQML::verifyPlanePositionValid() {
    if (m_PlaneGrid->doPlanesOverlap() || m_PlaneGrid->isPlaneOutsideGrid())
        emit planePositionNotValid(true);
    else
        emit planePositionNotValid(false);
}

void PlaneGridQML::computerBoardClick(int index) {
    qDebug() << index;

    ///@todo:
    /// calculate row and col
    /// add m_GuessList as member variable to planegridqml

    if (m_CurStage != GameStages::Game)
        return;

    ///@todo: see method data() above
    int row = index / m_LineSize;
    int col = index % m_LineSize;

    if (row < m_Padding || col < m_Padding)
        return;
    if (row >= m_PlaneGrid->getRowNo() + m_Padding)
        return;
    if (col >= m_PlaneGrid->getColNo() + m_Padding)
        return;

    QPoint qp(row - m_Padding, col - m_Padding);
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
        //to not let the user draw while the computer is thinking
//        m_currentMode = Editor;
        emit guessMade(gp);

        ///@todo: to adapt these
        //hidePlanes();
        //displayPlanes();
        //displayGuesses();
    }
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
