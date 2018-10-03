package com.example.vortex.qr_quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*画面生成処理はここに記載する*/

        //button
        Button start_game_button = findViewById(R.id.start_game_button);

        start_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent のインスタンスを取得する（最初の画面）
                Intent intent = new Intent(getApplication(), Game_Activity.class);
                // 遷移先の画面を呼び出す
                startActivity(intent);
            }
        });
    }

    /*activity 再開*/
    /*画面再描画はこのタイミングで行われる*/
    @Override
    protected void onResume() {
        super.onResume();

    }

    /*activity 一時停止*/
    /*データの更新や保存はonPauseで行う*/
    @Override
    protected void onPause() {
        super.onPause();
    }
}
