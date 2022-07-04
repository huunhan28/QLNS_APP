package com.example.food.util;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;

public class ChooseImageUtil {
    static Map<String, String> imageString = new HashMap<>();

    public static void chooseImage(Context context, ImageView img, String requestCode) {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                openImagePicker(context, img, requestCode);
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(context, "Permission deny\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private static void openImagePicker(Context context, ImageView img, String requestCode) {

        TedBottomPicker.with((FragmentActivity) context)
                .show(new TedBottomSheetDialogFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        // here is selected image uri
                        imageString.put(requestCode,uri.toString());
                        System.out.println("uri:" +imageString);

                        try {
                            img.setImageBitmap(
                                    MediaStore.Images.Media.getBitmap(context.getContentResolver()
                                            , Uri.parse(getImageString(requestCode))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public static String getImageString(String requestCode){
        if(imageString.size()!=0){
            if(imageString.get(requestCode)!=null)
                return imageString.get(requestCode);
        }
        return "";

    }

    public static void deleteCode(int requestCode){
        imageString.remove(requestCode);
    }


}
