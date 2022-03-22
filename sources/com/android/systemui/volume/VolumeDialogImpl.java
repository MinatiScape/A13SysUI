package com.android.systemui.volume;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RotateDrawable;
import android.media.AudioSystem;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.util.Slog;
import android.util.SparseBooleanArray;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.Utils;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.Prefs;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda2;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.plugins.VolumeDialogController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.policy.AccessibilityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import com.android.systemui.util.AlphaTintDrawableWrapper;
import com.android.systemui.util.RoundedCornerProgressDrawable;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class VolumeDialogImpl implements VolumeDialog, ConfigurationController.ConfigurationListener, ViewTreeObserver.OnComputeInternalInsetsListener {
    public static final String TAG = Util.logTag(VolumeDialogImpl.class);
    public final AccessibilityManagerWrapper mAccessibilityMgr;
    public int mActiveStream;
    public final ActivityManager mActivityManager;
    public final ActivityStarter mActivityStarter;
    public final boolean mChangeVolumeRowTintWhenInactive;
    public ConfigurableTexts mConfigurableTexts;
    public final ConfigurationController mConfigurationController;
    public final ContextThemeWrapper mContext;
    public final VolumeDialogController mController;
    public VolumeDialogImpl$$ExternalSyntheticLambda13 mCrossWindowBlurEnabledListener;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public CustomDialog mDialog;
    public int mDialogCornerRadius;
    public final int mDialogHideAnimationDurationMs;
    public ViewGroup mDialogRowsView;
    public BackgroundBlurDrawable mDialogRowsViewBackground;
    public ViewGroup mDialogRowsViewContainer;
    public final int mDialogShowAnimationDurationMs;
    public ViewGroup mDialogView;
    public int mDialogWidth;
    public boolean mHasSeenODICaptionsTooltip;
    public final KeyguardManager mKeyguard;
    public final MediaOutputDialogFactory mMediaOutputDialogFactory;
    public CaptionsToggleImageButton mODICaptionsIcon;
    public ViewStub mODICaptionsTooltipViewStub;
    public ViewGroup mODICaptionsView;
    public int mPrevActiveStream;
    public ViewGroup mRinger;
    public View mRingerAndDrawerContainer;
    public Drawable mRingerAndDrawerContainerBackground;
    public int mRingerCount;
    public ViewGroup mRingerDrawerContainer;
    public ImageView mRingerDrawerIconAnimatingDeselected;
    public ImageView mRingerDrawerIconAnimatingSelected;
    public int mRingerDrawerItemSize;
    public ViewGroup mRingerDrawerMute;
    public ImageView mRingerDrawerMuteIcon;
    public ViewGroup mRingerDrawerNewSelectionBg;
    public ViewGroup mRingerDrawerNormal;
    public ImageView mRingerDrawerNormalIcon;
    public ViewGroup mRingerDrawerVibrate;
    public ImageView mRingerDrawerVibrateIcon;
    public ImageButton mRingerIcon;
    public int mRingerRowsPadding;
    public SafetyWarningDialog mSafetyWarning;
    public ViewGroup mSelectedRingerContainer;
    public ImageView mSelectedRingerIcon;
    public ImageButton mSettingsIcon;
    public View mSettingsView;
    public boolean mShowA11yStream;
    public boolean mShowActiveStreamOnly;
    public final boolean mShowLowMediaVolumeIcon;
    public boolean mShowVibrate;
    public boolean mShowing;
    public VolumeDialogController.State mState;
    public View mTopContainer;
    public final boolean mUseBackgroundBlur;
    public Window mWindow;
    public FrameLayout mZenIcon;
    public final H mHandler = new H();
    public final Region mTouchableRegion = new Region();
    public final ValueAnimator mRingerDrawerIconColorAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    public final ValueAnimator mAnimateUpBackgroundToMatchDrawer = ValueAnimator.ofFloat(1.0f, 0.0f);
    public boolean mIsRingerDrawerOpen = false;
    public float mRingerDrawerClosedAmount = 1.0f;
    public final ArrayList mRows = new ArrayList();
    public final SparseBooleanArray mDynamic = new SparseBooleanArray();
    public final Object mSafetyWarningLock = new Object();
    public final Accessibility mAccessibility = new Accessibility();
    public boolean mAutomute = true;
    public boolean mSilentMode = true;
    public boolean mHovering = false;
    public boolean mConfigChanged = false;
    public boolean mIsAnimatingDismiss = false;
    public View mODICaptionsTooltipView = null;
    public final AnonymousClass6 mControllerCallbackH = new VolumeDialogController.Callbacks() { // from class: com.android.systemui.volume.VolumeDialogImpl.6
        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onAccessibilityModeChanged(Boolean bool) {
            boolean z;
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            if (bool == null) {
                z = false;
            } else {
                z = bool.booleanValue();
            }
            volumeDialogImpl.mShowA11yStream = z;
            VolumeRow activeRow = VolumeDialogImpl.this.getActiveRow();
            VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
            if (volumeDialogImpl2.mShowA11yStream || 10 != activeRow.stream) {
                volumeDialogImpl2.updateRowsH(activeRow);
            } else {
                volumeDialogImpl2.dismissH(7);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onCaptionComponentStateChanged(Boolean bool, Boolean bool2) {
            ViewStub viewStub;
            int i;
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            boolean booleanValue = bool.booleanValue();
            boolean booleanValue2 = bool2.booleanValue();
            Objects.requireNonNull(volumeDialogImpl);
            ViewGroup viewGroup = volumeDialogImpl.mODICaptionsView;
            if (viewGroup != null) {
                if (booleanValue) {
                    i = 0;
                } else {
                    i = 8;
                }
                viewGroup.setVisibility(i);
            }
            if (booleanValue) {
                volumeDialogImpl.updateCaptionsIcon();
                if (booleanValue2) {
                    if (!volumeDialogImpl.mHasSeenODICaptionsTooltip && (viewStub = volumeDialogImpl.mODICaptionsTooltipViewStub) != null) {
                        View inflate = viewStub.inflate();
                        volumeDialogImpl.mODICaptionsTooltipView = inflate;
                        inflate.findViewById(2131427850).setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda2(volumeDialogImpl, 2));
                        volumeDialogImpl.mODICaptionsTooltipViewStub = null;
                        volumeDialogImpl.rescheduleTimeoutH();
                    }
                    View view = volumeDialogImpl.mODICaptionsTooltipView;
                    if (view != null) {
                        view.setAlpha(0.0f);
                        volumeDialogImpl.mHandler.post(new VolumeDialogImpl$$ExternalSyntheticLambda10(volumeDialogImpl, 0));
                    }
                }
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onConfigurationChanged() {
            VolumeDialogImpl.this.mDialog.dismiss();
            VolumeDialogImpl.this.mConfigChanged = true;
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onDismissRequested(int i) {
            VolumeDialogImpl.this.dismissH(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onLayoutDirectionChanged(int i) {
            VolumeDialogImpl.this.mDialogView.setLayoutDirection(i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onScreenOff() {
            VolumeDialogImpl.this.dismissH(4);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowRequested(int i) {
            VolumeDialogImpl.m148$$Nest$mshowH(VolumeDialogImpl.this, i);
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowSafetyWarning(int i) {
            final VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            Objects.requireNonNull(volumeDialogImpl);
            if ((i & 1025) != 0 || volumeDialogImpl.mShowing) {
                synchronized (volumeDialogImpl.mSafetyWarningLock) {
                    if (volumeDialogImpl.mSafetyWarning == null) {
                        SafetyWarningDialog safetyWarningDialog = new SafetyWarningDialog(volumeDialogImpl.mContext, volumeDialogImpl.mController.getAudioManager()) { // from class: com.android.systemui.volume.VolumeDialogImpl.4
                            @Override // com.android.systemui.volume.SafetyWarningDialog
                            public final void cleanUp() {
                                VolumeDialogImpl volumeDialogImpl2;
                                synchronized (VolumeDialogImpl.this.mSafetyWarningLock) {
                                    volumeDialogImpl2 = VolumeDialogImpl.this;
                                    volumeDialogImpl2.mSafetyWarning = null;
                                }
                                volumeDialogImpl2.recheckH(null);
                            }
                        };
                        volumeDialogImpl.mSafetyWarning = safetyWarningDialog;
                        safetyWarningDialog.show();
                        volumeDialogImpl.recheckH(null);
                    } else {
                        return;
                    }
                }
            }
            volumeDialogImpl.rescheduleTimeoutH();
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowSilentHint() {
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            if (volumeDialogImpl.mSilentMode) {
                volumeDialogImpl.mController.setRingerMode(2, false);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onShowVibrateHint() {
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            if (volumeDialogImpl.mSilentMode) {
                volumeDialogImpl.mController.setRingerMode(0, false);
            }
        }

        @Override // com.android.systemui.plugins.VolumeDialogController.Callbacks
        public final void onStateChanged(VolumeDialogController.State state) {
            VolumeDialogImpl.this.onStateChangedH(state);
        }
    };

    /* loaded from: classes.dex */
    public final class Accessibility extends View.AccessibilityDelegate {
        public Accessibility() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public final boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }

        @Override // android.view.View.AccessibilityDelegate
        public final boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            List<CharSequence> text = accessibilityEvent.getText();
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            Objects.requireNonNull(volumeDialogImpl);
            text.add(volumeDialogImpl.mContext.getString(2131953502, volumeDialogImpl.getStreamLabelH(volumeDialogImpl.getActiveRow().ss)));
            return true;
        }
    }

    /* loaded from: classes.dex */
    public final class CustomDialog extends Dialog {
        @Override // android.app.Dialog
        public final void onStart() {
            setCanceledOnTouchOutside(true);
            super.onStart();
        }

        public CustomDialog(ContextThemeWrapper contextThemeWrapper) {
            super(contextThemeWrapper, 2132018748);
        }

        @Override // android.app.Dialog, android.view.Window.Callback
        public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
            VolumeDialogImpl.this.rescheduleTimeoutH();
            return super.dispatchTouchEvent(motionEvent);
        }

        @Override // android.app.Dialog
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            if (!VolumeDialogImpl.this.mShowing || motionEvent.getAction() != 4) {
                return false;
            }
            VolumeDialogImpl.this.dismissH(1);
            return true;
        }

        @Override // android.app.Dialog
        public final void onStop() {
            super.onStop();
            VolumeDialogImpl.this.mHandler.sendEmptyMessage(4);
        }
    }

    /* loaded from: classes.dex */
    public final class H extends Handler {
        public H() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            boolean z;
            switch (message.what) {
                case 1:
                    VolumeDialogImpl.m148$$Nest$mshowH(VolumeDialogImpl.this, message.arg1);
                    return;
                case 2:
                    VolumeDialogImpl.this.dismissH(message.arg1);
                    return;
                case 3:
                    VolumeDialogImpl.this.recheckH((VolumeRow) message.obj);
                    return;
                case 4:
                    VolumeDialogImpl.this.recheckH(null);
                    return;
                case 5:
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    int i = message.arg1;
                    if (message.arg2 != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    Objects.requireNonNull(volumeDialogImpl);
                    Iterator it = volumeDialogImpl.mRows.iterator();
                    while (it.hasNext()) {
                        VolumeRow volumeRow = (VolumeRow) it.next();
                        if (volumeRow.stream == i) {
                            volumeRow.important = z;
                            return;
                        }
                    }
                    return;
                case FalsingManager.VERSION /* 6 */:
                    VolumeDialogImpl.this.rescheduleTimeoutH();
                    return;
                case 7:
                    VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                    volumeDialogImpl2.onStateChangedH(volumeDialogImpl2.mState);
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public class RingerDrawerItemClickListener implements View.OnClickListener {
        public final int mClickedRingerMode;

        public RingerDrawerItemClickListener(int i) {
            this.mClickedRingerMode = i;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
            if (volumeDialogImpl.mIsRingerDrawerOpen) {
                volumeDialogImpl.setRingerMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                volumeDialogImpl2.mRingerDrawerIconAnimatingSelected = volumeDialogImpl2.getDrawerIconViewForMode(this.mClickedRingerMode);
                VolumeDialogImpl volumeDialogImpl3 = VolumeDialogImpl.this;
                volumeDialogImpl3.mRingerDrawerIconAnimatingDeselected = volumeDialogImpl3.getDrawerIconViewForMode(volumeDialogImpl3.mState.ringerModeInternal);
                VolumeDialogImpl.this.mRingerDrawerIconColorAnimator.start();
                VolumeDialogImpl.this.mSelectedRingerContainer.setVisibility(4);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.setAlpha(1.0f);
                VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().setInterpolator(Interpolators.ACCELERATE_DECELERATE).setDuration(175L).withEndAction(new StatusBar$$ExternalSyntheticLambda18(this, 12));
                if (!VolumeDialogImpl.this.isLandscape()) {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationY(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                } else {
                    VolumeDialogImpl.this.mRingerDrawerNewSelectionBg.animate().translationX(VolumeDialogImpl.this.getTranslationInDrawerForRingerMode(this.mClickedRingerMode)).start();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class VolumeRow {
        public ObjectAnimator anim;
        public int animTargetProgress;
        public boolean defaultStream;
        public FrameLayout dndIcon;
        public TextView header;
        public ImageButton icon;
        public int iconMuteRes;
        public int iconRes;
        public int iconState;
        public boolean important;
        public TextView number;
        public SeekBar slider;
        public AlphaTintDrawableWrapper sliderProgressIcon;
        public Drawable sliderProgressSolid;
        public VolumeDialogController.StreamState ss;
        public int stream;
        public boolean tracking;
        public long userAttempt;
        public View view;
        public int requestedLevel = -1;
        public int lastAudibleLevel = 1;
    }

    /* loaded from: classes.dex */
    public final class VolumeSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        public final VolumeRow mRow;

        public VolumeSeekBarChangeListener(VolumeRow volumeRow) {
            this.mRow = volumeRow;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            int i2;
            if (this.mRow.ss != null) {
                if (D.BUG) {
                    String str = VolumeDialogImpl.TAG;
                    Log.d(str, AudioSystem.streamToString(this.mRow.stream) + " onProgressChanged " + i + " fromUser=" + z);
                }
                if (z) {
                    int i3 = this.mRow.ss.levelMin;
                    if (i3 > 0 && i < (i2 = i3 * 100)) {
                        seekBar.setProgress(i2);
                        i = i2;
                    }
                    int impliedLevel = VolumeDialogImpl.getImpliedLevel(seekBar, i);
                    VolumeRow volumeRow = this.mRow;
                    VolumeDialogController.StreamState streamState = volumeRow.ss;
                    if (streamState.level != impliedLevel || (streamState.muted && impliedLevel > 0)) {
                        volumeRow.userAttempt = SystemClock.uptimeMillis();
                        VolumeRow volumeRow2 = this.mRow;
                        if (volumeRow2.requestedLevel != impliedLevel) {
                            VolumeDialogImpl.this.mController.setActiveStream(volumeRow2.stream);
                            VolumeDialogImpl.this.mController.setStreamVolume(this.mRow.stream, impliedLevel);
                            VolumeRow volumeRow3 = this.mRow;
                            volumeRow3.requestedLevel = impliedLevel;
                            Events.writeEvent(9, Integer.valueOf(volumeRow3.stream), Integer.valueOf(impliedLevel));
                        }
                    }
                }
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onStartTrackingTouch "), this.mRow.stream, VolumeDialogImpl.TAG);
            }
            VolumeDialogImpl.this.mController.setActiveStream(this.mRow.stream);
            this.mRow.tracking = true;
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            if (D.BUG) {
                KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onStopTrackingTouch "), this.mRow.stream, VolumeDialogImpl.TAG);
            }
            VolumeRow volumeRow = this.mRow;
            volumeRow.tracking = false;
            volumeRow.userAttempt = SystemClock.uptimeMillis();
            int impliedLevel = VolumeDialogImpl.getImpliedLevel(seekBar, seekBar.getProgress());
            Events.writeEvent(16, Integer.valueOf(this.mRow.stream), Integer.valueOf(impliedLevel));
            VolumeRow volumeRow2 = this.mRow;
            if (volumeRow2.ss.level != impliedLevel) {
                H h = VolumeDialogImpl.this.mHandler;
                h.sendMessageDelayed(h.obtainMessage(3, volumeRow2), 1000L);
            }
        }
    }

    public final ImageView getDrawerIconViewForMode(int i) {
        if (i == 1) {
            return this.mRingerDrawerVibrateIcon;
        }
        if (i == 0) {
            return this.mRingerDrawerMuteIcon;
        }
        return this.mRingerDrawerNormalIcon;
    }

    public final float getTranslationInDrawerForRingerMode(int i) {
        int i2;
        if (i == 1) {
            i2 = (-this.mRingerDrawerItemSize) * 2;
        } else if (i != 0) {
            return 0.0f;
        } else {
            i2 = -this.mRingerDrawerItemSize;
        }
        return i2;
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        this.mTouchableRegion.setEmpty();
        for (int i = 0; i < this.mDialogView.getChildCount(); i++) {
            unionViewBoundstoTouchableRegion(this.mDialogView.getChildAt(i));
        }
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            unionViewBoundstoTouchableRegion(this.mODICaptionsTooltipView);
        }
        internalInsetsInfo.touchableRegion.set(this.mTouchableRegion);
    }

    public final void setRingerMode(int i) {
        VibrationEffect vibrationEffect;
        VolumeDialogController.StreamState streamState;
        Events.writeEvent(18, Integer.valueOf(i));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Secure.putInt(contentResolver, "manual_ringer_toggle_count", Settings.Secure.getInt(contentResolver, "manual_ringer_toggle_count", 0) + 1);
        updateRingerH();
        String str = null;
        if (i == 0) {
            vibrationEffect = VibrationEffect.get(0);
        } else if (i != 2) {
            vibrationEffect = VibrationEffect.get(1);
        } else {
            this.mController.scheduleTouchFeedback();
            vibrationEffect = null;
        }
        if (vibrationEffect != null) {
            this.mController.vibrate(vibrationEffect);
        }
        this.mController.setRingerMode(i, false);
        ContextThemeWrapper contextThemeWrapper = this.mContext;
        int i2 = contextThemeWrapper.getSharedPreferences(contextThemeWrapper.getPackageName(), 0).getInt("RingerGuidanceCount", 0);
        if (i2 <= 12) {
            if (i == 0) {
                str = this.mContext.getString(17041672);
            } else if (i != 2) {
                str = this.mContext.getString(17041673);
            } else {
                if (this.mState.states.get(2) != null) {
                    str = this.mContext.getString(2131953501, NumberFormat.getPercentInstance().format(streamState.level / streamState.levelMax));
                }
            }
            Toast.makeText(this.mContext, str, 0).show();
            ContextThemeWrapper contextThemeWrapper2 = this.mContext;
            contextThemeWrapper2.getSharedPreferences(contextThemeWrapper2.getPackageName(), 0).edit().putInt("RingerGuidanceCount", i2 + 1).apply();
        }
    }

    public final void unionViewBoundstoTouchableRegion(View view) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        float f = iArr[0];
        float f2 = iArr[1];
        if (view == this.mTopContainer && !this.mIsRingerDrawerOpen) {
            if (!isLandscape()) {
                f2 += getRingerDrawerOpenExtraSize();
            } else {
                f += getRingerDrawerOpenExtraSize();
            }
        }
        this.mTouchableRegion.op((int) f, (int) f2, view.getWidth() + iArr[0], view.getHeight() + iArr[1], Region.Op.UNION);
    }

    public final void addAccessibilityDescription(ImageButton imageButton, int i, final String str) {
        int i2;
        ContextThemeWrapper contextThemeWrapper = this.mContext;
        if (i == 0) {
            i2 = 2131953513;
        } else if (i != 1) {
            i2 = 2131953512;
        } else {
            i2 = 2131953514;
        }
        imageButton.setContentDescription(contextThemeWrapper.getString(i2));
        imageButton.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.volume.VolumeDialogImpl.3
            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, str));
            }
        });
    }

    public final void addRow$1(int i, int i2, int i3, boolean z, boolean z2) {
        if (D.BUG) {
            String str = TAG;
            Slog.d(str, "Adding row for stream " + i);
        }
        VolumeRow volumeRow = new VolumeRow();
        initRow(volumeRow, i, i2, i3, z, z2);
        this.mDialogRowsView.addView(volumeRow.view);
        this.mRows.add(volumeRow);
    }

    public final void checkODICaptionsTooltip(boolean z) {
        boolean z2 = this.mHasSeenODICaptionsTooltip;
        if (!z2 && !z && this.mODICaptionsTooltipViewStub != null) {
            this.mController.getCaptionsComponentState(true);
        } else if (z2 && z && this.mODICaptionsTooltipView != null) {
            hideCaptionsTooltip();
        }
    }

    @Override // com.android.systemui.plugins.VolumeDialog
    public final void destroy() {
        this.mController.removeCallback(this.mControllerCallbackH);
        this.mHandler.removeCallbacksAndMessages(null);
        this.mConfigurationController.removeCallback(this);
    }

    public final void dismissH(int i) {
        boolean z;
        if (D.BUG) {
            String str = TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mDialog.dismiss() reason: ");
            m.append(Events.DISMISS_REASONS[i]);
            m.append(" from: ");
            m.append(Debug.getCaller());
            Log.d(str, m.toString());
        }
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(1);
        if (!this.mIsAnimatingDismiss) {
            this.mIsAnimatingDismiss = true;
            this.mDialogView.animate().cancel();
            if (this.mShowing) {
                this.mShowing = false;
                Events.writeEvent(1, Integer.valueOf(i));
            }
            this.mDialogView.setTranslationX(0.0f);
            this.mDialogView.setAlpha(1.0f);
            ViewPropertyAnimator withEndAction = this.mDialogView.animate().alpha(0.0f).setDuration(this.mDialogHideAnimationDurationMs).setInterpolator(new TimeInterpolator() { // from class: com.android.systemui.volume.SystemUIInterpolators$LogAccelerateInterpolator
                public final float mLogScale = 1.0f / ((0 * 1.0f) + (((float) (-Math.pow(100, -1.0f))) + 1.0f));

                @Override // android.animation.TimeInterpolator
                public final float getInterpolation(float f) {
                    float f2 = 1.0f - f;
                    return 1.0f - (((0 * f2) + (((float) (-Math.pow(100, -f2))) + 1.0f)) * this.mLogScale);
                }
            }).withEndAction(new TaskView$$ExternalSyntheticLambda3(this, 6));
            if (this.mContext.getDisplay().getRotation() != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                withEndAction.translationX(this.mDialogView.getWidth() / 2.0f);
            }
            withEndAction.start();
            checkODICaptionsTooltip(true);
            this.mController.notifyVisible(false);
            synchronized (this.mSafetyWarningLock) {
                if (this.mSafetyWarning != null) {
                    if (D.BUG) {
                        Log.d(TAG, "SafetyWarning dismissed");
                    }
                    this.mSafetyWarning.dismiss();
                }
            }
        }
    }

    public final VolumeRow getActiveRow() {
        Iterator it = this.mRows.iterator();
        while (it.hasNext()) {
            VolumeRow volumeRow = (VolumeRow) it.next();
            if (volumeRow.stream == this.mActiveStream) {
                return volumeRow;
            }
        }
        Iterator it2 = this.mRows.iterator();
        while (it2.hasNext()) {
            VolumeRow volumeRow2 = (VolumeRow) it2.next();
            if (volumeRow2.stream == 3) {
                return volumeRow2;
            }
        }
        return (VolumeRow) this.mRows.get(0);
    }

    public final int getRingerDrawerOpenExtraSize() {
        return (this.mRingerCount - 1) * this.mRingerDrawerItemSize;
    }

    public final String getStreamLabelH(VolumeDialogController.StreamState streamState) {
        if (streamState == null) {
            return "";
        }
        String str = streamState.remoteLabel;
        if (str != null) {
            return str;
        }
        try {
            return this.mContext.getResources().getString(streamState.name);
        } catch (Resources.NotFoundException unused) {
            String str2 = TAG;
            Slog.e(str2, "Can't find translation for stream " + streamState);
            return "";
        }
    }

    public final void hideCaptionsTooltip() {
        View view = this.mODICaptionsTooltipView;
        if (view != null && view.getVisibility() == 0) {
            this.mODICaptionsTooltipView.animate().cancel();
            this.mODICaptionsTooltipView.setAlpha(1.0f);
            this.mODICaptionsTooltipView.animate().alpha(0.0f).setStartDelay(0L).setDuration(this.mDialogHideAnimationDurationMs).withEndAction(new AccessPoint$$ExternalSyntheticLambda1(this, 9)).start();
        }
    }

    public final void hideRingerDrawer() {
        if (this.mRingerDrawerContainer != null && this.mIsRingerDrawerOpen) {
            getDrawerIconViewForMode(this.mState.ringerModeInternal).setVisibility(4);
            this.mRingerDrawerContainer.animate().alpha(0.0f).setDuration(250L).setStartDelay(0L).withEndAction(new VolumeDialogImpl$$ExternalSyntheticLambda11(this, 0));
            if (!isLandscape()) {
                this.mRingerDrawerContainer.animate().translationY(this.mRingerDrawerItemSize * 2).start();
            } else {
                this.mRingerDrawerContainer.animate().translationX(this.mRingerDrawerItemSize * 2).start();
            }
            this.mAnimateUpBackgroundToMatchDrawer.setDuration(250L);
            this.mAnimateUpBackgroundToMatchDrawer.setInterpolator(Interpolators.FAST_OUT_SLOW_IN_REVERSE);
            this.mAnimateUpBackgroundToMatchDrawer.reverse();
            this.mSelectedRingerContainer.animate().translationX(0.0f).translationY(0.0f).start();
            this.mSelectedRingerContainer.setContentDescription(this.mContext.getString(2131953508));
            this.mIsRingerDrawerOpen = false;
        }
    }

    public final void initDialog() {
        this.mDialog = new CustomDialog(this.mContext);
        initDimens();
        this.mConfigurableTexts = new ConfigurableTexts(this.mContext);
        this.mHovering = false;
        this.mShowing = false;
        Window window = this.mDialog.getWindow();
        this.mWindow = window;
        window.requestFeature(1);
        this.mWindow.setBackgroundDrawable(new ColorDrawable(0));
        this.mWindow.clearFlags(65538);
        this.mWindow.addFlags(17563688);
        this.mWindow.addPrivateFlags(536870912);
        this.mWindow.setType(2020);
        this.mWindow.setWindowAnimations(16973828);
        WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
        attributes.format = -3;
        attributes.setTitle("VolumeDialogImpl");
        attributes.windowAnimations = -1;
        attributes.gravity = this.mContext.getResources().getInteger(2131493045);
        this.mWindow.setAttributes(attributes);
        this.mWindow.setLayout(-2, -2);
        this.mDialog.setContentView(2131624649);
        ViewGroup viewGroup = (ViewGroup) this.mDialog.findViewById(2131429206);
        this.mDialogView = viewGroup;
        viewGroup.setAlpha(0.0f);
        this.mDialog.setCanceledOnTouchOutside(true);
        this.mDialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                boolean z;
                ViewTreeObserver.OnComputeInternalInsetsListener onComputeInternalInsetsListener = VolumeDialogImpl.this;
                Objects.requireNonNull(onComputeInternalInsetsListener);
                onComputeInternalInsetsListener.mDialogView.getViewTreeObserver().addOnComputeInternalInsetsListener(onComputeInternalInsetsListener);
                if (onComputeInternalInsetsListener.mContext.getDisplay().getRotation() != 0) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    ViewGroup viewGroup2 = onComputeInternalInsetsListener.mDialogView;
                    viewGroup2.setTranslationX(viewGroup2.getWidth() / 2.0f);
                }
                onComputeInternalInsetsListener.mDialogView.setAlpha(0.0f);
                onComputeInternalInsetsListener.mDialogView.animate().alpha(1.0f).translationX(0.0f).setDuration(onComputeInternalInsetsListener.mDialogShowAnimationDurationMs).setInterpolator(new TimeInterpolator() { // from class: com.android.systemui.volume.SystemUIInterpolators$LogDecelerateInterpolator
                    public final float mOutputScale = 1.0f / ((1.0f - ((float) Math.pow(400.0f, -0.71428573f))) + 0.0f);

                    @Override // android.animation.TimeInterpolator
                    public final float getInterpolation(float f) {
                        return ((f * 0.0f) + (1.0f - ((float) Math.pow(400.0f, (-f) * 0.71428573f)))) * this.mOutputScale;
                    }
                }).withEndAction(new CreateUserActivity$$ExternalSyntheticLambda2(onComputeInternalInsetsListener, 5)).start();
            }
        });
        this.mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                ViewTreeObserver.OnComputeInternalInsetsListener onComputeInternalInsetsListener = VolumeDialogImpl.this;
                Objects.requireNonNull(onComputeInternalInsetsListener);
                onComputeInternalInsetsListener.mDialogView.getViewTreeObserver().removeOnComputeInternalInsetsListener(onComputeInternalInsetsListener);
            }
        });
        this.mDialogView.setOnHoverListener(new View.OnHoverListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda8
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view, MotionEvent motionEvent) {
                boolean z;
                VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                Objects.requireNonNull(volumeDialogImpl);
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 9 || actionMasked == 7) {
                    z = true;
                } else {
                    z = false;
                }
                volumeDialogImpl.mHovering = z;
                volumeDialogImpl.rescheduleTimeoutH();
                return true;
            }
        });
        this.mDialogRowsView = (ViewGroup) this.mDialog.findViewById(2131429208);
        if (this.mUseBackgroundBlur) {
            this.mDialogView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.volume.VolumeDialogImpl.1
                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewAttachedToWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().addCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                    VolumeDialogImpl.this.mDialogRowsViewBackground = view.getViewRootImpl().createBackgroundBlurDrawable();
                    Resources resources = VolumeDialogImpl.this.mContext.getResources();
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    volumeDialogImpl.mDialogRowsViewBackground.setCornerRadius(volumeDialogImpl.mContext.getResources().getDimensionPixelSize(Utils.getThemeAttr(VolumeDialogImpl.this.mContext, 16844145)));
                    VolumeDialogImpl.this.mDialogRowsViewBackground.setBlurRadius(resources.getDimensionPixelSize(2131167286));
                    VolumeDialogImpl volumeDialogImpl2 = VolumeDialogImpl.this;
                    volumeDialogImpl2.mDialogRowsView.setBackground(volumeDialogImpl2.mDialogRowsViewBackground);
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewDetachedFromWindow(View view) {
                    VolumeDialogImpl.this.mWindow.getWindowManager().removeCrossWindowBlurEnabledListener(VolumeDialogImpl.this.mCrossWindowBlurEnabledListener);
                }
            });
        }
        this.mDialogRowsViewContainer = (ViewGroup) this.mDialogView.findViewById(2131429209);
        this.mTopContainer = this.mDialogView.findViewById(2131429210);
        View findViewById = this.mDialogView.findViewById(2131429225);
        this.mRingerAndDrawerContainer = findViewById;
        if (findViewById != null) {
            if (isLandscape()) {
                View view = this.mRingerAndDrawerContainer;
                view.setPadding(view.getPaddingLeft(), this.mRingerAndDrawerContainer.getPaddingTop(), this.mRingerAndDrawerContainer.getPaddingRight(), this.mRingerRowsPadding);
                this.mRingerAndDrawerContainer.setBackgroundDrawable(this.mContext.getDrawable(2131232757));
            }
            this.mRingerAndDrawerContainer.post(new CreateUserActivity$$ExternalSyntheticLambda1(this, 6));
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mDialog.findViewById(2131428711);
        this.mRinger = viewGroup2;
        if (viewGroup2 != null) {
            this.mRingerIcon = (ImageButton) viewGroup2.findViewById(2131428712);
            this.mZenIcon = (FrameLayout) this.mRinger.findViewById(2131427860);
        }
        this.mSelectedRingerIcon = (ImageView) this.mDialog.findViewById(2131429222);
        this.mSelectedRingerContainer = (ViewGroup) this.mDialog.findViewById(2131429223);
        this.mRingerDrawerMute = (ViewGroup) this.mDialog.findViewById(2131429212);
        this.mRingerDrawerNormal = (ViewGroup) this.mDialog.findViewById(2131429214);
        this.mRingerDrawerVibrate = (ViewGroup) this.mDialog.findViewById(2131429218);
        this.mRingerDrawerMuteIcon = (ImageView) this.mDialog.findViewById(2131429213);
        this.mRingerDrawerVibrateIcon = (ImageView) this.mDialog.findViewById(2131429219);
        this.mRingerDrawerNormalIcon = (ImageView) this.mDialog.findViewById(2131429215);
        this.mRingerDrawerNewSelectionBg = (ViewGroup) this.mDialog.findViewById(2131429217);
        ViewGroup viewGroup3 = (ViewGroup) this.mDialog.findViewById(2131429211);
        this.mRingerDrawerContainer = viewGroup3;
        if (viewGroup3 != null) {
            if (!this.mShowVibrate) {
                this.mRingerDrawerVibrate.setVisibility(8);
            }
            if (!isLandscape()) {
                ViewGroup viewGroup4 = this.mDialogView;
                viewGroup4.setPadding(viewGroup4.getPaddingLeft(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), getRingerDrawerOpenExtraSize() + this.mDialogView.getPaddingBottom());
            } else {
                ViewGroup viewGroup5 = this.mDialogView;
                viewGroup5.setPadding(getRingerDrawerOpenExtraSize() + viewGroup5.getPaddingLeft(), this.mDialogView.getPaddingTop(), this.mDialogView.getPaddingRight(), this.mDialogView.getPaddingBottom());
            }
            ((LinearLayout) this.mRingerDrawerContainer.findViewById(2131429216)).setOrientation(!isLandscape());
            this.mSelectedRingerContainer.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda6
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    int i;
                    int i2;
                    int i3;
                    long j;
                    int i4;
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    Objects.requireNonNull(volumeDialogImpl);
                    boolean z = volumeDialogImpl.mIsRingerDrawerOpen;
                    if (z) {
                        volumeDialogImpl.hideRingerDrawer();
                    } else if (!z) {
                        ImageView imageView = volumeDialogImpl.mRingerDrawerVibrateIcon;
                        int i5 = 4;
                        if (volumeDialogImpl.mState.ringerModeInternal == 1) {
                            i = 4;
                        } else {
                            i = 0;
                        }
                        imageView.setVisibility(i);
                        ImageView imageView2 = volumeDialogImpl.mRingerDrawerMuteIcon;
                        if (volumeDialogImpl.mState.ringerModeInternal == 0) {
                            i2 = 4;
                        } else {
                            i2 = 0;
                        }
                        imageView2.setVisibility(i2);
                        ImageView imageView3 = volumeDialogImpl.mRingerDrawerNormalIcon;
                        if (volumeDialogImpl.mState.ringerModeInternal != 2) {
                            i5 = 0;
                        }
                        imageView3.setVisibility(i5);
                        volumeDialogImpl.mRingerDrawerNewSelectionBg.setAlpha(0.0f);
                        if (!volumeDialogImpl.isLandscape()) {
                            volumeDialogImpl.mRingerDrawerNewSelectionBg.setTranslationY(volumeDialogImpl.getTranslationInDrawerForRingerMode(volumeDialogImpl.mState.ringerModeInternal));
                        } else {
                            volumeDialogImpl.mRingerDrawerNewSelectionBg.setTranslationX(volumeDialogImpl.getTranslationInDrawerForRingerMode(volumeDialogImpl.mState.ringerModeInternal));
                        }
                        if (!volumeDialogImpl.isLandscape()) {
                            volumeDialogImpl.mRingerDrawerContainer.setTranslationY((volumeDialogImpl.mRingerCount - 1) * volumeDialogImpl.mRingerDrawerItemSize);
                        } else {
                            volumeDialogImpl.mRingerDrawerContainer.setTranslationX((volumeDialogImpl.mRingerCount - 1) * volumeDialogImpl.mRingerDrawerItemSize);
                        }
                        volumeDialogImpl.mRingerDrawerContainer.setAlpha(0.0f);
                        volumeDialogImpl.mRingerDrawerContainer.setVisibility(0);
                        if (volumeDialogImpl.mState.ringerModeInternal == 1) {
                            i3 = 175;
                        } else {
                            i3 = 250;
                        }
                        ViewPropertyAnimator animate = volumeDialogImpl.mRingerDrawerContainer.animate();
                        PathInterpolator pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
                        long j2 = i3;
                        ViewPropertyAnimator duration = animate.setInterpolator(pathInterpolator).setDuration(j2);
                        if (volumeDialogImpl.mState.ringerModeInternal == 1) {
                            j = 75;
                        } else {
                            j = 0;
                        }
                        duration.setStartDelay(j).alpha(1.0f).translationX(0.0f).translationY(0.0f).start();
                        volumeDialogImpl.mSelectedRingerContainer.animate().setInterpolator(pathInterpolator).setDuration(250L).withEndAction(new LockIconViewController$$ExternalSyntheticLambda2(volumeDialogImpl, 6));
                        volumeDialogImpl.mAnimateUpBackgroundToMatchDrawer.setDuration(j2);
                        volumeDialogImpl.mAnimateUpBackgroundToMatchDrawer.setInterpolator(pathInterpolator);
                        volumeDialogImpl.mAnimateUpBackgroundToMatchDrawer.start();
                        if (!volumeDialogImpl.isLandscape()) {
                            volumeDialogImpl.mSelectedRingerContainer.animate().translationY(volumeDialogImpl.getTranslationInDrawerForRingerMode(volumeDialogImpl.mState.ringerModeInternal)).start();
                        } else {
                            volumeDialogImpl.mSelectedRingerContainer.animate().translationX(volumeDialogImpl.getTranslationInDrawerForRingerMode(volumeDialogImpl.mState.ringerModeInternal)).start();
                        }
                        ViewGroup viewGroup6 = volumeDialogImpl.mSelectedRingerContainer;
                        ContextThemeWrapper contextThemeWrapper = volumeDialogImpl.mContext;
                        int i6 = volumeDialogImpl.mState.ringerModeInternal;
                        if (i6 == 0) {
                            i4 = 2131953513;
                        } else if (i6 != 1) {
                            i4 = 2131953512;
                        } else {
                            i4 = 2131953514;
                        }
                        viewGroup6.setContentDescription(contextThemeWrapper.getString(i4));
                        volumeDialogImpl.mIsRingerDrawerOpen = true;
                    }
                }
            });
            this.mRingerDrawerVibrate.setOnClickListener(new RingerDrawerItemClickListener(1));
            this.mRingerDrawerMute.setOnClickListener(new RingerDrawerItemClickListener(0));
            this.mRingerDrawerNormal.setOnClickListener(new RingerDrawerItemClickListener(2));
            final int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.mContext, 16843829);
            final int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(this.mContext, 16844002);
            this.mRingerDrawerIconColorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    int i = colorAttrDefaultColor2;
                    int i2 = colorAttrDefaultColor;
                    Objects.requireNonNull(volumeDialogImpl);
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
                    int intValue2 = ((Integer) ArgbEvaluator.getInstance().evaluate(floatValue, Integer.valueOf(i2), Integer.valueOf(i))).intValue();
                    volumeDialogImpl.mRingerDrawerIconAnimatingDeselected.setColorFilter(intValue);
                    volumeDialogImpl.mRingerDrawerIconAnimatingSelected.setColorFilter(intValue2);
                }
            });
            this.mRingerDrawerIconColorAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.volume.VolumeDialogImpl.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingDeselected.clearColorFilter();
                    VolumeDialogImpl.this.mRingerDrawerIconAnimatingSelected.clearColorFilter();
                }
            });
            this.mRingerDrawerIconColorAnimator.setDuration(175L);
            this.mAnimateUpBackgroundToMatchDrawer.addUpdateListener(new VolumeDialogImpl$$ExternalSyntheticLambda0(this, 0));
        }
        ViewGroup viewGroup6 = (ViewGroup) this.mDialog.findViewById(2131428525);
        this.mODICaptionsView = viewGroup6;
        if (viewGroup6 != null) {
            this.mODICaptionsIcon = (CaptionsToggleImageButton) viewGroup6.findViewById(2131428526);
        }
        ViewStub viewStub = (ViewStub) this.mDialog.findViewById(2131428527);
        this.mODICaptionsTooltipViewStub = viewStub;
        if (this.mHasSeenODICaptionsTooltip && viewStub != null) {
            this.mDialogView.removeView(viewStub);
            this.mODICaptionsTooltipViewStub = null;
        }
        this.mSettingsView = this.mDialog.findViewById(2131428841);
        this.mSettingsIcon = (ImageButton) this.mDialog.findViewById(2131428837);
        if (this.mRows.isEmpty()) {
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                addRow$1(10, 2131232308, 2131232308, true, false);
            }
            addRow$1(3, 2131232314, 2131232318, true, true);
            if (!AudioSystem.isSingleVolume(this.mContext)) {
                addRow$1(2, 2131232324, 2131232325, true, false);
                addRow$1(4, 2131231749, 2131232310, true, false);
                addRow$1(0, 17302810, 17302810, false, false);
                addRow$1(6, 2131232311, 2131232311, false, false);
                addRow$1(1, 2131232327, 2131232328, false, false);
            }
        } else {
            int size = this.mRows.size();
            for (int i = 0; i < size; i++) {
                VolumeRow volumeRow = (VolumeRow) this.mRows.get(i);
                initRow(volumeRow, volumeRow.stream, volumeRow.iconRes, volumeRow.iconMuteRes, volumeRow.important, volumeRow.defaultStream);
                this.mDialogRowsView.addView(volumeRow.view);
                updateVolumeRowH(volumeRow);
            }
        }
        updateRowsH(getActiveRow());
        ImageButton imageButton = this.mRingerIcon;
        if (imageButton != null) {
            imageButton.setAccessibilityLiveRegion(1);
            this.mRingerIcon.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda5
                /* JADX WARN: Code restructure failed: missing block: B:6:0x0028, code lost:
                    if (r2 != false) goto L_0x0039;
                 */
                @Override // android.view.View.OnClickListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void onClick(android.view.View r6) {
                    /*
                        r5 = this;
                        com.android.systemui.volume.VolumeDialogImpl r5 = com.android.systemui.volume.VolumeDialogImpl.this
                        java.util.Objects.requireNonNull(r5)
                        android.view.ContextThemeWrapper r6 = r5.mContext
                        java.lang.String r0 = "TouchedRingerToggle"
                        r1 = 1
                        com.android.systemui.Prefs.putBoolean(r6, r0, r1)
                        com.android.systemui.plugins.VolumeDialogController$State r6 = r5.mState
                        android.util.SparseArray<com.android.systemui.plugins.VolumeDialogController$StreamState> r6 = r6.states
                        r0 = 2
                        java.lang.Object r6 = r6.get(r0)
                        com.android.systemui.plugins.VolumeDialogController$StreamState r6 = (com.android.systemui.plugins.VolumeDialogController.StreamState) r6
                        if (r6 != 0) goto L_0x001b
                        goto L_0x003c
                    L_0x001b:
                        com.android.systemui.plugins.VolumeDialogController r2 = r5.mController
                        boolean r2 = r2.hasVibrator()
                        com.android.systemui.plugins.VolumeDialogController$State r3 = r5.mState
                        int r3 = r3.ringerModeInternal
                        r4 = 0
                        if (r3 != r0) goto L_0x002b
                        if (r2 == 0) goto L_0x002d
                        goto L_0x0039
                    L_0x002b:
                        if (r3 != r1) goto L_0x002f
                    L_0x002d:
                        r1 = r4
                        goto L_0x0039
                    L_0x002f:
                        int r6 = r6.level
                        if (r6 != 0) goto L_0x0038
                        com.android.systemui.plugins.VolumeDialogController r6 = r5.mController
                        r6.setStreamVolume(r0, r1)
                    L_0x0038:
                        r1 = r0
                    L_0x0039:
                        r5.setRingerMode(r1)
                    L_0x003c:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda5.onClick(android.view.View):void");
                }
            });
        }
        updateRingerH();
        initSettingsH();
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        if (captionsToggleImageButton != null) {
            VolumeDialogImpl$$ExternalSyntheticLambda9 volumeDialogImpl$$ExternalSyntheticLambda9 = new VolumeDialogImpl$$ExternalSyntheticLambda9(this);
            H h = this.mHandler;
            captionsToggleImageButton.mConfirmedTapListener = volumeDialogImpl$$ExternalSyntheticLambda9;
            if (captionsToggleImageButton.mGestureDetector == null) {
                captionsToggleImageButton.mGestureDetector = new GestureDetector(captionsToggleImageButton.getContext(), captionsToggleImageButton.mGestureListener, h);
            }
        }
        this.mController.getCaptionsComponentState(false);
    }

    public final void initDimens() {
        int i;
        this.mDialogWidth = this.mContext.getResources().getDimensionPixelSize(2131167292);
        this.mDialogCornerRadius = this.mContext.getResources().getDimensionPixelSize(2131167293);
        this.mRingerDrawerItemSize = this.mContext.getResources().getDimensionPixelSize(2131167306);
        this.mRingerRowsPadding = this.mContext.getResources().getDimensionPixelSize(2131167295);
        boolean hasVibrator = this.mController.hasVibrator();
        this.mShowVibrate = hasVibrator;
        if (hasVibrator) {
            i = 3;
        } else {
            i = 2;
        }
        this.mRingerCount = i;
    }

    @SuppressLint({"InflateParams"})
    public final void initRow(final VolumeRow volumeRow, final int i, int i2, int i3, boolean z, boolean z2) {
        volumeRow.stream = i;
        volumeRow.iconRes = i2;
        volumeRow.iconMuteRes = i3;
        volumeRow.important = z;
        volumeRow.defaultStream = z2;
        AlphaTintDrawableWrapper alphaTintDrawableWrapper = null;
        View inflate = this.mDialog.getLayoutInflater().inflate(2131624650, (ViewGroup) null);
        volumeRow.view = inflate;
        inflate.setId(volumeRow.stream);
        volumeRow.view.setTag(volumeRow);
        TextView textView = (TextView) volumeRow.view.findViewById(2131429226);
        volumeRow.header = textView;
        textView.setId(volumeRow.stream * 20);
        if (i == 10) {
            volumeRow.header.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        }
        volumeRow.dndIcon = (FrameLayout) volumeRow.view.findViewById(2131427860);
        SeekBar seekBar = (SeekBar) volumeRow.view.findViewById(2131429228);
        volumeRow.slider = seekBar;
        seekBar.setOnSeekBarChangeListener(new VolumeSeekBarChangeListener(volumeRow));
        volumeRow.number = (TextView) volumeRow.view.findViewById(2131429224);
        volumeRow.anim = null;
        LayerDrawable layerDrawable = (LayerDrawable) this.mContext.getDrawable(2131232761);
        LayerDrawable layerDrawable2 = (LayerDrawable) ((RoundedCornerProgressDrawable) layerDrawable.findDrawableByLayerId(16908301)).getDrawable();
        volumeRow.sliderProgressSolid = layerDrawable2.findDrawableByLayerId(2131429233);
        Drawable findDrawableByLayerId = layerDrawable2.findDrawableByLayerId(2131429232);
        if (findDrawableByLayerId != null) {
            alphaTintDrawableWrapper = (AlphaTintDrawableWrapper) ((RotateDrawable) findDrawableByLayerId).getDrawable();
        }
        volumeRow.sliderProgressIcon = alphaTintDrawableWrapper;
        volumeRow.slider.setProgressDrawable(layerDrawable);
        volumeRow.icon = (ImageButton) volumeRow.view.findViewById(2131429227);
        Resources.Theme theme = this.mContext.getTheme();
        ImageButton imageButton = volumeRow.icon;
        if (imageButton != null) {
            imageButton.setImageResource(i2);
        }
        AlphaTintDrawableWrapper alphaTintDrawableWrapper2 = volumeRow.sliderProgressIcon;
        if (alphaTintDrawableWrapper2 != null) {
            alphaTintDrawableWrapper2.setDrawable(volumeRow.view.getResources().getDrawable(i2, theme));
        }
        ImageButton imageButton2 = volumeRow.icon;
        if (imageButton2 == null) {
            return;
        }
        if (volumeRow.stream != 10) {
            imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    VolumeDialogImpl.VolumeRow volumeRow2 = volumeRow;
                    int i4 = i;
                    Objects.requireNonNull(volumeDialogImpl);
                    int i5 = 0;
                    boolean z3 = true;
                    Events.writeEvent(7, Integer.valueOf(volumeRow2.stream), Integer.valueOf(volumeRow2.iconState));
                    volumeDialogImpl.mController.setActiveStream(volumeRow2.stream);
                    if (volumeRow2.stream == 2) {
                        boolean hasVibrator = volumeDialogImpl.mController.hasVibrator();
                        if (volumeDialogImpl.mState.ringerModeInternal != 2) {
                            volumeDialogImpl.mController.setRingerMode(2, false);
                            if (volumeRow2.ss.level == 0) {
                                volumeDialogImpl.mController.setStreamVolume(i4, 1);
                            }
                        } else if (hasVibrator) {
                            volumeDialogImpl.mController.setRingerMode(1, false);
                        } else {
                            if (volumeRow2.ss.level != 0) {
                                z3 = false;
                            }
                            VolumeDialogController volumeDialogController = volumeDialogImpl.mController;
                            if (z3) {
                                i5 = volumeRow2.lastAudibleLevel;
                            }
                            volumeDialogController.setStreamVolume(i4, i5);
                        }
                    } else {
                        VolumeDialogController.StreamState streamState = volumeRow2.ss;
                        int i6 = streamState.level;
                        int i7 = streamState.levelMin;
                        if (i6 == i7) {
                            i5 = 1;
                        }
                        VolumeDialogController volumeDialogController2 = volumeDialogImpl.mController;
                        if (i5 != 0) {
                            i7 = volumeRow2.lastAudibleLevel;
                        }
                        volumeDialogController2.setStreamVolume(i4, i7);
                    }
                    volumeRow2.userAttempt = 0L;
                }
            });
        } else {
            imageButton2.setImportantForAccessibility(2);
        }
    }

    public final void initSettingsH() {
        int i;
        View view = this.mSettingsView;
        if (view != null) {
            if (!this.mDeviceProvisionedController.isCurrentUserSetup() || this.mActivityManager.getLockTaskModeState() != 0) {
                i = 8;
            } else {
                i = 0;
            }
            view.setVisibility(i);
        }
        ImageButton imageButton = this.mSettingsIcon;
        if (imageButton != null) {
            imageButton.setOnClickListener(new VolumeDialogImpl$$ExternalSyntheticLambda4(this, 0));
        }
    }

    public final boolean isLandscape() {
        if (this.mContext.getResources().getConfiguration().orientation == 2) {
            return true;
        }
        return false;
    }

    public final boolean isRtl() {
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }

    public final void onStateChangedH(VolumeDialogController.State state) {
        VolumeRow volumeRow;
        int i;
        int i2;
        if (D.BUG) {
            String str = TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onStateChangedH() state: ");
            m.append(state.toString());
            Log.d(str, m.toString());
        }
        VolumeDialogController.State state2 = this.mState;
        if (!(state2 == null || state == null || (i = state2.ringerModeInternal) == -1 || i == (i2 = state.ringerModeInternal) || i2 != 1)) {
            this.mController.vibrate(VibrationEffect.get(5));
        }
        this.mState = state;
        this.mDynamic.clear();
        for (int i3 = 0; i3 < state.states.size(); i3++) {
            int keyAt = state.states.keyAt(i3);
            if (state.states.valueAt(i3).dynamic) {
                this.mDynamic.put(keyAt, true);
                Iterator it = this.mRows.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        volumeRow = null;
                        break;
                    }
                    volumeRow = (VolumeRow) it.next();
                    if (volumeRow.stream == keyAt) {
                        break;
                    }
                }
                if (volumeRow == null) {
                    addRow$1(keyAt, 2131232322, 2131232323, true, false);
                }
            }
        }
        int i4 = this.mActiveStream;
        int i5 = state.activeStream;
        if (i4 != i5) {
            this.mPrevActiveStream = i4;
            this.mActiveStream = i5;
            updateRowsH(getActiveRow());
            if (this.mShowing) {
                rescheduleTimeoutH();
            }
        }
        Iterator it2 = this.mRows.iterator();
        while (it2.hasNext()) {
            updateVolumeRowH((VolumeRow) it2.next());
        }
        updateRingerH();
        this.mWindow.setTitle(this.mContext.getString(2131953502, getStreamLabelH(getActiveRow().ss)));
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onUiModeChanged() {
        this.mContext.getTheme().applyStyle(this.mContext.getThemeResId(), true);
    }

    public final void recheckH(VolumeRow volumeRow) {
        if (volumeRow == null) {
            if (D.BUG) {
                Log.d(TAG, "recheckH ALL");
            }
            trimObsoleteH();
            Iterator it = this.mRows.iterator();
            while (it.hasNext()) {
                updateVolumeRowH((VolumeRow) it.next());
            }
            return;
        }
        if (D.BUG) {
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(VendorAtomValue$$ExternalSyntheticOutline1.m("recheckH "), volumeRow.stream, TAG);
        }
        updateVolumeRowH(volumeRow);
    }

    public final void rescheduleTimeoutH() {
        int i;
        this.mHandler.removeMessages(2);
        if (this.mHovering) {
            AccessibilityManagerWrapper accessibilityManagerWrapper = this.mAccessibilityMgr;
            Objects.requireNonNull(accessibilityManagerWrapper);
            i = accessibilityManagerWrapper.mAccessibilityManager.getRecommendedTimeoutMillis(16000, 4);
        } else if (this.mSafetyWarning != null) {
            AccessibilityManagerWrapper accessibilityManagerWrapper2 = this.mAccessibilityMgr;
            Objects.requireNonNull(accessibilityManagerWrapper2);
            i = accessibilityManagerWrapper2.mAccessibilityManager.getRecommendedTimeoutMillis(5000, 6);
        } else if (this.mHasSeenODICaptionsTooltip || this.mODICaptionsTooltipView == null) {
            AccessibilityManagerWrapper accessibilityManagerWrapper3 = this.mAccessibilityMgr;
            Objects.requireNonNull(accessibilityManagerWrapper3);
            i = accessibilityManagerWrapper3.mAccessibilityManager.getRecommendedTimeoutMillis(3000, 4);
        } else {
            AccessibilityManagerWrapper accessibilityManagerWrapper4 = this.mAccessibilityMgr;
            Objects.requireNonNull(accessibilityManagerWrapper4);
            i = accessibilityManagerWrapper4.mAccessibilityManager.getRecommendedTimeoutMillis(5000, 6);
        }
        H h = this.mHandler;
        h.sendMessageDelayed(h.obtainMessage(2, 3, 0), i);
        if (D.BUG) {
            String str = TAG;
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("rescheduleTimeout ", i, " ");
            m.append(Debug.getCaller());
            Log.d(str, m.toString());
        }
        this.mController.userActivity();
    }

    public final void trimObsoleteH() {
        if (D.BUG) {
            Log.d(TAG, "trimObsoleteH");
        }
        int size = this.mRows.size();
        while (true) {
            size--;
            if (size >= 0) {
                VolumeRow volumeRow = (VolumeRow) this.mRows.get(size);
                VolumeDialogController.StreamState streamState = volumeRow.ss;
                if (streamState != null && streamState.dynamic && !this.mDynamic.get(volumeRow.stream)) {
                    this.mRows.remove(size);
                    this.mDialogRowsView.removeView(volumeRow.view);
                    ConfigurableTexts configurableTexts = this.mConfigurableTexts;
                    TextView textView = volumeRow.header;
                    Objects.requireNonNull(configurableTexts);
                    configurableTexts.mTexts.remove(textView);
                    configurableTexts.mTextLabels.remove(textView);
                }
            } else {
                return;
            }
        }
    }

    public final void updateBackgroundForDrawerClosedAmount() {
        Drawable drawable = this.mRingerAndDrawerContainerBackground;
        if (drawable != null) {
            Rect copyBounds = drawable.copyBounds();
            if (!isLandscape()) {
                copyBounds.top = (int) (this.mRingerDrawerClosedAmount * getRingerDrawerOpenExtraSize());
            } else {
                copyBounds.left = (int) (this.mRingerDrawerClosedAmount * getRingerDrawerOpenExtraSize());
            }
            this.mRingerAndDrawerContainerBackground.setBounds(copyBounds);
        }
    }

    public final void updateCaptionsIcon() {
        String str;
        int i;
        boolean areCaptionsEnabled = this.mController.areCaptionsEnabled();
        CaptionsToggleImageButton captionsToggleImageButton = this.mODICaptionsIcon;
        Objects.requireNonNull(captionsToggleImageButton);
        if (captionsToggleImageButton.mCaptionsEnabled != areCaptionsEnabled) {
            H h = this.mHandler;
            CaptionsToggleImageButton captionsToggleImageButton2 = this.mODICaptionsIcon;
            Objects.requireNonNull(captionsToggleImageButton2);
            captionsToggleImageButton2.mCaptionsEnabled = areCaptionsEnabled;
            AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK;
            if (areCaptionsEnabled) {
                str = captionsToggleImageButton2.getContext().getString(2131953505);
            } else {
                str = captionsToggleImageButton2.getContext().getString(2131953506);
            }
            ViewCompat.replaceAccessibilityAction(captionsToggleImageButton2, accessibilityActionCompat, str, new CaptionsToggleImageButton$$ExternalSyntheticLambda0(captionsToggleImageButton2));
            if (captionsToggleImageButton2.mCaptionsEnabled) {
                i = 2131232320;
            } else {
                i = 2131232321;
            }
            h.post(captionsToggleImageButton2.setImageResourceAsync(i));
        }
        final boolean isCaptionStreamOptedOut = this.mController.isCaptionStreamOptedOut();
        CaptionsToggleImageButton captionsToggleImageButton3 = this.mODICaptionsIcon;
        Objects.requireNonNull(captionsToggleImageButton3);
        if (captionsToggleImageButton3.mOptedOut != isCaptionStreamOptedOut) {
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda12
                @Override // java.lang.Runnable
                public final void run() {
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    boolean z = isCaptionStreamOptedOut;
                    Objects.requireNonNull(volumeDialogImpl);
                    CaptionsToggleImageButton captionsToggleImageButton4 = volumeDialogImpl.mODICaptionsIcon;
                    Objects.requireNonNull(captionsToggleImageButton4);
                    captionsToggleImageButton4.mOptedOut = z;
                    captionsToggleImageButton4.refreshDrawableState();
                }
            });
        }
    }

    public final void updateRingerH() {
        VolumeDialogController.State state;
        VolumeDialogController.StreamState streamState;
        boolean z;
        int i;
        if (this.mRinger != null && (state = this.mState) != null && (streamState = state.states.get(2)) != null) {
            VolumeDialogController.State state2 = this.mState;
            int i2 = state2.zenMode;
            boolean z2 = false;
            if (i2 == 3 || i2 == 2 || (i2 == 1 && state2.disallowRinger)) {
                z = true;
            } else {
                z = false;
            }
            boolean z3 = !z;
            ImageButton imageButton = this.mRingerIcon;
            if (imageButton != null) {
                imageButton.setEnabled(z3);
            }
            FrameLayout frameLayout = this.mZenIcon;
            if (frameLayout != null) {
                if (z3) {
                    i = 8;
                } else {
                    i = 0;
                }
                frameLayout.setVisibility(i);
            }
            int i3 = this.mState.ringerModeInternal;
            if (i3 == 0) {
                this.mRingerIcon.setImageResource(2131232325);
                this.mSelectedRingerIcon.setImageResource(2131232325);
                this.mRingerIcon.setTag(2);
                addAccessibilityDescription(this.mRingerIcon, 0, this.mContext.getString(2131953510));
            } else if (i3 != 1) {
                if ((this.mAutomute && streamState.level == 0) || streamState.muted) {
                    z2 = true;
                }
                if (z || !z2) {
                    this.mRingerIcon.setImageResource(2131232324);
                    this.mSelectedRingerIcon.setImageResource(2131232324);
                    if (this.mController.hasVibrator()) {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(2131953511));
                    } else {
                        addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(2131953509));
                    }
                    this.mRingerIcon.setTag(1);
                    return;
                }
                this.mRingerIcon.setImageResource(2131232325);
                this.mSelectedRingerIcon.setImageResource(2131232325);
                addAccessibilityDescription(this.mRingerIcon, 2, this.mContext.getString(2131953510));
                this.mRingerIcon.setTag(2);
            } else {
                this.mRingerIcon.setImageResource(2131232326);
                this.mSelectedRingerIcon.setImageResource(2131232326);
                addAccessibilityDescription(this.mRingerIcon, 1, this.mContext.getString(2131953509));
                this.mRingerIcon.setTag(3);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0053, code lost:
        if (r8 == r12.mPrevActiveStream) goto L_0x006c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x006a, code lost:
        if (r12.mDynamic.get(r9) != false) goto L_0x006c;
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x009f  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x00de A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0025 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateRowsH(com.android.systemui.volume.VolumeDialogImpl.VolumeRow r13) {
        /*
            Method dump skipped, instructions count: 260
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.updateRowsH(com.android.systemui.volume.VolumeDialogImpl$VolumeRow):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:197:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:200:0x02c4  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:210:0x02e4  */
    /* JADX WARN: Removed duplicated region for block: B:211:0x02e6  */
    /* JADX WARN: Removed duplicated region for block: B:214:0x02f0  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x03ae  */
    /* JADX WARN: Removed duplicated region for block: B:256:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateVolumeRowH(com.android.systemui.volume.VolumeDialogImpl.VolumeRow r15) {
        /*
            Method dump skipped, instructions count: 950
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.volume.VolumeDialogImpl.updateVolumeRowH(com.android.systemui.volume.VolumeDialogImpl$VolumeRow):void");
    }

    public final void updateVolumeRowTintH(VolumeRow volumeRow, boolean z) {
        boolean z2;
        ColorStateList colorStateList;
        int i;
        if (z) {
            volumeRow.slider.requestFocus();
        }
        if (!z || !volumeRow.slider.isEnabled()) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2 || this.mChangeVolumeRowTintWhenInactive) {
            if (z2) {
                colorStateList = Utils.getColorAttr(this.mContext, 16843829);
            } else {
                colorStateList = Utils.getColorAttr(this.mContext, 17956902);
            }
            if (z2) {
                i = Color.alpha(colorStateList.getDefaultColor());
            } else {
                TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{16844115});
                float f = obtainStyledAttributes.getFloat(0, 0.0f);
                obtainStyledAttributes.recycle();
                i = (int) (f * 255.0f);
            }
            ColorStateList colorAttr = Utils.getColorAttr(this.mContext, 16844002);
            ColorStateList colorAttr2 = Utils.getColorAttr(this.mContext, 17957103);
            volumeRow.sliderProgressSolid.setTintList(colorStateList);
            AlphaTintDrawableWrapper alphaTintDrawableWrapper = volumeRow.sliderProgressIcon;
            if (alphaTintDrawableWrapper != null) {
                alphaTintDrawableWrapper.setTintList(colorAttr);
            }
            ImageButton imageButton = volumeRow.icon;
            if (imageButton != null) {
                imageButton.setImageTintList(colorAttr2);
                volumeRow.icon.setImageAlpha(i);
            }
            TextView textView = volumeRow.number;
            if (textView != null) {
                textView.setTextColor(colorStateList);
                volumeRow.number.setAlpha(i);
            }
        }
    }

    /* renamed from: -$$Nest$mshowH  reason: not valid java name */
    public static void m148$$Nest$mshowH(VolumeDialogImpl volumeDialogImpl, int i) {
        Objects.requireNonNull(volumeDialogImpl);
        if (D.BUG) {
            ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("showH r="), Events.SHOW_REASONS[i], TAG);
        }
        volumeDialogImpl.mHandler.removeMessages(1);
        volumeDialogImpl.mHandler.removeMessages(2);
        volumeDialogImpl.rescheduleTimeoutH();
        if (volumeDialogImpl.mConfigChanged) {
            volumeDialogImpl.initDialog();
            ConfigurableTexts configurableTexts = volumeDialogImpl.mConfigurableTexts;
            Objects.requireNonNull(configurableTexts);
            if (!configurableTexts.mTexts.isEmpty()) {
                configurableTexts.mTexts.keyAt(0).post(configurableTexts.mUpdateAll);
            }
            volumeDialogImpl.mConfigChanged = false;
        }
        volumeDialogImpl.initSettingsH();
        volumeDialogImpl.mShowing = true;
        volumeDialogImpl.mIsAnimatingDismiss = false;
        volumeDialogImpl.mDialog.show();
        Events.writeEvent(0, Integer.valueOf(i), Boolean.valueOf(volumeDialogImpl.mKeyguard.isKeyguardLocked()));
        volumeDialogImpl.mController.notifyVisible(true);
        volumeDialogImpl.mController.getCaptionsComponentState(false);
        volumeDialogImpl.checkODICaptionsTooltip(false);
        volumeDialogImpl.updateBackgroundForDrawerClosedAmount();
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.volume.VolumeDialogImpl$6] */
    /* JADX WARN: Type inference failed for: r7v2, types: [com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda13] */
    public VolumeDialogImpl(Context context, VolumeDialogController volumeDialogController, AccessibilityManagerWrapper accessibilityManagerWrapper, DeviceProvisionedController deviceProvisionedController, ConfigurationController configurationController, MediaOutputDialogFactory mediaOutputDialogFactory, ActivityStarter activityStarter) {
        boolean z = false;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, 2132018748);
        this.mContext = contextThemeWrapper;
        this.mController = volumeDialogController;
        this.mKeyguard = (KeyguardManager) contextThemeWrapper.getSystemService("keyguard");
        this.mActivityManager = (ActivityManager) contextThemeWrapper.getSystemService("activity");
        this.mAccessibilityMgr = accessibilityManagerWrapper;
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mConfigurationController = configurationController;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mActivityStarter = activityStarter;
        this.mShowActiveStreamOnly = (contextThemeWrapper.getPackageManager().hasSystemFeature("android.software.leanback") || contextThemeWrapper.getPackageManager().hasSystemFeature("android.hardware.type.television")) ? true : z;
        this.mHasSeenODICaptionsTooltip = Prefs.getBoolean(context, "HasSeenODICaptionsTooltip");
        this.mShowLowMediaVolumeIcon = contextThemeWrapper.getResources().getBoolean(2131034163);
        this.mChangeVolumeRowTintWhenInactive = contextThemeWrapper.getResources().getBoolean(2131034118);
        this.mDialogShowAnimationDurationMs = contextThemeWrapper.getResources().getInteger(2131492891);
        this.mDialogHideAnimationDurationMs = contextThemeWrapper.getResources().getInteger(2131492890);
        boolean z2 = contextThemeWrapper.getResources().getBoolean(2131034181);
        this.mUseBackgroundBlur = z2;
        if (z2) {
            final int color = contextThemeWrapper.getColor(2131100784);
            final int color2 = contextThemeWrapper.getColor(2131100783);
            this.mCrossWindowBlurEnabledListener = new Consumer() { // from class: com.android.systemui.volume.VolumeDialogImpl$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    VolumeDialogImpl volumeDialogImpl = VolumeDialogImpl.this;
                    int i = color;
                    int i2 = color2;
                    Objects.requireNonNull(volumeDialogImpl);
                    BackgroundBlurDrawable backgroundBlurDrawable = volumeDialogImpl.mDialogRowsViewBackground;
                    if (!((Boolean) obj).booleanValue()) {
                        i = i2;
                    }
                    backgroundBlurDrawable.setColor(i);
                    volumeDialogImpl.mDialogRowsView.invalidate();
                }
            };
        }
        initDimens();
    }

    public static int getImpliedLevel(SeekBar seekBar, int i) {
        int max = seekBar.getMax();
        int i2 = max / 100;
        int i3 = i2 - 1;
        if (i == 0) {
            return 0;
        }
        if (i == max) {
            return i2;
        }
        return ((int) ((i / max) * i3)) + 1;
    }

    @Override // com.android.systemui.plugins.VolumeDialog
    public final void init(int i, VolumeDialog.Callback callback) {
        initDialog();
        Accessibility accessibility = this.mAccessibility;
        Objects.requireNonNull(accessibility);
        VolumeDialogImpl.this.mDialogView.setAccessibilityDelegate(accessibility);
        this.mController.addCallback(this.mControllerCallbackH, this.mHandler);
        this.mController.getState();
        this.mConfigurationController.addCallback(this);
    }
}
