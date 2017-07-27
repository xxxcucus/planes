#ifndef PLANEGRIDQML_H
#define PLANEGRIDQML_H

/*****
 * Class extends the PlaneGrid class from common such that it can be used from within QML
 * ***/
#include <QObject>
#include "planegrid.h"

class PlaneGridQML : public QObject
{
    Q_OBJECT

public:
    PlaneGridQML(int rows, int cols, int planesNo, bool isComputer): m_PlaneGrid(rows, cols, planesNo, isComputer) {
        connect(&m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SLOT(planesPointsChanged()));
    }

    Q_INVOKABLE QList<QPoint> getPlanesPoints() {
        return m_PlaneGrid.getPlanesPoints();
    }
    Q_INVOKABLE int getRows() {
        return m_PlaneGrid.getRowNo();
    }
    Q_INVOKABLE int getCols() {
        return m_PlaneGrid.getColNo();
    }
    Q_INVOKABLE int getPlanesNo() {
        return m_PlaneGrid.getPlaneNo();
    }
    Q_INVOKABLE bool getIsComputer() {
        return m_PlaneGrid.isComputer();
    }

signals:
    void planesPointsChanged();


private:
    PlaneGrid m_PlaneGrid;
};

#endif // PLANEGRIDQML_H
