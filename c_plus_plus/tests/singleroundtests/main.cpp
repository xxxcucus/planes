#include "planetest.h"
#include "coordinate2dtest.h"
#include "vectoriteratortest.h"
#include "planepointiteratortest.h"
#include "planeintersectingpointiteratortest.h"
#include "orientationtest.h"
#include "guesspointtest.h"
#include "gamestagestest.h"
#include "planeorientationdatatest.h"
#include "headdatatest.h"
#include "planegridtest.h"
#include "computerlogictest.h"

int main(int argc, char** argv)
{
   int status = 0;
   {
      PlaneTest tc;
      status |= QTest::qExec(&tc, argc, argv);
   }

   {
       Coordinate2DTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       VectorIteratorTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       PlanePointIteratorTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       PlaneIntersectingPointIteratorTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       OrientationTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       GuessPointTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       GameStagesTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }


   {
       PlaneOrientationDataTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       HeadDataTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       PlaneGridTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   {
       ComputerLogicTest tc;
       status |= QTest::qExec(&tc, argc, argv);
   }

   return status;
}