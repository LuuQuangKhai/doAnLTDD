package com.example.doanciclerk.non_gui;

import android.app.Activity;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.bll.Store_BLL;
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.dto.Order_DTO;
import com.example.doanciclerk.gui.DetailCustomerItemActivity;
import com.example.doanciclerk.gui.DetailStorageItemActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CustomerMainItemAdapter extends RecyclerView.Adapter<CustomerMainItemAdapter.CustomerMainItemVH> {
    List<Goods_DTO> list;
    Context context;

    String customerID;

    Store_BLL store_bll;
    Order_DTO po;

    public CustomerMainItemAdapter(List<Goods_DTO> list, Context context, Order_DTO po, String customerID) {
        this.list = list;
        this.context = context;
        store_bll = new Store_BLL(context);
        this.po = po;
        this.customerID = customerID;
    }

    @NonNull
    @Override
    public CustomerMainItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customermainitem_layout, parent, false);
        CustomerMainItemVH vh = new CustomerMainItemVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerMainItemVH holder, int position) {
        Goods_DTO g = list.get(position);

        String imageName = g.getImage().split("\\.", 2)[0];
        InputStream is;
        try{
            is = context.getResources().openRawResource(context.getResources().getIdentifier(imageName, "raw", context.getPackageName()));
        }
        catch (Exception e){
            try {
                Path p = Paths.get(context.getFilesDir().getPath() + "/" + g.getImage());
                is = context.getContentResolver().openInputStream(Uri.parse(p.toUri().toString()));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        Bitmap img = BitmapFactory.decodeStream(is);

        holder.image.setImageBitmap(img);
        holder.name.setText(g.getName());

        String saleText = String.format("%.0f",g.getDiscount()) + " ₫";
        holder.discount.setText(saleText);

        String priceText = String.format("%.0f",g.getPrice()) + " ₫";
        holder.price.setText(priceText);

        if(g.getDiscount() > 0.0)
            holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            holder.discount.setVisibility(View.INVISIBLE);

        String storeNameText = store_bll.findStoreByID(g.getStoreID()).getName();
        holder.storeName.setText(storeNameText);

        String descText = g.getDescription();
        holder.desc.setText(descText);

        holder.itemView.setOnClickListener(v->{
            Goods_BLL goods_bll = new Goods_BLL(v.getContext());

            Intent i = new Intent(v.getContext(), DetailCustomerItemActivity.class);

            if(po != null)
                i.putExtra("PendingOrder", po);

            i.putExtra("Goods", goods_bll.findGoodsByID(g.getId(), g.getStoreID()));
            i.putExtra("CustomerID", customerID);

            AppCompatActivity act = (AppCompatActivity) context;
            act.startActivityIfNeeded(i,11);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();

        return 0;
    }

    public class CustomerMainItemVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, discount, price, desc, storeName;

        public CustomerMainItemVH(@NonNull View itemView) {
            super(itemView);


            image = itemView.findViewById(R.id.img_customermainItem_image);
            name = itemView.findViewById(R.id.txt_customermainItem_name);
            discount = itemView.findViewById(R.id.txt_customermainItem_discount);
            price = itemView.findViewById(R.id.txt_customermainItem_price);
            desc = itemView.findViewById(R.id.txt_customermainItem_desc);
            storeName = itemView.findViewById(R.id.txt_customermainItem_storename);
        }
    }
}
