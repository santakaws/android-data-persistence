// Name: Brennon Hahs
// CWID: 887487148
// Email: brennonhahs@csu.fullerton.edu

package com.example.assignment4_persistence

import android.graphics.Color.rgb
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    lateinit var redSw: Switch
    lateinit var greenSw: Switch
    lateinit var blueSw: Switch
    lateinit var redB: SeekBar
    lateinit var greenB: SeekBar
    lateinit var blueB: SeekBar
    private lateinit var colorVu: View
    private lateinit var reset: Button
    lateinit var redNum: EditText
    lateinit var greenNum: EditText
    lateinit var blueNum: EditText

    private val myViewModel: MyViewModel by lazy {
        MyPreferencesRepository.initialize(this)
        MyViewModel()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "Created")
        setContentView(R.layout.activity_main)
        this.connectViewPointers()
        this.myViewModel.loadInputs(this)
        this.switchCallbacks()
        this.sliderCallbacks()
        this.buttonCallbacks()
        this.textCallbacks()
        this.setViewColor()
    }

    override fun onPause() {
        super.onPause()
        Log.i("MainActivity", "Paused")
    }

    override fun onDestroy() {
        this.myViewModel.saveInput(this.redNum.text.toString(), 1)
        this.myViewModel.saveInput(this.greenNum.text.toString(), 2)
        this.myViewModel.saveInput(this.blueNum.text.toString(), 3)
        this.myViewModel.saveBooleanInput(this.redSw.isChecked, 4)
        this.myViewModel.saveBooleanInput(this.greenSw.isChecked, 5)
        this.myViewModel.saveBooleanInput(this.blueSw.isChecked, 6)
        super.onDestroy()
        Log.i("MainActivity", "Destroyed")
    }

    override fun onResume() {
        super.onResume()
        Log.i("MainActivity", "Resumed")
    }

    override fun onStart() {
        super.onStart()
        Log.i("MainActivity", "Started")
    }

    override fun onStop() {
        super.onStop()
        Log.i("MainActivity", "Stopped")
    }

    private fun connectViewPointers() {
        this.redSw = this.findViewById(R.id.redSw)
        this.greenSw = this.findViewById(R.id.greenSw)
        this.blueSw = this.findViewById(R.id.blueSw)
        this.redB = this.findViewById(R.id.redBar)
        this.greenB = this.findViewById(R.id.greenBar)
        this.blueB = this.findViewById(R.id.blueBar)
        this.colorVu = this.findViewById(R.id.colorView)
        this.reset = this.findViewById(R.id.reset)
        this.redNum = this.findViewById(R.id.redNum)
        this.greenNum = this.findViewById(R.id.greenNum)
        this.blueNum = this.findViewById(R.id.blueNum)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun switchCallbacks() {
        this.redSw.setOnCheckedChangeListener { compoundButton, b ->
            this.switchEnableOrDisable(this.redSw, this.redB, this.redNum)
            this.setViewColor()
        }
        this.greenSw.setOnCheckedChangeListener { compoundButton, b ->
            this.switchEnableOrDisable(this.greenSw, this.greenB, this.greenNum)
            this.setViewColor()
        }
        this.blueSw.setOnCheckedChangeListener { compoundButton, b ->
            this.switchEnableOrDisable(this.blueSw, this.blueB, this.blueNum)
            this.setViewColor()
        }
    }

    private fun sliderCallbacks() {
        this.redB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setViewColor()
                changeTxt(redNum, redB)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        this.greenB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setViewColor()
                changeTxt(greenNum, greenB)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        this.blueB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                setViewColor()
                changeTxt(blueNum, blueB)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun buttonCallbacks() {
        this.reset.setOnClickListener {
            this.redB.progress = 100
            this.greenB.progress = 100
            this.blueB.progress = 100
            this.redSw.isChecked = true
            this.greenSw.isChecked = true
            this.blueSw.isChecked = true
            this.redB.isEnabled = true
            this.greenB.isEnabled = true
            this.blueB.isEnabled = true
            this.redNum.isEnabled = true
            this.greenNum.isEnabled = true
            this.blueNum.isEnabled = true
            setViewColor()
        }
    }

    private fun textCallbacks() {
        this.redNum.doOnTextChanged { text, start, before, count ->
            changeNumField(this.redNum, this.redB)
        }
        this.greenNum.doOnTextChanged { text, start, before, count ->
            changeNumField(this.greenNum, this.greenB)
        }
        this.blueNum.doOnTextChanged { text, start, before, count ->
            changeNumField(this.blueNum, this.blueB)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun switchEnableOrDisable(sw: Switch, bar: SeekBar, tx: EditText) {
        if (sw.isChecked) {
            bar.isEnabled = true
            tx.isEnabled = true
        }
        else {
            bar.isEnabled = false
            tx.isEnabled = false
        }
    }

    fun setViewColor() {
        var r: Float = 255 * (redB.progress.toFloat()/100)
        var g: Float = 255 * (greenB.progress.toFloat()/100)
        var b: Float = 255 * (blueB.progress.toFloat()/100)
        if (!redB.isEnabled) {
            r = 0F
        }
        if (!greenB.isEnabled) {
            g = 0F
        }
        if (!blueB.isEnabled) {
            b = 0F
        }
        var color: Int = rgb(r.toInt(),g.toInt(),b.toInt())
        this.colorVu.setBackgroundColor(color)
    }

    private fun changeTxt(tx: EditText, bar: SeekBar) {
        var num: Float = bar.progress.toFloat()/100
        if (num == 0.0F) {
            tx.setText("0.")
        }
        else {
            tx.setText(num.toString())
        }
        tx.setSelection(tx.length())
    }

    private fun changeNumField(num: EditText, bar: SeekBar) {
        if (num.text.toString() == ".") {
            num.setText("0.")
        }

        if (num.text.toString() != "") {
            if (num.text.toString().toFloat() > 1) {
                num.setText("1.0")
            }
            var newVal: Float = num.text.toString().toFloat()*100
            bar.progress = newVal.toInt()
        }
        num.setSelection(num.length())
    }
}
