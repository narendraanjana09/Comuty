package com.nsa.comuty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.nsa.comuty.databinding.ActivityMainBinding
import com.nsa.comuty.extra.Spinner_Fragment
import com.nsa.comuty.home.HomeActivity
import com.nsa.comuty.onboarding.extra.Keys
import com.nsa.comuty.onboarding.extra.SavedText
import com.nsa.comuty.onboarding.ui.OnBoardingActivity
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.plugins.RxJavaPlugins


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

            if(SavedText(this).getText(Keys.GO_TO).toString().equals(Keys.HOME)
                && FirebaseAuth.getInstance().currentUser!=null){
                val intent=Intent(this@MainActivity,HomeActivity::class.java)
                startActivity(intent)
            }else{
            val intent=Intent(this@MainActivity,OnBoardingActivity::class.java)
            startActivity(intent)
            }
            finish()
        },3000)

//        RxJavaPlugins.setErrorHandler { e ->
//            if (e is UndeliverableException) {
//                // Merely log undeliverable exceptions
//                e.message?.let { Log.e("err", it) }
//            } else {
//                // Forward all others to current thread's uncaught exception handler
//                Thread.currentThread().also { thread ->
//                    thread.uncaughtExceptionHandler.uncaughtException(thread, e)
//                }
//            }
//        }
    }
}