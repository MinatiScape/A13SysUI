package com.android.systemui.util.condition;

import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda2;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.condition.Condition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class Monitor implements CallbackController<Callback> {
    public final HashSet mConditions;
    public final Executor mExecutor;
    public final ArrayList<Callback> mCallbacks = new ArrayList<>();
    public boolean mAllConditionsMet = false;
    public boolean mHaveConditionsStarted = false;
    public final AnonymousClass1 mConditionCallback = new AnonymousClass1();

    /* renamed from: com.android.systemui.util.condition.Monitor$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Condition.Callback {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.util.condition.Condition.Callback
        public final void onConditionChanged() {
            Monitor.this.mExecutor.execute(new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this, 4));
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
        void onConditionsChanged(boolean z);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(Callback callback) {
        this.mExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda2(this, callback, 3));
    }

    public final void addCallbackLocked(Callback callback) {
        if (!this.mCallbacks.contains(callback)) {
            if (Log.isLoggable("Monitor", 3)) {
                Log.d("Monitor", "adding callback");
            }
            this.mCallbacks.add(callback);
            callback.onConditionsChanged(this.mAllConditionsMet);
            if (!this.mHaveConditionsStarted) {
                if (Log.isLoggable("Monitor", 3)) {
                    Log.d("Monitor", "starting all conditions");
                }
                this.mConditions.forEach(new DozeTriggers$$ExternalSyntheticLambda3(this, 5));
                updateConditionMetState();
                this.mHaveConditionsStarted = true;
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(Callback callback) {
        this.mExecutor.execute(new Monitor$$ExternalSyntheticLambda1(this, callback, 0));
    }

    public final void updateConditionMetState() {
        boolean z;
        Collection collection = (Collection) this.mConditions.stream().filter(Monitor$$ExternalSyntheticLambda4.INSTANCE).collect(Collectors.toSet());
        if (collection.isEmpty()) {
            collection = this.mConditions;
        }
        if (collection.isEmpty()) {
            z = true;
        } else {
            z = collection.stream().map(Monitor$$ExternalSyntheticLambda3.INSTANCE).allMatch(Monitor$$ExternalSyntheticLambda5.INSTANCE);
        }
        if (z != this.mAllConditionsMet) {
            if (Log.isLoggable("Monitor", 3)) {
                ViewCompat$$ExternalSyntheticLambda0.m("all conditions met: ", z, "Monitor");
            }
            this.mAllConditionsMet = z;
            Iterator<Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                Callback next = it.next();
                if (next == null) {
                    it.remove();
                } else {
                    next.onConditionsChanged(this.mAllConditionsMet);
                }
            }
        }
    }

    public Monitor(Executor executor, Set<Condition> set, Set<Callback> set2) {
        HashSet hashSet = new HashSet();
        this.mConditions = hashSet;
        this.mExecutor = executor;
        if (set != null) {
            hashSet.addAll(set);
        }
        if (set2 != null) {
            for (Callback callback : set2) {
                addCallbackLocked(callback);
            }
        }
    }
}
