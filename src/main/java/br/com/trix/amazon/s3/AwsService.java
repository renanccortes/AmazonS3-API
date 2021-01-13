/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.trix.amazon.s3;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author Renan
 */
public interface AwsService extends Serializable {
    
    /**
     * METODO QUE FAZ A CONEXAO COM O AWS (GERA UMA EXCECAO PERSONALIZADA CASO NAO CONSIGA CONECTAR)
     * @throws AwsExcpetions
     */
    public void conectar() throws AwsExcpetions;
    
    /**
     *  METODO QUE ENVIA O ARQUIVO AO SERVIDOR AWS (GERA UMA EXCECAO CASO O ARQUIVO NAO TENHA SIDO ENVIADO )
     * @param chaveDoArquivo
     * CHAVE OU ID DO ARQUIVO A SER GRAVADO NO SERVIDOR, ESSA CHAVE SERVE PARA RECUPERARMOS 
     * OS DADOS DEPOIS
     * @param bucket
     * BUCKET E A PASTA QUE SERA BUSCADA ESSA CHAVE(ARQUIVO)
     * @param arquivo (FILE)
     * ARQUIVO A SER ENVIADO FILE
     * @return TRUE = SUCESS, FALSE = FALHA
     * @throws AwsExcpetions
     */
    public boolean enviarArquivo(String chaveDoArquivo, String bucket, File arquivo) throws AwsExcpetions;
    
   /**
     *  METODO QUE ENVIA O ARQUIVO AO SERVIDOR AWS (GERA UMA EXCECAO CASO O ARQUIVO NAO TENHA SIDO ENVIADO )
     * @param chaveDoArquivo
     * CHAVE OU ID DO ARQUIVO A SER GRAVADO NO SERVIDOR, ESSA CHAVE SERVE PARA RECUPERARMOS 
     * OS DADOS DEPOIS
     * @param bucket
     * BUCKET E A PASTA QUE SERA BUSCADA ESSA CHAVE(ARQUIVO)
     * @param arquivo ( INPUTSTREAM )
     * ARQUIVO A SER ENVIADO INPUTSTREAM
     * @return TRUE = SUCESS, FALSE = FALHA
     * @throws AwsExcpetions
     */
    public boolean enviarArquivo(String chaveDoArquivo, String bucket, byte[] arquivo) throws AwsExcpetions;
    
    /**
     * ENVIA UM ARQUIVO AO AWS ATRAVEZ DO MULTIPART PODENDO GERENCIAR UM PROGRESS BAR
     * @param chaveDoArquivo
     * @param bucket
     * @param arquivo
     * @return
     */
    public boolean enviarArquivoMultPart(String chaveDoArquivo, String bucket, File arquivo);
    
    /**
     *  METODO QUE RECEBE UM ARQUIVO DO SERVIDOR AWS (CASO NAO ENCONTRE OU AJA ALGUMA FALHA A EXCECCAO E DISPARADA)
     * @param chaveDoArquivo 
     * ID/CHAVE DO ARQUIVO A SER BAIXADO
     * @param bucket
     * CAMINHO/PASTA DO ARQUIVO A SER BUSCADO E BAIXADO
     * @return RETORNA UM INPUTSTREAM 
     * @throws AwsExcpetions
     */
    public byte[] receberArquivo(String chaveDoArquivo, String bucket) throws AwsExcpetions;
    
    /**
     *
     * @param chaveDoArquivo
     * ID/CHAVE DO ARQUIVO A SER EXCLUIDO
     * @param bucket
     * CAMINHO/PASTA DO ARQUIVO A SER EXLUIDO
     * @return 
     * @throws AwsExcpetions
     */
    public boolean exlcuirArquivo(String chaveDoArquivo, String bucket) throws AwsExcpetions;

    /** INFORMA SE O ARQUIVO EXISTE NO BUCKET
     * @param chaveArquivo
     * @param bucket
     * @return
     */
    public boolean isExisteArquivo(String chaveArquivo, String bucket);
    
}
