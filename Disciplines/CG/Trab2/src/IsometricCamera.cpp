#include "IsometricCamera.h"


IsometricCamera::IsometricCamera(void)
{
}


IsometricCamera::~IsometricCamera(void)
{
}

/**
 * Prepara a camera em modo isometrico
 */
void IsometricCamera::prepareCamera()
{
    float x_up = 0.0f;
    float y_up = 0.0f;
    float z_up = 1.0f;
    
    GLdouble fovy = 75;
    GLdouble aspect = 4/3;
    GLdouble zNear = 0.1;
    GLdouble zFar = 100;
    

	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	gluPerspective( fovy, aspect, zNear, zFar);
  
    gluLookAt(eye.x, eye.y, eye.z,
              center.x, center.y, center.z,
              x_up, y_up, z_up);
    
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
}


