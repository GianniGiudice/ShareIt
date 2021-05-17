package com.example.shareit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth;
    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener;
    lateinit var loginButton: Button;
    lateinit var emailInput: EditText;
    lateinit var passwordInput: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuthStateListener = (FirebaseAuth.AuthStateListener {
            fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser;
                if (user != null) {
                    val intent: Intent = Intent(application, MainActivity::class.java);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        })

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.login);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);

        loginButton.setOnClickListener(View.OnClickListener {
            fun onClick(view: View) {
                val email: String = emailInput.text.toString();
                val password: String = passwordInput.text.toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                    fun onComplete(task: Task<AuthResult>) {
                        if (!task.isSuccessful) {
                            Toast.makeText(baseContext, "Identifiants incorrects.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
            }
        })
    }

    override fun onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    override fun onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}