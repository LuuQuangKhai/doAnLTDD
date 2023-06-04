package com.example.doanciclerk.non_gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Customer_BLL;
import com.example.doanciclerk.bll.OrderDetails_BLL;
import com.example.doanciclerk.bll.Orders_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemVH> {
    List<Order_DTO> list;

    OrderDetails_BLL orderDetails_bll;
    Orders_BLL orders_bll;
    Customer_BLL customer_bll;
    Context context;

    public OrderItemAdapter(List<Order_DTO> list, Context context) {
        this.list = new ArrayList<>();
        if(list != null) {
            this.list = list;
        }

        this.context = context;

        orders_bll = new Orders_BLL(context);
        customer_bll = new Customer_BLL(context);
        orderDetails_bll = new OrderDetails_BLL(context);
    }

    @NonNull
    @Override
    public OrderItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_layout, parent, false);
        OrderItemVH vh = new OrderItemVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemVH holder, int position) {
        Order_DTO o = list.get(position);
        double sumNum = 0;

        if(o != null)
            sumNum = o.getPriceSum();

        String idText = "OrderID: " + o.getId();
        holder.id.setText(idText);

        String nameText = customer_bll.findCustomerByID(o.getCustomerID()).getName();
        holder.name.setText(nameText);

        String totalText = String.valueOf(o.getPriceSum());
        holder.total.setText(totalText);

        OrderItemDetailAdapter adapter = new OrderItemDetailAdapter(o, context);
        holder.recyclerViewBig.setAdapter(adapter);

        holder.no.setOnClickListener(v -> {
            String storeID = o.getStoreID();
            orders_bll.deleteOrder(o.getId());
            list = orders_bll.getOrders_List(storeID);
            notifyDataSetChanged();
            Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
        });

        holder.yes.setOnClickListener(v -> {
            String storeID = o.getStoreID();
            orders_bll.deleteOrder(o.getId());
            list = orders_bll.getOrders_List(storeID);
            notifyDataSetChanged();
            Toast.makeText(context, "Delivered", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();

        return 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class OrderItemVH extends RecyclerView.ViewHolder {
        private TextView id, name, total;

        private RecyclerView recyclerViewBig;

        private ImageButton no, yes;
        public OrderItemVH(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.orderidtxt);
            recyclerViewBig = itemView.findViewById(R.id.recyclerView);
            name = itemView.findViewById(R.id.customernametxt);
            total = itemView.findViewById(R.id.totaltxt);

            no = itemView.findViewById(R.id.btnNo);
            yes = itemView.findViewById(R.id.btnYes);
        }
    }
}
