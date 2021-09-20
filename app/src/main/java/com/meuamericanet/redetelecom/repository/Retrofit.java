package com.meuamericanet.redetelecom.repository;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.meuamericanet.redetelecom.model.Usuario;
import com.meuamericanet.redetelecom.toolbox.VariaveisGlobais;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Manipulador do Retrofit caller
 *
 * OBS: Generaliza as chamadas no backend
 * através da lib retrofit + gson
 *
 * @author  Igor Maximo
 * @date    16/09/2021
 */
public class Retrofit {

    // Endpoint da API
    private String func = "";
    // Usuário de autenticação da API
    private String user = "";
    // Senha do usuário de autenticação da API
    private String pass = "";
    // Base URl do link da API
    private String baseURL = "";
    private Object entidade;

    // Construtor da classe
    public <T> Retrofit(Class classeEntidade, Class interfaceAPI, Class classeJSON) {
//        this.entidade = classe;
//        this.func = endpoint;








//        Object retorno = new ExecutadorMetodoAssincrono(MainActivity.this).setExecutarMetodo(
//                        new Avisos(),
//                        "getAlertaMassivoTeste",
//                        new Object[] {"123", 555, true},
//                        new Class[] {String.class, int.class, boolean.class},
//                        MainActivity.this
//                );







        // Prepara interface para requisição da API de usuário autenticação
        Gson gson = new GsonBuilder().registerTypeAdapter(classeEntidade.getClass(), classeJSON).create();
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit
                .Builder()
                .baseUrl(VariaveisGlobais.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // Entidade carregada com os dados retornados da API
//        UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);




        // POST - REQUEST
//        final Call<T> callLuxury = interfaceAPI.selectAutenticacao(user.getEmail());
//        final Call<T> callLuxury = interfaceAPI.getMethod();


//        callLuxury.enqueue(new Callback<T>() {
//            @Override
//            public void onResponse(Call<T> call, Response<T> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<T> call, Throwable t) {
//                System.out.println("=====> " + t.getMessage());
//            }
//        });




        /**
         * Executa qualquer método de forma assíncrona com loader
         *
         * @param instanciaClasse       classe que contém os métodos
         * @param nomeMetodo            Nome do método que deve ser executado
         * @param parametros            Lista de parâmetros que são executados no método informado
         * @param tiposParametros       Lista dos tipos de cada parâmetro informado
         * @param context               Activity contexto que foi usado e deverá ser exibido o spinner
         * @author      Igor Maximo
         * @date        01/03/2021
         */
    /*    public Object setExecutarMetodo(Object instanciaClasse, String nomeMetodo, Object[] parametros, Object[] tiposParametros, Context
        context) {
//        Object retorno = null;
            // Execução do método
            ArrayList<Object> listaParamFunc = new ArrayList<>();
            // Objeto instância da classe
            listaParamFunc.add(0, instanciaClasse);
            // Nome do método
            listaParamFunc.add(1, nomeMetodo);
            // Tipos de cada parâmetro de entrada
            listaParamFunc.add(2, tiposParametros);
            // Parâmetros de entrada
            listaParamFunc.add(3, parametros);
            new ExecutadorMetodoAssincrono(context).execute(listaParamFunc);
            System.out.println("retorno===123" + this.retorno);
//        try {
//            retorno = new ExecutadorMetodoAssincrono(context).execute(listaParamFunc).get();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
            return this.retorno;
        }
        */

    }

}
