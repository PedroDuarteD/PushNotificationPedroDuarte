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
import android.util.Log;
import android.widget.Toast;

import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
              new NotificationCompat.Builder(context, String.valueOf(notificationId))
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


  }else if(call.method.equals("not_event")){




      String url ="https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g14/FOOD_CHOOSE/api/utilizador/verificacao.php";

        /*SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String email =preferences.getString("email","");
        String pass=preferences.getString("pass","");*/

      String email = "pedrootrabalhador@gmail.com";;
      String pass = "foodchoose";
      //while (!Thread.currentThread().isInterrupted()) {
      /*  boolean run =true;
        while (run){

            if (notificationId==1000){
                run=false;
            }*/


      RequestQueue queue = Volley.newRequestQueue(context);
      StringRequest postRequest = new StringRequest(Request.Method.POST, url,
              new Response.Listener<String>()
              {
                  @Override
                  public void onResponse(String response) {



                      try {
                          JSONObject jsonObjectRequest = new JSONObject(response);

                          int id_ = jsonObjectRequest.getInt("id_utilizador");

                          String cargo = jsonObjectRequest.getString("cargo");
                          String url2 ="https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g14/FOOD_CHOOSE/api/gestor/list_encomendas_gestor.php?cargo="+cargo
                                  +"&id_utilizador="+id_;


                          RequestQueue receber = Volley.newRequestQueue(context);
                          StringRequest pedido_bd = new StringRequest(Request.Method.GET, url2,
                                  new Response.Listener<String>() {
                                      @Override
                                      public void onResponse(String response) {

                                          try {
                                              JSONArray jsonArray = new JSONArray(response);
                                              int contador =0;
                                              for(int posicao=0;posicao<jsonArray.length();posicao++){

                                                  Encomenda encomenda = new Encomenda();
                                                  JSONObject ob = (JSONObject) jsonArray.get(posicao);
                                                  encomenda.quantidade=ob.getInt("quantidade");
                                                  encomenda.nome_receita =ob.getString("nome_receita");

                                                  String nome_do_criador="";


                                                  if(!ob.getString("nome_restaurante").isEmpty()){
                                                      nome_do_criador ="Criador: "+ob.getString("nome_restaurante");

                                                  }else if(!ob.getString("nome_criador_receita").isEmpty()){
                                                      nome_do_criador ="Criador: "+ob.getString("nome_criador_receita");

                                                  }else{
                                                      nome_do_criador="Receita de Agendamento !";
                                                  }


                                                  NotificationCompat.Builder builder =
                                                          new NotificationCompat.Builder(context,String.valueOf(notificationId))
                                                                  .setSmallIcon(context.getResources().getIdentifier("food_choose_background", "drawable", context.getPackageName()))
                                                                  .setContentTitle("Encomenda da receita: "+encomenda.nome_receita)
                                                                  .setSubText(nome_do_criador )
                                                                  .setGroup("food_choose")
                                                                  .setContentText("Para "+encomenda.quantidade+" pessoa"+(encomenda.quantidade>1? "s":"")+" .");

                                                  NotificationManager notificationManager =
                                                          (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                                  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                      NotificationChannel channel = new NotificationChannel(
                                                              String.valueOf(notificationId),
                                                              String.valueOf(notificationId),
                                                              NotificationManager.IMPORTANCE_DEFAULT);
                                                      channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                                                      notificationManager.createNotificationChannel(channel);
                                                  }

                                                  notificationManager.notify(notificationId,builder.build());
                                                  contador+=1;
                                                  notificationId+=1;
                                              }

                                              Toast.makeText(context, "VEZES: "+contador, Toast.LENGTH_SHORT).show();
                                          } catch (JSONException e) {
                                              e.printStackTrace();
                                          }

                                      }
                                  }, new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  Toast.makeText(context, "Erro", Toast.LENGTH_LONG).show();
                              }
                          });
                          receber.add(pedido_bd);


                      } catch (JSONException e) {
                          e.printStackTrace();
                      }

                  }
              },
              new Response.ErrorListener()
              {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      // error
                      Log.d("Error.Response", error.getMessage());
                  }
              }
      ) {
          @Override
          protected Map<String, String> getParams()
          {
              Map<String, String>  params = new HashMap<String, String>();
              params.put("email", email);
              params.put("password", pass);

              return params;
          }
      };
      queue.add(postRequest);
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
