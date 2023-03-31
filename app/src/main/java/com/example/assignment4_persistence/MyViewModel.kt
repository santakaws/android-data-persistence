// Name: Brennon Hahs
// CWID: 887487148
// Email: brennonhahs@csu.fullerton.edu

package com.example.assignment4_persistence

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {
    private val prefs = MyPreferencesRepository.get()

    fun saveInput(s: String, index: Int) {
        viewModelScope.launch {
            prefs.saveInput(s,index)
        }
    }

    fun saveBooleanInput(b: Boolean, index: Int) {
        viewModelScope.launch {
            prefs.saveBooleanInput(b, index)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadInputs(act: MainActivity) {
        viewModelScope.launch {
            prefs.redVal.collectLatest {
                act.redNum.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.greenVal.collectLatest {
                act.greenNum.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.blueVal.collectLatest {
                act.blueNum.setText(it)
            }
        }
        viewModelScope.launch {
            prefs.redBut.collectLatest {
                act.redSw.isChecked = it
                act.switchEnableOrDisable(act.redSw, act.redB, act.redNum)
                act.setViewColor()
                Log.i("MyViewModel", "Red Switch: $it")
            }
        }
        viewModelScope.launch {
            prefs.greenBut.collectLatest {
                act.greenSw.isChecked = it
                act.switchEnableOrDisable(act.greenSw, act.greenB, act.greenNum)
                act.setViewColor()
                Log.i("MyViewModel", "Green Switch: $it")
            }
        }
        viewModelScope.launch {
            prefs.blueBut.collectLatest {
                act.blueSw.isChecked = it
                act.switchEnableOrDisable(act.blueSw, act.blueB, act.blueNum)
                act.setViewColor()
                Log.i("MyViewModel", "Blue Switch: $it")
            }
        }
    }
}