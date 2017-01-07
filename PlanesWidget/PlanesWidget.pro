SOURCES += \
    baserenderarea.cpp \
    buttons.cpp \
    choicedebugwidget.cpp \
    debugrenderarea.cpp \
    editorwindow.cpp \
    editplanescontrolwidget.cpp \
    gamerenderarea.cpp \
    gamestatswidget.cpp \
    main.cpp \
    planeswview.cpp \
    planeswwindow.cpp

FORMS += \
    EditControl.ui \
    GameForm.ui

HEADERS += \
    baserenderarea.h \
    buttons.h \
    choicedebugwidget.h \
    debugrenderarea.h \
    editorwindow.h \
    editplanescontrolwidget.h \
    gamerenderarea.h \
    gamestatswidget.h \
    planeswwindow.h \
    planeswview.h

TARGET = PlanesWidget

QT += widgets

win32:CONFIG(release, debug|release): LIBS += -L$$OUT_PWD/../common/release/ -lcommon
else:win32:CONFIG(debug, debug|release): LIBS += -L$$OUT_PWD/../common/debug/ -lcommon
else:unix: LIBS += -L$$OUT_PWD/../common/ -lcommon

INCLUDEPATH += $$PWD/../common
DEPENDPATH += $$PWD/../common
