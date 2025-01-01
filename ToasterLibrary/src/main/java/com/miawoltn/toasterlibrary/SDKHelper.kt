import android.app.Activity
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.Base64
import android.util.Log
import android.util.Pair
import androidx.annotation.RequiresApi
import com.identy.face.*
import com.identy.face.AS
import com.identy.face.enums.FaceCaptureMode
import com.identy.face.enums.FaceTemplate
import com.identy.face.enums.UIOption
import com.identy.face.exception.LicenseValidationException
import com.miawoltn.toasterlibrary.FileCodecBase64
import com.miawoltn.toasterlibrary.Helpers
import com.miawoltn.toasterlibrary.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


class SDKHelper private constructor(private var activity: Activity) {
    private val logger = Logger(SDKHelper::class.simpleName.toString())
    private var faceLicense = "android_face_com.barnksforte.verxid.lic"
    private var fingerLicense = "android_finger_com.barnksforte.verxid.lic"
    private var docLicense = "1154_com.barnksforte.verxid2021-06-16 00_00_00.lic"
    private var BASE_PATH = "%s/Captures"
    var context: Context

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

    // Finger Settings
//    var fingerPath = "%s/finger_biometrics"
//    private var processedFingerPath = "%s/processed_fingers"
//    var fingers: ArrayList<Finger> = arrayListOf()
//    var fingerTemplates: List<Template> = arrayListOf(Template.PNG, Template.WSQ)
//    var fingerTemplateToReturn: Template = Template.WSQ
//    val templatesConfig: HashMap<Template, HashMap<Finger, ArrayList<TemplateSize>>> = hashMapOf()
//    var baseEncoding: Int = Base64.DEFAULT
//    var displayBoxes: Boolean = true
    var displayImages: Boolean = false
//    var enableSpoofCheck: Boolean = true
//    var compression: WSQCompression = WSQCompression.WSQ_10_1
//    var detectionModes: List<FingerDetectionMode> = arrayListOf()
//    var calculateNFIQ: Boolean = true
//    var allowHandChange: Boolean = false
//    var saveFingerOutput: Boolean = false

    // OCR Settings
//    var documentOutputPath = "%s/ocr_scan"
//    var documentName = "doc_scan.jpeg"
//    var scanType: DocScanType = DocScanType.A4.CUSTOM_CROP
//    var extractInfo = false
//    var captureFromGallery = false
//    var saveOCROutput: Boolean = false
//
//    // Signature Settings
//    var signatureOutputPath = "%s/signature"
//    var signatureFileName = "signature.jpeg"


    init {
        context = activity.baseContext
        BASE_PATH = String.format(BASE_PATH, context.filesDir.absolutePath)
        if(!File(BASE_PATH).exists()) File(BASE_PATH).mkdir()
        facePath = String.format(facePath, BASE_PATH)
        processedFacePath = String.format(processedFacePath, BASE_PATH)
//        fingerPath = String.format(fingerPath, BASE_PATH)
//        processedFingerPath = String.format(processedFingerPath, BASE_PATH)
//        documentOutputPath = String.format(documentOutputPath, BASE_PATH)
//        signatureOutputPath = String.format(signatureOutputPath, BASE_PATH)
    }

