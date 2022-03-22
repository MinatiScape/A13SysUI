package com.android.systemui.navigationbar.gestural;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.DeviceConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.StatsEvent;
import android.util.StatsLog;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.ISystemGestureExclusionListener;
import android.view.IWindowManager;
import android.view.InputMonitor;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import androidx.coordinatorlayout.R$styleable;
import com.android.internal.policy.GestureNavigationSettingsObserver;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputChannelCompat$InputEventReceiver;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.EdgeBackGestureHandlerProto;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda5;
import com.google.android.systemui.gesture.BackGestureTfClassifierProviderGoogle;
import com.google.protobuf.nano.MessageNano;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class EdgeBackGestureHandler extends CurrentUserTracker implements PluginListener<NavigationEdgeBackPlugin>, ProtoTraceable<SystemUiTraceProto> {
    public static final int MAX_LONG_PRESS_TIMEOUT = SystemProperties.getInt("gestures.back_timeout", 250);
    public BackAnimation mBackAnimation;
    public R$styleable mBackGestureTfClassifierProvider;
    public float mBottomGestureHeight;
    public final Context mContext;
    public boolean mDisabledForQuickstep;
    public final int mDisplayId;
    public NavigationEdgeBackPlugin mEdgeBackPlugin;
    public int mEdgeWidthLeft;
    public int mEdgeWidthRight;
    public final FalsingManager mFalsingManager;
    public boolean mGestureBlockingActivityRunning;
    public final GestureNavigationSettingsObserver mGestureNavigationSettingsObserver;
    public InputChannelCompat$InputEventReceiver mInputEventReceiver;
    public InputMonitor mInputMonitor;
    public boolean mIsAttached;
    public boolean mIsBackGestureAllowed;
    public boolean mIsEnabled;
    public boolean mIsGesturalModeEnabled;
    public boolean mIsInPipMode;
    public boolean mIsNavBarShownTransiently;
    public boolean mIsOnLeftEdge;
    public int mLeftInset;
    public final int mLongPressTimeout;
    public int mMLEnableWidth;
    public float mMLModelThreshold;
    public float mMLResults;
    public final Executor mMainExecutor;
    public final NavigationModeController mNavigationModeController;
    public final OverviewProxyService mOverviewProxyService;
    public String mPackageName;
    public final PluginManager mPluginManager;
    public final ProtoTracer mProtoTracer;
    public int mRightInset;
    public Runnable mStateChangeCallback;
    public int mSysUiFlags;
    public final SysUiState mSysUiState;
    public float mTouchSlop;
    public boolean mUseMLModel;
    public final ViewConfiguration mViewConfiguration;
    public Map<String, Integer> mVocab;
    public final WindowManager mWindowManager;
    public final IWindowManager mWindowManagerService;
    public AnonymousClass1 mGestureExclusionListener = new AnonymousClass1();
    public AnonymousClass2 mQuickSwitchListener = new OverviewProxyService.OverviewProxyListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.2
        @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
        public final void onPrioritizedRotation(int i) {
            boolean z;
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            edgeBackGestureHandler.mStartingQuickstepRotation = i;
            int rotation = edgeBackGestureHandler.mContext.getResources().getConfiguration().windowConfiguration.getRotation();
            int i2 = edgeBackGestureHandler.mStartingQuickstepRotation;
            if (i2 <= -1 || i2 == rotation) {
                z = false;
            } else {
                z = true;
            }
            edgeBackGestureHandler.mDisabledForQuickstep = z;
        }
    };
    public AnonymousClass3 mTaskStackListener = new TaskStackChangeListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.3
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onActivityPinned() {
            EdgeBackGestureHandler.this.mIsInPipMode = true;
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onActivityUnpinned() {
            EdgeBackGestureHandler.this.mIsInPipMode = false;
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskCreated(ComponentName componentName) {
            if (componentName != null) {
                EdgeBackGestureHandler.this.mPackageName = componentName.getPackageName();
                return;
            }
            EdgeBackGestureHandler.this.mPackageName = "_UNKNOWN";
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskStackChanged() {
            ComponentName componentName;
            boolean z;
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            Objects.requireNonNull(edgeBackGestureHandler);
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.sInstance.getRunningTask();
            if (runningTask == null) {
                componentName = null;
            } else {
                componentName = runningTask.topActivity;
            }
            if (componentName != null) {
                edgeBackGestureHandler.mPackageName = componentName.getPackageName();
            } else {
                edgeBackGestureHandler.mPackageName = "_UNKNOWN";
            }
            if (componentName == null || !edgeBackGestureHandler.mGestureBlockingActivities.contains(componentName)) {
                z = false;
            } else {
                z = true;
            }
            edgeBackGestureHandler.mGestureBlockingActivityRunning = z;
        }
    };
    public AnonymousClass4 mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.4
        public final void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (!"systemui".equals(properties.getNamespace())) {
                return;
            }
            if (properties.getKeyset().contains("back_gesture_ml_model_threshold") || properties.getKeyset().contains("use_back_gesture_ml_model") || properties.getKeyset().contains("back_gesture_ml_model_name")) {
                EdgeBackGestureHandler.this.updateMLModelState();
            }
        }
    };
    public final ArrayList mGestureBlockingActivities = new ArrayList();
    public final Point mDisplaySize = new Point();
    public final Rect mPipExcludedBounds = new Rect();
    public final Rect mNavBarOverlayExcludedBounds = new Rect();
    public final Region mExcludeRegion = new Region();
    public final Region mUnrestrictedExcludeRegion = new Region();
    public int mStartingQuickstepRotation = -1;
    public final PointF mDownPoint = new PointF();
    public final PointF mEndPoint = new PointF();
    public boolean mThresholdCrossed = false;
    public boolean mAllowGesture = false;
    public boolean mLogGesture = false;
    public boolean mInRejectedExclusion = false;
    public LogArray mPredictionLog = new LogArray();
    public LogArray mGestureLogInsideInsets = new LogArray();
    public LogArray mGestureLogOutsideInsets = new LogArray();
    public final AnonymousClass5 mBackCallback = new NavigationEdgeBackPlugin.BackCallback() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.5
        @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin.BackCallback
        public final void cancelBack() {
            EdgeBackGestureHandler.this.logGesture(4);
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            OverviewProxyService overviewProxyService = edgeBackGestureHandler.mOverviewProxyService;
            PointF pointF = edgeBackGestureHandler.mDownPoint;
            overviewProxyService.notifyBackAction(false, (int) pointF.x, (int) pointF.y, false, !edgeBackGestureHandler.mIsOnLeftEdge);
        }

        @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin.BackCallback
        public final void triggerBack() {
            EdgeBackGestureHandler.this.mFalsingManager.isFalseTouch(16);
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            OverviewProxyService overviewProxyService = edgeBackGestureHandler.mOverviewProxyService;
            PointF pointF = edgeBackGestureHandler.mDownPoint;
            int i = 1;
            overviewProxyService.notifyBackAction(true, (int) pointF.x, (int) pointF.y, false, !edgeBackGestureHandler.mIsOnLeftEdge);
            EdgeBackGestureHandler edgeBackGestureHandler2 = EdgeBackGestureHandler.this;
            if (edgeBackGestureHandler2.mInRejectedExclusion) {
                i = 2;
            }
            edgeBackGestureHandler2.logGesture(i);
        }
    };
    public final AnonymousClass6 mSysUiStateCallback = new AnonymousClass6();

    /* renamed from: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ISystemGestureExclusionListener.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public AnonymousClass1() {
        }

        public final void onSystemGestureExclusionChanged(int i, Region region, Region region2) {
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            if (i == edgeBackGestureHandler.mDisplayId) {
                edgeBackGestureHandler.mMainExecutor.execute(new PipTaskOrganizer$$ExternalSyntheticLambda5(this, region, region2, 1));
            }
        }
    }

    /* renamed from: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements SysUiState.SysUiStateCallback {
        public AnonymousClass6() {
        }

        @Override // com.android.systemui.model.SysUiState.SysUiStateCallback
        public final void onSystemUiStateChanged(int i) {
            EdgeBackGestureHandler.this.mSysUiFlags = i;
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final BroadcastDispatcher mBroadcastDispatcher;
        public final Executor mExecutor;
        public final FalsingManager mFalsingManager;
        public final NavigationModeController mNavigationModeController;
        public final OverviewProxyService mOverviewProxyService;
        public final PluginManager mPluginManager;
        public final ProtoTracer mProtoTracer;
        public final SysUiState mSysUiState;
        public final ViewConfiguration mViewConfiguration;
        public final WindowManager mWindowManager;
        public final IWindowManager mWindowManagerService;

        public final EdgeBackGestureHandler create(Context context) {
            return new EdgeBackGestureHandler(context, this.mOverviewProxyService, this.mSysUiState, this.mPluginManager, this.mExecutor, this.mBroadcastDispatcher, this.mProtoTracer, this.mNavigationModeController, this.mViewConfiguration, this.mWindowManager, this.mWindowManagerService, this.mFalsingManager);
        }

        public Factory(OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager) {
            this.mOverviewProxyService = overviewProxyService;
            this.mSysUiState = sysUiState;
            this.mPluginManager = pluginManager;
            this.mExecutor = executor;
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mProtoTracer = protoTracer;
            this.mNavigationModeController = navigationModeController;
            this.mViewConfiguration = viewConfiguration;
            this.mWindowManager = windowManager;
            this.mWindowManagerService = iWindowManager;
            this.mFalsingManager = falsingManager;
        }
    }

    public final void cancelGesture(MotionEvent motionEvent) {
        this.mAllowGesture = false;
        this.mLogGesture = false;
        this.mInRejectedExclusion = false;
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(3);
        this.mEdgeBackPlugin.onMotionEvent(obtain);
        obtain.recycle();
    }

    public final void onNavBarAttached() {
        this.mIsAttached = true;
        ProtoTracer protoTracer = this.mProtoTracer;
        Objects.requireNonNull(protoTracer);
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = protoTracer.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer);
        synchronized (frameProtoTracer.mLock) {
            frameProtoTracer.mTraceables.add(this);
        }
        this.mOverviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this.mQuickSwitchListener);
        SysUiState sysUiState = this.mSysUiState;
        AnonymousClass6 r1 = this.mSysUiStateCallback;
        Objects.requireNonNull(sysUiState);
        sysUiState.mCallbacks.add(r1);
        r1.onSystemUiStateChanged(sysUiState.mFlags);
        updateIsEnabled();
        startTracking();
    }

    public final void onNavBarDetached() {
        this.mIsAttached = false;
        ProtoTracer protoTracer = this.mProtoTracer;
        Objects.requireNonNull(protoTracer);
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = protoTracer.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer);
        synchronized (frameProtoTracer.mLock) {
            frameProtoTracer.mTraceables.remove(this);
        }
        this.mOverviewProxyService.removeCallback((OverviewProxyService.OverviewProxyListener) this.mQuickSwitchListener);
        SysUiState sysUiState = this.mSysUiState;
        AnonymousClass6 r1 = this.mSysUiStateCallback;
        Objects.requireNonNull(sysUiState);
        sysUiState.mCallbacks.remove(r1);
        updateIsEnabled();
        stopTracking();
    }

    /* loaded from: classes.dex */
    public static class LogArray extends ArrayDeque<String> {
        private final int mLength = 10;

        public final void log(String str) {
            if (size() >= this.mLength) {
                removeFirst();
            }
            addLast(str);
            Log.d("NoBackGesture", str);
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$5] */
    /* JADX WARN: Type inference failed for: r8v2, types: [com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$2] */
    /* JADX WARN: Type inference failed for: r8v3, types: [com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$3] */
    public EdgeBackGestureHandler(Context context, OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager) {
        super(broadcastDispatcher);
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = executor;
        this.mOverviewProxyService = overviewProxyService;
        this.mSysUiState = sysUiState;
        this.mPluginManager = pluginManager;
        this.mProtoTracer = protoTracer;
        this.mNavigationModeController = navigationModeController;
        this.mViewConfiguration = viewConfiguration;
        this.mWindowManager = windowManager;
        this.mWindowManagerService = iWindowManager;
        this.mFalsingManager = falsingManager;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(context.getString(17040010));
        if (unflattenFromString != null) {
            String packageName = unflattenFromString.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            try {
                Resources resourcesForApplication = packageManager.getResourcesForApplication(packageManager.getApplicationInfo(packageName, 9728));
                int identifier = resourcesForApplication.getIdentifier("gesture_blocking_activities", "array", packageName);
                if (identifier == 0) {
                    Log.e("EdgeBackGestureHandler", "No resource found for gesture-blocking activities");
                } else {
                    for (String str : resourcesForApplication.getStringArray(identifier)) {
                        this.mGestureBlockingActivities.add(ComponentName.unflattenFromString(str));
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("EdgeBackGestureHandler", "Failed to add gesture blocking activities", e);
            }
        }
        this.mLongPressTimeout = Math.min(MAX_LONG_PRESS_TIMEOUT, ViewConfiguration.getLongPressTimeout());
        this.mGestureNavigationSettingsObserver = new GestureNavigationSettingsObserver(this.mContext.getMainThreadHandler(), this.mContext, new WMShell$7$$ExternalSyntheticLambda1(this, 2));
        updateCurrentUserResources();
    }

    public final void dump(PrintWriter printWriter) {
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "EdgeBackGestureHandler:", "  mIsEnabled="), this.mIsEnabled, printWriter, "  mIsAttached="), this.mIsAttached, printWriter, "  mIsBackGestureAllowed="), this.mIsBackGestureAllowed, printWriter, "  mIsGesturalModeEnabled="), this.mIsGesturalModeEnabled, printWriter, "  mIsNavBarShownTransiently="), this.mIsNavBarShownTransiently, printWriter, "  mGestureBlockingActivityRunning="), this.mGestureBlockingActivityRunning, printWriter, "  mAllowGesture="), this.mAllowGesture, printWriter, "  mUseMLModel="), this.mUseMLModel, printWriter, "  mDisabledForQuickstep="), this.mDisabledForQuickstep, printWriter, "  mStartingQuickstepRotation=");
        m.append(this.mStartingQuickstepRotation);
        printWriter.println(m.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("  mInRejectedExclusion=");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mInRejectedExclusion, printWriter, "  mExcludeRegion=");
        m2.append(this.mExcludeRegion);
        printWriter.println(m2.toString());
        printWriter.println("  mUnrestrictedExcludeRegion=" + this.mUnrestrictedExcludeRegion);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mIsInPipMode=");
        StringBuilder m3 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb2, this.mIsInPipMode, printWriter, "  mPipExcludedBounds=");
        m3.append(this.mPipExcludedBounds);
        printWriter.println(m3.toString());
        printWriter.println("  mNavBarOverlayExcludedBounds=" + this.mNavBarOverlayExcludedBounds);
        printWriter.println("  mEdgeWidthLeft=" + this.mEdgeWidthLeft);
        printWriter.println("  mEdgeWidthRight=" + this.mEdgeWidthRight);
        printWriter.println("  mLeftInset=" + this.mLeftInset);
        printWriter.println("  mRightInset=" + this.mRightInset);
        printWriter.println("  mMLEnableWidth=" + this.mMLEnableWidth);
        printWriter.println("  mMLModelThreshold=" + this.mMLModelThreshold);
        printWriter.println("  mTouchSlop=" + this.mTouchSlop);
        printWriter.println("  mBottomGestureHeight=" + this.mBottomGestureHeight);
        printWriter.println("  mPredictionLog=" + String.join("\n", this.mPredictionLog));
        printWriter.println("  mGestureLogInsideInsets=" + String.join("\n", this.mGestureLogInsideInsets));
        printWriter.println("  mGestureLogOutsideInsets=" + String.join("\n", this.mGestureLogOutsideInsets));
        printWriter.println("  mEdgeBackPlugin=" + this.mEdgeBackPlugin);
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.dump(printWriter);
        }
    }

    public final void logGesture(int i) {
        String str;
        int i2;
        float f;
        if (this.mLogGesture) {
            this.mLogGesture = false;
            if (!this.mUseMLModel || !this.mVocab.containsKey(this.mPackageName) || this.mVocab.get(this.mPackageName).intValue() >= 100) {
                str = "";
            } else {
                str = this.mPackageName;
            }
            PointF pointF = this.mDownPoint;
            float f2 = pointF.y;
            int i3 = (int) f2;
            if (this.mIsOnLeftEdge) {
                i2 = 1;
            } else {
                i2 = 2;
            }
            int i4 = (int) pointF.x;
            int i5 = (int) f2;
            PointF pointF2 = this.mEndPoint;
            int i6 = (int) pointF2.x;
            int i7 = (int) pointF2.y;
            int i8 = this.mEdgeWidthLeft + this.mLeftInset;
            int i9 = this.mDisplaySize.x - (this.mEdgeWidthRight + this.mRightInset);
            if (this.mUseMLModel) {
                f = this.mMLResults;
            } else {
                f = -2.0f;
            }
            StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
            newBuilder.setAtomId(224);
            newBuilder.writeInt(i);
            newBuilder.writeInt(i3);
            newBuilder.writeInt(i2);
            newBuilder.writeInt(i4);
            newBuilder.writeInt(i5);
            newBuilder.writeInt(i6);
            newBuilder.writeInt(i7);
            newBuilder.writeInt(i8);
            newBuilder.writeInt(i9);
            newBuilder.writeFloat(f);
            newBuilder.writeString(str);
            newBuilder.usePooledBuffer();
            StatsLog.write(newBuilder.build());
        }
    }

    public final void onConfigurationChanged(Configuration configuration) {
        boolean z;
        if (this.mStartingQuickstepRotation > -1) {
            int rotation = configuration.windowConfiguration.getRotation();
            int i = this.mStartingQuickstepRotation;
            if (i <= -1 || i == rotation) {
                z = false;
            } else {
                z = true;
            }
            this.mDisabledForQuickstep = z;
        }
        Log.d("NoBackGesture", "Config changed: config=" + configuration);
        updateDisplaySize();
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin, Context context) {
        setEdgeBackPlugin(navigationEdgeBackPlugin);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext, this.mBackAnimation));
    }

    public final void setEdgeBackPlugin(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        NavigationEdgeBackPlugin navigationEdgeBackPlugin2 = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin2 != null) {
            navigationEdgeBackPlugin2.onDestroy();
        }
        this.mEdgeBackPlugin = navigationEdgeBackPlugin;
        navigationEdgeBackPlugin.setBackCallback(this.mBackCallback);
        NavigationEdgeBackPlugin navigationEdgeBackPlugin3 = this.mEdgeBackPlugin;
        Resources resources = this.mContext.getResources();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(resources.getDimensionPixelSize(2131166603), resources.getDimensionPixelSize(2131166601), 2024, 280, -3);
        layoutParams.accessibilityTitle = this.mContext.getString(2131952863);
        layoutParams.windowAnimations = 0;
        layoutParams.privateFlags |= 2097168;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("EdgeBackGestureHandler");
        m.append(this.mContext.getDisplayId());
        layoutParams.setTitle(m.toString());
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTrustedOverlay();
        navigationEdgeBackPlugin3.setLayoutParams(layoutParams);
        updateDisplaySize();
    }

    public final void updateCurrentUserResources() {
        Resources resources = this.mNavigationModeController.getCurrentUserContext().getResources();
        this.mEdgeWidthLeft = this.mGestureNavigationSettingsObserver.getLeftSensitivity(resources);
        this.mEdgeWidthRight = this.mGestureNavigationSettingsObserver.getRightSensitivity(resources);
        this.mIsBackGestureAllowed = !this.mGestureNavigationSettingsObserver.areNavigationButtonForcedVisible();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mBottomGestureHeight = TypedValue.applyDimension(1, DeviceConfig.getFloat("systemui", "back_gesture_bottom_height", resources.getDimension(17105362) / displayMetrics.density), displayMetrics);
        int applyDimension = (int) TypedValue.applyDimension(1, 12.0f, displayMetrics);
        this.mMLEnableWidth = applyDimension;
        int i = this.mEdgeWidthRight;
        if (applyDimension > i) {
            this.mMLEnableWidth = i;
        }
        int i2 = this.mMLEnableWidth;
        int i3 = this.mEdgeWidthLeft;
        if (i2 > i3) {
            this.mMLEnableWidth = i3;
        }
        this.mTouchSlop = this.mViewConfiguration.getScaledTouchSlop() * DeviceConfig.getFloat("systemui", "back_gesture_slop_multiplier", 0.75f);
    }

    public final void updateDisplaySize() {
        Rect bounds = this.mWindowManager.getMaximumWindowMetrics().getBounds();
        this.mDisplaySize.set(bounds.width(), bounds.height());
        Log.d("NoBackGesture", "Update display size: mDisplaySize=" + this.mDisplaySize);
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setDisplaySize(this.mDisplaySize);
        }
    }

    public final void updateIsEnabled() {
        boolean z;
        if (!this.mIsAttached || !this.mIsGesturalModeEnabled) {
            z = false;
        } else {
            z = true;
        }
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            InputChannelCompat$InputEventReceiver inputChannelCompat$InputEventReceiver = this.mInputEventReceiver;
            if (inputChannelCompat$InputEventReceiver != null) {
                inputChannelCompat$InputEventReceiver.mReceiver.dispose();
                this.mInputEventReceiver = null;
            }
            InputMonitor inputMonitor = this.mInputMonitor;
            if (inputMonitor != null) {
                inputMonitor.dispose();
                this.mInputMonitor = null;
            }
            NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
            if (navigationEdgeBackPlugin != null) {
                navigationEdgeBackPlugin.onDestroy();
                this.mEdgeBackPlugin = null;
            }
            if (!this.mIsEnabled) {
                this.mGestureNavigationSettingsObserver.unregister();
                Log.d("NoBackGesture", "Unregister display listener");
                this.mPluginManager.removePluginListener(this);
                TaskStackChangeListeners.INSTANCE.unregisterTaskStackListener(this.mTaskStackListener);
                DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.unregisterSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e) {
                    Log.e("EdgeBackGestureHandler", "Failed to unregister window manager callbacks", e);
                }
            } else {
                this.mGestureNavigationSettingsObserver.register();
                updateDisplaySize();
                Log.d("NoBackGesture", "Register display listener");
                TaskStackChangeListeners.INSTANCE.registerTaskStackListener(this.mTaskStackListener);
                final Executor executor = this.mMainExecutor;
                Objects.requireNonNull(executor);
                DeviceConfig.addOnPropertiesChangedListener("systemui", new Executor() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda1
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        executor.execute(runnable);
                    }
                }, this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.registerSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e2) {
                    Log.e("EdgeBackGestureHandler", "Failed to register window manager callbacks", e2);
                }
                InputMonitor monitorGestureInput = InputManager.getInstance().monitorGestureInput("edge-swipe", this.mDisplayId);
                this.mInputMonitor = monitorGestureInput;
                this.mInputEventReceiver = new InputChannelCompat$InputEventReceiver(monitorGestureInput.getInputChannel(), Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda0
                    /* JADX WARN: Removed duplicated region for block: B:77:0x01ae  */
                    /* JADX WARN: Removed duplicated region for block: B:78:0x01b1  */
                    /* JADX WARN: Removed duplicated region for block: B:85:0x01e7  */
                    /* JADX WARN: Removed duplicated region for block: B:89:0x01ee  */
                    /* JADX WARN: Removed duplicated region for block: B:92:0x01fe  */
                    /* JADX WARN: Removed duplicated region for block: B:94:0x0216  */
                    /* JADX WARN: Removed duplicated region for block: B:95:0x0219  */
                    @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct add '--show-bad-code' argument
                    */
                    public final void onInputEvent(android.view.InputEvent r20) {
                        /*
                            Method dump skipped, instructions count: 945
                            To view this dump add '--comments-level debug' option
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler$$ExternalSyntheticLambda0.onInputEvent(android.view.InputEvent):void");
                    }
                });
                setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext, this.mBackAnimation));
                this.mPluginManager.addPluginListener((PluginListener) this, NavigationEdgeBackPlugin.class, false);
            }
            updateMLModelState();
        }
    }

    public final void updateMLModelState() {
        boolean z;
        if (!this.mIsEnabled || !DeviceConfig.getBoolean("systemui", "use_back_gesture_ml_model", false)) {
            z = false;
        } else {
            z = true;
        }
        if (z != this.mUseMLModel) {
            if (z) {
                this.mBackGestureTfClassifierProvider = SystemUIFactory.mFactory.createBackGestureTfClassifierProvider(this.mContext.getAssets(), DeviceConfig.getString("systemui", "back_gesture_ml_model_name", "backgesture"));
                this.mMLModelThreshold = DeviceConfig.getFloat("systemui", "back_gesture_ml_model_threshold", 0.9f);
                R$styleable r$styleable = this.mBackGestureTfClassifierProvider;
                Objects.requireNonNull(r$styleable);
                if (r$styleable instanceof BackGestureTfClassifierProviderGoogle) {
                    Trace.beginSection("EdgeBackGestureHandler#loadVocab");
                    this.mVocab = this.mBackGestureTfClassifierProvider.loadVocab(this.mContext.getAssets());
                    Trace.endSection();
                    this.mUseMLModel = true;
                    return;
                }
            }
            this.mUseMLModel = false;
            R$styleable r$styleable2 = this.mBackGestureTfClassifierProvider;
            if (r$styleable2 != null) {
                r$styleable2.release();
                this.mBackGestureTfClassifierProvider = null;
            }
        }
    }

    @Override // com.android.systemui.shared.tracing.ProtoTraceable
    public final void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.edgeBackGestureHandler == null) {
            systemUiTraceProto.edgeBackGestureHandler = new EdgeBackGestureHandlerProto();
        }
        systemUiTraceProto.edgeBackGestureHandler.allowGesture = this.mAllowGesture;
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
    public final void onUserSwitched(int i) {
        updateIsEnabled();
        updateCurrentUserResources();
    }
}
