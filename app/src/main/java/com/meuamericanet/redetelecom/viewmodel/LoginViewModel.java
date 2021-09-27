package com.meuamericanet.redetelecom.viewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.meuamericanet.redetelecom.BR;
import com.meuamericanet.redetelecom.R;
import com.meuamericanet.redetelecom.model.Usuario;
import com.meuamericanet.redetelecom.model.UsuarioLogin;
import com.meuamericanet.redetelecom.repository.UsuarioAPI;
import com.meuamericanet.redetelecom.toolbox.Config.VariaveisGlobais;
import com.meuamericanet.redetelecom.toolbox.Ferramentas;
import com.meuamericanet.redetelecom.toolbox.Valid.ValidaCNPJ;
import com.meuamericanet.redetelecom.toolbox.Valid.ValidaCPF;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Classe ViewModel responsável pela tela de login
 *
 * @author Igor Maximo
 * @date 14/09/2021
 */
public class LoginViewModel extends BaseObservable {

    protected MutableLiveData<Usuario> usuario;

    private String cpfCnpjInformado;
    private String senhaInformada;



    @Bindable
    public String toastMessage = null;
    @Bindable
    public String campoCPFCNPJ = null;
    @Bindable
    public String campoSenha = null;
    @Bindable
    public String setErrorFieldCampoCPFCNPJ = null;
    @Bindable
    public String setErrorFieldCampoSenha = null;

    boolean seJuridica = false;

    // Construtor da classe
    public LoginViewModel() {
        usuario = new MutableLiveData<Usuario>();
    }

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    private void setErrorField(int campo) {
        notifyPropertyChanged(campo);
    }

    public void afterCPFCNPJTextChanged(CharSequence s) {
        cpfCnpjInformado = s.toString();
    }

    public void afterSenhaTextChanged(CharSequence s) {
        senhaInformada = s.toString();
    }

