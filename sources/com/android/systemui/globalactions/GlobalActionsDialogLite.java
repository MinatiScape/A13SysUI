package com.android.systemui.globalactions;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.service.dreams.IDreamManager;
import android.sysprop.TelephonyProperties;
import android.telecom.TelecomManager;
import android.util.ArraySet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.util.ScreenshotHelper;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.MultiListLayout;
import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda2;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.assist.PhoneStateMonitor$$ExternalSyntheticLambda1;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.model.SysUiState;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.plugins.GlobalActionsPanelPlugin;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda5;
import com.android.systemui.scrim.ScrimDrawable;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeLiveData;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda6;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda5;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class GlobalActionsDialogLite implements DialogInterface.OnDismissListener, DialogInterface.OnShowListener, ConfigurationController.ConfigurationListener, GlobalActionsPanelPlugin.Callbacks, LifecycleOwner {
    @VisibleForTesting
    public static final String GLOBAL_ACTION_KEY_POWER = "power";
    public MyAdapter mAdapter;
    public final AnonymousClass7 mAirplaneModeObserver;
    public AirplaneModeAction mAirplaneModeOn;
    public final AudioManager mAudioManager;
    public final Executor mBackgroundExecutor;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final ConfigurationController mConfigurationController;
    public final Context mContext;
    public final DevicePolicyManager mDevicePolicyManager;
    @VisibleForTesting
    public ActionsDialogLite mDialog;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final SystemUIDialogManager mDialogManager;
    public final IDreamManager mDreamManager;
    public final EmergencyAffordanceManager mEmergencyAffordanceManager;
    public final GlobalSettings mGlobalSettings;
    public boolean mHasTelephony;
    public boolean mHasVibrator;
    public final IActivityManager mIActivityManager;
    public final IWindowManager mIWindowManager;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final LockPatternUtils mLockPatternUtils;
    public Handler mMainHandler;
    public final MetricsLogger mMetricsLogger;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public MyOverflowAdapter mOverflowAdapter;
    public final AnonymousClass6 mPhoneStateListener;
    public MyPowerOptionsAdapter mPowerAdapter;
    public final Resources mResources;
    public final RingerModeTracker mRingerModeTracker;
    public final ScreenshotHelper mScreenshotHelper;
    public final SecureSettings mSecureSettings;
    public final boolean mShowSilentToggle;
    public Action mSilentModeAction;
    public int mSmallestScreenWidthDp;
    public final Optional<StatusBar> mStatusBarOptional;
    public final IStatusBarService mStatusBarService;
    public final SysUiState mSysUiState;
    public final SysuiColorExtractor mSysuiColorExtractor;
    public final TelecomManager mTelecomManager;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TrustManager mTrustManager;
    public final UiEventLogger mUiEventLogger;
    public final UserManager mUserManager;
    public final GlobalActions.GlobalActionsManager mWindowManagerFuncs;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);
    @VisibleForTesting
    public final ArrayList<Action> mItems = new ArrayList<>();
    @VisibleForTesting
    public final ArrayList<Action> mOverflowItems = new ArrayList<>();
    @VisibleForTesting
    public final ArrayList<Action> mPowerItems = new ArrayList<>();
    public boolean mKeyguardShowing = false;
    public boolean mDeviceProvisioned = false;
    public ToggleState mAirplaneState = ToggleState.Off;
    public boolean mIsWaitingForEcmExit = false;
    public int mDialogPressDelay = 850;
    public AnonymousClass5 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.5
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(action) || "android.intent.action.SCREEN_OFF".equals(action)) {
                String stringExtra = intent.getStringExtra("reason");
                if (!"globalactions".equals(stringExtra)) {
                    AnonymousClass8 r2 = GlobalActionsDialogLite.this.mHandler;
                    r2.sendMessage(r2.obtainMessage(0, stringExtra));
                }
            } else if ("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED".equals(action) && !intent.getBooleanExtra("android.telephony.extra.PHONE_IN_ECM_STATE", false)) {
                GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                if (globalActionsDialogLite.mIsWaitingForEcmExit) {
                    globalActionsDialogLite.mIsWaitingForEcmExit = false;
                    GlobalActionsDialogLite.m59$$Nest$mchangeAirplaneModeSystemSetting(globalActionsDialogLite, true);
                }
            }
        }
    };
    public AnonymousClass8 mHandler = new Handler() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.8
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i != 0) {
                if (i == 1) {
                    GlobalActionsDialogLite.this.refreshSilentMode();
                    GlobalActionsDialogLite.this.mAdapter.notifyDataSetChanged();
                }
            } else if (GlobalActionsDialogLite.this.mDialog != null) {
                if ("dream".equals(message.obj)) {
                    GlobalActionsDialogLite.this.mDialog.hide();
                    GlobalActionsDialogLite.this.mDialog.dismiss();
                } else {
                    GlobalActionsDialogLite.this.mDialog.dismiss();
                }
                GlobalActionsDialogLite.this.mDialog = null;
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Action {
        View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater);

        Drawable getIcon(Context context);

        CharSequence getMessage();

        int getMessageResId();

        boolean isEnabled();

        void onPress();

        default void shouldBeSeparated() {
        }

        default boolean shouldShow() {
            return true;
        }

        boolean showBeforeProvisioning();

        void showDuringKeyguard();
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ActionsDialogLite extends SystemUIDialog implements ColorExtractor.OnColorsChangedListener {
        public static final /* synthetic */ int $r8$clinit = 0;
        public final MyAdapter mAdapter;
        public ScrimDrawable mBackgroundDrawable;
        public final SysuiColorExtractor mColorExtractor;
        public ViewGroup mContainer;
        public final Context mContext;
        public GestureDetector mGestureDetector;
        @VisibleForTesting
        public GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 <= 0.0f || Math.abs(f2) <= Math.abs(f) || motionEvent.getY() > ((Integer) ActionsDialogLite.this.mStatusBarOptional.map(PhoneStateMonitor$$ExternalSyntheticLambda1.INSTANCE$2).orElse(0)).intValue()) {
                    return false;
                }
                ActionsDialogLite.m60$$Nest$mopenShadeAndDismiss(ActionsDialogLite.this);
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (f2 >= 0.0f || f2 <= f || motionEvent.getY() > ((Integer) ActionsDialogLite.this.mStatusBarOptional.map(GlobalActionsDialogLite$ActionsDialogLite$1$$ExternalSyntheticLambda0.INSTANCE).orElse(0)).intValue()) {
                    return false;
                }
                ActionsDialogLite.m60$$Nest$mopenShadeAndDismiss(ActionsDialogLite.this);
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public final boolean onSingleTapUp(MotionEvent motionEvent) {
                ActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
                ActionsDialogLite.this.cancel();
                return false;
            }
        };
        public MultiListLayout mGlobalActionsLayout;
        public boolean mKeyguardShowing;
        public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public LockPatternUtils mLockPatternUtils;
        public final NotificationShadeWindowController mNotificationShadeWindowController;
        public final Runnable mOnRefreshCallback;
        public final MyOverflowAdapter mOverflowAdapter;
        public GlobalActionsPopupMenu mOverflowPopup;
        public final MyPowerOptionsAdapter mPowerOptionsAdapter;
        public Dialog mPowerOptionsDialog;
        public Optional<StatusBar> mStatusBarOptional;
        public final SysUiState mSysUiState;
        public UiEventLogger mUiEventLogger;
        public float mWindowDimAmount;

        public ActionsDialogLite(Context context, MyAdapter myAdapter, MyOverflowAdapter myOverflowAdapter, SysuiColorExtractor sysuiColorExtractor, NotificationShadeWindowController notificationShadeWindowController, SysUiState sysUiState, TaskView$$ExternalSyntheticLambda4 taskView$$ExternalSyntheticLambda4, boolean z, MyPowerOptionsAdapter myPowerOptionsAdapter, UiEventLogger uiEventLogger, Optional optional, KeyguardUpdateMonitor keyguardUpdateMonitor, LockPatternUtils lockPatternUtils, SystemUIDialogManager systemUIDialogManager) {
            super(context, 2132018187, false, systemUIDialogManager);
            new Binder();
            this.mContext = context;
            this.mAdapter = myAdapter;
            this.mOverflowAdapter = myOverflowAdapter;
            this.mPowerOptionsAdapter = myPowerOptionsAdapter;
            this.mColorExtractor = sysuiColorExtractor;
            this.mNotificationShadeWindowController = notificationShadeWindowController;
            this.mSysUiState = sysUiState;
            this.mOnRefreshCallback = taskView$$ExternalSyntheticLambda4;
            this.mKeyguardShowing = z;
            this.mUiEventLogger = uiEventLogger;
            this.mStatusBarOptional = optional;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mLockPatternUtils = lockPatternUtils;
            this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialog
        public final int getHeight() {
            return -1;
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialog
        public final int getWidth() {
            return -1;
        }

        public final void startAnimation(final boolean z, final GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6 globalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6) {
            final float f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            Resources resources = getContext().getResources();
            if (z) {
                f = resources.getDimension(17105457);
                ofFloat.setInterpolator(Interpolators.STANDARD);
                ofFloat.setDuration(resources.getInteger(17694730));
            } else {
                f = resources.getDimension(17105458);
                ofFloat.setInterpolator(Interpolators.STANDARD_ACCELERATE);
                ofFloat.setDuration(resources.getInteger(17694731));
            }
            final Window window = getWindow();
            final int rotation = window.getWindowManager().getDefaultDisplay().getRotation();
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f2;
                    float f3;
                    GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.ActionsDialogLite.this;
                    boolean z2 = z;
                    Window window2 = window;
                    float f4 = f;
                    int i = rotation;
                    int i2 = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                    Objects.requireNonNull(actionsDialogLite);
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    if (z2) {
                        f2 = floatValue;
                    } else {
                        f2 = 1.0f - floatValue;
                    }
                    actionsDialogLite.mGlobalActionsLayout.setAlpha(f2);
                    window2.setDimAmount(actionsDialogLite.mWindowDimAmount * f2);
                    if (z2) {
                        f3 = (1.0f - floatValue) * f4;
                    } else {
                        f3 = f4 * floatValue;
                    }
                    if (i == 0) {
                        actionsDialogLite.mGlobalActionsLayout.setTranslationX(f3);
                    } else if (i == 1) {
                        actionsDialogLite.mGlobalActionsLayout.setTranslationY(-f3);
                    } else if (i == 2) {
                        actionsDialogLite.mGlobalActionsLayout.setTranslationX(-f3);
                    } else if (i == 3) {
                        actionsDialogLite.mGlobalActionsLayout.setTranslationY(f3);
                    }
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.4
                public int mPreviousLayerType;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    ActionsDialogLite.this.mGlobalActionsLayout.setLayerType(this.mPreviousLayerType, null);
                    Runnable runnable = globalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6;
                    if (runnable != null) {
                        runnable.run();
                    }
                }

                @Override // android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator, boolean z2) {
                    this.mPreviousLayerType = ActionsDialogLite.this.mGlobalActionsLayout.getLayerType();
                    ActionsDialogLite.this.mGlobalActionsLayout.setLayerType(2, null);
                }
            });
            ofFloat.start();
        }

        @Override // android.app.Dialog, android.content.DialogInterface
        public final void dismiss() {
            GlobalActionsPopupMenu globalActionsPopupMenu = this.mOverflowPopup;
            if (globalActionsPopupMenu != null) {
                globalActionsPopupMenu.dismiss();
            }
            Dialog dialog = this.mPowerOptionsDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
            this.mNotificationShadeWindowController.setRequestTopUi(false, "GlobalActionsDialogLite");
            SysUiState sysUiState = this.mSysUiState;
            sysUiState.setFlag(32768, false);
            sysUiState.commitUpdate(this.mContext.getDisplayId());
            super.dismiss();
        }

        public final void onColorsChanged(ColorExtractor colorExtractor, int i) {
            if (this.mKeyguardShowing) {
                if ((i & 2) != 0) {
                    updateColors(colorExtractor.getColors(2), true);
                }
            } else if ((i & 1) != 0) {
                updateColors(colorExtractor.getColors(1), true);
            }
        }

        @Override // android.app.Dialog
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            if (this.mGestureDetector.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent)) {
                return true;
            }
            return false;
        }

        public final void updateColors(ColorExtractor.GradientColors gradientColors, boolean z) {
            ScrimDrawable scrimDrawable = this.mBackgroundDrawable;
            if (scrimDrawable instanceof ScrimDrawable) {
                scrimDrawable.setColor(-16777216, z);
                View decorView = getWindow().getDecorView();
                if (gradientColors.supportsDarkText()) {
                    decorView.setSystemUiVisibility(8208);
                } else {
                    decorView.setSystemUiVisibility(0);
                }
            }
        }

        /* renamed from: -$$Nest$mopenShadeAndDismiss  reason: not valid java name */
        public static void m60$$Nest$mopenShadeAndDismiss(ActionsDialogLite actionsDialogLite) {
            Objects.requireNonNull(actionsDialogLite);
            actionsDialogLite.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_TAP_OUTSIDE);
            if (((Boolean) actionsDialogLite.mStatusBarOptional.map(WMShellBaseModule$$ExternalSyntheticLambda5.INSTANCE$1).orElse(Boolean.FALSE)).booleanValue()) {
                actionsDialogLite.mStatusBarOptional.ifPresent(GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda9.INSTANCE);
            } else {
                actionsDialogLite.mStatusBarOptional.ifPresent(GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda8.INSTANCE);
            }
            actionsDialogLite.dismiss();
        }

        @Override // android.app.Dialog
        public final void onBackPressed() {
            super.onBackPressed();
            this.mUiEventLogger.log(GlobalActionsEvent.GA_CLOSE_BACK);
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
        public final void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            setContentView(2131624122);
            ViewGroup viewGroup = (ViewGroup) findViewById(16908290);
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
            viewGroup2.setClipChildren(false);
            viewGroup2.setClipToPadding(false);
            MultiListLayout multiListLayout = (MultiListLayout) findViewById(2131428030);
            this.mGlobalActionsLayout = multiListLayout;
            View.AccessibilityDelegate accessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.2
                @Override // android.view.View.AccessibilityDelegate
                public final boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityEvent.getText().add(ActionsDialogLite.this.mContext.getString(17040396));
                    return true;
                }
            };
            Objects.requireNonNull(multiListLayout);
            multiListLayout.getListView().setAccessibilityDelegate(accessibilityDelegate);
            MultiListLayout multiListLayout2 = this.mGlobalActionsLayout;
            GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5 globalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5 = new GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5(this);
            Objects.requireNonNull(multiListLayout2);
            multiListLayout2.mRotationListener = globalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda5;
            MultiListLayout multiListLayout3 = this.mGlobalActionsLayout;
            MyAdapter myAdapter = this.mAdapter;
            Objects.requireNonNull(multiListLayout3);
            multiListLayout3.mAdapter = myAdapter;
            ViewGroup viewGroup3 = (ViewGroup) findViewById(2131428024);
            this.mContainer = viewGroup3;
            viewGroup3.setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda2
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.ActionsDialogLite.this;
                    int i = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                    Objects.requireNonNull(actionsDialogLite);
                    actionsDialogLite.mGestureDetector.onTouchEvent(motionEvent);
                    return view.onTouchEvent(motionEvent);
                }
            });
            View findViewById = findViewById(2131428028);
            if (findViewById != null) {
                if (this.mOverflowAdapter.getCount() > 0) {
                    findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda1
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            final GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.ActionsDialogLite.this;
                            int i = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                            Objects.requireNonNull(actionsDialogLite);
                            GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(new ContextThemeWrapper(actionsDialogLite.mContext, 2132017471), false);
                            globalActionsPopupMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda3
                                @Override // android.widget.AdapterView.OnItemClickListener
                                public final void onItemClick(AdapterView adapterView, View view2, int i2, long j) {
                                    GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite2 = GlobalActionsDialogLite.ActionsDialogLite.this;
                                    int i3 = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                                    Objects.requireNonNull(actionsDialogLite2);
                                    GlobalActionsDialogLite.MyOverflowAdapter myOverflowAdapter = actionsDialogLite2.mOverflowAdapter;
                                    Objects.requireNonNull(myOverflowAdapter);
                                    GlobalActionsDialogLite.Action action = GlobalActionsDialogLite.this.mOverflowItems.get(i2);
                                    if (!(action instanceof GlobalActionsDialogLite.SilentModeTriStateAction)) {
                                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                                        if (globalActionsDialogLite.mDialog != null) {
                                            globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                                            GlobalActionsDialogLite.this.mDialog.dismiss();
                                        } else {
                                            Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                                        }
                                        action.onPress();
                                    }
                                }
                            });
                            globalActionsPopupMenu.mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda4
                                @Override // android.widget.AdapterView.OnItemLongClickListener
                                public final boolean onItemLongClick(AdapterView adapterView, View view2, int i2, long j) {
                                    GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite2 = GlobalActionsDialogLite.ActionsDialogLite.this;
                                    int i3 = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                                    Objects.requireNonNull(actionsDialogLite2);
                                    GlobalActionsDialogLite.MyOverflowAdapter myOverflowAdapter = actionsDialogLite2.mOverflowAdapter;
                                    Objects.requireNonNull(myOverflowAdapter);
                                    GlobalActionsDialogLite.Action action = GlobalActionsDialogLite.this.mOverflowItems.get(i2);
                                    if (!(action instanceof GlobalActionsDialogLite.LongPressAction)) {
                                        return false;
                                    }
                                    GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                                    if (globalActionsDialogLite.mDialog != null) {
                                        globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                                        GlobalActionsDialogLite.this.mDialog.dismiss();
                                    } else {
                                        Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                                    }
                                    return ((GlobalActionsDialogLite.LongPressAction) action).onLongPress();
                                }
                            };
                            globalActionsPopupMenu.setAnchorView(actionsDialogLite.findViewById(2131428028));
                            globalActionsPopupMenu.setAdapter(actionsDialogLite.mOverflowAdapter);
                            actionsDialogLite.mOverflowPopup = globalActionsPopupMenu;
                            globalActionsPopupMenu.show();
                        }
                    });
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams.setMarginEnd(0);
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams);
                } else {
                    findViewById.setVisibility(8);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mGlobalActionsLayout.getLayoutParams();
                    layoutParams2.setMarginEnd(this.mContext.getResources().getDimensionPixelSize(2131165778));
                    this.mGlobalActionsLayout.setLayoutParams(layoutParams2);
                }
            }
            if (this.mBackgroundDrawable == null) {
                this.mBackgroundDrawable = new ScrimDrawable();
            }
            boolean userHasTrust = this.mKeyguardUpdateMonitor.getUserHasTrust(KeyguardUpdateMonitor.getCurrentUser());
            if (this.mKeyguardShowing && userHasTrust) {
                this.mLockPatternUtils.requireCredentialEntry(KeyguardUpdateMonitor.getCurrentUser());
                final View inflate = LayoutInflater.from(this.mContext).inflate(2131624125, this.mContainer, false);
                final int recommendedTimeoutMillis = ((AccessibilityManager) getContext().getSystemService("accessibility")).getRecommendedTimeoutMillis(3500, 2);
                inflate.setVisibility(0);
                inflate.setAlpha(0.0f);
                this.mContainer.addView(inflate);
                inflate.animate().alpha(1.0f).setDuration(333L).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ActionsDialogLite.3
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        inflate.animate().alpha(0.0f).setDuration(333L).setStartDelay(recommendedTimeoutMillis).setListener(null);
                    }
                });
            }
            this.mWindowDimAmount = getWindow().getAttributes().dimAmount;
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
        public final void onStart() {
            ColorExtractor.GradientColors gradientColors;
            super.onStart();
            MultiListLayout multiListLayout = this.mGlobalActionsLayout;
            Objects.requireNonNull(multiListLayout);
            if (multiListLayout.mAdapter != null) {
                multiListLayout.onUpdateList();
                if (this.mBackgroundDrawable instanceof ScrimDrawable) {
                    this.mColorExtractor.addOnColorsChangedListener(this);
                    SysuiColorExtractor sysuiColorExtractor = this.mColorExtractor;
                    Objects.requireNonNull(sysuiColorExtractor);
                    if (sysuiColorExtractor.mHasMediaArtwork) {
                        gradientColors = sysuiColorExtractor.mBackdropColors;
                    } else {
                        gradientColors = sysuiColorExtractor.mNeutralColorsLock;
                    }
                    updateColors(gradientColors, false);
                    return;
                }
                return;
            }
            throw new IllegalStateException("mAdapter must be set before calling updateList");
        }

        @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
        public final void onStop() {
            super.onStop();
            this.mColorExtractor.removeOnColorsChangedListener(this);
        }

        @Override // android.app.Dialog
        public final void show() {
            boolean z;
            super.show();
            this.mNotificationShadeWindowController.setRequestTopUi(true, "GlobalActionsDialogLite");
            SysUiState sysUiState = this.mSysUiState;
            sysUiState.setFlag(32768, true);
            sysUiState.commitUpdate(this.mContext.getDisplayId());
            if (getWindow().getAttributes().windowAnimations == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                startAnimation(true, null);
                setDismissOverride(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda7
                    /* JADX WARN: Multi-variable type inference failed */
                    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6] */
                    @Override // java.lang.Runnable
                    public final void run() {
                        final GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.ActionsDialogLite.this;
                        int i = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                        Objects.requireNonNull(actionsDialogLite);
                        actionsDialogLite.startAnimation(false, new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$ActionsDialogLite$$ExternalSyntheticLambda6
                            @Override // java.lang.Runnable
                            public final void run() {
                                GlobalActionsDialogLite.ActionsDialogLite actionsDialogLite2 = GlobalActionsDialogLite.ActionsDialogLite.this;
                                int i2 = GlobalActionsDialogLite.ActionsDialogLite.$r8$clinit;
                                Objects.requireNonNull(actionsDialogLite2);
                                actionsDialogLite2.setDismissOverride(null);
                                actionsDialogLite2.hide();
                                actionsDialogLite2.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    /* loaded from: classes.dex */
    public class AirplaneModeAction extends ToggleAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public AirplaneModeAction() {
            super(17302485, 17302487, 17040398, 17040397);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        public final void changeStateFromPress(boolean z) {
            ToggleState toggleState;
            if (GlobalActionsDialogLite.this.mHasTelephony && !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                if (z) {
                    toggleState = ToggleState.TurningOn;
                } else {
                    toggleState = ToggleState.TurningOff;
                }
                this.mState = toggleState;
                GlobalActionsDialogLite.this.mAirplaneState = toggleState;
            }
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        public final void onToggle(boolean z) {
            if (!GlobalActionsDialogLite.this.mHasTelephony || !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
                GlobalActionsDialogLite.m59$$Nest$mchangeAirplaneModeSystemSetting(GlobalActionsDialogLite.this, z);
                return;
            }
            GlobalActionsDialogLite.this.mIsWaitingForEcmExit = true;
            Intent intent = new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null);
            intent.addFlags(268435456);
            GlobalActionsDialogLite.this.mContext.startActivity(intent);
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class BugReportAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public BugReportAction() {
            super(17302489, 17039811);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public final boolean onLongPress() {
            if (ActivityManager.isUserAMonkey()) {
                return false;
            }
            try {
                GlobalActionsDialogLite.this.mMetricsLogger.action(293);
                GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_LONG_PRESS);
                GlobalActionsDialogLite.this.mIActivityManager.requestFullBugReport();
                GlobalActionsDialogLite.this.mStatusBarOptional.ifPresent(QSTileHost$$ExternalSyntheticLambda5.INSTANCE$2);
            } catch (RemoteException unused) {
            }
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            if (!ActivityManager.isUserAMonkey()) {
                GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                globalActionsDialogLite.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.BugReportAction.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            GlobalActionsDialogLite.this.mMetricsLogger.action(292);
                            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_BUGREPORT_PRESS);
                            if (!GlobalActionsDialogLite.this.mIActivityManager.launchBugReportHandlerApp()) {
                                Log.w("GlobalActionsDialogLite", "Bugreport handler could not be launched");
                                GlobalActionsDialogLite.this.mIActivityManager.requestInteractiveBugReport();
                            }
                            GlobalActionsDialogLite.this.mStatusBarOptional.ifPresent(SystemActions$$ExternalSyntheticLambda2.INSTANCE$2);
                        } catch (RemoteException unused) {
                        }
                    }
                }, globalActionsDialogLite.mDialogPressDelay);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public abstract class EmergencyAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void shouldBeSeparated() {
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public EmergencyAction(int i) {
            super(i, 17040383);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.SinglePressAction, com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            View create = super.create(context, view, viewGroup, layoutInflater);
            Objects.requireNonNull(GlobalActionsDialogLite.this);
            int color = context.getResources().getColor(2131099877);
            Objects.requireNonNull(GlobalActionsDialogLite.this);
            int color2 = context.getResources().getColor(2131099876);
            Objects.requireNonNull(GlobalActionsDialogLite.this);
            int color3 = context.getResources().getColor(2131099875);
            TextView textView = (TextView) create.findViewById(16908299);
            textView.setTextColor(color);
            textView.setSelected(true);
            ImageView imageView = (ImageView) create.findViewById(16908294);
            imageView.getDrawable().setTint(color2);
            imageView.setBackgroundTintList(ColorStateList.valueOf(color3));
            create.setBackgroundTintList(ColorStateList.valueOf(color3));
            return create;
        }
    }

    /* loaded from: classes.dex */
    public class EmergencyAffordanceAction extends EmergencyAction {
        public EmergencyAffordanceAction() {
            super(17302208);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite.this.mEmergencyAffordanceManager.performEmergencyCall();
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class EmergencyDialerAction extends EmergencyAction {
        public EmergencyDialerAction() {
            super(2131231945);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite.this.mMetricsLogger.action(1569);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_EMERGENCY_DIALER_PRESS);
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            if (globalActionsDialogLite.mTelecomManager != null) {
                globalActionsDialogLite.mStatusBarOptional.ifPresent(ShellInitImpl$$ExternalSyntheticLambda6.INSTANCE$1);
                Intent createLaunchEmergencyDialerIntent = GlobalActionsDialogLite.this.mTelecomManager.createLaunchEmergencyDialerIntent(null);
                createLaunchEmergencyDialerIntent.addFlags(343932928);
                createLaunchEmergencyDialerIntent.putExtra("com.android.phone.EmergencyDialer.extra.ENTRY_TYPE", 2);
                GlobalActionsDialogLite.this.mContext.startActivityAsUser(createLaunchEmergencyDialerIntent, UserHandle.CURRENT);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class LockDownAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public LockDownAction() {
            super(17302492, 17040385);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite.this.mLockPatternUtils.requireStrongAuth(32, -1);
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_LOCKDOWN_PRESS);
            try {
                GlobalActionsDialogLite.this.mIWindowManager.lockNow((Bundle) null);
                GlobalActionsDialogLite.this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$LockDownAction$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        int[] enabledProfileIds;
                        GlobalActionsDialogLite.LockDownAction lockDownAction = GlobalActionsDialogLite.LockDownAction.this;
                        Objects.requireNonNull(lockDownAction);
                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                        Objects.requireNonNull(globalActionsDialogLite);
                        int i = globalActionsDialogLite.getCurrentUser().id;
                        for (int i2 : globalActionsDialogLite.mUserManager.getEnabledProfileIds(i)) {
                            if (i2 != i) {
                                globalActionsDialogLite.mTrustManager.setDeviceLockedForUser(i2, true);
                            }
                        }
                    }
                });
            } catch (RemoteException e) {
                Log.e("GlobalActionsDialogLite", "Error while trying to lock device.", e);
            }
        }
    }

    /* loaded from: classes.dex */
    public final class LogoutAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public LogoutAction() {
            super(17302539, 17040386);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            globalActionsDialogLite.mHandler.postDelayed(new StatusBar$$ExternalSyntheticLambda18(this, 3), globalActionsDialogLite.mDialogPressDelay);
        }
    }

    /* loaded from: classes.dex */
    public interface LongPressAction extends Action {
        boolean onLongPress();
    }

    /* loaded from: classes.dex */
    public class MyAdapter extends MultiListLayout.MultiListAdapter {
        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public final boolean areAllItemsEnabled() {
            return false;
        }

        public final int countItems(boolean z) {
            int i = 0;
            for (int i2 = 0; i2 < GlobalActionsDialogLite.this.mItems.size(); i2++) {
                GlobalActionsDialogLite.this.mItems.get(i2).shouldBeSeparated();
                if (!z) {
                    i++;
                }
            }
            return i;
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            return countItems(false) + countItems(true);
        }

        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public MyAdapter() {
        }

        @Override // android.widget.Adapter
        public final Action getItem(int i) {
            int i2 = 0;
            for (int i3 = 0; i3 < GlobalActionsDialogLite.this.mItems.size(); i3++) {
                Action action = GlobalActionsDialogLite.this.mItems.get(i3);
                if (GlobalActionsDialogLite.this.shouldShowAction(action)) {
                    if (i2 == i) {
                        return action;
                    }
                    i2++;
                }
            }
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("position ", i, " out of range of showable actions, filtered count=");
            m.append(getCount());
            m.append(", keyguardshowing=");
            m.append(GlobalActionsDialogLite.this.mKeyguardShowing);
            m.append(", provisioned=");
            m.append(GlobalActionsDialogLite.this.mDeviceProvisioned);
            throw new IllegalArgumentException(m.toString());
        }

        @Override // android.widget.Adapter
        public final View getView(final int i, View view, ViewGroup viewGroup) {
            Action item = getItem(i);
            Context context = GlobalActionsDialogLite.this.mContext;
            View create = item.create(context, view, viewGroup, LayoutInflater.from(context));
            create.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GlobalActionsDialogLite.MyAdapter myAdapter = GlobalActionsDialogLite.MyAdapter.this;
                    int i2 = i;
                    Objects.requireNonNull(myAdapter);
                    GlobalActionsDialogLite.Action item2 = GlobalActionsDialogLite.this.mAdapter.getItem(i2);
                    if (!(item2 instanceof GlobalActionsDialogLite.SilentModeTriStateAction)) {
                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                        if (globalActionsDialogLite.mDialog == null) {
                            Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                        } else if (!(item2 instanceof GlobalActionsDialogLite.PowerOptionsAction)) {
                            globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                            GlobalActionsDialogLite.this.mDialog.dismiss();
                        }
                        item2.onPress();
                    }
                }
            });
            if (item instanceof LongPressAction) {
                create.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view2) {
                        GlobalActionsDialogLite.MyAdapter myAdapter = GlobalActionsDialogLite.MyAdapter.this;
                        int i2 = i;
                        Objects.requireNonNull(myAdapter);
                        GlobalActionsDialogLite.Action item2 = GlobalActionsDialogLite.this.mAdapter.getItem(i2);
                        if (!(item2 instanceof GlobalActionsDialogLite.LongPressAction)) {
                            return false;
                        }
                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                        if (globalActionsDialogLite.mDialog != null) {
                            globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                            GlobalActionsDialogLite.this.mDialog.dismiss();
                        } else {
                            Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                        }
                        return ((GlobalActionsDialogLite.LongPressAction) item2).onLongPress();
                    }
                });
            }
            return create;
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public final boolean isEnabled(int i) {
            return getItem(i).isEnabled();
        }
    }

    /* loaded from: classes.dex */
    public class MyOverflowAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public MyOverflowAdapter() {
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            return GlobalActionsDialogLite.this.mOverflowItems.size();
        }

        @Override // android.widget.Adapter
        public final Object getItem(int i) {
            return GlobalActionsDialogLite.this.mOverflowItems.get(i);
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            Action action = GlobalActionsDialogLite.this.mOverflowItems.get(i);
            if (action == null) {
                GridLayoutManager$$ExternalSyntheticOutline1.m("No overflow action found at position: ", i, "GlobalActionsDialogLite");
                return null;
            }
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(2131624054, viewGroup, false);
            }
            TextView textView = (TextView) view;
            if (action.getMessageResId() != 0) {
                textView.setText(action.getMessageResId());
            } else {
                textView.setText(action.getMessage());
            }
            return textView;
        }
    }

    /* loaded from: classes.dex */
    public class MyPowerOptionsAdapter extends BaseAdapter {
        @Override // android.widget.Adapter
        public final long getItemId(int i) {
            return i;
        }

        public MyPowerOptionsAdapter() {
        }

        @Override // android.widget.Adapter
        public final int getCount() {
            return GlobalActionsDialogLite.this.mPowerItems.size();
        }

        @Override // android.widget.Adapter
        public final Object getItem(int i) {
            return GlobalActionsDialogLite.this.mPowerItems.get(i);
        }

        @Override // android.widget.Adapter
        public final View getView(final int i, View view, ViewGroup viewGroup) {
            Action action = GlobalActionsDialogLite.this.mPowerItems.get(i);
            if (action == null) {
                GridLayoutManager$$ExternalSyntheticOutline1.m("No power options action found at position: ", i, "GlobalActionsDialogLite");
                return null;
            }
            if (view == null) {
                view = LayoutInflater.from(GlobalActionsDialogLite.this.mContext).inflate(2131624124, viewGroup, false);
            }
            view.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    GlobalActionsDialogLite.MyPowerOptionsAdapter myPowerOptionsAdapter = GlobalActionsDialogLite.MyPowerOptionsAdapter.this;
                    int i2 = i;
                    Objects.requireNonNull(myPowerOptionsAdapter);
                    GlobalActionsDialogLite.Action action2 = GlobalActionsDialogLite.this.mPowerItems.get(i2);
                    if (!(action2 instanceof GlobalActionsDialogLite.SilentModeTriStateAction)) {
                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                        if (globalActionsDialogLite.mDialog != null) {
                            globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                            GlobalActionsDialogLite.this.mDialog.dismiss();
                        } else {
                            Log.w("GlobalActionsDialogLite", "Action clicked while mDialog is null.");
                        }
                        action2.onPress();
                    }
                }
            });
            if (action instanceof LongPressAction) {
                view.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite$MyPowerOptionsAdapter$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnLongClickListener
                    public final boolean onLongClick(View view2) {
                        GlobalActionsDialogLite.MyPowerOptionsAdapter myPowerOptionsAdapter = GlobalActionsDialogLite.MyPowerOptionsAdapter.this;
                        int i2 = i;
                        Objects.requireNonNull(myPowerOptionsAdapter);
                        GlobalActionsDialogLite.Action action2 = GlobalActionsDialogLite.this.mPowerItems.get(i2);
                        if (!(action2 instanceof GlobalActionsDialogLite.LongPressAction)) {
                            return false;
                        }
                        GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
                        if (globalActionsDialogLite.mDialog != null) {
                            globalActionsDialogLite.mDialogLaunchAnimator.disableAllCurrentDialogsExitAnimations();
                            GlobalActionsDialogLite.this.mDialog.dismiss();
                        } else {
                            Log.w("GlobalActionsDialogLite", "Action long-clicked while mDialog is null.");
                        }
                        return ((GlobalActionsDialogLite.LongPressAction) action2).onLongPress();
                    }
                });
            }
            ImageView imageView = (ImageView) view.findViewById(16908294);
            TextView textView = (TextView) view.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(action.getIcon(GlobalActionsDialogLite.this.mContext));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (action.getMessage() != null) {
                textView.setText(action.getMessage());
            } else {
                textView.setText(action.getMessageResId());
            }
            return view;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class PowerOptionsAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public PowerOptionsAction() {
            super(2131232263, 17040388);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            ActionsDialogLite actionsDialogLite = GlobalActionsDialogLite.this.mDialog;
            if (actionsDialogLite != null) {
                Objects.requireNonNull(actionsDialogLite);
                Context context = actionsDialogLite.mContext;
                MyPowerOptionsAdapter myPowerOptionsAdapter = actionsDialogLite.mPowerOptionsAdapter;
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(2131624123, (ViewGroup) null);
                for (int i = 0; i < myPowerOptionsAdapter.getCount(); i++) {
                    viewGroup.addView(myPowerOptionsAdapter.getView(i, null, viewGroup));
                }
                Resources resources = context.getResources();
                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(1);
                dialog.setContentView(viewGroup);
                Window window = dialog.getWindow();
                window.setType(2020);
                window.setTitle("");
                window.setBackgroundDrawable(resources.getDrawable(2131231666, context.getTheme()));
                window.addFlags(131072);
                actionsDialogLite.mPowerOptionsDialog = dialog;
                dialog.show();
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class RestartAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public RestartAction() {
            super(17302830, 17040389);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public final boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_LONG_PRESS);
            if (GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                return false;
            }
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_REBOOT_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(false);
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public class ScreenshotAction extends SinglePressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public ScreenshotAction() {
            super(17302832, 17040390);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite globalActionsDialogLite = GlobalActionsDialogLite.this;
            globalActionsDialogLite.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.ScreenshotAction.1
                @Override // java.lang.Runnable
                public final void run() {
                    GlobalActionsDialogLite globalActionsDialogLite2 = GlobalActionsDialogLite.this;
                    globalActionsDialogLite2.mScreenshotHelper.takeScreenshot(1, true, true, 0, globalActionsDialogLite2.mHandler, (Consumer) null);
                    GlobalActionsDialogLite.this.mMetricsLogger.action(1282);
                    GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SCREENSHOT_PRESS);
                }
            }, globalActionsDialogLite.mDialogPressDelay);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean shouldShow() {
            if (1 == GlobalActionsDialogLite.this.mContext.getResources().getInteger(17694878)) {
                return true;
            }
            return false;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class ShutDownAction extends SinglePressAction implements LongPressAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public ShutDownAction() {
            super(17301552, 17040387);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.LongPressAction
        public final boolean onLongPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_LONG_PRESS);
            if (GlobalActionsDialogLite.this.mUserManager.hasUserRestriction("no_safe_boot")) {
                return false;
            }
            GlobalActionsDialogLite.this.mWindowManagerFuncs.reboot(true);
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            GlobalActionsDialogLite.this.mUiEventLogger.log(GlobalActionsEvent.GA_SHUTDOWN_PRESS);
            GlobalActionsDialogLite.this.mWindowManagerFuncs.shutdown();
        }
    }

    /* loaded from: classes.dex */
    public class SilentModeToggleAction extends ToggleAction {
        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public SilentModeToggleAction() {
            super(17302323, 17302322, 17040393, 17040392);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.ToggleAction
        public final void onToggle(boolean z) {
            if (z) {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(0);
            } else {
                GlobalActionsDialogLite.this.mAudioManager.setRingerMode(2);
            }
        }
    }

    /* loaded from: classes.dex */
    public abstract class SinglePressAction implements Action {
        public final Drawable mIcon;
        public final int mIconResId;
        public final CharSequence mMessage;
        public final int mMessageResId;

        public SinglePressAction(int i, int i2) {
            this.mIconResId = i;
            this.mMessageResId = i2;
            this.mMessage = null;
            this.mIcon = null;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean isEnabled() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            Objects.requireNonNull(GlobalActionsDialogLite.this);
            View inflate = layoutInflater.inflate(2131624120, viewGroup, false);
            inflate.setId(View.generateViewId());
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            textView.setSelected(true);
            imageView.setImageDrawable(getIcon(context));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            CharSequence charSequence = this.mMessage;
            if (charSequence != null) {
                textView.setText(charSequence);
            } else {
                textView.setText(this.mMessageResId);
            }
            return inflate;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final Drawable getIcon(Context context) {
            Drawable drawable = this.mIcon;
            if (drawable != null) {
                return drawable;
            }
            return context.getDrawable(this.mIconResId);
        }

        public SinglePressAction(Drawable drawable, String str) {
            this.mIconResId = 17302708;
            this.mMessageResId = 0;
            this.mMessage = str;
            this.mIcon = drawable;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final CharSequence getMessage() {
            return this.mMessage;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final int getMessageResId() {
            return this.mMessageResId;
        }
    }

    /* loaded from: classes.dex */
    public abstract class ToggleAction implements Action {
        public int mDisabledIconResid;
        public int mDisabledStatusMessageResId;
        public int mEnabledIconResId;
        public int mEnabledStatusMessageResId;
        public ToggleState mState = ToggleState.Off;

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final CharSequence getMessage() {
            return null;
        }

        public abstract void onToggle(boolean z);

        public void changeStateFromPress(boolean z) {
            ToggleState toggleState;
            if (z) {
                toggleState = ToggleState.On;
            } else {
                toggleState = ToggleState.Off;
            }
            this.mState = toggleState;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final Drawable getIcon(Context context) {
            boolean z;
            int i;
            ToggleState toggleState = this.mState;
            if (toggleState == ToggleState.On || toggleState == ToggleState.TurningOn) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i = this.mEnabledIconResId;
            } else {
                i = this.mDisabledIconResid;
            }
            return context.getDrawable(i);
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final int getMessageResId() {
            boolean z;
            ToggleState toggleState = this.mState;
            if (toggleState == ToggleState.On || toggleState == ToggleState.TurningOn) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return this.mEnabledStatusMessageResId;
            }
            return this.mDisabledStatusMessageResId;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean isEnabled() {
            return !this.mState.inTransition();
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
            boolean z;
            if (this.mState.inTransition()) {
                Log.w("GlobalActionsDialogLite", "shouldn't be able to toggle when in transition");
                return;
            }
            if (this.mState != ToggleState.On) {
                z = true;
            } else {
                z = false;
            }
            onToggle(z);
            changeStateFromPress(z);
        }

        public ToggleAction(int i, int i2, int i3, int i4) {
            this.mEnabledIconResId = i;
            this.mDisabledIconResid = i2;
            this.mEnabledStatusMessageResId = i3;
            this.mDisabledStatusMessageResId = i4;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            int i;
            boolean z = false;
            View inflate = layoutInflater.inflate(2131624121, viewGroup, false);
            ViewGroup.LayoutParams layoutParams = inflate.getLayoutParams();
            layoutParams.width = -2;
            inflate.setLayoutParams(layoutParams);
            ImageView imageView = (ImageView) inflate.findViewById(16908294);
            TextView textView = (TextView) inflate.findViewById(16908299);
            boolean isEnabled = isEnabled();
            if (textView != null) {
                textView.setText(getMessageResId());
                textView.setEnabled(isEnabled);
                textView.setSelected(true);
            }
            if (imageView != null) {
                ToggleState toggleState = this.mState;
                if (toggleState == ToggleState.On || toggleState == ToggleState.TurningOn) {
                    z = true;
                }
                if (z) {
                    i = this.mEnabledIconResId;
                } else {
                    i = this.mDisabledIconResid;
                }
                imageView.setImageDrawable(context.getDrawable(i));
                imageView.setEnabled(isEnabled);
            }
            inflate.setEnabled(isEnabled);
            return inflate;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v1, types: [com.android.systemui.globalactions.GlobalActionsDialogLite$8] */
    /* JADX WARN: Type inference failed for: r7v6, types: [com.android.systemui.globalactions.GlobalActionsDialogLite$5] */
    /* JADX WARN: Type inference failed for: r9v1, types: [com.android.systemui.globalactions.GlobalActionsDialogLite$7, android.database.ContentObserver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public GlobalActionsDialogLite(android.content.Context r14, com.android.systemui.plugins.GlobalActions.GlobalActionsManager r15, android.media.AudioManager r16, android.service.dreams.IDreamManager r17, android.app.admin.DevicePolicyManager r18, com.android.internal.widget.LockPatternUtils r19, com.android.systemui.broadcast.BroadcastDispatcher r20, com.android.systemui.telephony.TelephonyListenerManager r21, com.android.systemui.util.settings.GlobalSettings r22, com.android.systemui.util.settings.SecureSettings r23, com.android.systemui.statusbar.VibratorHelper r24, android.content.res.Resources r25, com.android.systemui.statusbar.policy.ConfigurationController r26, com.android.systemui.statusbar.policy.KeyguardStateController r27, android.os.UserManager r28, android.app.trust.TrustManager r29, android.app.IActivityManager r30, android.telecom.TelecomManager r31, com.android.internal.logging.MetricsLogger r32, com.android.systemui.colorextraction.SysuiColorExtractor r33, com.android.internal.statusbar.IStatusBarService r34, com.android.systemui.statusbar.NotificationShadeWindowController r35, android.view.IWindowManager r36, java.util.concurrent.Executor r37, com.android.internal.logging.UiEventLogger r38, com.android.systemui.util.RingerModeTracker r39, com.android.systemui.model.SysUiState r40, android.os.Handler r41, android.content.pm.PackageManager r42, java.util.Optional<com.android.systemui.statusbar.phone.StatusBar> r43, com.android.keyguard.KeyguardUpdateMonitor r44, com.android.systemui.animation.DialogLaunchAnimator r45, com.android.systemui.statusbar.phone.SystemUIDialogManager r46) {
        /*
            Method dump skipped, instructions count: 316
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.globalactions.GlobalActionsDialogLite.<init>(android.content.Context, com.android.systemui.plugins.GlobalActions$GlobalActionsManager, android.media.AudioManager, android.service.dreams.IDreamManager, android.app.admin.DevicePolicyManager, com.android.internal.widget.LockPatternUtils, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.telephony.TelephonyListenerManager, com.android.systemui.util.settings.GlobalSettings, com.android.systemui.util.settings.SecureSettings, com.android.systemui.statusbar.VibratorHelper, android.content.res.Resources, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.statusbar.policy.KeyguardStateController, android.os.UserManager, android.app.trust.TrustManager, android.app.IActivityManager, android.telecom.TelecomManager, com.android.internal.logging.MetricsLogger, com.android.systemui.colorextraction.SysuiColorExtractor, com.android.internal.statusbar.IStatusBarService, com.android.systemui.statusbar.NotificationShadeWindowController, android.view.IWindowManager, java.util.concurrent.Executor, com.android.internal.logging.UiEventLogger, com.android.systemui.util.RingerModeTracker, com.android.systemui.model.SysUiState, android.os.Handler, android.content.pm.PackageManager, java.util.Optional, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.animation.DialogLaunchAnimator, com.android.systemui.statusbar.phone.SystemUIDialogManager):void");
    }

    @VisibleForTesting
    public void setZeroDialogPressDelayForTesting() {
        this.mDialogPressDelay = 0;
    }

    @VisibleForTesting
    public boolean shouldDisplayLockdown(UserInfo userInfo) {
        if (userInfo == null) {
            return false;
        }
        int i = userInfo.id;
        if (!this.mKeyguardStateController.isMethodSecure()) {
            return false;
        }
        int strongAuthForUser = this.mLockPatternUtils.getStrongAuthForUser(i);
        return strongAuthForUser == 0 || strongAuthForUser == 4;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum GlobalActionsEvent implements UiEventLogger.UiEventEnum {
        GA_POWER_MENU_OPEN(337),
        GA_POWER_MENU_CLOSE(471),
        GA_BUGREPORT_PRESS(344),
        GA_BUGREPORT_LONG_PRESS(345),
        GA_EMERGENCY_DIALER_PRESS(346),
        GA_SCREENSHOT_PRESS(347),
        /* JADX INFO: Fake field, exist only in values array */
        GA_SCREENSHOT_LONG_PRESS(348),
        GA_SHUTDOWN_PRESS(802),
        GA_SHUTDOWN_LONG_PRESS(803),
        GA_REBOOT_PRESS(349),
        GA_REBOOT_LONG_PRESS(804),
        GA_LOCKDOWN_PRESS(354),
        GA_OPEN_QS(805),
        /* JADX INFO: Fake field, exist only in values array */
        GA_CLOSE_LONG_PRESS_POWER(806),
        /* JADX INFO: Fake field, exist only in values array */
        GA_OPEN_LONG_PRESS_POWER(807),
        /* JADX INFO: Fake field, exist only in values array */
        GA_CLOSE_LONG_PRESS_POWER(808),
        GA_CLOSE_BACK(809),
        GA_CLOSE_TAP_OUTSIDE(810),
        /* JADX INFO: Fake field, exist only in values array */
        GA_CLOSE_POWER_VOLUP(811);
        
        private final int mId;

        GlobalActionsEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    /* loaded from: classes.dex */
    public static class SilentModeTriStateAction implements Action, View.OnClickListener {
        public static final int[] ITEM_IDS = {16909302, 16909303, 16909304};
        public final AudioManager mAudioManager;
        public final Handler mHandler;

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final Drawable getIcon(Context context) {
            return null;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final CharSequence getMessage() {
            return null;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final int getMessageResId() {
            return 0;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean isEnabled() {
            return true;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void onPress() {
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final boolean showBeforeProvisioning() {
            return false;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final void showDuringKeyguard() {
        }

        public SilentModeTriStateAction(AudioManager audioManager, AnonymousClass8 r2) {
            this.mAudioManager = audioManager;
            this.mHandler = r2;
        }

        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
        public final View create(Context context, View view, ViewGroup viewGroup, LayoutInflater layoutInflater) {
            boolean z;
            View inflate = layoutInflater.inflate(17367170, viewGroup, false);
            int ringerMode = this.mAudioManager.getRingerMode();
            for (int i = 0; i < 3; i++) {
                View findViewById = inflate.findViewById(ITEM_IDS[i]);
                if (ringerMode == i) {
                    z = true;
                } else {
                    z = false;
                }
                findViewById.setSelected(z);
                findViewById.setTag(Integer.valueOf(i));
                findViewById.setOnClickListener(this);
            }
            return inflate;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            if (view.getTag() instanceof Integer) {
                this.mAudioManager.setRingerMode(((Integer) view.getTag()).intValue());
                this.mHandler.sendEmptyMessageDelayed(0, 300L);
            }
        }
    }

    /* loaded from: classes.dex */
    public enum ToggleState {
        Off(false),
        TurningOn(true),
        TurningOff(true),
        On(false);
        
        private final boolean mInTransition;

        ToggleState(boolean z) {
            this.mInTransition = z;
        }

        public final boolean inTransition() {
            return this.mInTransition;
        }
    }

    @VisibleForTesting
    public void createActionItems() {
        String[] strArr;
        boolean z;
        boolean z2;
        String[] strArr2;
        boolean z3;
        Drawable drawable;
        String str;
        if (!this.mHasVibrator) {
            this.mSilentModeAction = new SilentModeToggleAction();
        } else {
            this.mSilentModeAction = new SilentModeTriStateAction(this.mAudioManager, this.mHandler);
        }
        this.mAirplaneModeOn = new AirplaneModeAction();
        onAirplaneModeChanged();
        this.mItems.clear();
        this.mOverflowItems.clear();
        this.mPowerItems.clear();
        String[] defaultActions = getDefaultActions();
        Action shutDownAction = new ShutDownAction();
        Action restartAction = new RestartAction();
        ArraySet arraySet = new ArraySet();
        ArrayList arrayList = new ArrayList();
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            addIfShouldShowAction(arrayList, new EmergencyAffordanceAction());
            arraySet.add("emergency");
        }
        boolean z4 = false;
        int i = 0;
        boolean z5 = false;
        UserInfo userInfo = null;
        while (i < defaultActions.length) {
            String str2 = defaultActions[i];
            if (arraySet.contains(str2)) {
                strArr = defaultActions;
            } else {
                if (GLOBAL_ACTION_KEY_POWER.equals(str2)) {
                    addIfShouldShowAction(arrayList, shutDownAction);
                } else if ("airplane".equals(str2)) {
                    addIfShouldShowAction(arrayList, this.mAirplaneModeOn);
                } else if ("bugreport".equals(str2)) {
                    if (!z5) {
                        userInfo = getCurrentUser();
                        z5 = true;
                    }
                    if (shouldDisplayBugReport(userInfo)) {
                        addIfShouldShowAction(arrayList, new BugReportAction());
                    }
                } else if (!"silent".equals(str2)) {
                    if (!"users".equals(str2)) {
                        strArr = defaultActions;
                        if ("settings".equals(str2)) {
                            addIfShouldShowAction(arrayList, new SinglePressAction() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.1
                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final boolean showBeforeProvisioning() {
                                    return true;
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void showDuringKeyguard() {
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void onPress() {
                                    Intent intent = new Intent("android.settings.SETTINGS");
                                    intent.addFlags(335544320);
                                    GlobalActionsDialogLite.this.mContext.startActivity(intent);
                                }
                            });
                        } else if ("lockdown".equals(str2)) {
                            if (!z5) {
                                userInfo = getCurrentUser();
                                z5 = true;
                            }
                            if (shouldDisplayLockdown(userInfo)) {
                                addIfShouldShowAction(arrayList, new LockDownAction());
                            }
                        } else if ("voiceassist".equals(str2)) {
                            addIfShouldShowAction(arrayList, new SinglePressAction() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.3
                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final boolean showBeforeProvisioning() {
                                    return true;
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void showDuringKeyguard() {
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void onPress() {
                                    Intent intent = new Intent("android.intent.action.VOICE_ASSIST");
                                    intent.addFlags(335544320);
                                    GlobalActionsDialogLite.this.mContext.startActivity(intent);
                                }
                            });
                        } else if ("assist".equals(str2)) {
                            addIfShouldShowAction(arrayList, new SinglePressAction() { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.2
                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final boolean showBeforeProvisioning() {
                                    return true;
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void showDuringKeyguard() {
                                }

                                @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                public final void onPress() {
                                    Intent intent = new Intent("android.intent.action.ASSIST");
                                    intent.addFlags(335544320);
                                    GlobalActionsDialogLite.this.mContext.startActivity(intent);
                                }
                            });
                        } else if ("restart".equals(str2)) {
                            addIfShouldShowAction(arrayList, restartAction);
                        } else if ("screenshot".equals(str2)) {
                            addIfShouldShowAction(arrayList, new ScreenshotAction());
                        } else if ("logout".equals(str2)) {
                            if (this.mDevicePolicyManager.isLogoutEnabled()) {
                                if (!z5) {
                                    userInfo = getCurrentUser();
                                    z5 = true;
                                }
                                if (userInfo != null) {
                                    if (!z5) {
                                        userInfo = getCurrentUser();
                                        z = true;
                                    } else {
                                        z = z5;
                                    }
                                    if (userInfo.id != 0) {
                                        addIfShouldShowAction(arrayList, new LogoutAction());
                                    }
                                    z5 = z;
                                }
                            }
                        } else if ("emergency".equals(str2)) {
                            addIfShouldShowAction(arrayList, new EmergencyDialerAction());
                        } else {
                            Log.e("GlobalActionsDialogLite", "Invalid global action key " + str2);
                        }
                    } else if (SystemProperties.getBoolean("fw.power_user_switcher", z4)) {
                        if (!z5) {
                            userInfo = getCurrentUser();
                            z5 = true;
                        }
                        if (this.mUserManager.isUserSwitcherEnabled()) {
                            for (final UserInfo userInfo2 : this.mUserManager.getUsers()) {
                                if (userInfo2.supportsSwitchToByUser()) {
                                    if (userInfo != null ? userInfo.id != userInfo2.id : userInfo2.id != 0) {
                                        z3 = false;
                                    } else {
                                        z3 = true;
                                    }
                                    String str3 = userInfo2.iconPath;
                                    if (str3 != null) {
                                        drawable = Drawable.createFromPath(str3);
                                    } else {
                                        drawable = null;
                                    }
                                    strArr2 = defaultActions;
                                    StringBuilder sb = new StringBuilder();
                                    z2 = z5;
                                    String str4 = userInfo2.name;
                                    if (str4 == null) {
                                        str4 = "Primary";
                                    }
                                    sb.append(str4);
                                    if (z3) {
                                        str = " ✔";
                                    } else {
                                        str = "";
                                    }
                                    sb.append(str);
                                    addIfShouldShowAction(arrayList, new SinglePressAction(drawable, sb.toString()) { // from class: com.android.systemui.globalactions.GlobalActionsDialogLite.4
                                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                        public final boolean showBeforeProvisioning() {
                                            return false;
                                        }

                                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                        public final void showDuringKeyguard() {
                                        }

                                        @Override // com.android.systemui.globalactions.GlobalActionsDialogLite.Action
                                        public final void onPress() {
                                            try {
                                                GlobalActionsDialogLite.this.mIActivityManager.switchUser(userInfo2.id);
                                            } catch (RemoteException e) {
                                                Log.e("GlobalActionsDialogLite", "Couldn't switch user " + e);
                                            }
                                        }
                                    });
                                } else {
                                    strArr2 = defaultActions;
                                    z2 = z5;
                                }
                                defaultActions = strArr2;
                                z5 = z2;
                            }
                        }
                        strArr = defaultActions;
                        z5 = z5;
                    }
                    arraySet.add(str2);
                } else if (this.mShowSilentToggle) {
                    addIfShouldShowAction(arrayList, this.mSilentModeAction);
                }
                strArr = defaultActions;
                arraySet.add(str2);
            }
            i++;
            defaultActions = strArr;
            z4 = false;
        }
        if (arrayList.contains(shutDownAction) && arrayList.contains(restartAction) && arrayList.size() > getMaxShownPowerItems()) {
            int min = Math.min(arrayList.indexOf(restartAction), arrayList.indexOf(shutDownAction));
            arrayList.remove(shutDownAction);
            arrayList.remove(restartAction);
            this.mPowerItems.add(shutDownAction);
            this.mPowerItems.add(restartAction);
            arrayList.add(min, new PowerOptionsAction());
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Action action = (Action) it.next();
            if (this.mItems.size() < getMaxShownPowerItems()) {
                this.mItems.add(action);
            } else {
                this.mOverflowItems.add(action);
            }
        }
    }

    @Override // com.android.systemui.plugins.GlobalActionsPanelPlugin.Callbacks
    public final void dismissGlobalActionsMenu() {
        removeMessages(0);
        sendEmptyMessage(0);
    }

    public final UserInfo getCurrentUser() {
        try {
            return this.mIActivityManager.getCurrentUser();
        } catch (RemoteException unused) {
            return null;
        }
    }

    @VisibleForTesting
    public String[] getDefaultActions() {
        return this.mResources.getStringArray(17236067);
    }

    @VisibleForTesting
    public int getMaxShownPowerItems() {
        return this.mResources.getInteger(2131493024) * this.mResources.getInteger(2131493023);
    }

    @VisibleForTesting
    public BugReportAction makeBugReportActionForTesting() {
        return new BugReportAction();
    }

    @VisibleForTesting
    public EmergencyDialerAction makeEmergencyDialerActionForTesting() {
        return new EmergencyDialerAction();
    }

    @VisibleForTesting
    public ScreenshotAction makeScreenshotActionForTesting() {
        return new ScreenshotAction();
    }

    public final void onAirplaneModeChanged() {
        ToggleState toggleState;
        if (!this.mHasTelephony && this.mAirplaneModeOn != null) {
            boolean z = false;
            if (this.mGlobalSettings.getInt("airplane_mode_on", 0) == 1) {
                z = true;
            }
            if (z) {
                toggleState = ToggleState.On;
            } else {
                toggleState = ToggleState.Off;
            }
            this.mAirplaneState = toggleState;
            AirplaneModeAction airplaneModeAction = this.mAirplaneModeOn;
            Objects.requireNonNull(airplaneModeAction);
            airplaneModeAction.mState = toggleState;
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        int i;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite != null && actionsDialogLite.isShowing() && (i = configuration.smallestScreenWidthDp) != this.mSmallestScreenWidthDp) {
            this.mSmallestScreenWidthDp = i;
            ActionsDialogLite actionsDialogLite2 = this.mDialog;
            Objects.requireNonNull(actionsDialogLite2);
            actionsDialogLite2.mOnRefreshCallback.run();
            GlobalActionsPopupMenu globalActionsPopupMenu = actionsDialogLite2.mOverflowPopup;
            if (globalActionsPopupMenu != null) {
                globalActionsPopupMenu.dismiss();
            }
            Dialog dialog = actionsDialogLite2.mPowerOptionsDialog;
            if (dialog != null) {
                dialog.dismiss();
            }
            MultiListLayout multiListLayout = actionsDialogLite2.mGlobalActionsLayout;
            Objects.requireNonNull(multiListLayout);
            if (multiListLayout.mAdapter != null) {
                multiListLayout.onUpdateList();
                return;
            }
            throw new IllegalStateException("mAdapter must be set before calling updateList");
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        if (this.mDialog == dialogInterface) {
            this.mDialog = null;
        }
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_CLOSE);
        this.mWindowManagerFuncs.onGlobalActionsHidden();
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    @Override // android.content.DialogInterface.OnShowListener
    public final void onShow(DialogInterface dialogInterface) {
        this.mMetricsLogger.visible(1568);
        this.mUiEventLogger.log(GlobalActionsEvent.GA_POWER_MENU_OPEN);
    }

    public final void refreshSilentMode() {
        boolean z;
        ToggleState toggleState;
        if (!this.mHasVibrator) {
            RingerModeLiveData ringerMode = this.mRingerModeTracker.getRingerMode();
            Objects.requireNonNull(ringerMode);
            Integer value = ringerMode.getValue();
            if (value == null || value.intValue() == 2) {
                z = false;
            } else {
                z = true;
            }
            ToggleAction toggleAction = (ToggleAction) this.mSilentModeAction;
            if (z) {
                toggleState = ToggleState.On;
            } else {
                toggleState = ToggleState.Off;
            }
            Objects.requireNonNull(toggleAction);
            toggleAction.mState = toggleState;
        }
    }

    @VisibleForTesting
    public boolean shouldDisplayBugReport(UserInfo userInfo) {
        if (this.mGlobalSettings.getInt("bugreport_in_power_menu", 0) == 0) {
            return false;
        }
        if (userInfo == null || userInfo.isPrimary()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean shouldShowAction(Action action) {
        if (this.mKeyguardShowing) {
            action.showDuringKeyguard();
        }
        if (this.mDeviceProvisioned || action.showBeforeProvisioning()) {
            return action.shouldShow();
        }
        return false;
    }

    public final void showOrHideDialog(boolean z, boolean z2, View view) {
        this.mKeyguardShowing = z;
        this.mDeviceProvisioned = z2;
        ActionsDialogLite actionsDialogLite = this.mDialog;
        if (actionsDialogLite == null || !actionsDialogLite.isShowing()) {
            IDreamManager iDreamManager = this.mDreamManager;
            if (iDreamManager != null) {
                try {
                    if (iDreamManager.isDreaming()) {
                        this.mDreamManager.awaken();
                    }
                } catch (RemoteException unused) {
                }
            }
            createActionItems();
            this.mAdapter = new MyAdapter();
            this.mOverflowAdapter = new MyOverflowAdapter();
            this.mPowerAdapter = new MyPowerOptionsAdapter();
            ActionsDialogLite actionsDialogLite2 = new ActionsDialogLite(this.mContext, this.mAdapter, this.mOverflowAdapter, this.mSysuiColorExtractor, this.mNotificationShadeWindowController, this.mSysUiState, new TaskView$$ExternalSyntheticLambda4(this, 6), this.mKeyguardShowing, this.mPowerAdapter, this.mUiEventLogger, this.mStatusBarOptional, this.mKeyguardUpdateMonitor, this.mLockPatternUtils, this.mDialogManager);
            actionsDialogLite2.setOnDismissListener(this);
            actionsDialogLite2.setOnShowListener(this);
            this.mDialog = actionsDialogLite2;
            refreshSilentMode();
            AirplaneModeAction airplaneModeAction = this.mAirplaneModeOn;
            ToggleState toggleState = this.mAirplaneState;
            Objects.requireNonNull(airplaneModeAction);
            airplaneModeAction.mState = toggleState;
            this.mAdapter.notifyDataSetChanged();
            this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
            WindowManager.LayoutParams attributes = this.mDialog.getWindow().getAttributes();
            attributes.setTitle("ActionsDialog");
            attributes.layoutInDisplayCutoutMode = 3;
            this.mDialog.getWindow().setAttributes(attributes);
            this.mDialog.getWindow().addFlags(131072);
            if (view != null) {
                DialogLaunchAnimator dialogLaunchAnimator = this.mDialogLaunchAnimator;
                ActionsDialogLite actionsDialogLite3 = this.mDialog;
                Objects.requireNonNull(dialogLaunchAnimator);
                dialogLaunchAnimator.showFromView(actionsDialogLite3, view, false);
            } else {
                this.mDialog.show();
            }
            this.mWindowManagerFuncs.onGlobalActionsShown();
            return;
        }
        this.mWindowManagerFuncs.onGlobalActionsShown();
        this.mDialog.dismiss();
        this.mDialog = null;
    }

    /* renamed from: -$$Nest$mchangeAirplaneModeSystemSetting  reason: not valid java name */
    public static void m59$$Nest$mchangeAirplaneModeSystemSetting(GlobalActionsDialogLite globalActionsDialogLite, boolean z) {
        ToggleState toggleState;
        Objects.requireNonNull(globalActionsDialogLite);
        globalActionsDialogLite.mGlobalSettings.putInt("airplane_mode_on", z ? 1 : 0);
        Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
        intent.addFlags(536870912);
        intent.putExtra("state", z);
        globalActionsDialogLite.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
        if (!globalActionsDialogLite.mHasTelephony) {
            if (z) {
                toggleState = ToggleState.On;
            } else {
                toggleState = ToggleState.Off;
            }
            globalActionsDialogLite.mAirplaneState = toggleState;
        }
    }

    public final void addIfShouldShowAction(ArrayList arrayList, Action action) {
        if (shouldShowAction(action)) {
            arrayList.add(action);
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
