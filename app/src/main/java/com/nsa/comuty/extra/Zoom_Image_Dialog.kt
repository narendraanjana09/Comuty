package com.nsa.comuty.extra

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import com.nsa.comuty.R
import com.nsa.comuty.databinding.ZoomImageLayoutBinding
import com.nsa.comuty.onboarding.models.ImageModel
import kotlinx.coroutines.CoroutineScope
import java.io.File
import java.lang.Exception



class Zoom_Image_Dialog(private val imageModel: ImageModel) : DialogFragment() {




    private var _binding : ZoomImageLayoutBinding?= null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }

        _binding = ZoomImageLayoutBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            context?.let {
                if(imageModel.isLink){
                    Glide.with(it)
                        .load(imageModel.path)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView)
                }else{
                    imageView.setImageURI(Uri.parse(imageModel.path))
                }


            }
            val displayMetrics = DisplayMetrics()
            (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            imageView.layoutParams.height=height
            imageView.layoutParams.width=width
            imageView.requestLayout()
        }

        binding.closeIMG.setOnClickListener {
            dialog!!.dismiss()
        }

    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}