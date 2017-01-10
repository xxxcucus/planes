SOURCES += \
    main.cpp \
    planesgswindow.cpp \
    planesgsview.cpp \
    leftpane.cpp \
    gamestatsframe.cpp \
    rightpane.cpp \
    gameboard.cpp \
    gridsquare.cpp \
    playareagridsquare.cpp


HEADERS += \
    planesgswindow.h \
    planesgsview.h \
    leftpane.h \
    gamestatsframe.h \
    rightpane.h \
    gameboard.h \
    gridsquare.h \
    playareagridsquare.h

QT += widgets

win32:CONFIG(release, debug|release): LIBS += -L$$OUT_PWD/../common/release/ -lcommon
else:win32:CONFIG(debug, debug|release): LIBS += -L$$OUT_PWD/../common/debug/ -lcommon
else:unix: LIBS += -L$$OUT_PWD/../common/ -lcommon

INCLUDEPATH += $$PWD/../common
DEPENDPATH += $$PWD/../common
