package com.example.shareit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.shareit.Constants.TAG
import com.example.shareit.R
import com.example.shareit.RecyclerActivity
import com.example.shareit.UploadActivity
import com.example.shareit.databinding.HomeActivityBinding
import com.example.shareit.utils.activityViewBinding
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.squareup.picasso.Picasso


class HomeActivity : AppCompatActivity() {
    private val binding by activityViewBinding(HomeActivityBinding::inflate)
    lateinit var callbackManager: CallbackManager
    lateinit var auth: FirebaseAuth
    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener
    lateinit var accessTokenTraker: AccessTokenTracker
    lateinit var topMenu: Menu

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
                    menuInflater.inflate(R.menu.nav_menu, topMenu)
                    updateUI(user)
                }
                else {
                    invalidateOptionsMenu()
                    updateUI(null)
                }
            }
        }

        accessTokenTraker = object: AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAT: AccessToken?, currentAT: AccessToken?) {
                if (currentAT == null) {
                    updateUI(null)
                    invalidateOptionsMenu()
                    auth.signOut()
                }
            }
        }

        binding.send.setOnClickListener{
            val intent = Intent(this, SnapActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            topMenu = menu
        }
        if (auth.currentUser != null) {
            menuInflater.inflate(R.menu.nav_menu, menu)
        }
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
             R.id.activity1 -> {
                 val intent = Intent(this, HomeActivity::class.java)
                 this.startActivity(intent)
                 true;
             }
            R.id.activity2 -> {
                val intent = Intent(this, UploadActivity::class.java)
                this.startActivity(intent)
                true;
            }
            R.id.activity3 -> {
                val intent = Intent(this, RecyclerActivity::class.java)
                this.startActivity(intent)
                true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun handleFacebookToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token);
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                menuInflater.inflate(R.menu.nav_menu, topMenu)
                updateUI(user)
            }
            else {
                Log.d("Tag", "L'authentification a échoué.");
                Toast.makeText(baseContext, "L'authentification a échoué.", Toast.LENGTH_SHORT).show()
                invalidateOptionsMenu()
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
            invalidateOptionsMenu()
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