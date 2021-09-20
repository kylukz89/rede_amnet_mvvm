package com.meuamericanet.redetelecom.toolbox.Config;

import java.util.Arrays;

/**
 * Variáveis Globais
 * Armazena IP do servidor e demais variáveis diversas
 * <p>
 * OBS: Suporte para multiempresas (Telecom e Conectividade)
 *
 * @author      Igor Maximo
 * @date        19/02/2019
 */
public abstract class VariaveisGlobais {
// http://187.95.0.22/producao/central/Rotas.php/?func=selectAutenticacaoCentralAssinante&user=k6NZWq95y9x2NMxF9rRg&pass=1MiTgbYI4jWCKn3RCdub
    //////////////////////
    // Controle de IPs
    public static String BASE_URL = "http://187.95.0.22/producao/central/";
    public static String BASE_URL_DOWNLOAD_ARQUIVOS = "http://187.95.0.22/producao/central/App/APIs/Downloader/FaturasDownloaded"; // central
    // Controle de IPs das IPTVs
    public static String IP_IPTV_REDE_TELECOM = "https://iptv.redetelecom.com.br/login";
    public static String IP_IPTV_REDE_TELECOM_NOGGIN_APP = "https://play.google.com/store/apps/details?id=com.mtvn.nogginintlgoogle&hl=pt_BR&gl=US";
    public static String IP_IPTV_REDE_TELECOM_PARAMOUNT_APP = "https://play.google.com/store/apps/details?id=com.cbs.ca";
    //////////////////////
    // Autenticação Back-end
    private static String USER = "k6NZWq95y9x2NMxF9rRg";
    private static String PASS = "1MiTgbYI4jWCKn3RCdub";
    public static String API_AUTH = "&user=" + USER + "&pass=" + PASS;
    public static volatile int TEMPO_MAX_EXPIRAR = 300; // 5 minutos - Tempo máximo de inatividade do app sem expirar
    //////////////////////
    // Controle de versionamento
    public static final int VERSAO_DB = 45;
    public static int[] VERSAO_APP_LOCAL = {12, 0, 1}; // Para exibição
    public static String VERSAO = Arrays.toString(VERSAO_APP_LOCAL).trim().replace(",", ".").replace("[", "").replace("]", ""); // Para exibição
    public static String VERSAO_NOME = "SIMETRA";
    //////////////////////
    // SQLite
    public static final String NOME_BANCO = "redetelecom.db";
    //////////////////////
    // Conexões por http // PADRÃO 7K
    public static int HTTP_CONNECTION_TIME = 100;
    public static int HTTP_URL_CONNECTION = 95000; // Usado para json
    ////////////////////
    // Mascara Data e Hora
    public static String MASCARA_DATA_HORA = "dd/MM/yyyy";
    public static int QUALIDADE_IMAGEM = 100;
    ////////////////////
    // Para interface gráfica
    public static int VIBRAR_TOQUE_MILI = 45;
}
