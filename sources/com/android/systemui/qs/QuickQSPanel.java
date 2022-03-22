package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
/* loaded from: classes.dex */
public class QuickQSPanel extends QSPanel {
    public boolean mDisabledByPolicy;
    public int mMaxTiles = getResources().getInteger(2131493029);

    /* loaded from: classes.dex */
    public static class QQSSideLabelTileLayout extends SideLabelTileLayout {
        public boolean mLastSelected;

        public QQSSideLabelTileLayout(Context context) {
            super(context, null);
            setClipChildren(false);
            setClipToPadding(false);
            setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            setMaxColumns(4);
        }

        @Override // com.android.systemui.qs.QSPanel.QSTileLayout
        public final void setExpansion(float f, float f2) {
            boolean z;
            if (f <= 0.0f || f >= 1.0f) {
                if (f == 1.0f || f2 < 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                if (this.mLastSelected != z) {
                    setImportantForAccessibility(4);
                    for (int i = 0; i < getChildCount(); i++) {
                        getChildAt(i).setSelected(z);
                    }
                    setImportantForAccessibility(0);
                    this.mLastSelected = z;
                }
            }
        }

        @Override // com.android.systemui.qs.TileLayout, android.view.View
        public final void onMeasure(int i, int i2) {
            updateMaxRows(10000, this.mRecords.size());
            super.onMeasure(i, i2);
        }

        @Override // com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
        public final void setListening(boolean z, UiEventLogger uiEventLogger) {
            boolean z2;
            if (this.mListening || !z) {
                z2 = false;
            } else {
                z2 = true;
            }
            super.setListening(z, uiEventLogger);
            if (z2) {
                for (int i = 0; i < getNumVisibleTiles(); i++) {
                    QSTile qSTile = this.mRecords.get(i).tile;
                    uiEventLogger.logWithInstanceId(QSEvent.QQS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
                }
            }
        }

        @Override // android.view.View
        public final void onConfigurationChanged(Configuration configuration) {
            super.onConfigurationChanged(configuration);
            updateResources();
        }

        @Override // com.android.systemui.qs.SideLabelTileLayout, com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
        public final boolean updateResources() {
            this.mCellHeightResId = 2131166903;
            boolean updateResources = super.updateResources();
            this.mMaxAllowedRows = getResources().getInteger(2131493028);
            return updateResources;
        }
    }

    @Override // com.android.systemui.qs.QSPanel
    public final String getDumpableTag() {
        return "QuickQSPanel";
    }

    @Override // com.android.systemui.qs.QSPanel
    public final void updatePadding() {
    }

    @Override // com.android.systemui.qs.QSPanel
    public final void drawTile(QSPanelControllerBase.TileRecord tileRecord, QSTile.State state) {
        if (state instanceof QSTile.SignalState) {
            QSTile.SignalState signalState = new QSTile.SignalState();
            state.copyTo(signalState);
            signalState.activityIn = false;
            signalState.activityOut = false;
            state = signalState;
        }
        tileRecord.tileView.onStateChanged(state);
    }

    @Override // com.android.systemui.qs.QSPanel
    public final QSPanel.QSTileLayout getOrCreateTileLayout() {
        return new QQSSideLabelTileLayout(this.mContext);
    }

    @Override // com.android.systemui.qs.QSPanel
    public final void setHorizontalContentContainerClipping() {
        this.mHorizontalContentContainer.setClipToPadding(false);
        this.mHorizontalContentContainer.setClipChildren(false);
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        if (this.mDisabledByPolicy) {
            if (getVisibility() != 8) {
                i = 8;
            } else {
                return;
            }
        }
        super.setVisibility(i);
    }

    public QuickQSPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.android.systemui.qs.QSPanel, com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if ("qs_show_brightness".equals(str)) {
            super.onTuningChanged(str, "0");
        }
    }

    @Override // com.android.systemui.qs.QSPanel
    public final QSEvent closePanelEvent() {
        return QSEvent.QQS_PANEL_COLLAPSED;
    }

    @Override // com.android.systemui.qs.QSPanel
    public final QSEvent openPanelEvent() {
        return QSEvent.QQS_PANEL_EXPANDED;
    }
}
