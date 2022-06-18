package com.nsa.comuty.extra

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.nsa.comuty.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*

class Image {

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }

        fun loadCircleImage(context: Context, imageLink: String, profileImageview: CircleImageView) {
//            CoroutineScope(Dispatchers.IO+ exceptionHandler).launch{
//                withContext(Dispatchers.Main){
            Glide.with(context)
                .load(imageLink)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.ic_baseline_person_24)
                .into(profileImageview)
           // }}
        }
    fun loadImage(context: Context, imageLink: String, imageView: ImageView) {
//            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
//                withContext(Dispatchers.Main) {

                    Glide.with(context)
                        .load(imageLink)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_baseline_person_24)
                        .into(imageView)

//                }
//            }

        }



}