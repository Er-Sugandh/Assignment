package com.example.mygaller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import gun0912.tedbottompicker.TedRxBottomPicker;

public class MainGaller extends AppCompatActivity {

    private Button btnSelectImage;
    private RecyclerView revPhoto;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_galler);

        btnSelectImage = findViewById(R.id.btn_select_image);
        revPhoto = findViewById(R.id.rev_photo);
        photoAdapter = new PhotoAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        revPhoto.setLayoutManager(gridLayoutManager);
        revPhoto.setAdapter(photoAdapter);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               requestPermissions();
            }
        });

    }

    private void  requestPermissions(){

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
               selectImagesFromGallery();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainGaller.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }
    private void selectImagesFromGallery(){
        TedBottomPicker.with(MainGaller.this)
                .setPeekHeight(1600)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
                    @Override
                    public void onImagesSelected(List<Uri> uriList) {
                        if (uriList != null && !uriList.isEmpty()){
                            photoAdapter.setData(uriList);
                        }
                    }
                });
    }
}