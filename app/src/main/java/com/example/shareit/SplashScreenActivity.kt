package com.example.shareit;

import android.content.Intent
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

class SplashScreenActivity : AppCompatActivity() {
    // Eléments statics
    companion object {
        val started: Boolean = false;
    }

    lateinit var mAuth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        /*
            Si l'utilisateur est pas connecté, il va sur l'écran d'accueil utilisateur,
            sinon, il va sur la page Home où il peut choisir entre s'inscrire ou se connecter
         */

        val intent: Intent = Intent(application, HomeActivity::class.java);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }


}
