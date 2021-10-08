package com.websarva.wings.android.aquacare01

class DefaultValues {

    /*common*/
    val datePtn = "MM / dd"
    val timePtn = "HH : mm"

    /*notification機能*/
    val taskSaveFileName = "savedTaskInAquariumCare"
    //    アラーム表示最大数
    val nfMaxNum = 5
    //    アラームに関する各種キーを設定
    val alarmTaskNameKey = "taskName"
    val alarmNextLongKey = "taskNext"
    val alarmPrevLongKey = "taskPrev"
    val alarmRepeatDaysKey = "taskRepeat"
    val alarmBooleanKey = "taskState"

    /*record機能*/
    //sharedPreferencesのファイル名
    val recSpFileName = "savedRecordInAquariumCare"

    //sharedPreferencesのKeyの名前のKey
    val recFileNameKey = "recordFileName"
    val recDateKey = "recordDate"
    //画像の最大表示枚数
    val recMaxNum = 20

    //record機能で保存するファイルの先頭文字列
    val recFileName = "recordImg"
}