    companion object {
//        val RIGHT_INDEX = "02"
//        val RIGHT_MIDDLE = "03"
//        val RIGHT_RING = "04"
//        val RIGHT_LITTLE = "05"
//        val RIGHT_THUMB = "06"
//        val LEFT_INDEX = "07"
//        val LEFT_MIDDLE = "08"
//        val LEFT_RING = "09"
//        val LEFT_LITTLE = "10"
//        val LEFT_THUMB = "11"
//        fun getFingerPosition(position: String): FingerDetectionMode {
//            return when(position) {
//                RIGHT_THUMB -> FingerDetectionMode.RIGHT_THUMB
//                RIGHT_INDEX -> FingerDetectionMode.RIGHT_INDEX
//                RIGHT_MIDDLE -> FingerDetectionMode.RIGHT_MIDDLE
//                RIGHT_RING -> FingerDetectionMode.RIGHT_RING
//                RIGHT_LITTLE -> FingerDetectionMode.RIGHT_LITTLE
//                LEFT_THUMB -> FingerDetectionMode.LEFT_THUMB
//                LEFT_INDEX -> FingerDetectionMode.LEFT_INDEX
//                LEFT_MIDDLE -> FingerDetectionMode.LEFT_MIDDLE
//                LEFT_RING -> FingerDetectionMode.LEFT_RING
//                LEFT_LITTLE -> FingerDetectionMode.LEFT_LITTLE
//                else -> FingerDetectionMode.RIGHT_THUMB
//            }
//        }
//
//        fun getFinger(position: String): Finger {
//            return when(position) {
//                RIGHT_THUMB, LEFT_THUMB -> Finger.THUMB
//                RIGHT_INDEX, LEFT_INDEX -> Finger.INDEX
//                RIGHT_MIDDLE, LEFT_MIDDLE -> Finger.MIDDLE
//                RIGHT_RING, LEFT_RING-> Finger.RING
//                RIGHT_LITTLE, LEFT_LITTLE -> Finger.LITTLE
//                else -> Finger.THUMB
//            }
//        }
//        fun getHand(position: String): Hand {
//            return when(position) {
//                RIGHT_THUMB, RIGHT_INDEX, RIGHT_MIDDLE, RIGHT_RING, RIGHT_LITTLE -> Hand.RIGHT
//                else -> Hand.LEFT
//            }
//        }
        fun getInstance(activity: Activity): SDKHelper {
            return SDKHelper(activity)
        }
    }

    /*==================================== Face ==========================================*/
    fun capturedFaceString(): String? {
        return if(capturedFaceFile() != null){
            val bm = BitmapFactory.decodeFile(capturedFaceFile()?.absolutePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object

            val b = baos.toByteArray()

            Base64.encodeToString(b, Base64.DEFAULT)
        } else {
            null
        }
    }

    fun capturedFaceByteArray(): ByteArray? {
        return if(capturedFaceFile() != null){
            val bm = BitmapFactory.decodeFile(capturedFaceFile()?.absolutePath)
            val baos = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
            baos.toByteArray()
        } else {
            null
        }
    }

    fun capturedFaceBitmap(): Bitmap? {
        return if(capturedFaceFile() != null) {
            /*val bt = */BitmapFactory.decodeFile(capturedFaceFile()?.absolutePath)
//            Bitmap.createScaledBitmap(bt,  600 ,600, true)
        }else {
            null
        }
    }

    fun capturedFaceFile(): File? {
        val file =  File(
            facePath,
            "$faceName.${faceTemplateToReturn.name}"
        )
        if(file.exists()) {
            return file
        }
        return null
    }

    fun captureFace(onFaceCapture: OnFaceCapture) {
        if (!Helpers.hasCameraPermission(context)) {
            Helpers.requestCameraPermission(context, activity)
        } else {
            try {
                IdentyFaceSdk.newInstance(
                    activity,
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
                                e.message?.let { onFaceCapture.onError(it) }
                            }

                        }

                        override fun onInitFailed() {
                            onFaceCapture.onError("Error initialising.Try again.")
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
                                        onFaceCapture.onError("Multiple faces detect.")
                                        logger.debug("Multiple faces detect.")
                                        return
                                    }
                                    if(rightEyeVisibility < 80 || leftEyeVisibility < 80 || mouthVisibility < 80) {
                                        onFaceCapture.onError("Face elements not fully visible.")
                                        logger.debug("Face elements not fully visible.")
                                        return
                                    }
                                    if(backgroundContinuity < 70) {
                                        onFaceCapture.onError("Background not plain.")
                                        logger.debug("Background not plain.")
                                        return
                                    }
                                    if(lighting < 40) {
                                        onFaceCapture.onError("Face lighting is uneven.")
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
                                        e.message?.let { onFaceCapture.onError(it) }
                                    }
                                }
                            }

                            onFaceCapture.onComplete()
                            onFaceCapture.onComplete(output.templates[faceTemplateToReturn] ?: "")
                        }

