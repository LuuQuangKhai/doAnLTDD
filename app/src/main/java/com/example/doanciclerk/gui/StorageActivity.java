package com.example.doanciclerk.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.non_gui.StorageItemAdapter;

import java.util.List;

public class StorageActivity extends AppCompatActivity {
    List<Goods_DTO> list;
    Goods_BLL goods_bll;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storagemanager_ui);

        String storeID = getIntent().getStringExtra("Account ID");

        goods_bll = new Goods_BLL(this);
        list = goods_bll.getGoods_List();

        StorageItemAdapter storageItemAdapter = new StorageItemAdapter(list, this);
        recyclerView = findViewById(R.id.storageItem_recycler);
        recyclerView.setAdapter(storageItemAdapter);

        Button btnAdd = findViewById(R.id.btn_Them);
        btnAdd.setOnClickListener(v->{
            Intent i = new Intent(this, DetailStorageItemActivity_Add.class);

            i.putExtra("Account ID", storeID);
            startActivity(i);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        list = goods_bll.getGoods_List();
        StorageItemAdapter storageItemAdapter = new StorageItemAdapter(list, this);
        recyclerView.setAdapter(storageItemAdapter);
    }
}