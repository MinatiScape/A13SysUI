package com.android.systemui.util.leak;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.SystemProperties;
import androidx.core.content.FileProvider;
import com.google.android.collect.Lists;
import java.io.File;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class LeakReporter {
    public final Context mContext;
    public final LeakDetector mLeakDetector;
    public final String mLeakReportEmail;

    public final Intent getIntent(File file, File file2) {
        Uri uriForFile = FileProvider.getPathStrategy(this.mContext, "com.android.systemui.fileprovider").getUriForFile(file2);
        Uri uriForFile2 = FileProvider.getPathStrategy(this.mContext, "com.android.systemui.fileprovider").getUriForFile(file);
        Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
        intent.addFlags(1);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("application/vnd.android.leakreport");
        intent.putExtra("android.intent.extra.SUBJECT", "SystemUI leak report");
        intent.putExtra("android.intent.extra.TEXT", "Build info: " + SystemProperties.get("ro.build.description"));
        ClipData clipData = new ClipData(null, new String[]{"application/vnd.android.leakreport"}, new ClipData.Item(null, null, null, uriForFile));
        ArrayList<? extends Parcelable> newArrayList = Lists.newArrayList(new Uri[]{uriForFile});
        clipData.addItem(new ClipData.Item(null, null, null, uriForFile2));
        newArrayList.add(uriForFile2);
        intent.setClipData(clipData);
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", newArrayList);
        String str = this.mLeakReportEmail;
        if (str != null) {
            intent.putExtra("android.intent.extra.EMAIL", new String[]{str});
        }
        return intent;
    }

    public LeakReporter(Context context, LeakDetector leakDetector, String str) {
        this.mContext = context;
        this.mLeakDetector = leakDetector;
        this.mLeakReportEmail = str;
    }
}
