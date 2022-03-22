package com.android.systemui;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.InsetsState;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.decor.OverlayWindow;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.onehanded.BackgroundWindowManager;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedTutorialHandler;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ ScreenDecorations$2$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        BackgroundWindowManager backgroundWindowManager;
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations.AnonymousClass2 r0 = (ScreenDecorations.AnonymousClass2) this.f$0;
                View view = (View) this.f$1;
                for (int i = 0; i < 4; i++) {
                    OverlayWindow[] overlayWindowArr = ScreenDecorations.this.mOverlays;
                    if (overlayWindowArr[i] != null) {
                        OverlayWindow overlayWindow = overlayWindowArr[i];
                        Objects.requireNonNull(overlayWindow);
                        ViewGroup viewGroup = overlayWindow.rootView;
                        if (viewGroup.findViewById(view.getId()) != null) {
                            viewGroup.setVisibility(4);
                        }
                    }
                }
                Objects.requireNonNull(r0);
                return;
            case 1:
                DozeServiceHost dozeServiceHost = (DozeServiceHost) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) this.f$1;
                Objects.requireNonNull(dozeServiceHost);
                Objects.requireNonNull(notificationEntry);
                notificationEntry.mPulseSupressed = true;
                dozeServiceHost.mNotificationIconAreaController.updateAodNotificationIcons();
                return;
            case 2:
                DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl = (DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl) this.f$0;
                InsetsState insetsState = (InsetsState) this.f$1;
                int i2 = DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl.$r8$clinit;
                Objects.requireNonNull(displayWindowInsetsControllerImpl);
                DisplayInsetsController.PerDisplay perDisplay = DisplayInsetsController.PerDisplay.this;
                Objects.requireNonNull(perDisplay);
                CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = DisplayInsetsController.this.mListeners.get(perDisplay.mDisplayId);
                if (copyOnWriteArrayList != null) {
                    DisplayController displayController = DisplayInsetsController.this.mDisplayController;
                    int i3 = perDisplay.mDisplayId;
                    Objects.requireNonNull(displayController);
                    DisplayController.DisplayRecord displayRecord = displayController.mDisplays.get(i3);
                    if (displayRecord != null) {
                        displayRecord.mInsetsState = insetsState;
                        DisplayLayout displayLayout = displayRecord.mDisplayLayout;
                        Resources resources = displayRecord.mContext.getResources();
                        Objects.requireNonNull(displayLayout);
                        displayLayout.mInsetsState = insetsState;
                        displayLayout.recalcInsets(resources);
                    }
                    Iterator<DisplayInsetsController.OnInsetsChangedListener> it = copyOnWriteArrayList.iterator();
                    while (it.hasNext()) {
                        it.next().insetsChanged(insetsState);
                    }
                    return;
                }
                return;
            default:
                OneHandedController.OneHandedImpl oneHandedImpl = (OneHandedController.OneHandedImpl) this.f$0;
                Configuration configuration = (Configuration) this.f$1;
                int i4 = OneHandedController.OneHandedImpl.$r8$clinit;
                Objects.requireNonNull(oneHandedImpl);
                OneHandedController oneHandedController = OneHandedController.this;
                Objects.requireNonNull(oneHandedController);
                if (!(oneHandedController.mTutorialHandler == null || !oneHandedController.mIsOneHandedEnabled || configuration.orientation == 2)) {
                    OneHandedTutorialHandler oneHandedTutorialHandler = oneHandedController.mTutorialHandler;
                    Objects.requireNonNull(oneHandedTutorialHandler);
                    BackgroundWindowManager backgroundWindowManager2 = oneHandedTutorialHandler.mBackgroundWindowManager;
                    Objects.requireNonNull(backgroundWindowManager2);
                    int i5 = backgroundWindowManager2.mCurrentState;
                    if (i5 == 1 || i5 == 2) {
                        backgroundWindowManager2.updateThemeOnly();
                    }
                    oneHandedTutorialHandler.removeTutorialFromWindowManager();
                    int i6 = oneHandedTutorialHandler.mCurrentState;
                    if (i6 == 1 || i6 == 2) {
                        oneHandedTutorialHandler.createViewAndAttachToWindow(oneHandedTutorialHandler.mContext);
                        FrameLayout frameLayout = oneHandedTutorialHandler.mTargetViewContainer;
                        if (!(frameLayout == null || (backgroundWindowManager = oneHandedTutorialHandler.mBackgroundWindowManager) == null)) {
                            frameLayout.setBackgroundColor(backgroundWindowManager.getThemeColorForBackground());
                        }
                        oneHandedTutorialHandler.updateThemeColor();
                        oneHandedTutorialHandler.checkTransitionEnd();
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