                        override fun onErrorResponse(error: IdentyError, p1: HashSet<String>?) {
                            logger.error(error.message)
                            onFaceCapture.onError("Face not captured")
                        }

                    }, true
                )
            } catch (e: Exception) {
                e.printStackTrace()
                onFaceCapture.onError("Error occured.Try again.")
            }
        }
    }

    fun initFaceMatch(
        selfie: String,
        picture: String,
        onFaceMatch: OnFaceMatch
    ) {
        try {
            IdentyFaceSdk.newInstance(
                activity, faceLicense,
                object : InitializationListener<IdentyFaceSdk> {
                    override fun onInit(d: IdentyFaceSdk) {
//                        d.de = displayImages
                        d.displayResult(displayResult)
//                        d.setDebug(debug)
                        d.uioption = UIOption.STANDARD
                        val match: FaceMatch = IdentyFaceLocalMatch()
                        try {
                            d.matchwithPictureId(match, FaceTemplate.JPEG,
                                FileCodecBase64.decode(selfie, Base64.DEFAULT),
                                FaceTemplate.JPEG,
                                FileCodecBase64.decode(picture, Base64.DEFAULT))
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            onFaceMatch.onError("Unable to verify you. Try again.")
                        }
                    }

                    override fun onInitFailed() {}
                },
                object : IdentyResponseListener {
                    override fun onAttempt(i: Int, attempt: Attempt) {}
                    override fun onResponse(
                        identyResponse: IdentyResponse,
                        hashSet: java.util.HashSet<String>
                    ) {
                        val res = (identyResponse as VerifyIdentyResponse)
//                        val similarityScore = (identyResponse as VerifyIdentyResponse).similarityScore;

                        onFaceMatch.onComplete(res.isAuthSucess)
                    }

                    override fun onErrorResponse(
                        identyError: IdentyError,
                        hashSet: java.util.HashSet<String>
                    ) {
                        onFaceMatch.onError(identyError.message)
                    }
                },false
            )
        } catch (e: LicenseValidationException) {
            e.printStackTrace()
            onFaceMatch.onError(e.message.toString())
        }
    }

    /*================================ Finger ======================================*/
//    fun capturedFingerString(finger: String): String {
//        return if(capturedFingerFile(finger) != null){
//            if(fingerTemplateToReturn == Template.WSQ) {
//                return Base64.encodeToString(FileCodecBase64.loadFileAsBytesArray(capturedFingerFile(finger)?.absolutePath), Base64.DEFAULT)
//            }
//            val bm = BitmapFactory.decodeFile(capturedFingerFile(finger)?.absolutePath)
//            val baos = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
//
//            val b = baos.toByteArray()
//
//            Base64.encodeToString(b, Base64.DEFAULT)
//        } else {
//            ""
//        }
//    }

//    fun capturedFingerBinary(finger: String): ByteArray? {
//        return if(capturedFingerFile(finger) != null){
//            if(fingerTemplateToReturn == Template.WSQ) {
//                return FileCodecBase64.loadFileAsBytesArray(capturedFingerFile(finger)?.absolutePath)
//            }
//            val bm = BitmapFactory.decodeFile(capturedFingerFile(finger)?.absolutePath)
//            val baos = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
//            baos.toByteArray()
//        } else {
//            null
//        }
//    }

//    fun capturedFingerImage(finger: String): Bitmap? {
//        return if(capturedFingerFile(finger) != null) {
//            BitmapFactory.decodeFile(capturedFingerFile(finger)?.absolutePath)
//        }else {
//            null
//        }
//    }

//    fun capturedFingerFile(finger: String): File? {
//        val file =  File(
//            fingerPath,
//            "$finger.${fingerTemplateToReturn.name}"
//        )
//        if(file.exists()) {
//            return file
//        }
//        return null
//    }

