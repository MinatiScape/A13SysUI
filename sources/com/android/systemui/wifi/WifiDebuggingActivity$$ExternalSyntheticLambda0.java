package com.android.systemui.wifi;

import android.util.EventLog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiDebuggingActivity$$ExternalSyntheticLambda0 implements View.OnTouchListener {
    public static final /* synthetic */ WifiDebuggingActivity$$ExternalSyntheticLambda0 INSTANCE = new WifiDebuggingActivity$$ExternalSyntheticLambda0();

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        int i = WifiDebuggingActivity.$r8$clinit;
        if ((motionEvent.getFlags() & 1) == 0 && (motionEvent.getFlags() & 2) == 0) {
            return false;
        }
        if (motionEvent.getAction() != 1) {
            return true;
        }
        EventLog.writeEvent(1397638484, "62187985");
        Toast.makeText(view.getContext(), 2131953375, 0).show();
        return true;
    }
}
