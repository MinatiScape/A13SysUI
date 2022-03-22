package androidx.activity.result;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.transition.TransitionPropagation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/* loaded from: classes.dex */
public abstract class ActivityResultRegistry {
    public Random mRandom = new Random();
    public final HashMap mRcToKey = new HashMap();
    public final HashMap mKeyToRc = new HashMap();
    public final HashMap mKeyToLifecycleContainers = new HashMap();
    public ArrayList<String> mLaunchedKeys = new ArrayList<>();
    public final transient HashMap mKeyToCallback = new HashMap();
    public final HashMap mParsedPendingResults = new HashMap();
    public final Bundle mPendingResults = new Bundle();

    /* renamed from: androidx.activity.result.ActivityResultRegistry$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements LifecycleEventObserver {
        @Override // androidx.lifecycle.LifecycleEventObserver
        public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            if (Lifecycle.Event.ON_START.equals(event)) {
                throw null;
            } else if (Lifecycle.Event.ON_STOP.equals(event)) {
                throw null;
            } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                throw null;
            }
        }
    }

    /* renamed from: androidx.activity.result.ActivityResultRegistry$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass3 extends TransitionPropagation {
        public final /* synthetic */ String val$key;

        public AnonymousClass3(String str) {
            this.val$key = str;
        }
    }

    /* loaded from: classes.dex */
    public static class LifecycleContainer {
    }

    /* loaded from: classes.dex */
    public static class CallbackAndContract<O> {
        public final ActivityResultCallback<O> mCallback;
        public final ActivityResultContract<?, O> mContract;

        public CallbackAndContract(ActivityResultCallback<O> activityResultCallback, ActivityResultContract<?, O> activityResultContract) {
            this.mCallback = activityResultCallback;
            this.mContract = activityResultContract;
        }
    }

    public final boolean dispatchResult(int i, int i2, Intent intent) {
        ActivityResultCallback<O> activityResultCallback;
        String str = (String) this.mRcToKey.get(Integer.valueOf(i));
        if (str == null) {
            return false;
        }
        this.mLaunchedKeys.remove(str);
        CallbackAndContract callbackAndContract = (CallbackAndContract) this.mKeyToCallback.get(str);
        if (callbackAndContract == null || (activityResultCallback = callbackAndContract.mCallback) == 0) {
            this.mParsedPendingResults.remove(str);
            this.mPendingResults.putParcelable(str, new ActivityResult(i2, intent));
            return true;
        }
        activityResultCallback.onActivityResult(callbackAndContract.mContract.parseResult(i2, intent));
        return true;
    }

    public final AnonymousClass3 register(String str, ActivityResultContract activityResultContract, ActivityResultCallback activityResultCallback) {
        int i;
        if (((Integer) this.mKeyToRc.get(str)) == null) {
            int nextInt = this.mRandom.nextInt(2147418112);
            while (true) {
                i = nextInt + 65536;
                if (!this.mRcToKey.containsKey(Integer.valueOf(i))) {
                    break;
                }
                nextInt = this.mRandom.nextInt(2147418112);
            }
            this.mRcToKey.put(Integer.valueOf(i), str);
            this.mKeyToRc.put(str, Integer.valueOf(i));
        }
        this.mKeyToCallback.put(str, new CallbackAndContract(activityResultCallback, activityResultContract));
        if (this.mParsedPendingResults.containsKey(str)) {
            Object obj = this.mParsedPendingResults.get(str);
            this.mParsedPendingResults.remove(str);
            activityResultCallback.onActivityResult(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.mPendingResults.getParcelable(str);
        if (activityResult != null) {
            this.mPendingResults.remove(str);
            activityResultCallback.onActivityResult(activityResultContract.parseResult(activityResult.mResultCode, activityResult.mData));
        }
        return new AnonymousClass3(str);
    }
}
