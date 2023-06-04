package com.example.doanciclerk.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.non_gui.StorageItemAdapter;

import java.util.List;

public class StorageActivity extends AppCompatActivity {
    List<Goods_DTO> list;
    Goods_BLL goods_bll;
    RecyclerView recyclerView;
    StorageItemAdapter adapter;
    String storeID;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storagemanager_ui);

        context = this;

        Intent intent = getIntent();
        storeID = intent.getStringExtra("Account ID");

        goods_bll = new Goods_BLL(this);
        recyclerView = findViewById(R.id.storageItem_recycler);

        list = goods_bll.getGoods_List_StoreOnly(storeID);
        adapter = new StorageItemAdapter(list, this);
        recyclerView.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btn_Them);
        btnAdd.setOnClickListener(v->{
            Intent i = new Intent(this, DetailStorageItemActivity_Add.class);

            i.putExtra("Account ID", storeID);
            startActivity(i);
        });

        ImageButton btnBackTop = findViewById(R.id.btnBackTop);
        btnBackTop.setEnabled(false);
        btnBackTop.setVisibility(View.INVISIBLE);
        btnBackTop.setOnClickListener(v -> {
            recyclerView.scrollToPosition(0);

            btnBackTop.setEnabled(false);
            btnBackTop.setVisibility(View.INVISIBLE);
        });

        ImageButton btnBackStore = findViewById(R.id.btnBackStore);
        btnBackStore.setOnClickListener(v -> {
            finish();
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.computeVerticalScrollOffset() == 0){
                    btnBackTop.setEnabled(false);
                    btnBackTop.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    btnBackTop.setEnabled(true);
                    btnBackTop.setVisibility(View.VISIBLE);
                }
            }
        });

        SearchView searchView = findViewById(R.id.searchview);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter = new StorageItemAdapter(goods_bll.search(newText, storeID), context);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        list = goods_bll.getGoods_List_StoreOnly(storeID);
        adapter = new StorageItemAdapter(list, this);
        recyclerView.setAdapter(adapter);
    }
}