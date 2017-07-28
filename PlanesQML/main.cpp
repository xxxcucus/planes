#include <QGuiApplication>
#include <QQmlApplicationEngine>
#include <QQmlContext>
#include "planegridqml.h"

int main(int argc, char *argv[])
{
    QGuiApplication app(argc, argv);

    QQmlApplicationEngine engine;
    PlaneGridQML pgq(10, 10, 3, false);
    engine.rootContext()->setContextProperty("PlaneGrid",&pgq);
    engine.load(QUrl(QStringLiteral("qrc:/main.qml")));
    pgq.initGrid();

    return app.exec();
}
