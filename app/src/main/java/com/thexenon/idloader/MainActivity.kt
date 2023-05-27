package com.thexenon.idloader

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.thexenon.idloader.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    lateinit var binding: ActivityMainBinding
    private val REQUEST_IMAGE_CAPTURE = 111
    private val REQUEST_IMAGE_GALLERY = 222
    private var imageBitmap:Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {

            idsnapimagebtn.setOnClickListener {
                snapImage()
                idtextview.text = ""
            }
            idloadfromfilesbtn.setOnClickListener {
                galleryImage()
                idtextview.text = ""
            }
            idloadtextfromimagebtn.setOnClickListener {
                detectText()
            }



        }
    }

    private fun detectText() {
        if (imageBitmap != null){
            val image = imageBitmap?.let {
                InputImage.fromBitmap(it,0)
            }
            image?.let {
                recognizer.process(it)
                    .addOnSuccessListener {
                        binding.idtextview.text = it.text
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Text Recognition Failed", Toast.LENGTH_LONG).show()
                    }
            }
        } else {
            Toast.makeText(this, "No Image Detected", Toast.LENGTH_LONG ).show()
        }
    }

    private fun snapImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } catch (e:Exception){}

    }

    private fun galleryImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
        } catch (e:Exception){}

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val extras : Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            if (imageBitmap != null){
                binding.idimageview.setImageBitmap(imageBitmap)
            }
        } else {}
    }

}