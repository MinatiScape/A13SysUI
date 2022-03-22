package com.google.android.systemui.gamedashboard;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PlayGamesWidget {
    public final Context mContext;
    public final Handler mMainHandler;
    public final GameDashboardUiEventLogger mUiEventLogger;
    public final ResultReceiver mWidgetResultReceiver;
    public final WidgetView mWidgetView;

    /* loaded from: classes.dex */
    public class PlayGamesWidgetResultReceiver extends ResultReceiver {
        public PlayGamesWidgetResultReceiver(Handler handler) {
            super(handler);
        }

        @Override // android.os.ResultReceiver
        public final void onReceiveResult(int i, Bundle bundle) {
            PlayGamesWidget.this.updateFromData(bundle);
        }
    }

    public final void updateFromData(Bundle bundle) {
        View.OnClickListener onClickListener;
        Bitmap bitmap = (Bitmap) bundle.getParcelable("icon");
        String string = bundle.getString("title");
        String string2 = bundle.getString("tipText");
        String string3 = bundle.getString("description");
        final PendingIntent pendingIntent = (PendingIntent) bundle.getParcelable("onClickPendingIntent");
        boolean z = bundle.getBoolean("isActive");
        if (z) {
            onClickListener = new View.OnClickListener() { // from class: com.google.android.systemui.gamedashboard.PlayGamesWidget$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PlayGamesWidget playGamesWidget = PlayGamesWidget.this;
                    PendingIntent pendingIntent2 = pendingIntent;
                    Objects.requireNonNull(playGamesWidget);
                    try {
                        playGamesWidget.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_MENU_PLAY_GAMES_WIDGET);
                        ((Activity) playGamesWidget.mContext).startIntentSenderForResult(pendingIntent2.getIntentSender(), 0, null, 0, 0, 0);
                    } catch (IntentSender.SendIntentException unused) {
                        Log.w("PlayGamesWidget", "Failed to start Play Games PendingIntent.");
                    }
                }
            };
        } else {
            onClickListener = null;
        }
        WidgetView widgetView = this.mWidgetView;
        Objects.requireNonNull(widgetView);
        widgetView.mIcon.setImageBitmap(bitmap);
        widgetView.mTipText.setText(string2);
        widgetView.mTitle.setText(string);
        widgetView.mDescription.setText(string3);
        widgetView.setOnClickListener(onClickListener);
        this.mWidgetView.setEnabled(z);
        this.mWidgetView.setLoading(false);
    }

    public PlayGamesWidget(Context context, WidgetView widgetView, Handler handler, GameDashboardUiEventLogger gameDashboardUiEventLogger) {
        this.mContext = context;
        this.mWidgetView = widgetView;
        this.mMainHandler = handler;
        this.mUiEventLogger = gameDashboardUiEventLogger;
        PlayGamesWidgetResultReceiver playGamesWidgetResultReceiver = new PlayGamesWidgetResultReceiver(handler);
        Parcel obtain = Parcel.obtain();
        playGamesWidgetResultReceiver.writeToParcel(obtain, 0);
        obtain.setDataPosition(0);
        obtain.recycle();
        this.mWidgetResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(obtain);
    }
}
