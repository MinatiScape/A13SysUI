package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.leanback.R$color;
import com.android.systemui.navigationbar.NavigationModeController;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import kotlinx.atomicfu.AtomicFU;
/* loaded from: classes.dex */
public final class NgaMessageHandler {
    public static final boolean VERBOSE;
    public final AssistantPresenceHandler mAssistantPresenceHandler;
    public final Set<AudioInfoListener> mAudioInfoListeners;
    public final Set<CardInfoListener> mCardInfoListeners;
    public final Set<ChipsInfoListener> mChipsInfoListeners;
    public final Set<ClearListener> mClearListeners;
    public final Set<ConfigInfoListener> mConfigInfoListeners;
    public final Set<EdgeLightsInfoListener> mEdgeLightsInfoListeners;
    public final Set<GoBackListener> mGoBackListeners;
    public final Set<GreetingInfoListener> mGreetingInfoListeners;
    public final Handler mHandler;
    public boolean mIsGestureNav;
    public final Set<KeepAliveListener> mKeepAliveListeners;
    public final Set<KeyboardInfoListener> mKeyboardInfoListeners;
    public final Set<NavBarVisibilityListener> mNavBarVisibilityListeners;
    public final NgaUiController mNgaUiController;
    public final Set<StartActivityInfoListener> mStartActivityInfoListeners;
    public final Set<SwipeListener> mSwipeListeners;
    public final Set<TakeScreenshotListener> mTakeScreenshotListeners;
    public final Set<TranscriptionInfoListener> mTranscriptionInfoListeners;
    public final Set<WarmingListener> mWarmingListeners;
    public final Set<ZerostateInfoListener> mZerostateInfoListeners;

    /* loaded from: classes.dex */
    public interface AudioInfoListener {
        void onAudioInfo(float f, float f2);
    }

    /* loaded from: classes.dex */
    public interface CardInfoListener {
        void onCardInfo(boolean z, int i, boolean z2, boolean z3);
    }

    /* loaded from: classes.dex */
    public interface ChipsInfoListener {
        void onChipsInfo(ArrayList arrayList);
    }

    /* loaded from: classes.dex */
    public interface ClearListener {
        void onClear(boolean z);
    }

    /* loaded from: classes.dex */
    public interface ConfigInfoListener {
        void onConfigInfo(ConfigInfo configInfo);
    }

    /* loaded from: classes.dex */
    public interface EdgeLightsInfoListener {
        void onEdgeLightsInfo(String str, boolean z);
    }

    /* loaded from: classes.dex */
    public interface GoBackListener {
        void onGoBack();
    }

    /* loaded from: classes.dex */
    public interface GreetingInfoListener {
        void onGreetingInfo(String str, PendingIntent pendingIntent);
    }

    /* loaded from: classes.dex */
    public interface KeepAliveListener {
        void onKeepAlive();
    }

    /* loaded from: classes.dex */
    public interface KeyboardInfoListener {
        void onHideKeyboard();

        void onShowKeyboard(PendingIntent pendingIntent);
    }

    /* loaded from: classes.dex */
    public interface NavBarVisibilityListener {
        void onVisibleRequest(boolean z);
    }

    /* loaded from: classes.dex */
    public interface StartActivityInfoListener {
        void onStartActivityInfo(Intent intent, boolean z);
    }

    /* loaded from: classes.dex */
    public interface SwipeListener {
        void onSwipe(Bundle bundle);
    }

    /* loaded from: classes.dex */
    public interface TakeScreenshotListener {
        void onTakeScreenshot(PendingIntent pendingIntent);
    }

    /* loaded from: classes.dex */
    public interface TranscriptionInfoListener {
        void onTranscriptionInfo(String str, PendingIntent pendingIntent, int i);
    }

    /* loaded from: classes.dex */
    public interface WarmingListener {
        void onWarmingRequest(WarmingRequest warmingRequest);
    }

    /* loaded from: classes.dex */
    public interface ZerostateInfoListener {
        void onHideZerostate();

        void onShowZerostate(PendingIntent pendingIntent);
    }