//    fun captureFingers(onFingerCapture: OnFingerCapture) {
//        if (!Helpers.hasCameraPermission(context)) {
//            Helpers.requestCameraPermission(context, activity)
//        } else {
//            try {
//                val templatesConfig =
//                    hashMapOf<Template, HashMap<Finger, ArrayList<TemplateSize>>>()
//                val fingerSizeHP = HashMap<Finger, java.util.ArrayList<TemplateSize>>()
//                val sizes = ArrayList<TemplateSize>()
//                sizes.add(TemplateSize.DEFAULT)
//
//                for (finger in fingers) {
//                    fingerSizeHP[finger] = sizes
//                }
//
//                for (template in fingerTemplates) {
//                    templatesConfig[template] = fingerSizeHP
//                }
//
//                VerxidSDK.newInstance(activity, fingerLicense, { d ->
//                    try {
//                        d.run {
//                            setDebug(debug)
//                            setRequiredTemplates(templatesConfig)
//                            setDisplayBoxes(displayBoxes)
//                            setDetectionMode(detectionModes.toTypedArray())
//                            setAllowHandChange(allowHandChange)
//                            setAsSecMode(FingerAS.MEDIUM)
////                        setQualityMode(qualityMode)
////                        isAssistedMode = isAssisted
//                            wsqCompression = compression
//                            base64EncodingFlag = baseEncoding
//                            capture()
//                        }
//                    } catch (e: Exception) {
//                        logger.error(e.message!!)
//                        onFingerCapture.onError("Error while initialising. Try again.")
//                    }
//                },
//                    object : com.identy.IdentyResponseListener {
//                        override fun onAttempt(
//                            hand: Hand?,
//                            attemptCount: Int,
//                            attempt: MutableMap<Finger, com.identy.Attempt>?
//                        ) {
//
//                        }
//
//                        override fun onResponse(
//                            response: com.identy.IdentyResponse,
//                            transactionIds: java.util.HashSet<String>?
//                        ) {
////                        val fingers = mutableMapOf<String, String?>()
//                            val fingerMetas = mutableMapOf<String, FingerMetaData>()
//                            for (entry: MutableMap.MutableEntry<Pair<Hand, Finger>, FingerOutput> in response.prints) {
//                                val handFinger: Pair<Hand, Finger> = entry.key
//                                val fingerOutput: FingerOutput = entry.value
//                                var name=""
//                                if(handFinger.first != null){
//                                    name = getFileNamingConvention(
//                                        handFinger.first,
//                                        handFinger.second
//                                    )
//                                }
//                                val fingerMetaData = FingerMetaData(
//                                    data = "",
//                                    quality = fingerOutput.identyQuality,
//                                    width = fingerOutput.width,
//                                    height = fingerOutput.height,
//                                    spoofScore = fingerOutput.spoofScore
//                                )
//                                fingerOutput.toJson(context).also {
//                                    fingerMetaData.overallTime = it.optInt("overallTimeTaken")
//                                    fingerMetaData.captureTime = it.optInt("captureTimeTaken")
//                                    fingerMetaData.processingTime = it.optInt("processingTimeTaken")
//                                    fingerMetaData.nfiq = it.optInt("nfiq_1")
//                                }
//                                for (templateEntry in fingerOutput.templates) {
//
//                                    try {
//                                        val fingerData = templateEntry.value[TemplateSize.DEFAULT]
//
//                                        if (templateEntry.key == fingerTemplateToReturn) {
////                                        fingers[name] = fingerData
//                                            fingerMetaData.data = fingerData!!
//                                        }
//                                        if (saveFingerOutput) {
//                                            val fingerDir = File(fingerPath)
//                                            if (!fingerDir.exists()) fingerDir.mkdir()
//                                            val fingerFile = File(
//                                                fingerPath,
//                                                name + "." + templateEntry.key
//                                            )
//                                            val outputStream = FileOutputStream(fingerFile)
//                                            outputStream.write(
//                                                FileCodecBase64.decode(
//                                                    fingerData,
//                                                    Base64.DEFAULT
//                                                )
//                                            )
//                                            outputStream.flush()
//                                            outputStream.close()
//                                        }
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                        onFingerCapture.onError("Error while capturing. Please try again.")
//                                    }
//                                }
//
//                                fingerMetas[name] = fingerMetaData
//                            }
//                            onFingerCapture.onComplete()
//                            onFingerCapture.onComplete(fingerMetas)
//                        }
//
//                        override fun onErrorResponse(
//                            error: com.identy.IdentyError,
//                            transactionId: java.util.HashSet<String>?
//                        ) {
//                            onFingerCapture.onError(error.message)
//                        }
//
//                    }, true
//                )
//            } catch (e: Exception) {
//                onFingerCapture.onError("Error while capturing. Please try again")
//            }
//        }
//    }
//
//    fun captureRightFingers(onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(Finger.INDEX, Finger.MIDDLE, Finger.RING,Finger.LITTLE))
//        detectionModes = listOf(FingerDetectionMode.R4F)
//        captureFingers(onFingerCapture)
//    }

