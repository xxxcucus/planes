#include "com_planes_javafx_PlaneRoundJavaFx.h"
#include "planeround.h"

PlaneRound* global_Round = nullptr;

JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_createPlanesRound
(JNIEnv *, jobject)
{
	printf("Create plane round 1\n");
	global_Round = new PlaneRound(10, 10, 3);
	global_Round->initRound();
	printf("Create plane round 2\n");
}

JNIEXPORT jint JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_getRowNo
(JNIEnv *, jobject)
{
	return global_Round->getRowNo();
}

JNIEXPORT jint JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_getColNo
(JNIEnv *, jobject)
{
	return global_Round->getColNo();
}