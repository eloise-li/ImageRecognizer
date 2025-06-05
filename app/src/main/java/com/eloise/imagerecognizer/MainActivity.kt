package com.eloise.imagerecognizer

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.dnn.Dnn
import org.opencv.dnn.Net
import org.opencv.imgproc.Imgproc
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var resultText: TextView
    private lateinit var selectBtn: Button

    private lateinit var net: Net
    private lateinit var labels: List<String>

    private val SELECT_IMAGE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            var w = window;
//            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        resultText = findViewById(R.id.resultText)
        selectBtn = findViewById(R.id.selectButton)

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(this, "OpenCV failed to load", Toast.LENGTH_SHORT).show()
            return
        }

        loadModel()

        selectBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, SELECT_IMAGE)
        }
    }

    private fun loadModel() {
        val protoPath = assetFilePath("mobilenet_deploy.prototxt")
        val modelPath = assetFilePath("mobilenet.caffemodel")
        val labelInput = assets.open("synset.txt")
        labels = BufferedReader(InputStreamReader(labelInput)).readLines()
        net = Dnn.readNetFromCaffe(protoPath, modelPath)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val uri: Uri? = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            imageView.setImageBitmap(bitmap)
            classify(bitmap)
        }
    }

    private fun classify(bitmap: Bitmap) {
        try {
            val inputBlob = preprocess(bitmap)
            net.setInput(inputBlob)
            val output = net.forward(net.unconnectedOutLayersNames[0])

//            for (i in 0 until inputBlob.dims()) {
//                println("dim[$i] = ${inputBlob.size(i)}")
//            }

//            val output = net.forward("prob")
//            val outputNames = net.unconnectedOutLayersNames
//            println("Output layers: $outputNames")
//            val output = net.forward(outputNames[0])


            if (output.empty() || output.total() == 0L) {
                resultText.text = "model reasoning fails, output is empty!"
                return
            }

//            val probs = FloatArray(output.size(1).toInt())
//            output.get(0, 0, probs)
//
//            var maxIdx = 0
//            var maxVal = probs[0]
//            for (i in 1 until probs.size) {
//                if (probs[i] > maxVal) {
//                    maxVal = probs[i]
//                    maxIdx = i
//                }
//            }

            val reshaped = output.reshape(1, output.size(1))
            if (reshaped.empty() || reshaped.rows() == 0) {
                resultText.text = "no valid result output!"
                return
            }

            for (i in 0 until output.dims()) {
                println("dim[$i] = ${output.size(i)}")
            }

            val result = Core.minMaxLoc(reshaped)
            val maxConfidence = result.maxVal
            val maxIndex = result.maxLoc.y.toInt()

            val label = labels.getOrNull(maxIndex) ?: "Unknown"
            val confidencePercent = maxConfidence * 100.0

            resultText.text = "result：$label\nconfidence：%.2f%%".format(confidencePercent)

        } catch (e: Exception) {
            e.printStackTrace()
            resultText.text = "exception：${e.message}"
        }
    }



    private fun preprocess(bitmap: Bitmap): Mat {
        val resized = Bitmap.createScaledBitmap(bitmap, 224, 224, false)
        val mat = Mat()
        Utils.bitmapToMat(resized, mat)
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB)

        return Dnn.blobFromImage(
            mat,
            1.0 / 255.0,
            Size(224.0, 224.0),
            Scalar(0.0, 0.0, 0.0),
            true,
            false
        )
    }




    private fun assetFilePath(assetName: String): String {
        val file = File(filesDir, assetName)
        if (file.exists() && file.length() > 0) return file.absolutePath
        assets.open(assetName).use { input ->
            FileOutputStream(file).use { output ->
                val buffer = ByteArray(1024)
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
        return file.absolutePath
    }
}
