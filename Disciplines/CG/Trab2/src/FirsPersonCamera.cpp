#include "FirsPersonCamera.h"

FirsPersonCamera::FirsPersonCamera()
{
}

FirsPersonCamera::~FirsPersonCamera()
{
}

/************************************************************************/
/* Prepara a camera em primeira pessoa                                   */
/************************************************************************/
void FirsPersonCamera::prepareCamera()
{
    float x_up = 0.0f;
    float y_up = 0.0f;
    float z_up = 1.0f;
    
    glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
    
	gluPerspective(75, 4/3, 0.01, 100);
    gluLookAt(eye.x, eye.y, eye.z,
              center.x, center.y, center.z,
              x_up, y_up, z_up);
    
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
    
}

