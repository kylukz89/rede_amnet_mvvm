package com.meuamericanet.redetelecom.repository;

import com.meuamericanet.redetelecom.model.UsuarioLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Arquivo com todos endpoints
 *
 * @author Igor Maximo
 * @date 14/09/2021
 */
public interface UsuarioAPI {
    // AUTENTICAÇÃO DO USUÁRIO
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @POST("Rotas.php?func=selectAutenticacaoCentralAssinante&user=k6NZWq95y9x2NMxF9rRg&pass=1MiTgbYI4jWCKn3RCdub")
    Call<List<UsuarioLogin>> selectAutenticacao(@Field("cpf_cnpj") String cpfCnpj);

    // RETORNA TODOS OS DADOS DO CADASTRO DO USUÁRIO
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    @POST("Rotas.php?func=selectDadosClientePorCPFCNPJ&user=k6NZWq95y9x2NMxF9rRg&pass=1MiTgbYI4jWCKn3RCdub")
    Call<List<UsuarioLogin>> selectDadosUsuarioPorDocumento(@Field("cpf_cnpj") String cpfCnpj);
}
