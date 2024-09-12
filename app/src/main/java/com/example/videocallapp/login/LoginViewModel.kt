package com.example.videocallapp.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel @Inject constructor()  : ViewModel() {
  private  val loginState = MutableStateFlow<LoginState>(LoginState.Normal)
          val loginStateFlow = loginState.asStateFlow()
    fun login(username: String, password: String) {
         val auth = Firebase.auth
        loginState.value = LoginState.Loading
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    if (user != null) {
                        loginState.value = LoginState.Success
                    }
                    else{
                        loginState.value = LoginState.Error
                    }
                } else {
                    // Login failed
                    loginState.value = LoginState.Error
                }
            }
    }
}
    sealed class LoginState{
        object Normal : LoginState()
       object Loading : LoginState()
        object Success : LoginState()
        object Error : LoginState()


    }