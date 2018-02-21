#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include <QTime>
#include "planegridqml.h"
#include "planegameqml.h"

int main(int argc, char *argv[])
{
    QTime time = QTime::currentTime();
    int seed = time.msec();
    srand(seed);

    QGuiApplication app(argc, argv);
    QQmlApplicationEngine engine;

    PlaneGameQML planeGame;
    PlaneGridQML player_pgq(planeGame.playerGrid());
    PlaneGridQML computer_pgq(planeGame.computerGrid());
    player_pgq.initGrid1();
    computer_pgq.initGrid1();

    engine.rootContext()->setContextProperty("PlayerPlaneGrid", &player_pgq);
    engine.rootContext()->setContextProperty("ComputerPlaneGrid", &computer_pgq);
    engine.rootContext()->setContextProperty("PlaneGame", &planeGame);
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    //pgq.initGrid();
    //player_pgq.initGrid1();

    return app.exec();
}
