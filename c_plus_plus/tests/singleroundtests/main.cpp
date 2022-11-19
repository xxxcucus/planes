#include "planetest.h"
#include "coordinate2dtest.h"
#include "vectoriteratortest.h"
#include "planepointiteratortest.h"
#include "planeintersectingpointiteratortest.h"

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

   return status;
}