#include "CustomCamera.h"


CustomCamera::CustomCamera(void)
{
}


CustomCamera::~CustomCamera(void)
{
}


void CustomCamera::prepareCamera()
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-1, 1, -1, 1, -1, 2);
	gluLookAt(0.25, 0.25, 0.25, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

}
