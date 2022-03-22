package com.android.systemui.navigationbar;

import android.os.Handler;
import android.os.RemoteException;
import android.util.SparseArray;
import android.view.IWallpaperVisibilityListener;
import android.view.IWindowManager;
import android.view.View;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NavigationBarTransitions extends BarTransitions implements LightBarTransitionsController.DarkIntensityApplier {
    public final boolean mAllowAutoDimWallpaperNotVisible;
    public boolean mAutoDim;
    public final LightBarTransitionsController mLightTransitionsController;
    public boolean mLightsOut;
    public View mNavButtons;
    public final NavigationBarView mView;
    public final AnonymousClass1 mWallpaperVisibilityListener;
    public boolean mWallpaperVisible;
    public int mNavBarMode = 0;
    public final Handler mHandler = Handler.getMain();
    public ArrayList mDarkIntensityListeners = new ArrayList();

    /* renamed from: com.android.systemui.navigationbar.NavigationBarTransitions$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IWallpaperVisibilityListener.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
        }

        public final void onWallpaperVisibilityChanged(boolean z, int i) throws RemoteException {
            NavigationBarTransitions navigationBarTransitions = NavigationBarTransitions.this;
            navigationBarTransitions.mWallpaperVisible = z;
            navigationBarTransitions.mHandler.post(new StatusBar$$ExternalSyntheticLambda18(this, 4));
        }
    }

    /* loaded from: classes.dex */
    public interface DarkIntensityListener {
        void onDarkIntensity(float f);
    }

    public final boolean isLightsOut(int i) {
        boolean z;
        if (i == 3 || i == 6) {
            z = true;
        } else {
            z = false;
        }
        if (z || (this.mAllowAutoDimWallpaperNotVisible && this.mAutoDim && !this.mWallpaperVisible && i != 5)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public final void applyDarkIntensity(float f) {
        NavigationBarView navigationBarView = this.mView;
        Objects.requireNonNull(navigationBarView);
        SparseArray<ButtonDispatcher> sparseArray = navigationBarView.mButtonDispatchers;
        for (int size = sparseArray.size() - 1; size >= 0; size--) {
            sparseArray.valueAt(size).setDarkIntensity(f);
        }
        NavigationBarView navigationBarView2 = this.mView;
        Objects.requireNonNull(navigationBarView2);
        RotationButtonController rotationButtonController = navigationBarView2.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        rotationButtonController.mRotationButton.setDarkIntensity(f);
        Iterator it = this.mDarkIntensityListeners.iterator();
        while (it.hasNext()) {
            ((DarkIntensityListener) it.next()).onDarkIntensity(f);
        }
        if (this.mAutoDim) {
            applyLightsOut(false, true);
        }
    }

    public final void applyLightsOut(boolean z, boolean z2) {
        float f;
        int i;
        boolean isLightsOut = isLightsOut(this.mMode);
        if (z2 || isLightsOut != this.mLightsOut) {
            this.mLightsOut = isLightsOut;
            View view = this.mNavButtons;
            if (view != null) {
                view.animate().cancel();
                LightBarTransitionsController lightBarTransitionsController = this.mLightTransitionsController;
                Objects.requireNonNull(lightBarTransitionsController);
                float f2 = lightBarTransitionsController.mDarkIntensity / 10.0f;
                if (isLightsOut) {
                    f = f2 + 0.6f;
                } else {
                    f = 1.0f;
                }
                if (!z) {
                    this.mNavButtons.setAlpha(f);
                    return;
                }
                if (isLightsOut) {
                    i = 1500;
                } else {
                    i = 250;
                }
                this.mNavButtons.animate().alpha(f).setDuration(i).start();
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public final int getTintAnimationDuration() {
        if (Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) {
            return Math.max(1700, 400);
        }
        return 120;
    }

    public final void setAutoDim(boolean z) {
        if ((!z || !Utils.isGesturalModeOnDefaultDisplay(this.mView.getContext(), this.mNavBarMode)) && this.mAutoDim != z) {
            this.mAutoDim = z;
            applyLightsOut(true, false);
        }
    }

    public NavigationBarTransitions(NavigationBarView navigationBarView, CommandQueue commandQueue) {
        super(navigationBarView, 2131232510);
        AnonymousClass1 r1 = new AnonymousClass1();
        this.mWallpaperVisibilityListener = r1;
        this.mView = navigationBarView;
        this.mLightTransitionsController = new LightBarTransitionsController(navigationBarView.getContext(), this, commandQueue);
        this.mAllowAutoDimWallpaperNotVisible = navigationBarView.getContext().getResources().getBoolean(2131034143);
        try {
            this.mWallpaperVisible = ((IWindowManager) Dependency.get(IWindowManager.class)).registerWallpaperVisibilityListener(r1, 0);
        } catch (RemoteException unused) {
        }
        this.mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.navigationbar.NavigationBarTransitions$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                NavigationBarTransitions navigationBarTransitions = NavigationBarTransitions.this;
                Objects.requireNonNull(navigationBarTransitions);
                NavigationBarView navigationBarView2 = navigationBarTransitions.mView;
                Objects.requireNonNull(navigationBarView2);
                View view2 = navigationBarView2.mCurrentView;
                if (view2 != null) {
                    navigationBarTransitions.mNavButtons = view2.findViewById(2131428478);
                    navigationBarTransitions.applyLightsOut(false, true);
                }
            }
        });
        NavigationBarView navigationBarView2 = this.mView;
        Objects.requireNonNull(navigationBarView2);
        View view = navigationBarView2.mCurrentView;
        if (view != null) {
            this.mNavButtons = view.findViewById(2131428478);
        }
    }

    public final void onTransition(int i, int i2, boolean z) {
        applyModeBackground(i2, z);
        applyLightsOut(z, false);
        NavigationBarView navigationBarView = this.mView;
        Objects.requireNonNull(navigationBarView);
        if (i2 == 4) {
            navigationBarView.mRegionSamplingHelper.stop();
            navigationBarView.getLightTransitionsController().setIconsDark(false, true);
            return;
        }
        navigationBarView.mRegionSamplingHelper.start(navigationBarView.mSamplingBounds);
    }
}
