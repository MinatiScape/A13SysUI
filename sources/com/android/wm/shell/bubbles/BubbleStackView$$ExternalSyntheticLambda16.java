package com.android.wm.shell.bubbles;

import android.bluetooth.BluetoothDevice;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.display.NightDisplayListener;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import androidx.recyclerview.R$dimen;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import com.android.systemui.statusbar.policy.DateView;
import com.google.android.systemui.smartspace.IcuDateTextView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda16 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda16(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        byte[] metadata;
        Uri uri = null;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                if (bubbleStackView.getBubbleCount() == 0) {
                    BubbleController bubbleController = bubbleStackView.mBubbleController;
                    Objects.requireNonNull(bubbleController);
                    BubbleStackView bubbleStackView2 = bubbleController.mStackView;
                    if (bubbleStackView2 != null) {
                        bubbleStackView2.setVisibility(4);
                        if (bubbleController.mAddedToWindowManager) {
                            try {
                                bubbleController.mAddedToWindowManager = false;
                                BubbleStackView bubbleStackView3 = bubbleController.mStackView;
                                if (bubbleStackView3 != null) {
                                    bubbleController.mWindowManager.removeView(bubbleStackView3);
                                    BubbleData bubbleData = bubbleController.mBubbleData;
                                    Objects.requireNonNull(bubbleData);
                                    BubbleOverflow bubbleOverflow = bubbleData.mOverflow;
                                    Objects.requireNonNull(bubbleOverflow);
                                    BubbleExpandedView bubbleExpandedView = bubbleOverflow.expandedView;
                                    if (bubbleExpandedView != null) {
                                        bubbleExpandedView.cleanUpExpandedState();
                                    }
                                    bubbleOverflow.expandedView = null;
                                    return;
                                }
                                Log.w("Bubbles", "StackView added to WindowManager, but was null when removing!");
                                return;
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case 1:
                CachedBluetoothDevice cachedBluetoothDevice = (CachedBluetoothDevice) this.f$0;
                Objects.requireNonNull(cachedBluetoothDevice);
                if (BluetoothUtils.isAdvancedDetailsHeader(cachedBluetoothDevice.mDevice)) {
                    BluetoothDevice bluetoothDevice = cachedBluetoothDevice.mDevice;
                    if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(5)) == null) {
                        str = null;
                    } else {
                        str = new String(metadata);
                    }
                    if (str != null) {
                        uri = Uri.parse(str);
                    }
                    if (uri != null && cachedBluetoothDevice.mDrawableCache.get(uri.toString()) == null) {
                        cachedBluetoothDevice.mDrawableCache.put(uri.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(cachedBluetoothDevice.mContext, cachedBluetoothDevice).first);
                    }
                }
                R$dimen.postOnMainThread(new StatusBar$$ExternalSyntheticLambda18(cachedBluetoothDevice, 1));
                return;
            case 2:
                AutoTileManager.AnonymousClass5 r6 = (AutoTileManager.AnonymousClass5) this.f$0;
                Objects.requireNonNull(r6);
                AutoTileManager.this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
                return;
            case 3:
                StatusBar.AnonymousClass8 r62 = (StatusBar.AnonymousClass8) this.f$0;
                int i = StatusBar.AnonymousClass8.$r8$clinit;
                Objects.requireNonNull(r62);
                StatusBar.this.mBubblesOptional.get().collapseStack();
                return;
            case 4:
                DateView.AnonymousClass1 r63 = (DateView.AnonymousClass1) this.f$0;
                int i2 = DateView.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r63);
                DateView.this.mDateFormat = null;
                return;
            default:
                IcuDateTextView icuDateTextView = (IcuDateTextView) this.f$0;
                int i3 = IcuDateTextView.$r8$clinit;
                Objects.requireNonNull(icuDateTextView);
                icuDateTextView.onTimeChanged(false);
                if (icuDateTextView.mHandler != null) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    icuDateTextView.mHandler.postAtTime(icuDateTextView.mTicker, (1000 - (uptimeMillis % 1000)) + uptimeMillis);
                    return;
                }
                return;
        }
    }
}
