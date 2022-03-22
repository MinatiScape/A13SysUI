package com.airbnb.lottie;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.PathMeasure;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.appcompat.widget.AppCompatImageView;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.manager.ImageAssetManager;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.layer.CompositionLayer;
import com.airbnb.lottie.utils.LogcatLogger;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.LottieValueAnimator;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.LottieValueCallback;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.lang.ref.WeakReference;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.zip.ZipInputStream;
import javax.net.ssl.SSLException;
/* loaded from: classes.dex */
public class LottieAnimationView extends AppCompatImageView {
    public static final AnonymousClass1 DEFAULT_FAILURE_LISTENER = new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieAnimationView.1
        @Override // com.airbnb.lottie.LottieListener
        public final void onResult(Throwable th) {
            boolean z;
            Throwable th2 = th;
            PathMeasure pathMeasure = Utils.pathMeasure;
            if ((th2 instanceof SocketException) || (th2 instanceof ClosedChannelException) || (th2 instanceof InterruptedIOException) || (th2 instanceof ProtocolException) || (th2 instanceof SSLException) || (th2 instanceof UnknownHostException) || (th2 instanceof UnknownServiceException)) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                Objects.requireNonNull(Logger.INSTANCE);
                HashSet hashSet = LogcatLogger.loggedMessages;
                if (!hashSet.contains("Unable to load composition.")) {
                    Log.w("LOTTIE", "Unable to load composition.", th2);
                    hashSet.add("Unable to load composition.");
                    return;
                }
                return;
            }
            throw new IllegalStateException("Unable to parse composition", th2);
        }
    };
    public String animationName;
    public int animationResId;
    public LottieComposition composition;
    public LottieTask<LottieComposition> compositionTask;
    public LottieListener<Throwable> failureListener;
    public boolean isInitialized;
    public final AnonymousClass2 loadedListener = new LottieListener<LottieComposition>() { // from class: com.airbnb.lottie.LottieAnimationView.2
        @Override // com.airbnb.lottie.LottieListener
        public final void onResult(LottieComposition lottieComposition) {
            LottieAnimationView.this.setComposition(lottieComposition);
        }
    };
    public final AnonymousClass3 wrappedFailureListener = new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieAnimationView.3
        @Override // com.airbnb.lottie.LottieListener
        public final void onResult(Throwable th) {
            Throwable th2 = th;
            LottieAnimationView lottieAnimationView = LottieAnimationView.this;
            int i = lottieAnimationView.fallbackResource;
            if (i != 0) {
                lottieAnimationView.setImageResource(i);
            }
            LottieListener lottieListener = LottieAnimationView.this.failureListener;
            if (lottieListener == null) {
                lottieListener = LottieAnimationView.DEFAULT_FAILURE_LISTENER;
            }
            lottieListener.onResult(th2);
        }
    };
    public int fallbackResource = 0;
    public final LottieDrawable lottieDrawable = new LottieDrawable();
    public boolean wasAnimatingWhenNotShown = false;
    public boolean wasAnimatingWhenDetached = false;
    public boolean autoPlay = false;
    public boolean cacheComposition = true;
    public RenderMode renderMode = RenderMode.AUTOMATIC;
    public HashSet lottieOnCompositionLoadedListeners = new HashSet();
    public int buildDrawingCacheDepth = 0;

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.airbnb.lottie.LottieAnimationView.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public String animationName;
        public int animationResId;
        public String imageAssetsFolder;
        public boolean isAnimating;
        public float progress;
        public int repeatCount;
        public int repeatMode;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.animationName = parcel.readString();
            this.progress = parcel.readFloat();
            this.isAnimating = parcel.readInt() != 1 ? false : true;
            this.imageAssetsFolder = parcel.readString();
            this.repeatMode = parcel.readInt();
            this.repeatCount = parcel.readInt();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.animationName);
            parcel.writeFloat(this.progress);
            parcel.writeInt(this.isAnimating ? 1 : 0);
            parcel.writeString(this.imageAssetsFolder);
            parcel.writeInt(this.repeatMode);
            parcel.writeInt(this.repeatCount);
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.airbnb.lottie.LottieAnimationView$2] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.airbnb.lottie.LottieAnimationView$3] */
    public LottieAnimationView(Context context) {
        super(context);
        init(null);
    }

    public <T> void addValueCallback(KeyPath keyPath, T t, LottieValueCallback<T> lottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, lottieValueCallback);
    }

    public void cancelAnimation() {
        this.wasAnimatingWhenNotShown = false;
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.lazyCompositionTasks.clear();
        lottieDrawable.animator.cancel();
        enableOrDisableHardwareLayer();
    }

    public void pauseAnimation() {
        this.autoPlay = false;
        this.wasAnimatingWhenDetached = false;
        this.wasAnimatingWhenNotShown = false;
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.lazyCompositionTasks.clear();
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.removeFrameCallback(true);
        enableOrDisableHardwareLayer();
    }

    public void setAnimation(final InputStream inputStream, final String str) {
        setCompositionTask(LottieCompositionFactory.cache(str, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.4
            @Override // java.util.concurrent.Callable
            public final LottieResult<LottieComposition> call() throws Exception {
                return LottieCompositionFactory.fromJsonInputStreamSync(inputStream, str);
            }
        }));
    }

    @Deprecated
    public void setAnimationFromJson(String str) {
        setAnimationFromJson(str, null);
    }

    public final void setCompositionTask(LottieTask<LottieComposition> lottieTask) {
        this.composition = null;
        this.lottieDrawable.clearComposition();
        cancelLoaderTask();
        lottieTask.addListener(this.loadedListener);
        lottieTask.addFailureListener(this.wrappedFailureListener);
        this.compositionTask = lottieTask;
    }

    public void setMaxFrame(int i) {
        this.lottieDrawable.setMaxFrame(i);
    }

    public void setMinAndMaxFrame(String str) {
        this.lottieDrawable.setMinAndMaxFrame(str);
    }

    public void setMinFrame(int i) {
        this.lottieDrawable.setMinFrame(i);
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.addListener(animatorListener);
    }

    public void addAnimatorUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.addUpdateListener(animatorUpdateListener);
    }

    public boolean addLottieOnCompositionLoadedListener(LottieOnCompositionLoadedListener lottieOnCompositionLoadedListener) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition != null) {
            lottieOnCompositionLoadedListener.onCompositionLoaded(lottieComposition);
        }
        return this.lottieOnCompositionLoadedListeners.add(lottieOnCompositionLoadedListener);
    }

    public <T> void addValueCallback(KeyPath keyPath, T t, final SimpleLottieValueCallback<T> simpleLottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, new LottieValueCallback<T>() { // from class: com.airbnb.lottie.LottieAnimationView.4
            /* JADX WARN: Type inference failed for: r0v2, types: [T, android.graphics.PorterDuffColorFilter] */
            @Override // com.airbnb.lottie.value.LottieValueCallback
            public final T getValue(LottieFrameInfo<T> lottieFrameInfo) {
                return SimpleLottieValueCallback.this.getValue();
            }
        });
    }

    @Override // android.view.View
    public void buildDrawingCache(boolean z) {
        this.buildDrawingCacheDepth++;
        super.buildDrawingCache(z);
        if (this.buildDrawingCacheDepth == 1 && getWidth() > 0 && getHeight() > 0 && getLayerType() == 1 && getDrawingCache(z) == null) {
            setRenderMode(RenderMode.HARDWARE);
        }
        this.buildDrawingCacheDepth--;
        L.endSection();
    }

    public final void cancelLoaderTask() {
        LottieTask<LottieComposition> lottieTask = this.compositionTask;
        if (lottieTask != null) {
            AnonymousClass2 r1 = this.loadedListener;
            synchronized (lottieTask) {
                lottieTask.successListeners.remove(r1);
            }
            LottieTask<LottieComposition> lottieTask2 = this.compositionTask;
            AnonymousClass3 r3 = this.wrappedFailureListener;
            Objects.requireNonNull(lottieTask2);
            synchronized (lottieTask2) {
                lottieTask2.failureListeners.remove(r3);
            }
        }
    }

    public void disableExtraScaleModeInFitXY() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.isExtraScaleEnabled = false;
    }

    public void enableMergePathsForKitKatAndAbove(boolean z) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        if (lottieDrawable.enableMergePaths != z) {
            lottieDrawable.enableMergePaths = z;
            if (lottieDrawable.composition != null) {
                lottieDrawable.buildCompositionLayer();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0026, code lost:
        if (r0 == false) goto L_0x000c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x000a, code lost:
        if (r0 != 1) goto L_0x000c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000c, code lost:
        r1 = 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void enableOrDisableHardwareLayer() {
        /*
            r4 = this;
            com.airbnb.lottie.RenderMode r0 = r4.renderMode
            int r0 = r0.ordinal()
            r1 = 2
            r2 = 1
            if (r0 == 0) goto L_0x000e
            if (r0 == r2) goto L_0x0028
        L_0x000c:
            r1 = r2
            goto L_0x0028
        L_0x000e:
            com.airbnb.lottie.LottieComposition r0 = r4.composition
            if (r0 == 0) goto L_0x0017
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.hasDashPattern
        L_0x0017:
            com.airbnb.lottie.LottieComposition r0 = r4.composition
            if (r0 == 0) goto L_0x0025
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.maskAndMatteCount
            r3 = 4
            if (r0 <= r3) goto L_0x0025
            r0 = 0
            goto L_0x0026
        L_0x0025:
            r0 = r2
        L_0x0026:
            if (r0 == 0) goto L_0x000c
        L_0x0028:
            int r0 = r4.getLayerType()
            if (r1 == r0) goto L_0x0032
            r0 = 0
            r4.setLayerType(r1, r0)
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieAnimationView.enableOrDisableHardwareLayer():void");
    }

    public long getDuration() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition != null) {
            return lottieComposition.getDuration();
        }
        return 0L;
    }

    public int getFrame() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        Objects.requireNonNull(lottieValueAnimator);
        return (int) lottieValueAnimator.frame;
    }

    public String getImageAssetsFolder() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.imageAssetsFolder;
    }

    public float getMaxFrame() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.animator.getMaxFrame();
    }

    public float getMinFrame() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.animator.getMinFrame();
    }

    public PerformanceTracker getPerformanceTracker() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieComposition lottieComposition = lottieDrawable.composition;
        if (lottieComposition != null) {
            return lottieComposition.performanceTracker;
        }
        return null;
    }

    public float getProgress() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.animator.getAnimatedValueAbsolute();
    }

    public int getRepeatCount() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.animator.getRepeatCount();
    }

    public int getRepeatMode() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.animator.getRepeatMode();
    }

    public float getScale() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.scale;
    }

    public float getSpeed() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        Objects.requireNonNull(lottieValueAnimator);
        return lottieValueAnimator.speed;
    }

    public boolean hasMasks() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        CompositionLayer compositionLayer = lottieDrawable.compositionLayer;
        if (compositionLayer == null || !compositionLayer.hasMasks()) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean hasMatte() {
        /*
            r4 = this;
            com.airbnb.lottie.LottieDrawable r4 = r4.lottieDrawable
            java.util.Objects.requireNonNull(r4)
            com.airbnb.lottie.model.layer.CompositionLayer r4 = r4.compositionLayer
            r0 = 0
            r1 = 1
            if (r4 == 0) goto L_0x0050
            java.lang.Boolean r2 = r4.hasMatte
            if (r2 != 0) goto L_0x0047
            com.airbnb.lottie.model.layer.BaseLayer r2 = r4.matteLayer
            if (r2 == 0) goto L_0x0015
            r2 = r1
            goto L_0x0016
        L_0x0015:
            r2 = r0
        L_0x0016:
            if (r2 == 0) goto L_0x001d
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            r4.hasMatte = r2
            goto L_0x003e
        L_0x001d:
            java.util.ArrayList r2 = r4.layers
            int r2 = r2.size()
            int r2 = r2 - r1
        L_0x0024:
            if (r2 < 0) goto L_0x0043
            java.util.ArrayList r3 = r4.layers
            java.lang.Object r3 = r3.get(r2)
            com.airbnb.lottie.model.layer.BaseLayer r3 = (com.airbnb.lottie.model.layer.BaseLayer) r3
            java.util.Objects.requireNonNull(r3)
            com.airbnb.lottie.model.layer.BaseLayer r3 = r3.matteLayer
            if (r3 == 0) goto L_0x0037
            r3 = r1
            goto L_0x0038
        L_0x0037:
            r3 = r0
        L_0x0038:
            if (r3 == 0) goto L_0x0040
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            r4.hasMatte = r2
        L_0x003e:
            r4 = r1
            goto L_0x004d
        L_0x0040:
            int r2 = r2 + (-1)
            goto L_0x0024
        L_0x0043:
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            r4.hasMatte = r2
        L_0x0047:
            java.lang.Boolean r4 = r4.hasMatte
            boolean r4 = r4.booleanValue()
        L_0x004d:
            if (r4 == 0) goto L_0x0050
            r0 = r1
        L_0x0050:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieAnimationView.hasMatte():boolean");
    }

    public boolean isAnimating() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        if (lottieValueAnimator == null) {
            return false;
        }
        return lottieValueAnimator.running;
    }

    public boolean isMergePathsEnabledForKitKatAndAbove() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        return lottieDrawable.enableMergePaths;
    }

    @Deprecated
    public void loop(boolean z) {
        int i;
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (z) {
            i = -1;
        } else {
            i = 0;
        }
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.setRepeatCount(i);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        String str = savedState.animationName;
        this.animationName = str;
        if (!TextUtils.isEmpty(str)) {
            setAnimation(this.animationName);
        }
        int i = savedState.animationResId;
        this.animationResId = i;
        if (i != 0) {
            setAnimation(i);
        }
        setProgress(savedState.progress);
        if (savedState.isAnimating) {
            playAnimation();
        }
        LottieDrawable lottieDrawable = this.lottieDrawable;
        String str2 = savedState.imageAssetsFolder;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.imageAssetsFolder = str2;
        setRepeatMode(savedState.repeatMode);
        setRepeatCount(savedState.repeatCount);
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        if (this.isInitialized) {
            if (isShown()) {
                if (this.wasAnimatingWhenNotShown) {
                    resumeAnimation();
                    this.wasAnimatingWhenNotShown = false;
                }
            } else if (isAnimating()) {
                pauseAnimation();
                this.wasAnimatingWhenNotShown = true;
            }
        }
    }

    public void removeAllAnimatorListeners() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.removeAllListeners();
    }

    public void removeAllLottieOnCompositionLoadedListener() {
        this.lottieOnCompositionLoadedListeners.clear();
    }

    public void removeAllUpdateListeners() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.removeAllUpdateListeners();
        lottieDrawable.animator.addUpdateListener(lottieDrawable.progressUpdateListener);
    }

    public void removeAnimatorListener(Animator.AnimatorListener animatorListener) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.removeListener(animatorListener);
    }

    public boolean removeLottieOnCompositionLoadedListener(LottieOnCompositionLoadedListener lottieOnCompositionLoadedListener) {
        return this.lottieOnCompositionLoadedListeners.remove(lottieOnCompositionLoadedListener);
    }

    public void removeUpdateListener(ValueAnimator.AnimatorUpdateListener animatorUpdateListener) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.removeUpdateListener(animatorUpdateListener);
    }

    public List<KeyPath> resolveKeyPath(KeyPath keyPath) {
        return this.lottieDrawable.resolveKeyPath(keyPath);
    }

    public void reverseAnimationSpeed() {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.speed = -lottieValueAnimator.speed;
    }

    public void setAnimationFromJson(String str, String str2) {
        setAnimation(new ByteArrayInputStream(str.getBytes()), str2);
    }

    public void setAnimationFromUrl(final String str) {
        LottieTask<LottieComposition> lottieTask;
        if (this.cacheComposition) {
            final Context context = getContext();
            HashMap hashMap = LottieCompositionFactory.taskCache;
            lottieTask = LottieCompositionFactory.cache(SupportMenuInflater$$ExternalSyntheticOutline0.m("url_", str), new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.1
                /* JADX WARN: Removed duplicated region for block: B:19:0x0068  */
                /* JADX WARN: Removed duplicated region for block: B:32:0x0095  */
                /* JADX WARN: Removed duplicated region for block: B:33:0x009b  */
                @Override // java.util.concurrent.Callable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final com.airbnb.lottie.LottieResult<com.airbnb.lottie.LottieComposition> call() throws java.lang.Exception {
                    /*
                        r9 = this;
                        android.content.Context r0 = r1
                        java.lang.String r9 = r2
                        com.airbnb.lottie.network.NetworkFetcher r1 = new com.airbnb.lottie.network.NetworkFetcher
                        r1.<init>(r0, r9)
                        com.airbnb.lottie.network.FileExtension r9 = com.airbnb.lottie.network.FileExtension.ZIP
                        com.airbnb.lottie.network.NetworkCache r0 = r1.networkCache
                        java.util.Objects.requireNonNull(r0)
                        r2 = 0
                        java.lang.String r3 = r0.url     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r4 = new java.io.File     // Catch: FileNotFoundException -> 0x0064
                        android.content.Context r5 = r0.appContext     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r5 = r5.getCacheDir()     // Catch: FileNotFoundException -> 0x0064
                        com.airbnb.lottie.network.FileExtension r6 = com.airbnb.lottie.network.FileExtension.JSON     // Catch: FileNotFoundException -> 0x0064
                        r7 = 0
                        java.lang.String r8 = com.airbnb.lottie.network.NetworkCache.filenameForUrl(r3, r6, r7)     // Catch: FileNotFoundException -> 0x0064
                        r4.<init>(r5, r8)     // Catch: FileNotFoundException -> 0x0064
                        boolean r5 = r4.exists()     // Catch: FileNotFoundException -> 0x0064
                        if (r5 == 0) goto L_0x002c
                        goto L_0x0043
                    L_0x002c:
                        java.io.File r4 = new java.io.File     // Catch: FileNotFoundException -> 0x0064
                        android.content.Context r0 = r0.appContext     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r0 = r0.getCacheDir()     // Catch: FileNotFoundException -> 0x0064
                        java.lang.String r3 = com.airbnb.lottie.network.NetworkCache.filenameForUrl(r3, r9, r7)     // Catch: FileNotFoundException -> 0x0064
                        r4.<init>(r0, r3)     // Catch: FileNotFoundException -> 0x0064
                        boolean r0 = r4.exists()     // Catch: FileNotFoundException -> 0x0064
                        if (r0 == 0) goto L_0x0042
                        goto L_0x0043
                    L_0x0042:
                        r4 = r2
                    L_0x0043:
                        if (r4 != 0) goto L_0x0046
                        goto L_0x0064
                    L_0x0046:
                        java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: FileNotFoundException -> 0x0064
                        r0.<init>(r4)     // Catch: FileNotFoundException -> 0x0064
                        java.lang.String r3 = r4.getAbsolutePath()
                        java.lang.String r5 = ".zip"
                        boolean r3 = r3.endsWith(r5)
                        if (r3 == 0) goto L_0x0058
                        r6 = r9
                    L_0x0058:
                        r4.getAbsolutePath()
                        com.airbnb.lottie.utils.Logger.debug()
                        androidx.core.util.Pair r3 = new androidx.core.util.Pair
                        r3.<init>(r6, r0)
                        goto L_0x0065
                    L_0x0064:
                        r3 = r2
                    L_0x0065:
                        if (r3 != 0) goto L_0x0068
                        goto L_0x0093
                    L_0x0068:
                        F r0 = r3.first
                        com.airbnb.lottie.network.FileExtension r0 = (com.airbnb.lottie.network.FileExtension) r0
                        S r3 = r3.second
                        java.io.InputStream r3 = (java.io.InputStream) r3
                        if (r0 != r9) goto L_0x0086
                        java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream
                        r9.<init>(r3)
                        java.lang.String r0 = r1.url
                        com.airbnb.lottie.LottieResult r0 = com.airbnb.lottie.LottieCompositionFactory.fromZipStreamSyncInternal(r9, r0)     // Catch: all -> 0x0081
                        com.airbnb.lottie.utils.Utils.closeQuietly(r9)
                        goto L_0x008c
                    L_0x0081:
                        r0 = move-exception
                        com.airbnb.lottie.utils.Utils.closeQuietly(r9)
                        throw r0
                    L_0x0086:
                        java.lang.String r9 = r1.url
                        com.airbnb.lottie.LottieResult r0 = com.airbnb.lottie.LottieCompositionFactory.fromJsonInputStreamSync(r3, r9)
                    L_0x008c:
                        V r9 = r0.value
                        if (r9 == 0) goto L_0x0093
                        r2 = r9
                        com.airbnb.lottie.LottieComposition r2 = (com.airbnb.lottie.LottieComposition) r2
                    L_0x0093:
                        if (r2 == 0) goto L_0x009b
                        com.airbnb.lottie.LottieResult r9 = new com.airbnb.lottie.LottieResult
                        r9.<init>(r2)
                        goto L_0x00aa
                    L_0x009b:
                        com.airbnb.lottie.utils.Logger.debug()
                        com.airbnb.lottie.LottieResult r9 = r1.fetchFromNetworkInternal()     // Catch: IOException -> 0x00a3
                        goto L_0x00aa
                    L_0x00a3:
                        r9 = move-exception
                        com.airbnb.lottie.LottieResult r0 = new com.airbnb.lottie.LottieResult
                        r0.<init>(r9)
                        r9 = r0
                    L_0x00aa:
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieCompositionFactory.AnonymousClass1.call():java.lang.Object");
                }
            });
        } else {
            final Context context2 = getContext();
            lottieTask = LottieCompositionFactory.cache(null, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.1
                @Override // java.util.concurrent.Callable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final com.airbnb.lottie.LottieResult<com.airbnb.lottie.LottieComposition> call() throws java.lang.Exception {
                    /*
                        r9 = this;
                        android.content.Context r0 = r1
                        java.lang.String r9 = r2
                        com.airbnb.lottie.network.NetworkFetcher r1 = new com.airbnb.lottie.network.NetworkFetcher
                        r1.<init>(r0, r9)
                        com.airbnb.lottie.network.FileExtension r9 = com.airbnb.lottie.network.FileExtension.ZIP
                        com.airbnb.lottie.network.NetworkCache r0 = r1.networkCache
                        java.util.Objects.requireNonNull(r0)
                        r2 = 0
                        java.lang.String r3 = r0.url     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r4 = new java.io.File     // Catch: FileNotFoundException -> 0x0064
                        android.content.Context r5 = r0.appContext     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r5 = r5.getCacheDir()     // Catch: FileNotFoundException -> 0x0064
                        com.airbnb.lottie.network.FileExtension r6 = com.airbnb.lottie.network.FileExtension.JSON     // Catch: FileNotFoundException -> 0x0064
                        r7 = 0
                        java.lang.String r8 = com.airbnb.lottie.network.NetworkCache.filenameForUrl(r3, r6, r7)     // Catch: FileNotFoundException -> 0x0064
                        r4.<init>(r5, r8)     // Catch: FileNotFoundException -> 0x0064
                        boolean r5 = r4.exists()     // Catch: FileNotFoundException -> 0x0064
                        if (r5 == 0) goto L_0x002c
                        goto L_0x0043
                    L_0x002c:
                        java.io.File r4 = new java.io.File     // Catch: FileNotFoundException -> 0x0064
                        android.content.Context r0 = r0.appContext     // Catch: FileNotFoundException -> 0x0064
                        java.io.File r0 = r0.getCacheDir()     // Catch: FileNotFoundException -> 0x0064
                        java.lang.String r3 = com.airbnb.lottie.network.NetworkCache.filenameForUrl(r3, r9, r7)     // Catch: FileNotFoundException -> 0x0064
                        r4.<init>(r0, r3)     // Catch: FileNotFoundException -> 0x0064
                        boolean r0 = r4.exists()     // Catch: FileNotFoundException -> 0x0064
                        if (r0 == 0) goto L_0x0042
                        goto L_0x0043
                    L_0x0042:
                        r4 = r2
                    L_0x0043:
                        if (r4 != 0) goto L_0x0046
                        goto L_0x0064
                    L_0x0046:
                        java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: FileNotFoundException -> 0x0064
                        r0.<init>(r4)     // Catch: FileNotFoundException -> 0x0064
                        java.lang.String r3 = r4.getAbsolutePath()
                        java.lang.String r5 = ".zip"
                        boolean r3 = r3.endsWith(r5)
                        if (r3 == 0) goto L_0x0058
                        r6 = r9
                    L_0x0058:
                        r4.getAbsolutePath()
                        com.airbnb.lottie.utils.Logger.debug()
                        androidx.core.util.Pair r3 = new androidx.core.util.Pair
                        r3.<init>(r6, r0)
                        goto L_0x0065
                    L_0x0064:
                        r3 = r2
                    L_0x0065:
                        if (r3 != 0) goto L_0x0068
                        goto L_0x0093
                    L_0x0068:
                        F r0 = r3.first
                        com.airbnb.lottie.network.FileExtension r0 = (com.airbnb.lottie.network.FileExtension) r0
                        S r3 = r3.second
                        java.io.InputStream r3 = (java.io.InputStream) r3
                        if (r0 != r9) goto L_0x0086
                        java.util.zip.ZipInputStream r9 = new java.util.zip.ZipInputStream
                        r9.<init>(r3)
                        java.lang.String r0 = r1.url
                        com.airbnb.lottie.LottieResult r0 = com.airbnb.lottie.LottieCompositionFactory.fromZipStreamSyncInternal(r9, r0)     // Catch: all -> 0x0081
                        com.airbnb.lottie.utils.Utils.closeQuietly(r9)
                        goto L_0x008c
                    L_0x0081:
                        r0 = move-exception
                        com.airbnb.lottie.utils.Utils.closeQuietly(r9)
                        throw r0
                    L_0x0086:
                        java.lang.String r9 = r1.url
                        com.airbnb.lottie.LottieResult r0 = com.airbnb.lottie.LottieCompositionFactory.fromJsonInputStreamSync(r3, r9)
                    L_0x008c:
                        V r9 = r0.value
                        if (r9 == 0) goto L_0x0093
                        r2 = r9
                        com.airbnb.lottie.LottieComposition r2 = (com.airbnb.lottie.LottieComposition) r2
                    L_0x0093:
                        if (r2 == 0) goto L_0x009b
                        com.airbnb.lottie.LottieResult r9 = new com.airbnb.lottie.LottieResult
                        r9.<init>(r2)
                        goto L_0x00aa
                    L_0x009b:
                        com.airbnb.lottie.utils.Logger.debug()
                        com.airbnb.lottie.LottieResult r9 = r1.fetchFromNetworkInternal()     // Catch: IOException -> 0x00a3
                        goto L_0x00aa
                    L_0x00a3:
                        r9 = move-exception
                        com.airbnb.lottie.LottieResult r0 = new com.airbnb.lottie.LottieResult
                        r0.<init>(r9)
                        r9 = r0
                    L_0x00aa:
                        return r9
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieCompositionFactory.AnonymousClass1.call():java.lang.Object");
                }
            });
        }
        setCompositionTask(lottieTask);
    }

    public void setApplyingOpacityToLayersEnabled(boolean z) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.isApplyingOpacityToLayersEnabled = z;
    }

    public void setComposition(LottieComposition lottieComposition) {
        this.lottieDrawable.setCallback(this);
        this.composition = lottieComposition;
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        boolean z = false;
        if (lottieDrawable.composition != lottieComposition) {
            lottieDrawable.isDirty = false;
            lottieDrawable.clearComposition();
            lottieDrawable.composition = lottieComposition;
            lottieDrawable.buildCompositionLayer();
            LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
            Objects.requireNonNull(lottieValueAnimator);
            if (lottieValueAnimator.composition == null) {
                z = true;
            }
            lottieValueAnimator.composition = lottieComposition;
            if (z) {
                float f = lottieValueAnimator.minFrame;
                Objects.requireNonNull(lottieComposition);
                lottieValueAnimator.setMinAndMaxFrames((int) Math.max(f, lottieComposition.startFrame), (int) Math.min(lottieValueAnimator.maxFrame, lottieComposition.endFrame));
            } else {
                Objects.requireNonNull(lottieComposition);
                lottieValueAnimator.setMinAndMaxFrames((int) lottieComposition.startFrame, (int) lottieComposition.endFrame);
            }
            float f2 = lottieValueAnimator.frame;
            lottieValueAnimator.frame = 0.0f;
            lottieValueAnimator.setFrame((int) f2);
            lottieDrawable.setProgress(lottieDrawable.animator.getAnimatedFraction());
            lottieDrawable.scale = lottieDrawable.scale;
            lottieDrawable.updateBounds();
            lottieDrawable.updateBounds();
            Iterator it = new ArrayList(lottieDrawable.lazyCompositionTasks).iterator();
            while (it.hasNext()) {
                ((LottieDrawable.LazyCompositionTask) it.next()).run();
                it.remove();
            }
            lottieDrawable.lazyCompositionTasks.clear();
            boolean z2 = lottieDrawable.performanceTrackingEnabled;
            Objects.requireNonNull(lottieComposition);
            PerformanceTracker performanceTracker = lottieComposition.performanceTracker;
            Objects.requireNonNull(performanceTracker);
            performanceTracker.enabled = z2;
            z = true;
        }
        enableOrDisableHardwareLayer();
        if (getDrawable() != this.lottieDrawable || z) {
            setImageDrawable(null);
            setImageDrawable(this.lottieDrawable);
            onVisibilityChanged(this, getVisibility());
            requestLayout();
            Iterator it2 = this.lottieOnCompositionLoadedListeners.iterator();
            while (it2.hasNext()) {
                ((LottieOnCompositionLoadedListener) it2.next()).onCompositionLoaded(lottieComposition);
            }
        }
    }

    public void setFontAssetDelegate(FontAssetDelegate fontAssetDelegate) {
        Objects.requireNonNull(this.lottieDrawable);
    }

    public void setFrame(int i) {
        this.lottieDrawable.setFrame(i);
    }

    public void setImageAssetDelegate(ImageAssetDelegate imageAssetDelegate) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.imageAssetDelegate = imageAssetDelegate;
        ImageAssetManager imageAssetManager = lottieDrawable.imageAssetManager;
        if (imageAssetManager != null) {
            imageAssetManager.delegate = imageAssetDelegate;
        }
    }

    public void setImageAssetsFolder(String str) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.imageAssetsFolder = str;
    }

    public void setMaxFrame(String str) {
        this.lottieDrawable.setMaxFrame(str);
    }

    public void setMaxProgress(float f) {
        this.lottieDrawable.setMaxProgress(f);
    }

    public void setMinAndMaxFrame(String str, String str2, boolean z) {
        this.lottieDrawable.setMinAndMaxFrame(str, str2, z);
    }

    public void setMinAndMaxProgress(float f, float f2) {
        this.lottieDrawable.setMinAndMaxProgress(f, f2);
    }

    public void setMinFrame(String str) {
        this.lottieDrawable.setMinFrame(str);
    }

    public void setMinProgress(float f) {
        this.lottieDrawable.setMinProgress(f);
    }

    public void setPerformanceTrackingEnabled(boolean z) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.performanceTrackingEnabled = z;
        LottieComposition lottieComposition = lottieDrawable.composition;
        if (lottieComposition != null) {
            PerformanceTracker performanceTracker = lottieComposition.performanceTracker;
            Objects.requireNonNull(performanceTracker);
            performanceTracker.enabled = z;
        }
    }

    public void setProgress(float f) {
        this.lottieDrawable.setProgress(f);
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
        enableOrDisableHardwareLayer();
    }

    public void setRepeatCount(int i) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.setRepeatCount(i);
    }

    public void setRepeatMode(int i) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.animator.setRepeatMode(i);
    }

    public void setSafeMode(boolean z) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.safeMode = z;
    }

    public void setScale(float f) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.scale = f;
        lottieDrawable.updateBounds();
        if (getDrawable() == this.lottieDrawable) {
            setImageDrawable(null);
            setImageDrawable(this.lottieDrawable);
        }
    }

    public void setSpeed(float f) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        LottieValueAnimator lottieValueAnimator = lottieDrawable.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.speed = f;
    }

    public void setTextDelegate(TextDelegate textDelegate) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        lottieDrawable.textDelegate = textDelegate;
    }

    public Bitmap updateBitmap(String str, Bitmap bitmap) {
        LottieDrawable lottieDrawable = this.lottieDrawable;
        Objects.requireNonNull(lottieDrawable);
        ImageAssetManager imageAssetManager = lottieDrawable.getImageAssetManager();
        Bitmap bitmap2 = null;
        if (imageAssetManager == null) {
            Logger.warning("Cannot update bitmap. Most likely the drawable is not added to a View which prevents Lottie from getting a Context.");
        } else {
            if (bitmap == null) {
                LottieImageAsset lottieImageAsset = imageAssetManager.imageAssets.get(str);
                Objects.requireNonNull(lottieImageAsset);
                Bitmap bitmap3 = lottieImageAsset.bitmap;
                lottieImageAsset.bitmap = null;
                bitmap2 = bitmap3;
            } else {
                LottieImageAsset lottieImageAsset2 = imageAssetManager.imageAssets.get(str);
                Objects.requireNonNull(lottieImageAsset2);
                bitmap2 = lottieImageAsset2.bitmap;
                imageAssetManager.putBitmap(str, bitmap);
            }
            lottieDrawable.invalidateSelf();
        }
        return bitmap2;
    }

    public final void init(AttributeSet attributeSet) {
        String string;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView);
        boolean z = false;
        if (!isInEditMode()) {
            this.cacheComposition = obtainStyledAttributes.getBoolean(1, true);
            boolean hasValue = obtainStyledAttributes.hasValue(9);
            boolean hasValue2 = obtainStyledAttributes.hasValue(5);
            boolean hasValue3 = obtainStyledAttributes.hasValue(15);
            if (!hasValue || !hasValue2) {
                if (hasValue) {
                    int resourceId = obtainStyledAttributes.getResourceId(9, 0);
                    if (resourceId != 0) {
                        setAnimation(resourceId);
                    }
                } else if (hasValue2) {
                    String string2 = obtainStyledAttributes.getString(5);
                    if (string2 != null) {
                        setAnimation(string2);
                    }
                } else if (hasValue3 && (string = obtainStyledAttributes.getString(15)) != null) {
                    setAnimationFromUrl(string);
                }
                setFallbackResource(obtainStyledAttributes.getResourceId(4, 0));
            } else {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            }
        }
        if (obtainStyledAttributes.getBoolean(0, false)) {
            this.wasAnimatingWhenDetached = true;
            this.autoPlay = true;
        }
        if (obtainStyledAttributes.getBoolean(7, false)) {
            LottieDrawable lottieDrawable = this.lottieDrawable;
            Objects.requireNonNull(lottieDrawable);
            lottieDrawable.animator.setRepeatCount(-1);
        }
        if (obtainStyledAttributes.hasValue(12)) {
            setRepeatMode(obtainStyledAttributes.getInt(12, 1));
        }
        if (obtainStyledAttributes.hasValue(11)) {
            setRepeatCount(obtainStyledAttributes.getInt(11, -1));
        }
        if (obtainStyledAttributes.hasValue(14)) {
            setSpeed(obtainStyledAttributes.getFloat(14, 1.0f));
        }
        setImageAssetsFolder(obtainStyledAttributes.getString(6));
        setProgress(obtainStyledAttributes.getFloat(8, 0.0f));
        enableMergePathsForKitKatAndAbove(obtainStyledAttributes.getBoolean(3, false));
        if (obtainStyledAttributes.hasValue(2)) {
            addValueCallback(new KeyPath("**"), (KeyPath) LottieProperty.COLOR_FILTER, (LottieValueCallback<KeyPath>) new LottieValueCallback(new SimpleColorFilter(obtainStyledAttributes.getColor(2, 0))));
        }
        if (obtainStyledAttributes.hasValue(13)) {
            LottieDrawable lottieDrawable2 = this.lottieDrawable;
            float f = obtainStyledAttributes.getFloat(13, 1.0f);
            Objects.requireNonNull(lottieDrawable2);
            lottieDrawable2.scale = f;
            lottieDrawable2.updateBounds();
        }
        if (obtainStyledAttributes.hasValue(10)) {
            int i = obtainStyledAttributes.getInt(10, 0);
            if (i >= RenderMode.values().length) {
                i = 0;
            }
            setRenderMode(RenderMode.values()[i]);
        }
        if (getScaleType() != null) {
            LottieDrawable lottieDrawable3 = this.lottieDrawable;
            ImageView.ScaleType scaleType = getScaleType();
            Objects.requireNonNull(lottieDrawable3);
            lottieDrawable3.scaleType = scaleType;
        }
        obtainStyledAttributes.recycle();
        LottieDrawable lottieDrawable4 = this.lottieDrawable;
        Context context = getContext();
        PathMeasure pathMeasure = Utils.pathMeasure;
        if (Settings.Global.getFloat(context.getContentResolver(), "animator_duration_scale", 1.0f) != 0.0f) {
            z = true;
        }
        Boolean valueOf = Boolean.valueOf(z);
        Objects.requireNonNull(lottieDrawable4);
        lottieDrawable4.systemAnimationsEnabled = valueOf.booleanValue();
        enableOrDisableHardwareLayer();
        this.isInitialized = true;
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable drawable2 = getDrawable();
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (drawable2 == lottieDrawable) {
            super.invalidateDrawable(lottieDrawable);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.autoPlay || this.wasAnimatingWhenDetached) {
            playAnimation();
            this.autoPlay = false;
            this.wasAnimatingWhenDetached = false;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDetachedFromWindow() {
        if (isAnimating()) {
            cancelAnimation();
            this.wasAnimatingWhenDetached = true;
        }
        super.onDetachedFromWindow();
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0038, code lost:
        if (r3.wasAnimatingWhenDetached != false) goto L_0x003a;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.os.Parcelable onSaveInstanceState() {
        /*
            r3 = this;
            android.os.Parcelable r0 = super.onSaveInstanceState()
            com.airbnb.lottie.LottieAnimationView$SavedState r1 = new com.airbnb.lottie.LottieAnimationView$SavedState
            r1.<init>(r0)
            java.lang.String r0 = r3.animationName
            r1.animationName = r0
            int r0 = r3.animationResId
            r1.animationResId = r0
            com.airbnb.lottie.LottieDrawable r0 = r3.lottieDrawable
            java.util.Objects.requireNonNull(r0)
            com.airbnb.lottie.utils.LottieValueAnimator r0 = r0.animator
            float r0 = r0.getAnimatedValueAbsolute()
            r1.progress = r0
            com.airbnb.lottie.LottieDrawable r0 = r3.lottieDrawable
            java.util.Objects.requireNonNull(r0)
            com.airbnb.lottie.utils.LottieValueAnimator r0 = r0.animator
            r2 = 0
            if (r0 != 0) goto L_0x002a
            r0 = r2
            goto L_0x002c
        L_0x002a:
            boolean r0 = r0.running
        L_0x002c:
            if (r0 != 0) goto L_0x003a
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r0 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            boolean r0 = androidx.core.view.ViewCompat.Api19Impl.isAttachedToWindow(r3)
            if (r0 != 0) goto L_0x003b
            boolean r0 = r3.wasAnimatingWhenDetached
            if (r0 == 0) goto L_0x003b
        L_0x003a:
            r2 = 1
        L_0x003b:
            r1.isAnimating = r2
            com.airbnb.lottie.LottieDrawable r0 = r3.lottieDrawable
            java.util.Objects.requireNonNull(r0)
            java.lang.String r0 = r0.imageAssetsFolder
            r1.imageAssetsFolder = r0
            com.airbnb.lottie.LottieDrawable r0 = r3.lottieDrawable
            java.util.Objects.requireNonNull(r0)
            com.airbnb.lottie.utils.LottieValueAnimator r0 = r0.animator
            int r0 = r0.getRepeatMode()
            r1.repeatMode = r0
            com.airbnb.lottie.LottieDrawable r3 = r3.lottieDrawable
            java.util.Objects.requireNonNull(r3)
            com.airbnb.lottie.utils.LottieValueAnimator r3 = r3.animator
            int r3 = r3.getRepeatCount()
            r1.repeatCount = r3
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieAnimationView.onSaveInstanceState():android.os.Parcelable");
    }

    public void playAnimation() {
        if (isShown()) {
            this.lottieDrawable.playAnimation();
            enableOrDisableHardwareLayer();
            return;
        }
        this.wasAnimatingWhenNotShown = true;
    }

    public void resumeAnimation() {
        if (isShown()) {
            this.lottieDrawable.resumeAnimation();
            enableOrDisableHardwareLayer();
            return;
        }
        this.wasAnimatingWhenNotShown = true;
    }

    public void setAnimation(final int i) {
        LottieTask<LottieComposition> lottieTask;
        this.animationResId = i;
        this.animationName = null;
        if (this.cacheComposition) {
            Context context = getContext();
            String rawResCacheKey = LottieCompositionFactory.rawResCacheKey(context, i);
            final WeakReference weakReference = new WeakReference(context);
            final Context applicationContext = context.getApplicationContext();
            lottieTask = LottieCompositionFactory.cache(rawResCacheKey, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.3
                @Override // java.util.concurrent.Callable
                public final LottieResult<LottieComposition> call() throws Exception {
                    Context context2 = (Context) weakReference.get();
                    if (context2 == null) {
                        context2 = applicationContext;
                    }
                    int i2 = i;
                    try {
                        return LottieCompositionFactory.fromJsonInputStreamSync(context2.getResources().openRawResource(i2), LottieCompositionFactory.rawResCacheKey(context2, i2));
                    } catch (Resources.NotFoundException e) {
                        return new LottieResult<>(e);
                    }
                }
            });
        } else {
            Context context2 = getContext();
            HashMap hashMap = LottieCompositionFactory.taskCache;
            final WeakReference weakReference2 = new WeakReference(context2);
            final Context applicationContext2 = context2.getApplicationContext();
            lottieTask = LottieCompositionFactory.cache(null, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.3
                @Override // java.util.concurrent.Callable
                public final LottieResult<LottieComposition> call() throws Exception {
                    Context context22 = (Context) weakReference2.get();
                    if (context22 == null) {
                        context22 = applicationContext2;
                    }
                    int i2 = i;
                    try {
                        return LottieCompositionFactory.fromJsonInputStreamSync(context22.getResources().openRawResource(i2), LottieCompositionFactory.rawResCacheKey(context22, i2));
                    } catch (Resources.NotFoundException e) {
                        return new LottieResult<>(e);
                    }
                }
            });
        }
        setCompositionTask(lottieTask);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        cancelLoaderTask();
        super.setImageBitmap(bitmap);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        cancelLoaderTask();
        super.setImageDrawable(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i) {
        cancelLoaderTask();
        super.setImageResource(i);
    }

    public void setMinAndMaxFrame(int i, int i2) {
        this.lottieDrawable.setMinAndMaxFrame(i, i2);
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        super.setScaleType(scaleType);
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (lottieDrawable != null) {
            Objects.requireNonNull(lottieDrawable);
            lottieDrawable.scaleType = scaleType;
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.airbnb.lottie.LottieAnimationView$2] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.airbnb.lottie.LottieAnimationView$3] */
    public LottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public void setAnimation(final String str) {
        LottieTask<LottieComposition> lottieTask;
        this.animationName = str;
        this.animationResId = 0;
        if (this.cacheComposition) {
            Context context = getContext();
            HashMap hashMap = LottieCompositionFactory.taskCache;
            final String m = SupportMenuInflater$$ExternalSyntheticOutline0.m("asset_", str);
            final Context applicationContext = context.getApplicationContext();
            lottieTask = LottieCompositionFactory.cache(m, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.2
                @Override // java.util.concurrent.Callable
                public final LottieResult<LottieComposition> call() throws Exception {
                    LottieResult<LottieComposition> lottieResult;
                    Context context2 = applicationContext;
                    String str2 = str;
                    String str3 = m;
                    try {
                        if (str2.endsWith(".zip")) {
                            ZipInputStream zipInputStream = new ZipInputStream(context2.getAssets().open(str2));
                            lottieResult = LottieCompositionFactory.fromZipStreamSyncInternal(zipInputStream, str3);
                            Utils.closeQuietly(zipInputStream);
                        } else {
                            lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(context2.getAssets().open(str2), str3);
                        }
                        return lottieResult;
                    } catch (IOException e) {
                        return new LottieResult<>(e);
                    }
                }
            });
        } else {
            Context context2 = getContext();
            HashMap hashMap2 = LottieCompositionFactory.taskCache;
            final Context applicationContext2 = context2.getApplicationContext();
            lottieTask = LottieCompositionFactory.cache(null, new Callable<LottieResult<LottieComposition>>() { // from class: com.airbnb.lottie.LottieCompositionFactory.2
                @Override // java.util.concurrent.Callable
                public final LottieResult<LottieComposition> call() throws Exception {
                    LottieResult<LottieComposition> lottieResult;
                    Context context22 = applicationContext2;
                    String str2 = str;
                    String str3 = m;
                    try {
                        if (str2.endsWith(".zip")) {
                            ZipInputStream zipInputStream = new ZipInputStream(context22.getAssets().open(str2));
                            lottieResult = LottieCompositionFactory.fromZipStreamSyncInternal(zipInputStream, str3);
                            Utils.closeQuietly(zipInputStream);
                        } else {
                            lottieResult = LottieCompositionFactory.fromJsonInputStreamSync(context22.getAssets().open(str2), str3);
                        }
                        return lottieResult;
                    } catch (IOException e) {
                        return new LottieResult<>(e);
                    }
                }
            });
        }
        setCompositionTask(lottieTask);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.airbnb.lottie.LottieAnimationView$2] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.airbnb.lottie.LottieAnimationView$3] */
    public LottieAnimationView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public void setCacheComposition(boolean z) {
        this.cacheComposition = z;
    }

    public void setFailureListener(LottieListener<Throwable> lottieListener) {
        this.failureListener = lottieListener;
    }

    public void setFallbackResource(int i) {
        this.fallbackResource = i;
    }

    public LottieComposition getComposition() {
        return this.composition;
    }
}
