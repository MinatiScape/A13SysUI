package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.provider.FontRequestWorker;
import androidx.transition.ViewUtilsBase;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CallbackWithHandler {
    public final ViewUtilsBase mCallback;
    public final Handler mCallbackHandler;

    /* renamed from: androidx.core.provider.CallbackWithHandler$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements Runnable {
        public final /* synthetic */ ViewUtilsBase val$callback;
        public final /* synthetic */ Typeface val$typeface;

        public AnonymousClass1(ViewUtilsBase viewUtilsBase, Typeface typeface) {
            this.val$callback = viewUtilsBase;
            this.val$typeface = typeface;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ViewUtilsBase viewUtilsBase = this.val$callback;
            Typeface typeface = this.val$typeface;
            TypefaceCompat.ResourcesCallbackAdapter resourcesCallbackAdapter = (TypefaceCompat.ResourcesCallbackAdapter) viewUtilsBase;
            Objects.requireNonNull(resourcesCallbackAdapter);
            ResourcesCompat.FontCallback fontCallback = resourcesCallbackAdapter.mFontCallback;
            if (fontCallback != null) {
                fontCallback.onFontRetrieved(typeface);
            }
        }
    }

    /* renamed from: androidx.core.provider.CallbackWithHandler$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 implements Runnable {
        public final /* synthetic */ ViewUtilsBase val$callback;
        public final /* synthetic */ int val$reason;

        public AnonymousClass2(ViewUtilsBase viewUtilsBase, int i) {
            this.val$callback = viewUtilsBase;
            this.val$reason = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            ViewUtilsBase viewUtilsBase = this.val$callback;
            int i = this.val$reason;
            TypefaceCompat.ResourcesCallbackAdapter resourcesCallbackAdapter = (TypefaceCompat.ResourcesCallbackAdapter) viewUtilsBase;
            Objects.requireNonNull(resourcesCallbackAdapter);
            ResourcesCompat.FontCallback fontCallback = resourcesCallbackAdapter.mFontCallback;
            if (fontCallback != null) {
                fontCallback.onFontRetrievalFailed(i);
            }
        }
    }

    public CallbackWithHandler(TypefaceCompat.ResourcesCallbackAdapter resourcesCallbackAdapter, Handler handler) {
        this.mCallback = resourcesCallbackAdapter;
        this.mCallbackHandler = handler;
    }

    public final void onTypefaceResult(FontRequestWorker.TypefaceResult typefaceResult) {
        boolean z;
        Objects.requireNonNull(typefaceResult);
        int i = typefaceResult.mResult;
        if (i == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Typeface typeface = typefaceResult.mTypeface;
            this.mCallbackHandler.post(new AnonymousClass1(this.mCallback, typeface));
            return;
        }
        this.mCallbackHandler.post(new AnonymousClass2(this.mCallback, i));
    }
}
