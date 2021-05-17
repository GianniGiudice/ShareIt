package com.example.shareit;

import android.content.Intent
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity: AppCompatActivity() {
    // Eléments statics
    companion object {
        val started: Boolean = false;
    }

    var mAuth: FirebaseAuth? = null
        get() {
            return field;
        }
        set(value) {
            field = value;
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        /*
            Si l'utilisateur est pas connecté, il va sur l'écran d'accueil utilisateur,
            sinon, il va sur la page Home où il peut choisir entre s'inscrire ou se connecter
         */
        if (mAuth!!.currentUser != null) {
            val intent: Intent = Intent(application, MainActivity::class.java);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
        else {
            val intent: Intent = Intent(application, HomeActivity::class.java);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return;
        }
    }

}
