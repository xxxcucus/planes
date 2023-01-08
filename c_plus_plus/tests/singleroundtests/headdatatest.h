#ifndef __HEAD_DATA_TEST__
#define __HEAD_DATA_TEST__


#include <QObject>
#include <QTest>


class HeadDataTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void HeadData_Init();
    void HeadData_UpdateCorrectOrientSet();
    void HeadData_UpdateOneOrientationCompletAndNotDiscarded();
    void HeadData_UpdateThreeOrientationsDiscardedAndOneNotDiscarded();
    void HeadData_UpdateFourOrientationsDiscarded();
    void HeadData_UpdateTwoOrientationsDiscardedAndTwoNotDiscarded();
    void cleanupTestCase();
};

#endif