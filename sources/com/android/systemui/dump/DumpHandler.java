package com.android.systemui.dump;

import android.content.Context;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import androidx.cardview.R$attr;
import com.android.systemui.CoreStartable;
import com.android.systemui.util.io.Files;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.inject.Provider;
import kotlin.collections.ArrayAsCollection;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler {
    public final Context context;
    public final DumpManager dumpManager;
    public final LogBufferEulogizer logBufferEulogizer;
    public final Map<Class<?>, Provider<CoreStartable>> startables;

    public static ParsedArgs parseArgs(String[] strArr) {
        ArrayList arrayList = new ArrayList(new ArrayAsCollection(strArr, false));
        ParsedArgs parsedArgs = new ParsedArgs(strArr, arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str.startsWith("-")) {
                it.remove();
                switch (str.hashCode()) {
                    case 1499:
                        if (!str.equals("-h")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.command = "help";
                        break;
                    case 1503:
                        if (!str.equals("-l")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.listOnly = true;
                        break;
                    case 1511:
                        if (!str.equals("-t")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.tailLength = ((Number) readArgument(it, str, DumpHandler$parseArgs$2.INSTANCE)).intValue();
                        break;
                    case 1056887741:
                        if (str.equals("--dump-priority")) {
                            parsedArgs.dumpPriority = (String) readArgument(it, "--dump-priority", DumpHandler$parseArgs$1.INSTANCE);
                            break;
                        } else {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                    case 1333069025:
                        if (!str.equals("--help")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.command = "help";
                        break;
                    case 1333192254:
                        if (!str.equals("--list")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.listOnly = true;
                        break;
                    case 1333422576:
                        if (!str.equals("--tail")) {
                            throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                        }
                        parsedArgs.tailLength = ((Number) readArgument(it, str, DumpHandler$parseArgs$2.INSTANCE)).intValue();
                        break;
                    default:
                        throw new ArgParseException(Intrinsics.stringPlus("Unknown flag: ", str));
                }
            }
        }
        if (parsedArgs.command == null && (!arrayList.isEmpty()) && ArraysKt___ArraysKt.contains(DumpHandlerKt.COMMANDS, arrayList.get(0))) {
            parsedArgs.command = (String) arrayList.remove(0);
        }
        return parsedArgs;
    }

    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Trace.beginSection("DumpManager#dump()");
        long uptimeMillis = SystemClock.uptimeMillis();
        try {
            ParsedArgs parseArgs = parseArgs(strArr);
            String str = parseArgs.dumpPriority;
            if (Intrinsics.areEqual(str, "CRITICAL")) {
                this.dumpManager.dumpDumpables(fileDescriptor, printWriter, parseArgs.rawArgs);
                dumpConfig(printWriter);
            } else if (Intrinsics.areEqual(str, "NORMAL")) {
                dumpNormal(printWriter, parseArgs);
            } else {
                String str2 = parseArgs.command;
                if (str2 != null) {
                    switch (str2.hashCode()) {
                        case -1354792126:
                            if (str2.equals("config")) {
                                dumpConfig(printWriter);
                                break;
                            }
                            break;
                        case -1353714459:
                            if (str2.equals("dumpables")) {
                                if (parseArgs.listOnly) {
                                    this.dumpManager.listDumpables(printWriter);
                                    break;
                                } else {
                                    this.dumpManager.dumpDumpables(fileDescriptor, printWriter, parseArgs.rawArgs);
                                    break;
                                }
                            }
                            break;
                        case -1045369428:
                            if (str2.equals("bugreport-normal")) {
                                dumpNormal(printWriter, parseArgs);
                                break;
                            }
                            break;
                        case 3198785:
                            if (str2.equals("help")) {
                                printWriter.println("Let <invocation> be:");
                                printWriter.println("$ adb shell dumpsys activity service com.android.systemui/.SystemUIService");
                                printWriter.println();
                                printWriter.println("Most common usage:");
                                printWriter.println("$ <invocation> <targets>");
                                printWriter.println("$ <invocation> NotifLog");
                                printWriter.println("$ <invocation> StatusBar FalsingManager BootCompleteCacheImpl");
                                printWriter.println("etc.");
                                printWriter.println();
                                printWriter.println("Special commands:");
                                printWriter.println("$ <invocation> dumpables");
                                printWriter.println("$ <invocation> buffers");
                                printWriter.println("$ <invocation> bugreport-critical");
                                printWriter.println("$ <invocation> bugreport-normal");
                                printWriter.println();
                                printWriter.println("Targets can be listed:");
                                printWriter.println("$ <invocation> --list");
                                printWriter.println("$ <invocation> dumpables --list");
                                printWriter.println("$ <invocation> buffers --list");
                                printWriter.println();
                                printWriter.println("Show only the most recent N lines of buffers");
                                printWriter.println("$ <invocation> NotifLog --tail 30");
                                break;
                            }
                            break;
                        case 227996723:
                            if (str2.equals("buffers")) {
                                if (parseArgs.listOnly) {
                                    this.dumpManager.listBuffers(printWriter);
                                    break;
                                } else {
                                    this.dumpManager.dumpBuffers(printWriter, parseArgs.tailLength);
                                    break;
                                }
                            }
                            break;
                        case 842828580:
                            if (str2.equals("bugreport-critical")) {
                                this.dumpManager.dumpDumpables(fileDescriptor, printWriter, parseArgs.rawArgs);
                                dumpConfig(printWriter);
                                break;
                            }
                            break;
                    }
                }
                List<String> list = parseArgs.nonFlagArgs;
                if (!list.isEmpty()) {
                    for (String str3 : list) {
                        DumpManager dumpManager = this.dumpManager;
                        String[] strArr2 = parseArgs.rawArgs;
                        int i = parseArgs.tailLength;
                        Objects.requireNonNull(dumpManager);
                        synchronized (dumpManager) {
                            Iterator it = dumpManager.dumpables.values().iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    RegisteredDumpable registeredDumpable = (RegisteredDumpable) it.next();
                                    Objects.requireNonNull(registeredDumpable);
                                    if (registeredDumpable.name.endsWith(str3)) {
                                        DumpManager.dumpDumpable(registeredDumpable, fileDescriptor, printWriter, strArr2);
                                    }
                                } else {
                                    Iterator it2 = dumpManager.buffers.values().iterator();
                                    while (true) {
                                        if (it2.hasNext()) {
                                            RegisteredDumpable registeredDumpable2 = (RegisteredDumpable) it2.next();
                                            Objects.requireNonNull(registeredDumpable2);
                                            if (registeredDumpable2.name.endsWith(str3)) {
                                                DumpManager.dumpBuffer(registeredDumpable2, printWriter, i);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (parseArgs.listOnly) {
                    printWriter.println("Dumpables:");
                    this.dumpManager.listDumpables(printWriter);
                    printWriter.println();
                    printWriter.println("Buffers:");
                    this.dumpManager.listBuffers(printWriter);
                } else {
                    printWriter.println("Nothing to dump :(");
                }
            }
            printWriter.println();
            printWriter.println("Dump took " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms");
            Trace.endSection();
        } catch (ArgParseException e) {
            printWriter.println(e.getMessage());
        }
    }

    public final void dumpConfig(PrintWriter printWriter) {
        printWriter.println("SystemUiServiceComponents configuration:");
        printWriter.print("vendor component: ");
        printWriter.println(this.context.getResources().getString(2131952148));
        Set<Class<?>> keySet = this.startables.keySet();
        ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(keySet, 10));
        Iterator<T> it = keySet.iterator();
        while (it.hasNext()) {
            arrayList.add(((Class) it.next()).getSimpleName());
        }
        ArrayList arrayList2 = new ArrayList(arrayList);
        arrayList2.add(this.context.getResources().getString(2131952148));
        Object[] array = arrayList2.toArray(new String[0]);
        Objects.requireNonNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        dumpServiceList(printWriter, "global", (String[]) array);
        dumpServiceList(printWriter, "per-user", this.context.getResources().getStringArray(2130903104));
    }

    public final void dumpNormal(final PrintWriter printWriter, ParsedArgs parsedArgs) {
        this.dumpManager.dumpBuffers(printWriter, parsedArgs.tailLength);
        LogBufferEulogizer logBufferEulogizer = this.logBufferEulogizer;
        Objects.requireNonNull(logBufferEulogizer);
        try {
            long millisSinceLastWrite = logBufferEulogizer.getMillisSinceLastWrite(logBufferEulogizer.logPath);
            if (millisSinceLastWrite > logBufferEulogizer.maxLogAgeToDump) {
                Log.i("BufferEulogizer", "Not eulogizing buffers; they are " + TimeUnit.HOURS.convert(millisSinceLastWrite, TimeUnit.MILLISECONDS) + " hours old");
            } else {
                Files files = logBufferEulogizer.files;
                Path path = logBufferEulogizer.logPath;
                Objects.requireNonNull(files);
                Stream<String> lines = java.nio.file.Files.lines(path);
                printWriter.println();
                printWriter.println();
                printWriter.println("=============== BUFFERS FROM MOST RECENT CRASH ===============");
                lines.forEach(new Consumer() { // from class: com.android.systemui.dump.LogBufferEulogizer$readEulogyIfPresent$1$1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        printWriter.println((String) obj);
                    }
                });
                R$attr.closeFinally(lines, null);
            }
        } catch (IOException unused) {
        } catch (UncheckedIOException e) {
            Log.e("BufferEulogizer", "UncheckedIOException while dumping the core", e);
        }
    }

    public DumpHandler(Context context, DumpManager dumpManager, LogBufferEulogizer logBufferEulogizer, Map<Class<?>, Provider<CoreStartable>> map) {
        this.context = context;
        this.dumpManager = dumpManager;
        this.logBufferEulogizer = logBufferEulogizer;
        this.startables = map;
    }

    public static void dumpServiceList(PrintWriter printWriter, String str, String[] strArr) {
        printWriter.print(str);
        printWriter.print(": ");
        if (strArr == null) {
            printWriter.println("N/A");
            return;
        }
        printWriter.print(strArr.length);
        printWriter.println(" services");
        int length = strArr.length;
        for (int i = 0; i < length; i++) {
            printWriter.print("  ");
            printWriter.print(i);
            printWriter.print(": ");
            printWriter.println(strArr[i]);
        }
    }

    public static Object readArgument(Iterator it, String str, Function1 function1) {
        if (it.hasNext()) {
            String str2 = (String) it.next();
            try {
                Object invoke = function1.invoke(str2);
                it.remove();
                return invoke;
            } catch (Exception unused) {
                throw new ArgParseException("Invalid argument '" + str2 + "' for flag " + str);
            }
        } else {
            throw new ArgParseException(Intrinsics.stringPlus("Missing argument for ", str));
        }
    }
}
