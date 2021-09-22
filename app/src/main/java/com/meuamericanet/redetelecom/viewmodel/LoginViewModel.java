package com.meuamericanet.redetelecom.viewmodel;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.meuamericanet.redetelecom.BR;
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

    private String successMessage = "Login was successful";
    private String errorMessage = "Email or Password not valid";

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

            boolean seJuridica = false;
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
            AlertDialog dialog = Ferramentas.setShowProgressDialog(context);
            // POST - REQUEST
            final Call<List<UsuarioLogin>> usuarioCall = usuarioAPI.selectAutenticacao(cpfCnpjInformado);
            usuarioCall.enqueue(new Callback<List<UsuarioLogin>>() {
                @Override
                public void onResponse(Call<List<UsuarioLogin>> call, Response<List<UsuarioLogin>> response) {
                    System.out.println("1=====> " + response.body().get(0).getCodClie());
                    System.out.println("1=====> " + response.body().get(0).getNomeRazaoSocial());
                    System.out.println("1=====> " + response.body().get(0).getCpfCnpj());
                    System.out.println("1=====> " + response.body().get(0).getSenha());
                    System.out.println(response.body().get(0).getCpfCnpj()  + " == " + (cpfCnpjInformado) + " && " + response.body().get(0).getSenha()  + " == " + (senhaInformada));

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
        if (!autenticacao.get(0).getCpfCnpj().equals(cpfCnpjInformado)) {
            setToastMessage("CPF não encontrado");
            return new Object[] {false, "CPF inválido"};
        }

        if (autenticacao.get(0).getCpfCnpj().equals(cpfCnpjInformado) && autenticacao.get(0).getSenha().equals(senhaInformada)) {
            setToastMessage(successMessage);
            return new Object[] {false, "CNPJ inválido"};
        }
        return new Object[] {true};
    }


}