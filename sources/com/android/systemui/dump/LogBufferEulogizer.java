package com.android.systemui.dump;

import android.content.Context;
import android.util.Log;
import com.android.systemui.util.io.Files;
import com.android.systemui.util.time.SystemClock;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Objects;
import kotlin.io.CloseableKt;
/* compiled from: LogBufferEulogizer.kt */
/* loaded from: classes.dex */
public final class LogBufferEulogizer {
    public final DumpManager dumpManager;
    public final Files files;
    public final Path logPath;
    public final long maxLogAgeToDump;
    public final long minWriteGap;
    public final SystemClock systemClock;

    public final long getMillisSinceLastWrite(Path path) {
        BasicFileAttributes basicFileAttributes;
        FileTime lastModifiedTime;
        try {
            Objects.requireNonNull(this.files);
            basicFileAttributes = java.nio.file.Files.readAttributes(path, BasicFileAttributes.class, new LinkOption[0]);
        } catch (IOException unused) {
            basicFileAttributes = null;
        }
        long currentTimeMillis = this.systemClock.currentTimeMillis();
        long j = 0;
        if (!(basicFileAttributes == null || (lastModifiedTime = basicFileAttributes.lastModifiedTime()) == null)) {
            j = lastModifiedTime.toMillis();
        }
        return currentTimeMillis - j;
    }

    public final Exception record(IllegalStateException illegalStateException) {
        long uptimeMillis = this.systemClock.uptimeMillis();
        Log.i("BufferEulogizer", "Performing emergency dump of log buffers");
        long millisSinceLastWrite = getMillisSinceLastWrite(this.logPath);
        if (millisSinceLastWrite < this.minWriteGap) {
            Log.w("BufferEulogizer", "Cannot dump logs, last write was only " + millisSinceLastWrite + " ms ago");
            return illegalStateException;
        }
        long j = 0;
        try {
            Files files = this.files;
            Path path = this.logPath;
            OpenOption[] openOptionArr = {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            Objects.requireNonNull(files);
            BufferedWriter newBufferedWriter = java.nio.file.Files.newBufferedWriter(path, StandardCharsets.UTF_8, openOptionArr);
            PrintWriter printWriter = new PrintWriter(newBufferedWriter);
            printWriter.println(LogBufferEulogizerKt.DATE_FORMAT.format(Long.valueOf(this.systemClock.currentTimeMillis())));
            printWriter.println();
            printWriter.println("Dump triggered by exception:");
            illegalStateException.printStackTrace(printWriter);
            this.dumpManager.dumpBuffers(printWriter, 0);
            j = this.systemClock.uptimeMillis() - uptimeMillis;
            printWriter.println();
            printWriter.println("Buffer eulogy took " + j + "ms");
            CloseableKt.closeFinally(newBufferedWriter, null);
        } catch (Exception e) {
            Log.e("BufferEulogizer", "Exception while attempting to dump buffers, bailing", e);
        }
        Log.i("BufferEulogizer", "Buffer eulogy took " + j + "ms");
        return illegalStateException;
    }

    public LogBufferEulogizer(Context context, DumpManager dumpManager, SystemClock systemClock, Files files) {
        Path path = Paths.get(context.getFilesDir().toPath().toString(), "log_buffers.txt");
        long j = LogBufferEulogizerKt.MIN_WRITE_GAP;
        long j2 = LogBufferEulogizerKt.MAX_AGE_TO_DUMP;
        this.dumpManager = dumpManager;
        this.systemClock = systemClock;
        this.files = files;
        this.logPath = path;
        this.minWriteGap = j;
        this.maxLogAgeToDump = j2;
    }
}
