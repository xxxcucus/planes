#include "stompframetest.h"
#include "stompframeparsertest.h"

#include <QTest>

int main(int argc, char** argv)
{
   int status = 0;
   {
      StompFrameTest tc;
      status |= QTest::qExec(&tc, argc, argv);
   }

   {
      StompFrameParserTest tc;
      status |= QTest::qExec(&tc, argc, argv);
   }

   return status;
}
