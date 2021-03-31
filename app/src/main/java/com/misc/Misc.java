package com.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.activities.InfoPageActivity;
import com.activities.R;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Misc {

    /**
     * Share an image with another application
     * @param act The activity performing the share
     * @param fname The file name to share as
     * @param bmp Bitmap containing the image
     */
    public static void share(Activity act, String fname, Bitmap bmp) {
        try {
            File cachePath = new File(act.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/" + fname); // overwrites this image every time
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        File imagePath = new File(act.getCacheDir(), "images");
        File newFile = new File(imagePath, fname);
        Uri contentUri = FileProvider.getUriForFile(act, "com.JWPatterns.fileprovider", newFile);

        if (contentUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, act.getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            act.startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

}
