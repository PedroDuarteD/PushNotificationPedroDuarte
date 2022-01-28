package com.example.push_notification_pedro_duarte;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.widget.Toast;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.IconCompat;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class PushNotificationPedroDuartePlugin implements  FlutterPlugin, MethodCallHandler, ActivityAware {
  private MethodChannel channel;

  private  Context context;
  private  Activity  activity;
  int notificationId = 1;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "push_notification_pedro_duarte");
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
  if (call.method.equals("notification")) {

      String texto = call.argument("texto");
      String titulo = call.argument("titulo");

      NotificationCompat.Builder builder =
              new NotificationCompat.Builder(context,String.valueOf(notificationId))
                      .setSmallIcon(context.getResources().getIdentifier("food_choose_background", "drawable", context.getPackageName()))
                      .setContentTitle(titulo)
                      .setContentText(texto);


      NotificationManager noti =
              (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel(
                String.valueOf(notificationId),
                String.valueOf(notificationId),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
        noti.createNotificationChannel(channel);
      }

     Intent resultIntent = new Intent(activity.getApplicationContext(), activity.getClass());
      PendingIntent resulPendingIntent =
              PendingIntent.getActivity(activity.getApplicationContext(), 0, resultIntent,
                      PendingIntent.FLAG_UPDATE_CURRENT);
      builder.setContentIntent(resulPendingIntent);



      noti.notify(notificationId, builder.build());


    }else {
        result.notImplemented();
      }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

}
