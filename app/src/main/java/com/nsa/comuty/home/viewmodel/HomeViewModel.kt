package com.nsa.comuty.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.nsa.comuty.extra.Database
import com.nsa.comuty.onboarding.extra.Keys
import com.nsa.comuty.onboarding.extra.SavedText
import com.nsa.comuty.onboarding.models.UserModel
import kotlinx.coroutines.*

class HomeViewModel(
): ViewModel() {
    var context: Context? =null
    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val successMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val updates=MutableLiveData<Boolean>()
    val userModelLiveData = MutableLiveData<UserModel>()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    fun getUserModel() {

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
           val userRef= Database().referenceColleges.child(SavedText(context).getText(Keys.COLLEGE))
            .child("users")
            .child(FirebaseAuth.getInstance().uid.toString())

            val postListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                            val userModel = dataSnapshot.getValue(UserModel::class.java)
                        userModelLiveData.postValue(userModel!!)

                    }else{
                        onError("Error : No data Found ")
                    }
                    loading.value=false
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    onError("Error : ${databaseError.message} ")
                }
            }
            userRef.addListenerForSingleValueEvent(postListener)
        }

    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun addUpdateListener(mQuery: Query) {
        job= CoroutineScope(Dispatchers.IO+exceptionHandler).launch {
            mQuery.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    updates.value=true
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("TAG", "onCancelled: $error")
                }
            })
        }
    }
}