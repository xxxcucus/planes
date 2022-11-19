#include "planetest.h"
#include "coordinate2dtest.h"

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

   return status;
}