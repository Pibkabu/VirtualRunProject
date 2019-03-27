package com.example.quynh.virtualrunproject.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.view.Display;

/**
 * Created by quynh on 2/9/2019.
 */

public class PictureResizeHandler {
    public static Drawable resizeImage(int imageResource, FragmentActivity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(context.getResources(),
                getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    public static Drawable resizeImageWithBitmap(Bitmap bitmap, FragmentActivity context){
        Display display = context.getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        double imageHeight = bitmap.getHeight();
        double imageWidth = bitmap.getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Drawable drawable = new BitmapDrawable(context.getResources(),
                getResizedBitmap(bitmap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
