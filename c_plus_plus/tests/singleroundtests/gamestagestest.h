#ifndef __GAME_STAGES_TEST__
#define __GAME_STAGES_TEST__


#include <QObject>
#include <QTest>


class GameStagesTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void GameStages_Values();
    void cleanupTestCase();
};

#endif