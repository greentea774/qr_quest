package com.example.vortex.qr_quest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vortex on 2018/09/02.
 */

public class Game_Activity extends AppCompatActivity {

    /*QRコード読み取り結果格納用変数*/
    String QR_Result = "";

    /*各種問題情報*/
    Qr_Quest_Data Data = new Qr_Quest_Data();

    /*ダイアログフラグメント*/
    AlertDialogFragment Dialog = new AlertDialogFragment();

    /*ペナルティ*/
    String penalty_text = "ペナルティ加算";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*画面初期生成処理はここに記載する*/

        /*問題データ取得（初期化）*/

        //button
        Button qr_input_button = findViewById(R.id.qr_input_button);
        Button quest_answer_button = findViewById(R.id.quest_answer_button);

        /*QRコード読み取りボタンタップ時のイベント処理*/
        qr_input_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // qrコード読み取りカメラを起動する．
                IntentIntegrator integrator = new IntentIntegrator(Game_Activity.this);

                // スキャン画面の回転の制御
                integrator.setOrientationLocked(true);

                // キャプチャ画面起動
                integrator.initiateScan();

                //カメラ終了後はonResumeから再開
            }
        });

        /*回答ボタンタップ時のイベント処理*/
        quest_answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //【デバッグ用】QRコード読み取り結果を初期化する
                // R_Result = "";
                /*最後の問題かどうかチェック*/
                /*最後の問題の場合，正答後の遷移先を別の画面に*/
                if (Data.questNumber ==9){
                    /*問題の回答が正しいかどうかチェック*/
                    if (((EditText) findViewById(R.id.quest_answer_text)).getText().toString().equals(Data.qr_quest_answer[Data.questNumber])){
                        /*問題に正解していた場合，おめでとう画面へ進む*///
                        /*問題正答開始時間をメモする*/
                        // try-with-resources
                        try (FileOutputStream  fileOutputstream = openFileOutput("Result.txt",
                                Context.MODE_APPEND)){

                            fileOutputstream.write(getNowDate().getBytes());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        QR_Result = "";
                        Data.quest_answer[Data.questNumber] = true;
                        Data.questNumber += 1;
                        // Intent のインスタンスを取得する（最初の画面）
                        Intent intent = new Intent(getApplication(), Finish_Activity.class);
                        // 遷移先の画面を呼び出す
                        startActivity(intent);
                    } else {
                        //回答が間違っているので次の問題へは進めない
                        /*ペナルティをメモする*/
                        // try-with-resources
                        try (FileOutputStream  fileOutputstream = openFileOutput("Result.txt",
                                Context.MODE_APPEND)){

                            fileOutputstream.write(penalty_text.getBytes());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //ダイアログ表示のみ行う
                        Dialog.set_message("回答が間違っています．ペナルティとしてポイント減少しました．");
                        Dialog.show(getFragmentManager(), "Alert");
                    }

                /*最後の問題以外の場合，通常処理へ*/
                } else {
                    /*問題の回答が正しいかどうかチェック*/
                    if (((EditText) findViewById(R.id.quest_answer_text)).getText().toString().equals(Data.qr_quest_answer[Data.questNumber])){
                        /*問題に正解していた場合，次の問題へ進める*/
                        /*問題正答開始時間をメモする*/
                        // try-with-resources
                        try (FileOutputStream  fileOutputstream = openFileOutput("Result.txt",
                                Context.MODE_APPEND)){

                            fileOutputstream.write(getNowDate().getBytes());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        QR_Result = "";
                        Data.quest_answer[Data.questNumber] = true;
                        Data.questNumber += 1;
                        ((EditText) findViewById(R.id.quest_answer_text)).setText("");
                        // Intent のインスタンスを取得する（最初の画面）
                        Intent intent = new Intent(getApplication(), NextAnswerWait_Activity.class);
                        // 遷移先の画面を呼び出す
                        startActivity(intent);
                    } else {
                        //回答が間違っているので次の問題へは進めない
                        //回答が間違っているので次の問題へは進めない
                        /*ペナルティをメモする*/
                        // try-with-resources
                        try (FileOutputStream  fileOutputstream = openFileOutput("Result.txt",
                                Context.MODE_APPEND)){

                            fileOutputstream.write(penalty_text.getBytes());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //ダイアログ表示のみ行う
                        Dialog.set_message("回答が間違っています．ペナルティとしてポイント減少しました．");
                        Dialog.show(getFragmentManager(), "Alert");
                    }
                }
            }
        });

    }

    /*戻るボタンを無効化するクラス*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    // ダイアログ表示など特定の処理を行いたい場合はここに記述
                    // 親クラスのdispatchKeyEvent()を呼び出さずにtrueを返す
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /*QRコード読み取り結果取得*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result == null) {
            Log.d("readQR", "Weird");
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        if (result.getContents() == null) {
            // 戻るボタンをタップした場合
            Log.d("readQR", "Cancelled Scan");

        } else {
            // ＱＲコード読み取り後，Activity内配列に読み取り結果を格納する．
            // 格納した値は，onTResumeで正しい値かどうか判定する．
            Log.d("readQR", "Scanned");
            ((TextView) findViewById(R.id.qr_location_hint_text)).setText(result.getContents());
            QR_Result = result.getContents().substring(0,5);
        }
    }

    /*画面生成前に呼び出されるクラス*/
    /*再描画の際のパラメータ書き換え等はここで行う*/
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "onResume is called");

        /*問題着手開始時間をメモする*/
        // try-with-resources
        try (FileOutputStream  fileOutputstream = openFileOutput("Result.txt",
                Context.MODE_APPEND)){

            fileOutputstream.write(getNowDate().getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*問題文更新処理*/
        ((TextView) findViewById(R.id.qr_location_hint_text)).setText(Data.quest_data[Data.questNumber]);

        if (Data.quest_answer[Data.questNumber]){

        } else {
            /*問題が不正解の場合，問題文は初期化する*/
            ((TextView) findViewById(R.id.qr_quest_text)).setText("QRコード読み取り後，さらに問題文が表示されます．");
        }

        /*ＱＲ読み取り正答後処理*/
        /*正しいＱＲコードを読み取っていれば，本問題文の出力をし，
        * 回答欄および，回答するボタンの活性化を行う．*/
        if (QR_Result != ""){
            Log.d("onResume", "QRCode is " + QR_Result);
            if (QR_Result.equals(Data.qr_data[Data.questNumber])) {
                // QRコード読み取り後である場合，画面描画の更新を行う．
                Log.d("onResume", "QRCode is corrected");
                ((TextView) findViewById(R.id.qr_quest_text)).setText(Data.qr_quest_data[Data.questNumber]);
            } else {
                // 読み取ったQRコードが誤っているため，ダイアログ通知の上，ペナルティ
                Log.d("onResume", "QRCode is not corrected...");
                ((TextView) findViewById(R.id.qr_quest_text)).setText("そのＱＲコードではない．．．");
            }
        } else {
            ((TextView) findViewById(R.id.qr_quest_text)).setText("QRコード読み取り後，さらに問題文が表示されます．");
        }


    }

    /**
     * 現在日時をyyyy/MM/dd HH:mm:ss形式で取得する.<br>
     */
    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss\n");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }
}
