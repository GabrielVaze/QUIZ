package br.com.softwareminas.quizzzz.thread;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.softwareminas.quizzzz.util.ConexaoHttpClient;


public class TConsultaAPI extends Thread{

    private Handler handler;



    public TConsultaAPI(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        String resposta;
        String url = "https://opentdb.com/api.php?amount=10&category=19&difficulty=easy";
        Message msg;
        try {
            resposta = ConexaoHttpClient.executaHttpGet(url).toString();

        } catch (Exception e) {
            msg = new Message();
            msg.arg1 = 10;//falha de navegação
            handler.sendMessage(msg);
            return;
        }
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(resposta);
        } catch (JSONException e) {
            msg = new Message(); //falha no json
            msg.arg1 = 11;//falha no json (falha no site!)
            handler.sendMessage(msg);
            return;
        }
        try {
            if (jsonObject.getInt("response_code") == 0) {
                msg = new Message();
                msg.arg1 = 1000;
                Bundle data;
                data = new Bundle();
                data.putString("resposta", resposta);
                msg.setData(data);
                handler.sendMessage(msg);
            }else {
                msg = new Message(); //falha no json
                msg.arg1 = 12;//retorno inválido do site.. não tem a variavel respose_code
                handler.sendMessage(msg);
            }
        } catch (JSONException e) {
            msg = new Message(); //falha no json
            msg.arg1 = 12;//retorno inválido do site.. não tem a variavel respose_code
            handler.sendMessage(msg);
        }
    }
}