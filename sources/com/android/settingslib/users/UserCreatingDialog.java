package com.android.settingslib.users;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
/* loaded from: classes.dex */
public final class UserCreatingDialog extends AlertDialog {
    public UserCreatingDialog(Context context) {
        super(context, 16974546);
        setCancelable(false);
        View inflate = LayoutInflater.from(getContext()).inflate(2131624643, (ViewGroup) null);
        String string = getContext().getString(2131952214);
        inflate.setAccessibilityPaneTitle(string);
        ((TextView) inflate.findViewById(2131428366)).setText(string);
        setView(inflate);
        getWindow().setType(2010);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.privateFlags = 272;
        getWindow().setAttributes(attributes);
    }
}
