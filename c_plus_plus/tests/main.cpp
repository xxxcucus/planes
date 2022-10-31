#include "cancelroundcommobjtest.h"
#include "connecttogamecommobjtest.h"
#include "creategamecommobjtest.h"
#include "logincommobjtest.h"
#include "norobotcommobjtest.h"
#include "refreshgamestatuscommobjtest.h"

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
   {
       CreateGameCommObjTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }
   {
       LoginCommObjTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }
   {
       NoRobotCommObjTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }
   {
       RefreshGameStatusCommObjTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }
   return status;
}