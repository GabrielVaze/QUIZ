package br.com.softwareminas.quizzzz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import br.com.softwareminas.quizzzz.thread.TConsultaAPI;

public class MainActivity extends AppCompatActivity {

    int acertos;
    int numeropergunta;
    JSONObject jsonresposta;
    JSONArray respostas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void preparatelainicial(){
        setContentView(R.layout.activity_main);
        Button btiniciar = findViewById(R.id.fmMain_btIniciar);
        btiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarquiz();
            }
        });
    }
    private void preparatelaresultado(){
        setContentView(R.layout.activity_result);
        TextView lbacertos = findViewById(R.id.fmResult_lbacertos);
        lbacertos.setText(String.valueOf(acertos)+"/10");
        TextView msgquestoes = findViewById(R.id.fmResult_msgquestoes);
        if (acertos == 10) {
            msgquestoes.setText("VOCÊ FOI PERFEITO!!! PARABÉNS!!!!! \n ACERTOU TODAS AS QUESTÕES! \n\n QI+100!!! ");
        }else {
            if (acertos >= 6) {
                msgquestoes.setText("Você acertou " + String.valueOf(acertos) + " questões! Parabéns pelo resultado, está na média!!!!");
            } else {
                if (acertos >= 3) {
                    msgquestoes.setText("Que pena você acertou só " + String.valueOf(acertos) + " questões! Volte a estudar mais um pouco!!");
                } else {
                    if (acertos == 0) {
                        msgquestoes.setText("FALA SÉRIO, VC NÃO ACERTOU NENHUMA QUESTÃO!!!!!!!!! \n\n QI MENOR QUE UM PRIMATA!!!");
                    } else {
                        if (acertos == 1) {
                            msgquestoes.setText("Você está de parabéns, conseguiu apenas 1 questão!!!!!");
                        } else {
                            msgquestoes.setText("Só 2 questões? Tá de brincadeira comigo?");
                        }
                    }
                }
            }
        }
        Button btnovoquiz = findViewById(R.id.fmResult_btNovoquiz);
        btnovoquiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparatelainicial();
            }
        });
    }

    private void preparatelaperguntas(){
        setContentView(R.layout.activity_questoes);

        Button btcancelar = findViewById(R.id.fmQuestao_btiniciar);
        btcancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preparatelainicial();
            }
        });

    }

    private void montapergunta(){
        Random random = new Random();
        int respostacorreta = random.nextInt(4) + 1;

        TextView lbnumquestao = findViewById(R.id.fmQuestao_lbnumquestao);
        TextView lbpergunta = findViewById(R.id.fmQuestao_lbpergunta);

        Button bt1 = findViewById(R.id.fmQuestao_btresp1);
        Button bt2 = findViewById(R.id.fmQuestao_btresp2);
        Button bt3 = findViewById(R.id.fmQuestao_btresp3);
        Button bt4 = findViewById(R.id.fmQuestao_btresp4);

        lbnumquestao.setText("Questão "+String.valueOf(numeropergunta)+"/10");

        JSONObject questao;
        try {
            questao = respostas.getJSONObject(numeropergunta-1);
            lbpergunta.setText(questao.getString("question"));

            JSONArray incorrectAnswersArray = questao.getJSONArray("incorrect_answers");


            bt1.setText(incorrectAnswersArray.getString(0));
            bt2.setText(incorrectAnswersArray.getString(1));
            bt3.setText(incorrectAnswersArray.getString(2));

            switch (respostacorreta){
                case 1:
                    bt4.setText(incorrectAnswersArray.getString(0));
                    bt1.setText(questao.getString("correct_answer"));
                    break;
                case 2:
                    bt4.setText(incorrectAnswersArray.getString(1));
                    bt2.setText(questao.getString("correct_answer"));
                    break;
                case 3:
                    bt4.setText(incorrectAnswersArray.getString(3));
                    bt3.setText(questao.getString("correct_answer"));
                    break;
                case 4:
                    bt4.setText(questao.getString("correct_answer"));
                    break;
            }
        }catch (JSONException e){

        }

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaresposta(respostacorreta == 1);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaresposta(respostacorreta == 2);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaresposta(respostacorreta == 3);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificaresposta(respostacorreta == 4);
            }
        });

    }

    private void verificaresposta(boolean acertou){
        if (acertou){acertos++;}
        numeropergunta++;
        if (numeropergunta > 10) {
            preparatelaresultado();
        }else{
            montapergunta();
        }
    }

    private void iniciarquiz(){
        final ProgressDialog pDialog = ProgressDialog.show(this, "Aguarde", "Gerando perguntas para o quiz...");
        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                switch (msg.arg1) {
                    case 1000:
                        pDialog.dismiss();

                        numeropergunta = 1;
                        acertos = 0;

                        try {
                            jsonresposta = new JSONObject(msg.getData().getString("resposta").toString());
                            respostas =  jsonresposta.getJSONArray("results");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        preparatelaperguntas();
                        montapergunta();
                        break;
                    default:
                        pDialog.dismiss();
                        String msgresposta = "";
                        switch (msg.arg1) {
                            case 1:msgresposta="Falha na navegação";break;
                            case 11:msgresposta="Resposta do site inválida (conteúdo json inválido)";break;
                            case 12:msgresposta="Retorno inválido do site";break;
                        }

                        builder.setMessage("Não foi possível concluir a operação! \n "+msgresposta);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        TConsultaAPI thread = new TConsultaAPI(handler);
        thread.start();
    }

}