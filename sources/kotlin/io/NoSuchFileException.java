package kotlin.io;

import java.io.File;
/* compiled from: Exceptions.kt */
/* loaded from: classes.dex */
public final class NoSuchFileException extends FileSystemException {
    public NoSuchFileException(File file) {
        super(file, null, "The source file doesn't exist.");
    }
}
