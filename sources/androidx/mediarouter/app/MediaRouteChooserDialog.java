package androidx.mediarouter.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaRouteChooserDialog extends AppCompatDialog {
    public RouteAdapter mAdapter;
    public boolean mAttachedToWindow;
    public long mLastUpdateTime;
    public ListView mListView;
    public ArrayList<MediaRouter.RouteInfo> mRoutes;
    public TextView mTitleView;
    public MediaRouteSelector mSelector = MediaRouteSelector.EMPTY;
    public final AnonymousClass1 mHandler = new Handler() { // from class: androidx.mediarouter.app.MediaRouteChooserDialog.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 1) {
                MediaRouteChooserDialog mediaRouteChooserDialog = MediaRouteChooserDialog.this;
                Objects.requireNonNull(mediaRouteChooserDialog);
                mediaRouteChooserDialog.mLastUpdateTime = SystemClock.uptimeMillis();
                mediaRouteChooserDialog.mRoutes.clear();
                mediaRouteChooserDialog.mRoutes.addAll((List) message.obj);
                mediaRouteChooserDialog.mAdapter.notifyDataSetChanged();
            }
        }
    };
    public final MediaRouter mRouter = MediaRouter.getInstance(getContext());
    public final MediaRouterCallback mCallback = new MediaRouterCallback();

    /* loaded from: classes.dex */
    public final class MediaRouterCallback extends MediaRouter.Callback {
        public MediaRouterCallback() {
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteAdded() {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteChanged(MediaRouter.RouteInfo routeInfo) {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteRemoved() {
            MediaRouteChooserDialog.this.refreshRoutes();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public final void onRouteSelected(MediaRouter.RouteInfo routeInfo) {
            MediaRouteChooserDialog.this.dismiss();
        }
    }

    /* loaded from: classes.dex */
    public static final class RouteComparator implements Comparator<MediaRouter.RouteInfo> {
        public static final RouteComparator sInstance = new RouteComparator();

        @Override // java.util.Comparator
        public final int compare(MediaRouter.RouteInfo routeInfo, MediaRouter.RouteInfo routeInfo2) {
            MediaRouter.RouteInfo routeInfo3 = routeInfo;
            MediaRouter.RouteInfo routeInfo4 = routeInfo2;
            Objects.requireNonNull(routeInfo3);
            String str = routeInfo3.mName;
            Objects.requireNonNull(routeInfo4);
            return str.compareToIgnoreCase(routeInfo4.mName);
        }
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Type inference failed for: r2v3, types: [androidx.mediarouter.app.MediaRouteChooserDialog$1] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaRouteChooserDialog(android.content.Context r2) {
        /*
            r1 = this;
            r0 = 0
            android.view.ContextThemeWrapper r2 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogContext(r2, r0)
            int r0 = androidx.mediarouter.app.MediaRouterThemeHelper.createThemedDialogStyle(r2)
            r1.<init>(r2, r0)
            androidx.mediarouter.media.MediaRouteSelector r2 = androidx.mediarouter.media.MediaRouteSelector.EMPTY
            r1.mSelector = r2
            androidx.mediarouter.app.MediaRouteChooserDialog$1 r2 = new androidx.mediarouter.app.MediaRouteChooserDialog$1
            r2.<init>()
            r1.mHandler = r2
            android.content.Context r2 = r1.getContext()
            androidx.mediarouter.media.MediaRouter r2 = androidx.mediarouter.media.MediaRouter.getInstance(r2)
            r1.mRouter = r2
            androidx.mediarouter.app.MediaRouteChooserDialog$MediaRouterCallback r2 = new androidx.mediarouter.app.MediaRouteChooserDialog$MediaRouterCallback
            r2.<init>()
            r1.mCallback = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteChooserDialog.<init>(android.content.Context):void");
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onDetachedFromWindow() {
        this.mAttachedToWindow = false;
        this.mRouter.removeCallback(this.mCallback);
        removeMessages(1);
        super.onDetachedFromWindow();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void setTitle(CharSequence charSequence) {
        this.mTitleView.setText(charSequence);
    }

    /* loaded from: classes.dex */
    public static final class RouteAdapter extends ArrayAdapter<MediaRouter.RouteInfo> implements AdapterView.OnItemClickListener {
        public final Drawable mDefaultIcon;
        public final LayoutInflater mInflater;
        public final Drawable mSpeakerGroupIcon;
        public final Drawable mSpeakerIcon;
        public final Drawable mTvIcon;

        public RouteAdapter(Context context, ArrayList arrayList) {
            super(context, 0, arrayList);
            this.mInflater = LayoutInflater.from(context);
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{2130969461, 2130969470, 2130969467, 2130969466});
            this.mDefaultIcon = AppCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(0, 0));
            this.mTvIcon = AppCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(1, 0));
            this.mSpeakerIcon = AppCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(2, 0));
            this.mSpeakerGroupIcon = AppCompatResources.getDrawable(context, obtainStyledAttributes.getResourceId(3, 0));
            obtainStyledAttributes.recycle();
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public final boolean areAllItemsEnabled() {
            return false;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x0082, code lost:
            if (r0 != null) goto L_0x00b4;
         */
        @Override // android.widget.ArrayAdapter, android.widget.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final android.view.View getView(int r7, android.view.View r8, android.view.ViewGroup r9) {
            /*
                r6 = this;
                r0 = 0
                if (r8 != 0) goto L_0x000c
                android.view.LayoutInflater r8 = r6.mInflater
                r1 = 2131624278(0x7f0e0156, float:1.8875731E38)
                android.view.View r8 = r8.inflate(r1, r9, r0)
            L_0x000c:
                java.lang.Object r7 = r6.getItem(r7)
                androidx.mediarouter.media.MediaRouter$RouteInfo r7 = (androidx.mediarouter.media.MediaRouter.RouteInfo) r7
                r9 = 2131428420(0x7f0b0444, float:1.8478484E38)
                android.view.View r9 = r8.findViewById(r9)
                android.widget.TextView r9 = (android.widget.TextView) r9
                r1 = 2131428418(0x7f0b0442, float:1.847848E38)
                android.view.View r1 = r8.findViewById(r1)
                android.widget.TextView r1 = (android.widget.TextView) r1
                java.util.Objects.requireNonNull(r7)
                java.lang.String r2 = r7.mName
                r9.setText(r2)
                java.lang.String r2 = r7.mDescription
                int r3 = r7.mConnectionState
                r4 = 2
                r5 = 1
                if (r3 == r4) goto L_0x0039
                if (r3 != r5) goto L_0x0037
                goto L_0x0039
            L_0x0037:
                r3 = r0
                goto L_0x003a
            L_0x0039:
                r3 = r5
            L_0x003a:
                if (r3 == 0) goto L_0x004e
                boolean r3 = android.text.TextUtils.isEmpty(r2)
                if (r3 != 0) goto L_0x004e
                r3 = 80
                r9.setGravity(r3)
                r1.setVisibility(r0)
                r1.setText(r2)
                goto L_0x005d
            L_0x004e:
                r0 = 16
                r9.setGravity(r0)
                r9 = 8
                r1.setVisibility(r9)
                java.lang.String r9 = ""
                r1.setText(r9)
            L_0x005d:
                boolean r9 = r7.mEnabled
                r8.setEnabled(r9)
                r9 = 2131428419(0x7f0b0443, float:1.8478482E38)
                android.view.View r9 = r8.findViewById(r9)
                android.widget.ImageView r9 = (android.widget.ImageView) r9
                if (r9 == 0) goto L_0x00b7
                android.net.Uri r0 = r7.mIconUri
                if (r0 == 0) goto L_0x009c
                android.content.Context r1 = r6.getContext()     // Catch: IOException -> 0x0085
                android.content.ContentResolver r1 = r1.getContentResolver()     // Catch: IOException -> 0x0085
                java.io.InputStream r1 = r1.openInputStream(r0)     // Catch: IOException -> 0x0085
                r2 = 0
                android.graphics.drawable.Drawable r0 = android.graphics.drawable.Drawable.createFromStream(r1, r2)     // Catch: IOException -> 0x0085
                if (r0 == 0) goto L_0x009c
                goto L_0x00b4
            L_0x0085:
                r1 = move-exception
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Failed to load "
                r2.append(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                java.lang.String r2 = "MediaRouteChooserDialog"
                android.util.Log.w(r2, r0, r1)
            L_0x009c:
                int r0 = r7.mDeviceType
                if (r0 == r5) goto L_0x00b1
                if (r0 == r4) goto L_0x00ae
                boolean r7 = r7.isGroup()
                if (r7 == 0) goto L_0x00ab
                android.graphics.drawable.Drawable r6 = r6.mSpeakerGroupIcon
                goto L_0x00b3
            L_0x00ab:
                android.graphics.drawable.Drawable r6 = r6.mDefaultIcon
                goto L_0x00b3
            L_0x00ae:
                android.graphics.drawable.Drawable r6 = r6.mSpeakerIcon
                goto L_0x00b3
            L_0x00b1:
                android.graphics.drawable.Drawable r6 = r6.mTvIcon
            L_0x00b3:
                r0 = r6
            L_0x00b4:
                r9.setImageDrawable(r0)
            L_0x00b7:
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.mediarouter.app.MediaRouteChooserDialog.RouteAdapter.getView(int, android.view.View, android.view.ViewGroup):android.view.View");
        }

        @Override // android.widget.BaseAdapter, android.widget.ListAdapter
        public final boolean isEnabled(int i) {
            MediaRouter.RouteInfo item = getItem(i);
            Objects.requireNonNull(item);
            return item.mEnabled;
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public final void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            MediaRouter.RouteInfo item = getItem(i);
            Objects.requireNonNull(item);
            if (item.mEnabled) {
                ImageView imageView = (ImageView) view.findViewById(2131428419);
                ProgressBar progressBar = (ProgressBar) view.findViewById(2131428421);
                if (!(imageView == null || progressBar == null)) {
                    imageView.setVisibility(8);
                    progressBar.setVisibility(0);
                }
                item.select();
            }
        }
    }

    public final void refreshRoutes() {
        Collection collection;
        if (this.mAttachedToWindow) {
            Objects.requireNonNull(this.mRouter);
            MediaRouter.checkCallingThread();
            MediaRouter.GlobalMediaRouter globalRouter = MediaRouter.getGlobalRouter();
            if (globalRouter == null) {
                collection = Collections.emptyList();
            } else {
                collection = globalRouter.mRoutes;
            }
            ArrayList arrayList = new ArrayList(collection);
            int size = arrayList.size();
            while (true) {
                int i = size - 1;
                boolean z = true;
                if (size <= 0) {
                    break;
                }
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) arrayList.get(i);
                if (routeInfo.isDefaultOrBluetooth() || !routeInfo.mEnabled || !routeInfo.matchesSelector(this.mSelector)) {
                    z = false;
                }
                if (!z) {
                    arrayList.remove(i);
                }
                size = i;
            }
            Collections.sort(arrayList, RouteComparator.sInstance);
            if (SystemClock.uptimeMillis() - this.mLastUpdateTime >= 300) {
                this.mLastUpdateTime = SystemClock.uptimeMillis();
                this.mRoutes.clear();
                this.mRoutes.addAll(arrayList);
                this.mAdapter.notifyDataSetChanged();
                return;
            }
            removeMessages(1);
            AnonymousClass1 r1 = this.mHandler;
            r1.sendMessageAtTime(r1.obtainMessage(1, arrayList), this.mLastUpdateTime + 300);
        }
    }

    public final void setRouteSelector(MediaRouteSelector mediaRouteSelector) {
        if (mediaRouteSelector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else if (!this.mSelector.equals(mediaRouteSelector)) {
            this.mSelector = mediaRouteSelector;
            if (this.mAttachedToWindow) {
                this.mRouter.removeCallback(this.mCallback);
                this.mRouter.addCallback(mediaRouteSelector, this.mCallback, 1);
            }
            refreshRoutes();
        }
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void setTitle(int i) {
        this.mTitleView.setText(i);
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(this.mSelector, this.mCallback, 1);
        refreshRoutes();
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(2131624277);
        this.mRoutes = new ArrayList<>();
        this.mAdapter = new RouteAdapter(getContext(), this.mRoutes);
        ListView listView = (ListView) findViewById(2131428417);
        this.mListView = listView;
        listView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setOnItemClickListener(this.mAdapter);
        this.mListView.setEmptyView(findViewById(16908292));
        this.mTitleView = (TextView) findViewById(2131428422);
        getWindow().setLayout(MediaRouteDialogHelper.getDialogWidth(getContext()), -2);
    }
}
