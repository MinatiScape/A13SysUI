package com.android.systemui.backup;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import com.android.systemui.controls.controller.AuxiliaryPersistenceWrapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import kotlin.Unit;
import kotlin.io.FileAlreadyExistsException;
import kotlin.io.FileSystemException;
import kotlin.io.NoSuchFileException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: BackupHelper.kt */
/* loaded from: classes.dex */
final class BackupHelperKt$getPPControlsFile$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Context $context;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BackupHelperKt$getPPControlsFile$1(Context context) {
        super(0);
        this.$context = context;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        File filesDir = this.$context.getFilesDir();
        File buildPath = Environment.buildPath(filesDir, new String[]{"controls_favorites.xml"});
        if (buildPath.exists()) {
            File buildPath2 = Environment.buildPath(filesDir, new String[]{"aux_controls_favorites.xml"});
            if (!buildPath.exists()) {
                throw new NoSuchFileException(buildPath);
            } else if (!buildPath2.exists()) {
                if (!buildPath.isDirectory()) {
                    File parentFile = buildPath2.getParentFile();
                    if (parentFile != null) {
                        parentFile.mkdirs();
                    }
                    FileOutputStream fileInputStream = new FileInputStream(buildPath);
                    try {
                        fileInputStream = new FileOutputStream(buildPath2);
                        byte[] bArr = new byte[8192];
                        for (int read = fileInputStream.read(bArr); read >= 0; read = fileInputStream.read(bArr)) {
                            fileInputStream.write(bArr, 0, read);
                        }
                        th = null;
                    } finally {
                        try {
                            throw th;
                        } finally {
                        }
                    }
                } else if (!buildPath2.mkdirs()) {
                    throw new FileSystemException(buildPath, buildPath2, "Failed to create target directory.");
                }
                JobScheduler jobScheduler = (JobScheduler) this.$context.getSystemService(JobScheduler.class);
                if (jobScheduler != null) {
                    int i = AuxiliaryPersistenceWrapper.DeletionJobService.$r8$clinit;
                    Context context = this.$context;
                    jobScheduler.schedule(new JobInfo.Builder(context.getUserId() + 1000, new ComponentName(context, AuxiliaryPersistenceWrapper.DeletionJobService.class)).setMinimumLatency(AuxiliaryPersistenceWrapper.DeletionJobService.WEEK_IN_MILLIS).setPersisted(true).build());
                }
            } else {
                throw new FileAlreadyExistsException(buildPath, buildPath2, "The destination file already exists.");
            }
        }
        return Unit.INSTANCE;
    }
}
