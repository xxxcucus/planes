SOURCES += \
    main.cpp \
    planesgswindow.cpp \
    planesgsview.cpp \
    leftpane.cpp \
    gamestatsframe.cpp \
    rightpane.cpp \
    gridsquare.cpp \
    playareagridsquare.cpp \
    playerboard.cpp \
    genericboard.cpp \
    computerboard.cpp \
    customgraphicsscene.cpp


HEADERS += \
    planesgswindow.h \
    planesgsview.h \
    leftpane.h \
    gamestatsframe.h \
    rightpane.h \
    gridsquare.h \
    playareagridsquare.h \
    playerboard.h \
    genericboard.h \
    computerboard.h \
    customgraphicsscene.h

QT += widgets
CONFIG += console

win32:CONFIG(release, debug|release): LIBS += -L$$OUT_PWD/../common/release/ -lcommon
else:win32:CONFIG(debug, debug|release): LIBS += -L$$OUT_PWD/../common/debug/ -lcommon
else:unix: LIBS += -L$$OUT_PWD/../common/ -lcommon

INCLUDEPATH += $$PWD/../common
DEPENDPATH += $$PWD/../common
