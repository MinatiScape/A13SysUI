package com.android.settingslib.core.lifecycle;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.R$dimen;
import com.android.settingslib.core.lifecycle.events.OnAttach;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.android.systemui.plugins.FalsingManager;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Lifecycle extends LifecycleRegistry {
    public final ArrayList mObservers = new ArrayList();

    /* loaded from: classes.dex */
    public class LifecycleProxy implements LifecycleObserver {
        public LifecycleProxy() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        public void onLifecycleEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            int i = AnonymousClass1.$SwitchMap$androidx$lifecycle$Lifecycle$Event[event.ordinal()];
            int i2 = 0;
            switch (i) {
                case 2:
                    Lifecycle lifecycle = Lifecycle.this;
                    Objects.requireNonNull(lifecycle);
                    int size = lifecycle.mObservers.size();
                    while (i2 < size) {
                        LifecycleObserver lifecycleObserver = (LifecycleObserver) lifecycle.mObservers.get(i2);
                        if (lifecycleObserver instanceof OnStart) {
                            ((OnStart) lifecycleObserver).onStart();
                        }
                        i2++;
                    }
                    return;
                case 3:
                    Lifecycle lifecycle2 = Lifecycle.this;
                    Objects.requireNonNull(lifecycle2);
                    int size2 = lifecycle2.mObservers.size();
                    while (i2 < size2) {
                        LifecycleObserver lifecycleObserver2 = (LifecycleObserver) lifecycle2.mObservers.get(i2);
                        if (lifecycleObserver2 instanceof OnResume) {
                            ((OnResume) lifecycleObserver2).onResume();
                        }
                        i2++;
                    }
                    return;
                case 4:
                    Lifecycle lifecycle3 = Lifecycle.this;
                    Objects.requireNonNull(lifecycle3);
                    int size3 = lifecycle3.mObservers.size();
                    while (i2 < size3) {
                        LifecycleObserver lifecycleObserver3 = (LifecycleObserver) lifecycle3.mObservers.get(i2);
                        if (lifecycleObserver3 instanceof OnPause) {
                            ((OnPause) lifecycleObserver3).onPause();
                        }
                        i2++;
                    }
                    return;
                case 5:
                    Lifecycle lifecycle4 = Lifecycle.this;
                    Objects.requireNonNull(lifecycle4);
                    int size4 = lifecycle4.mObservers.size();
                    while (i2 < size4) {
                        LifecycleObserver lifecycleObserver4 = (LifecycleObserver) lifecycle4.mObservers.get(i2);
                        if (lifecycleObserver4 instanceof OnStop) {
                            ((OnStop) lifecycleObserver4).onStop();
                        }
                        i2++;
                    }
                    return;
                case FalsingManager.VERSION /* 6 */:
                    Lifecycle lifecycle5 = Lifecycle.this;
                    Objects.requireNonNull(lifecycle5);
                    int size5 = lifecycle5.mObservers.size();
                    while (i2 < size5) {
                        LifecycleObserver lifecycleObserver5 = (LifecycleObserver) lifecycle5.mObservers.get(i2);
                        if (lifecycleObserver5 instanceof OnDestroy) {
                            ((OnDestroy) lifecycleObserver5).onDestroy();
                        }
                        i2++;
                    }
                    return;
                case 7:
                    Log.wtf("LifecycleObserver", "Should not receive an 'ANY' event!");
                    return;
                default:
                    return;
            }
        }
    }

    public Lifecycle(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner, true);
        addObserver(new LifecycleProxy());
    }

    /* renamed from: com.android.settingslib.core.lifecycle.Lifecycle$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$androidx$lifecycle$Lifecycle$Event;

        static {
            int[] iArr = new int[Lifecycle.Event.values().length];
            $SwitchMap$androidx$lifecycle$Lifecycle$Event = iArr;
            try {
                iArr[Lifecycle.Event.ON_CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_RESUME.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_STOP.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_DESTROY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$androidx$lifecycle$Lifecycle$Event[Lifecycle.Event.ON_ANY.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    @Override // androidx.lifecycle.LifecycleRegistry, androidx.lifecycle.Lifecycle
    public final void addObserver(LifecycleObserver lifecycleObserver) {
        boolean z;
        if (R$dimen.sMainThread == null) {
            R$dimen.sMainThread = Looper.getMainLooper().getThread();
        }
        if (Thread.currentThread() == R$dimen.sMainThread) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            super.addObserver(lifecycleObserver);
            if (lifecycleObserver instanceof LifecycleObserver) {
                this.mObservers.add((LifecycleObserver) lifecycleObserver);
                return;
            }
            return;
        }
        throw new RuntimeException("Must be called on the UI thread");
    }

    public final void onAttach() {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = (LifecycleObserver) this.mObservers.get(i);
            if (lifecycleObserver instanceof OnAttach) {
                ((OnAttach) lifecycleObserver).onAttach$1();
            }
        }
    }

    public final void onCreate(Bundle bundle) {
        int size = this.mObservers.size();
        for (int i = 0; i < size; i++) {
            LifecycleObserver lifecycleObserver = (LifecycleObserver) this.mObservers.get(i);
            if (lifecycleObserver instanceof OnCreate) {
                ((OnCreate) lifecycleObserver).onCreate(bundle);
            }
        }
    }

    @Override // androidx.lifecycle.LifecycleRegistry, androidx.lifecycle.Lifecycle
    public final void removeObserver(LifecycleObserver lifecycleObserver) {
        boolean z;
        if (R$dimen.sMainThread == null) {
            R$dimen.sMainThread = Looper.getMainLooper().getThread();
        }
        if (Thread.currentThread() == R$dimen.sMainThread) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            super.removeObserver(lifecycleObserver);
            if (lifecycleObserver instanceof LifecycleObserver) {
                this.mObservers.remove(lifecycleObserver);
                return;
            }
            return;
        }
        throw new RuntimeException("Must be called on the UI thread");
    }
}
