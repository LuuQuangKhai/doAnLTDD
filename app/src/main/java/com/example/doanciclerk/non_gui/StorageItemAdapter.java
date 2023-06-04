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
import com.example.doanciclerk.dto.Goods_DTO;
import com.example.doanciclerk.gui.DetailStorageItemActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class StorageItemAdapter extends RecyclerView.Adapter<StorageItemAdapter.StorageItemVH> {
    List<Goods_DTO> list;
    Context context;

    public StorageItemAdapter(List<Goods_DTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StorageItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.storageitem_layout, parent, false);
        StorageItemVH vh = new StorageItemVH(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StorageItemVH holder, int position) {
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

        String saleText = String.valueOf(g.getDiscount());
        holder.discount.setText(saleText);

        String priceText = String.format("%.0f",g.getPrice()) + " ₫";
        holder.price.setText(priceText);
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.itemView.setOnClickListener(v->{
            Goods_BLL goods_bll = new Goods_BLL(v.getContext());

            Intent i = new Intent(v.getContext(), DetailStorageItemActivity.class);

            i.putExtra("Goods", goods_bll.findGoodsByID(g.getId(), g.getStoreID()));

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        if(list != null)
            return list.size();

        return 0;
    }

    public class StorageItemVH extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, discount, price;

        public StorageItemVH(@NonNull View itemView) {
            super(itemView);


            image = itemView.findViewById(R.id.img_storageItem_image);
            name = itemView.findViewById(R.id.txt_storageItem_name);
            discount = itemView.findViewById(R.id.txt_storageItem_discount);
            price = itemView.findViewById(R.id.txt_storageItem_price);
        }
    }
}
