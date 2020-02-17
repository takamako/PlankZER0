/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tensorflow.lite.examples.posenet

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
//import android.support.v7.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.plank.CompareActivity
import com.example.plank.ImageActivity
import com.example.plank.R

class CameraActivity : AppCompatActivity() {

  fun showHelp(){
    AlertDialog.Builder(this).apply {
      val imageView = ImageView(context)
      imageView.setImageResource(R.drawable.how2use)
      imageView.scaleType = ImageView.ScaleType.FIT_XY
      imageView.adjustViewBounds = true
      setView(  imageView )
//      setTitle("Title")
//      setMessage("Message")
//      setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
//        // OKをタップしたときの処理
//        Toast.makeText(context, "Dialog OK", Toast.LENGTH_LONG).show()
//      })
//      setNegativeButton("Cancel", null)
      show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    showHelp()

    setContentView(R.layout.activity_camera)
    savedInstanceState ?: supportFragmentManager.beginTransaction()
      .replace(R.id.container, PosenetActivity())
      .commit()

    val PhotoButton = findViewById<ImageButton>(R.id.helpPose)
    PhotoButton.setOnClickListener {
      showHelp()
    }

  }
}
