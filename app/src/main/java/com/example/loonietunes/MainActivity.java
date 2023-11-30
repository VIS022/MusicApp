package com.example.loonietunes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the ImageView and TextView to animate
        ImageView imageView = findViewById(R.id.imageView);
        TextView loonieText = findViewById(R.id.loonie);

        // Create a rotate animation (360 degrees) for the ImageView
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(2000); // Adjust the duration as needed

        // Set an animation listener to start the next activity after the rotation animation completes
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended, navigate to the main activity or next screen
                navigateToHomeActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated, if needed
            }
        });

        // Start the rotation animation on the ImageView
        imageView.startAnimation(rotateAnimation);

        // Create a move-up animation for the TextView
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -100);
        translateAnimation.setDuration(2000); // Adjust the duration as needed
        translateAnimation.setFillAfter(true); // Keep the final position after the animation

        // Set an animation listener to start the next activity after the translation animation completes
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation ended
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeated, if needed
            }
        });

        // Start the move-up animation on the TextView
        loonieText.startAnimation(translateAnimation);
    }

    private void navigateToHomeActivity() {
        // Start the main activity or next screen after a delay
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // Adjust the delay as needed
    }
}
