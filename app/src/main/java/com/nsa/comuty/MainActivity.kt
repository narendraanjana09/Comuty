package com.nsa.comuty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.nsa.comuty.databinding.ActivityMainBinding
import com.nsa.comuty.onboarding.ui.OnBoardingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val anim=AnimationUtils.loadAnimation(this,R.anim.splash)


        Handler().postDelayed({
            binding.textView.visibility= View.VISIBLE
            binding.textView.startAnimation(anim)

        },1000)
        Handler().postDelayed({
            binding.gifImageView.isVisible=false
        },2000)
        Handler().postDelayed({
            val intent=Intent(this@MainActivity,OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        },3000)

    }
}