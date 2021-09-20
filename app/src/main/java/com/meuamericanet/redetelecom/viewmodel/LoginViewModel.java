package com.meuamericanet.redetelecom.viewmodel;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.meuamericanet.redetelecom.BR;
import com.meuamericanet.redetelecom.model.Usuario;
import com.meuamericanet.redetelecom.model.UsuarioLogin;
import com.meuamericanet.redetelecom.repository.UsuarioAPI;
import com.meuamericanet.redetelecom.repository.UsuarioJSON;
import com.meuamericanet.redetelecom.toolbox.Ferramentas;
import com.meuamericanet.redetelecom.toolbox.VariaveisGlobais;

import java.util.List;

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

    public void afterEmailTextChanged(CharSequence s) {
        cpfCnpjInformado.setValue(s.toString());
    }

    public void afterPasswordTextChanged(CharSequence s) {
        senhaInformada.setValue(s.toString());
    }

    /**
     * Botão responsável por chamar API de validação do login
     *
     * @author      Igor Maximo
     * @date        15/09/2021
     */
    public void setOnClickBotaoAcessar() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VariaveisGlobais.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Entidade carregada com os dados retornados da API
        UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);
        // POST - REQUEST
        final Call<List<UsuarioLogin>> usuarioCall = usuarioAPI.selectAutenticacao(cpfCnpjInformado.getValue());
        usuarioCall.enqueue(new Callback<List<UsuarioLogin>>() {
            @Override
            public void onResponse(Call<List<UsuarioLogin>> call, Response<List<UsuarioLogin>> response) {

                System.out.println("1=====> " + response.body().get(0).getCodClie());
                System.out.println("1=====> " + response.body().get(0).getNomeRazaoSocial());
                System.out.println("1=====> " + response.body().get(0).getCpfCnpj());
                System.out.println("1=====> " + response.body().get(0).getSenha());


                if (!response.body().get(0).getCpfCnpj().equals(cpfCnpjInformado.getValue())) {
                    setToastMessage("CPF/CNPJ não encontrado");
                    return;
                }

                if (response.body().get(0).getCpfCnpj().equals(cpfCnpjInformado.getValue()) && response.body().get(0).getCpfCnpj().equals(senhaInformada.getValue())) {
                    setToastMessage(successMessage);
                    return;
                }

                /*
                ////////////////////////////////////
                if (usuario.getValue().isInputDataValid()) {
                    setToastMessage(successMessage);
                } else {
                    setToastMessage(errorMessage);
                }
                */
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
                .build();
        // Entidade carregada com os dados retornados da API
        UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);
        // POST - REQUEST
        final Call<UsuarioLogin> usuarioCall = usuarioAPI.selectAutenticacao(user.getEmail());
        usuarioCall.enqueue(new Callback<UsuarioLogin>() {
            @Override
            public void onResponse(Call<UsuarioLogin> call, Response<UsuarioLogin> response) {
                System.out.println("1=====> " + response.toString());
                int statusCode = response.code();
                String cpfCnpj = response.toString();

                System.out.println("1=====> " + cpfCnpj);
                System.out.println("1=====> " + cpfCnpj.toString());

                Log.e("sdasd", cpfCnpj);
                //Log.w("response",new Gson().toJson(response));
                Log.w("response",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                //  recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<UsuarioLogin> call, Throwable t) {
                System.out.println("2=====> " + t.getMessage());
            }
        });
        */





    }
}