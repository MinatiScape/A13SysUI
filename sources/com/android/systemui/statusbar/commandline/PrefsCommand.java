package com.android.systemui.statusbar.commandline;

import com.android.systemui.Prefs;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class PrefsCommand implements Command {
    @Override // com.android.systemui.statusbar.commandline.Command
    public final void execute(PrintWriter printWriter, List<String> list) {
        if (list.isEmpty()) {
            printWriter.println("usage: prefs <command> [args]");
            printWriter.println("Available commands:");
            printWriter.println("  list-prefs");
            printWriter.println("  set-pref <pref name> <value>");
            return;
        }
        int i = 0;
        if (Intrinsics.areEqual(list.get(0), "list-prefs")) {
            printWriter.println("Available keys:");
            Field[] declaredFields = Prefs.Key.class.getDeclaredFields();
            int length = declaredFields.length;
            while (i < length) {
                Field field = declaredFields[i];
                i++;
                printWriter.print("  ");
                printWriter.println(field.get(Prefs.Key.class));
            }
            return;
        }
        printWriter.println("usage: prefs <command> [args]");
        printWriter.println("Available commands:");
        printWriter.println("  list-prefs");
        printWriter.println("  set-pref <pref name> <value>");
    }
}
