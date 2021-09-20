package com.meuamericanet.redetelecom.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.meuamericanet.redetelecom.R;

/**
 * Classe responsável pela animação inicial/splash
 * do projeto do app
 *
 * @author      Igor Maximo
 * @date        10/09/2021
 */
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // remove barra de título
        getWindow().getDecorView();
        setContentView(R.layout.activity_splash);
        // Aguarda um tempo para realizar transição de tela
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    startActivity(new Intent(Splash.this, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    //
                }
            }
        }.start();
    }
}