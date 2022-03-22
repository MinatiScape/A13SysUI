package com.android.systemui.qs;

import android.content.res.Configuration;
import androidx.preference.R$id;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaFlags;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public final class QuickQSPanelController extends QSPanelControllerBase<QuickQSPanel> {
    public final MediaFlags mMediaFlags;
    public final QuickQSPanelController$$ExternalSyntheticLambda0 mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() { // from class: com.android.systemui.qs.QuickQSPanelController$$ExternalSyntheticLambda0
        @Override // com.android.systemui.qs.QSPanel.OnConfigurationChangedListener
        public final void onConfigurationChange(Configuration configuration) {
            QuickQSPanelController quickQSPanelController = QuickQSPanelController.this;
            Objects.requireNonNull(quickQSPanelController);
            int integer = quickQSPanelController.getResources().getInteger(2131493029);
            QuickQSPanel quickQSPanel = (QuickQSPanel) quickQSPanelController.mView;
            Objects.requireNonNull(quickQSPanel);
            if (integer != quickQSPanel.mMaxTiles) {
                QuickQSPanel quickQSPanel2 = (QuickQSPanel) quickQSPanelController.mView;
                Objects.requireNonNull(quickQSPanel2);
                quickQSPanel2.mMaxTiles = integer;
                quickQSPanelController.setTiles();
            }
        }
    };
    public final boolean mUsingCollapsedLandscapeMedia;

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.qs.QuickQSPanelController$$ExternalSyntheticLambda0] */
    public QuickQSPanelController(QuickQSPanel quickQSPanel, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, boolean z2, MediaFlags mediaFlags, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(quickQSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager);
        this.mUsingCollapsedLandscapeMedia = z2;
        this.mMediaFlags = mediaFlags;
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    public final void setTiles() {
        ArrayList arrayList = new ArrayList();
        QSTileHost qSTileHost = this.mHost;
        Objects.requireNonNull(qSTileHost);
        for (QSTile qSTile : qSTileHost.mTiles.values()) {
            arrayList.add(qSTile);
            int size = arrayList.size();
            QuickQSPanel quickQSPanel = (QuickQSPanel) this.mView;
            Objects.requireNonNull(quickQSPanel);
            if (size == quickQSPanel.mMaxTiles) {
                break;
            }
        }
        setTiles(arrayList, true);
    }

    public int getRotation() {
        return R$id.getRotation(getContext());
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onInit() {
        super.onInit();
        updateMediaExpansion();
        MediaHost mediaHost = this.mMediaHost;
        Objects.requireNonNull(mediaHost);
        MediaHost.MediaHostStateHolder mediaHostStateHolder = mediaHost.state;
        Objects.requireNonNull(mediaHostStateHolder);
        if (!Boolean.TRUE.equals(Boolean.valueOf(mediaHostStateHolder.showsOnlyActiveMedia))) {
            mediaHostStateHolder.showsOnlyActiveMedia = true;
            Function0<Unit> function0 = mediaHostStateHolder.changedListener;
            if (function0 != null) {
                function0.invoke();
            }
        }
        this.mMediaHost.init(1);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onViewAttached() {
        super.onViewAttached();
        QuickQSPanel quickQSPanel = (QuickQSPanel) this.mView;
        QuickQSPanelController$$ExternalSyntheticLambda0 quickQSPanelController$$ExternalSyntheticLambda0 = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(quickQSPanel);
        quickQSPanel.mOnConfigurationChangedListeners.add(quickQSPanelController$$ExternalSyntheticLambda0);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public final void onViewDetached() {
        super.onViewDetached();
        QuickQSPanel quickQSPanel = (QuickQSPanel) this.mView;
        QuickQSPanelController$$ExternalSyntheticLambda0 quickQSPanelController$$ExternalSyntheticLambda0 = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(quickQSPanel);
        quickQSPanel.mOnConfigurationChangedListeners.remove(quickQSPanelController$$ExternalSyntheticLambda0);
    }

    public final void updateMediaExpansion() {
        int rotation = getRotation();
        boolean z = true;
        if (!(rotation == 1 || rotation == 3)) {
            z = false;
        }
        if (!this.mMediaFlags.useMediaSessionLayout() || (this.mUsingCollapsedLandscapeMedia && z)) {
            this.mMediaHost.setExpansion(0.0f);
        } else {
            this.mMediaHost.setExpansion(1.0f);
        }
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    public final void setListening(boolean z) {
        super.setListening(z);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    public final void onConfigurationChanged() {
        updateMediaExpansion();
    }
}
