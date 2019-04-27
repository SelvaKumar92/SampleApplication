package com.tvs.sample.fragments


import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tvs.sample.BuildConfig
import com.tvs.sample.R
import com.tvs.sample.entities.UserData
import com.tvs.sample.utilis.SASession
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class SADetailFragment : Fragment() {

    var filePath: Uri? = null

    private var mySession: SASession? = null

    private var myNameTXT: TextView? = null
    private var myDateTXT: TextView? = null
    private var myLocationTXT: TextView? = null
    private var mySalaryTXT: TextView? = null
    private var myPositionTXT: TextView? = null

    // TODO: Rename and change types of parameters

    private var mParam1: String? = null

    private val TAKE_PHOTO = 89

    private var myCaptureIMG: ImageView? = null

    private var myTakePhotoBT: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mySession = SASession(activity!!)

    }

    private fun loadData() {
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)
            val aUserData = Gson().fromJson(mParam1, UserData::class.java)
            myNameTXT!!.text = aUserData.name + "-" + aUserData.code
            myDateTXT!!.text = aUserData.date_of_joining
            myLocationTXT!!.text = aUserData.location
            myPositionTXT!!.text = aUserData.post
            mySalaryTXT!!.text = aUserData.salary

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val aVew = inflater.inflate(R.layout.fragment_detail, container, false)

        intalize(aVew)

        loadData()

        clickLisstener()

        return aVew
    }

    private fun clickLisstener() {
        myTakePhotoBT!!.setOnClickListener { onTakePhoto() }
    }

    fun onTakePhoto() {
        if (!checkPermissions()) {
            Dexter.withActivity(activity)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            captureImage()

                        } else if (report.isAnyPermissionPermanentlyDenied) {
                            showPermissionsAlert()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        } else {
            captureImage()
        }

    }


    /**
     * Launching camera app to capture image
     */
    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

        filePath = fileUri

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

        // start the image capture Intent
        startActivityForResult(intent, TAKE_PHOTO)
    }

    /**
     * Creating file uri to store image/video
     */
    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    private fun showPermissionsAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("TVS")
            .setMessage("Camera permissions required. Allow them in settings.")
            .setPositiveButton("SETTINGS") { dialog, which ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            .setNegativeButton("CANCEL", null).show()
    }

    private fun intalize(aView: View) {
        mySession = SASession(activity!!)
        myNameTXT = aView.findViewById<View>(R.id.name) as TextView
        myDateTXT = aView.findViewById<View>(R.id.date) as TextView
        myLocationTXT = aView.findViewById<View>(R.id.location) as TextView
        myPositionTXT = aView.findViewById<View>(R.id.position) as TextView
        mySalaryTXT = aView.findViewById<View>(R.id.salary) as TextView
        myCaptureIMG = aView.findViewById<View>(R.id.photo_image) as ImageView
        myTakePhotoBT = aView.findViewById<View>(R.id.photo_txt) as Button

    }

    fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // if the result is capturing Image
        if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val dateTime = sdf.format(Calendar.getInstance().time)
                val bmp = BitmapFactory.decodeFile(filePath?.path)
                addWatermark(resources, bmp, dateTime)

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(
                    activity,
                    "User cancelled image capture", Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                // failed to capture image
                Toast.makeText(
                    activity,
                    "Failed to capture image", Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }


    fun addWatermark(res: Resources, source: Bitmap, aTet: String): Bitmap {
        val w: Int
        val h: Int
        val c: Canvas
        val paint: Paint
        val bmp: Bitmap
        val watermark: Bitmap
        val matrix: Matrix
        val scale: Float
        val r: RectF
        w = source.width
        h = source.height
        // Create the new bitmap
        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
        // Copy the original bitmap into the new one
        c = Canvas(bmp)
        c.drawBitmap(source, 0f, 0f, paint)
        // Load the watermark
        watermark = BitmapFactory.decodeResource(res, R.drawable.ic_tvs_motors_logo)
        // Scale the watermark to be approximately 40% of the source image height
        scale = (h.toFloat() * 0.40 / watermark.height.toFloat()).toFloat()
        // Create the matrix
        matrix = Matrix()
        matrix.postScale(scale, scale)
        // Determine the post-scaled size of the watermark
        r = RectF(0f, 0f, watermark.width.toFloat(), watermark.height.toFloat())
        matrix.mapRect(r)
        // Move the watermark to the bottom right corner
        matrix.postTranslate(w - r.width(), h - r.height())
        // Draw the watermark
        c.drawBitmap(watermark, matrix, paint)
        paint.textSize = 24f
        paint.color = resources.getColor(R.color.stamp)
        paint.isUnderlineText = true
        c.drawText(aTet, watermark.width.toFloat(), watermark.height.toFloat(), paint)
        // Free up the bitmap memory
        watermark.recycle()
        myCaptureIMG!!.visibility = View.VISIBLE
        myCaptureIMG!!.setImageBitmap(bmp)
        return bmp
    }

    companion object {

        // TODO: Rename parameter arguments, choose names that match

        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

        private val ARG_PARAM1 = "data"

        /**
         * returning image / video
         */
        private fun getOutputMediaFile(type: Int): File? {

            // External sdcard location
            val mediaStorageDir = File("/storage/emulated/0/", "WaterMarkImages")

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }

            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(mediaStorageDir.path + File.separator + "test" + ".jpg")
            } else {
                return null
            }

            return mediaFile
        }
    }
}
