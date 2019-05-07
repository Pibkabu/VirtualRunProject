package com.example.quynh.virtualrunproject.functionscreen.supports;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quynh.virtualrunproject.R;
import com.example.quynh.virtualrunproject.helper.PictureResizeHandler;

public class TutorialScreen extends AppCompatActivity {

    private ImageView backBtn;
    private ImageView tut1, tut2, tut3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);

        setupView();
        setupAction();
    }

    private void setupView() {
        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText("Hướng dẫn");
        backBtn = (ImageView) findViewById(R.id.back_btn);
        backBtn.setVisibility(View.VISIBLE);

        tut1 = (ImageView) findViewById(R.id.image1);
        tut2 = (ImageView) findViewById(R.id.image2);
        tut3 = (ImageView) findViewById(R.id.image3);

        tut1.setImageDrawable(PictureResizeHandler.resizeImage(R.drawable.tutorial1, this));
        tut2.setImageDrawable(PictureResizeHandler.resizeImage(R.drawable.tutorial2, this));
        tut3.setImageDrawable(PictureResizeHandler.resizeImage(R.drawable.tutorial3, this));
    }

    private void setupAction() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
