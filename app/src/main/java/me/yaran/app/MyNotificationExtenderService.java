package me.yaran.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import java.math.BigInteger;

public class MyNotificationExtenderService extends NotificationExtenderService {
    private final String tag = getClass()+"Atiar = ";

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {

        Log.e(tag, notification.payload.toJSONObject().toString());

        OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {

            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                 builder.setColor(new BigInteger("FF7E0901", 16).intValue());
                 builder.setSound(getSoundUri(MyApplication.getContext(),"onesignal_default_sound"));
                return builder;
            }
        };



        OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
        Log.d(tag, "Notification displayed with id: " + displayedResult.androidNotificationId);

        return true;
    }

















    private Uri getSoundUri(Context context, String sound) {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int soundId;

        if (isValidResourceName(sound)) {
            soundId = resources.getIdentifier(sound, "raw", packageName);
            if (soundId != 0)
                return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + soundId);
        }

        soundId = resources.getIdentifier("onesignal_default_sound", "raw", packageName);
        if (soundId != 0)
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + soundId);

        return null;
    }

    static boolean isValidResourceName(String name) {
        return (name != null && !name.matches("^[0-9]"));
    }
}
