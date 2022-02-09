package com.c1ctech.sharedatatofacebook

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.c1ctech.sharedatatofacebook.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookSdk
import com.facebook.share.widget.ShareDialog
import android.content.Intent
import com.facebook.share.model.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_VIDEO_CODE: Int = 1000
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var shareDialog: ShareDialog
    lateinit var callbackManager: CallbackManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        // initializes the Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)

        setContentView(activityMainBinding.root)

        // create an instance of callbackManager
        // It manages the callbacks into the FacebookSdk from an Activity's onActivityResult()'s method.
        callbackManager = CallbackManager.Factory.create()

        //create a ShareDialog.
        shareDialog = ShareDialog(this)

        //share a link from this app to Facebook
        activityMainBinding.btnShareLink.setOnClickListener {

            val linkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://www.youtube.com/"))
                .setQuote("Useful link")
                .build()

            //returns True if the ShareLinkContent type can be shown via the dialog
            if (ShareDialog.canShow(ShareLinkContent::class.java)) {

                shareDialog.show(linkContent)
            }

        }

        //share a photo from this app to Facebook
        activityMainBinding.btnSharePhoto.setOnClickListener {

            val image: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tiger)
            val photo = SharePhoto.Builder()
                .setBitmap(image)
                .build()

            //returns True if the SharePhotoContent type can be shown via the dialog
            if (ShareDialog.canShow(SharePhotoContent::class.java)) {

                val photoContent = SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build()
                shareDialog.show(photoContent)
            }

        }

        //share a video from this app to Facebook
        activityMainBinding.btnShareVideo.setOnClickListener {

            // uploading video from phone
            val sharingIntent = Intent()
            sharingIntent.setType("video/*")
            sharingIntent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                Intent.createChooser(sharingIntent, "Select a video"),
                REQUEST_VIDEO_CODE
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // call the SDK's callbackManager in your onActivityResult to handle the response
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_VIDEO_CODE) {
                val videoUrl = data?.data

                val video = ShareVideo.Builder()
                    .setLocalUrl(videoUrl)
                    .build()

                val videoContent = ShareVideoContent.Builder()
                    .setVideo(video)
                    .setContentTitle("This is useful video")
                    .setContentDescription("Video made by me")
                    .build()

                //returns True if the ShareVideoContent type can be shown via the dialog
                if (ShareDialog.canShow(ShareVideoContent::class.java)) {

                    shareDialog.show(videoContent)
                }
            }
        }
    }
}

