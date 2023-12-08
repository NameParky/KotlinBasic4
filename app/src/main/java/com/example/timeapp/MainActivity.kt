package com.example.timeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)

        mainFun()

    }

    // 메인에 뷰를 다시불러올때 같이 건네줄 이벤트 함수.
    private fun mainFun() {

        val timeText = findViewById<TextView>(R.id.timeText)
        val timeTextFun = timeText.setOnClickListener {
            var fmDate = setTime()
            timeText.setText(fmDate)
            timeLoop(timeText)
        }
        val goStopGameBtn = findViewById<Button>(R.id.goStopGame).setOnClickListener {
            goStopGameFunPage()
        }

    }

    // 재귀호출로 시간을 1초단위로 갱신시켜줄 함수.
    private fun timeLoop(timeText : TextView) {
        Handler().postDelayed(Runnable {
            var fmDate = setTime()
            timeText.setText(fmDate)
            timeLoop(timeText)
        }, 1000)
    }

    // 현재시간데이터를 "시간 : 분 : 초로 넘겨줄 함수".
    private fun setTime() : String {
        var currentTimeMillis = System.currentTimeMillis()
        var currentTime = Date(currentTimeMillis)
        var sdfTime = SimpleDateFormat("HH : mm : ss")
        sdfTime.timeZone = TimeZone.getTimeZone("Asia/Seoul")
        var fmDate = sdfTime.format(currentTime)

        return fmDate
    }

    private fun goStopGameFunPage() {
        setContentView(R.layout.stop_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.stopGame)
        stopGameFun()
    }



    ////////////////////////// 스탑워치 게임화면과 함수들 시작부분

    var stopFlags = true
    var winFlags = true

    private fun stopGameFun() {

        stopFlags = true
        winFlags = true

        val goMainBtn = findViewById<Button>(R.id.goMain).setOnClickListener {
            goMainPage()
        }

        val setRanTime = findViewById<Button>(R.id.setRanTime).setOnClickListener {
            val ranTimeText = findViewById<TextView>(R.id.ranTimeText)
            val sec = setRandomTime()
            ranTimeText.setText("$sec : 0")
            val startBtn = findViewById<Button>(R.id.stopTime)
            startBtn.visibility = View.VISIBLE
        }

        val stopTime = findViewById<Button>(R.id.stopTime).setOnClickListener {
            if (stopFlags){
                val stopTimeText = findViewById<TextView>(R.id.stopTimeText)
                milliTimeLoop(stopTimeText)
                stopFlags = false
            }else {
                var rndText = findViewById<TextView>(R.id.ranTimeText)
                var stopTimeText = findViewById<TextView>(R.id.stopTimeText)

                if(rndText.text != stopTimeText.text) winFlags = false

                resultPage()
            }
        }

    }

    private fun goMainPage() {
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)
        mainFun()
    }

    // 랜덤으로 시간을 세팅해주는 함수
    private fun setRandomTime() : String{
        var range = 1..20
        var ranSec = range.random().toString().padStart(2,'0')
        return ranSec
    }

    companion object {
        var millis = 0;
        var sec = 0;
    }

    // 재귀호출로 시간을 밀리초단위로 갱신시켜줄 함수.
    private fun milliTimeLoop(timeText : TextView) {
        Handler().postDelayed(Runnable {
            if(!stopFlags) {
                millis++
            }
            if (millis==10) {
                sec ++
                millis = 0
            }
            timeText.setText("${sec.toString().padStart(2,'0')} : ${millis.toString()}")
            if(!stopFlags) {
                milliTimeLoop(timeText)
            } else {
                millis = 0;
                sec = 0;
            }
        }, 100)
    }


    //////////////////////// 게임결과 화면과 함수 시작부분
    private fun resultPage() {
        setContentView(R.layout.result)
        var resultImage = findViewById<ImageView>(R.id.resultImage)
        var resultText = findViewById<TextView>(R.id.resultText)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.result)
        if(winFlags) {
            resultImage.setImageResource(R.drawable.feelgood)
            resultText.setText("성공!!")
        } else {
            resultImage.setImageResource(R.drawable.feelsad)
            resultText.setText("실패!!")
        }
        resultFun()
    }

    private fun resultFun() {
        var backMain = findViewById<Button>(R.id.backMain).setOnClickListener {
            goMainPage()
        }
        var backGame = findViewById<Button>(R.id.backGame).setOnClickListener {
            goStopGameFunPage()
        }
    }


}