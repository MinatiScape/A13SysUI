package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.internal.logging.UiEventLogger;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
/* compiled from: NotificationVoiceReplyLogger.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyLogger {
    public final UiEventLogger eventLogger;
    public final LogBuffer logBuffer;

    /* compiled from: NotificationVoiceReplyLogger.kt */
    /* loaded from: classes.dex */
    public enum CtaVisibleState {
        NOT_DOZING,
        DISABLED,
        NO_HUN_VIEW,
        SHOWING
    }

    /* compiled from: NotificationVoiceReplyLogger.kt */
    /* loaded from: classes.dex */
    public enum SendType {
        UNLOCKED,
        BYPASS,
        DELAYED,
        BOUNCED
    }

    public final void logFocus(String str, boolean z) {
        VoiceReplyEvent voiceReplyEvent;
        UiEventLogger uiEventLogger = this.eventLogger;
        if (z) {
            voiceReplyEvent = VoiceReplyEvent.STATE_IN_SESSION_HAS_START_TEXT;
        } else {
            voiceReplyEvent = VoiceReplyEvent.STATE_IN_SESSION_NO_START_TEXT;
        }
        uiEventLogger.log(voiceReplyEvent);
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logFocus$2 notificationVoiceReplyLogger$logFocus$2 = NotificationVoiceReplyLogger$logFocus$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logFocus$2);
            obtain.str1 = str;
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logHotwordAvailabilityChanged(int i, boolean z) {
        String str;
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logHotwordAvailabilityChanged$2 notificationVoiceReplyLogger$logHotwordAvailabilityChanged$2 = NotificationVoiceReplyLogger$logHotwordAvailabilityChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logHotwordAvailabilityChanged$2);
            obtain.int1 = i;
            if (z) {
                str = "Enabling";
            } else {
                str = "Disabling";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logMsgSent(String str, SendType sendType) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logMsgSent$2 notificationVoiceReplyLogger$logMsgSent$2 = NotificationVoiceReplyLogger$logMsgSent$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logMsgSent$2);
            obtain.str1 = str;
            obtain.str2 = sendType.name();
            logBuffer.push(obtain);
        }
    }

    public final void logQuickPhraseAvailabilityChanged(int i, boolean z) {
        String str;
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2 notificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2 = NotificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logQuickPhraseAvailabilityChanged$2);
            obtain.int1 = i;
            if (z) {
                str = "Enabling";
            } else {
                str = "Disabling";
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logRegisterCallbacks(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logRegisterCallbacks$2 notificationVoiceReplyLogger$logRegisterCallbacks$2 = NotificationVoiceReplyLogger$logRegisterCallbacks$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logRegisterCallbacks$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logRejectCandidate(String str, String str2) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logRejectCandidate$2 notificationVoiceReplyLogger$logRejectCandidate$2 = NotificationVoiceReplyLogger$logRejectCandidate$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logRejectCandidate$2);
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logSessionEnd() {
        this.eventLogger.log(VoiceReplyEvent.SESSION_ENDED);
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStatic$2 notificationVoiceReplyLogger$logStatic$2 = new NotificationVoiceReplyLogger$logStatic$2("Session has ended");
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStatic$2));
        }
    }

    public final void logSetFeatureEnabled(int i, int i2) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logSetFeatureEnabled$2 notificationVoiceReplyLogger$logSetFeatureEnabled$2 = NotificationVoiceReplyLogger$logSetFeatureEnabled$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logSetFeatureEnabled$2);
            obtain.int1 = i;
            obtain.int2 = i2;
            logBuffer.push(obtain);
        }
    }

    public final void logStartVoiceReply(int i, int i2, String str) {
        boolean z;
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStartVoiceReply$2 notificationVoiceReplyLogger$logStartVoiceReply$2 = NotificationVoiceReplyLogger$logStartVoiceReply$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStartVoiceReply$2);
            obtain.int1 = i;
            obtain.int2 = i2;
            if (str != null) {
                z = true;
            } else {
                z = false;
            }
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logStateHasCandidate(String str, CtaVisibleState ctaVisibleState, CtaState ctaState) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logStateHasCandidate$2 notificationVoiceReplyLogger$logStateHasCandidate$2 = NotificationVoiceReplyLogger$logStateHasCandidate$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logStateHasCandidate$2);
            obtain.str1 = str;
            obtain.str2 = ctaVisibleState.name();
            obtain.str3 = ctaState.name();
            logBuffer.push(obtain);
        }
    }

    public final void logUnregisterCallbacks(int i) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logUnregisterCallbacks$2 notificationVoiceReplyLogger$logUnregisterCallbacks$2 = NotificationVoiceReplyLogger$logUnregisterCallbacks$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logUnregisterCallbacks$2);
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logVoiceAuthStateChanged(int i, int i2, int i3) {
        LogBuffer logBuffer = this.logBuffer;
        LogLevel logLevel = LogLevel.DEBUG;
        NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2 notificationVoiceReplyLogger$logVoiceAuthStateChanged$2 = NotificationVoiceReplyLogger$logVoiceAuthStateChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("NotifVoiceReply", logLevel, notificationVoiceReplyLogger$logVoiceAuthStateChanged$2);
            obtain.int1 = i;
            obtain.int2 = i2;
            boolean z = true;
            if (i3 != 1) {
                z = false;
            }
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public NotificationVoiceReplyLogger(LogBuffer logBuffer, UiEventLogger uiEventLogger) {
        this.logBuffer = logBuffer;
        this.eventLogger = uiEventLogger;
    }
}
