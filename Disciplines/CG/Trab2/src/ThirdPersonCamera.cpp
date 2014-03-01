#include "ThirdPersonCamera.h"


ThirdPersonCamera::ThirdPersonCamera(void)
{
}


ThirdPersonCamera::~ThirdPersonCamera(void)
{
}


void ThirdPersonCamera::prepareCamera()
{
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	gluPerspective(75, 4/3, 0.01, 100);
    gluLookAt(eye.x, eye.y, eye.z,
              center.x, center.y, center.z,
              0.0f, 0.0f, 1.0f);
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();

}
