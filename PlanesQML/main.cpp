#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QTime>
#include "planegridqml.h"
#include "planesmodel.h"
#include "planeround.h"

int main(int argc, char *argv[])
{
    QTime time = QTime::currentTime();
    int seed = time.msec();
    srand(seed);

    QGuiApplication app(argc, argv);
    QQmlApplicationEngine engine;

    //The model object
    PlanesModel* mPlanesModel;
    //The controller object
    PlaneRound* mRound;

    //builds the model object
    mPlanesModel = new PlanesModel(10, 10, 3);

    //builds the game object - the controller
    mRound = new PlaneRound(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), false);
    mRound->play();

    PlaneGridQML player_pgq(mPlanesModel->playerGrid());
    PlaneGridQML computer_pgq(mPlanesModel->computerGrid());
    player_pgq.initGrid1();
    computer_pgq.initGrid1();

    engine.rootContext()->setContextProperty("PlayerPlaneGrid", &player_pgq);
    engine.rootContext()->setContextProperty("ComputerPlaneGrid", &computer_pgq);
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    //pgq.initGrid();
    player_pgq.initGrid1();

    return app.exec();
}
