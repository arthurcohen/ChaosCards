package com.forcohen.chaoscards;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.forcohen.chaoscards.models.Player;
import com.forcohen.chaoscards.models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomePageActivity extends Activity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();

    }

    public void onLogout(View v){
        mAuth.signOut();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

        finish();
    }

    public void onPlay(View v){
        String targetTrial = ((EditText)findViewById(R.id.target_trial)).getText().toString();

        Toast.makeText(this, targetTrial, Toast.LENGTH_SHORT).show();

        if (targetTrial.isEmpty()){
            Trial newTrial = new Trial(mAuth.getCurrentUser().getUid());
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Query availableTrials = db.collection("trials").whereEqualTo("available", true);

            availableTrials.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot q : task.getResult()){
                        //Toast.makeText(HomePageActivity.this, q.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            Task<DocumentReference> newTrialDoc = db.collection("trials").add(newTrial).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
                    intent.putExtra("currentTrial", task.getResult().getId());
                    startActivity(intent);
                }
            });
        }else{
            Intent intent = new Intent(getApplicationContext(), PlayActivity.class);
            intent.putExtra("currentTrial", targetTrial);
            startActivity(intent);
        }
    }
}
