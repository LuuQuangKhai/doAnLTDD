package com.example.doanciclerk.non_gui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.dto.Store_DTO;
import com.example.doanciclerk.gui.DetailCustomerItemActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StoreCustomerMainItemAdapter extends RecyclerView.Adapter<StoreCustomerMainItemAdapter.StoreCustomerMainItemVH> {
    Context context;

    Goods_BLL goods_bll;
    Store_BLL store_bll;

    List<Store_DTO> list;

    RecyclerView r;
    String storeID;
    String customerID;
    Order_DTO po;

    public StoreCustomerMainItemAdapter(Context context, List<Store_DTO> list, RecyclerView r, String storeID, Order_DTO po, String customerID) {
        this.context = context;
        goods_bll = new Goods_BLL(context);
        store_bll = new Store_BLL(context);
        this.r = r;
        this.storeID = storeID;
        this.customerID = customerID;

        this.list = list;

        this.po = po;
    }

    @NonNull
    @Override
    public StoreCustomerMainItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.storecustomermainitem_layout, parent, false);
        StoreCustomerMainItemVH vh = new StoreCustomerMainItemVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoreCustomerMainItemVH holder, int position) {
        Store_DTO g = list.get(position);

        String storeNameText = store_bll.findStoreByID(g.getId()).getName();
        holder.storeName.setText(storeNameText);

        holder.itemView.setOnClickListener(v->{
            this.storeID = g.getId();
            CustomerMainItemAdapter adapter = new CustomerMainItemAdapter(goods_bll.getGoods_List_StoreOnly(storeID), context, po, customerID);
            r.setAdapter(adapter);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();

        return 0;
    }

    public class StoreCustomerMainItemVH extends RecyclerView.ViewHolder {
        private TextView storeName;

        public StoreCustomerMainItemVH(@NonNull View itemView) {
            super(itemView);

            storeName = itemView.findViewById(R.id.storenametxt);
        }
    }
}