//    fun captureLeftFingers(onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(Finger.INDEX, Finger.MIDDLE, Finger.RING,Finger.LITTLE))
//        detectionModes = listOf(FingerDetectionMode.L4F)
//        captureFingers(onFingerCapture)
//    }

//    fun captureThumbs(onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(Finger.THUMB))
//        detectionModes = listOf(FingerDetectionMode.TWO_THUMB)
//        captureFingers(onFingerCapture)
//    }
//
//    fun captureRightThumb(onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(Finger.THUMB))
//        detectionModes = listOf(FingerDetectionMode.RIGHT_THUMB)
//        captureFingers(onFingerCapture)
//    }

//    fun captureLeftThumb(onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(Finger.THUMB))
//        detectionModes = listOf(FingerDetectionMode.LEFT_THUMB)
//        captureFingers(onFingerCapture)
//    }
//
//    fun captureFinger(finger: Finger, detectionMode: FingerDetectionMode, onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(arrayListOf(finger))
//        detectionModes = listOf(detectionMode)
//        captureFingers(onFingerCapture)
//    }

//    fun captureFingers(fingerList: List<Finger>, detectionModeList: List<FingerDetectionMode>, onFingerCapture: OnFingerCapture) {
//        fingers.clear()
//        fingers.addAll(fingerList)
//        detectionModes = detectionModeList
//        captureFingers(onFingerCapture)
//    }

//    fun verifyFinger(
//        templates: HashMap<Pair<Hand, Finger>, ByteArray>,
//        onFingerMatch: OnFingerMatch
//    ) {
//        templatesConfig.clear()
//        val fingerSizeHP = HashMap<Finger, java.util.ArrayList<TemplateSize>>()
//        val sizes = ArrayList<TemplateSize>()
//        sizes.add(TemplateSize.DEFAULT)
//        for (finger in fingers) {
//            fingerSizeHP[finger] = sizes
//        }
//        for(template in fingerTemplates) {
//            templatesConfig[template] = fingerSizeHP
//        }
//        try {
//            IdentySdk.newInstance(
//                activity,fingerLicense,
//                { d ->
//                    d.run {
//                        setDebug(debug)
//                        setRequiredTemplates(templatesConfig)
//                        base64EncodingFlag = Base64.DEFAULT
//                        setDisplayImages(displayImages)
//                        setDisplayBoxes(displayBoxes)
//                        wsqCompression = compression
//                        setAllowHandChange(allowHandChange)
//                        setDetectionMode(detectionModes.toTypedArray())
//                        isCalculateNFIQ = calculateNFIQ
//                        enableCustomSDK()
//                        verifyWithTemplatesbyteArray(Template.WSQ, templates)
//                    }
//                },
//                object : com.identy.IdentyResponseListener {
//                    override fun onAttempt(
//                        hand: Hand,
//                        attemptCount: Int,
//                        attempt: Map<Finger, com.identy.Attempt>
//                    ) {}
//
//                    override fun onResponse(
//                        response: com.identy.IdentyResponse,
//                        transactionIds: java.util.HashSet<String>
//                    ) {
//                        try {
//                            onFingerMatch.onComplete(response.prints.values.first().score > 100)
//                        } catch (e: java.lang.Exception) {
//                            Log.e("initFingerMatch:Exception", e.message!!)
//                            e.printStackTrace()
//                            onFingerMatch.onError(e.message!!)
//                        }
//                    }
//
//                    override fun onErrorResponse(
//                        error: com.identy.IdentyError,
//                        transactionIds: java.util.HashSet<String>
//                    ) {
//                        onFingerMatch.onError(error.message)
//                    }
//                },true
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            onFingerMatch.onError(e.message!!)
//        }
//    }

    private fun calculateBrightnessEstimate(bitmap: Bitmap, pixelSpacing: Int): Int {
        var R = 0
        var G = 0
        var B = 0
        val height = bitmap.height
        val width = bitmap.width
        var n = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 0
        while (i < pixels.size) {
            val color = pixels[i]
            R += Color.red(color)
            G += Color.green(color)
            B += Color.blue(color)
            n++
            i += pixelSpacing
        }
        return (R + B + G) / (n * 3)
    }

    private fun calculateBrightness(bitmap: Bitmap): Int {
        return calculateBrightnessEstimate(bitmap, 1)
    }

