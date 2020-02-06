package com.example.plank

import android.util.Log
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import java.lang.Float.min

fun plankJudg(person:Person,widthRatio:Float,heightRatio:Float,top:Int,left:Int):Boolean{
    //肩，腰，膝の三点から角度計算して姿勢判定
    var judg_flag:Boolean =false
    class sholder{
        var x =0F
        var y=0F
    }
    class hip{
        var x=0F
        var y=0F
    }
    class knee{
        var x=0F
        var y=0F
    }
    var sholderX :Float = 0F
    var sholderY :Float = 1000000F
    var hipX:Float = 0F
    var hipY:Float = 1000000F
    var kneeX:Float = 0F
    var kneeY:Float = 1000000F
    //if(person.keyPoints.containsAll())
    for(keyPoint in person.keyPoints){
        var body =keyPoint.bodyPart
        val position = keyPoint.position

        if(body== BodyPart.LEFT_SHOULDER || body== BodyPart.RIGHT_SHOULDER) {
            sholderX = position.x.toFloat() * widthRatio + left
            sholderY = minOf(sholderY,position.y.toFloat() * heightRatio + top)
        }
        else if(body== BodyPart.LEFT_HIP || body== BodyPart.RIGHT_HIP) {
            hipX = position.x.toFloat() * widthRatio + left
            hipY = minOf(hipY,position.y.toFloat() * heightRatio + top)
        }
        else if(body== BodyPart.LEFT_KNEE || body== BodyPart.RIGHT_KNEE) {
            kneeX = position.x.toFloat() * widthRatio + left
            kneeY = minOf(kneeY,position.y.toFloat() * heightRatio + top)
        }


    }
    val sholderToHipX=sholderX-hipX
    val sholderToHipY =sholderY-hipY
    val hipToKneeX =hipX-kneeX
    val hipToKneeY = hipY-kneeY
    val sholderToKneeX = sholderX - kneeX
    val sholerToKneeY = sholderY -kneeY
    val babc = sholderToHipX * hipToKneeX+ sholderToHipY * hipToKneeY;
    val ban = (sholderToHipX * sholderToHipX) + (hipToKneeX * hipToKneeX)
    val bcn = (sholderToHipY * sholderToHipY) + (hipToKneeY * hipToKneeY)
    val radian = Math.acos(babc / (Math.sqrt((ban * bcn).toDouble())))
    val angle = radian * 180 / Math.PI  // 結果（ラジアンから角度に変換）
    Log.d("xxxxxxxxx", "angle:  $angle")
//    Log.d("xxxxxxxxx", "sholder:  x=$sholderX y=$sholderY")
//    Log.d("xxxxxxxxx", "hip:  x=$hipX y=$hipY")
//    Log.d("xxxxxxxxx", "knee:  x=$kneeX y=$kneeY")




    return judg_flag
}