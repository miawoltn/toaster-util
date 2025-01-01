package com.miawoltn.toasterlibrary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.identy.face.Attempt
import com.identy.face.FaceOutput
import com.identy.face.IdentyError
import com.identy.face.IdentyFaceLocalMatch
import com.identy.face.IdentyFaceSdk
import com.identy.face.IdentyResponse
import com.identy.face.IdentyResponseListener
import com.identy.face.InitializationListener
import com.identy.face.enums.FaceCaptureMode
import com.identy.face.enums.FaceTemplate
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private val logger = Logger(MainActivity::class.simpleName.toString())
    private var faceLicense = "android_face_com.barnksforte.verxid.lic"
    private var fingerLicense = "android_finger_com.barnksforte.verxid.lic"
    private var docLicense = "1154_com.barnksforte.verxid2021-06-16 00_00_00.lic"
    private var BASE_PATH = "%s/Captures"

    var displayResult : Boolean = false
    var debug: Boolean = false

    // Face Settings
    val faceName = "face_output"
    var facePath = "%s/facial_biometrics"
    private var processedFacePath = "%s/processed_face"
    var cameraMode: FaceCaptureMode = FaceCaptureMode.FRONTAL_MODE
    var faceTemplates: ArrayList<FaceTemplate> = arrayListOf(FaceTemplate.JPEG)
    var faceTemplateToReturn: FaceTemplate = FaceTemplate.JPEG
    var saveFaceOutput: Boolean = false
    var checkIcao: Boolean = false
    var displayImages: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        captureFace()
    }

    private fun captureFace() {
        if (!Helpers.hasCameraPermission(this)) {
            Helpers.requestCameraPermission(this, this)
        } else {
            try {
                IdentyFaceSdk.newInstance(
                    this@MainActivity,
                    faceLicense,
                    object : InitializationListener<IdentyFaceSdk> {
                        override fun onInit(d: IdentyFaceSdk) {
                            try {
                                faceTemplates.add(faceTemplateToReturn)
                                d.run {
                                    displayResult(displayResult)
                                    displayImages = displayImages
                                    setRequiredTemplates(faceTemplates)
                                    disableTraining()

//                                    setASSecLevel(AS.MEDIUM)
//                            enableBackgroundRemoval()
                                    val match = IdentyFaceLocalMatch()
                                    enableICAOChecks()
//                                    debug = (debug)
                                    cameraMode = cameraMode
                                    capture()
                                }
                            } catch (e: Exception) {
                                logger.error(e.message!!)
//                                e.message?.let { onFaceCapture.onError(it) }
                            }

                        }

                        override fun onInitFailed() {
//                            onFaceCapture.onError("Error initialising.Try again.")
                        }

                    },
                    object : IdentyResponseListener {
                        override fun onAttempt(p0: Int, p1: Attempt?) {

                        }

                        override fun onResponse(
                            response: IdentyResponse,
                            hashSet: HashSet<String>
                        ) {
//                        logger.info("FACE_CAPTURE:onResponse", response.toJson(activity).toString())
                            val dir = File(facePath)
                            if (!dir.exists()) {
                                dir.mkdirs()
                            }

                            val output: FaceOutput = response.prints

                            if(checkIcao) {
                                if (response.icaoPrints != null) {
                                    val icaoCheckResults = response.icaoPrints.icaoResult
                                    logger.info(icaoCheckResults.toJson().toString())
                                    val multipleFaces = icaoCheckResults.multiple_faces
//                                    val eyeFullVisibility = icaoCheckResults.eyes_full_visibility
                                    val rightEyeVisibility = icaoCheckResults.eyeR_visibility
                                    val leftEyeVisibility = icaoCheckResults.eyeL_visibility
                                    val mouthVisibility = icaoCheckResults.mouth_visibility
                                    val backgroundContinuity = icaoCheckResults.background_continuity
                                    val lighting = icaoCheckResults.illumination_uniformity

                                    if(multipleFaces) {
//                                        onFaceCapture.onError("Multiple faces detect.")
                                        logger.debug("Multiple faces detect.")
                                        return
                                    }
                                    if(rightEyeVisibility < 80 || leftEyeVisibility < 80 || mouthVisibility < 80) {
//                                        onFaceCapture.onError("Face elements not fully visible.")
                                        logger.debug("Face elements not fully visible.")
                                        return
                                    }
                                    if(backgroundContinuity < 70) {
//                                        onFaceCapture.onError("Background not plain.")
                                        logger.debug("Background not plain.")
                                        return
                                    }
                                    if(lighting < 40) {
//                                        onFaceCapture.onError("Face lighting is uneven.")
                                        logger.debug("Face lighting is uneven.")
                                        return
                                    }
                                }
                            }

                            if (saveFaceOutput) {
                                for (entry: MutableMap.MutableEntry<FaceTemplate, String> in output.templates) {
//                                logger.info("TEMPLATES", entry.toString())
                                    val base64Str = entry.value

                                    try {
                                        val outputStream = FileOutputStream(
                                            File(
                                                facePath,
                                                "$faceName.${entry.key}"
                                            ).absolutePath
                                        )
                                        outputStream.write(FileCodecBase64.decode(base64Str, 0))
                                    } catch (e: Exception) {
                                        e.printStackTrace()
//                                        e.message?.let { onFaceCapture.onError(it) }
                                    }
                                }
                            }
//
//                            onFaceCapture.onComplete()
//                            onFaceCapture.onComplete(output.templates[faceTemplateToReturn] ?: "")
                        }

                        override fun onErrorResponse(error: IdentyError, p1: HashSet<String>?) {
                            logger.error(error.message)
//                            onFaceCapture.onError("Face not captured")
                        }

                    }, true
                )
            } catch (e: Exception) {
                e.printStackTrace()
//                onFaceCapture.onError("Error occured.Try again.")
            }
        }
    }

}