package com.example.videocallapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel @Inject constructor()  : ViewModel() {
    private  val loginState = MutableStateFlow<SignUpState>(SignUpState.Normal)
    val signUpState = loginState.asStateFlow()
    fun signUp(username: String, password: String) {
        val auth = Firebase.auth
        loginState.value = SignUpState.Loading
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    if (user != null) {
                        loginState.value = SignUpState.Success
                    }
                    else{
                        loginState.value = SignUpState.Error
                    }
                } else {
                    // Login failed
                    loginState.value = SignUpState.Error
                }
            }
    }
}
sealed class SignUpState{
    object Normal : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()


}