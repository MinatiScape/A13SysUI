package com.android.systemui.media.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.recyclerview.R$dimen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda1;
import com.android.settingslib.Utils;
import com.android.settingslib.media.InfoMediaDevice;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaOutputAdapter extends MediaOutputBaseAdapter {
    public static final boolean DEBUG = Log.isLoggable("MediaOutputAdapter", 3);

    /* loaded from: classes.dex */
    public class MediaDeviceViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        public static final /* synthetic */ int $r8$clinit = 0;

        public static void setCheckBoxColor(CheckBox checkBox, int i) {
            checkBox.setButtonTintList(new ColorStateList(new int[][]{new int[]{16842912}, new int[0]}, new int[]{i, i}));
        }

        /* JADX WARN: Removed duplicated region for block: B:39:0x0261  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0267  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x028c  */
        @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter.MediaDeviceBaseViewHolder
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onBind(final com.android.settingslib.media.MediaDevice r8, boolean r9, boolean r10, int r11) {
            /*
                Method dump skipped, instructions count: 939
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputAdapter.MediaDeviceViewHolder.onBind(com.android.settingslib.media.MediaDevice, boolean, boolean, int):void");
        }

        public MediaDeviceViewHolder(View view) {
            super(view);
        }

        public final void onCheckBoxClicked(boolean z, MediaDevice mediaDevice) {
            if (z && isDeviceIncluded(MediaOutputAdapter.this.mController.getSelectableMediaDevice(), mediaDevice)) {
                MediaOutputController mediaOutputController = MediaOutputAdapter.this.mController;
                Objects.requireNonNull(mediaOutputController);
                LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
                Objects.requireNonNull(localMediaManager);
                InfoMediaManager infoMediaManager = localMediaManager.mInfoMediaManager;
                Objects.requireNonNull(infoMediaManager);
                if (TextUtils.isEmpty(infoMediaManager.mPackageName)) {
                    Log.w("InfoMediaManager", "addDeviceToPlayMedia() package name is null or empty!");
                    return;
                }
                RoutingSessionInfo routingSessionInfo = infoMediaManager.getRoutingSessionInfo();
                if (routingSessionInfo == null || !routingSessionInfo.getSelectableRoutes().contains(mediaDevice.mRouteInfo.getId())) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("addDeviceToPlayMedia() Ignoring selecting a non-selectable device : ");
                    m.append(mediaDevice.getName());
                    Log.w("InfoMediaManager", m.toString());
                    return;
                }
                infoMediaManager.mRouterManager.selectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
            } else if (!z) {
                MediaOutputController mediaOutputController2 = MediaOutputAdapter.this.mController;
                Objects.requireNonNull(mediaOutputController2);
                LocalMediaManager localMediaManager2 = mediaOutputController2.mLocalMediaManager;
                Objects.requireNonNull(localMediaManager2);
                InfoMediaManager infoMediaManager2 = localMediaManager2.mInfoMediaManager;
                Objects.requireNonNull(infoMediaManager2);
                ArrayList arrayList = new ArrayList();
                if (TextUtils.isEmpty(infoMediaManager2.mPackageName)) {
                    Log.d("InfoMediaManager", "getDeselectableMediaDevice() package name is null or empty!");
                } else {
                    RoutingSessionInfo routingSessionInfo2 = infoMediaManager2.getRoutingSessionInfo();
                    if (routingSessionInfo2 != null) {
                        for (MediaRoute2Info mediaRoute2Info : infoMediaManager2.mRouterManager.getDeselectableRoutes(routingSessionInfo2)) {
                            arrayList.add(new InfoMediaDevice(infoMediaManager2.mContext, infoMediaManager2.mRouterManager, mediaRoute2Info, infoMediaManager2.mPackageName));
                            StringBuilder sb = new StringBuilder();
                            sb.append((Object) mediaRoute2Info.getName());
                            sb.append(" is deselectable for ");
                            ExifInterface$$ExternalSyntheticOutline2.m(sb, infoMediaManager2.mPackageName, "InfoMediaManager");
                        }
                    } else {
                        ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("getDeselectableMediaDevice() cannot found deselectable MediaDevice from : "), infoMediaManager2.mPackageName, "InfoMediaManager");
                    }
                }
                if (isDeviceIncluded(arrayList, mediaDevice)) {
                    MediaOutputController mediaOutputController3 = MediaOutputAdapter.this.mController;
                    Objects.requireNonNull(mediaOutputController3);
                    LocalMediaManager localMediaManager3 = mediaOutputController3.mLocalMediaManager;
                    Objects.requireNonNull(localMediaManager3);
                    InfoMediaManager infoMediaManager3 = localMediaManager3.mInfoMediaManager;
                    Objects.requireNonNull(infoMediaManager3);
                    if (TextUtils.isEmpty(infoMediaManager3.mPackageName)) {
                        Log.w("InfoMediaManager", "removeDeviceFromMedia() package name is null or empty!");
                        return;
                    }
                    RoutingSessionInfo routingSessionInfo3 = infoMediaManager3.getRoutingSessionInfo();
                    if (routingSessionInfo3 == null || !routingSessionInfo3.getSelectedRoutes().contains(mediaDevice.mRouteInfo.getId())) {
                        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("removeDeviceFromMedia() Ignoring deselecting a non-deselectable device : ");
                        m2.append(mediaDevice.getName());
                        Log.w("InfoMediaManager", m2.toString());
                        return;
                    }
                    infoMediaManager3.mRouterManager.deselectRoute(routingSessionInfo3, mediaDevice.mRouteInfo);
                }
            }
        }

        public final void onItemClick(MediaDevice mediaDevice) {
            if (!MediaOutputAdapter.this.mController.isTransferring()) {
                MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                Objects.requireNonNull(mediaOutputAdapter);
                String id = mediaDevice.getId();
                MediaOutputController mediaOutputController = mediaOutputAdapter.mController;
                Objects.requireNonNull(mediaOutputController);
                LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
                Objects.requireNonNull(localMediaManager);
                if (TextUtils.equals(id, localMediaManager.mCurrentConnectedDevice.getId())) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("This device is already connected! : ");
                    m.append(mediaDevice.getName());
                    Log.d("MediaOutputAdapter", m.toString());
                    return;
                }
                MediaOutputAdapter mediaOutputAdapter2 = MediaOutputAdapter.this;
                mediaOutputAdapter2.mCurrentActivePosition = -1;
                MediaOutputController mediaOutputController2 = mediaOutputAdapter2.mController;
                Objects.requireNonNull(mediaOutputController2);
                MediaOutputMetricLogger mediaOutputMetricLogger = mediaOutputController2.mMetricLogger;
                LocalMediaManager localMediaManager2 = mediaOutputController2.mLocalMediaManager;
                Objects.requireNonNull(localMediaManager2);
                MediaDevice mediaDevice2 = localMediaManager2.mCurrentConnectedDevice;
                Objects.requireNonNull(mediaOutputMetricLogger);
                mediaOutputMetricLogger.mSourceDevice = mediaDevice2;
                mediaOutputMetricLogger.mTargetDevice = mediaDevice;
                if (MediaOutputMetricLogger.DEBUG) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("updateOutputEndPoints - source:");
                    m2.append(mediaOutputMetricLogger.mSourceDevice.toString());
                    m2.append(" target:");
                    m2.append(mediaOutputMetricLogger.mTargetDevice.toString());
                    Log.d("MediaOutputMetricLogger", m2.toString());
                }
                R$dimen.postOnBackgroundThread(new ClockManager$$ExternalSyntheticLambda1(mediaOutputController2, mediaDevice, 1));
                mediaDevice.mState = 1;
                Objects.requireNonNull(MediaOutputAdapter.this);
                MediaOutputAdapter.this.notifyDataSetChanged();
            }
        }

        public static boolean isDeviceIncluded(ArrayList arrayList, MediaDevice mediaDevice) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (TextUtils.equals(((MediaDevice) it.next()).getId(), mediaDevice.getId())) {
                    return true;
                }
            }
            return false;
        }

        @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter.MediaDeviceBaseViewHolder
        public final void onBind() {
            TextView textView = this.mTitleText;
            MediaOutputController mediaOutputController = MediaOutputAdapter.this.mController;
            Objects.requireNonNull(mediaOutputController);
            textView.setTextColor(mediaOutputController.mColorInactiveItem);
            this.mCheckBox.setVisibility(8);
            setSingleLineLayout(MediaOutputAdapter.this.mContext.getText(2131952740), false, false, false);
            Drawable drawable = MediaOutputAdapter.this.mContext.getDrawable(2131231745);
            drawable.setColorFilter(new PorterDuffColorFilter(Utils.getColorAttrDefaultColor(MediaOutputAdapter.this.mContext, 16843829), PorterDuff.Mode.SRC_IN));
            this.mTitleIcon.setImageDrawable(drawable);
            LinearLayout linearLayout = this.mContainerLayout;
            MediaOutputController mediaOutputController2 = MediaOutputAdapter.this.mController;
            Objects.requireNonNull(mediaOutputController2);
            linearLayout.setOnClickListener(new InternetDialog$$ExternalSyntheticLambda4(mediaOutputController2, 1));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final int getItemCount() {
        if (this.mController.isZeroMode()) {
            MediaOutputController mediaOutputController = this.mController;
            Objects.requireNonNull(mediaOutputController);
            return mediaOutputController.mMediaDevices.size() + 1;
        }
        MediaOutputController mediaOutputController2 = this.mController;
        Objects.requireNonNull(mediaOutputController2);
        return mediaOutputController2.mMediaDevices.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        boolean z;
        MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder2 = mediaDeviceBaseViewHolder;
        MediaOutputController mediaOutputController = this.mController;
        Objects.requireNonNull(mediaOutputController);
        int size = mediaOutputController.mMediaDevices.size();
        if (i == size && this.mController.isZeroMode()) {
            mediaDeviceBaseViewHolder2.onBind();
        } else if (i < size) {
            MediaOutputController mediaOutputController2 = this.mController;
            Objects.requireNonNull(mediaOutputController2);
            MediaDevice mediaDevice = mediaOutputController2.mMediaDevices.get(i);
            boolean z2 = false;
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            if (i == size - 1) {
                z2 = true;
            }
            mediaDeviceBaseViewHolder2.onBind(mediaDevice, z, z2, i);
        } else if (DEBUG) {
            ExifInterface$$ExternalSyntheticOutline1.m("Incorrect position: ", i, "MediaOutputAdapter");
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
        Context context = recyclerView.getContext();
        this.mContext = context;
        context.getResources().getDimensionPixelSize(2131166345);
        this.mHolderView = LayoutInflater.from(this.mContext).inflate(2131624261, (ViewGroup) recyclerView, false);
        return new MediaDeviceViewHolder(this.mHolderView);
    }

    public MediaOutputAdapter(MediaOutputController mediaOutputController) {
        super(mediaOutputController);
    }
}
