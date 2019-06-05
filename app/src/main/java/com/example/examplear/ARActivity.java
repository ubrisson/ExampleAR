package com.example.examplear;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.examplear.AR.GLView;

import cn.easyar.Engine;

public class ARActivity extends AppCompatActivity {

    /* ARActivity est une activité qui gère la vue AR et les permissions de la caméra */
    /* Son layout ne contient que la Vue camera + AR */

    private GLView glView;
    private Button endGameButton;

    //Start of region Cycle de Vie
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        /* Désactive la mise en veille automatique de l'écran */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        /* Lancement du moteur easyAR */
        /*
         * Steps to create the key:
         *  1. login www.easyar.com
         *  2. create app with
         *      Name: Appname
         *      Package Name: com.example.______
         *  3. find the created item in the list and show key
         *  4. set key string bellow
         */
        String key = "0LEL05PfPLSl783DoRUDyzkxs8Wr1f3M01GHjGFd5dTWxaktlkBYFAtaKtgGHXMqCFQ0H5" +
                "8eqWrHpE5UWWUWwSJ9ObXSwqR9ORqxHgHgo58qpEP0evE58IanYxqY9Sf1WNxoPydIpRekophlE" +
                "7VMlBVTfSIYVzdWcCeUJRMVtWytG5G5sLPVX5L78SW9di77qeVzhS0J";
        if (!Engine.initialize(this, key)) {
            Log.e("ARActivity", "Initialization Failed.");
        }

        /*Création de la Vue spéciale AR */
        glView = new GLView(this);

        endGameButton = new Button(this);
        endGameButton.setText("NEXT MEME");
        endGameButton.setOnClickListener(glView);


        /* Demande des permissions camera */
        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ViewGroup group = findViewById(R.id.preview);
                group.addView(glView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));
                group.addView(endGameButton,(ViewGroup.LayoutParams.WRAP_CONTENT),(ViewGroup.LayoutParams.WRAP_CONTENT));
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause() {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }
    //end of region Cycle de Vie

    //Start of region interface
    private interface PermissionCallback {
        void onSuccess();
        void onFailure();
    }
    //end of region interface

    //Start of region fonctions utilitaires
    private SparseArray<PermissionCallback> permissionCallbacks = new SparseArray<>();
    private int permissionRequestCodeSerial = 0;

    @TargetApi(23)
    private void requestCameraPermission(PermissionCallback callback) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionCallbacks.get(requestCode) != null) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    // end of region fonctions utilitaires



}
