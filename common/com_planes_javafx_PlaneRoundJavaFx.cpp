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

JNIEXPORT jint JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_getPlaneNo
(JNIEnv *, jobject)
{
	return global_Round->getPlaneNo();
}

JNIEXPORT jint JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_getPlaneSquareType
(JNIEnv *, jobject, jint i, jint j, jboolean isComputer)
{
	return global_Round->getPlaneSquareType(i, j, bool(isComputer));
}

JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_movePlaneLeft
(JNIEnv *, jobject, jint idx)
{
	global_Round->movePlaneLeft(int(idx));
}

JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_movePlaneRight
(JNIEnv *, jobject, jint idx)
{
	global_Round->movePlaneRight(int(idx));
}


JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_movePlaneUpwards
(JNIEnv *, jobject, jint idx)
{
	global_Round->movePlaneUpwards(int(idx));
}


JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_movePlaneDownwards
(JNIEnv *, jobject, jint idx)
{
	global_Round->movePlaneDownwards(int(idx));
}


JNIEXPORT void JNICALL Java_com_planes_javafx_PlaneRoundJavaFx_rotatePlane
(JNIEnv *, jobject, jint idx)
{
	global_Round->rotatePlane(int(idx));
}