package com.android.systemui.statusbar.policy;

import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda32;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class BrightnessMirrorController implements CallbackController<BrightnessMirrorListener> {
    public FrameLayout mBrightnessMirror;
    public int mBrightnessMirrorBackgroundPadding;
    public final NotificationShadeDepthController mDepthController;
    public final NotificationPanelViewController mNotificationPanel;
    public final NotificationShadeWindowView mStatusBarWindow;
    public BrightnessSliderController mToggleSliderController;
    public final BrightnessSliderController.Factory mToggleSliderFactory;
    public final Consumer<Boolean> mVisibilityCallback;
    public final ArraySet<BrightnessMirrorListener> mBrightnessMirrorListeners = new ArraySet<>();
    public final int[] mInt2Cache = new int[2];
    public int mLastBrightnessSliderWidth = -1;

    /* loaded from: classes.dex */
    public interface BrightnessMirrorListener {
        void onBrightnessMirrorReinflated();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(BrightnessMirrorListener brightnessMirrorListener) {
        BrightnessMirrorListener brightnessMirrorListener2 = brightnessMirrorListener;
        Objects.requireNonNull(brightnessMirrorListener2);
        this.mBrightnessMirrorListeners.add(brightnessMirrorListener2);
    }

    public final void reinflate() {
        int indexOfChild = this.mStatusBarWindow.indexOfChild(this.mBrightnessMirror);
        this.mStatusBarWindow.removeView(this.mBrightnessMirror);
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mStatusBarWindow.getContext()).inflate(2131624016, (ViewGroup) this.mStatusBarWindow, false);
        this.mBrightnessMirror = frameLayout;
        BrightnessSliderController create = this.mToggleSliderFactory.create(frameLayout.getContext(), this.mBrightnessMirror);
        create.init();
        this.mBrightnessMirror.addView(create.mView, -1, -2);
        this.mToggleSliderController = create;
        this.mStatusBarWindow.addView(this.mBrightnessMirror, indexOfChild);
        for (int i = 0; i < this.mBrightnessMirrorListeners.size(); i++) {
            this.mBrightnessMirrorListeners.valueAt(i).onBrightnessMirrorReinflated();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(BrightnessMirrorListener brightnessMirrorListener) {
        this.mBrightnessMirrorListeners.remove(brightnessMirrorListener);
    }

    public BrightnessMirrorController(NotificationShadeWindowView notificationShadeWindowView, NotificationPanelViewController notificationPanelViewController, NotificationShadeDepthController notificationShadeDepthController, BrightnessSliderController.Factory factory, StatusBar$$ExternalSyntheticLambda32 statusBar$$ExternalSyntheticLambda32) {
        this.mStatusBarWindow = notificationShadeWindowView;
        this.mToggleSliderFactory = factory;
        FrameLayout frameLayout = (FrameLayout) notificationShadeWindowView.findViewById(2131427604);
        this.mBrightnessMirror = frameLayout;
        BrightnessSliderController create = factory.create(frameLayout.getContext(), this.mBrightnessMirror);
        create.init();
        this.mBrightnessMirror.addView(create.mView, -1, -2);
        this.mToggleSliderController = create;
        this.mNotificationPanel = notificationPanelViewController;
        this.mDepthController = notificationShadeDepthController;
        TaskView$$ExternalSyntheticLambda4 taskView$$ExternalSyntheticLambda4 = new TaskView$$ExternalSyntheticLambda4(this, 9);
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mPanelAlphaEndAction = taskView$$ExternalSyntheticLambda4;
        this.mVisibilityCallback = statusBar$$ExternalSyntheticLambda32;
    }
}
