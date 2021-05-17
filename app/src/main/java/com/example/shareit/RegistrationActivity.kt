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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth;
    lateinit var firebaseAuthStateListener: FirebaseAuth.AuthStateListener;
    lateinit var regButton: Button;
    lateinit var nameInput: EditText;
    lateinit var emailInput: EditText;
    lateinit var passwordInput: EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        firebaseAuthStateListener = (FirebaseAuth.AuthStateListener {
            @Override
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
        regButton = findViewById(R.id.registration);
        nameInput = findViewById(R.id.name);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);

        regButton.setOnClickListener {
            val name: String = nameInput.text.toString();
            val email: String = emailInput.text.toString();
            val password: String = passwordInput.text.toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> {
                    fun onComplete(task: Task<AuthResult>) {
                        if (!task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Identifiants incorrects.",
                                Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            val userId: String = mAuth.currentUser?.uid ?: String();
                            val currentUserDb: DatabaseReference =
                                FirebaseDatabase.getInstance().getReference().child("users")
                                    .child(userId);
                            val userInfo: MutableMap<String, Any> = HashMap<String, Any>();

                            userInfo.put("name", name);
                            userInfo.put("email", email);
                            userInfo.put("profileImageUrl", "default");

                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                })
        }

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