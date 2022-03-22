package com.android.keyguard;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.systemui.navigationbar.NavigationBarController;
import com.android.systemui.navigationbar.NavigationBarView;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardDisplayManager {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final Context mContext;
    public final AnonymousClass1 mDisplayListener;
    public final DisplayManager mDisplayService;
    public final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    public final Lazy<NavigationBarController> mNavigationBarControllerLazy;
    public boolean mShowing;
    public MediaRouter mMediaRouter = null;
    public final DisplayInfo mTmpDisplayInfo = new DisplayInfo();
    public final SparseArray<Presentation> mPresentations = new SparseArray<>();
    public final AnonymousClass2 mMediaRouterCallback = new MediaRouter.SimpleCallback() { // from class: com.android.keyguard.KeyguardDisplayManager.2
        @Override // android.media.MediaRouter.Callback
        public final void onRoutePresentationDisplayChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRoutePresentationDisplayChanged: info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public final void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRouteSelected: type=" + i + ", info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }

        @Override // android.media.MediaRouter.SimpleCallback, android.media.MediaRouter.Callback
        public final void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (KeyguardDisplayManager.DEBUG) {
                Log.d("KeyguardDisplayManager", "onRouteUnselected: type=" + i + ", info=" + routeInfo);
            }
            KeyguardDisplayManager keyguardDisplayManager = KeyguardDisplayManager.this;
            keyguardDisplayManager.updateDisplays(keyguardDisplayManager.mShowing);
        }
    };

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class KeyguardPresentation extends Presentation {
        public View mClock;
        public KeyguardClockSwitchController mKeyguardClockSwitchController;
        public final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
        public int mMarginLeft;
        public int mMarginTop;
        public AnonymousClass1 mMoveTextRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardDisplayManager.KeyguardPresentation.1
            @Override // java.lang.Runnable
            public final void run() {
                int i = KeyguardPresentation.this.mMarginLeft;
                double random = Math.random();
                KeyguardPresentation keyguardPresentation = KeyguardPresentation.this;
                int width = i + ((int) (random * (keyguardPresentation.mUsableWidth - keyguardPresentation.mClock.getWidth())));
                int i2 = KeyguardPresentation.this.mMarginTop;
                double random2 = Math.random();
                KeyguardPresentation keyguardPresentation2 = KeyguardPresentation.this;
                int height = i2 + ((int) (random2 * (keyguardPresentation2.mUsableHeight - keyguardPresentation2.mClock.getHeight())));
                KeyguardPresentation.this.mClock.setTranslationX(width);
                KeyguardPresentation.this.mClock.setTranslationY(height);
                KeyguardPresentation keyguardPresentation3 = KeyguardPresentation.this;
                keyguardPresentation3.mClock.postDelayed(keyguardPresentation3.mMoveTextRunnable, 10000L);
            }
        };
        public int mUsableHeight;
        public int mUsableWidth;

        @Override // android.app.Dialog, android.content.DialogInterface
        public final void cancel() {
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public final void onDetachedFromWindow() {
            this.mClock.removeCallbacks(this.mMoveTextRunnable);
        }

        /* JADX WARN: Type inference failed for: r3v1, types: [com.android.keyguard.KeyguardDisplayManager$KeyguardPresentation$1] */
        public KeyguardPresentation(Context context, Display display, KeyguardStatusViewComponent.Factory factory) {
            super(context, display, 2132018189, 2009);
            this.mKeyguardStatusViewComponentFactory = factory;
            setCancelable(false);
        }

        @Override // android.app.Dialog
        public final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            updateBounds();
            setContentView(LayoutInflater.from(getContext()).inflate(2131624166, (ViewGroup) null));
            getWindow().getDecorView().setSystemUiVisibility(1792);
            getWindow().getAttributes().setFitInsetsTypes(0);
            getWindow().setNavigationBarContrastEnforced(false);
            getWindow().setNavigationBarColor(0);
            View findViewById = findViewById(2131427717);
            this.mClock = findViewById;
            findViewById.post(this.mMoveTextRunnable);
            KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardStatusViewComponentFactory.build((KeyguardStatusView) findViewById(2131427717)).getKeyguardClockSwitchController();
            this.mKeyguardClockSwitchController = keyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            keyguardClockSwitchController.mOnlyClock = true;
            this.mKeyguardClockSwitchController.init();
        }

        @Override // android.app.Presentation
        public final void onDisplayChanged() {
            updateBounds();
            getWindow().getDecorView().requestLayout();
        }

        public final void updateBounds() {
            Rect bounds = getWindow().getWindowManager().getMaximumWindowMetrics().getBounds();
            this.mUsableWidth = (bounds.width() * 80) / 100;
            this.mUsableHeight = (bounds.height() * 80) / 100;
            this.mMarginLeft = (bounds.width() * 20) / 200;
            this.mMarginTop = (bounds.height() * 20) / 200;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0049 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean showPresentation(android.view.Display r8) {
        /*
            r7 = this;
            r0 = 1
            r1 = 0
            java.lang.String r2 = "KeyguardDisplayManager"
            if (r8 != 0) goto L_0x0011
            boolean r3 = com.android.keyguard.KeyguardDisplayManager.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.String r3 = "Cannot show Keyguard on null display"
            android.util.Log.i(r2, r3)
        L_0x000f:
            r3 = r1
            goto L_0x0047
        L_0x0011:
            int r3 = r8.getDisplayId()
            if (r3 != 0) goto L_0x0021
            boolean r3 = com.android.keyguard.KeyguardDisplayManager.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.String r3 = "Do not show KeyguardPresentation on the default display"
            android.util.Log.i(r2, r3)
            goto L_0x000f
        L_0x0021:
            android.view.DisplayInfo r3 = r7.mTmpDisplayInfo
            r8.getDisplayInfo(r3)
            android.view.DisplayInfo r3 = r7.mTmpDisplayInfo
            int r4 = r3.flags
            r4 = r4 & 4
            if (r4 == 0) goto L_0x0038
            boolean r3 = com.android.keyguard.KeyguardDisplayManager.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.String r3 = "Do not show KeyguardPresentation on a private display"
            android.util.Log.i(r2, r3)
            goto L_0x000f
        L_0x0038:
            int r3 = r3.displayGroupId
            if (r3 == 0) goto L_0x0046
            boolean r3 = com.android.keyguard.KeyguardDisplayManager.DEBUG
            if (r3 == 0) goto L_0x000f
            java.lang.String r3 = "Do not show KeyguardPresentation on a non-default group display"
            android.util.Log.i(r2, r3)
            goto L_0x000f
        L_0x0046:
            r3 = r0
        L_0x0047:
            if (r3 != 0) goto L_0x004a
            return r1
        L_0x004a:
            boolean r3 = com.android.keyguard.KeyguardDisplayManager.DEBUG
            if (r3 == 0) goto L_0x0062
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Keyguard enabled on display: "
            r3.append(r4)
            r3.append(r8)
            java.lang.String r3 = r3.toString()
            android.util.Log.i(r2, r3)
        L_0x0062:
            int r3 = r8.getDisplayId()
            android.util.SparseArray<android.app.Presentation> r4 = r7.mPresentations
            java.lang.Object r4 = r4.get(r3)
            android.app.Presentation r4 = (android.app.Presentation) r4
            if (r4 != 0) goto L_0x0094
            com.android.keyguard.KeyguardDisplayManager$KeyguardPresentation r4 = new com.android.keyguard.KeyguardDisplayManager$KeyguardPresentation
            android.content.Context r5 = r7.mContext
            com.android.keyguard.dagger.KeyguardStatusViewComponent$Factory r6 = r7.mKeyguardStatusViewComponentFactory
            r4.<init>(r5, r8, r6)
            com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda0 r8 = new com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda0
            r8.<init>()
            r4.setOnDismissListener(r8)
            r4.show()     // Catch: InvalidDisplayException -> 0x0085
            goto L_0x008c
        L_0x0085:
            r8 = move-exception
            java.lang.String r4 = "Invalid display:"
            android.util.Log.w(r2, r4, r8)
            r4 = 0
        L_0x008c:
            if (r4 == 0) goto L_0x0094
            android.util.SparseArray<android.app.Presentation> r7 = r7.mPresentations
            r7.append(r3, r4)
            return r0
        L_0x0094:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardDisplayManager.showPresentation(android.view.Display):boolean");
    }

    public final boolean updateDisplays(boolean z) {
        Display[] displays;
        boolean z2 = false;
        if (z) {
            boolean z3 = false;
            for (Display display : this.mDisplayService.getDisplays()) {
                updateNavigationBarVisibility(display.getDisplayId(), false);
                z3 |= showPresentation(display);
            }
            return z3;
        }
        if (this.mPresentations.size() > 0) {
            z2 = true;
        }
        for (int size = this.mPresentations.size() - 1; size >= 0; size--) {
            updateNavigationBarVisibility(this.mPresentations.keyAt(size), true);
            this.mPresentations.valueAt(size).dismiss();
        }
        this.mPresentations.clear();
        return z2;
    }

    public final void updateNavigationBarVisibility(int i, boolean z) {
        NavigationBarView navigationBarView;
        if (i != 0 && (navigationBarView = this.mNavigationBarControllerLazy.get().getNavigationBarView(i)) != null) {
            if (z) {
                navigationBarView.getRootView().setVisibility(0);
            } else {
                navigationBarView.getRootView().setVisibility(8);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [android.hardware.display.DisplayManager$DisplayListener, com.android.keyguard.KeyguardDisplayManager$1] */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.keyguard.KeyguardDisplayManager$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardDisplayManager(android.content.Context r4, dagger.Lazy<com.android.systemui.navigationbar.NavigationBarController> r5, com.android.keyguard.dagger.KeyguardStatusViewComponent.Factory r6, java.util.concurrent.Executor r7) {
        /*
            r3 = this;
            r3.<init>()
            r0 = 0
            r3.mMediaRouter = r0
            android.view.DisplayInfo r1 = new android.view.DisplayInfo
            r1.<init>()
            r3.mTmpDisplayInfo = r1
            android.util.SparseArray r1 = new android.util.SparseArray
            r1.<init>()
            r3.mPresentations = r1
            com.android.keyguard.KeyguardDisplayManager$1 r1 = new com.android.keyguard.KeyguardDisplayManager$1
            r1.<init>()
            r3.mDisplayListener = r1
            com.android.keyguard.KeyguardDisplayManager$2 r2 = new com.android.keyguard.KeyguardDisplayManager$2
            r2.<init>()
            r3.mMediaRouterCallback = r2
            r3.mContext = r4
            r3.mNavigationBarControllerLazy = r5
            r3.mKeyguardStatusViewComponentFactory = r6
            com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1 r5 = new com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1
            r6 = 0
            r5.<init>(r3, r6)
            r7.execute(r5)
            java.lang.Class<android.hardware.display.DisplayManager> r5 = android.hardware.display.DisplayManager.class
            java.lang.Object r4 = r4.getSystemService(r5)
            android.hardware.display.DisplayManager r4 = (android.hardware.display.DisplayManager) r4
            r3.mDisplayService = r4
            r4.registerDisplayListener(r1, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardDisplayManager.<init>(android.content.Context, dagger.Lazy, com.android.keyguard.dagger.KeyguardStatusViewComponent$Factory, java.util.concurrent.Executor):void");
    }
}
