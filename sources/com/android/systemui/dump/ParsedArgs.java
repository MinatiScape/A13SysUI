package com.android.systemui.dump;

import java.util.ArrayList;
import java.util.List;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class ParsedArgs {
    public String command;
    public String dumpPriority;
    public boolean listOnly;
    public final List<String> nonFlagArgs;
    public final String[] rawArgs;
    public int tailLength;

    public ParsedArgs(String[] strArr, ArrayList arrayList) {
        this.rawArgs = strArr;
        this.nonFlagArgs = arrayList;
    }
}
