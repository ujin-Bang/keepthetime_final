package com.example.keepthetime_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.keepthetime_final.databinding.ActivityManagePlacesBinding

class ManagePlacesActivity : BaseActivity() {

    lateinit var binding: ActivityManagePlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = DataBindingUtil.setContentView(this,R.layout.activity_manage_places)
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}