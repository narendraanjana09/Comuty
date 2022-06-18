package com.nsa.comuty.home.ui.posts.interfaces

interface OnImagesUploadedListener {
    fun onUploaded( imagesLinkList :List<String>)
    fun gotError()
}