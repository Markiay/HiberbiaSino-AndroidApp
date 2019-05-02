package com.mark.addition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mark.hibernia.R;

import uk.co.senab.photoview.PhotoView;

public class ImageZoom extends AppCompatActivity {
    private PhotoView photoView;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        imageView = findViewById(R.id.backImageButton);
        photoView = findViewById(R.id.photoView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageZoom.this.finish();
            }
        });

        byte[] ImageZoomBytes = ImageZoom.this.getIntent().getByteArrayExtra("imageURL");
        Glide.with(this)
                .load(ImageZoomBytes)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.test)
                .into(photoView);
    }
}
