package com.android.wm.shell.bubbles;

import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.dream.DreamBackend$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.ComplicationTypesUpdater;
import com.android.systemui.dreams.complication.ComplicationUtils$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.complication.ComplicationUtils$$ExternalSyntheticLambda1;
import com.android.systemui.globalactions.GlobalActionsColumnLayout;
import com.android.systemui.navigationbar.NavigationBar;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda6;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.animation.AnimatableScaleMatrix;
import com.android.wm.shell.transition.Transitions;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kotlin.jvm.functions.Function1;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda19 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda19(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        Collection collection;
        switch (this.$r8$classId) {
            case 0:
                final BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                if (!bubbleStackView.mIsExpanded) {
                    bubbleStackView.mIsBubbleSwitchAnimating = false;
                    return;
                }
                AnimatableScaleMatrix animatableScaleMatrix = bubbleStackView.mExpandedViewContainerMatrix;
                Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
                PhysicsAnimator.Companion.getInstance(animatableScaleMatrix).cancel();
                PhysicsAnimator instance = PhysicsAnimator.Companion.getInstance(bubbleStackView.mExpandedViewContainerMatrix);
                instance.spring(AnimatableScaleMatrix.SCALE_X, 499.99997f, 0.0f, bubbleStackView.mScaleInSpringConfig);
                instance.spring(AnimatableScaleMatrix.SCALE_Y, 499.99997f, 0.0f, bubbleStackView.mScaleInSpringConfig);
                instance.updateListeners.add(new PhysicsAnimator.UpdateListener() { // from class: com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda13
                    @Override // com.android.wm.shell.animation.PhysicsAnimator.UpdateListener
                    public final void onAnimationUpdateForProperty(Object obj) {
                        BubbleStackView bubbleStackView2 = BubbleStackView.this;
                        AnimatableScaleMatrix animatableScaleMatrix2 = (AnimatableScaleMatrix) obj;
                        Objects.requireNonNull(bubbleStackView2);
                        bubbleStackView2.mExpandedViewContainer.setAnimationMatrix(bubbleStackView2.mExpandedViewContainerMatrix);
                    }
                });
                instance.withEndActions(new TaskView$$ExternalSyntheticLambda6(bubbleStackView, 6));
                instance.start();
                return;
            case 1:
                ComplicationTypesUpdater.AnonymousClass1 r8 = (ComplicationTypesUpdater.AnonymousClass1) this.f$0;
                int i = ComplicationTypesUpdater.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r8);
                ComplicationTypesUpdater complicationTypesUpdater = ComplicationTypesUpdater.this;
                final DreamOverlayStateController dreamOverlayStateController = complicationTypesUpdater.mDreamOverlayStateController;
                DreamBackend dreamBackend = complicationTypesUpdater.mDreamBackend;
                Objects.requireNonNull(dreamBackend);
                String string = Settings.Secure.getString(dreamBackend.mContext.getContentResolver(), "screensaver_enabled_complications");
                if (string == null) {
                    collection = dreamBackend.mDefaultEnabledComplications;
                } else if (TextUtils.isEmpty(string)) {
                    collection = new HashSet();
                } else {
                    collection = (Set) Arrays.stream(string.split(",")).map(DreamBackend$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toSet());
                }
                final int reduce = collection.stream().mapToInt(ComplicationUtils$$ExternalSyntheticLambda1.INSTANCE).reduce(0, ComplicationUtils$$ExternalSyntheticLambda0.INSTANCE);
                Objects.requireNonNull(dreamOverlayStateController);
                dreamOverlayStateController.mExecutor.execute(new Runnable() { // from class: com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        DreamOverlayStateController dreamOverlayStateController2 = DreamOverlayStateController.this;
                        int i2 = reduce;
                        Objects.requireNonNull(dreamOverlayStateController2);
                        dreamOverlayStateController2.mAvailableComplicationTypes = i2;
                        dreamOverlayStateController2.mCallbacks.forEach(DreamOverlayStateController$$ExternalSyntheticLambda4.INSTANCE);
                    }
                });
                return;
            case 2:
                GlobalActionsColumnLayout globalActionsColumnLayout = (GlobalActionsColumnLayout) this.f$0;
                int i2 = GlobalActionsColumnLayout.$r8$clinit;
                Objects.requireNonNull(globalActionsColumnLayout);
                globalActionsColumnLayout.updateSnap();
                return;
            case 3:
                NavigationBar navigationBar = (NavigationBar) this.f$0;
                Objects.requireNonNull(navigationBar);
                ButtonDispatcher homeButton = navigationBar.mNavigationBarView.getHomeButton();
                Objects.requireNonNull(homeButton);
                if (navigationBar.onHomeLongClick(homeButton.mCurrentView)) {
                    ButtonDispatcher homeButton2 = navigationBar.mNavigationBarView.getHomeButton();
                    Objects.requireNonNull(homeButton2);
                    homeButton2.mCurrentView.performHapticFeedback(0, 1);
                    return;
                }
                return;
            case 4:
                NotificationListener notificationListener = (NotificationListener) this.f$0;
                int i3 = NotificationListener.$r8$clinit;
                Objects.requireNonNull(notificationListener);
                NotificationListenerService.RankingMap rankingMap = (NotificationListenerService.RankingMap) notificationListener.mRankingMapQueue.pollFirst();
                if (rankingMap == null) {
                    Log.wtf("NotificationListener", "mRankingMapQueue was empty!");
                }
                if (!notificationListener.mRankingMapQueue.isEmpty()) {
                    long elapsedRealtime = notificationListener.mSystemClock.elapsedRealtime();
                    if (notificationListener.mSkippingRankingUpdatesSince == -1) {
                        notificationListener.mSkippingRankingUpdatesSince = elapsedRealtime;
                    }
                    if (elapsedRealtime - notificationListener.mSkippingRankingUpdatesSince < 500) {
                        return;
                    }
                }
                notificationListener.mSkippingRankingUpdatesSince = -1L;
                Iterator it = notificationListener.mNotificationHandlers.iterator();
                while (it.hasNext()) {
                    ((NotificationListener.NotificationHandler) it.next()).onNotificationRankingUpdate(rankingMap);
                }
                return;
            case 5:
                StackScrollerDecorView stackScrollerDecorView = (StackScrollerDecorView) this.f$0;
                int i4 = StackScrollerDecorView.$r8$clinit;
                Objects.requireNonNull(stackScrollerDecorView);
                stackScrollerDecorView.mContentAnimating = false;
                if (stackScrollerDecorView.getVisibility() != 8 && !stackScrollerDecorView.mIsVisible) {
                    stackScrollerDecorView.setVisibility(8);
                    stackScrollerDecorView.mWillBeGone = false;
                    stackScrollerDecorView.notifyHeightChanged(false);
                    return;
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                ((NotificationStackScrollLayout) this.f$0).invalidate();
                return;
            default:
                ((Transitions.TransitionFinishCallback) this.f$0).onTransitionFinished(null);
                return;
        }
    }
}
