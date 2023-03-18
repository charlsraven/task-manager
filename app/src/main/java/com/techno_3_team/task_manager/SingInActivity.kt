package com.techno_3_team.task_manager

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task


const val SCOPE_TASKS = "https://www.googleapis.com/auth/tasks"

// RC_SIGN in is the request code you will assign for starting the new activity. this can be any number.
const val RC_SIGN_IN = 123
lateinit var mGoogleSignInClient: GoogleSignInClient

class SingInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        // added minimal scopes auth.me and tasks

        //val serverClientId = getString(R.string.server_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(Scope(SCOPE_TASKS), Scope(Scopes.PLUS_ME))
//            .requestServerAuthCode(serverClientId)
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

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

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account,
        // if the user is already signed in the GoogleSignInAccount will be non-null
        // otherwise it'll return null
        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    // Update UI accordingly — that is, hide the sign-in button, launch your main activity,
    // or whatever is appropriate for your app.
    private fun updateUI(account: GoogleSignInAccount?) {
        TODO("Not yet implemented")
    }

    // После входа пользователя получаем GoogleSignInAccount для пользователя
    // в методе onActivityResult действия.
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    /*
    Объект GoogleSignInAccount содержит информацию о вошедшем в систему пользователе,
    например, имя пользователя:
     */
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    /*
    больше инфы про сам аккаунт
    https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInAccount
     */
    private fun getAccInfo() {
        val acct = GoogleSignIn.getLastSignedInAccount(getActivity())
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
        }
    }

    private fun getActivity(): Context {
        // TODO("Not yet implemented")
        return this
    }

}