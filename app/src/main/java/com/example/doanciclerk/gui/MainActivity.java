package com.example.doanciclerk.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.doanciclerk.dal.DatabaseHelper;
import com.example.doanciclerk.dto.Account_DTO;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getApplicationContext(), "db_a", null, 1);
        db.onUpgrade(db.getWritableDatabase(),1,1);

        SharedPreferences sharedPreferences = getSharedPreferences("Logged Account", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("Account ID") && sharedPreferences.contains("Account Type")) {
            String id = sharedPreferences.getString("Account ID", "");
            int accountType = sharedPreferences.getInt("Account Type", -1);

            Account_DTO acc = db.findAccountByID(db.getReadableDatabase(), id, accountType);

            Intent i = new Intent(this, ShopMainActivity.class);

            i.putExtra("Account", acc);
            startActivity(i);
        } else {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        db.close();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}