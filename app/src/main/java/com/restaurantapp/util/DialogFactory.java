package com.restaurantapp.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
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

    public static Dialog createSimpleOkErrorDialog(Context context, @StringRes int titleResId,
                                                   @StringRes int messageResId) {
        return createSimpleOkErrorDialog(context,
                context.getString(titleResId),
                context.getString(messageResId));
    }

    public static Dialog createGenericErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.sorry_text))
                .setMessage(message)
                .setNeutralButton(R.string.ok_text, null);
        return alertDialog.create();
    }

    public static Dialog createGenericErrorDialog(Context context,
                                                  @StringRes int messageResId) {
        return createGenericErrorDialog(context, context.getString(messageResId));
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
