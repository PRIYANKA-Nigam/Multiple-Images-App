package com.example.multipleimages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
Button button;ImageView imageView;@Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.bt);button.setOnClickListener(new View.OnClickListener() {@Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},100);
                    return; }
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setType("image/*");startActivityForResult(intent,1); }}); } @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (requestCode==1&&resultCode==RESULT_OK){
          imageView=(ImageView)findViewById(R.id.img); List<Bitmap> bitmaps=new ArrayList<>();
          ClipData clipData=data.getClipData();
          if (clipData!=null){ for (int i=0;i<clipData.getItemCount();i++){
              Uri uri=clipData.getItemAt(i).getUri();
                 try { InputStream in=getContentResolver().openInputStream(uri);
                     Bitmap bitmap= BitmapFactory.decodeStream(in);bitmaps.add(bitmap);
                 } catch (FileNotFoundException e) { e.printStackTrace(); } }
          }else { Uri uri=data.getData();
              try { InputStream in=getContentResolver().openInputStream(uri);
                  Bitmap bitmap= BitmapFactory.decodeStream(in);bitmaps.add(bitmap);
              } catch (FileNotFoundException e) { e.printStackTrace(); } }
          new Thread(new Runnable() {@Override public void run() { for (final Bitmap b :bitmaps){
                      runOnUiThread(new Runnable() {@Override
                          public void run() { imageView.setImageBitmap(b); }});
                      try { Thread.sleep(5000);
                      } catch (InterruptedException e) { e.printStackTrace(); } } }}).start(); }
                      super.onActivityResult(requestCode, resultCode, data); }}