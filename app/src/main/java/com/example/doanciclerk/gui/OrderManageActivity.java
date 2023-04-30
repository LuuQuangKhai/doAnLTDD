package com.example.doanciclerk.gui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.non_gui.OrderItemAdapter;
import com.example.doanciclerk.non_gui.StorageItemAdapter;

import java.util.List;

public class OrderManageActivity extends AppCompatActivity {
    List<Order_DTO> list;
    String storeID;

    Orders_BLL orders_bll;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordermanager_ui);

        storeID = getIntent().getStringExtra("Account ID");

        orders_bll = new Orders_BLL(this);
        list = orders_bll.getOrders_List(storeID);

        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(list, this);
        recyclerView = findViewById(R.id.orderItem_recycler);
        recyclerView.setAdapter(orderItemAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        list = orders_bll.getOrders_List(storeID);
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(list, this);
        recyclerView = findViewById(R.id.orderItem_recycler);
        recyclerView.setAdapter(orderItemAdapter);
    }
}