package com.example.shareit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.shareit.R
import com.example.shareit.databinding.HomeActivityBinding;
import com.example.shareit.utils.activityViewBinding
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.squareup.picasso.Picasso
import com.facebook.FacebookSdk;

class HomeActivity : AppCompatActivity() {
    private val binding by activityViewBinding(HomeActivityBinding::inflate)
    lateinit var callbackManager: CallbackManager
    lateinit var auth: FirebaseAuth
    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener
    lateinit var accessTokenTraker: AccessTokenTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        binding.loginButton.setReadPermissions("email", "public_profile")


        if (auth.currentUser != null) {
            binding.homeText.text = "Bienvenue " + auth.currentUser!!.displayName
            binding.homePar.text = "Envoyez directement des photos à vos amis ! :)"
        }

        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    handleFacebookToken(result.accessToken)
                }
            }

            override fun onCancel() {
                Log.d("CAN", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("ERR", "onError")
            }
        });

        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
            @Override
            fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user: FirebaseUser? = firebaseAuth.currentUser
                if (user != null) {
                    updateUI(user)
                }
                else {
                    updateUI(null)
                }
            }
        }

        accessTokenTraker = object: AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAT: AccessToken?, currentAT: AccessToken?) {
                if (currentAT == null) {
                    updateUI(null)
                    auth.signOut()
                }
            }
        }

        binding.send.setOnClickListener{
            val intent = Intent(this, SnapActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleFacebookToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token);
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                updateUI(user)
            }
            else {
                Log.d("Tag", "L'authentification a échoué.");
                Toast.makeText(baseContext, "L'authentification a échoué.", Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.homeText.text = "Bienvenue " + user.displayName
            binding.homePar.text = "Envoyez directement des photos à vos amis ! :)"
            if (user.photoUrl != null) {
                var photoUrl: String = user.photoUrl.toString();
                photoUrl = "$photoUrl?type=large"
                Picasso.get().load(photoUrl).into(binding.imageLogo)
                Picasso.get().load(R.drawable.send).into(binding.send)
            }
        }
        else {
            binding.homeText.text = "Bienvenue sur ShareIt"
            binding.homePar.text =  "ShareIt est une application de partage de photos. Pas besoin d'inscription, connectez-vous simplement avec votre compte Facebook pour commencer à utiliser notre application ! ;)"
            binding.imageLogo.setImageResource(R.drawable.shareitlogo)
            binding.send.setImageResource(0)
        }
    }

    override fun onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListener)
    }

    override fun onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthStateListener)
    }
}