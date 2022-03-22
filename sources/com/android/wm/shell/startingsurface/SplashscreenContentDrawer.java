package com.android.wm.shell.startingsurface;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
/* loaded from: classes.dex */
public final class SplashscreenContentDrawer {
    public int mBrandingImageHeight;
    public int mBrandingImageWidth;
    @VisibleForTesting
    public final ColorCache mColorCache;
    public final Context mContext;
    public int mDefaultIconSize;
    public final IconProvider mIconProvider;
    public int mIconSize;
    public int mLastPackageContextConfigHash;
    public int mMainWindowShiftLength;
    public final Handler mSplashscreenWorkerHandler;
    public final SplashScreenWindowAttrs mTmpAttrs = new SplashScreenWindowAttrs();
    public final TransactionPool mTransactionPool;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ColorCache extends BroadcastReceiver {
        public final ArrayMap<String, Colors> mColorMap = new ArrayMap<>();

        /* loaded from: classes.dex */
        public static class Colors {
            public final WindowColor[] mWindowColors = new WindowColor[2];
            public final IconColor[] mIconColors = new IconColor[2];
        }

        public static <T extends Cache> T getCache(T[] tArr, int i, int[] iArr) {
            int i2 = Integer.MAX_VALUE;
            for (int i3 = 0; i3 < 2; i3++) {
                T t = tArr[i3];
                if (t == null) {
                    i2 = -1;
                    iArr[0] = i3;
                } else if (t.mHash == i) {
                    t.mReuseCount++;
                    return t;
                } else {
                    int i4 = t.mReuseCount;
                    if (i4 < i2) {
                        iArr[0] = i3;
                        i2 = i4;
                    }
                }
            }
            return null;
        }

        /* loaded from: classes.dex */
        public static class Cache {
            public final int mHash;
            public int mReuseCount;

            public Cache(int i) {
                this.mHash = i;
            }
        }

        /* loaded from: classes.dex */
        public static class IconColor extends Cache {
            public final int mBgColor;
            public final int mFgColor;
            public final float mFgNonTranslucentRatio;
            public final boolean mIsBgComplex;
            public final boolean mIsBgGrayscale;

            public IconColor(int i, int i2, int i3, boolean z, boolean z2, float f) {
                super(i);
                this.mFgColor = i2;
                this.mBgColor = i3;
                this.mIsBgComplex = z;
                this.mIsBgGrayscale = z2;
                this.mFgNonTranslucentRatio = f;
            }
        }

        /* loaded from: classes.dex */
        public static class WindowColor extends Cache {
            public final int mBgColor;

            public WindowColor(int i, int i2) {
                super(i);
                this.mBgColor = i2;
            }
        }

        public ColorCache(Context context, Handler handler) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            context.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, null, handler);
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            if (data != null) {
                this.mColorMap.remove(data.getEncodedSchemeSpecificPart());
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SplashScreenWindowAttrs {
        public int mWindowBgResId = 0;
        public int mWindowBgColor = 0;
        public Drawable mSplashScreenIcon = null;
        public Drawable mBrandingImage = null;
        public int mIconBgColor = 0;
        public int mAnimationDuration = 0;
    }

    /* loaded from: classes.dex */
    public class StartingWindowViewBuilder {
        public boolean mAllowHandleEmpty;
        public Drawable[] mFinalIconDrawables;
        public int mFinalIconSize;
        public Drawable mOverlayDrawable;
        public int mSuggestType;
        public int mThemeColor;
        public Consumer<Runnable> mUiThreadInitTask;

        /* loaded from: classes.dex */
        public class ShapeIconFactory extends BaseIconFactory {
            public ShapeIconFactory(Context context, int i, int i2) {
                super(context, i, i2, true);
            }
        }

        public final void createIconDrawable(Drawable drawable, boolean z) {
            boolean z2;
            Drawable drawable2;
            SplashscreenIconDrawableFactory$MaskBackgroundDrawable splashscreenIconDrawableFactory$MaskBackgroundDrawable;
            if (z) {
                SplashscreenContentDrawer splashscreenContentDrawer = SplashscreenContentDrawer.this;
                this.mFinalIconDrawables = new Drawable[]{new SplashscreenIconDrawableFactory$ImmobileIconDrawable(drawable, splashscreenContentDrawer.mDefaultIconSize, this.mFinalIconSize, splashscreenContentDrawer.mSplashscreenWorkerHandler)};
                return;
            }
            SplashscreenContentDrawer splashscreenContentDrawer2 = SplashscreenContentDrawer.this;
            int i = splashscreenContentDrawer2.mTmpAttrs.mIconBgColor;
            int i2 = this.mThemeColor;
            int i3 = splashscreenContentDrawer2.mDefaultIconSize;
            int i4 = this.mFinalIconSize;
            Handler handler = splashscreenContentDrawer2.mSplashscreenWorkerHandler;
            if (i == 0 || i == i2) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (drawable instanceof Animatable) {
                drawable2 = new SplashscreenIconDrawableFactory$AnimatableIconAnimateListener(drawable);
            } else if (drawable instanceof AdaptiveIconDrawable) {
                drawable2 = new SplashscreenIconDrawableFactory$ImmobileIconDrawable(drawable, i3, i4, handler);
                z2 = false;
            } else {
                drawable2 = new SplashscreenIconDrawableFactory$ImmobileIconDrawable(new SplashscreenIconDrawableFactory$AdaptiveForegroundDrawable(drawable), i3, i4, handler);
            }
            if (z2) {
                splashscreenIconDrawableFactory$MaskBackgroundDrawable = new SplashscreenIconDrawableFactory$MaskBackgroundDrawable(i);
            } else {
                splashscreenIconDrawableFactory$MaskBackgroundDrawable = null;
            }
            this.mFinalIconDrawables = new Drawable[]{drawable2, splashscreenIconDrawableFactory$MaskBackgroundDrawable};
        }

        public StartingWindowViewBuilder(Context context, ActivityInfo activityInfo) {
            this.mFinalIconSize = SplashscreenContentDrawer.this.mIconSize;
        }
    }

    @VisibleForTesting
    public static long getShowingDuration(long j, long j2) {
        return (j > j2 && j2 < 500) ? (j > 500 || j2 < 400) ? 400L : 500L : j2;
    }

    /* renamed from: -$$Nest$smisRgbSimilarInHsv  reason: not valid java name */
    public static boolean m157$$Nest$smisRgbSimilarInHsv(int i, int i2) {
        float f;
        double d;
        boolean z;
        boolean z2;
        if (i != i2) {
            float luminance = Color.luminance(i);
            float luminance2 = Color.luminance(i2);
            if (luminance > luminance2) {
                f = (luminance + 0.05f) / (luminance2 + 0.05f);
            } else {
                f = (luminance2 + 0.05f) / (luminance + 0.05f);
            }
            if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, -853329785, 32, null, String.valueOf(Integer.toHexString(i)), String.valueOf(Integer.toHexString(i2)), Double.valueOf(f));
            }
            if (f >= 2.0f) {
                float[] fArr = new float[3];
                float[] fArr2 = new float[3];
                Color.colorToHSV(i, fArr);
                Color.colorToHSV(i2, fArr2);
                int abs = ((((int) Math.abs(fArr[0] - fArr2[0])) + 180) % 360) - 180;
                double pow = Math.pow(abs / 180.0f, 2.0d);
                double pow2 = Math.pow(fArr[1] - fArr2[1], 2.0d);
                double pow3 = Math.pow(fArr[2] - fArr2[2], 2.0d);
                double sqrt = Math.sqrt(((pow + pow2) + pow3) / 3.0d);
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    double d2 = fArr[0];
                    d = sqrt;
                    double d3 = fArr[1];
                    double d4 = fArr2[1];
                    double d5 = fArr[2];
                    ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
                    z2 = false;
                    Double valueOf = Double.valueOf(d2);
                    z = true;
                    ShellProtoLogImpl.v(shellProtoLogGroup, -137676175, 2796201, null, Long.valueOf(abs), valueOf, Double.valueOf(fArr2[0]), Double.valueOf(d3), Double.valueOf(d4), Double.valueOf(d5), Double.valueOf(fArr2[2]), Double.valueOf(pow), Double.valueOf(pow2), Double.valueOf(pow3), Double.valueOf(d));
                } else {
                    z2 = false;
                    z = true;
                    d = sqrt;
                }
                if (d < 0.1d) {
                    return z;
                }
                return z2;
            }
        }
        return true;
    }

    public static int estimateWindowBGColor(Drawable drawable) {
        SplashscreenContentDrawer$DrawableColorTester$ColorTester splashscreenContentDrawer$DrawableColorTester$ColorTester;
        SplashscreenContentDrawer$DrawableColorTester$ColorTester splashscreenContentDrawer$DrawableColorTester$ColorTester2;
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            if (layerDrawable.getNumberOfLayers() > 0) {
                if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 428468608, 0, null, null);
                }
                drawable = layerDrawable.getDrawable(0);
            }
        }
        if (drawable == null) {
            splashscreenContentDrawer$DrawableColorTester$ColorTester = new SplashscreenContentDrawer$DrawableColorTester$SingleColorTester(new ColorDrawable(getSystemBGColor()));
        } else {
            if (drawable instanceof ColorDrawable) {
                splashscreenContentDrawer$DrawableColorTester$ColorTester2 = new SplashscreenContentDrawer$DrawableColorTester$SingleColorTester((ColorDrawable) drawable);
            } else {
                splashscreenContentDrawer$DrawableColorTester$ColorTester2 = new SplashscreenContentDrawer$DrawableColorTester$ComplexDrawableTester(drawable, 1);
            }
            splashscreenContentDrawer$DrawableColorTester$ColorTester = splashscreenContentDrawer$DrawableColorTester$ColorTester2;
        }
        if (splashscreenContentDrawer$DrawableColorTester$ColorTester.passFilterRatio() != 0.0f) {
            return splashscreenContentDrawer$DrawableColorTester$ColorTester.getDominantColor();
        }
        Slog.w("ShellStartingWindow", "Window background is transparent, fill background with black color");
        return getSystemBGColor();
    }

    public static void getWindowAttrs(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        boolean z;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Window);
        splashScreenWindowAttrs.mWindowBgResId = obtainStyledAttributes.getResourceId(1, 0);
        splashScreenWindowAttrs.mWindowBgColor = ((Integer) safeReturnAttrDefault(new UnaryOperator() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(obtainStyledAttributes.getColor(56, ((Integer) obj).intValue()));
            }
        }, 0)).intValue();
        splashScreenWindowAttrs.mSplashScreenIcon = (Drawable) safeReturnAttrDefault(new UnaryOperator() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Drawable drawable = (Drawable) obj;
                return obtainStyledAttributes.getDrawable(57);
            }
        }, null);
        splashScreenWindowAttrs.mAnimationDuration = ((Integer) safeReturnAttrDefault(new UnaryOperator() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(obtainStyledAttributes.getInt(58, ((Integer) obj).intValue()));
            }
        }, 0)).intValue();
        splashScreenWindowAttrs.mBrandingImage = (Drawable) safeReturnAttrDefault(new UnaryOperator() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Drawable drawable = (Drawable) obj;
                return obtainStyledAttributes.getDrawable(59);
            }
        }, null);
        splashScreenWindowAttrs.mIconBgColor = ((Integer) safeReturnAttrDefault(new UnaryOperator() { // from class: com.android.wm.shell.startingsurface.SplashscreenContentDrawer$$ExternalSyntheticLambda9
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(obtainStyledAttributes.getColor(60, ((Integer) obj).intValue()));
            }
        }, 0)).intValue();
        obtainStyledAttributes.recycle();
        if (ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled) {
            String valueOf = String.valueOf(Integer.toHexString(splashScreenWindowAttrs.mWindowBgColor));
            if (splashScreenWindowAttrs.mSplashScreenIcon != null) {
                z = true;
            } else {
                z = false;
            }
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, 1508732488, 28, null, valueOf, Boolean.valueOf(z), Long.valueOf(splashScreenWindowAttrs.mAnimationDuration));
        }
    }

    public static int peekWindowBGColor(Context context, SplashScreenWindowAttrs splashScreenWindowAttrs) {
        Drawable drawable;
        Trace.traceBegin(32L, "peekWindowBGColor");
        if (splashScreenWindowAttrs.mWindowBgColor != 0) {
            drawable = new ColorDrawable(splashScreenWindowAttrs.mWindowBgColor);
        } else {
            int i = splashScreenWindowAttrs.mWindowBgResId;
            if (i != 0) {
                drawable = context.getDrawable(i);
            } else {
                drawable = new ColorDrawable(getSystemBGColor());
                Slog.w("ShellStartingWindow", "Window background does not exist, using " + drawable);
            }
        }
        int estimateWindowBGColor = estimateWindowBGColor(drawable);
        Trace.traceEnd(32L);
        return estimateWindowBGColor;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x002d, code lost:
        if (r5 != null) goto L_0x0049;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getBGColorFromCache(android.content.pm.ActivityInfo r5, java.util.function.IntSupplier r6) {
        /*
            r4 = this;
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache r0 = r4.mColorCache
            java.lang.String r5 = r5.packageName
            int r1 = r4.mLastPackageContextConfigHash
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$SplashScreenWindowAttrs r4 = r4.mTmpAttrs
            int r2 = r4.mWindowBgColor
            int r4 = r4.mWindowBgResId
            java.util.Objects.requireNonNull(r0)
            android.util.ArrayMap<java.lang.String, com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors> r3 = r0.mColorMap
            java.lang.Object r3 = r3.get(r5)
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors r3 = (com.android.wm.shell.startingsurface.SplashscreenContentDrawer.ColorCache.Colors) r3
            int r1 = r1 * 31
            int r1 = r1 + r2
            int r1 = r1 * 31
            int r1 = r1 + r4
            r4 = 1
            int[] r4 = new int[r4]
            r2 = 0
            r4[r2] = r2
            if (r3 == 0) goto L_0x0030
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor[] r5 = r3.mWindowColors
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Cache r5 = com.android.wm.shell.startingsurface.SplashscreenContentDrawer.ColorCache.getCache(r5, r1, r4)
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor r5 = (com.android.wm.shell.startingsurface.SplashscreenContentDrawer.ColorCache.WindowColor) r5
            if (r5 == 0) goto L_0x003a
            goto L_0x0049
        L_0x0030:
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors r3 = new com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors
            r3.<init>()
            android.util.ArrayMap<java.lang.String, com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$Colors> r0 = r0.mColorMap
            r0.put(r5, r3)
        L_0x003a:
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor r5 = new com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor
            int r6 = r6.getAsInt()
            r5.<init>(r1, r6)
            com.android.wm.shell.startingsurface.SplashscreenContentDrawer$ColorCache$WindowColor[] r6 = r3.mWindowColors
            r4 = r4[r2]
            r6[r4] = r5
        L_0x0049:
            int r4 = r5.mBgColor
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.SplashscreenContentDrawer.getBGColorFromCache(android.content.pm.ActivityInfo, java.util.function.IntSupplier):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x0167, code lost:
        if (r9 != null) goto L_0x0220;
     */
    /* JADX WARN: Removed duplicated region for block: B:13:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x042d  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0470  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x00af  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0196  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x01a5  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01bf  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x01e9  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0224  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02c1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.window.SplashScreenView makeSplashScreenContentView(final android.content.Context r25, android.window.StartingWindowInfo r26, @android.window.StartingWindowInfo.StartingWindowType int r27, java.util.function.Consumer<java.lang.Runnable> r28) {
        /*
            Method dump skipped, instructions count: 1172
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.SplashscreenContentDrawer.makeSplashScreenContentView(android.content.Context, android.window.StartingWindowInfo, int, java.util.function.Consumer):android.window.SplashScreenView");
    }

    public SplashscreenContentDrawer(Context context, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        this.mIconProvider = iconProvider;
        this.mTransactionPool = transactionPool;
        HandlerThread handlerThread = new HandlerThread("wmshell.splashworker", -10);
        handlerThread.start();
        Handler threadHandler = handlerThread.getThreadHandler();
        this.mSplashscreenWorkerHandler = threadHandler;
        this.mColorCache = new ColorCache(context, threadHandler);
    }

    public static int getSystemBGColor() {
        Application currentApplication = ActivityThread.currentApplication();
        if (currentApplication != null) {
            return currentApplication.getResources().getColor(2131100631);
        }
        Slog.e("ShellStartingWindow", "System context does not exist!");
        return -16777216;
    }

    public static Object safeReturnAttrDefault(UnaryOperator unaryOperator, Integer num) {
        try {
            return unaryOperator.apply(num);
        } catch (RuntimeException e) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Get attribute fail, return default: ");
            m.append(e.getMessage());
            Slog.w("ShellStartingWindow", m.toString());
            return num;
        }
    }
}
