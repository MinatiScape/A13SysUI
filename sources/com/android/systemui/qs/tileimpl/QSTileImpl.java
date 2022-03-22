package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTile.State;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSEvent;
import com.android.systemui.qs.QSFooterView$$ExternalSyntheticLambda0;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SideLabelTileLayout;
import com.android.systemui.qs.logging.QSLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class QSTileImpl<TState extends QSTile.State> implements QSTile, LifecycleOwner, Dumpable {
    public final String TAG;
    public final ActivityStarter mActivityStarter;
    public final Context mContext;
    public RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    public final FalsingManager mFalsingManager;
    public final QSTileImpl<TState>.H mHandler;
    public final QSHost mHost;
    public final InstanceId mInstanceId;
    public int mIsFullQs;
    public final MetricsLogger mMetricsLogger;
    public final QSLogger mQSLogger;
    public volatile int mReadyState;
    public final StatusBarStateController mStatusBarStateController;
    public String mTileSpec;
    public TState mTmpState;
    public final UiEventLogger mUiEventLogger;
    public final Handler mUiHandler;
    public static final boolean DEBUG = Log.isLoggable("Tile", 3);
    public static final Object ARG_SHOW_TRANSIENT_ENABLING = new Object();
    public final ArraySet<Object> mListeners = new ArraySet<>();
    public final ArrayList<QSTile.Callback> mCallbacks = new ArrayList<>();
    public final Object mStaleListener = new Object();
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);
    public TState mState = newTileState();

    /* loaded from: classes.dex */
    public final class H extends Handler {
        @VisibleForTesting
        public static final int STALE = 11;

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            try {
                int i = message.what;
                boolean z = true;
                if (i == 1) {
                    QSTileImpl qSTileImpl = QSTileImpl.this;
                    QSTile.Callback callback = (QSTile.Callback) message.obj;
                    boolean z2 = QSTileImpl.DEBUG;
                    Objects.requireNonNull(qSTileImpl);
                    qSTileImpl.mCallbacks.add(callback);
                    callback.onStateChanged(qSTileImpl.mState);
                } else if (i == 8) {
                    QSTileImpl qSTileImpl2 = QSTileImpl.this;
                    boolean z3 = QSTileImpl.DEBUG;
                    Objects.requireNonNull(qSTileImpl2);
                    qSTileImpl2.mCallbacks.clear();
                } else if (i == 9) {
                    QSTileImpl qSTileImpl3 = QSTileImpl.this;
                    boolean z4 = QSTileImpl.DEBUG;
                    Objects.requireNonNull(qSTileImpl3);
                    qSTileImpl3.mCallbacks.remove((QSTile.Callback) message.obj);
                } else if (i == 2) {
                    QSTileImpl qSTileImpl4 = QSTileImpl.this;
                    if (qSTileImpl4.mState.disabledByPolicy) {
                        QSTileImpl.this.mActivityStarter.postStartActivityDismissingKeyguard(RestrictedLockUtils.getShowAdminSupportDetailsIntent(qSTileImpl4.mEnforcedAdmin), 0);
                        return;
                    }
                    qSTileImpl4.handleClick((View) message.obj);
                } else if (i == 3) {
                    QSTileImpl.this.handleSecondaryClick((View) message.obj);
                } else if (i == 4) {
                    QSTileImpl.this.handleLongClick((View) message.obj);
                } else if (i == 5) {
                    QSTileImpl.this.handleRefreshState(message.obj);
                } else if (i == 6) {
                    QSTileImpl.this.handleUserSwitch(message.arg1);
                } else if (i == 7) {
                    QSTileImpl.this.handleDestroy();
                } else if (i == 10) {
                    QSTileImpl qSTileImpl5 = QSTileImpl.this;
                    Object obj = message.obj;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    QSTileImpl.m77$$Nest$mhandleSetListeningInternal(qSTileImpl5, obj, z);
                } else if (i == 11) {
                    QSTileImpl.this.handleStale();
                } else if (i == 12) {
                    QSTileImpl.this.handleInitialize();
                } else {
                    throw new IllegalArgumentException("Unknown msg: " + message.what);
                }
            } catch (Throwable th) {
                Log.w(QSTileImpl.this.TAG, SupportMenuInflater$$ExternalSyntheticOutline0.m("Error in ", null), th);
                QSTileImpl.this.mHost.warn();
            }
        }

        @VisibleForTesting
        public H(Looper looper) {
            super(looper);
        }
    }

    /* loaded from: classes.dex */
    public static class ResourceIcon extends QSTile.Icon {
        public static final SparseArray<QSTile.Icon> ICONS = new SparseArray<>();
        public final int mResId;

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final String toString() {
            return String.format("ResourceIcon[resId=0x%08x]", Integer.valueOf(this.mResId));
        }

        public static synchronized QSTile.Icon get(int i) {
            QSTile.Icon icon;
            synchronized (ResourceIcon.class) {
                SparseArray<QSTile.Icon> sparseArray = ICONS;
                icon = sparseArray.get(i);
                if (icon == null) {
                    icon = new ResourceIcon(i);
                    sparseArray.put(i, icon);
                }
            }
            return icon;
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof ResourceIcon) || ((ResourceIcon) obj).mResId != this.mResId) {
                return false;
            }
            return true;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getInvisibleDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public ResourceIcon(int i) {
            this.mResId = i;
        }
    }

    public abstract Intent getLongClickIntent();

    @Override // com.android.systemui.plugins.qs.QSTile
    @Deprecated
    public int getMetricsCategory() {
        return 0;
    }

    public long getStaleTimeout() {
        return 600000L;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public abstract CharSequence getTileLabel();

    public abstract void handleClick(View view);

    public void handleInitialize() {
    }

    public abstract void handleUpdateState(TState tstate, Object obj);

    public void handleUserSwitch(int i) {
        handleRefreshState(null);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return true;
    }

    public abstract TState newTileState();

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void refreshState() {
        refreshState(null);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void setDetailListening(boolean z) {
    }

    /* loaded from: classes.dex */
    public static class DrawableIcon extends QSTile.Icon {
        public final Drawable mDrawable;
        public final Drawable mInvisibleDrawable;

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final String toString() {
            return "DrawableIcon";
        }

        public DrawableIcon(Drawable drawable) {
            this.mDrawable = drawable;
            this.mInvisibleDrawable = drawable.getConstantState().newDrawable();
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getDrawable(Context context) {
            return this.mDrawable;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getInvisibleDrawable(Context context) {
            return this.mInvisibleDrawable;
        }
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void addCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(1, callback).sendToTarget();
    }

    public final void checkIfRestrictionEnforcedByAdminOnly(QSTile.State state, String str) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, this.mHost.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, str, this.mHost.getUserId())) {
            state.disabledByPolicy = false;
            this.mEnforcedAdmin = null;
            return;
        }
        state.disabledByPolicy = true;
        this.mEnforcedAdmin = checkIfRestrictionEnforced;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void click(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(925).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_CLICK, 0, getMetricsSpec(), this.mInstanceId);
        this.mQSLogger.logTileClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mHandler.obtainMessage(2, view).sendToTarget();
        }
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public QSIconView createTileView(Context context) {
        return new QSIconViewImpl(context);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void destroy() {
        this.mHandler.sendEmptyMessage(7);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(this.mState.toString());
    }

    public void handleDestroy() {
        this.mQSLogger.logTileDestroyed(this.mTileSpec, "Handle destroy");
        if (this.mListeners.size() != 0) {
            handleSetListening(false);
            this.mListeners.clear();
        }
        this.mCallbacks.clear();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mUiHandler.post(new QSFooterView$$ExternalSyntheticLambda0(this, 3));
    }

    public void handleLongClick(View view) {
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController;
        if (view != null) {
            ghostedViewLaunchAnimatorController = ActivityLaunchAnimator.Controller.fromView(view, 32);
        } else {
            ghostedViewLaunchAnimatorController = null;
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, ghostedViewLaunchAnimatorController);
    }

    public final void handleRefreshState(Object obj) {
        handleUpdateState(this.mTmpState, obj);
        boolean copyTo = this.mTmpState.copyTo(this.mState);
        if (this.mReadyState == 1) {
            this.mReadyState = 2;
            copyTo = true;
        }
        if (copyTo) {
            this.mQSLogger.logTileUpdated(this.mTileSpec, this.mState);
            if (this.mCallbacks.size() != 0) {
                for (int i = 0; i < this.mCallbacks.size(); i++) {
                    this.mCallbacks.get(i).onStateChanged(this.mState);
                }
            }
        }
        this.mHandler.removeMessages(11);
        this.mHandler.sendEmptyMessageDelayed(11, getStaleTimeout());
        setListening(this.mStaleListener, false);
    }

    public void handleSetListening(boolean z) {
        String str = this.mTileSpec;
        if (str != null) {
            this.mQSLogger.logTileChangeListening(str, z);
        }
    }

    @VisibleForTesting
    public void handleStale() {
        setListening(this.mStaleListener, true);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final boolean isTileReady() {
        if (this.mReadyState == 2) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void longClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(366).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_LONG_PRESS, 0, getMetricsSpec(), this.mInstanceId);
        this.mQSLogger.logTileLongClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(4, view).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public LogMaker populate(LogMaker logMaker) {
        TState tstate = this.mState;
        if (tstate instanceof QSTile.BooleanState) {
            logMaker.addTaggedData(928, Integer.valueOf(((QSTile.BooleanState) tstate).value ? 1 : 0));
        }
        return logMaker.setSubtype(getMetricsCategory()).addTaggedData(1593, Integer.valueOf(this.mIsFullQs)).addTaggedData(927, Integer.valueOf(this.mHost.indexOf(this.mTileSpec)));
    }

    public final void refreshState(Object obj) {
        this.mHandler.obtainMessage(5, obj).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void removeCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(9, callback).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void removeCallbacks() {
        this.mHandler.sendEmptyMessage(8);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void secondaryClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(926).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_SECONDARY_CLICK, 0, getMetricsSpec(), this.mInstanceId);
        this.mQSLogger.logTileSecondaryClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(3, view).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void setListening(Object obj, boolean z) {
        this.mHandler.obtainMessage(10, z ? 1 : 0, 0, obj).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void setTileSpec(String str) {
        this.mTileSpec = str;
        this.mState.spec = str;
        this.mTmpState.spec = str;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final void userSwitch(int i) {
        this.mHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    /* renamed from: -$$Nest$mhandleSetListeningInternal  reason: not valid java name */
    public static void m77$$Nest$mhandleSetListeningInternal(QSTileImpl qSTileImpl, Object obj, boolean z) {
        Objects.requireNonNull(qSTileImpl);
        if (z) {
            if (qSTileImpl.mListeners.add(obj) && qSTileImpl.mListeners.size() == 1) {
                if (DEBUG) {
                    Log.d(qSTileImpl.TAG, "handleSetListening true");
                }
                qSTileImpl.handleSetListening(z);
                qSTileImpl.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda0(qSTileImpl, 0));
            }
        } else if (qSTileImpl.mListeners.remove(obj) && qSTileImpl.mListeners.size() == 0) {
            if (DEBUG) {
                Log.d(qSTileImpl.TAG, "handleSetListening false");
            }
            qSTileImpl.handleSetListening(z);
            qSTileImpl.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda1(qSTileImpl, 0));
        }
        Iterator<Object> it = qSTileImpl.mListeners.iterator();
        while (it.hasNext()) {
            if (SideLabelTileLayout.class.equals(it.next().getClass())) {
                qSTileImpl.mIsFullQs = 1;
                return;
            }
        }
        qSTileImpl.mIsFullQs = 0;
    }

    public QSTileImpl(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Tile.");
        m.append(getClass().getSimpleName());
        this.TAG = m.toString();
        this.mHost = qSHost;
        this.mContext = qSHost.getContext();
        this.mInstanceId = qSHost.getNewInstanceId();
        this.mUiEventLogger = qSHost.getUiEventLogger();
        this.mUiHandler = handler;
        this.mHandler = new H(looper);
        this.mFalsingManager = falsingManager;
        this.mQSLogger = qSLogger;
        this.mMetricsLogger = metricsLogger;
        this.mStatusBarStateController = statusBarStateController;
        this.mActivityStarter = activityStarter;
        TState newTileState = newTileState();
        this.mTmpState = newTileState;
        TState tstate = this.mState;
        String str = this.mTileSpec;
        tstate.spec = str;
        newTileState.spec = str;
        handler.post(new DozeScreenState$$ExternalSyntheticLambda0(this, 4));
    }

    public void handleSecondaryClick(View view) {
        handleClick(view);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final InstanceId getInstanceId() {
        return this.mInstanceId;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public String getMetricsSpec() {
        return this.mTileSpec;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final TState getState() {
        return this.mState;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public final String getTileSpec() {
        return this.mTileSpec;
    }
}
