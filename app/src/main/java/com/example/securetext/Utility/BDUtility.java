package com.example.securetext.Utility;

import static android.os.Build.VERSION.SDK_INT;

import android.content.Context;
import android.os.Build;

public class BDUtility {

    public static void setClipBoard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(text);
        } else {
            android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clipData = android.content.ClipData.newPlainText("Copied Text", text);
            clipboardManager.setPrimaryClip(clipData);
        }
    }

}
