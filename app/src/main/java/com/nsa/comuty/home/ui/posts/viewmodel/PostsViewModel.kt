package com.nsa.comuty.home.ui.posts.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.nsa.comuty.extra.Database
import com.nsa.comuty.extra.Storage
import com.nsa.comuty.home.ui.posts.interfaces.OnGetPostIdListener
import com.nsa.comuty.home.ui.posts.interfaces.OnImagesUploadedListener
import com.nsa.comuty.home.ui.posts.models.PostModel
import com.nsa.comuty.onboarding.models.UserModel
import kotlinx.coroutines.*
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

class PostsViewModel : ViewModel() {
    var context: Context? =null
    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val totalsize = MutableLiveData<String>()
    var firebaseUser:FirebaseUser? = null
    private fun clearValues() {
        successMessage.value=""
        errorMessage.value=""
        totalsize.value=""
    }

    init {
        firebaseUser=FirebaseAuth.getInstance().currentUser
    }
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        CoroutineScope(Dispatchers.Main).launch{
        errorMessage.value = message
        loading.value = false
        }
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun uploadPost(
        postModel: PostModel,
        imagesList: List<Uri>,
        userModel: UserModel
    ) {

        loading.value=true
        calculateSize(imagesList)
        getPostID(object : OnGetPostIdListener{
            override fun gotID(@NonNull postId: String) {
                postModel.postId=postId
                if(imagesList.isEmpty()){
                    uploadPostModel(postModel, userModel)
                }else {
                    var imagesLinkList = arrayListOf<String>()
                    uploadImageToFirebase(object : OnImagesUploadedListener {
                        override fun onUploaded(imagesLinkList: List<String>) {
                            postModel.imagesList = imagesLinkList
                            uploadPostModel(postModel, userModel)
                        }

                        override fun gotError() {
                            onError("ImageUpload Error")
                        }

                    }, imagesLinkList, userModel, postModel, imagesList.toMutableList())
                }
            }

            override fun gotError() {
               onError("PostId Error")
            }

        },userModel)



    }

    private fun calculateSize(imagesList: List<Uri>){
        CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            var totalSize:Double=0.0
            for(uri in imagesList){

                totalSize+=getImageSizeMB(context!!,uri)
            }
            withContext(Dispatchers.Main){
                val decimalFormatter = DecimalFormat("#.###")
                decimalFormatter.roundingMode = RoundingMode.UP
                totalsize.value=decimalFormatter.format(totalSize)
        }
        }
    }
    fun getImageSizeMB(context: Context, uri: Uri): Double {
        val scheme = uri.scheme
        var dataSize = 0.0
        if (scheme == ContentResolver.SCHEME_CONTENT) {
            try {
                val fileInputStream = context.contentResolver.openInputStream(uri)
                if (fileInputStream != null) {
                    dataSize = fileInputStream.available().toDouble()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (scheme == ContentResolver.SCHEME_FILE) {
            val path = uri.path
            var file: File? = null
            try {
                file = File(path)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (file != null) {
                dataSize = file.length().toDouble()
            }
        }
        return dataSize / (1024 * 1024)
    }


    private fun uploadPostModel(postModel: PostModel, userModel: UserModel) {

            Database().referenceColleges.child(userModel.college).child("posts")
                .child(postModel.postId)
                .setValue(postModel)
                .addOnSuccessListener {
                    Database().referenceColleges
                        .child(userModel.college)
                        .child("users")
                        .child(firebaseUser!!.uid)
                        .child("posts")
                        .push().setValue(postModel.postId)
                    loading.value=false
                    successMessage.value="Post Uploaded"
                    clearValues()



                }.addOnFailureListener{
                    onError("upload post error")
                    Log.e(TAG, "uploadPostModel: ${it.message}" )
                }
    }



    val TAG="POSTVIEW"

    private fun uploadImageToFirebase(onImagesUploadedListener: OnImagesUploadedListener,
                                      imagesLinKList: MutableList<String>,
                                      userModel: UserModel,
                                      postModel: PostModel,
                                      imageUriList:MutableList<Uri>) {
        if (imageUriList.size!=0) {
            val fileName = System.currentTimeMillis().toString()+".jpg"
            val ref = Storage().collegeRef.child(userModel.college)
                .child("users/${firebaseUser!!.uid}/posts/${postModel.postId}/$fileName")
            val fileUri= imageUriList[0]
            imageUriList.removeAt(0)
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                ref.putFile(fileUri)
                    .addOnSuccessListener(
                        OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                val imageUrl = it.toString()
                                imagesLinKList.add(imageUrl)
                                uploadImageToFirebase(onImagesUploadedListener,imagesLinKList,userModel,postModel,imageUriList)
                            }
                        })

                    .addOnFailureListener(OnFailureListener { e ->
                        print(e.message)
                        onImagesUploadedListener.gotError()
                    })
            }
        }else{
            onImagesUploadedListener.onUploaded(imagesLinKList)
        }
    }
    fun getPostID(onGetPostIdListener: OnGetPostIdListener,userModel: UserModel){

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
        val referensecount =
            Database().referenceColleges.child(userModel.college).child("countposts")
        referensecount.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val n = snapshot.value as Long
                    val value = n - 1
                    val item = Int.MAX_VALUE - value
                    Log.e("TAG", "onDataChange: $item")
                    onGetPostIdListener.gotID(value.toString())
                    referensecount.setValue(value)
                } else {
                    onGetPostIdListener.gotID(Int.MAX_VALUE.toString() )
                    referensecount.setValue(Int.MAX_VALUE)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onGetPostIdListener.gotError()
            }
        })
        }
    }
}