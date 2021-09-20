package com.meuamericanet.redetelecom.repository;


import com.meuamericanet.redetelecom.toolbox.Config.VariaveisGlobais;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Manipulador do Retrofit caller
 *
 * OBS: Generaliza as chamadas no backend
 * através da lib retrofit + gson
 *
 * @author  Igor Maximo
 * @date    16/09/2021
 */
public class RetrofitCaller {

    // Endpoint da API
    private String func = "";
    // Usuário de autenticação da API
    private String user = "";
    // Senha do usuário de autenticação da API
    private String pass = "";
    private Object entidade;

    // Construtor da classe
    public <T> RetrofitCaller(Class classeEntidade, Class interfaceAPI, Class classeJSON) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VariaveisGlobais.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Entidade carregada com os dados retornados da API
//        UsuarioAPI usuarioAPI = retrofit.create(UsuarioAPI.class);

        interfaceAPI = retrofit.create(interfaceAPI.getClass());

        // POST - REQUEST
//        final Call<List<T>> callList = setExecutarMetodo(classeEntidade,"selectAutenticacao", new Object[] {"37941115832"}, new Class[] {String.class});



        final Call<List<Object>> callList;
        callList = (Call<List<Object>>) setMetodo(
                classeEntidade,
                "selectAutenticacao",
                new Class[] {String.class},
                new Object[] {"37941115832"}
        );



        callList.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                System.out.println("1=====> " + response.body().get(0).toString());
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                System.out.println("2=====> " + t.getMessage());
            }
        });


    /* // Prepara interface para requisição da API de usuário autenticação
        Gson gson = new GsonBuilder().registerTypeAdapter(classeEntidade.getClass(), classeJSON).create();
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit
                .Builder()
                .baseUrl(VariaveisGlobais.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();*/
    }



    private Object retorno = null;

    private Object setMetodo(Object classe, String nomeMetodo, Class[] tiposParametros,  Object[] valoresParametros) {
        Object retorno = null;
        try {
            retorno = setMetodoExecutar(
                    classe,
                    classe.getClass().getMethod(nomeMetodo, tiposParametros),
                    valoresParametros
            );
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return retorno;
    }
    public Object setMetodoExecutar(Object object, Method metodo, Object[] valoresParametros) {
        Object retorno = null;
        try {
            retorno = metodo.invoke(object, valoresParametros);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return retorno;
    }



}
