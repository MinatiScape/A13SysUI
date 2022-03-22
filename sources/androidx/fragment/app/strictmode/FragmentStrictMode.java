package androidx.fragment.app.strictmode;

import android.annotation.SuppressLint;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
@SuppressLint({"SyntheticAccessor"})
/* loaded from: classes.dex */
public final class FragmentStrictMode {
    public static Policy defaultPolicy = Policy.LAX;

    /* loaded from: classes.dex */
    public enum Flag {
        PENALTY_LOG,
        PENALTY_DEATH,
        DETECT_FRAGMENT_REUSE,
        DETECT_FRAGMENT_TAG_USAGE,
        /* JADX INFO: Fake field, exist only in values array */
        DETECT_RETAIN_INSTANCE_USAGE,
        /* JADX INFO: Fake field, exist only in values array */
        DETECT_SET_USER_VISIBLE_HINT,
        DETECT_TARGET_FRAGMENT_USAGE,
        DETECT_WRONG_FRAGMENT_CONTAINER
    }

    /* loaded from: classes.dex */
    public interface OnViolationListener {
        void onViolation();
    }

    public static void logIfDebuggingEnabled(Violation violation) {
        if (FragmentManager.isLoggingEnabled(3)) {
            StringBuilder sb = new StringBuilder();
            sb.append("StrictMode violation in ");
            Objects.requireNonNull(violation);
            sb.append(violation.mFragment.getClass().getName());
            Log.d("FragmentManager", sb.toString(), violation);
        }
    }

    /* loaded from: classes.dex */
    public static final class Policy {
        public static final Policy LAX = new Policy(new HashSet(), new HashMap());
        public final HashMap mAllowedViolations;
        public final HashSet mFlags;
        public final OnViolationListener mListener = null;

        public Policy(HashSet hashSet, HashMap hashMap) {
            this.mFlags = new HashSet(hashSet);
            HashMap hashMap2 = new HashMap();
            for (Map.Entry entry : hashMap.entrySet()) {
                hashMap2.put((Class) entry.getKey(), new HashSet((Collection) entry.getValue()));
            }
            this.mAllowedViolations = hashMap2;
        }
    }

    public static Policy getNearestPolicy(Fragment fragment) {
        boolean z;
        while (fragment != null) {
            if (fragment.mHost == null || !fragment.mAdded) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                fragment.getParentFragmentManager();
            }
            fragment = fragment.mParentFragment;
        }
        return defaultPolicy;
    }

    public static void onFragmentReuse(Fragment fragment, String str) {
        FragmentReuseViolation fragmentReuseViolation = new FragmentReuseViolation(fragment, str);
        logIfDebuggingEnabled(fragmentReuseViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_FRAGMENT_REUSE) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), FragmentReuseViolation.class)) {
            handlePolicyViolation(nearestPolicy, fragmentReuseViolation);
        }
    }

    public static void onFragmentTagUsage(Fragment fragment, ViewGroup viewGroup) {
        FragmentTagUsageViolation fragmentTagUsageViolation = new FragmentTagUsageViolation(fragment, viewGroup);
        logIfDebuggingEnabled(fragmentTagUsageViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_FRAGMENT_TAG_USAGE) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), FragmentTagUsageViolation.class)) {
            handlePolicyViolation(nearestPolicy, fragmentTagUsageViolation);
        }
    }

    public static void onWrongFragmentContainer(Fragment fragment, ViewGroup viewGroup) {
        WrongFragmentContainerViolation wrongFragmentContainerViolation = new WrongFragmentContainerViolation(fragment, viewGroup);
        logIfDebuggingEnabled(wrongFragmentContainerViolation);
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (nearestPolicy.mFlags.contains(Flag.DETECT_WRONG_FRAGMENT_CONTAINER) && shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), WrongFragmentContainerViolation.class)) {
            handlePolicyViolation(nearestPolicy, wrongFragmentContainerViolation);
        }
    }

    public static void runOnHostThread(Fragment fragment, Runnable runnable) {
        boolean z;
        if (fragment.mHost == null || !fragment.mAdded) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            FragmentHostCallback<?> fragmentHostCallback = fragment.getParentFragmentManager().mHost;
            Objects.requireNonNull(fragmentHostCallback);
            Handler handler = fragmentHostCallback.mHandler;
            if (handler.getLooper() == Looper.myLooper()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        } else {
            runnable.run();
        }
    }

    public static boolean shouldHandlePolicyViolation(Policy policy, Class<? extends Fragment> cls, Class<? extends Violation> cls2) {
        Set set = (Set) policy.mAllowedViolations.get(cls);
        if (set == null) {
            return true;
        }
        if (cls2.getSuperclass() == Violation.class || !set.contains(cls2.getSuperclass())) {
            return !set.contains(cls2);
        }
        return false;
    }

    public static void handlePolicyViolation(final Policy policy, final Violation violation) {
        Objects.requireNonNull(violation);
        Fragment fragment = violation.mFragment;
        final String name = fragment.getClass().getName();
        if (policy.mFlags.contains(Flag.PENALTY_LOG)) {
            Log.d("FragmentStrictMode", "Policy violation in " + name, violation);
        }
        if (policy.mListener != null) {
            runOnHostThread(fragment, new Runnable() { // from class: androidx.fragment.app.strictmode.FragmentStrictMode.1
                @Override // java.lang.Runnable
                public final void run() {
                    Policy.this.mListener.onViolation();
                }
            });
        }
        if (policy.mFlags.contains(Flag.PENALTY_DEATH)) {
            runOnHostThread(fragment, new Runnable() { // from class: androidx.fragment.app.strictmode.FragmentStrictMode.2
                @Override // java.lang.Runnable
                public final void run() {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Policy violation with PENALTY_DEATH in ");
                    m.append(name);
                    Log.e("FragmentStrictMode", m.toString(), violation);
                    throw violation;
                }
            });
        }
    }

    public static void onPolicyViolation(Violation violation) {
        logIfDebuggingEnabled(violation);
        Objects.requireNonNull(violation);
        Fragment fragment = violation.mFragment;
        Policy nearestPolicy = getNearestPolicy(fragment);
        if (shouldHandlePolicyViolation(nearestPolicy, fragment.getClass(), violation.getClass())) {
            handlePolicyViolation(nearestPolicy, violation);
        }
    }
}
