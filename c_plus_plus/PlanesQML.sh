#!/bin/sh
appname=`basename $0 | sed s,\.sh$,,`
dirname=`dirname $0`
#export LIBGL_ALWAYS_SOFTWARE=true
#export QT_XCB_GL_INTEGRATION=none
export QT_QUICK_BACKEND=software
LD_LIBRARY_PATH=$dirname/lib
export LD_LIBRARY_PATH
$dirname/bin/$appname "$@"
