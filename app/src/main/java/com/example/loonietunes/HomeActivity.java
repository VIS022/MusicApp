package com.example.loonietunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MaterialButton btn3 = findViewById(R.id.songs);
        imageView = findViewById(R.id.imageView); // Replace with your actual ImageView ID

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SongList.class);
                startActivity(intent);
            }
        });

        // Start the continuous flipping animation
        startFlippingAnimation();
    }

    private void startFlippingAnimation() {
        // Create a set of animations for continuously flipping the coin from right to left
        RotateAnimation rotateRightToLeft = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateRightToLeft.setDuration(2000); // Adjust the duration as needed
        rotateRightToLeft.setRepeatCount(Animation.INFINITE);

        // Start the animation
        imageView.startAnimation(rotateRightToLeft);
    }
}
