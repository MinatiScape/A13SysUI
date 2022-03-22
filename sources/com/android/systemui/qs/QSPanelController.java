package com.android.systemui.qs;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.leanback.R$raw;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler;
import com.android.systemui.settings.brightness.BrightnessMirrorHandler$brightnessMirrorListener$1;
import com.android.systemui.settings.brightness.BrightnessSliderController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public final class QSPanelController extends QSPanelControllerBase<QSPanel> {
    public final BrightnessController mBrightnessController;
    public final BrightnessMirrorHandler mBrightnessMirrorHandler;
    public final BrightnessSliderController mBrightnessSliderController;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public final QSFgsManagerFooter mQSFgsManagerFooter;
    public final QSCustomizerController mQsCustomizerController;
    public final QSSecurityFooter mQsSecurityFooter;
    public final QSTileRevealController.Factory mQsTileRevealControllerFactory;
    public final TunerService mTunerService;
    public final AnonymousClass1 mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() { // from class: com.android.systemui.qs.QSPanelController.1
        @Override // com.android.systemui.qs.QSPanel.OnConfigurationChangedListener
        public final void onConfigurationChange(Configuration configuration) {
            float f;
            int i;
            QSPanelController qSPanelController = QSPanelController.this;
            Objects.requireNonNull(qSPanelController);
            MediaHost mediaHost = qSPanelController.mMediaHost;
            if (Utils.shouldUseSplitNotificationShade(qSPanelController.getResources())) {
                f = 0.0f;
            } else {
                f = 1.0f;
            }
            mediaHost.setExpansion(f);
            ((QSPanel) QSPanelController.this.mView).updateResources();
            QSSecurityFooter qSSecurityFooter = QSPanelController.this.mQsSecurityFooter;
            Objects.requireNonNull(qSSecurityFooter);
            R$raw.updateFontSize(qSSecurityFooter.mFooterText, 2131166920);
            if (qSSecurityFooter.mIsMovable) {
                Resources resources = qSSecurityFooter.mContext.getResources();
                qSSecurityFooter.mFooterText.setMaxLines(resources.getInteger(2131493027));
                int dimensionPixelSize = resources.getDimensionPixelSize(2131166865);
                qSSecurityFooter.mRootView.setPaddingRelative(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
                int dimensionPixelSize2 = resources.getDimensionPixelSize(2131166867);
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) qSSecurityFooter.mRootView.getLayoutParams();
                marginLayoutParams.bottomMargin = dimensionPixelSize2;
                if (resources.getConfiguration().orientation == 1) {
                    i = -1;
                } else {
                    i = -2;
                }
                marginLayoutParams.width = i;
                qSSecurityFooter.mRootView.setLayoutParams(marginLayoutParams);
            }
            qSSecurityFooter.mRootView.setBackground(qSSecurityFooter.mContext.getDrawable(2131232604));
            QSPanel qSPanel = (QSPanel) QSPanelController.this.mView;
            Objects.requireNonNull(qSPanel);
            if (qSPanel.mListening) {
                QSPanelController.this.refreshAllTiles();
            }
            QSPanelController qSPanelController2 = QSPanelController.this;
            ((QSPanel) qSPanelController2.mView).switchSecurityFooter(qSPanelController2.mShouldUseSplitNotificationShade);
        }
    };
    public AnonymousClass2 mTileLayoutTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.qs.QSPanelController.2
        @Override // android.view.View.OnTouchListener
        public final boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 1) {
                return false;
            }
            QSPanelController.this.mFalsingManager.isFalseTouch(15);
            return false;
        }
    };

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.qs.QSPanelController$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.qs.QSPanelController$2] */
    public QSPanelController(QSPanel qSPanel, QSFgsManagerFooter qSFgsManagerFooter, QSSecurityFooter qSSecurityFooter, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, QSTileRevealController.Factory factory, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory2, BrightnessSliderController.Factory factory3, FalsingManager falsingManager, FeatureFlags featureFlags) {
        super(qSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mQSFgsManagerFooter = qSFgsManagerFooter;
        this.mQsSecurityFooter = qSSecurityFooter;
        this.mTunerService = tunerService;
        this.mQsCustomizerController = qSCustomizerController;
        this.mQsTileRevealControllerFactory = factory;
        this.mFalsingManager = falsingManager;
        BrightnessSliderController create = factory3.create(getContext(), qSPanel);
        this.mBrightnessSliderController = create;
        View view = create.mView;
        Objects.requireNonNull(qSPanel);
        View view2 = qSPanel.mBrightnessView;
        if (view2 != null) {
            qSPanel.removeView(view2);
            qSPanel.mMovableContentStartIndex--;
        }
        qSPanel.addView(view, 0);
        qSPanel.mBrightnessView = view;
        qSPanel.setBrightnessViewMargin();
        qSPanel.mMovableContentStartIndex++;
        Objects.requireNonNull(factory2);
        BrightnessController brightnessController = new BrightnessController(factory2.mContext, create, factory2.mBroadcastDispatcher, factory2.mBackgroundHandler);
        this.mBrightnessController = brightnessController;
        this.mBrightnessMirrorHandler = new BrightnessMirrorHandler(brightnessController);
        this.mFeatureFlags = featureFlags;
        qSPanel.mUseNewFooter = featureFlags.isEnabled(Flags.NEW_FOOTER);
    }

    public final void setListening(boolean z, boolean z2) {
        boolean z3;
        if (!z || (!z2 && !this.mShouldUseSplitNotificationShade)) {
            z3 = false;
        } else {
            z3 = true;
        }
        setListening(z3);
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        if (qSPanel.mListening) {
            refreshAllTiles();
        }
        if (!this.mFeatureFlags.isEnabled(Flags.NEW_FOOTER)) {
            this.mQSFgsManagerFooter.setListening(z);
            QSSecurityFooter qSSecurityFooter = this.mQsSecurityFooter;
            Objects.requireNonNull(qSSecurityFooter);
            if (z) {
                qSSecurityFooter.mSecurityController.addCallback(qSSecurityFooter.mCallback);
                qSSecurityFooter.mHandler.sendEmptyMessage(1);
            } else {
                qSSecurityFooter.mSecurityController.removeCallback(qSSecurityFooter.mCallback);
            }
        }
        if (z) {
            BrightnessController brightnessController = this.mBrightnessController;
            Objects.requireNonNull(brightnessController);
            brightnessController.mBackgroundHandler.post(brightnessController.mStartListeningRunnable);
            return;
        }
        BrightnessController brightnessController2 = this.mBrightnessController;
        Objects.requireNonNull(brightnessController2);
        brightnessController2.mBackgroundHandler.post(brightnessController2.mStopListeningRunnable);
        brightnessController2.mControlValueInitialized = false;
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    public final QSTileRevealController createTileRevealController() {
        QSTileRevealController.Factory factory = this.mQsTileRevealControllerFactory;
        Objects.requireNonNull(factory);
        return new QSTileRevealController(factory.mContext, this, (PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout(), factory.mQsCustomizerController);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mTunerService.removeTunable((TunerService.Tunable) this.mView);
        QSPanel qSPanel = (QSPanel) this.mView;
        AnonymousClass1 r1 = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(qSPanel);
        qSPanel.mOnConfigurationChangedListeners.remove(r1);
        BrightnessMirrorHandler brightnessMirrorHandler = this.mBrightnessMirrorHandler;
        Objects.requireNonNull(brightnessMirrorHandler);
        BrightnessMirrorController brightnessMirrorController = brightnessMirrorHandler.mirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.mBrightnessMirrorListeners.remove(brightnessMirrorHandler.brightnessMirrorListener);
        }
        super.onViewDetached();
    }

    public final void refreshAllTiles() {
        final BrightnessController brightnessController = this.mBrightnessController;
        Objects.requireNonNull(brightnessController);
        brightnessController.mBackgroundHandler.post(new Runnable() { // from class: com.android.systemui.settings.brightness.BrightnessController.10
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                BrightnessController brightnessController2 = brightnessController;
                ToggleSlider toggleSlider = brightnessController2.mControl;
                RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(brightnessController2.mContext, "no_config_brightness", brightnessController2.mUserTracker.getCurrentUserId());
                BrightnessSliderController brightnessSliderController = (BrightnessSliderController) toggleSlider;
                Objects.requireNonNull(brightnessSliderController);
                BrightnessSliderView brightnessSliderView = (BrightnessSliderView) brightnessSliderController.mView;
                Objects.requireNonNull(brightnessSliderView);
                ToggleSeekBar toggleSeekBar = brightnessSliderView.mSlider;
                if (checkIfRestrictionEnforced == null) {
                    z = true;
                } else {
                    z = false;
                }
                toggleSeekBar.setEnabled(z);
                ToggleSeekBar toggleSeekBar2 = brightnessSliderView.mSlider;
                Objects.requireNonNull(toggleSeekBar2);
                toggleSeekBar2.mEnforcedAdmin = checkIfRestrictionEnforced;
            }
        });
        Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.refreshState();
        }
        QSFgsManagerFooter qSFgsManagerFooter = this.mQSFgsManagerFooter;
        Objects.requireNonNull(qSFgsManagerFooter);
        qSFgsManagerFooter.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(qSFgsManagerFooter, 0));
        QSSecurityFooter qSSecurityFooter = this.mQsSecurityFooter;
        Objects.requireNonNull(qSSecurityFooter);
        qSSecurityFooter.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onInit() {
        float f;
        super.onInit();
        MediaHost mediaHost = this.mMediaHost;
        if (Utils.shouldUseSplitNotificationShade(getResources())) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        mediaHost.setExpansion(f);
        MediaHost mediaHost2 = this.mMediaHost;
        Objects.requireNonNull(mediaHost2);
        MediaHost.MediaHostStateHolder mediaHostStateHolder = mediaHost2.state;
        Objects.requireNonNull(mediaHostStateHolder);
        if (!Boolean.FALSE.equals(Boolean.valueOf(mediaHostStateHolder.showsOnlyActiveMedia))) {
            mediaHostStateHolder.showsOnlyActiveMedia = false;
            Function0<Unit> function0 = mediaHostStateHolder.changedListener;
            if (function0 != null) {
                function0.invoke();
            }
        }
        this.mMediaHost.init(0);
        this.mQsCustomizerController.init();
        this.mBrightnessSliderController.init();
        this.mQSFgsManagerFooter.init();
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        super.onViewAttached();
        updateMediaDisappearParameters();
        this.mTunerService.addTunable((TunerService.Tunable) this.mView, "qs_show_brightness");
        ((QSPanel) this.mView).updateResources();
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        if (qSPanel.mListening) {
            refreshAllTiles();
        }
        QSPanel qSPanel2 = (QSPanel) this.mView;
        AnonymousClass1 r1 = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(qSPanel2);
        qSPanel2.mOnConfigurationChangedListeners.add(r1);
        if (!this.mFeatureFlags.isEnabled(Flags.NEW_FOOTER)) {
            QSPanel qSPanel3 = (QSPanel) this.mView;
            QSSecurityFooter qSSecurityFooter = this.mQsSecurityFooter;
            Objects.requireNonNull(qSSecurityFooter);
            View view = qSSecurityFooter.mRootView;
            boolean z = this.mShouldUseSplitNotificationShade;
            Objects.requireNonNull(qSPanel3);
            qSPanel3.mSecurityFooter = view;
            qSPanel3.switchSecurityFooter(z);
            QSPanel qSPanel4 = (QSPanel) this.mView;
            QSFgsManagerFooter qSFgsManagerFooter = this.mQSFgsManagerFooter;
            Objects.requireNonNull(qSFgsManagerFooter);
            View view2 = qSFgsManagerFooter.mRootView;
            Objects.requireNonNull(qSPanel4);
            qSPanel4.mFgsManagerFooter = view2;
            QSPanel.switchToParent(qSPanel4.mFgsManagerFooter, qSPanel4, qSPanel4.indexOfChild(qSPanel4.mFooter) + 1, qSPanel4.getDumpableTag());
        }
        switchTileLayout(true);
        BrightnessMirrorHandler brightnessMirrorHandler = this.mBrightnessMirrorHandler;
        Objects.requireNonNull(brightnessMirrorHandler);
        BrightnessMirrorController brightnessMirrorController = brightnessMirrorHandler.mirrorController;
        if (brightnessMirrorController != null) {
            BrightnessMirrorHandler$brightnessMirrorListener$1 brightnessMirrorHandler$brightnessMirrorListener$1 = brightnessMirrorHandler.brightnessMirrorListener;
            Objects.requireNonNull(brightnessMirrorHandler$brightnessMirrorListener$1);
            brightnessMirrorController.mBrightnessMirrorListeners.add(brightnessMirrorHandler$brightnessMirrorListener$1);
        }
        ((PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout()).setOnTouchListener(this.mTileLayoutTouchListener);
    }
}
