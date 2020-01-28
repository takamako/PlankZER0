package com.example.plank

import android.util.Log
import org.tensorflow.lite.examples.posenet.lib.BodyPart
import org.tensorflow.lite.examples.posenet.lib.Person

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
    var sholderY :Float = 0F
    var hipX:Float = 0F
    var hipY:Float = 0F
    var kneeX:Float = 0F
    var kneeY:Float = 0F
    //if(person.keyPoints.containsAll())
    for(keyPoint in person.keyPoints){
        var body =keyPoint.bodyPart
        val position = keyPoint.position

        if(body== BodyPart.LEFT_SHOULDER || body== BodyPart.RIGHT_SHOULDER) {
            sholderX = position.x.toFloat() * widthRatio + left
            sholderY = position.y.toFloat() * heightRatio + top
        }
        else if(body== BodyPart.LEFT_HIP || body== BodyPart.RIGHT_HIP) {
            hipX = position.x.toFloat() * widthRatio + left
            hipY = position.y.toFloat() * heightRatio + top
        }
        else if(body== BodyPart.LEFT_KNEE || body== BodyPart.RIGHT_KNEE) {
            kneeX = position.x.toFloat() * widthRatio + left
            kneeY = position.y.toFloat() * heightRatio + top
        }


    }
    Log.d("xxxxxxxxx", "sholder:  x=$sholderX y=$sholderY")
    Log.d("xxxxxxxxx", "hip:  x=$hipX y=$hipY")
    Log.d("xxxxxxxxx", "knee:  x=$kneeX y=$kneeY")




    return judg_flag
}