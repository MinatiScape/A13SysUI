package com.android.systemui.qs.customize;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArraySet;
import android.widget.Button;
import com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda2;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.settings.UserTracker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class TileQueryHelper {
    public final Executor mBgExecutor;
    public final Context mContext;
    public boolean mFinished;
    public TileStateListener mListener;
    public final Executor mMainExecutor;
    public final UserTracker mUserTracker;
    public final ArrayList<TileInfo> mTiles = new ArrayList<>();
    public final ArraySet<String> mSpecs = new ArraySet<>();

    /* loaded from: classes.dex */
    public class TileCollector implements QSTile.Callback {
        public final QSTileHost mQSTileHost;
        public final ArrayList mQSTileList = new ArrayList();

        public TileCollector(ArrayList arrayList, QSTileHost qSTileHost) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mQSTileList.add(new TilePair((QSTile) it.next()));
            }
            this.mQSTileHost = qSTileHost;
            if (arrayList.isEmpty()) {
                TileQueryHelper.this.mBgExecutor.execute(new ScreenDecorations$$ExternalSyntheticLambda2(this, 3));
            }
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Callback
        public final void onStateChanged(QSTile.State state) {
            Iterator it = this.mQSTileList.iterator();
            boolean z = true;
            while (it.hasNext()) {
                TilePair tilePair = (TilePair) it.next();
                if (!tilePair.mReady && tilePair.mTile.isTileReady()) {
                    tilePair.mTile.removeCallback(this);
                    tilePair.mTile.setListening(this, false);
                    tilePair.mReady = true;
                } else if (!tilePair.mReady) {
                    z = false;
                }
            }
            if (z) {
                Iterator it2 = this.mQSTileList.iterator();
                while (it2.hasNext()) {
                    QSTile qSTile = ((TilePair) it2.next()).mTile;
                    QSTile.State copy = qSTile.getState().copy();
                    copy.label = qSTile.getTileLabel();
                    qSTile.destroy();
                    TileQueryHelper.this.addTile(qSTile.getTileSpec(), null, copy, true);
                }
                TileQueryHelper tileQueryHelper = TileQueryHelper.this;
                Objects.requireNonNull(tileQueryHelper);
                tileQueryHelper.mMainExecutor.execute(new TileQueryHelper$$ExternalSyntheticLambda0(tileQueryHelper, new ArrayList(tileQueryHelper.mTiles), false));
                TileQueryHelper tileQueryHelper2 = TileQueryHelper.this;
                QSTileHost qSTileHost = this.mQSTileHost;
                Objects.requireNonNull(tileQueryHelper2);
                tileQueryHelper2.mBgExecutor.execute(new KeyguardPatternView$$ExternalSyntheticLambda0(tileQueryHelper2, qSTileHost, 3));
            }
        }
    }

    /* loaded from: classes.dex */
    public interface TileStateListener {
    }

    /* loaded from: classes.dex */
    public static class TileInfo {
        public boolean isSystem;
        public String spec;
        public QSTile.State state;

        public TileInfo(String str, QSTile.State state, boolean z) {
            this.spec = str;
            this.state = state;
            this.isSystem = z;
        }
    }

    /* loaded from: classes.dex */
    public static class TilePair {
        public boolean mReady = false;
        public QSTile mTile;

        public TilePair(QSTile qSTile) {
            this.mTile = qSTile;
        }
    }

    public final void addTile(String str, CharSequence charSequence, QSTile.State state, boolean z) {
        if (!this.mSpecs.contains(str)) {
            state.dualTarget = false;
            state.expandedAccessibilityClassName = Button.class.getName();
            if (z || TextUtils.equals(state.label, charSequence)) {
                charSequence = null;
            }
            state.secondaryLabel = charSequence;
            this.mTiles.add(new TileInfo(str, state, z));
            this.mSpecs.add(str);
        }
    }

    public TileQueryHelper(Context context, UserTracker userTracker, Executor executor, Executor executor2) {
        this.mContext = context;
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mUserTracker = userTracker;
    }
}
