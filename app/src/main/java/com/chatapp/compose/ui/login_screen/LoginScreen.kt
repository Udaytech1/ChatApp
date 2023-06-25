package com.chatapp.compose.ui.login_screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chatapp.compose.ApplicationRoutes
import com.chatapp.compose.R
import com.chatapp.compose.data.firebase_connection.FirebaseHelper
import com.chatapp.compose.data.repogistory.LoginRepogistory
import com.chatapp.compose.ui.theme.AppLightColor
import com.chatapp.compose.ui.theme.ChatAppTheme
import com.chatapp.compose.utils.Commons
import com.chatapp.compose.utils.DatastoreHelper
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController? = null, loginViewModel: LoginViewModel) {

    val phoneNumber = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val datastoreHelper = DatastoreHelper(context)

    val coroutineScope = rememberCoroutineScope()

    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (result.data != null) {
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(intent)
                    Log.d(TAG, "LoginScreen: $task")
                    Commons.showToast(
                        context = context,
                        "Login success ${task.result.email.toString()}"
                    )
                    coroutineScope.launch(Dispatchers.IO) {
                        datastoreHelper.updateLogin(true)
                        datastoreHelper.updateUserEmail(task.result.email.toString())
                        coroutineScope.launch(Dispatchers.Main) {
                            navController?.navigate(ApplicationRoutes.HOME_SCREEN)
                        }
                    }
                }
            }

        }

    Scaffold(
        topBar = {
            /*CommanAppBar(title = stringResource(R.string.login), onStartIconClicked = {
                navController?.popBackStack()
            })*/
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = AppLightColor)
                    .padding(paddingValues = paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                        Image(
                            painter = painterResource(id = R.drawable.login_img),
                            contentDescription = "",
                            modifier = Modifier.size(250.dp),
                            contentScale = ContentScale.Crop,
                        )
                    }
                    TextField(
                        value = phoneNumber.value, onValueChange = {
                            if (it.length <= 10) {
                                phoneNumber.value = it
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        placeholder = {
                            Text(text = context.getString(R.string.enter_phone))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Button(
                        onClick = {
                            if (phoneNumber.value.isEmpty()) {
                                Commons.showToast(context, context.getString(R.string.enter_phone))
                            } else if (phoneNumber.value.length < 10) {
                                Commons.showToast(
                                    context,
                                    context.getString(R.string.phone_should_be_10_digit)
                                )
                            } else {
                                loginViewModel.login(mobile = phoneNumber.value)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.login),
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            val gso =
                                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                    .requestEmail()
                                    .requestIdToken("1066800980194-jd3tr2ab423173s6oqf5kfa7mrb98q2p.apps.googleusercontent.com")
                                    .requestId()
                                    .requestProfile()
                                    .build()
                            val g = GoogleSignIn.getClient(context, gso)
                            startForResult.launch(g.signInIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.login_with_google),
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )
                    }
                    OutlinedButton(
                        onClick = {

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.login_with_facebook),
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )
                    }
                }
            }
        },
    )
    navController?.let { GetLoginReponse(loginViewModel, it) }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GetLoginReponse(loginViewModel: LoginViewModel, navController: NavController) {
    val datastoreHelper = DatastoreHelper(LocalContext.current)
    val response = loginViewModel.loginResponse.collectAsState()
    if (response.value.success) {
        LaunchedEffect(key1 = true) {
            datastoreHelper.updateUserPhone(response.value.mobile)
            datastoreHelper.updateLogin(true)
            navController.navigate(ApplicationRoutes.HOME_SCREEN) {
                popUpTo(ApplicationRoutes.LOGIN) {
                    inclusive = true
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, device = Devices.NEXUS_5)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview() {
    ChatAppTheme {
        LoginScreen(
            null, LoginViewModel(
                LoginRepogistory(
                    FirebaseHelper.getDatabaseReference(
                        LocalContext.current
                    )
                )
            )
        )
    }
}