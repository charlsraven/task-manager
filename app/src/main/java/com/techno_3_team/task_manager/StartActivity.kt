package com.techno_3_team.task_manager

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.techno_3_team.task_manager.databinding.ActivityLoginBinding


class StartActivity : AppCompatActivity() {

// RC_SIGN in is the request code you will assign for starting the new activity. this can be any number.
//const val RC_SIGN_IN = 123

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var oneTapClient: SignInClient

    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var auth: FirebaseAuth
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e("tag", "launcher")
            Log.e("launcher", result.resultCode.toString())
//            if (result.resultCode == Activity.RESULT_OK) {
            Log.e("launcher", "ok")
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResultes(task)
//            } else
//                Log.e("launcher", "NOT OK")
        }

    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        val buttonWithoutAuth = loginBinding.continueWithoutAutorization
        val buttonGoogleAuth = loginBinding.continueWithGoogle

        buttonWithoutAuth.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

//        auth = Firebase.auth
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .requestScopes(Scope(SCOPE_TASKS), Scope(Scopes.PLUS_ME))
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
        lateinit var credential : SignInCredential
        val activityResultLauncher: ActivityResultLauncher<IntentSenderRequest> =
            registerForActivityResult(
                ActivityResultContracts.StartIntentSenderForResult(),
                ActivityResultCallback { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        try {
                            credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                            val idToken = credential.googleIdToken
                            val username = credential.id
                            val password = credential.password
                            when {
                                idToken != null -> {
                                    // Got an ID token from Google. Use it to authenticate
                                    // with your backend.
                                    Log.e(TAG, "Got ID token.")
                                    val email = credential.id
                                }
                                password != null -> {
                                    // Got a saved username and password. Use them to authenticate
                                    // with your backend.
                                    Log.e(TAG, "Got password.")
                                }
                                else -> {
                                    // Shouldn't happen.
                                    Log.e(TAG, "No ID token or password!")
                                }
                            }
                        } catch (e: ApiException) {
                            Log.e("intent", "error")
                        }
                    }
                })
        buttonGoogleAuth.setOnClickListener {
            Log.e("tag", "clicked on google authorization")
            oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
//                        val intentSenderRequest =
//                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                        activityResultLauncher.launch(intentSenderRequest)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("name", credential.id)
                        startActivity(intent)

                    } catch (e: SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.localizedMessage)
                    }
                }
                .addOnFailureListener(this) {
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.e("google", "failed")
                }
        }
//        oneTapClient = Identity.getSignInClient(this)

//


        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        // added minimal scopes auth.me and tasks


        // Build a GoogleSignInClient with the options specified by gso.

        // получаем код авторизации
//        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            val authCode = account.serverAuthCode
//            // Show signed-un UI
//            updateUI(account)
//            // TODO(developer): send code to server and exchange for access/refresh/ID tokens
//        } catch (e: ApiException) {
//            Log.w(TAG, "Sign-in failed", e)
//            updateUI(null)
//        }


        // отправляем этот код авторизации в серверную часть приложения с помощью HTTPS POST:
        //внимание: эта штука использует firebase! я с ним не разбиралась, но отвечаю, это страшная вещь
//        val httpPost = HttpPost("https://yourbackend.example.com/authcode")
//        try {
//            val nameValuePairs: MutableList<NameValuePair> = ArrayList<NameValuePair>(1)
//            nameValuePairs.add(BasicNameValuePair("authCode", authCode))
//            httpPost.entity = UrlEncodedFormEntity(nameValuePairs)
//            val response = httpClient.execute(httpPost)
//            val statusCode = response.getStatusLine().getStatusCode()
//            val responseBody = EntityUtils.toString(response.getEntity())
//        } catch (e: ClientProtocolException) {
//            Log.e(TAG, "Error sending auth code to backend.", e)
//        } catch (e: IOException) {
//            Log.e(TAG, "Error sending auth code to backend.", e)
//        }
    }

    private fun signInGoogle() {
        Log.e("tag", "signInGoogle")
        val singInIntent = googleSignInClient.signInIntent
        launcher.launch(singInIntent)
    }

    private fun handleResultes(task: Task<GoogleSignInAccount>) {
        Log.e("tag", "handleResultes")
//        if (task.isSuccessful) {
        Log.e("handling", "successful")
        val account = task.result
        Log.e("handling", account?.displayName.toString())
        if (account != null) {
            updateUI(account)
        }
//        } else {
//            Log.e("handling", "UNsuccessful")
//            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
//        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val intent = Intent(this, MainActivity::class.java)
//                intent.putExtra("name", account.displayName)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
//            }
//        }
        Log.e("tag", "updateUI")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("name", account.displayName)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

//    override fun onStart() {
//        super.onStart()
//        var currentUser = auth.currentUser
//        updateUI(currentUser);

//        // Check for existing Google Sign In account,
//        // if the user is already signed in the GoogleSignInAccount will be non-null
//        // otherwise it'll return null
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
//    }

    // Update UI accordingly — that is, hide the sign-in button, launch your main activity,
    // or whatever is appropriate for your app.


    // После входа пользователя получаем GoogleSignInAccount для пользователя
    // в методе onActivityResult действия.
//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
//        val idToken = googleCredential.googleIdToken
//        when {
//            idToken != null -> {
//                // Got an ID token from Google. Use it to authenticate
//                // with Firebase.
//                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                auth.signInWithCredential(firebaseCredential)
//                    .addOnCompleteListener(this) { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success")
//                            val user = auth.currentUser
//                            updateUI(user)
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.exception)
//                            updateUI(null)
//                        }
//                    }
//            }
//            else -> {
//                // Shouldn't happen.
//                Log.d(TAG, "No ID token!")
//            }
//        }

//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task)
//        }
//    }

    /*
    Объект GoogleSignInAccount содержит информацию о вошедшем в систему пользователе,
    например, имя пользователя:
     */
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
//            // Signed in successfully, show authenticated UI.
//            updateUI(account)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
//        }
//    }

    /*
    больше инфы про сам аккаунт
    https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInAccount
     */
//    private fun getAccInfo() {
//        val acct = GoogleSignIn.getLastSignedInAccount(getActivity())
//        if (acct != null) {
//            val personName = acct.displayName
//            val personGivenName = acct.givenName
//            val personFamilyName = acct.familyName
//            val personEmail = acct.email
//            val personId = acct.id
//            val personPhoto: Uri? = acct.photoUrl
//        }
//    }

    //    private fun getActivity(): Context {
//        // TODO("Not yet implemented")
//        return this
//    }
    companion object {
        const val SCOPE_TASKS = "https://www.googleapis.com/auth/tasks"
    }
}
