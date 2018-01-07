#ifndef PLANEGRIDQML_H
#define PLANEGRIDQML_H

/*****
 * Class extends the PlaneGrid class from common such that it can be used from within QML
 * ***/
#include <QObject>
#include <QColor>
#include "planegrid.h"

class PlaneGridQML : public QObject
{
    Q_OBJECT

public:
    PlaneGridQML(int rows, int cols, int planesNo, bool isComputer): m_PlaneGrid(new PlaneGrid(rows, cols, planesNo, isComputer)) {
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
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

    inline void initGrid() {
        m_PlaneGrid->initGrid();
    }

signals:
    void planesPointsChanged();

private:
    PlaneGrid* m_PlaneGrid;

    ///grey colors to draw the planes
    int m_MinPlaneBodyColor = 0;
    int m_MaxPlaneBodyColor = 200;

    int m_SelectedPlane = 0;
};

#endif // PLANEGRIDQML_H
