package com.android.systemui.statusbar.commandline;

import android.content.Context;
import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import kotlin.Unit;
import kotlin.collections.ArrayAsCollection;
import kotlin.collections.EmptyList;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandRegistry {
    public final LinkedHashMap commandMap = new LinkedHashMap();
    public final Context context;
    public boolean initialized;
    public final Executor mainExecutor;

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0, Executor executor) {
        if (this.commandMap.get(str) == null) {
            this.commandMap.put(str, new CommandWrapper(function0, executor));
        } else {
            throw new IllegalStateException("A command is already registered for (" + str + ')');
        }
    }

    public final void help(PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar <command>");
        printWriter.println("  known commands:");
        for (String str : this.commandMap.keySet()) {
            printWriter.println(Intrinsics.stringPlus("   ", str));
        }
    }

    public final void onShellCommand(final PrintWriter printWriter, final String[] strArr) {
        boolean z = true;
        if (!this.initialized) {
            this.initialized = true;
            registerCommand("prefs", new CommandRegistry$initializeCommands$1(this));
        }
        if (strArr.length != 0) {
            z = false;
        }
        if (z) {
            help(printWriter);
            return;
        }
        CommandWrapper commandWrapper = (CommandWrapper) this.commandMap.get(strArr[0]);
        if (commandWrapper == null) {
            help(printWriter);
            return;
        }
        final Command invoke = commandWrapper.commandFactory.invoke();
        final FutureTask futureTask = new FutureTask(new Callable() { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$task$1
            @Override // java.util.concurrent.Callable
            public final Object call() {
                boolean z2;
                List<String> list;
                Command command = Command.this;
                PrintWriter printWriter2 = printWriter;
                String[] strArr2 = strArr;
                int length = strArr2.length - 1;
                if (length < 0) {
                    length = 0;
                }
                if (length >= 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    if (length == 0) {
                        list = EmptyList.INSTANCE;
                    } else {
                        int length2 = strArr2.length;
                        if (length >= length2) {
                            int length3 = strArr2.length;
                            if (length3 == 0) {
                                list = EmptyList.INSTANCE;
                            } else if (length3 != 1) {
                                list = new ArrayList(new ArrayAsCollection(strArr2, false));
                            } else {
                                list = Collections.singletonList(strArr2[0]);
                            }
                        } else if (length == 1) {
                            list = Collections.singletonList(strArr2[length2 - 1]);
                        } else {
                            ArrayList arrayList = new ArrayList(length);
                            for (int i = length2 - length; i < length2; i++) {
                                arrayList.add(strArr2[i]);
                            }
                            list = arrayList;
                        }
                    }
                    command.execute(printWriter2, list);
                    return Unit.INSTANCE;
                }
                throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested element count ", length, " is less than zero.").toString());
            }
        });
        commandWrapper.executor.execute(new Runnable() { // from class: com.android.systemui.statusbar.commandline.CommandRegistry$onShellCommand$1
            @Override // java.lang.Runnable
            public final void run() {
                futureTask.run();
            }
        });
        futureTask.get();
    }

    public CommandRegistry(Context context, Executor executor) {
        this.context = context;
        this.mainExecutor = executor;
    }

    public final synchronized void registerCommand(String str, Function0<? extends Command> function0) {
        registerCommand(str, function0, this.mainExecutor);
    }
}
