package com.example.doanciclerk.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanciclerk.R;
import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Account_DTO;
import com.google.android.material.tabs.TabLayout;

public class LoginActivity extends AppCompatActivity {
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ui);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red, null));
        tabLayout.addTab(tabLayout.newTab().setText("Store"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Customer"), 1);

        db = new DatabaseHelper(this,"db_a", null, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Button login = findViewById(R.id.btn_DangNhap);

        login.setOnClickListener(v -> {
            String username = ((EditText) findViewById(R.id.txt_SDT)).getText().toString();
            String password = ((EditText) findViewById(R.id.txt_MatKhau)).getText().toString();

            int type = tabLayout.getSelectedTabPosition();
            Account_DTO acc = new Account_DTO(username, password, type);

            if (db.checkLogin(db.getReadableDatabase(), acc)) {
                SharedPreferences sharedPreferences = getSharedPreferences("Logged Account", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Account ID", acc.getId());
                editor.putInt("Account Type", acc.getAccountType());
                editor.apply();

                Intent i;

                if(acc.getAccountType() == 0){
                    i = new Intent(this, ShopMainActivity.class);
                }else{
                    i = new Intent(this, CustomerMainActivity.class);
                }

                i.putExtra("Account", acc);
                startActivity(i);

                db.close();

                finish();
            } else
                Toast.makeText(this, "ID or Password is not valid", Toast.LENGTH_SHORT).show();
        });

        Button signup = findViewById(R.id.btnSignUp);

        signup.setOnClickListener(v -> {
            int type = tabLayout.getSelectedTabPosition();

            Intent i = new Intent(this, SignUpActivity.class);
            i.putExtra("Type", type);
            startActivity(i);
        });
    }
}