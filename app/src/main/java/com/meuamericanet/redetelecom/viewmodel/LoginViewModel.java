package com.meuamericanet.redetelecom.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.meuamericanet.redetelecom.BR;
import com.meuamericanet.redetelecom.R;
import com.meuamericanet.redetelecom.model.Usuario;
import com.meuamericanet.redetelecom.model.UsuarioLogin;
import com.meuamericanet.redetelecom.repository.UsuarioAPI;
import com.meuamericanet.redetelecom.toolbox.Config.VariaveisGlobais;
import com.meuamericanet.redetelecom.toolbox.Ferramentas;
import com.meuamericanet.redetelecom.toolbox.Valid.ValidaCNPJ;
import com.meuamericanet.redetelecom.toolbox.Valid.ValidaCPF;
import com.meuamericanet.redetelecom.view.LoginActivity;

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

    private MutableLiveData<String> cpfCnpjInformado;
    private MutableLiveData<String> senhaInformada;

    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";

    @Bindable
    public String toastMessage = null;
    @Bindable
    public String campoCPFCNPJ = null;
    @Bindable
    public String campoSenha = null;
    @Bindable
    public String setErrorField = null;

    // Construtor da classe
    public LoginViewModel() {
        usuario = new MutableLiveData<Usuario>();

        cpfCnpjInformado = new MutableLiveData<String>();
        senhaInformada = new MutableLiveData<String>();
    }

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    public void afterCPFCNPJTextChanged(CharSequence s) {
        cpfCnpjInformado.setValue(s.toString());
    }

    public void afterSenhaTextChanged(CharSequence s) {
        senhaInformada.setValue(s.toString());
    }

    /**
     * Insere máscara pertinente ao documento
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public void setMascara() {
        // Máscara de CPF/CNPJ
        if (Objects.requireNonNull(cpfCnpjInformado.getValue()).length() == 11) {
            this.campoCPFCNPJ = Ferramentas.mascaraCPF(cpfCnpjInformado.getValue());
            notifyPropertyChanged(BR.campoCPFCNPJ);
        } else {
            if (Objects.requireNonNull(cpfCnpjInformado.getValue()).length() == 14) {
                this.campoCPFCNPJ = Ferramentas.mascaraCPF(cpfCnpjInformado.getValue());
                notifyPropertyChanged(BR.campoCPFCNPJ);
            }
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
        try {
            if (cpfCnpjInformado.getValue() == null) {
                notifyPropertyChanged(BR.setErrorField);
                return;
            }

            boolean seJuridica = false;
            if (cpfCnpjInformado.getValue().length() > 14) {
                seJuridica = true;
            }
            if (!seJuridica) {
                // Valida CPF
                if (!ValidaCPF.valida(cpfCnpjInformado.getValue().replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
                    setToastMessage("CPF inválido");
                    return;
                }
            } else {
                // Valida CNPJ
                if (!ValidaCNPJ.valida(cpfCnpjInformado.getValue().replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]", "").replaceAll("[:]", "").replaceAll("[)]", ""))) {
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
            AlertDialog dialog = Ferramentas.setShowProgressDialog(context);
            // POST - REQUEST
            final Call<List<UsuarioLogin>> usuarioCall = usuarioAPI.selectAutenticacao(cpfCnpjInformado.getValue());
            usuarioCall.enqueue(new Callback<List<UsuarioLogin>>() {
                @Override
                public void onResponse(Call<List<UsuarioLogin>> call, Response<List<UsuarioLogin>> response) {
                    System.out.println("1=====> " + response.body().get(0).getCodClie());
                    System.out.println("1=====> " + response.body().get(0).getNomeRazaoSocial());
                    System.out.println("1=====> " + response.body().get(0).getCpfCnpj());
                    System.out.println("1=====> " + response.body().get(0).getSenha());
                    System.out.println(response.body().get(0).getCpfCnpj()  + " == " + (cpfCnpjInformado.getValue()) + " && " + response.body().get(0).getSenha()  + " == " + (senhaInformada.getValue()));

                    if (Boolean.parseBoolean(String.valueOf(setValidacao(response.body())[0]))) {

                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<UsuarioLogin>> call, Throwable t) {
                    System.out.println("2=====> " + t.getMessage());
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
        if (!autenticacao.get(0).getCpfCnpj().equals(cpfCnpjInformado.getValue())) {
            setToastMessage("CPF não encontrado");
            return new Object[] {false, "CPF inválido"};
        }

        if (autenticacao.get(0).getCpfCnpj().equals(cpfCnpjInformado.getValue()) && autenticacao.get(0).getSenha().equals(senhaInformada.getValue())) {
            setToastMessage(successMessage);
            return new Object[] {false, "CNPJ inválido"};
        }
        return new Object[] {true};
    }


}