#include "Camera.h"

int Camera::angle = 0;
Camera::Camera(void)
{
}

Camera::~Camera(void)
{
}

/************************************************************************/
/*Actualiza o vector para onde a camera olha.                           */
/************************************************************************/
void Camera::updateLookAt(float xCenter, float yCenter, float zCenter)
{
	center.x = xCenter;
	center.y = yCenter;
	center.z = zCenter;
}

/************************************************************************/
/*Actualiza o vector de onde a camara olha.                             */
/************************************************************************/
void Camera::updateLookFrom(float xEye, float yEye, float zEye)
{
	eye.x = xEye;
	eye.y = yEye;
	eye.z = zEye;
}

/************************************************************************/
/* Atribui a posicao da camera.                                         */
/************************************************************************/
void Camera::setPosition(int cameraID, int direction)
{
    vector3 playerPosition = Common::getGame()->getPlayer()->getPosition();
    float distanceFromPlayer = 0.0f;
    float diff = 0.1;
    
    switch (cameraID) {
        case CAMERA_MINIMAP:
        {
            printf("Camera normal\n");
        }break;
        case CAMERA_ISOMETRIC:
        {
            vector3 playerPosition = Common::getGame()->getPlayer()->getPosition();
            Common::getGame()->getCamera()->updateLookFrom( playerPosition.x - 2, playerPosition.y - 2, playerPosition.z + 5.0f);
            Common::getGame()->getCamera()->updateLookAt(playerPosition.x, playerPosition.y, playerPosition.z);
        }break;
        case CAMERA_FIRST_PERSON:
        {
            
            switch (direction) {
                case DIRECTION_X_POSITIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x + distanceFromPlayer, playerPosition.y + 0.2f, playerPosition.z + 0.2);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x + 10, playerPosition.y, playerPosition.z);
                }break;
                case DIRECTION_X_NEGATIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x - distanceFromPlayer, playerPosition.y + 0.2f, playerPosition.z + 0.2);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x - 10, playerPosition.y, playerPosition.z);
                }break;
                case DIRECTION_Y_POSITIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x, playerPosition.y + distanceFromPlayer , playerPosition.z + 0.2);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x, playerPosition.y + 10, playerPosition.z);
                }break;
                case DIRECTION_Y_NEGATIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x, playerPosition.y - distanceFromPlayer , playerPosition.z + 0.2);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x, playerPosition.y - 10, playerPosition.z);
                }break;
                default:
                    break;
            }
            
        }break;
        case CAMERA_THIRD_PERSON:
        {
            switch (direction) {
                case DIRECTION_X_POSITIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom(playerPosition.x - distanceFromPlayer , playerPosition.y + diff, playerPosition.z + 0.4);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x + 10, playerPosition.y +diff, playerPosition.z);
                }break;
                case DIRECTION_X_NEGATIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x + distanceFromPlayer + 0.2, playerPosition.y + diff, playerPosition.z + 0.4);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x - 10, playerPosition.y + diff, playerPosition.z);
                }break;
                case DIRECTION_Y_POSITIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x + diff, playerPosition.y - distanceFromPlayer, playerPosition.z + 0.4);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x + diff, playerPosition.y + 10, playerPosition.z);
                }break;
                case DIRECTION_Y_NEGATIVE:
                {
                    Common::getGame()->getCamera()->updateLookFrom( playerPosition.x + diff, playerPosition.y + distanceFromPlayer + 0.2 , playerPosition.z + 0.4);
                    Common::getGame()->getCamera()->updateLookAt(playerPosition.x + diff, playerPosition.y - 10, playerPosition.z);
                }break;
                default:
                    break;
            }
        }break;
        default:
            break;
    }
}

/**
 * Roda a camera graus para a 
 * esquerda (0) ou para a direita (1)
 *
 */
void Camera::rotate(int degree, int direction)
{
    for (int i = 0; i < degree; i++) {
        
        vector3 playerPosition =  Common::getGame()->getPlayer()->getPosition();
        if(direction == 0)
            angle=(angle+1)%360;
        if(direction == 1)
            angle=(angle-1)%360;
        float camXLookFrom = 10*cosf((angle)*0.0174532925f) * 0.09 + playerPosition.x;
        float camYLookFrom = 10*sinf((angle)*0.0174532925f) * 0.09 + playerPosition.y;
        
        float camXLookAt = 10*cosf((angle)*0.0174532925f) + playerPosition.x;
        float camYLookAt = 10*sinf((angle)*0.0174532925f) + playerPosition.y;
        
        Common::getGame()->getCamera()->updateLookFrom( camXLookFrom, camYLookFrom, 0.2f);
        Common::getGame()->getCamera()->updateLookAt( camXLookAt, camYLookAt, 0.2f);
    }
}

/************************************************************************/
/* Atribui o vector para o olho da camara.                              */
/************************************************************************/
void Camera::setEye(vector3 eye)
{
   this->eye = eye;
}

/************************************************************************/
/* Obtem o vector para o olho da camara.                                */
/************************************************************************/
vector3 Camera::getEye()
{
	return eye;
}

/************************************************************************/
/* Atribui o centro da camara.                                          */
/************************************************************************/
void Camera::setCenter(vector3 center)
{
	this->center = center;
}

/************************************************************************/
/* Obtém o centro da camara.                                            */
/************************************************************************/
vector3 Camera::getCenter()
{
	return center;
}
