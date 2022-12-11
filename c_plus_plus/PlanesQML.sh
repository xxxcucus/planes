#!/bin/sh
appname=`basename $0 | sed s,\.sh$,,`
dirname=`dirname $0`
LD_LIBRARY_PATH=$dirname/lib
export LD_LIBRARY_PATH
$dirname/bin/$appname "$@"
