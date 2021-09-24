package com.meuamericanet.redetelecom.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.meuamericanet.redetelecom.R;
import com.meuamericanet.redetelecom.databinding.ActivityLoginBinding;
import com.meuamericanet.redetelecom.toolbox.Anim.Animatoo;
import com.meuamericanet.redetelecom.toolbox.Anim.CustomBounceInterpolator;
import com.meuamericanet.redetelecom.toolbox.Ferramentas;
import com.meuamericanet.redetelecom.viewmodel.LoginViewModel;

/**
 * Classe de login do app
 *
 * @author Igor Maximo
 * @date 10/09/2021
 */
public class LoginActivity extends AppCompatActivity {

    private final Ferramentas ferramenta = new Ferramentas();

    private static ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide(); // remove barra de título
        getWindow().getDecorView();
        ///////////////////////////// INDISPENSÁVEL PARA CONEXÃO /////////////////////////////////////
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //////////////////////////////////////////// //////////////////////////////////////////////////
        // Fade
        Animatoo.animateFade(LoginActivity.this);
        // Responsável pelo biding dos dados da tela de login
        ActivityLoginBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activityMainBinding.setViewModel(new LoginViewModel());
        activityMainBinding.executePendingBindings();
        // Animação de interpolação da esquerda para direita
        startaAnimacao(findViewById(R.id.cardViewLogin), findViewById(R.id.textViewTorneseCliente));
        TextView textViewEsqueciSenha = (TextView) findViewById(R.id.textViewProblemasAcesso);
        // Sombreamento dos elementos
        float nivelSombra = 15f;
        float dx = 0f;
        float dy = 2.8f;
        int corSombra = Color.parseColor("#09000000");
        this.ferramenta.setSombraTextView(findViewById(R.id.textViewTitulo), nivelSombra, Color.GRAY, dx, dy);
        this.ferramenta.setSombraTextView(findViewById(R.id.tituloEsqueciSenha), nivelSombra, Color.GRAY, dx, dy);
        this.ferramenta.setSombraTextView(textViewEsqueciSenha, nivelSombra, corSombra, dx, dy);
        this.ferramenta.setSombraTextView(findViewById(R.id.textViewTorneseCliente), nivelSombra, corSombra, dx, dy);
        this.ferramenta.setSombraTextView(textViewEsqueciSenha, nivelSombra, corSombra, dx, dy);
//        this.ferramenta.setSombraTextView(findViewById(R.id.slogan), 3f, Color.WHITE, dx, dy);
        this.ferramenta.setSombraEditText(findViewById(R.id.campoCPFCNPJ), nivelSombra, corSombra, dx, dy);
        this.ferramenta.setSombraEditText(findViewById(R.id.campoSenha), nivelSombra, corSombra, dx, dy);
        this.ferramenta.setSombraEditText(findViewById(R.id.editTextCampoCPFEsqueciSenha), nivelSombra, corSombra, dx, dy);
        // Bottom sheet de esqueci minha senha
        textViewEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Esconde bottomsheet
                setExibeColapsaBottomSheet(1);
            }
        });
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null) {
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }


    @BindingAdapter({"setErrorField"})
    public static void setErrorField(EditText editText, String message) {
        if (message != null) {
            editText.setError(message);
        }
    }

    /**
     * Animações do cardview de login
     *
     * @author Igor Maximo
     * @date 14/09/2021
     */
    public void startaAnimacao(CardView cardView, TextView textViewTorneseCliente) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(300);
                    // Executa animação de entrada do cardview
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cardView.setVisibility(View.VISIBLE);
                            final Animation animacaoCardView = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right);
                            animacaoCardView.setInterpolator(new CustomBounceInterpolator(0.5, 30));
                            animacaoCardView.setDuration(200);
                            cardView.startAnimation(animacaoCardView);
                        }
                    });
                    // Executa animação de entrada do botão torna-se cliente
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewTorneseCliente.setVisibility(View.VISIBLE);
                            final Animation animacaoTorneseCliente = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.enter_from_right);
                            animacaoTorneseCliente.setInterpolator(new CustomBounceInterpolator(0.5, 30));
                            animacaoTorneseCliente.setDuration(350);
                            textViewTorneseCliente.startAnimation(animacaoTorneseCliente);
                        }
                    });
                } catch (Exception e) {
                    //
                }
            }
        }.start();
    }

    private BottomSheetBehavior mBottomSheetBehavior;

    /**
     * Altera o state da bottom sheet
     *
     * @author Igor Maximo <igormaximo_1989@hotmail.com>
     * @date 01/05/2020
     */
    private void setExpandeColapsaBottomSheet(int state, int bottomSheetAcao) {
        View bottomSheet = null;
        switch (bottomSheetAcao) {
            case 0:
//                bottomSheet = findViewById(R.id.bottom_sheet_cadastra_pin);
                break;
            case 1:
                bottomSheet = findViewById(R.id.bottom_sheet_esqueci_senha);
                break;
        }
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(state);
        bottomSheet.requestLayout();
        bottomSheet.invalidate();
    }


    /**
     * Exibe a roleta de produtos
     */
    public void setExibeColapsaBottomSheet(int bottomSheetAcao) {
        try {
            View bottomSheet = null;
            switch (bottomSheetAcao) {
                case 0:
//                     bottomSheet = findViewById(R.id.bottom_sheet_cadastra_pin);
                    break;
                case 1:
                    bottomSheet = findViewById(R.id.bottom_sheet_esqueci_senha);
                    break;
            }

            // Some existing RelativeLayout from your layout xml
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.my_relative_layout);
            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);


            switch (mBottomSheetBehavior.getState()) {
                case BottomSheetBehavior.STATE_COLLAPSED:
                    rl.setVisibility(View.VISIBLE);
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    rl.setVisibility(View.GONE);
                    bottomSheet.requestLayout();
                    bottomSheet.invalidate();
                    break;
            }

            mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    switch (newState) {
                        case BottomSheetBehavior.STATE_COLLAPSED:
                            rl.setVisibility(View.GONE);
                            break;
                        case BottomSheetBehavior.STATE_EXPANDED:
                            rl.setVisibility(View.VISIBLE);
                            break;
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // React to dragging events
                }
            });


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}