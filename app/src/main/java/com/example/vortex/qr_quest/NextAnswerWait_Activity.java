package com.example.vortex.qr_quest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class NextAnswerWait_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nextanswerwait);

        //button
        Button nextanswer_stanby_button = findViewById(R.id.nextanswer_stanby_button);

        /*次の回答者へのボタンタップ時のイベント処理*/
        nextanswer_stanby_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //この画面を終わらせ，問題画面を再描画する
                finish();
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
}
