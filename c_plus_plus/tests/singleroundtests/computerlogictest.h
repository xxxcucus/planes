#ifndef __COMPUTER_LOGIC_TEST__
#define __COMPUTER_LOGIC_TEST__


#include <QObject>
#include <QTest>


class ComputerLogicTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void ComputerLogic_MapIndexToPlane();
    void ComputerLogic_UpdateChoiceMapMissInfo();
    void ComputerLogic_UpdateChoiceMapHitInfo();
    void ComputerLogic_UpdateChoiceMapPlaneData();
    void ComputerLogic_MakeChoiceFindHeadModeMaxExists();
    void ComputerLogic_MakeChoiceFindHeadModeMaxDoesNotExist();
    void ComputerLogic_MakeChoiceFindPositionModeChoiceDoesNotExist();
    void ComputerLogic_MakeChoiceFindPositionModeChoiceDoesExist();
    void ComputerLogic_MakeChoiceRandomModeChoiceDoesExist();
    void ComputerLogic_MakeChoiceRandomModeChoiceDoesNotExist();
    void cleanupTestCase();
};

#endif