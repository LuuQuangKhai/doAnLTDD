package com.example.doanciclerk.gui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doanciclerk.R;
import com.example.doanciclerk.bll.Goods_BLL;
import com.example.doanciclerk.dto.Goods_DTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DetailStorageItemActivity extends AppCompatActivity {
    ImageView image;
    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editstorageitem_ui);

        Goods_DTO goods_dto = (Goods_DTO) getIntent().getSerializableExtra("Goods");
        imageName = goods_dto.getImage();
        Goods_BLL goods_bll = new Goods_BLL(this);

        EditText name = findViewById(R.id.txt_TenMatHang);
        EditText desc = findViewById(R.id.txt_MoTa);
        EditText price = findViewById(R.id.txt_GiaTien);
        EditText amount = findViewById(R.id.txt_SoLuong);
        image = findViewById(R.id.thumbnailImage);

        name.setText(goods_dto.getName());
        desc.setText(goods_dto.getDescription());
        price.setText(String.valueOf(goods_dto.getPrice()));
        amount.setText(String.valueOf(goods_dto.getAmount()));
        image.setImageBitmap(getBitmapFromImage(goods_dto.getImage(), getApplicationContext()));

        image.setOnClickListener(v-> {
            Intent i = new Intent();
            i.setType("image/*");
            i.setAction(Intent.ACTION_GET_CONTENT);
            startActivityIfNeeded(Intent.createChooser(i, "Select image"), 102);
        });

        Button btnCon = findViewById(R.id.btn_XacNhan);
        btnCon.setOnClickListener(v-> {
            String nameText = name.getText().toString().trim();
            String descText = desc.getText().toString().trim();

            double priceText = 0;
            if(!price.getText().toString().isEmpty())
                priceText = Double.parseDouble(price.getText().toString());

            int amountText = 0;
            if(!amount.getText().toString().isEmpty())
                amountText = Integer.parseInt(amount.getText().toString());

            if(!imageName.isEmpty() && !nameText.isEmpty() && !descText.isEmpty()){
                goods_bll.updateGoods(new Goods_DTO(goods_dto.getId(), imageName, nameText, descText, goods_dto.getStoreID(), amountText, priceText));
                Toast.makeText(this, "Changed successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(this, "Cannot change", Toast.LENGTH_SHORT).show();
        });

        Button btnDel = findViewById(R.id.btn_Xoa);
        btnDel.setOnClickListener(v ->{
            goods_bll.deleteGoods(goods_dto.getId());

            String a = goods_dto.getName() + " deleted";
            Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private Bitmap getBitmapFromImage(String imageName, Context context){
        String i = imageName.split("\\.", 2)[0];
        InputStream is;

        try {
            is = context.getResources().openRawResource(context.getResources().getIdentifier(i, "raw", context.getPackageName()));
        }
        catch (Exception e){
            try {
                Path p = Paths.get(context.getFilesDir().getPath() + "/" + imageName);
                is = context.getContentResolver().openInputStream(Uri.parse(p.toUri().toString()));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        return BitmapFactory.decodeStream(is);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 102)
        {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                    image.setImageBitmap(bitmap);

                    imageName = getFilenameFromURI(data.getData());

                    InputStream is = getContentResolver().openInputStream(data.getData());
                    File file = new File(getFilesDir(), imageName);

                    OutputStream os = new FileOutputStream(file);
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    os.write(bytes);

                    os.close();
                    is.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String getFilenameFromURI(Uri data){
        Uri uri = data;
        Cursor c = getContentResolver().query(uri, null,null,null);
        int name = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        c.moveToFirst();
        return c.getString(name);
    }
}