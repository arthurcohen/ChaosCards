package com.forcohen.chaoscards;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.forcohen.chaoscards.models.EnemyCard;
import com.forcohen.chaoscards.models.Jokenpo;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends FragmentActivity {
    private Trial trial;
    private Player localPlayer;
    private EnemyCard enemy = new EnemyCard();
    private Button playButtonRock;
    private Button playButtonPaper;
    private Button playButtonScissors;
    private ImageView playerChoose;
    private ImageView enemyChoose;
    private TextView playerScoreView;
    private TextView enemyScoreView;
    private int playerScore = 0;
    private int enemyScore = 0;
    private boolean enemyHere = false;
    DocumentReference trialDoc;
    FirebaseFirestore db;
    ArrayAdapter<Play> playsAdapter;
    Jokenpo choice;
    Jokenpo enemyChoice;
    String currentTrialId = null;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);



        playButtonRock = findViewById(R.id.rock_button);
        playButtonPaper = findViewById(R.id.paper_button);
        playButtonScissors = findViewById(R.id.scissors_button);
        playerChoose = findViewById(R.id.player_image);
        enemyChoose = findViewById(R.id.enemy_image);
        playerScoreView = findViewById(R.id.player_score);
        enemyScoreView = findViewById(R.id.enemy_score);

        disableButtons();


        if (getIntent().hasExtra("currentTrial")){
            currentTrialId = getIntent().getStringExtra("currentTrial");
        }
        ((TextView)findViewById(R.id.game_id)).setText("#" + currentTrialId);

        Log.wtf("PLAY_TRIAL", currentTrialId);

        if (currentTrialId != null){
            Toast.makeText(this, currentTrialId, Toast.LENGTH_SHORT).show();

            ((EditText)findViewById(R.id.trial_id)).setText(currentTrialId);

            db = FirebaseFirestore.getInstance();
            localPlayer = new Player(FirebaseAuth.getInstance().getCurrentUser().getUid());
            Log.i("play_filter", localPlayer.getPlayer());

            trialDoc = db.collection("trials").document(currentTrialId);

            trialDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    trial = task.getResult().toObject(Trial.class);


                    if (trial != null){

                        Log.i("play_filter", String.valueOf(trial.getPlayers().size()));
                        if (trial.getPlayers().size() < 2){
                            trial.getPlayers().add(localPlayer.getPlayer());
                            if (trial.getPlayers().size() == 2)
                                enableButtons();
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

                    Log.i("play_filter", "recieved something");

                    if (trial != null){

                        if (trial.getPlayers() != null){

                            if (!enemyHere && trial.getPlayers().size() == 2){
                                enemyHere = true;

                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),"channel_01")
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentText("Your enemy came to the battle. Go crash him out!")
                                        .setAutoCancel(true)
                                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                                        .setChannelId("channel_01")
                                        .setAutoCancel(true)
                                        .setContentTitle("IIIIIIT's TIIIIIMEEEE!!!");

                                mNotificationManager.notify(1, mBuilder.build());

                                NotificationChannel channel = new NotificationChannel("channel_01", "Playback Notification", NotificationManager.IMPORTANCE_HIGH);

                                assert mNotificationManager != null;
                                mBuilder.setChannelId("channel_01");
                                mNotificationManager.createNotificationChannel(channel);
                            }
                        }

                        if (trial.getPlays().size() > 0){
                            int howManyPlays = trial.getPlays().size() - 1;
                            if (!trial.getPlays().get(howManyPlays).getPlayer().getPlayer().equals(localPlayer.getPlayer())){
                                Log.i("play_filter", "enemy");
                                Log.i("play_filter", trial.getPlays().get(howManyPlays).getPlayer().getPlayer());

                                enemyChoice = trial.getPlays().get(howManyPlays).getPlay();

                                if (choice != null){
                                    compare();
                                }
                                enableButtons();

                            }else{
                                Log.i("play_filter", "player");

                                if (enemyChoice != null){
                                    compare();
                                }
                            }
                        }
                    }

                    if (playsAdapter != null){
                        playsAdapter.clear();
                        playsAdapter.addAll(trial.getPlays());
                        playsAdapter.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    public void onPlayClick(Jokenpo played){

        Log.i("play_filter", "clicked play");

        disableButtons();

        choice = played;

        final Play play = new Play(enemy, localPlayer, 10, choice);
        trial.getPlays().add(play);

        trialDoc.set(trial);
    }

    public void onPlayClickRock(View v){
        onPlayClick(Jokenpo.ROCK);
    }
    public void onPlayClickPaper(View v){
        onPlayClick(Jokenpo.PAPER);
    }
    public void onPlayClickScissors(View v){
        onPlayClick(Jokenpo.SCISSORS);
    }

    void compare(){
        try{
            Log.i("play_filter", choice.toString() + " vs " + enemyChoice.toString());

            if (enemyChoice == choice){
                Toast.makeText(PlayActivity.this, "Draw", Toast.LENGTH_SHORT).show();
                if (choice == Jokenpo.ROCK){
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/oncoming-fist.png"), null));
                    enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/oncoming-fist.png"), null));
                }else if(choice == Jokenpo.PAPER){
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/raised-hand-with-fingers-splayed.png"), null));
                    enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/raised-hand-with-fingers-splayed.png"), null));
                }else{
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/victory-hand.png"), null));
                    enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/victory-hand.png"), null));
                }
            }else{
                if (choice == Jokenpo.ROCK){
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/oncoming-fist.png"), null));

                    if (enemyChoice == Jokenpo.PAPER){
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/raised-hand-with-fingers-splayed.png"), null));
                        enemyScore++;
                    }else if (enemyChoice == Jokenpo.SCISSORS){
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/victory-hand.png"), null));
                        playerScore++;
                    }

                }else if (choice == Jokenpo.PAPER){
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/raised-hand-with-fingers-splayed.png"), null));
                    if (enemyChoice == Jokenpo.SCISSORS){
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/victory-hand.png"), null));
                        enemyScore++;
                    }else if (enemyChoice == Jokenpo.ROCK){
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/oncoming-fist.png"), null));
                        playerScore++;
                    }

                }else{
                    playerChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/victory-hand.png"), null));
                    if (enemyChoice == Jokenpo.PAPER){
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/raised-hand-with-fingers-splayed.png"), null));
                        playerScore++;
                    }else{
                        enemyChoose.setImageDrawable(Drawable.createFromStream(getAssets().open("images/oncoming-fist.png"), null));
                        enemyScore++;
                    }
                }
            }

        }catch (Exception e){
            Log.i("play_filter", e.getMessage());
        }

        Log.i("play_filter", "player " + playerScore);
        Log.i("play_filter", "enemy " + enemyScore);
        playerScoreView.setText(String.valueOf(playerScore));
        enemyScoreView.setText(String.valueOf(enemyScore));

        choice = null;
        enemyChoice = null;
    }

    private void disableButtons(){
        playButtonRock.setEnabled(false);
        playButtonPaper.setEnabled(false);
        playButtonScissors.setEnabled(false);
    }

    private void enableButtons(){
        playButtonRock.setEnabled(true);
        playButtonPaper.setEnabled(true);
        playButtonScissors.setEnabled(true);
    }

    public void copyId(View v){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Added to clipboard", currentTrialId);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Room ID copied", Toast.LENGTH_SHORT).show();
    }
}
