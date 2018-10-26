package com.forcohen.chaoscards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Toast.makeText(this, "Welcome back", Toast.LENGTH_LONG).show();
            doLogin();
        }
    }

    public void onClickRegister(View v){
        final String email = ((TextView)findViewById(R.id.login_field)).getText().toString();
        final String password = ((TextView)findViewById(R.id.pass_field)).getText().toString();


        if (!email.isEmpty() && !password.isEmpty()){
            final AlertDialog.Builder passwordDialog = new AlertDialog.Builder(this);

            passwordDialog.setTitle("Password");
            passwordDialog.setView(R.layout.fragment_password);

            passwordDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(LoginActivity.this, "Wait", Toast.LENGTH_SHORT).show();



                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "User created!", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Now you can play this s*** game...", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this, "Error when creating new user.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, "Maybe, the user already exists. Maybe not...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            passwordDialog.show();

        }else{
            AlertDialog.Builder wrongDataDialog = new AlertDialog.Builder(this);
            wrongDataDialog.setTitle("The field email and/or password can not be empty.");
            TextView message = new TextView(this);
            wrongDataDialog.setView(message);
            wrongDataDialog.setPositiveButton("Ok", null);
            wrongDataDialog.show();
        }
    }

    public void onClickLogin(View v){
        final String email = ((TextView)findViewById(R.id.login_field)).getText().toString();
        final String password = ((TextView)findViewById(R.id.pass_field)).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "logou", Toast.LENGTH_SHORT).show();
                    doLogin();
                }else{
                    Toast.makeText(LoginActivity.this, "Usuário e/ou senha incorretos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void doLogin(){
        final Intent homeIntent = new Intent(this, HomePageActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