    /**
     * Insere máscara pertinente ao documento
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public void setMascara() {
        try {
            // Máscara de CPF/CNPJ
            if (Objects.requireNonNull(cpfCnpjInformado).length() == 11) {
                this.campoCPFCNPJ = Ferramentas.setMascaraCPF(cpfCnpjInformado);
                notifyPropertyChanged(BR.campoCPFCNPJ);
            } else {
                if (Objects.requireNonNull(cpfCnpjInformado).length() == 14) {
                    this.campoCPFCNPJ = Ferramentas.setMascaraCNPJ(cpfCnpjInformado);
                    notifyPropertyChanged(BR.campoCPFCNPJ);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Evento do edittext campoCPFCNPJ
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public EditText.OnFocusChangeListener getCampoCPFCNPJFocusListener() {
        return (v, hasFocus) -> {
            if (!hasFocus) {
                setMascara();
            }
        };
    }

    /**
     * Evento do edittext campoCPFCNPJ
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public EditText.OnKeyListener getCampoCPFCNPJKeyListener() {
        return (v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                this.campoCPFCNPJ = "";
                notifyPropertyChanged(BR.campoCPFCNPJ);
            }
            return false;
        };
    }


    /**
     * Botão responsável por chamar API de validação do login
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public void setOnClickBotaoAcessar(Context context) {
//        Ferramentas.setShowProgressDialog(context, false,true);
        try {
            // Valida se o campo está preenchido
            if (cpfCnpjInformado == null) {
                this.setErrorFieldCampoCPFCNPJ = "CPF inválido!";
                setErrorField(BR.setErrorFieldCampoCPFCNPJ);
                return;
            }
            // Valida se o campo está preenchido
            if (senhaInformada == null) {
                this.setErrorFieldCampoSenha = "Senha não informada!";
                setErrorField(BR.setErrorFieldCampoSenha);
                return;
            }


            if (cpfCnpjInformado.length() > 14) {
                seJuridica = true;
            }
            if (!seJuridica) {
                // Valida CPF
                if (!ValidaCPF.valida(cpfCnpjInformado.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
                    setToastMessage("CPF inválido");
                    return;
                }
            } else {
                // Valida CNPJ
                if (!ValidaCNPJ.valida(cpfCnpjInformado.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
                    setToastMessage("CNPJ inválido");
                    return;
                }
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(VariaveisGlobais.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            // Entidade carregada com os dados retornados da API
            UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);
            Dialog dialog = Ferramentas.setShowProgressDialog(context, true, true);
            // POST - REQUEST
            final Call<List<UsuarioLogin>> usuarioCall = usuarioAPI.selectAutenticacao(cpfCnpjInformado);
            usuarioCall.enqueue(new Callback<List<UsuarioLogin>>() {
                @Override
                public void onResponse(Call<List<UsuarioLogin>> call, Response<List<UsuarioLogin>> response) {



                    if (Boolean.parseBoolean(String.valueOf(setValidacao(response.body())[0]))) {
                        LinearLayout linearLayout = (LinearLayout) dialog.getWindow().findViewById(R.id.progressCorFundo);
                        ProgressBar mProgressDialog = (ProgressBar) dialog.getWindow().findViewById(R.id.progressBar);
                        TextView textViewProgress = (TextView) dialog.getWindow().findViewById(R.id.textViewProgressDialog);
                        ///////////////////////
                        Thread threadAnimacao = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ////////////////////////////////////////////
                                            // Background do processamento...
                                            linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.background_gradient_progresdialog_fullscreen_sucesso));
                                            linearLayout.setAlpha(0.5f);
                                            linearLayout.animate()
                                                    .alpha(1.5f)
                                                    .setDuration(1500)
                                                    .setStartDelay(750)
                                                    .start();
                                        }
                                    });


                                    try {
                                        sleep(600);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ////////////////////////////////////////////
                                            // Texto de processamento...
                                            final Animation animacaotextViewProgress = AnimationUtils.loadAnimation(context, R.anim.animate_in_out_enter);
                                            animacaotextViewProgress.setDuration(500);
                                            textViewProgress.startAnimation(animacaotextViewProgress);
                                            textViewProgress.setText("Sucesso");
                                            ////////////////////////////////////////////
                                            // Ícone do processamento...
                                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_ok);
                                            Rect bounds = mProgressDialog.getIndeterminateDrawable().getBounds(); // re-use bounds from current drawable
                                            mProgressDialog.setIndeterminateDrawable(drawable); // set new drawable
                                            mProgressDialog.getIndeterminateDrawable().setBounds(bounds); // set bounds to new drawable

                                            final Animation animacaomProgressDialog = AnimationUtils.loadAnimation(context, R.anim.animate_shrink_enter);
                                            animacaomProgressDialog.setDuration(500);
                                            textViewProgress.startAnimation(animacaomProgressDialog);

                                            mProgressDialog.startAnimation(animacaomProgressDialog);
                                        }
                                    });


                                    try {
                                        sleep(1700);
                                        dialog.dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }




                                } catch (Exception e) {
                                    System.out.println("==================> " + e.toString());
                                }
                            }
                        };
                        threadAnimacao.start();
                    } else {
                        LinearLayout linearLayout = (LinearLayout) dialog.getWindow().findViewById(R.id.progressCorFundo);
                        ProgressBar mProgressDialog = (ProgressBar) dialog.getWindow().findViewById(R.id.progressBar);
                        TextView textViewProgress = (TextView) dialog.getWindow().findViewById(R.id.textViewProgressDialog);
                        ///////////////////////
                        Thread threadAnimacao = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ////////////////////////////////////////////
                                            // Background do processamento...
                                            linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.background_gradient_progresdialog_fullscreen_erro));
                                            linearLayout.setAlpha(0.5f);
                                            linearLayout.animate()
                                                    .alpha(1.5f)
                                                    .setDuration(1500)
                                                    .setStartDelay(750)
                                                    .start();
                                        }
                                    });


                                    try {
                                        sleep(600);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ////////////////////////////////////////////
                                            // Texto de processamento...
                                            final Animation animacaotextViewProgress = AnimationUtils.loadAnimation(context, R.anim.animate_in_out_enter);
                                            animacaotextViewProgress.setDuration(500);
                                            textViewProgress.startAnimation(animacaotextViewProgress);
                                            textViewProgress.setText("Erro");
                                            ////////////////////////////////////////////
                                            // Ícone do processamento...
                                            Drawable drawable = context.getResources().getDrawable(R.drawable.ic_erro);
                                            Rect bounds = mProgressDialog.getIndeterminateDrawable().getBounds(); // re-use bounds from current drawable
                                            mProgressDialog.setIndeterminateDrawable(drawable); // set new drawable
                                            mProgressDialog.getIndeterminateDrawable().setBounds(bounds); // set bounds to new drawable

                                            final Animation animacaomProgressDialog = AnimationUtils.loadAnimation(context, R.anim.animate_shrink_enter);
                                            animacaomProgressDialog.setDuration(500);
                                            textViewProgress.startAnimation(animacaomProgressDialog);

                                            mProgressDialog.startAnimation(animacaomProgressDialog);
                                        }
                                    });


                                    try {
                                        sleep(1700);
                                        dialog.dismiss();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }



                                } catch (Exception e) {
                                    System.out.println("==================> " + e.toString());
                                }
                            }
                        };
                        threadAnimacao.start();
                    }




                }

                @Override
                public void onFailure(Call<List<UsuarioLogin>> call, Throwable t) {
//                    dialog.dismiss();
                }
            });

        /*
        // Prepara interface para requisição da API de usuário autenticação
        Gson gson = new GsonBuilder().registerTypeAdapter(Usuario.class, new UsuarioJSON()).create();
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(VariaveisGlobais.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build(); */
        } catch (Exception e) {

        }
    }

    private Object[] setValidacao(List<UsuarioLogin> autenticacao) {
        if (this.seJuridica) {
            if (autenticacao.get(0).getCpfCnpj().equals(Ferramentas.setExtrairNumeroString(cpfCnpjInformado))) {
                setToastMessage("CNPJ inválido");
                return new Object[] {false, "CNPJ inválido"};
            }
        } else {
            if (!autenticacao.get(0).getCpfCnpj().equals(Ferramentas.setExtrairNumeroString(cpfCnpjInformado))) {
                setToastMessage("CPF não encontrado");
                return new Object[] {false, "CPF inválido"};
            }
        }

        if (!autenticacao.get(0).getSenha().equals(senhaInformada)) {
            setToastMessage("Senha incorreta");
            return new Object[] {false, "Senha incorreta"};
        }

        return new Object[] {true};
    }


}