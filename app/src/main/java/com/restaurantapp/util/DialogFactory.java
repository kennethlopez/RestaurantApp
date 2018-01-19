package com.restaurantapp.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.restaurantapp.R;


public final class DialogFactory {

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.ok_text, null);
        return alertDialog.create();
    }

    public static Dialog createSimpleOkErrorDialog(Context context, int titleResId, int messageResId) {
        return createSimpleOkErrorDialog(context,
                context.getString(titleResId),
                context.getString(messageResId));
    }

    public static Dialog createGenericErrorDialog(Context context, String message,
                                                  DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.sorry_text))
                .setMessage(message)
                .setNeutralButton(R.string.ok_text, clickListener)
                .setCancelable(false);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context, int messageResId,
                                                  DialogInterface.OnClickListener clickListener) {
        return createGenericErrorDialog(context, context.getString(messageResId), clickListener);
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        return progressDialog;
    }

    public static ProgressDialog createProgressDialog(Context context, int messageResource) {
        return createProgressDialog(context, context.getString(messageResource));
    }

}
