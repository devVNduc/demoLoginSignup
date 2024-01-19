package com.example.demologinsignup.Animation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.demologinsignup.R;

public class animation extends AppCompatActivity {
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);
        animationView = findViewById(R.id.animation_view);
        animationView.animate().translationX(2000).setDuration(500).setStartDelay(6000);
    }
}