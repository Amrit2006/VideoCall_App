package com.example.videocallapp.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.videocallapp.R
import com.example.videocallapp.ui.theme.col1

@Composable
fun SignUpScreen(navController: NavController) {

    val userName = remember { mutableStateOf("") }
    val passWord = remember { mutableStateOf("") }
    val confirmPassWord = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }

    val viewModel: SignUpViewModel = hiltViewModel()
   Surface(modifier = Modifier.fillMaxSize()) {
       Image(painter = painterResource(id = R.drawable.back4_2_50), contentDescription = null,
           modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
       Row(modifier = Modifier.padding(8.dp)) {
           Image(painter = painterResource(id = R.drawable.zoom), contentDescription = null, modifier = Modifier.size(40.dp))
           Image(painter = painterResource(id = R.drawable.textlogo), contentDescription = null, modifier = Modifier.size(90.dp).offset(y = -27.dp))
       }
       Column(
           modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Text(text = "SignUp", color = Color.White, fontSize = 30.sp)
           OutlinedTextField(value = userName.value,
               onValueChange = { userName.value = it },
               label = { Text(text = "Username", color = Color.White) })

           OutlinedTextField(value = passWord.value,
               onValueChange = { passWord.value = it },
               label = { Text(text = "Password", color = Color.White) })

           OutlinedTextField(value = confirmPassWord.value,
               onValueChange = { confirmPassWord.value = it },
               label = { Text(text = "Confirm Password", color = Color.White) })
           Spacer(modifier = Modifier.size(16.dp))
           if (loading.value) {
               CircularProgressIndicator()
           } else {
               Button(
                   onClick = {
                       viewModel.signUp(userName.value, passWord.value)
                   },colors = ButtonDefaults.buttonColors(containerColor = col1),
                   shape = RoundedCornerShape(6.dp),
                   modifier = Modifier.width(180.dp).height(40.dp),
                   enabled = userName.value.isNotEmpty() && passWord.value.isNotEmpty() && confirmPassWord.value.isNotEmpty() && passWord.value == confirmPassWord.value
               ){
                   Text(text = "SignUp", color = Color.White)
               }

           }
       }
   }
        val state = viewModel.signUpState.collectAsState()
        LaunchedEffect(state.value) {
            when(state.value) {
                is SignUpState.Normal -> {
                    loading.value = false
                }

                is SignUpState.Loading -> {
                    loading.value = true
                }

                is SignUpState.Success -> {
                    navController.navigate("home"){
                        popUpTo("login"){
                            inclusive = true
                        }
                    }
                }

                is SignUpState.Error -> {
                    loading.value = false
                }

                else -> {}
            }
        }
    }


@Composable
@Preview(showBackground = true)
fun SignUpScreenPreview() {
    SignUpScreen(rememberNavController())
}
