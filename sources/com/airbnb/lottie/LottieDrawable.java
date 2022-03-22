package com.airbnb.lottie;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.Choreographer;
import android.view.View;
import android.widget.ImageView;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.airbnb.lottie.manager.FontAssetManager;
import com.airbnb.lottie.manager.ImageAssetManager;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.model.KeyPathElement;
import com.airbnb.lottie.model.Marker;
import com.airbnb.lottie.model.animatable.AnimatableTransform;
import com.airbnb.lottie.model.layer.CompositionLayer;
import com.airbnb.lottie.model.layer.Layer;
import com.airbnb.lottie.parser.LayerParser;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.LottieValueAnimator;
import com.airbnb.lottie.utils.MiscUtils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LottieDrawable extends Drawable implements Drawable.Callback, Animatable {
    public final LottieValueAnimator animator;
    public LottieComposition composition;
    public CompositionLayer compositionLayer;
    public boolean enableMergePaths;
    public FontAssetManager fontAssetManager;
    public ImageAssetDelegate imageAssetDelegate;
    public ImageAssetManager imageAssetManager;
    public String imageAssetsFolder;
    public boolean isApplyingOpacityToLayersEnabled;
    public boolean performanceTrackingEnabled;
    public final AnonymousClass1 progressUpdateListener;
    public ImageView.ScaleType scaleType;
    public TextDelegate textDelegate;
    public final Matrix matrix = new Matrix();
    public float scale = 1.0f;
    public boolean systemAnimationsEnabled = true;
    public boolean safeMode = false;
    public final ArrayList<LazyCompositionTask> lazyCompositionTasks = new ArrayList<>();
    public int alpha = 255;
    public boolean isExtraScaleEnabled = true;
    public boolean isDirty = false;

    /* loaded from: classes.dex */
    public interface LazyCompositionTask {
        void run();
    }

    @Override // android.graphics.drawable.Drawable
    public final void draw(Canvas canvas) {
        this.isDirty = false;
        if (this.safeMode) {
            try {
                drawInternal(canvas);
            } catch (Throwable unused) {
                Objects.requireNonNull(Logger.INSTANCE);
            }
        } else {
            drawInternal(canvas);
        }
        L.endSection();
    }

    @Override // android.graphics.drawable.Drawable
    public final int getOpacity() {
        return -3;
    }

    public final void setMaxFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.6
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMaxFrame(i);
                }
            });
            return;
        }
        LottieValueAnimator lottieValueAnimator = this.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.setMinAndMaxFrames(lottieValueAnimator.minFrame, i + 0.99f);
    }

    public final void setMinAndMaxFrame(final String str) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.10
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinAndMaxFrame(str);
                }
            });
            return;
        }
        Marker marker = lottieComposition.getMarker(str);
        if (marker != null) {
            int i = (int) marker.startFrame;
            setMinAndMaxFrame(i, ((int) marker.durationFrames) + i);
            return;
        }
        throw new IllegalArgumentException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Cannot find marker with name ", str, "."));
    }

    public final void setMinFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.4
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinFrame(i);
                }
            });
            return;
        }
        LottieValueAnimator lottieValueAnimator = this.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.setMinAndMaxFrames(i, (int) lottieValueAnimator.maxFrame);
    }

    public final <T> void addValueCallback(final KeyPath keyPath, final T t, final LottieValueCallback<T> lottieValueCallback) {
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.16
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.addValueCallback(keyPath, t, lottieValueCallback);
                }
            });
            return;
        }
        Objects.requireNonNull(keyPath);
        KeyPathElement keyPathElement = keyPath.resolvedElement;
        boolean z = true;
        if (keyPathElement != null) {
            keyPathElement.addValueCallback(t, lottieValueCallback);
        } else {
            List<KeyPath> resolveKeyPath = resolveKeyPath(keyPath);
            for (int i = 0; i < resolveKeyPath.size(); i++) {
                KeyPath keyPath2 = resolveKeyPath.get(i);
                Objects.requireNonNull(keyPath2);
                keyPath2.resolvedElement.addValueCallback(t, lottieValueCallback);
            }
            z = true ^ resolveKeyPath.isEmpty();
        }
        if (z) {
            invalidateSelf();
            if (t == LottieProperty.TIME_REMAP) {
                setProgress(this.animator.getAnimatedValueAbsolute());
            }
        }
    }

    public final void buildCompositionLayer() {
        LottieComposition lottieComposition = this.composition;
        JsonReader.Options options = LayerParser.NAMES;
        Objects.requireNonNull(lottieComposition);
        Rect rect = lottieComposition.bounds;
        Layer layer = new Layer(Collections.emptyList(), lottieComposition, "__container", -1L, Layer.LayerType.PRE_COMP, -1L, null, Collections.emptyList(), new AnimatableTransform(), 0, 0, 0, 0.0f, 0.0f, rect.width(), rect.height(), null, null, Collections.emptyList(), Layer.MatteType.NONE, null, false);
        LottieComposition lottieComposition2 = this.composition;
        Objects.requireNonNull(lottieComposition2);
        this.compositionLayer = new CompositionLayer(this, layer, lottieComposition2.layers, this.composition);
    }

    public final void clearComposition() {
        LottieValueAnimator lottieValueAnimator = this.animator;
        Objects.requireNonNull(lottieValueAnimator);
        if (lottieValueAnimator.running) {
            this.animator.cancel();
        }
        this.composition = null;
        this.compositionLayer = null;
        this.imageAssetManager = null;
        LottieValueAnimator lottieValueAnimator2 = this.animator;
        Objects.requireNonNull(lottieValueAnimator2);
        lottieValueAnimator2.composition = null;
        lottieValueAnimator2.minFrame = -2.14748365E9f;
        lottieValueAnimator2.maxFrame = 2.14748365E9f;
        invalidateSelf();
    }

    public final void drawInternal(Canvas canvas) {
        LottieComposition lottieComposition;
        LottieComposition lottieComposition2;
        float f;
        LottieComposition lottieComposition3;
        LottieComposition lottieComposition4;
        LottieComposition lottieComposition5;
        LottieComposition lottieComposition6;
        float f2;
        int i = -1;
        if (ImageView.ScaleType.FIT_XY == this.scaleType) {
            if (this.compositionLayer != null) {
                Rect bounds = getBounds();
                Objects.requireNonNull(this.composition);
                float width = bounds.width() / lottieComposition5.bounds.width();
                Objects.requireNonNull(this.composition);
                float height = bounds.height() / lottieComposition6.bounds.height();
                if (this.isExtraScaleEnabled) {
                    float min = Math.min(width, height);
                    if (min < 1.0f) {
                        f2 = 1.0f / min;
                        width /= f2;
                        height /= f2;
                    } else {
                        f2 = 1.0f;
                    }
                    if (f2 > 1.0f) {
                        i = canvas.save();
                        float width2 = bounds.width() / 2.0f;
                        float height2 = bounds.height() / 2.0f;
                        float f3 = width2 * min;
                        float f4 = min * height2;
                        canvas.translate(width2 - f3, height2 - f4);
                        canvas.scale(f2, f2, f3, f4);
                    }
                }
                this.matrix.reset();
                this.matrix.preScale(width, height);
                this.compositionLayer.draw(canvas, this.matrix, this.alpha);
                if (i > 0) {
                    canvas.restoreToCount(i);
                }
            }
        } else if (this.compositionLayer != null) {
            float f5 = this.scale;
            Objects.requireNonNull(this.composition);
            Objects.requireNonNull(this.composition);
            float min2 = Math.min(canvas.getWidth() / lottieComposition.bounds.width(), canvas.getHeight() / lottieComposition2.bounds.height());
            if (f5 > min2) {
                f = this.scale / min2;
            } else {
                min2 = f5;
                f = 1.0f;
            }
            if (f > 1.0f) {
                i = canvas.save();
                Objects.requireNonNull(this.composition);
                float width3 = lottieComposition3.bounds.width() / 2.0f;
                Objects.requireNonNull(this.composition);
                float height3 = lottieComposition4.bounds.height() / 2.0f;
                float f6 = width3 * min2;
                float f7 = height3 * min2;
                float f8 = this.scale;
                canvas.translate((width3 * f8) - f6, (f8 * height3) - f7);
                canvas.scale(f, f, f6, f7);
            }
            this.matrix.reset();
            this.matrix.preScale(min2, min2);
            this.compositionLayer.draw(canvas, this.matrix, this.alpha);
            if (i > 0) {
                canvas.restoreToCount(i);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicHeight() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return -1;
        }
        Objects.requireNonNull(lottieComposition);
        return (int) (lottieComposition.bounds.height() * this.scale);
    }

    @Override // android.graphics.drawable.Drawable
    public final int getIntrinsicWidth() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            return -1;
        }
        Objects.requireNonNull(lottieComposition);
        return (int) (lottieComposition.bounds.width() * this.scale);
    }

    @Override // android.graphics.drawable.Drawable
    public final void invalidateSelf() {
        if (!this.isDirty) {
            this.isDirty = true;
            Drawable.Callback callback = getCallback();
            if (callback != null) {
                callback.invalidateDrawable(this);
            }
        }
    }

    @Override // android.graphics.drawable.Animatable
    public final boolean isRunning() {
        LottieValueAnimator lottieValueAnimator = this.animator;
        if (lottieValueAnimator == null) {
            return false;
        }
        return lottieValueAnimator.running;
    }

    public final void playAnimation() {
        float f;
        float f2;
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.2
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.playAnimation();
                }
            });
            return;
        }
        if (this.systemAnimationsEnabled || this.animator.getRepeatCount() == 0) {
            LottieValueAnimator lottieValueAnimator = this.animator;
            Objects.requireNonNull(lottieValueAnimator);
            lottieValueAnimator.running = true;
            boolean isReversed = lottieValueAnimator.isReversed();
            Iterator it = lottieValueAnimator.listeners.iterator();
            while (it.hasNext()) {
                ((Animator.AnimatorListener) it.next()).onAnimationStart(lottieValueAnimator, isReversed);
            }
            if (lottieValueAnimator.isReversed()) {
                f2 = lottieValueAnimator.getMaxFrame();
            } else {
                f2 = lottieValueAnimator.getMinFrame();
            }
            lottieValueAnimator.setFrame((int) f2);
            lottieValueAnimator.lastFrameTimeNs = 0L;
            lottieValueAnimator.repeatCount = 0;
            if (lottieValueAnimator.running) {
                lottieValueAnimator.removeFrameCallback(false);
                Choreographer.getInstance().postFrameCallback(lottieValueAnimator);
            }
        }
        if (!this.systemAnimationsEnabled) {
            LottieValueAnimator lottieValueAnimator2 = this.animator;
            Objects.requireNonNull(lottieValueAnimator2);
            if (lottieValueAnimator2.speed < 0.0f) {
                f = this.animator.getMinFrame();
            } else {
                f = this.animator.getMaxFrame();
            }
            setFrame((int) f);
            LottieValueAnimator lottieValueAnimator3 = this.animator;
            Objects.requireNonNull(lottieValueAnimator3);
            lottieValueAnimator3.removeFrameCallback(true);
            boolean isReversed2 = lottieValueAnimator3.isReversed();
            Iterator it2 = lottieValueAnimator3.listeners.iterator();
            while (it2.hasNext()) {
                ((Animator.AnimatorListener) it2.next()).onAnimationEnd(lottieValueAnimator3, isReversed2);
            }
        }
    }

    public final List<KeyPath> resolveKeyPath(KeyPath keyPath) {
        if (this.compositionLayer == null) {
            Logger.warning("Cannot resolve KeyPath. Composition is not set yet.");
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        this.compositionLayer.resolveKeyPath(keyPath, 0, arrayList, new KeyPath(new String[0]));
        return arrayList;
    }

    public final void resumeAnimation() {
        float f;
        if (this.compositionLayer == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.3
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.resumeAnimation();
                }
            });
            return;
        }
        if (this.systemAnimationsEnabled || this.animator.getRepeatCount() == 0) {
            LottieValueAnimator lottieValueAnimator = this.animator;
            Objects.requireNonNull(lottieValueAnimator);
            lottieValueAnimator.running = true;
            lottieValueAnimator.removeFrameCallback(false);
            Choreographer.getInstance().postFrameCallback(lottieValueAnimator);
            lottieValueAnimator.lastFrameTimeNs = 0L;
            if (lottieValueAnimator.isReversed() && lottieValueAnimator.frame == lottieValueAnimator.getMinFrame()) {
                lottieValueAnimator.frame = lottieValueAnimator.getMaxFrame();
            } else if (!lottieValueAnimator.isReversed() && lottieValueAnimator.frame == lottieValueAnimator.getMaxFrame()) {
                lottieValueAnimator.frame = lottieValueAnimator.getMinFrame();
            }
        }
        if (!this.systemAnimationsEnabled) {
            LottieValueAnimator lottieValueAnimator2 = this.animator;
            Objects.requireNonNull(lottieValueAnimator2);
            if (lottieValueAnimator2.speed < 0.0f) {
                f = this.animator.getMinFrame();
            } else {
                f = this.animator.getMaxFrame();
            }
            setFrame((int) f);
            LottieValueAnimator lottieValueAnimator3 = this.animator;
            Objects.requireNonNull(lottieValueAnimator3);
            lottieValueAnimator3.removeFrameCallback(true);
            boolean isReversed = lottieValueAnimator3.isReversed();
            Iterator it = lottieValueAnimator3.listeners.iterator();
            while (it.hasNext()) {
                ((Animator.AnimatorListener) it.next()).onAnimationEnd(lottieValueAnimator3, isReversed);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final void setAlpha(int i) {
        this.alpha = i;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public final void setColorFilter(ColorFilter colorFilter) {
        Logger.warning("Use addColorFilter instead.");
    }

    public final void setFrame(final int i) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.14
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setFrame(i);
                }
            });
        } else {
            this.animator.setFrame(i);
        }
    }

    public final void setMaxProgress(final float f) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.7
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMaxProgress(f);
                }
            });
            return;
        }
        float f2 = lottieComposition.startFrame;
        Objects.requireNonNull(lottieComposition);
        float f3 = lottieComposition.endFrame;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        setMaxFrame((int) MotionController$$ExternalSyntheticOutline0.m(f3, f2, f, f2));
    }

    public final void setMinAndMaxProgress(final float f, final float f2) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.13
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinAndMaxProgress(f, f2);
                }
            });
            return;
        }
        float f3 = lottieComposition.startFrame;
        Objects.requireNonNull(lottieComposition);
        float f4 = lottieComposition.endFrame;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        LottieComposition lottieComposition2 = this.composition;
        Objects.requireNonNull(lottieComposition2);
        float f5 = lottieComposition2.startFrame;
        LottieComposition lottieComposition3 = this.composition;
        Objects.requireNonNull(lottieComposition3);
        setMinAndMaxFrame((int) MotionController$$ExternalSyntheticOutline0.m(f4, f3, f, f3), (int) MotionController$$ExternalSyntheticOutline0.m(lottieComposition3.endFrame, f5, f2, f5));
    }

    public final void setMinProgress(final float f) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.5
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinProgress(f);
                }
            });
            return;
        }
        float f2 = lottieComposition.startFrame;
        Objects.requireNonNull(lottieComposition);
        float f3 = lottieComposition.endFrame;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        setMinFrame((int) MotionController$$ExternalSyntheticOutline0.m(f3, f2, f, f2));
    }

    public final void setProgress(final float f) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.15
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setProgress(f);
                }
            });
            return;
        }
        LottieValueAnimator lottieValueAnimator = this.animator;
        float f2 = lottieComposition.startFrame;
        Objects.requireNonNull(lottieComposition);
        float f3 = lottieComposition.endFrame;
        PointF pointF = MiscUtils.pathFromDataCurrentPoint;
        lottieValueAnimator.setFrame(((f3 - f2) * f) + f2);
        L.endSection();
    }

    @Override // android.graphics.drawable.Animatable
    public final void stop() {
        this.lazyCompositionTasks.clear();
        LottieValueAnimator lottieValueAnimator = this.animator;
        Objects.requireNonNull(lottieValueAnimator);
        lottieValueAnimator.removeFrameCallback(true);
        boolean isReversed = lottieValueAnimator.isReversed();
        Iterator it = lottieValueAnimator.listeners.iterator();
        while (it.hasNext()) {
            ((Animator.AnimatorListener) it.next()).onAnimationEnd(lottieValueAnimator, isReversed);
        }
    }

    public final void updateBounds() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition != null) {
            float f = this.scale;
            Objects.requireNonNull(lottieComposition);
            LottieComposition lottieComposition2 = this.composition;
            Objects.requireNonNull(lottieComposition2);
            setBounds(0, 0, (int) (lottieComposition.bounds.width() * f), (int) (lottieComposition2.bounds.height() * f));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v2, types: [android.animation.ValueAnimator$AnimatorUpdateListener, com.airbnb.lottie.LottieDrawable$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public LottieDrawable() {
        /*
            r5 = this;
            r5.<init>()
            android.graphics.Matrix r0 = new android.graphics.Matrix
            r0.<init>()
            r5.matrix = r0
            com.airbnb.lottie.utils.LottieValueAnimator r0 = new com.airbnb.lottie.utils.LottieValueAnimator
            r0.<init>()
            r5.animator = r0
            r1 = 1065353216(0x3f800000, float:1.0)
            r5.scale = r1
            r1 = 1
            r5.systemAnimationsEnabled = r1
            r2 = 0
            r5.safeMode = r2
            java.util.HashSet r3 = new java.util.HashSet
            r3.<init>()
            java.util.ArrayList r3 = new java.util.ArrayList
            r3.<init>()
            r5.lazyCompositionTasks = r3
            com.airbnb.lottie.LottieDrawable$1 r3 = new com.airbnb.lottie.LottieDrawable$1
            r3.<init>()
            r5.progressUpdateListener = r3
            r4 = 255(0xff, float:3.57E-43)
            r5.alpha = r4
            r5.isExtraScaleEnabled = r1
            r5.isDirty = r2
            r0.addUpdateListener(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieDrawable.<init>():void");
    }

    public final ImageAssetManager getImageAssetManager() {
        Context context;
        boolean z;
        if (getCallback() == null) {
            return null;
        }
        ImageAssetManager imageAssetManager = this.imageAssetManager;
        if (imageAssetManager != null) {
            Drawable.Callback callback = getCallback();
            if (callback != null && (callback instanceof View)) {
                context = ((View) callback).getContext();
            } else {
                context = null;
            }
            Objects.requireNonNull(imageAssetManager);
            if (!(context == null && imageAssetManager.context == null) && !imageAssetManager.context.equals(context)) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                this.imageAssetManager = null;
            }
        }
        if (this.imageAssetManager == null) {
            Drawable.Callback callback2 = getCallback();
            String str = this.imageAssetsFolder;
            ImageAssetDelegate imageAssetDelegate = this.imageAssetDelegate;
            LottieComposition lottieComposition = this.composition;
            Objects.requireNonNull(lottieComposition);
            this.imageAssetManager = new ImageAssetManager(callback2, str, imageAssetDelegate, lottieComposition.images);
        }
        return this.imageAssetManager;
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void invalidateDrawable(Drawable drawable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void scheduleDrawable(Drawable drawable, Runnable runnable, long j) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, runnable, j);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public final void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, runnable);
        }
    }

    public final void setMaxFrame(final String str) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.9
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMaxFrame(str);
                }
            });
            return;
        }
        Marker marker = lottieComposition.getMarker(str);
        if (marker != null) {
            setMaxFrame((int) (marker.startFrame + marker.durationFrames));
            return;
        }
        throw new IllegalArgumentException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Cannot find marker with name ", str, "."));
    }

    public final void setMinFrame(final String str) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.8
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinFrame(str);
                }
            });
            return;
        }
        Marker marker = lottieComposition.getMarker(str);
        if (marker != null) {
            setMinFrame((int) marker.startFrame);
            return;
        }
        throw new IllegalArgumentException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Cannot find marker with name ", str, "."));
    }

    public final void setMinAndMaxFrame(final String str, final String str2, final boolean z) {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.11
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinAndMaxFrame(str, str2, z);
                }
            });
            return;
        }
        Marker marker = lottieComposition.getMarker(str);
        if (marker != null) {
            int i = (int) marker.startFrame;
            Marker marker2 = this.composition.getMarker(str2);
            if (str2 != null) {
                setMinAndMaxFrame(i, (int) (marker2.startFrame + (z ? 1.0f : 0.0f)));
                return;
            }
            throw new IllegalArgumentException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Cannot find marker with name ", str2, "."));
        }
        throw new IllegalArgumentException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Cannot find marker with name ", str, "."));
    }

    public final void setMinAndMaxFrame(final int i, final int i2) {
        if (this.composition == null) {
            this.lazyCompositionTasks.add(new LazyCompositionTask() { // from class: com.airbnb.lottie.LottieDrawable.12
                @Override // com.airbnb.lottie.LottieDrawable.LazyCompositionTask
                public final void run() {
                    LottieDrawable.this.setMinAndMaxFrame(i, i2);
                }
            });
        } else {
            this.animator.setMinAndMaxFrames(i, i2 + 0.99f);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final int getAlpha() {
        return this.alpha;
    }

    @Override // android.graphics.drawable.Animatable
    public final void start() {
        playAnimation();
    }
}
