package com.android.systemui.statusbar.notification.collection.listbuilder;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import java.util.List;
import java.util.Objects;
/* compiled from: ShadeListBuilderLogger.kt */
/* loaded from: classes.dex */
public final class ShadeListBuilderLogger {
    public final LogBuffer buffer;

    public final void logDuplicateSummary(int i, String str, String str2, String str3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        ShadeListBuilderLogger$logDuplicateSummary$2 shadeListBuilderLogger$logDuplicateSummary$2 = ShadeListBuilderLogger$logDuplicateSummary$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logDuplicateSummary$2);
            obtain.long1 = i;
            obtain.str1 = str;
            obtain.str2 = str2;
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logDuplicateTopLevelKey(int i, String str) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.WARNING;
        ShadeListBuilderLogger$logDuplicateTopLevelKey$2 shadeListBuilderLogger$logDuplicateTopLevelKey$2 = ShadeListBuilderLogger$logDuplicateTopLevelKey$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logDuplicateTopLevelKey$2);
            obtain.long1 = i;
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logEndBuildList(int i, int i2, int i3) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logEndBuildList$2 shadeListBuilderLogger$logEndBuildList$2 = ShadeListBuilderLogger$logEndBuildList$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logEndBuildList$2);
            obtain.long1 = i;
            obtain.int1 = i2;
            obtain.int2 = i3;
            logBuffer.push(obtain);
        }
    }

    public final void logEntryAttachStateChanged(int i, String str, GroupEntry groupEntry, GroupEntry groupEntry2) {
        String str2;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logEntryAttachStateChanged$2 shadeListBuilderLogger$logEntryAttachStateChanged$2 = ShadeListBuilderLogger$logEntryAttachStateChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logEntryAttachStateChanged$2);
            obtain.long1 = i;
            obtain.str1 = str;
            String str3 = null;
            if (groupEntry == null) {
                str2 = null;
            } else {
                str2 = groupEntry.mKey;
            }
            obtain.str2 = str2;
            if (groupEntry2 != null) {
                str3 = groupEntry2.mKey;
            }
            obtain.str3 = str3;
            logBuffer.push(obtain);
        }
    }

    public final void logFilterChanged(int i, NotifFilter notifFilter, NotifFilter notifFilter2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logFilterChanged$2 shadeListBuilderLogger$logFilterChanged$2 = ShadeListBuilderLogger$logFilterChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFilterChanged$2);
            obtain.long1 = i;
            String str2 = null;
            if (notifFilter == null) {
                str = null;
            } else {
                str = notifFilter.mName;
            }
            obtain.str1 = str;
            if (notifFilter2 != null) {
                str2 = notifFilter2.mName;
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logFinalList(List<? extends ListEntry> list) {
        LogLevel logLevel = LogLevel.DEBUG;
        if (list.isEmpty()) {
            LogBuffer logBuffer = this.buffer;
            ShadeListBuilderLogger$logFinalList$2 shadeListBuilderLogger$logFinalList$2 = ShadeListBuilderLogger$logFinalList$2.INSTANCE;
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                logBuffer.push(logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFinalList$2));
            }
        }
        int size = list.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            ListEntry listEntry = (ListEntry) list.get(i);
            LogBuffer logBuffer2 = this.buffer;
            ShadeListBuilderLogger$logFinalList$4 shadeListBuilderLogger$logFinalList$4 = ShadeListBuilderLogger$logFinalList$4.INSTANCE;
            Objects.requireNonNull(logBuffer2);
            if (!logBuffer2.frozen) {
                LogMessageImpl obtain = logBuffer2.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFinalList$4);
                obtain.int1 = i;
                obtain.str1 = listEntry.getKey();
                logBuffer2.push(obtain);
            }
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                Objects.requireNonNull(groupEntry);
                NotificationEntry notificationEntry = groupEntry.mSummary;
                if (notificationEntry != null) {
                    LogBuffer logBuffer3 = this.buffer;
                    ShadeListBuilderLogger$logFinalList$5$2 shadeListBuilderLogger$logFinalList$5$2 = ShadeListBuilderLogger$logFinalList$5$2.INSTANCE;
                    Objects.requireNonNull(logBuffer3);
                    if (!logBuffer3.frozen) {
                        LogMessageImpl obtain2 = logBuffer3.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFinalList$5$2);
                        obtain2.str1 = notificationEntry.mKey;
                        logBuffer3.push(obtain2);
                    }
                }
                int size2 = groupEntry.mUnmodifiableChildren.size();
                int i3 = 0;
                while (i3 < size2) {
                    int i4 = i3 + 1;
                    NotificationEntry notificationEntry2 = groupEntry.mUnmodifiableChildren.get(i3);
                    LogBuffer logBuffer4 = this.buffer;
                    ShadeListBuilderLogger$logFinalList$7 shadeListBuilderLogger$logFinalList$7 = ShadeListBuilderLogger$logFinalList$7.INSTANCE;
                    Objects.requireNonNull(logBuffer4);
                    if (!logBuffer4.frozen) {
                        LogMessageImpl obtain3 = logBuffer4.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFinalList$7);
                        obtain3.int1 = i3;
                        Objects.requireNonNull(notificationEntry2);
                        obtain3.str1 = notificationEntry2.mKey;
                        logBuffer4.push(obtain3);
                    }
                    i3 = i4;
                }
            }
            i = i2;
        }
    }

    public final void logFinalizeFilterInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logFinalizeFilterInvalidated$2 shadeListBuilderLogger$logFinalizeFilterInvalidated$2 = ShadeListBuilderLogger$logFinalizeFilterInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logFinalizeFilterInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logGroupPruningSuppressed(int i, GroupEntry groupEntry) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logGroupPruningSuppressed$2 shadeListBuilderLogger$logGroupPruningSuppressed$2 = ShadeListBuilderLogger$logGroupPruningSuppressed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logGroupPruningSuppressed$2);
            obtain.long1 = i;
            if (groupEntry == null) {
                str = null;
            } else {
                str = groupEntry.mKey;
            }
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifComparatorInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logNotifComparatorInvalidated$2 shadeListBuilderLogger$logNotifComparatorInvalidated$2 = ShadeListBuilderLogger$logNotifComparatorInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logNotifComparatorInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logNotifSectionInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logNotifSectionInvalidated$2 shadeListBuilderLogger$logNotifSectionInvalidated$2 = ShadeListBuilderLogger$logNotifSectionInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logNotifSectionInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logOnBuildList() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logOnBuildList$2 shadeListBuilderLogger$logOnBuildList$2 = ShadeListBuilderLogger$logOnBuildList$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logOnBuildList$2));
        }
    }

    public final void logParentChangeSuppressed(int i, GroupEntry groupEntry, GroupEntry groupEntry2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logParentChangeSuppressed$2 shadeListBuilderLogger$logParentChangeSuppressed$2 = ShadeListBuilderLogger$logParentChangeSuppressed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logParentChangeSuppressed$2);
            obtain.long1 = i;
            String str2 = null;
            if (groupEntry == null) {
                str = null;
            } else {
                str = groupEntry.mKey;
            }
            obtain.str1 = str;
            if (groupEntry2 != null) {
                str2 = groupEntry2.mKey;
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logParentChanged(int i, GroupEntry groupEntry, GroupEntry groupEntry2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logParentChanged$2 shadeListBuilderLogger$logParentChanged$2 = ShadeListBuilderLogger$logParentChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logParentChanged$2);
            obtain.long1 = i;
            String str2 = null;
            if (groupEntry == null) {
                str = null;
            } else {
                str = groupEntry.mKey;
            }
            obtain.str1 = str;
            if (groupEntry2 != null) {
                str2 = groupEntry2.mKey;
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logPipelineRunSuppressed() {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logPipelineRunSuppressed$2 shadeListBuilderLogger$logPipelineRunSuppressed$2 = ShadeListBuilderLogger$logPipelineRunSuppressed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            logBuffer.push(logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPipelineRunSuppressed$2));
        }
    }

    public final void logPreGroupFilterInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logPreGroupFilterInvalidated$2 shadeListBuilderLogger$logPreGroupFilterInvalidated$2 = ShadeListBuilderLogger$logPreGroupFilterInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPreGroupFilterInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logPreRenderInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logPreRenderInvalidated$2 shadeListBuilderLogger$logPreRenderInvalidated$2 = ShadeListBuilderLogger$logPreRenderInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPreRenderInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logPromoterChanged(int i, NotifPromoter notifPromoter, NotifPromoter notifPromoter2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logPromoterChanged$2 shadeListBuilderLogger$logPromoterChanged$2 = ShadeListBuilderLogger$logPromoterChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPromoterChanged$2);
            obtain.long1 = i;
            String str2 = null;
            if (notifPromoter == null) {
                str = null;
            } else {
                str = notifPromoter.mName;
            }
            obtain.str1 = str;
            if (notifPromoter2 != null) {
                str2 = notifPromoter2.mName;
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logPromoterInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logPromoterInvalidated$2 shadeListBuilderLogger$logPromoterInvalidated$2 = ShadeListBuilderLogger$logPromoterInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPromoterInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logPrunedReasonChanged(int i, String str, String str2) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logPrunedReasonChanged$2 shadeListBuilderLogger$logPrunedReasonChanged$2 = ShadeListBuilderLogger$logPrunedReasonChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logPrunedReasonChanged$2);
            obtain.long1 = i;
            obtain.str1 = str;
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logReorderingAllowedInvalidated(String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.DEBUG;
        ShadeListBuilderLogger$logReorderingAllowedInvalidated$2 shadeListBuilderLogger$logReorderingAllowedInvalidated$2 = ShadeListBuilderLogger$logReorderingAllowedInvalidated$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logReorderingAllowedInvalidated$2);
            obtain.str1 = str;
            obtain.int1 = i;
            logBuffer.push(obtain);
        }
    }

    public final void logSectionChangeSuppressed(int i, NotifSection notifSection, NotifSection notifSection2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logSectionChangeSuppressed$2 shadeListBuilderLogger$logSectionChangeSuppressed$2 = ShadeListBuilderLogger$logSectionChangeSuppressed$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logSectionChangeSuppressed$2);
            obtain.long1 = i;
            String str2 = null;
            if (notifSection == null) {
                str = null;
            } else {
                str = notifSection.getLabel();
            }
            obtain.str1 = str;
            if (notifSection2 != null) {
                str2 = notifSection2.getLabel();
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public final void logSectionChanged(int i, NotifSection notifSection, NotifSection notifSection2) {
        String str;
        LogBuffer logBuffer = this.buffer;
        LogLevel logLevel = LogLevel.INFO;
        ShadeListBuilderLogger$logSectionChanged$2 shadeListBuilderLogger$logSectionChanged$2 = ShadeListBuilderLogger$logSectionChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("ShadeListBuilder", logLevel, shadeListBuilderLogger$logSectionChanged$2);
            obtain.long1 = i;
            String str2 = null;
            if (notifSection == null) {
                str = null;
            } else {
                str = notifSection.getLabel();
            }
            obtain.str1 = str;
            if (notifSection2 != null) {
                str2 = notifSection2.getLabel();
            }
            obtain.str2 = str2;
            logBuffer.push(obtain);
        }
    }

    public ShadeListBuilderLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }
}
