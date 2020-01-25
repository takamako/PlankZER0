package com.example.plank

import org.tensorflow.lite.examples.posenet.lib.Person

fun plankJudg(person:Person,widthRatio:Float,heightRatio:Float,top:Int,left:Int):Boolean{
    //肩，腰，膝の三点から角度計算して姿勢判定
    var judg_flag:Boolean =false
    var sholderX:Float?
    var sholderY:Float?
    var hipX:Float?
    var hipY:Float?
    var kneeX:Float?
    var kneeY:Float?
    for(keyPoint in person.keyPoints){
        val position = keyPoint.position
        val adjustedX: Float = position.x.toFloat() * widthRatio + left
        val adjustedY: Float = position.y.toFloat() * heightRatio + top
    }


    return judg_flag
}