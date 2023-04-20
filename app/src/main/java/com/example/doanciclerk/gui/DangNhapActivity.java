package com.example.doanciclerk.gui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanciclerk.R;
import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Account_DTO;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DangNhapActivity extends AppCompatActivity {
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#727272"), Color.parseColor("#000000"));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.red, null));
        tabLayout.addTab(tabLayout.newTab().setText("Shop"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("Customer"), 1);

        db = new DatabaseHelper(this,"test", null, 1);
        db.onUpgrade(db.getWritableDatabase(),1,1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Button login = findViewById(R.id.btn_DangNhap);

        login.setOnClickListener(v -> {
            String username = ((EditText)findViewById(R.id.txt_SDT)).getText().toString();
            String password = ((EditText)findViewById(R.id.txt_MatKhau)).getText().toString();

            int type = tabLayout.getSelectedTabPosition();
            Account_DTO acc = new Account_DTO(username, password, type);

            if(db.checkLogin(db.getReadableDatabase(), acc))
            {
                Intent i = new Intent(this, ShopMainActivity.class);
                i.putExtra("Account", acc);
                startActivity(i);
            }
            else
                Toast.makeText(this, "ID or Password is not valid", Toast.LENGTH_SHORT).show();
        });
    }
}