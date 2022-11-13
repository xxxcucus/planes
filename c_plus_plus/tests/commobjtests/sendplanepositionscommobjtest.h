#ifndef __SEND_PLANE_POSITIONS_COMMOBJ_TEST__
#define __SEND_PLANE_POSITIONS_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "sendplanepositionscommobj.h"
#include "multiplayerround.h"

class SendPlanePositionsCommObjTest : public QObject {
    Q_OBJECT

private:
    class MultiplayerRoundMock : public MultiplayerRound {
        void getPlayerPlaneNo(int pos, Plane& pl) override {
            pl.row(2);
            pl.col(1);
            pl.orientation(Plane::Orientation::NorthSouth);
        }

        bool setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) override {       
            testPlanes(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient);
            return true;
        }

        void testPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) {
            QVERIFY(plane1_x == 1);
            QVERIFY(plane1_y == 2);
            QVERIFY((int)plane1_orient == 0);
            QVERIFY(plane2_x == 3);
            QVERIFY(plane2_y == 4);
            QVERIFY((int)plane2_orient == 1);
            QVERIFY(plane3_x == 5);
            QVERIFY(plane3_y == 6);
            QVERIFY((int)plane3_orient == 2);
        }
    };

private:
    SendPlanePositionsCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTestOtherExistTrue();
    void ProcessResponseTestOtherExistFalse();
    void cleanupTestCase();
};

#endif