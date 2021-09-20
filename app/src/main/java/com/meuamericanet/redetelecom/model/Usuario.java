package com.meuamericanet.redetelecom.model;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Entidade responsável pelo gerenciamento
 * de tudo referente ao usuário
 *
 * @author      Igor Maximo
 * @date        10/09/1989
 */
public class Usuario {

    // Para demais serviços
    private String status;
    private String codigo;
    private String nome;
    private String cpfCnpj;
    private String endereco;
    private String bairro;
    private String numero;
    private String cep;
    private String cidade;
    private String uf;
    private String fone;
    private String celular;
    private String email;
    private String senha;
    // Para Autenticação
    private String tipoCliente;
    // Contato
    private String celular1;
    private String celular2;
    private String celular3;
    private String celular4;
    // Paramount e Noggin
//    private String userApp;
//    private String passApp;
    // Para cartão recorrente
//    private ArrayList<CartaoRecorrente> cartoesRecorrentes;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getCelular1() {
        return celular1;
    }

    public void setCelular1(String celular1) {
        this.celular1 = celular1;
    }

    public String getCelular2() {
        return celular2;
    }

    public void setCelular2(String celular2) {
        this.celular2 = celular2;
    }

    public String getCelular3() {
        return celular3;
    }

    public void setCelular3(String celular3) {
        this.celular3 = celular3;
    }

    public String getCelular4() {
        return celular4;
    }

    public void setCelular4(String celular4) {
        this.celular4 = celular4;
    }

    // Validação interna da entidade
    public boolean isInputDataValid() {
        return !TextUtils.isEmpty(getEmail()) && Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches() && getSenha().length() > 5;
    }

}
