package com.hardrelice.wyyparser.utils

infix fun String.times(other: Int):String{
    var string = ""
    for (x in 0 until other){
        string+=this
    }
    return string
}

infix fun Int.times(other: String):String{
    var string = ""
    for (x in 0 until this){
        string+=other
    }
    return string
}

infix fun Char.times(other: Int):String{
    var string = ""
    for (x in 0 until other){
        string+=this
    }
    return string
}

infix fun Int.times(other: Char):String{
    var string = ""
    for (x in 0 until this){
        string+=other
    }
    return string
}