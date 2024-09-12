package com.example.videocallapp.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.videocallapp.R
import com.example.videocallapp.myFont
import com.example.videocallapp.ui.theme.col1
import com.example.videocallapp.ui.theme.col3

@Composable
fun LoginScreen(navController: NavController){
    val viewModel:LoginViewModel = hiltViewModel()
 val userName = remember { mutableStateOf("") }
    val passWord = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }


Surface(modifier = Modifier.fillMaxSize()){
    Image(painter = painterResource(id = R.drawable.back4_2_50), contentDescription = null,
        modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
      Row(modifier = Modifier.padding(8.dp)) {
          Image(painter = painterResource(id = R.drawable.zoom), contentDescription = null, modifier = Modifier.size(40.dp))
          Image(painter = painterResource(id = R.drawable.textlogo), contentDescription = null, modifier = Modifier.size(90.dp).offset(y = -27.dp))
      }
    Column(
        modifier = Modifier
            .fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(text = "LogIn", color = Color.White, fontSize = 30.sp, fontFamily = FontFamily.SansSerif)
        OutlinedTextField(
            value = userName.value,
            onValueChange = { userName.value = it },
            label = { Text(text = "Username", color = Color.White) },
        )

        OutlinedTextField(value = passWord.value,
            onValueChange = { passWord.value = it },
            label = { Text(text = "Password", color = Color.White) })

        Spacer(modifier = Modifier.size(16.dp))

        if (loading.value) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                viewModel.login(userName.value, passWord.value)
            },
             shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = col1),
                modifier = Modifier
                    .width(180.dp)
                    .height(40.dp)


                    ) {
                Text(text = "Login")
            }
            Spacer(modifier = Modifier.size(8.dp))
            TextButton(onClick = { navController.navigate("signup") }) {
                Text(text = "Dont have an Account? SignUp", color = Color.White)
            }
        }
    }
}
    val state = viewModel.loginStateFlow.collectAsState()
  LaunchedEffect(state.value) {
      when(state.value){
          is LoginState.Normal -> {
            loading.value = false
          }
          is LoginState.Loading -> {
          loading.value = true
          }
          is LoginState.Success -> {
              navController.navigate("home"){
                  popUpTo("login"){
                      inclusive = true
                  }
              }
          }
          is LoginState.Error -> {
             loading.value = false
          }
      }
  }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}