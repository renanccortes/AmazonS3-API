/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.trix.amazon.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Renan
 */
public class AwsAmazon implements AwsService {

    private static final long serialVersionUID = -5936744659510567736L;

    private AmazonS3 s3Client;
    private final AWSCredentials credentials;
    private final Regions region;


    protected AwsAmazon(String id, String key, Regions region) throws AwsExcpetions {
        credentials = new BasicAWSCredentials( id, key );
        this.region = region;
        conectar();
    }

    public boolean isConectado() {
        return s3Client != null;
    }

    @Override
    public void conectar() throws AwsExcpetions {
        if (s3Client != null) {
            return;
        }

        s3Client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        verificaConexao();
    }


    /**
     * VERIFICA SE FOI CONECTADO COM SUCESSO, CASO O CLIENT FOR NULL SOBE UMA
     * EXCECAO, A SER TRATADO EM UMA CAMADA MAIS ACIMA.
     *
     * @throws AwsExcpetions
     */
    private void verificaConexao() throws AwsExcpetions {
        if (s3Client == null) {
            throw new AwsExcpetions("Falha ao acessar repositorio de documentos, por favor tente mais tarde.");
        }
    }

    @Override
    public boolean enviarArquivo(String chaveDoArquivo, String bucket, File arquivo) throws AwsExcpetions {

        try {
            verificaConexao();

            s3Client.putObject(new PutObjectRequest(bucket, chaveDoArquivo, arquivo));

            return true;
        } catch (AmazonClientException e) {
            throw new AwsExcpetions("Ocorreu uma falha ao enviar o arquivo. \nDetalhe do erro: " + e.getMessage());
        }
    }

    @Override
    public boolean enviarArquivo(String chaveDoArquivo, String bucket, byte[] arquivo) throws AwsExcpetions {
        if (s3Client == null || arquivo == null) {
            return false;
        }
        try {

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentLength(arquivo.length);
            s3Client.putObject(new PutObjectRequest(bucket, chaveDoArquivo, new ByteArrayInputStream(arquivo), metaData));
            return true;
        } catch (AmazonClientException | NullPointerException e) {
            throw e;
        }
    }

    @Override
    public byte[] receberArquivo(String chaveDoArquivo, String bucket) throws AwsExcpetions {
        S3Object object = null;
        try {
            verificaConexao();
            object = s3Client.getObject(new GetObjectRequest(bucket, chaveDoArquivo));

            return Utils.getBytesFromInputStream(object.getObjectContent());

        } catch (AmazonClientException e) {
            throw new AwsExcpetions("Ocorreu uma falha ao receber o arquivo.\nDetalhe do erro: " + e.getMessage());
        } catch (IOException e) {
            throw new AwsExcpetions("Ocorreu uma falha ao receber o arquivo.\nDetalhe do erro: " + e.getMessage());
        } finally {
            try {
                object.close();
            } catch (Exception ex) {
                Logger.getLogger(br.com.trix.amazon.s3.AwsAmazon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean exlcuirArquivo(String chaveDoArquivo, String bucket) throws AwsExcpetions {
        try {
            verificaConexao();

            s3Client.deleteObject(new DeleteObjectRequest(bucket, chaveDoArquivo));

            return true;
        } catch (AmazonClientException e) {
            throw new AwsExcpetions("Ocorreu uma falha ao receber o arquivo.\nDetalhe do erro: " + e.getMessage());
        }
    }

    @Override
    public boolean enviarArquivoMultPart(String chaveDoArquivo, String bucket, File arquivo) {
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());

        PutObjectRequest request = new PutObjectRequest(bucket, chaveDoArquivo, arquivo);

        request.setGeneralProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                System.out.println("Transferred bytes: "
                        + progressEvent.getBytesTransferred());
            }
        });

          Upload upload = tm.upload(request);

        try {
            // You can block and wait for the upload to finish
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
            System.out.println("Unable to upload file, upload aborted.");
            amazonClientException.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(br.com.trix.amazon.s3.AwsAmazon.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public boolean isExisteArquivo(String chaveArquivo, String bucket) {
        try {
            verificaConexao();

            boolean retorno = s3Client.doesObjectExist(bucket, chaveArquivo);
            return retorno;
        } catch (AwsExcpetions ex) {
            Logger.getLogger(br.com.trix.amazon.s3.AwsAmazon.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
