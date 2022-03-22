package com.android.systemui.log;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LogMessageImpl.kt */
/* loaded from: classes.dex */
public final class LogMessageImpl implements LogMessage {
    public LogLevel level;
    public Function1<? super LogMessage, String> printer;
    public String tag = "UnknownTag";
    public long timestamp = 0;
    public String str1 = null;
    public String str2 = null;
    public String str3 = null;
    public int int1 = 0;
    public int int2 = 0;
    public long long1 = 0;
    public long long2 = 0;
    public double double1 = 0.0d;
    public boolean bool1 = false;
    public boolean bool2 = false;
    public boolean bool3 = false;
    public boolean bool4 = false;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LogMessageImpl)) {
            return false;
        }
        LogMessageImpl logMessageImpl = (LogMessageImpl) obj;
        LogLevel logLevel = this.level;
        Objects.requireNonNull(logMessageImpl);
        return logLevel == logMessageImpl.level && Intrinsics.areEqual(this.tag, logMessageImpl.tag) && this.timestamp == logMessageImpl.timestamp && Intrinsics.areEqual(this.printer, logMessageImpl.printer) && Intrinsics.areEqual(this.str1, logMessageImpl.str1) && Intrinsics.areEqual(this.str2, logMessageImpl.str2) && Intrinsics.areEqual(this.str3, logMessageImpl.str3) && this.int1 == logMessageImpl.int1 && this.int2 == logMessageImpl.int2 && this.long1 == logMessageImpl.long1 && this.long2 == logMessageImpl.long2 && Intrinsics.areEqual(Double.valueOf(this.double1), Double.valueOf(logMessageImpl.double1)) && this.bool1 == logMessageImpl.bool1 && this.bool2 == logMessageImpl.bool2 && this.bool3 == logMessageImpl.bool3 && this.bool4 == logMessageImpl.bool4;
    }

    public LogMessageImpl() {
        LogLevel logLevel = LogLevel.DEBUG;
        Function1<LogMessage, String> function1 = LogMessageImplKt.DEFAULT_RENDERER;
        this.level = logLevel;
        this.printer = function1;
    }

    public final int hashCode() {
        int i;
        int i2;
        int hashCode = this.tag.hashCode();
        int hashCode2 = (this.printer.hashCode() + ((Long.hashCode(this.timestamp) + ((hashCode + (this.level.hashCode() * 31)) * 31)) * 31)) * 31;
        String str = this.str1;
        int i3 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i4 = (hashCode2 + i) * 31;
        String str2 = this.str2;
        if (str2 == null) {
            i2 = 0;
        } else {
            i2 = str2.hashCode();
        }
        int i5 = (i4 + i2) * 31;
        String str3 = this.str3;
        if (str3 != null) {
            i3 = str3.hashCode();
        }
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.int2, FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.int1, (i5 + i3) * 31, 31), 31);
        int hashCode3 = (Double.hashCode(this.double1) + ((Long.hashCode(this.long2) + ((Long.hashCode(this.long1) + m) * 31)) * 31)) * 31;
        boolean z = this.bool1;
        int i6 = 1;
        if (z) {
            z = true;
        }
        int i7 = z ? 1 : 0;
        int i8 = z ? 1 : 0;
        int i9 = (hashCode3 + i7) * 31;
        boolean z2 = this.bool2;
        if (z2) {
            z2 = true;
        }
        int i10 = z2 ? 1 : 0;
        int i11 = z2 ? 1 : 0;
        int i12 = (i9 + i10) * 31;
        boolean z3 = this.bool3;
        if (z3) {
            z3 = true;
        }
        int i13 = z3 ? 1 : 0;
        int i14 = z3 ? 1 : 0;
        int i15 = (i12 + i13) * 31;
        boolean z4 = this.bool4;
        if (!z4) {
            i6 = z4 ? 1 : 0;
        }
        return i15 + i6;
    }

    public final void reset(String str, LogLevel logLevel, long j, Function1<? super LogMessage, String> function1) {
        this.level = logLevel;
        this.tag = str;
        this.timestamp = j;
        this.printer = function1;
        this.str1 = null;
        this.str2 = null;
        this.str3 = null;
        this.int1 = 0;
        this.int2 = 0;
        this.long1 = 0L;
        this.long2 = 0L;
        this.double1 = 0.0d;
        this.bool1 = false;
        this.bool2 = false;
        this.bool3 = false;
        this.bool4 = false;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("LogMessageImpl(level=");
        m.append(this.level);
        m.append(", tag=");
        m.append(this.tag);
        m.append(", timestamp=");
        m.append(this.timestamp);
        m.append(", printer=");
        m.append(this.printer);
        m.append(", str1=");
        m.append((Object) this.str1);
        m.append(", str2=");
        m.append((Object) this.str2);
        m.append(", str3=");
        m.append((Object) this.str3);
        m.append(", int1=");
        m.append(this.int1);
        m.append(", int2=");
        m.append(this.int2);
        m.append(", long1=");
        m.append(this.long1);
        m.append(", long2=");
        m.append(this.long2);
        m.append(", double1=");
        m.append(this.double1);
        m.append(", bool1=");
        m.append(this.bool1);
        m.append(", bool2=");
        m.append(this.bool2);
        m.append(", bool3=");
        m.append(this.bool3);
        m.append(", bool4=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.bool4, ')');
    }

    @Override // com.android.systemui.log.LogMessage
    public final boolean getBool1() {
        return this.bool1;
    }

    @Override // com.android.systemui.log.LogMessage
    public final boolean getBool2() {
        return this.bool2;
    }

    @Override // com.android.systemui.log.LogMessage
    public final boolean getBool3() {
        return this.bool3;
    }

    @Override // com.android.systemui.log.LogMessage
    public final boolean getBool4() {
        return this.bool4;
    }

    @Override // com.android.systemui.log.LogMessage
    public final double getDouble1() {
        return this.double1;
    }

    @Override // com.android.systemui.log.LogMessage
    public final int getInt1() {
        return this.int1;
    }

    @Override // com.android.systemui.log.LogMessage
    public final int getInt2() {
        return this.int2;
    }

    @Override // com.android.systemui.log.LogMessage
    public final long getLong1() {
        return this.long1;
    }

    @Override // com.android.systemui.log.LogMessage
    public final long getLong2() {
        return this.long2;
    }

    @Override // com.android.systemui.log.LogMessage
    public final String getStr1() {
        return this.str1;
    }

    @Override // com.android.systemui.log.LogMessage
    public final String getStr2() {
        return this.str2;
    }

    @Override // com.android.systemui.log.LogMessage
    public final String getStr3() {
        return this.str3;
    }
}
