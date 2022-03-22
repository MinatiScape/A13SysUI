package com.google.android.setupcompat.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.fragment.R$id;
import com.android.systemui.R$array;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class LifecycleFragment extends Fragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public long durationInNanos = 0;
    public MetricKey metricKey;
    public long startInNanos;

    public LifecycleFragment() {
        setRetainInstance(true);
    }

    @Override // android.app.Fragment
    public final void onAttach(Context context) {
        super.onAttach(context);
        this.metricKey = MetricKey.get("ScreenDuration", getActivity());
    }

    @Override // android.app.Fragment
    public final void onDetach() {
        boolean z;
        super.onDetach();
        Activity activity = getActivity();
        MetricKey metricKey = this.metricKey;
        long millis = TimeUnit.NANOSECONDS.toMillis(this.durationInNanos);
        Objects.requireNonNull(activity, "Context cannot be null.");
        Objects.requireNonNull(metricKey, "Timer name cannot be null.");
        if (millis >= 0) {
            z = true;
        } else {
            z = false;
        }
        R$array.checkArgument(z, "Duration cannot be negative.");
        SetupCompatServiceInvoker setupCompatServiceInvoker = SetupCompatServiceInvoker.get(activity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("MetricKey_bundle", MetricKey.fromMetricKey(metricKey));
        bundle.putLong("timeMillis", millis);
        setupCompatServiceInvoker.logMetricEvent(2, bundle);
    }

    @Override // android.app.Fragment
    public final void onPause() {
        super.onPause();
        this.durationInNanos = (ClockProvider.ticker.read() - this.startInNanos) + this.durationInNanos;
    }

    @Override // android.app.Fragment
    public final void onResume() {
        super.onResume();
        this.startInNanos = ClockProvider.ticker.read();
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putLong("onScreenResume", System.nanoTime());
        R$id.logCustomEvent(getActivity(), CustomEvent.create(MetricKey.get("ScreenActivity", getActivity()), persistableBundle));
    }
}
