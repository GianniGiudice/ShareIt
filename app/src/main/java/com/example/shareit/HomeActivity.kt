package com.example.shareit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        val logButton: Button = findViewById(R.id.login);
        val regButton: Button = findViewById(R.id.registration);

        logButton.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(view: View) {
                val intent: Intent = Intent(application, LoginActivity::class.java);
                startActivity(intent);
                return;

            }
        })

        regButton.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(view: View) {
                val intent: Intent = Intent(application, RegistrationActivity::class.java);
                startActivity(intent);
                return;
            }
        })
    }
}