package com.meuamericanet.redetelecom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Classe responsável por gerenciar
 * a operação de login o usuário
 *
 * @author  Igor Maximo
 * @date   17/09/2021
 */
public class UsuarioLogin implements Serializable {

    @SerializedName("COD_CLIE")
//    @Expose
    private int codClie;
    @SerializedName("CNPJ_CPF_CLIE")
//    @Expose
    private String cpfCnpj;
    @SerializedName("RZAO_SOCL_CLIE")
//    @Expose
    private String nomeRazaoSocial;
    @SerializedName("ASSINANTE_SENHA_PORTAL")
//    @Expose
    private String senha;

    public int getCodClie() {
        return codClie;
    }

    public void setCodClie(int codClie) {
        this.codClie = codClie;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
