package com.example.doanciclerk.gui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Account_DTO;
import com.example.doanciclerk.dto.Store_DTO;

public class ShopMainActivity extends AppCompatActivity {
    Store_BLL store_bll;
    Store_DTO thisStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopmain_ui);

        Account_DTO acc = (Account_DTO) getIntent().getSerializableExtra("Account");
        store_bll = new Store_BLL(this);
        thisStore = store_bll.findStoreByID(acc.getId());

        TextView welcome = findViewById(R.id.txtWelcome);
        String newWelcome = "Welcome " + thisStore.getName();
        welcome.setText(newWelcome);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            logoutAlert();
        });

        ImageButton btnQLTK = findViewById(R.id.btnQuanLyTonKho);
        btnQLTK.setOnClickListener(v->{
            Intent i = new Intent(this, StorageActivity.class);

            i.putExtra("Account ID", acc.getId());
            startActivity(i);
        });

        ImageButton btnInfo = findViewById(R.id.btnUserInfo);
        btnInfo.setOnClickListener(v -> {
            Intent i = new Intent(this, StoreInfoActivity.class);

            i.putExtra("Store", thisStore);
            startActivity(i);
        });

        ImageButton btnQLDH = findViewById(R.id.btnQuanLyDonHang);
        btnQLDH.setOnClickListener(v -> {
            Intent i = new Intent(this, OrderManageActivity.class);

            i.putExtra("Account ID", acc.getId());
            startActivity(i);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        thisStore = store_bll.findStoreByID(thisStore.getId());
        TextView welcome = findViewById(R.id.txtWelcome);
        String newWelcome = "Welcome " + thisStore.getName();
        welcome.setText(newWelcome);
    }

    @Override
    public void onBackPressed() {
        logoutAlert();
    }

    private void logoutAlert(){
        AlertDialog.Builder b = new AlertDialog.Builder(this, com.google.android.material.R.style.Theme_Material3_Dark_Dialog_Alert);
        b.setTitle("Log out");
        b.setMessage("Do you want to log out ?");
        b.setPositiveButton("Confirm", (dialog, which) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Logged Account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        b.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog a = b.show();
    }
}