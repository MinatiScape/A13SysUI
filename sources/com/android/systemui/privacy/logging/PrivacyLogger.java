package com.android.systemui.privacy.logging;

import android.permission.PermissionGroupUsage;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.privacy.PrivacyItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
/* compiled from: PrivacyLogger.kt */
/* loaded from: classes.dex */
public final class PrivacyLogger {
    public final LogBuffer buffer;

    public final void logChipVisible(boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logChipVisible$2 privacyLogger$logChipVisible$2 = PrivacyLogger$logChipVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logChipVisible$2);
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public final void logCurrentProfilesChanged(List<Integer> list) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logCurrentProfilesChanged$2 privacyLogger$logCurrentProfilesChanged$2 = PrivacyLogger$logCurrentProfilesChanged$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logCurrentProfilesChanged$2);
            obtain.str1 = list.toString();
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyDialogDismissed() {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyDialogDismissed$2 privacyLogger$logPrivacyDialogDismissed$2 = PrivacyLogger$logPrivacyDialogDismissed$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyDialogDismissed$2));
        }
    }

    public final void logPrivacyItemsToHold(ArrayList arrayList) {
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logPrivacyItemsToHold$2 privacyLogger$logPrivacyItemsToHold$2 = PrivacyLogger$logPrivacyItemsToHold$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsToHold$2);
            obtain.str1 = CollectionsKt___CollectionsKt.joinToString$default(arrayList, ", ", null, null, PrivacyLogger$listToString$1.INSTANCE, 30);
            logBuffer.push(obtain);
        }
    }

    public final void logPrivacyItemsUpdateScheduled(long j) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logPrivacyItemsUpdateScheduled$2 privacyLogger$logPrivacyItemsUpdateScheduled$2 = PrivacyLogger$logPrivacyItemsUpdateScheduled$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logPrivacyItemsUpdateScheduled$2);
            obtain.str1 = PrivacyLoggerKt.DATE_FORMAT.format(Long.valueOf(System.currentTimeMillis() + j));
            logBuffer.push(obtain);
        }
    }

    public final void logRetrievedPrivacyItemsList(List<PrivacyItem> list) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logRetrievedPrivacyItemsList$2 privacyLogger$logRetrievedPrivacyItemsList$2 = PrivacyLogger$logRetrievedPrivacyItemsList$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logRetrievedPrivacyItemsList$2);
            obtain.str1 = CollectionsKt___CollectionsKt.joinToString$default(list, ", ", null, null, PrivacyLogger$listToString$1.INSTANCE, 30);
            logBuffer.push(obtain);
        }
    }

    public final void logShowDialogContents(ArrayList arrayList) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logShowDialogContents$2 privacyLogger$logShowDialogContents$2 = PrivacyLogger$logShowDialogContents$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logShowDialogContents$2);
            obtain.str1 = arrayList.toString();
            logBuffer.push(obtain);
        }
    }

    public final void logStartSettingsActivityFromDialog(String str, int i) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStartSettingsActivityFromDialog$2 privacyLogger$logStartSettingsActivityFromDialog$2 = PrivacyLogger$logStartSettingsActivityFromDialog$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStartSettingsActivityFromDialog$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logStatusBarIconsVisible(boolean z, boolean z2, boolean z3) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logStatusBarIconsVisible$2 privacyLogger$logStatusBarIconsVisible$2 = PrivacyLogger$logStatusBarIconsVisible$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logStatusBarIconsVisible$2);
            obtain.bool1 = z;
            obtain.bool2 = z2;
            obtain.bool3 = z3;
            logBuffer.push(obtain);
        }
    }

    public final void logUnfilteredPermGroupUsage(List<PermissionGroupUsage> list) {
        LogLevel logLevel = LogLevel.DEBUG;
        PrivacyLogger$logUnfilteredPermGroupUsage$2 privacyLogger$logUnfilteredPermGroupUsage$2 = PrivacyLogger$logUnfilteredPermGroupUsage$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUnfilteredPermGroupUsage$2);
            obtain.str1 = list.toString();
            logBuffer.push(obtain);
        }
    }

    public final void logUpdatedItemFromAppOps(int i, int i2, String str, boolean z) {
        LogLevel logLevel = LogLevel.INFO;
        PrivacyLogger$logUpdatedItemFromAppOps$2 privacyLogger$logUpdatedItemFromAppOps$2 = PrivacyLogger$logUpdatedItemFromAppOps$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("PrivacyLog", logLevel, privacyLogger$logUpdatedItemFromAppOps$2);
            obtain.int1 = i;
            obtain.int2 = i2;
            obtain.str1 = str;
            obtain.bool1 = z;
            logBuffer.push(obtain);
        }
    }

    public PrivacyLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
