package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.DeviceConfig;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BluetoothUtils {
    public static ErrorListener sErrorListener;

    /* loaded from: classes.dex */
    public interface ErrorListener {
    }

    public static Pair<Drawable, String> getBtDrawableWithDescription(Context context, CachedBluetoothDevice cachedBluetoothDevice) {
        String str;
        byte[] metadata;
        Pair<Drawable, String> btClassDrawableWithDescription = getBtClassDrawableWithDescription(context, cachedBluetoothDevice);
        BluetoothDevice bluetoothDevice = cachedBluetoothDevice.mDevice;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131165411);
        Resources resources = context.getResources();
        if (isAdvancedDetailsHeader(bluetoothDevice)) {
            Uri uri = null;
            if (bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(5)) == null) {
                str = null;
            } else {
                str = new String(metadata);
            }
            if (str != null) {
                uri = Uri.parse(str);
            }
            if (uri != null) {
                try {
                    context.getContentResolver().takePersistableUriPermission(uri, 1);
                } catch (SecurityException e) {
                    Log.e("BluetoothUtils", "Failed to take persistable permission for: " + uri, e);
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                    if (bitmap != null) {
                        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, dimensionPixelSize, dimensionPixelSize, false);
                        bitmap.recycle();
                        return new Pair<>(new BitmapDrawable(resources, createScaledBitmap), (String) btClassDrawableWithDescription.second);
                    }
                } catch (IOException e2) {
                    Log.e("BluetoothUtils", "Failed to get drawable for: " + uri, e2);
                } catch (SecurityException e3) {
                    Log.e("BluetoothUtils", "Failed to get permission for: " + uri, e3);
                }
            }
        }
        return new Pair<>((Drawable) btClassDrawableWithDescription.first, (String) btClassDrawableWithDescription.second);
    }

    public static Pair<Drawable, String> getBtClassDrawableWithDescription(Context context, CachedBluetoothDevice cachedBluetoothDevice) {
        int i;
        Objects.requireNonNull(cachedBluetoothDevice);
        BluetoothClass bluetoothClass = cachedBluetoothDevice.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            int majorDeviceClass = bluetoothClass.getMajorDeviceClass();
            if (majorDeviceClass == 256) {
                return new Pair<>(context.getDrawable(17302332), context.getString(2131952046));
            }
            if (majorDeviceClass == 512) {
                return new Pair<>(context.getDrawable(17302810), context.getString(2131952051));
            }
            if (majorDeviceClass == 1280) {
                int deviceClass = bluetoothClass.getDeviceClass();
                if (deviceClass != 1344) {
                    if (deviceClass == 1408) {
                        i = 17302335;
                    } else if (deviceClass != 1472) {
                        i = 17302333;
                    }
                    return new Pair<>(context.getDrawable(i), context.getString(2131952050));
                }
                i = 17302522;
                return new Pair<>(context.getDrawable(i), context.getString(2131952050));
            } else if (majorDeviceClass == 1536) {
                return new Pair<>(context.getDrawable(17302843), context.getString(2131952049));
            }
        }
        Iterator it = new ArrayList(cachedBluetoothDevice.mProfiles).iterator();
        while (it.hasNext()) {
            int drawableResource = ((LocalBluetoothProfile) it.next()).getDrawableResource(bluetoothClass);
            if (drawableResource != 0) {
                return new Pair<>(context.getDrawable(drawableResource), null);
            }
        }
        if (bluetoothClass != null) {
            if (bluetoothClass.doesClassMatch(0)) {
                return new Pair<>(context.getDrawable(17302330), context.getString(2131952048));
            }
            if (bluetoothClass.doesClassMatch(1)) {
                return new Pair<>(context.getDrawable(17302329), context.getString(2131952047));
            }
        }
        return new Pair<>(context.getDrawable(17302841).mutate(), context.getString(2131952045));
    }

    public static boolean isAdvancedDetailsHeader(BluetoothDevice bluetoothDevice) {
        boolean z;
        byte[] metadata;
        byte[] metadata2;
        if (!DeviceConfig.getBoolean("settings_ui", "bt_advanced_header_enabled", true)) {
            Log.d("BluetoothUtils", "isAdvancedDetailsHeader: advancedEnabled is false");
            return false;
        }
        if (bluetoothDevice == null || (metadata2 = bluetoothDevice.getMetadata(6)) == null) {
            z = false;
        } else {
            z = Boolean.parseBoolean(new String(metadata2));
        }
        if (z) {
            Log.d("BluetoothUtils", "isAdvancedDetailsHeader: untetheredHeadset is true");
            return true;
        }
        String str = null;
        if (!(bluetoothDevice == null || (metadata = bluetoothDevice.getMetadata(17)) == null)) {
            str = new String(metadata);
        }
        if (!TextUtils.equals(str, "Untethered Headset") && !TextUtils.equals(str, "Watch") && !TextUtils.equals(str, "Default")) {
            return false;
        }
        DialogFragment$$ExternalSyntheticOutline0.m("isAdvancedDetailsHeader: deviceType is ", str, "BluetoothUtils");
        return true;
    }
}
