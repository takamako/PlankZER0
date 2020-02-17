package com.example.plank

import android.util.Log
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person
import java.lang.Float.min
import java.lang.Math.atan2
import java.lang.Math.floor
import kotlin.math.atan2

fun plankJudg(person:Person,widthRatio:Float,heightRatio:Float,top:Int,left:Int): Double {
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
    var sholderX :Float = 1000000F
    var sholderY :Float = 0F
    var hipX:Float = 1000000F
    var hipY:Float = 0F
    var kneeX:Float = 1000000F
    var kneeY:Float = 0F
    var elbowX:Float = 1000000F
    //if(person.keyPoints.containsAll())
    for(keyPoint in person.keyPoints){
        var body =keyPoint.bodyPart
        val position = keyPoint.position

        if(body== BodyPart.LEFT_SHOULDER || body== BodyPart.RIGHT_SHOULDER) {
            sholderX = minOf(sholderX,position.x.toFloat() * widthRatio + left)
            sholderY = position.y.toFloat() * heightRatio + top
        }
        else if(body== BodyPart.LEFT_HIP || body== BodyPart.RIGHT_HIP) {
            hipX = minOf(hipX,position.x.toFloat() * widthRatio + left)
            hipY = position.y.toFloat() * heightRatio + top
        }
        else if(body== BodyPart.LEFT_KNEE || body== BodyPart.RIGHT_KNEE) {
            kneeX = minOf(kneeX,position.x.toFloat() * widthRatio + left)
            kneeY = position.y.toFloat() * heightRatio + top
        }
//        else if(body== BodyPart.LEFT_ELBOW || body== BodyPart.RIGHT_ELBOW) {
//            elbowX = minOf(elbowX,position.x.toFloat() * widthRatio + left)
//        }


    }
    val sholderToHipX=hipX-sholderX
    val sholderToHipY =hipY-sholderY
    val hipToKneeX =hipX-kneeX
    val hipToKneeY = hipY-kneeY
//    val sholderToAnkleX = sholderX - kneeX
//    val sholderToAnkleY = sholderY -kneeY

    val dot = sholderToHipX * hipToKneeX+ sholderToHipY * hipToKneeY// dot product
    val cross = sholderToHipX * hipToKneeY- sholderToHipY * hipToKneeX // cross product
    val alpha = atan2(cross, dot)
    var angle=floor(alpha * 180.0 / Math.PI + 0.5)

    if(kneeX<hipX){
        angle=0.0
    }

    return angle

//    val babc = sholderToHipX * hipToKneeX+ sholderToHipY * hipToKneeY
//    val ban = (sholderToHipX * sholderToHipX) + (hipToKneeX * hipToKneeX)
//    val bcn = (sholderToHipY * sholderToHipY) + (hipToKneeY * hipToKneeY)
//    val radian = Math.acos(babc / (Math.sqrt((ban * bcn).toDouble())))
//    val angle = radian * 180 / Math.PI  // 結果（ラジアンから角度に変換）
//    Log.d("xxxxxxxxx", "angle:  $angle")
//    Log.d("xxxxxxxxx", "sholder:  x=$sholderX y=$sholderY")
//    Log.d("xxxxxxxxx", "hip:  x=$hipX y=$hipY")
//    Log.d("xxxxxxxxx", "knee:  x=$kneeX y=$kneeY")





//    return judg_flag
//    return angle
}

//int CGlEngineFunctions::GetAngleABC( POINTFLOAT a, POINTFLOAT b, POINTFLOAT c )
//{
//    POINTFLOAT ab = { b.x - a.x, b.y - a.y };
//    POINTFLOAT cb = { b.x - c.x, b.y - c.y };
//
//    float dot = (ab.x * cb.x + ab.y * cb.y); // dot product
//    float cross = (ab.x * cb.y - ab.y * cb.x); // cross product
//
//    float alpha = atan2(cross, dot);
//
//    return (int) floor(alpha * 180. / pi + 0.5);
//}