#include "planegridqml.h"
#include <QDebug>
#include <QColor>

QColor PlaneGridQML::getPlanePointColor(int idx) const
{
   ///@todo: mark the head of the planes with green
   int colorStep = (m_MaxPlaneBodyColor - m_MinPlaneBodyColor) / m_PlaneGrid->getPlaneNo();
   int annotation = m_PlaneGrid->getPlanePointAnnotation(idx);
   std::vector<int> planesIdx = m_PlaneGrid->decodeAnnotation(annotation);
   if (planesIdx.size() > 1)    //point belongs to more planes mark it with red
       return QColor(255, 0, 0);
   if (planesIdx.size() == 1) {
       if (m_SelectedPlane != planesIdx[0]) {
           int grayCol = m_MinPlaneBodyColor + planesIdx[0] * colorStep;
           return QColor(grayCol, grayCol, grayCol);
       } else {
           return QColor(0, 0, 255);
       }
   }
   qDebug() << "Error: point belongs to no plane";
   return QColor(255, 255, 255);
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

    int idxInPlanePointList = -1;
    int idxR = row - m_Padding;
    int idxC = col - m_Padding;
    bool isOnPlane = m_PlaneGrid->isPointOnPlane(idxR, idxC, idxInPlanePointList);

    QColor retCol(0, 0, 0);
    if (!isOnPlane) {
        retCol = QColor("#ea7025");
        if (row < m_Padding || row >= m_PlaneGrid->getRowNo() + m_Padding)
            retCol = QColor("aqua");
        if (col < m_Padding || col >= m_PlaneGrid->getColNo() + m_Padding)
            retCol = QColor("aqua");
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
