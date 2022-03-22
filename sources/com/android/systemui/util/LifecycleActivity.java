package com.android.systemui.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.core.lifecycle.Lifecycle;
/* compiled from: LifecycleActivity.kt */
/* loaded from: classes.dex */
public class LifecycleActivity extends Activity implements LifecycleOwner {
    public final Lifecycle lifecycle = new Lifecycle(this);

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        this.lifecycle.onAttach();
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }

    @Override // android.app.Activity
    public void onPause() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        super.onPause();
    }

    @Override // android.app.Activity
    public void onResume() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        super.onResume();
    }

    @Override // android.app.Activity
    public void onStart() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        super.onStart();
    }

    @Override // android.app.Activity
    public void onStop() {
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle, PersistableBundle persistableBundle) {
        this.lifecycle.onAttach();
        this.lifecycle.onCreate(bundle);
        this.lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        super.onCreate(bundle, persistableBundle);
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final androidx.lifecycle.Lifecycle getLifecycle() {
        return this.lifecycle;
    }
}
