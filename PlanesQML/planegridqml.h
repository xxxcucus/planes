#ifndef PLANEGRIDQML_H
#define PLANEGRIDQML_H

/*****
 * Class extends the PlaneGrid class from common such that it can be used from within QML
 * ***/
#include <QObject>
#include <QDebug>
#include <QColor>
#include <QAbstractListModel>
#include "planegrid.h"
#include "planegameqml.h"

class PlaneGridQML : public QAbstractListModel
{
    Q_OBJECT

public:
    enum class GameStages { GameNotStarted, BoardEditing, Game };

   /* PlaneGridQML(int rows, int cols, int planesNo, bool isComputer): m_PlaneGrid(new PlaneGrid(rows, cols, planesNo, isComputer)) {
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SLOT(verifyPlanePositionValid()));
        m_LineSize = m_PlaneGrid->getColNo() + 2 * m_Padding;
        m_NoLines = m_PlaneGrid->getRowNo() + 2 * m_Padding;
    }*/

    PlaneGridQML(PlaneGameQML* planeGame, PlaneGrid* planeGrid): m_PlaneGame(planeGame), m_PlaneGrid(planeGrid) {
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SIGNAL(planesPointsChanged()));
        connect(m_PlaneGrid, SIGNAL(planesPointsChanged()), this, SLOT(verifyPlanePositionValid()));
        if (m_PlaneGrid->isComputer()) {
            connect(this, SIGNAL(guessMade(const GuessPoint&)), m_PlaneGame, SIGNAL(guessMade(const GuessPoint&)));
        }
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
        beginResetModel();
        emit planesPointsChanged();
        endResetModel();
    }

    Q_INVOKABLE void rotateSelectedPlane() {
        qDebug() << "Rotate selected plane";
        beginResetModel();
        m_PlaneGrid->rotatePlane(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveUpSelectedPlane() {
        qDebug() << "Move up selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneUpwards(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveDownSelectedPlane() {
        qDebug() << "Move down selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneDownwards(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveLeftSelectedPlane() {
        qDebug() << "Move left selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneLeft(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveRightSelectedPlane() {
        qDebug() << "Move right selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneRight(m_SelectedPlane);
        endResetModel();
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

    QHash<int, QByteArray> roleNames() const override;

    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;

    Q_INVOKABLE void computerBoardClick(int index);
    Q_INVOKABLE void doneEditing() {
        if (m_CurStage == GameStages::BoardEditing)
            m_CurStage = GameStages::Game;
        else
            qDebug() << "Board editing done received, but not in the right state";
    }

signals:
    void planesPointsChanged();
    void planePositionNotValid(bool val);
    void guessMade(const GuessPoint& gp);

public slots:
    /*
     * Compute whether done button is active in the board editor
     */
    void verifyPlanePositionValid();

private:
    PlaneGrid* m_PlaneGrid;
    PlaneGameQML* m_PlaneGame;

    ///grey colors to draw the planes
    int m_MinPlaneBodyColor = 0;
    int m_MaxPlaneBodyColor = 200;

    int m_Padding = 3;
    int m_SelectedPlane = 0;
    int m_LineSize = 0;
    int m_NoLines = 0;

    GameStages m_CurStage = GameStages::BoardEditing;

    ///list of guesses made up to now
    QList<GuessPoint> m_GuessList;

    ///@todo: to define
    QColor m_SelectedPlaneColor = QColor(0, 0, 255);
    QColor m_NotSelectedPlaneColor;
    QColor m_PaddingColor = QColor("aqua");
    QColor m_BoardColor = QColor("#ea7025");
};

#endif // PLANEGRIDQML_H
