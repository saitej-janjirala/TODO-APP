package com.saitejajanjirala.todo_app.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.saitejajanjirala.todo_app.R
import com.saitejajanjirala.todo_app.activities.main.MainActivity
import com.saitejajanjirala.todo_app.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var mBinding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_splash)
        Handler().postDelayed({
            mBinding.splashAnimationLayout.loop(false)
        },2500)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },3000)
    }
}