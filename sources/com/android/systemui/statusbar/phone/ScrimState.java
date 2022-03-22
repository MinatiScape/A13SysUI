package com.android.systemui.statusbar.phone;

import android.graphics.Color;
import android.os.Trace;
import com.android.systemui.dock.DockManager;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda4;
import java.util.Objects;
/* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
/* loaded from: classes.dex */
public class ScrimState extends Enum<ScrimState> {
    public static final /* synthetic */ ScrimState[] $VALUES;
    public static final ScrimState AOD;
    public static final ScrimState AUTH_SCRIMMED;
    public static final ScrimState AUTH_SCRIMMED_SHADE;
    public static final ScrimState BOUNCER;
    public static final ScrimState BOUNCER_SCRIMMED;
    public static final ScrimState BRIGHTNESS_MIRROR;
    public static final ScrimState KEYGUARD;
    public static final ScrimState OFF;
    public static final ScrimState PULSING;
    public static final ScrimState SHADE_LOCKED;
    public static final ScrimState UNINITIALIZED;
    public static final ScrimState UNLOCKED;
    public float mAodFrontScrimAlpha;
    public float mBehindAlpha;
    public boolean mClipQsScrim;
    public float mDefaultScrimAlpha;
    public boolean mDisplayRequiresBlanking;
    public DockManager mDockManager;
    public DozeParameters mDozeParameters;
    public float mFrontAlpha;
    public boolean mHasBackdrop;
    public boolean mKeyguardFadingAway;
    public long mKeyguardFadingAwayDuration;
    public boolean mLaunchingAffordanceWithPreview;
    public float mNotifAlpha;
    public ScrimView mScrimBehind;
    public float mScrimBehindAlphaKeyguard;
    public ScrimView mScrimInFront;
    public boolean mWakeLockScreenSensorActive;
    public boolean mWallpaperSupportsAmbientMode;
    public boolean mBlankScreen = false;
    public long mAnimationDuration = 220;
    public int mFrontTint = 0;
    public int mBehindTint = 0;
    public int mNotifTint = 0;
    public boolean mAnimateChange = true;

    public float getMaxLightRevealScrimAlpha() {
        return 1.0f;
    }

    public boolean isLowPowerState() {
        return this instanceof AnonymousClass1;
    }

    public void prepare(ScrimState scrimState) {
    }

