package com.android.wm.shell.legacysplitscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
/* loaded from: classes.dex */
public class ForcedResizableInfoActivity extends Activity implements View.OnTouchListener {
    public final AnonymousClass1 mFinishRunnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivity.1
        @Override // java.lang.Runnable
        public final void run() {
            ForcedResizableInfoActivity.this.finish();
        }
    };

    @Override // android.app.Activity
    public final void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
    }

    @Override // android.app.Activity
    public final void finish() {
        super.finish();
        overridePendingTransition(0, 2130772505);
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(2131624100);
        TextView textView = (TextView) findViewById(16908299);
        int intExtra = getIntent().getIntExtra("extra_forced_resizeable_reason", -1);
        if (intExtra == 1) {
            str = getString(2131952295);
        } else if (intExtra == 2) {
            str = getString(2131952373);
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Unexpected forced resizeable reason: ", intExtra));
        }
        textView.setText(str);
        getWindow().setTitle(str);
        getWindow().getDecorView().setOnTouchListener(this);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        finish();
        return true;
    }

    @Override // android.app.Activity
    public final void onStart() {
        super.onStart();
        getWindow().getDecorView().postDelayed(this.mFinishRunnable, 2500L);
    }

    @Override // android.app.Activity
    public final void onStop() {
        super.onStop();
        finish();
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        finish();
        return true;
    }
}
