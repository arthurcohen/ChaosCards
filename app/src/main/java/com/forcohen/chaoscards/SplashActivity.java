package com.forcohen.chaoscards;

import android.animation.Animator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_splash);

        Typeface gameDescriptionFont = Typeface.createFromAsset(getAssets(), "fonts/Cinzel-Regular.ttf");
        Typeface gameTitleFont = Typeface.createFromAsset(getAssets(), "fonts/CinzelDecorative-Bold.ttf");
        final TextView gameTitle = (TextView) findViewById(R.id.game_title);
        final TextView gameDescription = (TextView) findViewById(R.id.game_description);

        if (gameTitleFont != null)
            gameTitle.setTypeface(gameTitleFont);


        if (gameDescriptionFont != null)
            gameDescription.setTypeface(gameDescriptionFont);

        final Intent homePageIntent = new Intent(this, LoginActivity.class);

        final ValueAnimator alphaAnimation = ValueAnimator.ofFloat(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("alpha", String.valueOf((float) animation.getAnimatedValue()));
                gameDescription.setAlpha((float) animation.getAnimatedValue());
            }
        });

        alphaAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(homePageIntent);
                SplashActivity.this.overridePendingTransition(R.anim.abc_popup_enter, R.anim.abc_popup_exit);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        float textSize = gameTitle.getTextSize();
        final ValueAnimator textAnimation = ValueAnimator.ofFloat(textSize, (textSize * 1.2f));
        textAnimation.setDuration(2000);
        textAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                gameTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue());
            }
        });

        textAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                alphaAnimation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        textAnimation.start();

        findViewById(R.id.activity_splash_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAnimation.end();
                alphaAnimation.end();
            }
        });

    }

}