//    private fun getFileNamingConvention(hand: Hand, finger: Finger): String {
//        if (hand == Hand.RIGHT) {
//            when (finger) {
//                Finger.INDEX -> {
//                    return RIGHT_INDEX
//                }
//                Finger.MIDDLE -> {
//                    return RIGHT_MIDDLE
//                }
//                Finger.RING -> {
//                    return RIGHT_RING
//                }
//                Finger.LITTLE -> {
//                    return RIGHT_LITTLE
//                }
//                Finger.THUMB -> {
//                    return RIGHT_THUMB
//                }
//            }
//        } else if (hand == Hand.LEFT) {
//            when (finger) {
//                Finger.INDEX -> {
//                    return LEFT_INDEX
//                }
//                Finger.MIDDLE -> {
//                    return LEFT_MIDDLE
//                }
//                Finger.RING -> {
//                    return LEFT_RING
//                }
//                Finger.LITTLE -> {
//                    return LEFT_LITTLE
//                }
//                Finger.THUMB -> {
//                    return LEFT_THUMB
//                }
//            }
//        }
//        return ""
//    }

    private fun showDialog(): android.app.Dialog {
        val dialog = android.app.Dialog(context)
        dialog.setTitle("Please wait...")
        dialog.setCancelable(false)
        dialog.show()
        return dialog
    }


    /*================================ OCR ======================================*/
//    fun captureDocument(onDocumentCapture: OnDocumentCapture) {
////        if (!Helpers.hasCameraPermission(context)) {
////            Helpers.requestCameraPermission(context, activity)
////        } else {
//            try {
//                IdentyOCRSdk.newInstance(
//                    activity, docLicense,
//                    { identyOCRSdk: IdentyOCRSdk ->
//                        identyOCRSdk.run {
//                            doctype = scanType
//                            if (captureFromGallery) {
//                                captureFromGallery(extractInfo)
//                            } else {
//                                capture(extractInfo)
//                            }
////                        capture(extractInfo)
//                        }
//                    },
//                    object : com.identy.ocr.IdentyResponseListener {
//                        override fun onResponse(identyResponse: com.identy.ocr.IdentyResponse) {
//                            val cards = identyResponse.resultBitmap
//                            if (cards.isNotEmpty()) {
//                                val frontSide = cards[0]
//                                val backSide = null //cards[1]
//
//                                if (saveOCROutput) {
//                                    val dir = File(documentOutputPath)
//                                    if (!dir.exists()) {
//                                        dir.mkdirs()
//                                    }
//                                    val docFile = File(documentOutputPath, documentName)
//                                    try {
//                                        val outputStream = FileOutputStream(
//                                            docFile.absolutePath
//                                        )
//                                        val byteArrayStream = ByteArrayOutputStream()
//                                        frontSide.compress(
//                                            Bitmap.CompressFormat.JPEG,
//                                            50,
//                                            byteArrayStream
//                                        )
//                                        outputStream.write(byteArrayStream.toByteArray())
//                                        onDocumentCapture.onComplete()
//                                        onDocumentCapture.onComplete(frontSide, backSide)
//                                    } catch (e: java.lang.Exception) {
//                                        e.printStackTrace()
//                                        onDocumentCapture.onError("Error processing document.")
//                                    }
//                                } else {
//                                    onDocumentCapture.onComplete()
//                                    onDocumentCapture.onComplete(frontSide, backSide)
//                                }
//                            } else {
//                                onDocumentCapture.onError("Document scanning failed. Try again.")
//                            }
//                        }
//
//                        override fun onErrorResponse(identyError: com.identy.ocr.IdentyError) {
//                            onDocumentCapture.onError("Error scanning document.")
//                        }
//                    }, true
//                )
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//                onDocumentCapture.onError("Unable to initiate scanning. Try again.")
//            }
////        }
//    }
//
//    fun capturedDocString(): String {
//        return if(capturedDocFile() != null){
//            val bm = BitmapFactory.decodeFile(capturedDocFile()?.absolutePath)
//            val baos = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
//
//            val b = baos.toByteArray()
//
//            Base64.encodeToString(b, Base64.DEFAULT)
//        } else {
//            ""
//        }
//    }
//
//    fun capturedDocFile(): File? {
//        val file =  File(
//            documentOutputPath,
//            documentName
//        )
//        if(file.exists()) {
//            return file
//        }
//        return null
//    }

    /*
    *
    * Signature
    *
    * */
