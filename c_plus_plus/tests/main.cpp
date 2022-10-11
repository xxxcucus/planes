#include "cancelroundcommobjtest.h"
#include "connecttogamecommobjtest.h"

int main(int argc, char** argv)
{
   int status = 0;
   {
      CancelRoundCommObjTest tc;
      status |= QTest::qExec(&tc, argc, argv);
   }
   {
      ConnectToGameCommObjTest tc;
      status |= QTest::qExec(&tc, argc, argv);
   }
   return status;
}