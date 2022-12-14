#!/bin/sh
appname=`basename $0 | sed s,\.sh$,,`
dirname=`dirname $0`
export LIBGL_ALWAYS_SOFTWARE=true
LD_LIBRARY_PATH=$dirname/lib
export LD_LIBRARY_PATH
$dirname/bin/$appname "$@"
