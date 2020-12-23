package com.example.test

import android.util.Log
import java.time.YearMonth

private typealias Airport = Flight.Airport

fun generateFlights(): List<Flight> {
    val list = mutableListOf<Flight>()
    val currentMonth = YearMonth.now()

//    Log.d("이건 어떻게 뜰까", "${YearMonth.now()}")
//
//    val currentMonth17 = currentMonth.atDay(17)
//
//    list.add(Flight(currentMonth17.atTime(14, 0), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, null))
//    list.add(Flight(currentMonth17.atTime(21, 30), Airport("하의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_grey_700, null))
//
////    val currentMonth22 = currentMonth.atDay(22)
////    list.add(Flight(currentMonth22.atTime(13, 20), Airport("Ibadan", "IBA"), Airport("Benin", "BNI"), R.color.blue_800))
////    list.add(Flight(currentMonth22.atTime(17, 40), Airport("Sokoto", "SKO"), Airport("Ilorin", "ILR"), R.color.red_800))
//
//    val currentMonth22 = currentMonth.atDay(22)
//    list.add(Flight(currentMonth22.atTime(13, 20), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "data:image/jpeg;base64,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"))
//    list.add(Flight(currentMonth22.atTime(17, 40), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.red_800, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfPPTMnXP6bGHP2nq1z-uQIjavz3xEXdgcbhrx0MYGVoRvD1EAXLsGA4GzZSgGcUoY4Dfop5bN&usqp=CAc"))
//
//    list.add(Flight(currentMonth.atDay(3).atTime(20, 0), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.teal_700, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2EGgUT4iwXpFoOgEbwButFCBmlt6GWAV7NQ&usqp=CAU"))
//
//    list.add(Flight(currentMonth.atDay(12).atTime(18, 15), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.cyan_700, null))
//
//    val nextMonth13 = currentMonth.plusMonths(1).atDay(13)
//    list.add(Flight(nextMonth13.atTime(7, 30), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSC9Fu5plLh2KhY6rniFJrqsq1hng3ytXXtLA&usqp=CAU"))
//    list.add(Flight(nextMonth13.atTime(10, 50), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj9DUKxUNMCDLOfQBJ7I966Sxyz5Igvt4JTw&usqp=CAU"))
//    list.add(Flight(currentMonth.minusMonths(1).atDay(9).atTime(20, 15), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.orange_800, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQrQXzIxSKQbyVI3ffqCQuYxJ4tg_85K8vgaeDGpcLLUTIZ_Ddu5ItLEjQ85juWnxWRTQQqEC5H&usqp=CAc")
//    )


//    val a20201101 = currentMonth.minusMonths(1).atDay(1).atTime(10, 15)
//    list.add(Flight((a20201101), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.01_0001.jpg"))
//    list.add(Flight((a20201101), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.01_0002.jpg"))
//    list.add(Flight((a20201101), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.01_0003.jpg"))
//    list.add(Flight((a20201101), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.01_0004.jpg"))


    for (i in (1..9)){
        var empty = currentMonth.minusMonths(1).atDay(i).atTime(7, 15)
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0001.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0002.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0003.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0004.jpg"))
    }

    for (i in (10..30)){
        var empty = currentMonth.minusMonths(1).atDay(i).atTime(7, 15)
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0001.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0002.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0003.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0004.jpg"))
    }

    for (i in (1..9)){
        var empty = currentMonth.atDay(i).atTime(7, 15)
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0001.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0002.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0003.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.0"+i+"_0004.jpg"))
    }

    for (i in (10..21)){
        var empty = currentMonth.atDay(i).atTime(7, 15)
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0001.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0002.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0003.jpg"))
        list.add(Flight((empty), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11."+i+"_0004.jpg"))
    }

    list.add(Flight((currentMonth.atDay(23).atTime(7, 15)), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.pink_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.17_0001.jpg"))
    list.add(Flight((currentMonth.atDay(23).atTime(7, 15)), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.green_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.13_0002.jpg"))
    list.add(Flight((currentMonth.atDay(23).atTime(7, 15)), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.blue_800, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.11_0003.jpg"))
    list.add(Flight((currentMonth.atDay(23).atTime(7, 15)), Airport("상의", "다른 정보"), Airport("하의", "다른 정보"), R.color.brown_700, "https://s3transferimages1209133644-dev.s3.amazonaws.com/public/2020.11.11_0004.jpg"))

    Log.d("이건 어떻게 뜰까", "${list}")
    return list
}

