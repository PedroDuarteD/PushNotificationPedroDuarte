package com.example.push_notification_pedro_duarte;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

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

import java.util.HashMap;
import java.util.Map;

public  class Notificacao_Esan_BD_14 extends BroadcastReceiver {
    int notificationId = 100;
    private Handler handler;
    private Runnable runnable;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Inicio", Toast.LENGTH_SHORT).show();

 SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        String email =preferences.getString("email","");
        String pass=preferences.getString("pass","");

        Toast.makeText(context, "E: "+email, Toast.LENGTH_LONG).show();
        Toast.makeText(context, "P: "+pass, Toast.LENGTH_LONG).show();

        handler = new Handler();
runnable = new Runnable() {
    @Override
    public void run() {
        handler.postDelayed(runnable, 10000);



        String url ="https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g14/FOOD_CHOOSE/api/utilizador/verificacao.php";




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



    }
};
handler.post(runnable);




    }
}
