package com.android.settingslib.widget;

import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
/* loaded from: classes.dex */
public class ActionBarShadowController implements LifecycleObserver {
    public static final float ELEVATION_HIGH = 8.0f;
    public static final float ELEVATION_LOW = 0.0f;
    public boolean mIsScrollWatcherAttached;
    public ScrollChangeWatcher mScrollChangeWatcher;

    /* loaded from: classes.dex */
    public final class ScrollChangeWatcher implements View.OnScrollChangeListener {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private void attachScrollWatcher() {
        if (!this.mIsScrollWatcherAttached) {
            this.mIsScrollWatcherAttached = true;
            throw null;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private void detachScrollWatcher() {
        throw null;
    }
}
