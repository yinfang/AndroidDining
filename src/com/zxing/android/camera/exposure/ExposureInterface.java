package com.zxing.android.camera.exposure;

import android.hardware.Camera;

/**
 * Implementations control auto-exposure settings of the camera, if available.
 * 
 * @author Sean Owen
 */
public interface ExposureInterface {

	void setExposure(Camera.Parameters parameters, boolean lightOn);

}
