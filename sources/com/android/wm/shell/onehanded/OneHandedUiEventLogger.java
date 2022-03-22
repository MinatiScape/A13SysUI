package com.android.wm.shell.onehanded;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
/* loaded from: classes.dex */
public final class OneHandedUiEventLogger {
    public final UiEventLogger mUiEventLogger;

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum OneHandedSettingsTogglesEvent implements UiEventLogger.UiEventEnum {
        /* JADX INFO: Fake field, exist only in values array */
        INVALID(0),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON(356),
        ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF(357),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON(358),
        ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF(359),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON(360),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF(361),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER(362),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4(363),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8(364),
        ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12(365),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON(847),
        ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF(848),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON(870),
        ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF(871);
        
        private final int mId;

        OneHandedSettingsTogglesEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum OneHandedTriggerEvent implements UiEventLogger.UiEventEnum {
        /* JADX INFO: Fake field, exist only in values array */
        INVALID(0),
        ONE_HANDED_TRIGGER_GESTURE_IN(366),
        ONE_HANDED_TRIGGER_GESTURE_OUT(367),
        ONE_HANDED_TRIGGER_OVERSPACE_OUT(368),
        ONE_HANDED_TRIGGER_POP_IME_OUT(369),
        ONE_HANDED_TRIGGER_ROTATION_OUT(370),
        ONE_HANDED_TRIGGER_APP_TAPS_OUT(371),
        ONE_HANDED_TRIGGER_TIMEOUT_OUT(372),
        ONE_HANDED_TRIGGER_SCREEN_OFF_OUT(449);
        
        private final int mId;

        OneHandedTriggerEvent(int i) {
            this.mId = i;
        }

        public final int getId() {
            return this.mId;
        }
    }

    public OneHandedUiEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }

    public final void writeEvent(int i) {
        switch (i) {
            case 0:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_IN);
                return;
            case 1:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_GESTURE_OUT);
                return;
            case 2:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_OVERSPACE_OUT);
                return;
            case 3:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_POP_IME_OUT);
                return;
            case 4:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_ROTATION_OUT);
                return;
            case 5:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_APP_TAPS_OUT);
                return;
            case FalsingManager.VERSION /* 6 */:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_TIMEOUT_OUT);
                return;
            case 7:
                this.mUiEventLogger.log(OneHandedTriggerEvent.ONE_HANDED_TRIGGER_SCREEN_OFF_OUT);
                return;
            case 8:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_ON);
                return;
            case 9:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_ENABLED_OFF);
                return;
            case 10:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_ON);
                return;
            case QSTileImpl.H.STALE /* 11 */:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_APP_TAPS_EXIT_OFF);
                return;
            case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_ON);
                return;
            case QS.VERSION /* 13 */:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_EXIT_OFF);
                return;
            case 14:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_NEVER);
                return;
            case 15:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_4);
                return;
            case 16:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_8);
                return;
            case 17:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_TIMEOUT_SECONDS_12);
                return;
            case 18:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_ON);
                return;
            case 19:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHOW_NOTIFICATION_ENABLED_OFF);
                return;
            case 20:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_ON);
                return;
            case 21:
                this.mUiEventLogger.log(OneHandedSettingsTogglesEvent.ONE_HANDED_SETTINGS_TOGGLES_SHORTCUT_ENABLED_OFF);
                return;
            default:
                return;
        }
    }
}
