TEMPLATE = subdirs

SUBDIRS = common PlanesWidget PlanesGraphicsScene \
    PlanesQML

PlanesWidget.depends = common
PlanesGraphicsScene.depends = common

