
#include "Common.h"

#ifndef CAMERA_H
#define CAMERA_H

// CAMERAS
#define CAMERA_MINIMAP 0
#define CAMERA_ISOMETRIC 1
#define CAMERA_FIRST_PERSON 2
#define CAMERA_THIRD_PERSON 3

#define DIRECTION_X_POSITIVE 0
#define DIRECTION_Y_POSITIVE 1
#define DIRECTION_X_NEGATIVE 2
#define DIRECTION_Y_NEGATIVE 3
#define DIRECTION_NONE 5


/**
 * Classe abstracta para implementacao de uma camera.
 *
 */
class Camera
{
protected:
	vector3 eye;
	vector3 center;
    
    static int angle;
  
public:
	Camera(void);
	virtual ~Camera(void);
	virtual void prepareCamera() = 0;
    virtual void updateLookAt(float xCenter, float yCenter, float zCenter);
    virtual void updateLookFrom(float xEye, float yEye, float zEye);
    
    // GETTERS AND SETTERS
	void setEye(vector3 v);
    vector3 getEye();
    
    void setCenter(vector3 v);
	vector3 getCenter();

    void setAngle(int angle);
    int getAngle();
    
    void setPosition(int cameraID, int direction);
    void rotate(int degree, int direction);
};

#endif