    static {
        ScrimState scrimState = new ScrimState("UNINITIALIZED", 0);
        UNINITIALIZED = scrimState;
        ScrimState scrimState2 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.1
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState3) {
                this.mFrontTint = -16777216;
                this.mBehindTint = -16777216;
                this.mFrontAlpha = 1.0f;
                this.mBehindAlpha = 1.0f;
                this.mAnimationDuration = 1000L;
            }
        };
        OFF = scrimState2;
        ScrimState scrimState3 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.2
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState4) {
                int i = 0;
                this.mBlankScreen = false;
                if (scrimState4 == ScrimState.AOD) {
                    this.mAnimationDuration = 667L;
                    if (this.mDisplayRequiresBlanking) {
                        this.mBlankScreen = true;
                    }
                } else if (scrimState4 == ScrimState.KEYGUARD) {
                    this.mAnimationDuration = 667L;
                } else {
                    this.mAnimationDuration = 220L;
                }
                this.mFrontTint = -16777216;
                this.mBehindTint = -16777216;
                boolean z = this.mClipQsScrim;
                if (z) {
                    i = -16777216;
                }
                this.mNotifTint = i;
                float f = 0.0f;
                this.mFrontAlpha = 0.0f;
                float f2 = 1.0f;
                if (!z) {
                    f2 = this.mScrimBehindAlphaKeyguard;
                }
                this.mBehindAlpha = f2;
                if (z) {
                    f = this.mScrimBehindAlphaKeyguard;
                }
                this.mNotifAlpha = f;
                if (z) {
                    updateScrimColor(this.mScrimBehind);
                }
            }
        };
        KEYGUARD = scrimState3;
        ScrimState scrimState4 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.3
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState5) {
                this.mFrontTint = -16777216;
                this.mFrontAlpha = 0.66f;
            }
        };
        AUTH_SCRIMMED_SHADE = scrimState4;
        ScrimState scrimState5 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.4
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState6) {
                this.mNotifTint = scrimState6.mNotifTint;
                this.mNotifAlpha = scrimState6.mNotifAlpha;
                this.mBehindTint = scrimState6.mBehindTint;
                this.mBehindAlpha = scrimState6.mBehindAlpha;
                this.mFrontTint = -16777216;
                this.mFrontAlpha = 0.66f;
            }
        };
        AUTH_SCRIMMED = scrimState5;
        ScrimState scrimState6 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.5
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState7) {
                float f;
                int i;
                float f2;
                boolean z = this.mClipQsScrim;
                if (z) {
                    f = 1.0f;
                } else {
                    f = this.mDefaultScrimAlpha;
                }
                this.mBehindAlpha = f;
                if (z) {
                    i = -16777216;
                } else {
                    i = 0;
                }
                this.mBehindTint = i;
                if (z) {
                    f2 = this.mDefaultScrimAlpha;
                } else {
                    f2 = 0.0f;
                }
                this.mNotifAlpha = f2;
                this.mNotifTint = 0;
                this.mFrontAlpha = 0.0f;
            }
        };
        BOUNCER = scrimState6;
        ScrimState scrimState7 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.6
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState8) {
                this.mBehindAlpha = 0.0f;
                this.mFrontAlpha = this.mDefaultScrimAlpha;
            }
        };
        BOUNCER_SCRIMMED = scrimState7;
        ScrimState scrimState8 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.7
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final int getBehindTint() {
                return -16777216;
            }

            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState9) {
                float f;
                boolean z = this.mClipQsScrim;
                if (z) {
                    f = 1.0f;
                } else {
                    f = this.mDefaultScrimAlpha;
                }
                this.mBehindAlpha = f;
                this.mNotifAlpha = 1.0f;
                this.mFrontAlpha = 0.0f;
                this.mBehindTint = -16777216;
                if (z) {
                    updateScrimColor(this.mScrimBehind);
                }
            }
        };
        SHADE_LOCKED = scrimState8;
        ScrimState scrimState9 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.8
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState10) {
                this.mBehindAlpha = 0.0f;
                this.mFrontAlpha = 0.0f;
            }
        };
        BRIGHTNESS_MIRROR = scrimState9;
        ScrimState scrimState10 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.9
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final boolean isLowPowerState() {
                return true;
            }

            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final float getMaxLightRevealScrimAlpha() {
                if (!this.mWallpaperSupportsAmbientMode || this.mHasBackdrop) {
                    return 1.0f;
                }
                return 0.0f;
            }

            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState11) {
                float f;
                boolean z;
                boolean alwaysOn = this.mDozeParameters.getAlwaysOn();
                DozeParameters dozeParameters = this.mDozeParameters;
                Objects.requireNonNull(dozeParameters);
                boolean quickPickupSensorEnabled = dozeParameters.mAmbientDisplayConfiguration.quickPickupSensorEnabled(-2);
                boolean isDocked = this.mDockManager.isDocked();
                this.mBlankScreen = this.mDisplayRequiresBlanking;
                this.mFrontTint = -16777216;
                if (alwaysOn || isDocked || quickPickupSensorEnabled) {
                    f = this.mAodFrontScrimAlpha;
                } else {
                    f = 1.0f;
                }
                this.mFrontAlpha = f;
                this.mBehindTint = -16777216;
                this.mBehindAlpha = 0.0f;
                this.mAnimationDuration = 1000L;
                DozeParameters dozeParameters2 = this.mDozeParameters;
                Objects.requireNonNull(dozeParameters2);
                if (!dozeParameters2.mControlScreenOffAnimation || this.mDozeParameters.shouldShowLightRevealScrim()) {
                    z = false;
                } else {
                    z = true;
                }
                this.mAnimateChange = z;
            }
        };
        AOD = scrimState10;
        ScrimState scrimState11 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.10
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final float getMaxLightRevealScrimAlpha() {
                if (this.mWakeLockScreenSensorActive) {
                    return 0.6f;
                }
                return ScrimState.AOD.getMaxLightRevealScrimAlpha();
            }

            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState12) {
                long j;
                this.mFrontAlpha = this.mAodFrontScrimAlpha;
                this.mBehindTint = -16777216;
                this.mFrontTint = -16777216;
                this.mBlankScreen = this.mDisplayRequiresBlanking;
                if (this.mWakeLockScreenSensorActive) {
                    j = 1000;
                } else {
                    j = 220;
                }
                this.mAnimationDuration = j;
            }
        };
        PULSING = scrimState11;
        ScrimState scrimState12 = new ScrimState() { // from class: com.android.systemui.statusbar.phone.ScrimState.11
            @Override // com.android.systemui.statusbar.phone.ScrimState
            public final void prepare(ScrimState scrimState13) {
                float f;
                long j;
                boolean z;
                boolean z2;
                if (this.mClipQsScrim) {
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                this.mBehindAlpha = f;
                this.mNotifAlpha = 0.0f;
                this.mFrontAlpha = 0.0f;
                if (this.mKeyguardFadingAway) {
                    j = this.mKeyguardFadingAwayDuration;
                } else {
                    j = 300;
                }
                this.mAnimationDuration = j;
                ScrimState scrimState14 = ScrimState.AOD;
                if (scrimState13 == scrimState14 || scrimState13 == ScrimState.PULSING) {
                    z = true;
                } else {
                    z = false;
                }
                if (this.mLaunchingAffordanceWithPreview || z) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                this.mAnimateChange = z2;
                this.mFrontTint = 0;
                this.mBehindTint = -16777216;
                this.mBlankScreen = false;
                if (scrimState13 == scrimState14) {
                    updateScrimColor(this.mScrimInFront);
                    updateScrimColor(this.mScrimBehind);
                    this.mFrontTint = -16777216;
                    this.mBehindTint = -16777216;
                    this.mBlankScreen = true;
                }
                if (this.mClipQsScrim) {
                    updateScrimColor(this.mScrimBehind);
                }
            }
        };
        UNLOCKED = scrimState12;
        $VALUES = new ScrimState[]{scrimState, scrimState2, scrimState3, scrimState4, scrimState5, scrimState6, scrimState7, scrimState8, scrimState9, scrimState10, scrimState11, scrimState12};
    }

    public static ScrimState valueOf(String str) {
        return (ScrimState) Enum.valueOf(ScrimState.class, str);
    }

    public static ScrimState[] values() {
        return (ScrimState[]) $VALUES.clone();
    }

    public final void updateScrimColor(ScrimView scrimView) {
        String str;
        String str2;
        if (scrimView == this.mScrimInFront) {
            str = "front_scrim_alpha";
        } else {
            str = "back_scrim_alpha";
        }
        Trace.traceCounter(4096L, str, (int) 255.0f);
        if (scrimView == this.mScrimInFront) {
            str2 = "front_scrim_tint";
        } else {
            str2 = "back_scrim_tint";
        }
        Trace.traceCounter(4096L, str2, Color.alpha(-16777216));
        Objects.requireNonNull(scrimView);
        scrimView.executeOnExecutor(new ScrimView$$ExternalSyntheticLambda4(scrimView, -16777216));
        scrimView.setViewAlpha(1.0f);
    }

    public ScrimState(String str, int i) {
    }

    public int getBehindTint() {
        return this.mBehindTint;
    }
}