    public NgaMessageHandler(NgaUiController ngaUiController, AssistantPresenceHandler assistantPresenceHandler, NavigationModeController navigationModeController, Set<KeepAliveListener> set, Set<AudioInfoListener> set2, Set<CardInfoListener> set3, Set<ConfigInfoListener> set4, Set<EdgeLightsInfoListener> set5, Set<TranscriptionInfoListener> set6, Set<GreetingInfoListener> set7, Set<ChipsInfoListener> set8, Set<ClearListener> set9, Set<StartActivityInfoListener> set10, Set<KeyboardInfoListener> set11, Set<ZerostateInfoListener> set12, Set<GoBackListener> set13, Set<SwipeListener> set14, Set<TakeScreenshotListener> set15, Set<WarmingListener> set16, Set<NavBarVisibilityListener> set17, Handler handler) {
        this.mNgaUiController = ngaUiController;
        this.mAssistantPresenceHandler = assistantPresenceHandler;
        this.mKeepAliveListeners = set;
        this.mAudioInfoListeners = set2;
        this.mCardInfoListeners = set3;
        this.mConfigInfoListeners = set4;
        this.mEdgeLightsInfoListeners = set5;
        this.mTranscriptionInfoListeners = set6;
        this.mGreetingInfoListeners = set7;
        this.mChipsInfoListeners = set8;
        this.mClearListeners = set9;
        this.mStartActivityInfoListeners = set10;
        this.mKeyboardInfoListeners = set11;
        this.mZerostateInfoListeners = set12;
        this.mGoBackListeners = set13;
        this.mSwipeListeners = set14;
        this.mTakeScreenshotListeners = set15;
        this.mWarmingListeners = set16;
        this.mNavBarVisibilityListeners = set17;
        this.mHandler = handler;
        this.mIsGestureNav = R$color.isGesturalMode(navigationModeController.addListener(new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.assist.uihints.NgaMessageHandler$$ExternalSyntheticLambda0
            @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
            public final void onNavigationModeChanged(int i) {
                NgaMessageHandler ngaMessageHandler = NgaMessageHandler.this;
                Objects.requireNonNull(ngaMessageHandler);
                ngaMessageHandler.mIsGestureNav = R$color.isGesturalMode(i);
            }
        }));
    }

    /* loaded from: classes.dex */
    public static class ConfigInfo {
        public final PendingIntent configurationCallback;
        public final boolean ngaIsAssistant;
        public PendingIntent onColorChanged;
        public final PendingIntent onKeyboardShowingChange;
        public final PendingIntent onTaskChange;
        public final PendingIntent onTouchInside;
        public final PendingIntent onTouchOutside;
        public final boolean sysUiIsNgaUi;

        public ConfigInfo(Bundle bundle) {
            boolean z;
            boolean z2 = bundle.getBoolean("is_available");
            boolean z3 = bundle.getBoolean("nga_is_assistant", z2);
            this.ngaIsAssistant = z3;
            if (!z2 || !z3) {
                z = false;
            } else {
                z = true;
            }
            this.sysUiIsNgaUi = z;
            this.onColorChanged = (PendingIntent) bundle.getParcelable("color_changed");
            this.onTouchOutside = (PendingIntent) bundle.getParcelable("touch_outside");
            this.onTouchInside = (PendingIntent) bundle.getParcelable("touch_inside");
            this.onTaskChange = (PendingIntent) bundle.getParcelable("task_stack_changed");
            this.onKeyboardShowingChange = (PendingIntent) bundle.getParcelable("keyboard_showing_changed");
            this.configurationCallback = (PendingIntent) bundle.getParcelable("configuration");
        }
    }

    /* loaded from: classes.dex */
    public static class WarmingRequest {
        public final PendingIntent onWarm;
        public final float threshold;

        public WarmingRequest(PendingIntent pendingIntent, float f) {
            this.onWarm = pendingIntent;
            this.threshold = AtomicFU.clamp(f, 0.0f, 1.0f);
        }
    }

    static {
        boolean z;
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        if (str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng")) {
            z = true;
        } else {
            z = false;
        }
        VERBOSE = z;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:107:0x025e  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x0297 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x02a5 A[LOOP:11: B:115:0x029f->B:117:0x02a5, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x02b8  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x02c6  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02d2  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x02e0  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x02eb  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x02f6  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:141:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:144:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x0321  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x032d  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0339  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0340  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0343  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:165:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:169:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x03c8  */
    /* JADX WARN: Removed duplicated region for block: B:177:0x03e2  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x03f8  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x041f  */
    /* JADX WARN: Removed duplicated region for block: B:189:0x0435  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0451  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x046d  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0478  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x0174  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01a2  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01a7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void processBundle(android.os.Bundle r18, java.lang.Runnable r19) {
        /*
            Method dump skipped, instructions count: 1276
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.assist.uihints.NgaMessageHandler.processBundle(android.os.Bundle, java.lang.Runnable):void");
    }
}
