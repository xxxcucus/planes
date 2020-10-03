#ifndef PLANEGRIDQML_H
#define PLANEGRIDQML_H

/*****
 * Class extends the PlaneGrid class from common such that it can be used from within QML
 * ***/
#include <QObject>
#include <QColor>
#include <QAbstractListModel>
#include "planegrid.h"
#include "planegameqml.h"

class PlaneGridQML : public QAbstractListModel
{
    Q_OBJECT

public:
    enum class GameStages { GameNotStarted, BoardEditing, Game };
    enum PlaneDescription { NoPlane = 0, Plane = 1, PlaneHead = 2 };
    enum BoardDescription { Board = 0, Padding = 1 };

    PlaneGridQML(PlaneGameQML* planeGame, PlaneGrid* planeGrid);

    Q_INVOKABLE int getPlanesPointsCount() const {
        return m_PlaneGrid->getPlanesPointsCount();
    }

    //Q_INVOKABLE QPoint getPlanePoint(int idx) const {
    //    return m_PlaneGrid->getPlanePoint(idx);
    //}

    QColor getPlanePointColor(int idx, bool& isPlaneHead) const;

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
        //qDebug() << "Rotate selected plane";
        beginResetModel();
        m_PlaneGrid->rotatePlane(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveUpSelectedPlane() {
        //qDebug() << "Move up selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneUpwards(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveDownSelectedPlane() {
        //qDebug() << "Move down selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneDownwards(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveLeftSelectedPlane() {
        //qDebug() << "Move left selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneLeft(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE void moveRightSelectedPlane() {
        //qDebug() << "Move right selected plane";
        beginResetModel();
        m_PlaneGrid->movePlaneRight(m_SelectedPlane);
        endResetModel();
    }

    Q_INVOKABLE bool isComputer() {
        return m_PlaneGrid->isComputer();
    }

    Q_INVOKABLE inline void initGrid() {
        beginResetModel();
        m_PlaneGrid->initGrid();
        if (!m_PlaneGrid->isComputer())
            m_SelectedPlane = 0;
        m_GuessList.clear();
        endResetModel();
    }

    Q_INVOKABLE inline void initGrid1() {
        emit planesPointsChanged();
    }

    ///for QAbstractTableModel
    enum QMLGridRoles {
        PlaneRole = Qt::UserRole + 1,
        PlaneColorRole = Qt::UserRole + 2,
        GuessRole = Qt::UserRole + 3,
        BoardRole = Qt::UserRole + 4
    };

    QHash<int, QByteArray> roleNames() const override;

    int rowCount(const QModelIndex &parent = QModelIndex()) const override;
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override;

    Q_INVOKABLE void computerBoardClick(int index);
    Q_INVOKABLE void doneEditing();

    Q_INVOKABLE inline void resetModel() {
        beginResetModel();
        endResetModel();
    }

private:
    bool wasGuessMade(int row, int col, GuessPoint::Type& guessRes) const;

signals:
    void planesPointsChanged();
    void planePositionNotValid(bool val);
    void guessMade(const GuessPoint& gp);

public slots:

    void showComputerMove(const GuessPoint& gp);

	/*
	* Compute whether done button is active in the board editor
	*/
	Q_INVOKABLE void verifyPlanePositionValid();

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
    std::vector<GuessPoint> m_GuessList;

    ///@todo: to define
    QColor m_SelectedPlaneColor = QColor(0, 0, 255);
    QColor m_PlaneHeadColor = QColor(0, 255, 0);
    QColor m_PlaneIntersectionColor = QColor(255, 0, 0);
    QColor m_PaddingColor = QColor("aqua");
    QColor m_BoardColor = QColor("#ea7025");
    QColor m_InvalidPointColor = QColor(255, 255, 255);
};

#endif // PLANEGRIDQML_H