//    @RequiresApi(Build.VERSION_CODES.O)
//    @Throws(IOException::class)
//    fun saveSignature(bitmap: Bitmap) {
//        if(!Files.exists(Paths.get(signatureOutputPath))) {
//            Files.createDirectories(Paths.get(signatureOutputPath))
//        }
//        val signatureFile = File(signatureOutputPath, signatureFileName)
//        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(newBitmap)
//        canvas.drawColor(Color.WHITE)
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//        Files.newOutputStream(signatureFile.toPath()).use { stream ->
//            newBitmap.compress(
//                Bitmap.CompressFormat.JPEG,
//                80,
//                stream
//            )
//        }
//    }
//
//    fun signatureFileBase64(): String {
//        return if(signatureFile() != null){
//            val bm = BitmapFactory.decodeFile(signatureFile()?.absolutePath)
//            val baos = ByteArrayOutputStream()
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) // bm is the bitmap object
//            val b = baos.toByteArray()
//            Base64.encodeToString(b, Base64.DEFAULT)
//        } else {
//            ""
//        }
//    }
//
//    fun signatureFile(): File? {
//        val file =  File(
//            signatureOutputPath,
//            signatureFileName
//        )
//        if(file.exists()) {
//            return file
//        }
//        return null
//    }


    fun clearData() {
        recursiveDelete(File(facePath))
//        recursiveDelete(File(fingerPath))
//        recursiveDelete(File(documentOutputPath))
//        recursiveDelete(File(signatureOutputPath))
    }

    fun recursiveDelete(file: File) {
        if (!file.exists()) return

        if (file.isDirectory) {
            val files = file.listFiles() ?: return
            for (f in files) {
                recursiveDelete(f)
            }
        } else {
            file.writeText("") // empty file content before deleting
        }
    }

    class FingerMetaData(
        var data: String,
        var quality: Double,
        var width: Int,
        var height: Int,
        var spoofScore: Float,
        var captureTime: Int? = null,
        var overallTime: Int? = null,
        var processingTime: Int? = null,
        var bits: Int? = null,
        var nfiq: Int? = null,
    )
}

interface OnFaceCapture {
    fun onComplete()
    fun onComplete(base64Str: String)
    fun onError(error: String)
}

//interface OnProcessFace {
//    fun onSuccess()
//    fun onError(error: String)
//}

//interface OnProcessBackground {
//    fun onSuccess(image: String)
//    fun onSuccess(imageUrl: String, imagePath: String)
//    fun onError(error: String)
//}

//interface OnFingerCapture {
//    fun onComplete()
//    //    fun onComplete(fingers: MutableMap<String, String?>)
//    fun onComplete(fingerMetas: MutableMap<String, SDKHelper.FingerMetaData>)
//    fun onError(message: String)
//}

interface OnFaceMatch {
    fun onComplete(isMatch: Boolean)
    fun onError(message: String)
}

//interface OnFingerMatch {
//    fun onComplete(isMatch: Boolean)
//    fun onError(message: String)
//}

//interface OnDocumentCapture {
//    fun onComplete()
//    fun onComplete(frontSide: Bitmap?, backSide: Bitmap?)
//    fun onError(message: String)
//}
