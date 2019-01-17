#include "com_planes_javafx_PlaneRoundJavaFx.h"
#include "planeround.h"

PlaneRound* global_Round = nullptr;

JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_createPlanesRound
(JNIEnv *, jobject)
{
	printf("Create plane round 1\n");
	global_Round = new PlaneRound(10, 10, 3);
	printf("Create plane round 2\n");
}
