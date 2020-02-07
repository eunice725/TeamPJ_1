package com.example.teampj_1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edtId, edtPassword;
    TextView tvLogin;
    ImageView imgExit;

    BluetoothDB btDB;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtId = (EditText) findViewById(R.id.edtId);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        imgExit = (ImageView) findViewById(R.id.imgExit);
        tvLogin = (TextView) findViewById(R.id.tvLogin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btDB = new BluetoothDB(this);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtId.getText().toString();
                String password = edtPassword.getText().toString();

                if (password.equals("")) {
                    showToast("password null error");
                } else if (id.equals("")) {
                    showToast("id null error");
                } else {
                    sqlDB = btDB.getReadableDatabase(); //읽다
                    Cursor cUser = sqlDB.rawQuery("SELECT id,password FROM bluetoothUserTBL WHERE id='" + id + "';", null);
                    if (cUser.moveToFirst()) { //디비가 존재한다면
                        String strId, strPassword;
                        strId = cUser.getString(0);
                        strPassword = cUser.getString(1);
                        if (id.equals(strId) && password.equals(strPassword)) { //동일하면
                            showToast("로그인을 하였습니다");
                            finish(); //현재창 종료하기
                        } else {
                            showToast("비밀번호를 다시한번 확인해주세요.");
                            Log.i("test", "비번틀림" + strId);
                        }
                    } else {
                        showToast("회원 정보가 없습니다.");
                    }
                    sqlDB.close();
                    cUser.close();
                }
            }
        });

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //현재창 종료하기
            }
        });
    } //onCreate END

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
