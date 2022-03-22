package com.android.systemui.sensorprivacy;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import com.android.internal.widget.DialogTitle;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SensorUseDialog.kt */
/* loaded from: classes.dex */
public final class SensorUseDialog extends SystemUIDialog {
    public SensorUseDialog(Context context, int i, DialogInterface.OnClickListener onClickListener, DialogInterface.OnDismissListener onDismissListener) {
        super(context);
        int i2;
        int i3;
        int i4;
        Window window = getWindow();
        Intrinsics.checkNotNull(window);
        window.addFlags(524288);
        Window window2 = getWindow();
        Intrinsics.checkNotNull(window2);
        window2.addSystemFlags(524288);
        View inflate = LayoutInflater.from(context).inflate(2131624470, (ViewGroup) null);
        DialogTitle requireViewById = inflate.requireViewById(2131428833);
        if (i == 1) {
            i2 = 2131953261;
        } else if (i == 2) {
            i2 = 2131953257;
        } else if (i != Integer.MAX_VALUE) {
            i2 = 0;
        } else {
            i2 = 2131953259;
        }
        requireViewById.setText(i2);
        ImageView imageView = (ImageView) inflate.requireViewById(2131428832);
        int i5 = 8;
        if (i == 1 || i == Integer.MAX_VALUE) {
            i3 = 0;
        } else {
            i3 = 8;
        }
        imageView.setVisibility(i3);
        ((ImageView) inflate.requireViewById(2131428831)).setVisibility((i == 2 || i == Integer.MAX_VALUE) ? 0 : i5);
        setCustomTitle(inflate);
        if (i == 1) {
            i4 = 2131953260;
        } else if (i == 2) {
            i4 = 2131953256;
        } else if (i != Integer.MAX_VALUE) {
            i4 = 0;
        } else {
            i4 = 2131953258;
        }
        setMessage(Html.fromHtml(context.getString(i4), 0));
        setButton(-1, context.getString(17041455), onClickListener);
        setButton(-2, context.getString(17039360), onClickListener);
        setOnDismissListener(onDismissListener);
        setCancelable(false);
    }
}
