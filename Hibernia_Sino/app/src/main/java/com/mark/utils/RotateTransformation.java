package com.mark.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RotateTransformation  extends BitmapTransformation {

    //旋转默认0
    private float rotateRotationAngle = 0f;

    public RotateTransformation(float rotateRotationAngle)
    {
        super();
        this.rotateRotationAngle = rotateRotationAngle ;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        //旋转
        matrix.postRotate(rotateRotationAngle);
        //生成新的Bitmap
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);

        //return null;
    }



    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
