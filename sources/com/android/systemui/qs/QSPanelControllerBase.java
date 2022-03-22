package com.android.systemui.qs;

import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.metrics.LogMaker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.RemeasuringLinearLayout;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizer;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.util.Utils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.animation.DisappearParameters;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public abstract class QSPanelControllerBase<T extends QSPanel> extends ViewController<T> implements Dumpable {
    public final DumpManager mDumpManager;
    public final QSTileHost mHost;
    public int mLastOrientation;
    public final MediaHost mMediaHost;
    public Consumer<Boolean> mMediaVisibilityChangedListener;
    public final MetricsLogger mMetricsLogger;
    public final QSLogger mQSLogger;
    public final QSCustomizerController mQsCustomizerController;
    public QSTileRevealController mQsTileRevealController;
    public float mRevealExpansion;
    public final UiEventLogger mUiEventLogger;
    public boolean mUsingHorizontalLayout;
    public Runnable mUsingHorizontalLayoutChangedListener;
    public final boolean mUsingMediaPlayer;
    public final ArrayList<TileRecord> mRecords = new ArrayList<>();
    public String mCachedSpecs = "";
    public final QSPanelControllerBase$$ExternalSyntheticLambda0 mQSHostCallback = new QSHost.Callback() { // from class: com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda0
        @Override // com.android.systemui.qs.QSHost.Callback
        public final void onTilesChanged() {
            QSPanelControllerBase.this.setTiles();
        }
    };
    @VisibleForTesting
    public final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() { // from class: com.android.systemui.qs.QSPanelControllerBase.1
        @Override // com.android.systemui.qs.QSPanel.OnConfigurationChangedListener
        public final void onConfigurationChange(Configuration configuration) {
            QSPanelControllerBase qSPanelControllerBase = QSPanelControllerBase.this;
            qSPanelControllerBase.mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(qSPanelControllerBase.getResources());
            QSPanelControllerBase.this.onConfigurationChanged();
            int i = configuration.orientation;
            QSPanelControllerBase qSPanelControllerBase2 = QSPanelControllerBase.this;
            if (i != qSPanelControllerBase2.mLastOrientation) {
                qSPanelControllerBase2.mLastOrientation = i;
                qSPanelControllerBase2.switchTileLayout(false);
            }
        }
    };
    public final QSPanelControllerBase$$ExternalSyntheticLambda1 mMediaHostVisibilityListener = new Function1() { // from class: com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            QSPanelControllerBase qSPanelControllerBase = QSPanelControllerBase.this;
            Boolean bool = (Boolean) obj;
            Objects.requireNonNull(qSPanelControllerBase);
            Consumer<Boolean> consumer = qSPanelControllerBase.mMediaVisibilityChangedListener;
            if (consumer != null) {
                consumer.accept(bool);
            }
            qSPanelControllerBase.switchTileLayout(false);
            return null;
        }
    };
    public boolean mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(getResources());

    public QSTileRevealController createTileRevealController() {
        return null;
    }

    public void onConfigurationChanged() {
    }

    public void setTiles() {
        QSTileHost qSTileHost = this.mHost;
        Objects.requireNonNull(qSTileHost);
        setTiles(qSTileHost.mTiles.values(), false);
    }

    /* loaded from: classes.dex */
    public static final class TileRecord {
        public QSPanel.AnonymousClass1 callback;
        public QSTile tile;
        public QSTileView tileView;

        public TileRecord(QSTile qSTile, QSTileView qSTileView) {
            this.tile = qSTile;
            this.tileView = qSTileView;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.println("  Tile records:");
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile instanceof Dumpable) {
                printWriter.print("    ");
                ((Dumpable) next.tile).dump(fileDescriptor, printWriter, strArr);
                printWriter.print("    ");
                printWriter.println(next.tileView.toString());
            }
        }
        if (this.mMediaHost != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  media bounds: ");
            m.append(this.mMediaHost.getCurrentBounds());
            printWriter.println(m.toString());
        }
    }

    public final QSPanel.QSTileLayout getTileLayout() {
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        return qSPanel.mTileLayout;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        qSPanel.mTileLayout = qSPanel.getOrCreateTileLayout();
        if (qSPanel.mUsingMediaPlayer) {
            RemeasuringLinearLayout remeasuringLinearLayout = new RemeasuringLinearLayout(qSPanel.mContext);
            qSPanel.mHorizontalLinearLayout = remeasuringLinearLayout;
            remeasuringLinearLayout.setOrientation(0);
            qSPanel.mHorizontalLinearLayout.setClipChildren(false);
            qSPanel.mHorizontalLinearLayout.setClipToPadding(false);
            RemeasuringLinearLayout remeasuringLinearLayout2 = new RemeasuringLinearLayout(qSPanel.mContext);
            qSPanel.mHorizontalContentContainer = remeasuringLinearLayout2;
            remeasuringLinearLayout2.setOrientation(1);
            qSPanel.setHorizontalContentContainerClipping();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, -2, 1.0f);
            layoutParams.setMarginStart(0);
            layoutParams.setMarginEnd((int) qSPanel.mContext.getResources().getDimension(2131166889));
            layoutParams.gravity = 16;
            qSPanel.mHorizontalLinearLayout.addView(qSPanel.mHorizontalContentContainer, layoutParams);
            qSPanel.addView((View) qSPanel.mHorizontalLinearLayout, (ViewGroup.LayoutParams) new LinearLayout.LayoutParams(-1, 0, 1.0f));
        }
        QSLogger qSLogger = this.mQSLogger;
        QSPanel qSPanel2 = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel2);
        qSLogger.logAllTilesChangeListening(qSPanel2.mListening, ((QSPanel) this.mView).getDumpableTag(), "");
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        QSPanel qSPanel = (QSPanel) this.mView;
        QSPanel.OnConfigurationChangedListener onConfigurationChangedListener = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(qSPanel);
        qSPanel.mOnConfigurationChangedListeners.remove(onConfigurationChangedListener);
        QSTileHost qSTileHost = this.mHost;
        QSPanelControllerBase$$ExternalSyntheticLambda0 qSPanelControllerBase$$ExternalSyntheticLambda0 = this.mQSHostCallback;
        Objects.requireNonNull(qSTileHost);
        qSTileHost.mCallbacks.remove(qSPanelControllerBase$$ExternalSyntheticLambda0);
        QSPanel qSPanel2 = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel2);
        qSPanel2.mTileLayout.setListening(false, this.mUiEventLogger);
        MediaHost mediaHost = this.mMediaHost;
        QSPanelControllerBase$$ExternalSyntheticLambda1 qSPanelControllerBase$$ExternalSyntheticLambda1 = this.mMediaHostVisibilityListener;
        Objects.requireNonNull(mediaHost);
        mediaHost.visibleChangedListeners.remove(qSPanelControllerBase$$ExternalSyntheticLambda1);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.removeCallbacks();
        }
        this.mRecords.clear();
        this.mDumpManager.unregisterDumpable(((QSPanel) this.mView).getDumpableTag());
    }

    public final void setExpanded(boolean z) {
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        if (qSPanel.mExpanded != z) {
            this.mQSLogger.logPanelExpanded(z, ((QSPanel) this.mView).getDumpableTag());
            QSPanel qSPanel2 = (QSPanel) this.mView;
            Objects.requireNonNull(qSPanel2);
            if (qSPanel2.mExpanded != z) {
                qSPanel2.mExpanded = z;
                if (!z) {
                    QSPanel.QSTileLayout qSTileLayout = qSPanel2.mTileLayout;
                    if (qSTileLayout instanceof PagedTileLayout) {
                        ((PagedTileLayout) qSTileLayout).setCurrentItem(0, false);
                    }
                }
            }
            this.mMetricsLogger.visibility(111, z);
            if (!z) {
                this.mUiEventLogger.log(((QSPanel) this.mView).closePanelEvent());
                QSCustomizerController qSCustomizerController = this.mQsCustomizerController;
                Objects.requireNonNull(qSCustomizerController);
                if (((QSCustomizer) qSCustomizerController.mView).isShown()) {
                    this.mQsCustomizerController.hide();
                    return;
                }
                return;
            }
            this.mUiEventLogger.log(((QSPanel) this.mView).openPanelEvent());
            for (int i = 0; i < this.mRecords.size(); i++) {
                QSTile qSTile = this.mRecords.get(i).tile;
                this.mMetricsLogger.write(qSTile.populate(new LogMaker(qSTile.getMetricsCategory()).setType(1)));
            }
        }
    }

    public void setListening(boolean z) {
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        qSPanel.mListening = z;
        QSPanel qSPanel2 = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel2);
        if (qSPanel2.mTileLayout != null) {
            this.mQSLogger.logAllTilesChangeListening(z, ((QSPanel) this.mView).getDumpableTag(), this.mCachedSpecs);
            QSPanel qSPanel3 = (QSPanel) this.mView;
            Objects.requireNonNull(qSPanel3);
            qSPanel3.mTileLayout.setListening(z, this.mUiEventLogger);
        }
    }

    public final void setSquishinessFraction(float f) {
        QSPanel qSPanel = (QSPanel) this.mView;
        Objects.requireNonNull(qSPanel);
        if (Float.compare(f, qSPanel.mSquishinessFraction) != 0) {
            qSPanel.mSquishinessFraction = f;
            QSPanel.QSTileLayout qSTileLayout = qSPanel.mTileLayout;
            if (qSTileLayout != null) {
                qSTileLayout.setSquishinessFraction(f);
                if (qSPanel.getMeasuredWidth() != 0) {
                    qSPanel.updateViewPositions();
                }
            }
        }
    }

    public final boolean switchTileLayout(boolean z) {
        boolean z2;
        RemeasuringLinearLayout remeasuringLinearLayout;
        int i;
        int i2;
        RemeasuringLinearLayout remeasuringLinearLayout2;
        int i3;
        float f;
        int i4;
        int i5;
        int i6 = 2;
        int i7 = 0;
        if (!this.mShouldUseSplitNotificationShade && this.mUsingMediaPlayer && this.mMediaHost.getVisible() && this.mLastOrientation == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2 == this.mUsingHorizontalLayout && !z) {
            return false;
        }
        this.mUsingHorizontalLayout = z2;
        RemeasuringLinearLayout remeasuringLinearLayout3 = (QSPanel) this.mView;
        UniqueObjectHostView hostView = this.mMediaHost.getHostView();
        Objects.requireNonNull(remeasuringLinearLayout3);
        if (z2 != remeasuringLinearLayout3.mUsingHorizontalLayout || z) {
            remeasuringLinearLayout3.mUsingHorizontalLayout = z2;
            if (z2) {
                remeasuringLinearLayout = remeasuringLinearLayout3.mHorizontalContentContainer;
            } else {
                remeasuringLinearLayout = remeasuringLinearLayout3;
            }
            QSPanel.QSTileLayout qSTileLayout = remeasuringLinearLayout3.mTileLayout;
            if (remeasuringLinearLayout == remeasuringLinearLayout3) {
                i = remeasuringLinearLayout3.mMovableContentStartIndex;
            } else {
                i = 0;
            }
            QSPanel.switchToParent((View) qSTileLayout, remeasuringLinearLayout, i, remeasuringLinearLayout3.getDumpableTag());
            int i8 = i + 1;
            View view = remeasuringLinearLayout3.mFooter;
            if (view != null) {
                QSPanel.switchToParent(view, remeasuringLinearLayout, i8, remeasuringLinearLayout3.getDumpableTag());
            }
            if (remeasuringLinearLayout3.mUsingMediaPlayer) {
                if (z2) {
                    remeasuringLinearLayout2 = remeasuringLinearLayout3.mHorizontalLinearLayout;
                } else {
                    remeasuringLinearLayout2 = remeasuringLinearLayout3;
                }
                RemeasuringLinearLayout remeasuringLinearLayout4 = (ViewGroup) hostView.getParent();
                if (remeasuringLinearLayout4 != remeasuringLinearLayout2) {
                    if (remeasuringLinearLayout4 != null) {
                        remeasuringLinearLayout4.removeView(hostView);
                    }
                    remeasuringLinearLayout2.addView(hostView);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) hostView.getLayoutParams();
                    layoutParams.height = -2;
                    if (z2) {
                        i3 = 0;
                    } else {
                        i3 = -1;
                    }
                    layoutParams.width = i3;
                    if (z2) {
                        f = 1.0f;
                    } else {
                        f = 0.0f;
                    }
                    layoutParams.weight = f;
                    if (!z2 || (!(remeasuringLinearLayout3 instanceof QuickQSPanel))) {
                        i4 = Math.max(remeasuringLinearLayout3.mMediaTotalBottomMargin - remeasuringLinearLayout3.getPaddingBottom(), 0);
                    } else {
                        i4 = 0;
                    }
                    layoutParams.bottomMargin = i4;
                    if (!(remeasuringLinearLayout3 instanceof QuickQSPanel) || z2) {
                        i5 = 0;
                    } else {
                        i5 = remeasuringLinearLayout3.mMediaTopMargin;
                    }
                    layoutParams.topMargin = i5;
                }
            }
            QSPanel.QSTileLayout qSTileLayout2 = remeasuringLinearLayout3.mTileLayout;
            if (z2) {
                i2 = 2;
            } else {
                i2 = 1;
            }
            qSTileLayout2.setMinRows(i2);
            QSPanel.QSTileLayout qSTileLayout3 = remeasuringLinearLayout3.mTileLayout;
            if (!z2) {
                i6 = 4;
            }
            qSTileLayout3.setMaxColumns(i6);
            remeasuringLinearLayout3.updateMediaHostContentMargins(hostView);
            RemeasuringLinearLayout remeasuringLinearLayout5 = remeasuringLinearLayout3.mHorizontalLinearLayout;
            if (remeasuringLinearLayout5 != null && !(!(remeasuringLinearLayout3 instanceof QuickQSPanel))) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) remeasuringLinearLayout5.getLayoutParams();
                layoutParams2.bottomMargin = Math.max(remeasuringLinearLayout3.mMediaTotalBottomMargin - remeasuringLinearLayout3.getPaddingBottom(), 0);
                remeasuringLinearLayout3.mHorizontalLinearLayout.setLayoutParams(layoutParams2);
            }
            remeasuringLinearLayout3.updatePadding();
            RemeasuringLinearLayout remeasuringLinearLayout6 = remeasuringLinearLayout3.mHorizontalLinearLayout;
            if (!z2) {
                i7 = 8;
            }
            remeasuringLinearLayout6.setVisibility(i7);
        }
        updateMediaDisappearParameters();
        Runnable runnable = this.mUsingHorizontalLayoutChangedListener;
        if (runnable != null) {
            runnable.run();
        }
        return true;
    }

    public final void updateMediaDisappearParameters() {
        if (this.mUsingMediaPlayer) {
            DisappearParameters disappearParameters = this.mMediaHost.getDisappearParameters();
            if (this.mUsingHorizontalLayout) {
                Objects.requireNonNull(disappearParameters);
                disappearParameters.disappearSize.set(0.0f, 0.4f);
                disappearParameters.gonePivot.set(1.0f, 1.0f);
                disappearParameters.contentTranslationFraction.set(0.25f, 1.0f);
                disappearParameters.disappearEnd = 0.6f;
            } else {
                Objects.requireNonNull(disappearParameters);
                disappearParameters.disappearSize.set(1.0f, 0.0f);
                disappearParameters.gonePivot.set(0.0f, 1.0f);
                disappearParameters.contentTranslationFraction.set(0.0f, 1.05f);
                disappearParameters.disappearEnd = 0.95f;
            }
            Objects.requireNonNull(disappearParameters);
            disappearParameters.fadeStartPosition = 0.95f;
            disappearParameters.disappearStart = 0.0f;
            MediaHost mediaHost = this.mMediaHost;
            Objects.requireNonNull(mediaHost);
            mediaHost.state.setDisappearParameters(disappearParameters);
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda1] */
    public QSPanelControllerBase(T t, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager) {
        super(t);
        this.mHost = qSTileHost;
        this.mQsCustomizerController = qSCustomizerController;
        this.mUsingMediaPlayer = z;
        this.mMediaHost = mediaHost;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mQSLogger = qSLogger;
        this.mDumpManager = dumpManager;
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        QSTileRevealController createTileRevealController = createTileRevealController();
        this.mQsTileRevealController = createTileRevealController;
        if (createTileRevealController != null) {
            if (this.mRevealExpansion == 1.0f) {
                createTileRevealController.mHandler.postDelayed(createTileRevealController.mRevealQsTiles, 500L);
            } else {
                createTileRevealController.mHandler.removeCallbacks(createTileRevealController.mRevealQsTiles);
            }
        }
        MediaHost mediaHost = this.mMediaHost;
        QSPanelControllerBase$$ExternalSyntheticLambda1 qSPanelControllerBase$$ExternalSyntheticLambda1 = this.mMediaHostVisibilityListener;
        Objects.requireNonNull(mediaHost);
        mediaHost.visibleChangedListeners.add(qSPanelControllerBase$$ExternalSyntheticLambda1);
        QSPanel qSPanel = (QSPanel) this.mView;
        QSPanel.OnConfigurationChangedListener onConfigurationChangedListener = this.mOnConfigurationChangedListener;
        Objects.requireNonNull(qSPanel);
        qSPanel.mOnConfigurationChangedListeners.add(onConfigurationChangedListener);
        QSTileHost qSTileHost = this.mHost;
        QSPanelControllerBase$$ExternalSyntheticLambda0 qSPanelControllerBase$$ExternalSyntheticLambda0 = this.mQSHostCallback;
        Objects.requireNonNull(qSTileHost);
        qSTileHost.mCallbacks.add(qSPanelControllerBase$$ExternalSyntheticLambda0);
        setTiles();
        this.mLastOrientation = getResources().getConfiguration().orientation;
        switchTileLayout(true);
        this.mDumpManager.registerDumpable(((QSPanel) this.mView).getDumpableTag(), this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.plugins.qs.QSTile$Callback, com.android.systemui.qs.QSPanel$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setTiles(java.util.Collection<com.android.systemui.plugins.qs.QSTile> r8, boolean r9) {
        /*
            Method dump skipped, instructions count: 285
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.QSPanelControllerBase.setTiles(java.util.Collection, boolean):void");
    }
}
