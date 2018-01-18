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

/*int PlaneGridQML::rowCount(const QModelIndex & parent) const {
    Q_UNUSED(parent)
    qDebug() << "row count " << m_PlaneGrid->getRowNo() + 2 * m_Padding;
    return m_PlaneGrid->getRowNo() + 2 * m_Padding;

}

int PlaneGridQML::columnCount(const QModelIndex &parent) const {
    Q_UNUSED(parent)
    qDebug() << " col count " << m_PlaneGrid->getColNo() + 2 * m_Padding;
    return m_PlaneGrid->getColNo() + 2 * m_Padding;
}

QVariant PlaneGridQML::data(const QModelIndex &index, int role) const {
    qDebug() << index.row() << " " << index.column();

    if (role != ColorRole)
        return QVariant();

    // Check boudaries
    if(index.column() < 0 || columnCount() <= index.column() ||
            index.row() < 0 || rowCount() <= index.row()) {
        qDebug() << "Warning: " << index.row() << ", " << index.column();
        return QVariant();
    }

    if (index.row() < m_Padding || index.row() >= m_PlaneGrid->getRowNo() + m_Padding)
        return QColor("aqua");
    if (index.column() < m_Padding || index.column() >= m_PlaneGrid->getColNo() + m_Padding)
        return QColor("aqua");

    int idxInPlanePointList = -1;
    int idxR = index.row() - m_Padding;
    int idxC = index.column() - m_Padding;
    bool isOnPlane = m_PlaneGrid->isPointOnPlane(idxR, idxC, idxInPlanePointList);

    QColor retCol(0, 0, 0);
    if (!isOnPlane)
        retCol = QColor(255, 255, 255);
    else
        retCol = getPlanePointColor(idxInPlanePointList);
    qDebug() << index.row() << " " << index.column() << " " << retCol.red() << " , " << retCol.green() << " , " << retCol.blue();
    return retCol;
}

QModelIndex PlaneGridQML::parent(const QModelIndex &index) const {
    Q_UNUSED(index)
    return QModelIndex();
}

QModelIndex	PlaneGridQML::index(int row, int column, const QModelIndex& parent) const {
    Q_UNUSED(parent)
    if (row < 0 || row >= rowCount())
        return QModelIndex();
    if (column < 0 || column >= columnCount())
        return QModelIndex();
    return createIndex(row, column);
}*/

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
    qDebug() << index.row() << " " << index.column();

    if (role != ColorRole)
        return QVariant();

    // Check boudaries
    if(index.row() < 0 || rowCount() <= index.row()) {
        qDebug() << "Warning: " << index.row() << ", " << index.column();
        return QVariant();
    }

    int row = index.row() / m_LineSize;
    int col = index.row() % m_LineSize;

    if (row < m_Padding || row >= m_PlaneGrid->getRowNo() + m_Padding)
        return QColor("aqua");
    if (col < m_Padding || col >= m_PlaneGrid->getColNo() + m_Padding)
        return QColor("aqua");

    int idxInPlanePointList = -1;
    int idxR = row - m_Padding;
    int idxC = col - m_Padding;
    bool isOnPlane = m_PlaneGrid->isPointOnPlane(idxR, idxC, idxInPlanePointList);

    QColor retCol(0, 0, 0);
    if (!isOnPlane)
        retCol = QColor("#ea7025");
    else
        retCol = getPlanePointColor(idxInPlanePointList);
    return retCol;
}
