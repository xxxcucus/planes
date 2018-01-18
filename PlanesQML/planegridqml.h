#ifndef PLANEGRIDQML_H
#define PLANEGRIDQML_H

/*****
 * Class extends the PlaneGrid class from common such that it can be used from within QML
 * ***/
#include <QObject>
#include <QColor>
#include <QAbstractListModel>
#include "planegrid.h"

class PlaneGridQML : public QAbstractListModel
{
    Q_OBJECT

public:
    PlaneGridQML(int rows, int cols, int planesNo, bool isComputer): m_PlaneGrid(new PlaneGrid(rows, cols, planesNo, isComputer)) {
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
        m_LineSize = m_PlaneGrid->getColNo() + 2 * m_Padding;
        m_NoLines = m_PlaneGrid->getRowNo() + 2 * m_Padding;
    }

    PlaneGridQML(PlaneGrid* planeGrid): m_PlaneGrid(planeGrid) {
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
        m_LineSize = m_PlaneGrid->getColNo() + 2 * m_Padding;
        m_NoLines = m_PlaneGrid->getRowNo() + 2 * m_Padding;
    }

    Q_INVOKABLE int getPlanesPointsCount() const {
        return m_PlaneGrid->getPlanesPointsCount();
    }

    Q_INVOKABLE QPoint getPlanePoint(int idx) const {
        return m_PlaneGrid->getPlanePoint(idx);
    }

    Q_INVOKABLE QColor getPlanePointColor(int idx) const;

    Q_INVOKABLE int getRows() const {
        return m_PlaneGrid->getRowNo();
    }

    Q_INVOKABLE int getCols() const {
        return m_PlaneGrid->getColNo();
    }

    Q_INVOKABLE int getPlanesNo() const {
        return m_PlaneGrid->getPlaneNo();
    }

    Q_INVOKABLE bool getIsComputer() const {
        return m_PlaneGrid->isComputer();
    }

    Q_INVOKABLE void toggleSelectedPlane() {
        m_SelectedPlane = (m_SelectedPlane + 1) % m_PlaneGrid->getPlaneNo();
        emit planesPointsChanged();
    }

    Q_INVOKABLE void rotateSelectedPlane() {
        m_PlaneGrid->rotatePlane(m_SelectedPlane);
    }

    Q_INVOKABLE void moveUpSelectedPlane() {
        m_PlaneGrid->movePlaneUpwards(m_SelectedPlane);
    }

    Q_INVOKABLE void moveDownSelectedPlane() {
        m_PlaneGrid->movePlaneDownwards(m_SelectedPlane);
    }

    Q_INVOKABLE void moveLeftSelectedPlane() {
        m_PlaneGrid->movePlaneLeft(m_SelectedPlane);
    }

    Q_INVOKABLE void moveRightSelectedPlane() {
        m_PlaneGrid->movePlaneRight(m_SelectedPlane);
    }

    Q_INVOKABLE bool isComputer() {
        return m_PlaneGrid->isComputer();
    }

    inline void initGrid() {
        m_PlaneGrid->initGrid();
    }

    inline void initGrid1() {
        emit planesPointsChanged();
    }

    ///for QAbstractTableModel
    enum QMLGridRoles {
        ColorRole = Qt::UserRole + 1
    };

    /*
    int rowCount(const QModelIndex & parent = QModelIndex()) const override;
    int columnCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;
    QModelIndex parent(const QModelIndex &index) const override;
    QModelIndex	index(int row, int column, const QModelIndex &parent = QModelIndex()) const override;*/
    QHash<int, QByteArray> roleNames() const override;

    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;
signals:
    void planesPointsChanged();

private:
    PlaneGrid* m_PlaneGrid;

    ///grey colors to draw the planes
    int m_MinPlaneBodyColor = 0;
    int m_MaxPlaneBodyColor = 200;

    int m_Padding = 3;
    int m_SelectedPlane = 0;
    int m_LineSize = 0;
    int m_NoLines = 0;
};

#endif // PLANEGRIDQML_H
