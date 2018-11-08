package com.forcohen.chaoscards;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.forcohen.chaoscards.models.EnemyCard;
import com.forcohen.chaoscards.models.Player;
import com.forcohen.chaoscards.models.Trial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.forcohen.chaoscards.models.Play;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends FragmentActivity {
    private Trial trial;
    private Player localPlayer;
    private EnemyCard enemy = new EnemyCard();
    DocumentReference trialDoc;
    FirebaseFirestore db;
    ArrayAdapter<Play> playsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        String currentTrialId = null;

        if (getIntent().hasExtra("currentTrial")){
            currentTrialId = getIntent().getStringExtra("currentTrial");
        }

        Log.wtf("PLAY_TRIAL", currentTrialId);

        if (currentTrialId != null){
            Toast.makeText(this, currentTrialId, Toast.LENGTH_SHORT).show();

            ((EditText)findViewById(R.id.trial_id)).setText(currentTrialId);

            db = FirebaseFirestore.getInstance();
            localPlayer = new Player(FirebaseAuth.getInstance().getCurrentUser().getUid());

            trialDoc = db.collection("trials").document(currentTrialId);

            trialDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    trial = task.getResult().toObject(Trial.class);


                    if (trial != null){
                        if (trial.getPlayers().size() < 2){
                            trial.getPlayers().add(localPlayer.getPlayer());
                        }else if (trial.getPlayers().indexOf(localPlayer.getPlayer()) == -1){
                            Toast.makeText(PlayActivity.this, "Sessao lotada", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        List<Play> plays = trial.getPlays();

                        ListView listView = findViewById(R.id.plays_list);
                        playsAdapter = new ArrayAdapter<Play>(getApplicationContext(), android.R.layout.simple_list_item_1, plays);
                        //TODO Atualizar o listView e adapter
                        listView.setAdapter(playsAdapter);

                        trialDoc.set(trial);
                    }else {
                        Toast.makeText(PlayActivity.this, "Nada encontrado...", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            trialDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                    trial = documentSnapshot.toObject(Trial.class);
                    if (playsAdapter != null){
                        playsAdapter.clear();
                        playsAdapter.addAll(trial.getPlays());
                        playsAdapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    public void onPlayClick(View v){

        final Play play = new Play(enemy, localPlayer, 10);
        trial.getPlays().add(play);

        trialDoc.set(trial);
    }
}
