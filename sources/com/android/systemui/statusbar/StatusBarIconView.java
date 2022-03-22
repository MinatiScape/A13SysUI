package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.R$array;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.NotificationIconDozeHelper;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda2;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda4;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class StatusBarIconView extends AnimatedImageView implements StatusIconDisplayable {
    public boolean mAlwaysScaleIcon;
    public int mAnimationStartColor;
    public final boolean mBlocked;
    public int mCachedContrastBackgroundColor;
    public ValueAnimator mColorAnimator;
    public final ValueAnimator.AnimatorUpdateListener mColorUpdater;
    public int mContrastedDrawableColor;
    public int mCurrentSetColor;
    public int mDecorColor;
    public int mDensity;
    public ObjectAnimator mDotAnimator;
    public float mDotAppearAmount;
    public final Paint mDotPaint;
    public float mDotRadius;
    public float mDozeAmount;
    public final NotificationIconDozeHelper mDozer;
    public int mDrawableColor;
    public StatusBarIcon mIcon;
    public float mIconAppearAmount;
    public ObjectAnimator mIconAppearAnimator;
    public int mIconColor;
    public float mIconScale;
    public boolean mIncreasedSize;
    public Runnable mLayoutRunnable;
    public float[] mMatrix;
    public ColorMatrixColorFilter mMatrixColorFilter;
    public boolean mNightMode;
    public StatusBarNotification mNotification;
    public Drawable mNumberBackground;
    public Paint mNumberPain;
    public String mNumberText;
    public int mNumberX;
    public int mNumberY;
    public Runnable mOnDismissListener;
    public boolean mShowsConversation;
    @ViewDebug.ExportedProperty
    public String mSlot;
    public int mStaticDotRadius;
    public int mStatusBarIconDrawingSize;
    public int mStatusBarIconDrawingSizeIncreased;
    public int mStatusBarIconSize;
    public float mSystemIconDefaultScale;
    public float mSystemIconDesiredHeight;
    public float mSystemIconIntrinsicHeight;
    public int mVisibleState;
    public static final AnonymousClass1 ICON_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>() { // from class: com.android.systemui.statusbar.StatusBarIconView.1
        @Override // android.util.Property
        public final Float get(Object obj) {
            StatusBarIconView statusBarIconView = (StatusBarIconView) obj;
            Objects.requireNonNull(statusBarIconView);
            return Float.valueOf(statusBarIconView.mIconAppearAmount);
        }

        @Override // android.util.FloatProperty
        public final void setValue(StatusBarIconView statusBarIconView, float f) {
            StatusBarIconView statusBarIconView2 = statusBarIconView;
            Objects.requireNonNull(statusBarIconView2);
            if (statusBarIconView2.mIconAppearAmount != f) {
                statusBarIconView2.mIconAppearAmount = f;
                statusBarIconView2.invalidate();
            }
        }
    };
    public static final AnonymousClass2 DOT_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>() { // from class: com.android.systemui.statusbar.StatusBarIconView.2
        @Override // android.util.Property
        public final Float get(Object obj) {
            StatusBarIconView statusBarIconView = (StatusBarIconView) obj;
            Objects.requireNonNull(statusBarIconView);
            return Float.valueOf(statusBarIconView.mDotAppearAmount);
        }

        @Override // android.util.FloatProperty
        public final void setValue(StatusBarIconView statusBarIconView, float f) {
            StatusBarIconView statusBarIconView2 = statusBarIconView;
            Objects.requireNonNull(statusBarIconView2);
            if (statusBarIconView2.mDotAppearAmount != f) {
                statusBarIconView2.mDotAppearAmount = f;
                statusBarIconView2.invalidate();
            }
        }
    };

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification) {
        this(context, str, statusBarNotification, false);
    }

    @Override // com.android.systemui.statusbar.AnimatedImageView, android.widget.ImageView, android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setVisibleState(int i) {
        setVisibleState(i, true, null, 0L);
    }

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification, boolean z) {
        super(context);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 0.88235295f;
        boolean z2 = true;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new StatusBarIconView$$ExternalSyntheticLambda0(this, 0);
        this.mCachedContrastBackgroundColor = 0;
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = z;
        this.mSlot = str;
        Paint paint = new Paint();
        this.mNumberPain = paint;
        paint.setTextAlign(Paint.Align.CENTER);
        this.mNumberPain.setColor(context.getColor(2131232527));
        this.mNumberPain.setAntiAlias(true);
        setNotification(statusBarNotification);
        setScaleType(ImageView.ScaleType.CENTER);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNightMode = (context.getResources().getConfiguration().uiMode & 48) != 32 ? false : z2;
        if (this.mNotification != null) {
            setDecorColor(getContext().getColor(this.mNightMode ? 17170975 : 17170976));
        }
        reloadDimens();
        maybeUpdateIconScaleDimens();
    }

    public static String contentDescForNotification(Context context, Notification notification) {
        CharSequence charSequence;
        CharSequence charSequence2 = "";
        try {
            charSequence = Notification.Builder.recoverBuilder(context, notification).loadHeaderAppName();
        } catch (RuntimeException e) {
            Log.e("StatusBarIconView", "Unable to recover builder", e);
            Parcelable parcelable = notification.extras.getParcelable("android.appInfo");
            if (parcelable instanceof ApplicationInfo) {
                charSequence = String.valueOf(((ApplicationInfo) parcelable).loadLabel(context.getPackageManager()));
            } else {
                charSequence = charSequence2;
            }
        }
        CharSequence charSequence3 = notification.extras.getCharSequence("android.title");
        CharSequence charSequence4 = notification.extras.getCharSequence("android.text");
        CharSequence charSequence5 = notification.tickerText;
        if (TextUtils.equals(charSequence3, charSequence)) {
            charSequence3 = charSequence4;
        }
        if (!TextUtils.isEmpty(charSequence3)) {
            charSequence2 = charSequence3;
        } else if (!TextUtils.isEmpty(charSequence5)) {
            charSequence2 = charSequence5;
        }
        return context.getString(2131951709, charSequence, charSequence2);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final boolean isIconVisible() {
        StatusBarIcon statusBarIcon = this.mIcon;
        if (statusBarIcon == null || !statusBarIcon.visible) {
            return false;
        }
        return true;
    }

    public final void maybeUpdateIconScaleDimens() {
        int i;
        float f;
        if (this.mNotification != null || this.mAlwaysScaleIcon) {
            if (this.mIncreasedSize) {
                i = this.mStatusBarIconDrawingSizeIncreased;
            } else {
                i = this.mStatusBarIconDrawingSize;
            }
            this.mIconScale = i / this.mStatusBarIconSize;
            updatePivot();
            return;
        }
        if (getDrawable() != null) {
            f = getDrawable().getIntrinsicHeight();
        } else {
            f = this.mSystemIconIntrinsicHeight;
        }
        if (f != 0.0f) {
            this.mIconScale = this.mSystemIconDesiredHeight / f;
        } else {
            this.mIconScale = this.mSystemIconDefaultScale;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDraw(Canvas canvas) {
        float f;
        if (this.mIconAppearAmount > 0.0f) {
            canvas.save();
            float f2 = this.mIconScale;
            float f3 = this.mIconAppearAmount;
            canvas.scale(f2 * f3, f2 * f3, getWidth() / 2, getHeight() / 2);
            super.onDraw(canvas);
            canvas.restore();
        }
        Drawable drawable = this.mNumberBackground;
        if (drawable != null) {
            drawable.draw(canvas);
            canvas.drawText(this.mNumberText, this.mNumberX, this.mNumberY, this.mNumberPain);
        }
        if (this.mDotAppearAmount != 0.0f) {
            float alpha = Color.alpha(this.mDecorColor) / 255.0f;
            float f4 = this.mDotAppearAmount;
            if (f4 <= 1.0f) {
                f = this.mDotRadius * f4;
            } else {
                float f5 = f4 - 1.0f;
                alpha *= 1.0f - f5;
                f = R$array.interpolate(this.mDotRadius, getWidth() / 4, f5);
            }
            this.mDotPaint.setAlpha((int) (alpha * 255.0f));
            canvas.drawCircle(this.mStatusBarIconSize / 2, getHeight() / 2, f, this.mDotPaint);
        }
    }

    public final void reloadDimens() {
        boolean z;
        if (this.mDotRadius == this.mStaticDotRadius) {
            z = true;
        } else {
            z = false;
        }
        Resources resources = getResources();
        this.mStaticDotRadius = resources.getDimensionPixelSize(2131166726);
        this.mStatusBarIconSize = resources.getDimensionPixelSize(2131167067);
        this.mStatusBarIconDrawingSizeIncreased = resources.getDimensionPixelSize(2131167064);
        this.mStatusBarIconDrawingSize = resources.getDimensionPixelSize(2131167063);
        if (z) {
            this.mDotRadius = this.mStaticDotRadius;
        }
        this.mSystemIconDesiredHeight = resources.getDimension(17105556);
        float dimension = resources.getDimension(17105555);
        this.mSystemIconIntrinsicHeight = dimension;
        this.mSystemIconDefaultScale = this.mSystemIconDesiredHeight / dimension;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0049, code lost:
        if (r0.getResId() == r3.getResId()) goto L_0x004b;
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0050  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean set(com.android.internal.statusbar.StatusBarIcon r8) {
        /*
            Method dump skipped, instructions count: 229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.StatusBarIconView.set(com.android.internal.statusbar.StatusBarIcon):boolean");
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setDecorColor(int i) {
        this.mDecorColor = i;
        updateDecorColor();
    }

    public final void setDozing(boolean z, boolean z2) {
        float f;
        NotificationIconDozeHelper notificationIconDozeHelper = this.mDozer;
        final ShellCommandHandlerImpl$$ExternalSyntheticLambda2 shellCommandHandlerImpl$$ExternalSyntheticLambda2 = new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(this, 1);
        Objects.requireNonNull(notificationIconDozeHelper);
        float f2 = 1.0f;
        if (z2) {
            ValueAnimator.AnimatorUpdateListener notificationDozeHelper$$ExternalSyntheticLambda0 = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.NotificationDozeHelper$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    shellCommandHandlerImpl$$ExternalSyntheticLambda2.accept((Float) valueAnimator.getAnimatedValue());
                }
            };
            AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.NotificationDozeHelper.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    this.setTag(2131427869, null);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator) {
                    this.setTag(2131427869, animator);
                }
            };
            if (z) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            if (!z) {
                f2 = 0.0f;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(f, f2);
            ofFloat.addUpdateListener(notificationDozeHelper$$ExternalSyntheticLambda0);
            ofFloat.setDuration(500L);
            ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            ofFloat.setStartDelay(0L);
            ofFloat.addListener(animatorListenerAdapter);
            ofFloat.start();
            return;
        }
        Animator animator = (Animator) getTag(2131427869);
        if (animator != null) {
            animator.cancel();
        }
        if (!z) {
            f2 = 0.0f;
        }
        shellCommandHandlerImpl$$ExternalSyntheticLambda2.accept(Float.valueOf(f2));
    }

    public final void setIconColor(int i, boolean z) {
        if (this.mIconColor != i) {
            this.mIconColor = i;
            ValueAnimator valueAnimator = this.mColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            int i2 = this.mCurrentSetColor;
            if (i2 != i) {
                if (!z || i2 == 0) {
                    this.mCurrentSetColor = i;
                    updateIconColor();
                    return;
                }
                this.mAnimationStartColor = i2;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.mColorAnimator = ofFloat;
                ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                this.mColorAnimator.setDuration(100L);
                this.mColorAnimator.addUpdateListener(this.mColorUpdater);
                this.mColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        StatusBarIconView statusBarIconView = StatusBarIconView.this;
                        statusBarIconView.mColorAnimator = null;
                        statusBarIconView.mAnimationStartColor = 0;
                    }
                });
                this.mColorAnimator.start();
            }
        }
    }

    public final void setNotification(StatusBarNotification statusBarNotification) {
        Notification notification;
        this.mNotification = statusBarNotification;
        if (!(statusBarNotification == null || (notification = statusBarNotification.getNotification()) == null)) {
            String contentDescForNotification = contentDescForNotification(((ImageView) this).mContext, notification);
            if (!TextUtils.isEmpty(contentDescForNotification)) {
                setContentDescription(contentDescForNotification);
            }
        }
        maybeUpdateIconScaleDimens();
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setStaticDrawableColor(int i) {
        this.mDrawableColor = i;
        this.mCurrentSetColor = i;
        updateIconColor();
        updateContrastedStaticColor();
        this.mIconColor = i;
        Objects.requireNonNull(this.mDozer);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final void setVisibleState(int i, boolean z) {
        setVisibleState(i, z, null, 0L);
    }

    @Override // android.view.View
    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StatusBarIconView(slot=");
        m.append(this.mSlot);
        m.append(" icon=");
        m.append(this.mIcon);
        m.append(" notification=");
        m.append(this.mNotification);
        m.append(")");
        return m.toString();
    }

    public final void updateContrastedStaticColor() {
        if (Color.alpha(this.mCachedContrastBackgroundColor) != 255) {
            this.mContrastedDrawableColor = this.mDrawableColor;
            return;
        }
        int i = this.mDrawableColor;
        if (!ContrastColorUtil.satisfiesTextContrast(this.mCachedContrastBackgroundColor, i)) {
            float[] fArr = new float[3];
            int i2 = this.mDrawableColor;
            ThreadLocal<double[]> threadLocal = ColorUtils.TEMP_ARRAY;
            ColorUtils.RGBToHSL(Color.red(i2), Color.green(i2), Color.blue(i2), fArr);
            if (fArr[1] < 0.2f) {
                i = 0;
            }
            i = ContrastColorUtil.resolveContrastColor(((ImageView) this).mContext, i, this.mCachedContrastBackgroundColor, !ContrastColorUtil.isColorLight(this.mCachedContrastBackgroundColor));
        }
        this.mContrastedDrawableColor = i;
    }

    public final void updateDecorColor() {
        int interpolateColors = R$array.interpolateColors(this.mDecorColor, -1, this.mDozeAmount);
        if (this.mDotPaint.getColor() != interpolateColors) {
            this.mDotPaint.setColor(interpolateColors);
            if (this.mDotAppearAmount != 0.0f) {
                invalidate();
            }
        }
    }

    public final boolean updateDrawable(boolean z) {
        if (this.mIcon == null) {
            return false;
        }
        try {
            Trace.beginSection("StatusBarIconView#updateDrawable()");
            Drawable icon = getIcon(this.mIcon);
            if (icon == null) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No icon for slot ");
                m.append(this.mSlot);
                m.append("; ");
                m.append(this.mIcon.icon);
                Log.w("StatusBarIconView", m.toString());
                return false;
            }
            if (z) {
                setImageDrawable(null);
            }
            setImageDrawable(icon);
            return true;
        } catch (OutOfMemoryError unused) {
            Log.w("StatusBarIconView", "OOM while inflating " + this.mIcon.icon + " for slot " + this.mSlot);
            return false;
        } finally {
            Trace.endSection();
        }
    }

    public final void updateIconColor() {
        if (this.mShowsConversation) {
            setColorFilter((ColorFilter) null);
        } else if (this.mCurrentSetColor != 0) {
            if (this.mMatrixColorFilter == null) {
                this.mMatrix = new float[20];
                this.mMatrixColorFilter = new ColorMatrixColorFilter(this.mMatrix);
            }
            int interpolateColors = R$array.interpolateColors(this.mCurrentSetColor, -1, this.mDozeAmount);
            float[] fArr = this.mMatrix;
            Arrays.fill(fArr, 0.0f);
            fArr[4] = Color.red(interpolateColors);
            fArr[9] = Color.green(interpolateColors);
            fArr[14] = Color.blue(interpolateColors);
            fArr[18] = (Color.alpha(interpolateColors) / 255.0f) + (this.mDozeAmount * 0.67f);
            this.mMatrixColorFilter.setColorMatrixArray(this.mMatrix);
            setColorFilter((ColorFilter) null);
            setColorFilter(this.mMatrixColorFilter);
        } else {
            NotificationIconDozeHelper notificationIconDozeHelper = this.mDozer;
            float f = this.mDozeAmount;
            if (f > 0.0f) {
                notificationIconDozeHelper.mGrayscaleColorMatrix.setSaturation(1.0f - f);
                setColorFilter(new ColorMatrixColorFilter(notificationIconDozeHelper.mGrayscaleColorMatrix));
                return;
            }
            Objects.requireNonNull(notificationIconDozeHelper);
            setColorFilter((ColorFilter) null);
        }
    }

    public final void debug(int i) {
        super.debug(i);
        Log.d("View", ImageView.debugIndent(i) + "slot=" + this.mSlot);
        Log.d("View", ImageView.debugIndent(i) + "icon=" + this.mIcon);
    }

    @Override // android.view.View
    public final void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (rect.left + translationX);
        rect.right = (int) (rect.right + translationX);
        rect.top = (int) (rect.top + translationY);
        rect.bottom = (int) (rect.bottom + translationY);
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x007b A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0080 A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0088 A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00a2 A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00a7 A[Catch: all -> 0x01dd, TRY_LEAVE, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ad A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00f0 A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0115 A[Catch: all -> 0x01dd, TryCatch #0 {all -> 0x01dd, blocks: (B:18:0x005c, B:20:0x0061, B:24:0x0069, B:27:0x0071, B:29:0x007b, B:30:0x0080, B:31:0x0084, B:33:0x0088, B:37:0x0090, B:40:0x0098, B:42:0x00a2, B:43:0x00a7, B:49:0x00bc, B:51:0x00c2, B:52:0x00ea, B:54:0x00f0, B:56:0x00f4, B:59:0x00f9, B:61:0x00fd, B:63:0x0101, B:65:0x0105, B:75:0x0115, B:79:0x0129, B:81:0x012f, B:82:0x0165, B:84:0x0169, B:88:0x0171, B:91:0x0178, B:93:0x017e, B:94:0x0180, B:95:0x019c), top: B:104:0x005c }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.graphics.drawable.Drawable getIcon(com.android.internal.statusbar.StatusBarIcon r14) {
        /*
            Method dump skipped, instructions count: 496
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.StatusBarIconView.getIcon(com.android.internal.statusbar.StatusBarIcon):android.graphics.drawable.Drawable");
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        int i;
        super.onConfigurationChanged(configuration);
        int i2 = configuration.densityDpi;
        boolean z = true;
        if (i2 != this.mDensity) {
            this.mDensity = i2;
            reloadDimens();
            updateDrawable(true);
            maybeUpdateIconScaleDimens();
        }
        if ((configuration.uiMode & 48) != 32) {
            z = false;
        }
        if (z != this.mNightMode) {
            this.mNightMode = z;
            if (this.mNotification != null) {
                Context context = getContext();
                if (this.mNightMode) {
                    i = 17170975;
                } else {
                    i = 17170976;
                }
                setDecorColor(context.getColor(i));
            }
        }
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public final void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        setImageTintList(ColorStateList.valueOf(tint));
        setDecorColor(tint);
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        StatusBarNotification statusBarNotification = this.mNotification;
        if (statusBarNotification != null) {
            accessibilityEvent.setParcelableData(statusBarNotification.getNotification());
        }
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Runnable runnable = this.mLayoutRunnable;
        if (runnable != null) {
            runnable.run();
            this.mLayoutRunnable = null;
        }
        updatePivot();
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateDrawable(true);
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mNumberBackground != null) {
            placeNumber();
        }
    }

    public final void placeNumber() {
        String str;
        if (this.mIcon.number > getContext().getResources().getInteger(17694723)) {
            str = getContext().getResources().getString(17039383);
        } else {
            str = NumberFormat.getIntegerInstance().format(this.mIcon.number);
        }
        this.mNumberText = str;
        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect();
        this.mNumberPain.getTextBounds(str, 0, str.length(), rect);
        int i = rect.right - rect.left;
        int i2 = rect.bottom - rect.top;
        this.mNumberBackground.getPadding(rect);
        int i3 = rect.left + i + rect.right;
        if (i3 < this.mNumberBackground.getMinimumWidth()) {
            i3 = this.mNumberBackground.getMinimumWidth();
        }
        int i4 = rect.right;
        this.mNumberX = (width - i4) - (((i3 - i4) - rect.left) / 2);
        int i5 = rect.top + i2 + rect.bottom;
        if (i5 < this.mNumberBackground.getMinimumWidth()) {
            i5 = this.mNumberBackground.getMinimumWidth();
        }
        int i6 = rect.bottom;
        this.mNumberY = (height - i6) - ((((i5 - rect.top) - i2) - i6) / 2);
        this.mNumberBackground.setBounds(width - i3, height - i5, width, height);
    }

    public final void setVisibleState(int i, boolean z, final PipTaskOrganizer$$ExternalSyntheticLambda4 pipTaskOrganizer$$ExternalSyntheticLambda4, long j) {
        float f;
        PathInterpolator pathInterpolator;
        boolean z2;
        boolean z3 = true;
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            ObjectAnimator objectAnimator = this.mIconAppearAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = this.mDotAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.cancel();
            }
            float f2 = 1.0f;
            if (z) {
                PathInterpolator pathInterpolator2 = Interpolators.FAST_OUT_LINEAR_IN;
                if (i == 0) {
                    pathInterpolator = Interpolators.LINEAR_OUT_SLOW_IN;
                    f = 1.0f;
                } else {
                    pathInterpolator = pathInterpolator2;
                    f = 0.0f;
                }
                float f3 = this.mIconAppearAmount;
                long j2 = 100;
                if (f != f3) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ICON_APPEAR_AMOUNT, f3, f);
                    this.mIconAppearAnimator = ofFloat;
                    ofFloat.setInterpolator(pathInterpolator);
                    this.mIconAppearAnimator.setDuration(j == 0 ? 100L : j);
                    this.mIconAppearAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.4
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            StatusBarIconView.this.mIconAppearAnimator = null;
                            Runnable runnable = pipTaskOrganizer$$ExternalSyntheticLambda4;
                            if (runnable != null) {
                                runnable.run();
                            }
                        }
                    });
                    this.mIconAppearAnimator.start();
                    z2 = true;
                } else {
                    z2 = false;
                }
                f2 = i == 0 ? 2.0f : 0.0f;
                if (i == 1) {
                    pathInterpolator2 = Interpolators.LINEAR_OUT_SLOW_IN;
                }
                float f4 = this.mDotAppearAmount;
                if (f2 != f4) {
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, DOT_APPEAR_AMOUNT, f4, f2);
                    this.mDotAnimator = ofFloat2;
                    ofFloat2.setInterpolator(pathInterpolator2);
                    ObjectAnimator objectAnimator3 = this.mDotAnimator;
                    if (j != 0) {
                        j2 = j;
                    }
                    objectAnimator3.setDuration(j2);
                    final boolean z4 = !z2;
                    this.mDotAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.StatusBarIconView.5
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            Runnable runnable;
                            StatusBarIconView.this.mDotAnimator = null;
                            if (z4 && (runnable = pipTaskOrganizer$$ExternalSyntheticLambda4) != null) {
                                runnable.run();
                            }
                        }
                    });
                    this.mDotAnimator.start();
                } else {
                    z3 = z2;
                }
                if (!z3 && pipTaskOrganizer$$ExternalSyntheticLambda4 != null) {
                    pipTaskOrganizer$$ExternalSyntheticLambda4.run();
                    return;
                }
            }
            float f5 = i == 0 ? 1.0f : 0.0f;
            if (this.mIconAppearAmount != f5) {
                this.mIconAppearAmount = f5;
                invalidate();
            }
            float f6 = i == 1 ? 1.0f : i == 0 ? 2.0f : 0.0f;
            if (this.mDotAppearAmount != f6) {
                this.mDotAppearAmount = f6;
                invalidate();
            }
        }
        z3 = false;
        if (!z3) {
        }
    }

    public final void updatePivot() {
        if (isLayoutRtl()) {
            setPivotX(((this.mIconScale + 1.0f) / 2.0f) * getWidth());
        } else {
            setPivotX(((1.0f - this.mIconScale) / 2.0f) * getWidth());
        }
        setPivotY((getHeight() - (this.mIconScale * getWidth())) / 2.0f);
    }

    public StatusBarIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 15.0f / 17.0f;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.StatusBarIconView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                StatusBarIconView statusBarIconView = StatusBarIconView.this;
                StatusBarIconView.AnonymousClass1 r0 = StatusBarIconView.ICON_APPEAR_AMOUNT;
                Objects.requireNonNull(statusBarIconView);
                statusBarIconView.mCurrentSetColor = R$array.interpolateColors(statusBarIconView.mAnimationStartColor, statusBarIconView.mIconColor, valueAnimator.getAnimatedFraction());
                statusBarIconView.updateIconColor();
            }
        };
        this.mCachedContrastBackgroundColor = 0;
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = false;
        this.mAlwaysScaleIcon = true;
        reloadDimens();
        maybeUpdateIconScaleDimens();
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
    }

    @Override // android.widget.ImageView, android.view.View
    public final void setVisibility(int i) {
        super.setVisibility(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final String getSlot() {
        return this.mSlot;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final int getVisibleState() {
        return this.mVisibleState;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public final boolean isIconBlocked() {
        return this.mBlocked;
    }
}
