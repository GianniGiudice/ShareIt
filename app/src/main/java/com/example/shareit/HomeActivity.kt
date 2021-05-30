package com.example.shareit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.squareup.picasso.Picasso
import com.facebook.FacebookSdk;

class HomeActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager;
    lateinit var auth: FirebaseAuth;
    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener;
    lateinit var accessTokenTraker: AccessTokenTracker;
    lateinit var loginBtn: LoginButton;
    lateinit var mText: TextView;
    lateinit var mPar: TextView;
    lateinit var mPicture: ImageView;
    lateinit var mSend: ImageView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(applicationContext);
        callbackManager = CallbackManager.Factory.create();
        loginBtn = findViewById(R.id.login_button);
        loginBtn.setReadPermissions("email", "public_profile");
        mText = findViewById(R.id.home_text);
        mPar = findViewById(R.id.home_par);
        mPicture = findViewById(R.id.image_logo);
        mSend = findViewById(R.id.send);

        if (auth.currentUser != null) {
            mText.text = "Bienvenue " + auth.currentUser!!.displayName;
            mPar.text = "Envoyez directement des photos à vos amis ! :)";
        }

        loginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    handleFacebookToken(result.accessToken)
                }
            }

            override fun onCancel() {
                Log.d("CAN", "onCancel");
            }

            override fun onError(error: FacebookException?) {
                Log.d("ERR", "onError");
            }
        });

        firebaseAuthStateListener = FirebaseAuth.AuthStateListener {
            @Override
            fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user: FirebaseUser? = firebaseAuth.currentUser;
                if (user != null) {
                    updateUI(user);
                }
                else {
                    updateUI(null);
                }
            }
        }

        accessTokenTraker = object: AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAT: AccessToken?, currentAT: AccessToken?) {
                if (currentAT == null) {
                    updateUI(null);
                    auth.signOut();
                }
            }
        }

        mSend.setOnClickListener{
            val intent = Intent(this, SnapActivity::class.java)
            startActivity(intent)
        }
    }

    fun handleFacebookToken(token: AccessToken) {
        val credential: AuthCredential = FacebookAuthProvider.getCredential(token.token);
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser;
                updateUI(user);
            }
            else {
                Log.d("Tag", "L'authentification a échoué.");
                Toast.makeText(baseContext, "L'authentification a échoué.", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            mText.text = "Bienvenue " + user.displayName;
            mPar.text = "Envoyez directement des photos à vos amis ! :)";
            if (user.photoUrl != null) {
                var photoUrl: String = user.photoUrl.toString();
                photoUrl = "$photoUrl?type=large";
                Picasso.get().load(photoUrl).into(mPicture);
                Picasso.get().load(R.drawable.send).into(mSend);
            }
        }
        else {
            mText.text = "Bienvenue sur ShareIt";
            mPar.text =  "ShareIt est une application de partage de photos. Pas besoin d'inscription, connectez-vous simplement avec votre compte Facebook pour commencer à utiliser notre application ! ;)";
            mPicture.setImageResource(R.drawable.shareitlogo);
            mSend.setImageResource(0);
        }
    }

    override fun onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthStateListener);
    }

    override fun onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthStateListener)
    }
}