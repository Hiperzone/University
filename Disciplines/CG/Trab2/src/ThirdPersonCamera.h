#include "Common.h"

#ifndef CUSTOMCAMERA_H
#define CUSTOMCAMERA_H
class ThirdPersonCamera : public Camera
{
public:
	ThirdPersonCamera(void);
	~ThirdPersonCamera(void);

	void prepareCamera();
};

#endif


