package com.example.push_notification_pedro_duarte;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Meu_servico extends Service {
    int notificationId = 1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



      /*  SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
       String email =preferences.getString("email",""); //String email = "pedrootrabalhador@gmail.com";
       String pass=preferences.getString("pass",""); //String pass = "d0ninhas";*/

        String url ="https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g14/FOOD_CHOOSE/api/utilizador/verificacao.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {



                        try {
                            JSONObject jsonObjectRequest = new JSONObject(response);

                            int id_ = jsonObjectRequest.getInt("id_utilizador");

                            String cargo = jsonObjectRequest.getString("cargo");
                            String url2 ="https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g14/FOOD_CHOOSE/api/gestor/list_encomendas_gestor.php?cargo="+cargo+"&id_utilizador="+id_;


                          RequestQueue receber = Volley.newRequestQueue(getApplicationContext());
                          StringRequest pedido_bd = new StringRequest(Request.Method.GET, url2,
                                  new Response.Listener<String>() {
                                      @Override
                                      public void onResponse(String response) {

                                          try {
                                              JSONArray jsonArray = new JSONArray(response);
int contador =0;
                                             for(int posicao=0;posicao<jsonArray.length();posicao++){
                                                 if(contador==0){

                                                 Encomenda encomenda = new Encomenda();
                                                 JSONObject ob = (JSONObject) jsonArray.get(posicao);
                                                 encomenda.nome_receita =ob.getString("nome_receita");

                                                 String nome_do_criador="";


                                                 if(ob.getString("nome_criador_receita").isEmpty()){
                                                    nome_do_criador ="Criador: "+ob.getString("nome_restaurante");

                                                 }else if(ob.getString("nome_restaurante").isEmpty()){
                                                    nome_do_criador ="Criador: "+ob.getString("nome_criador_receita");

                                                 }else{
                                                     nome_do_criador="Receita de Agendamento !";
                                                 }


                                                 NotificationCompat.Builder builder =
                                                         new NotificationCompat.Builder(getApplicationContext(),String.valueOf(notificationId))
                                                                 .setSmallIcon(getApplicationContext().getResources().getIdentifier("food_choose_background", "drawable", getApplicationContext().getPackageName()))
                                                                 .setContentTitle(nome_do_criador)
                                                                 .setSubText("Encomenda da receita: "+encomenda.nome_receita)
                                                                 .setContentText("Para "+encomenda.quantidade+" pessoa"+(encomenda.quantidade>1? "s":"")+" .");

                                                 NotificationManager notificationManager =
                                                         (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                                                 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                                     NotificationChannel channel = new NotificationChannel(
                                                             String.valueOf(notificationId),
                                                             String.valueOf(notificationId),
                                                             NotificationManager.IMPORTANCE_DEFAULT);
                                                     channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                                                     notificationManager.createNotificationChannel(channel);
                                                 }

                notificationManager.notify(notificationId,builder.build());


                                             }
                                                 contador+=1;
                                             }

                                              Toast.makeText(getApplicationContext(), "VEZES: "+contador, Toast.LENGTH_SHORT).show();
                                          } catch (JSONException e) {
                                              e.printStackTrace();
                                          }

                                      }
                                  }, new Response.ErrorListener() {
                              @Override
                              public void onErrorResponse(VolleyError error) {
                                  Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
                              }
                          });
receber.add(pedido_bd);

                            /**
                             *
                             NotificationCompat.Builder builder =
                             new NotificationCompat.Builder(getApplicationContext(),String.valueOf(notificationId))
                             .setSmallIcon(getApplicationContext().getResources().getIdentifier("food_choose", "drawable", getApplicationContext().getPackageName()))
                             .setContentTitle("Food Choose")
                             .setContentText("Mensagem "+id_+" !");

                             NotificationManager notificationManager =
                             (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                             if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                             NotificationChannel channel = new NotificationChannel(
                             String.valueOf(notificationId),
                             String.valueOf(notificationId),
                             NotificationManager.IMPORTANCE_DEFAULT);
                             channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                             notificationManager.createNotificationChannel(channel);
                             }

                             startForeground(notificationId,builder.build() );
                             *
                             * */

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
                params.put("email", "pedrootrabalhador@gmail.com");
                params.put("password", "foodchoose");

                return params;
            }
        };
        queue.add(postRequest);







        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

