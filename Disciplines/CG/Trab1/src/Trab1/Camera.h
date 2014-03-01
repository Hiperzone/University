
#include "Common.h"

#ifndef CAMERA_H
#define CAMERA_H


/**
 * Classe abstracta para implementacao de uma camera.
 *
 */
class Camera
{
public:
	Camera(void);
	virtual ~Camera(void);
	virtual void prepareCamera() = 0;
};

#endif
