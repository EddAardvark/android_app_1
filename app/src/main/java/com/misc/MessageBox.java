package com.misc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageBox {

    /**
     * Show an "OK" message box where the user has no option but to click "OK"
     * @param title The title
     * @param message A more detailed message
     * @param button_text What appears on the button
     */
    public static void showOK (Context ctx, String title, String message, String button_text) {

        final AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
