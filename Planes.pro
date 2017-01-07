TEMPLATE = subdirs

SUBDIRS = common PlanesWidget PlanesGraphicsScene

PlanesWidget.depends = common
PlanesGraphicsScene.depends = common

