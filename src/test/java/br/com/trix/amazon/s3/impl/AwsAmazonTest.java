package br.com.trix.amazon.s3.impl;

import br.com.trix.amazon.s3.AwsExcpetions;
import br.com.trix.amazon.s3.AwsService;
import br.com.trix.amazon.s3.Credentials;
import br.com.trix.amazon.s3.FactoryS3;
import com.amazonaws.regions.Regions;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwsAmazonTest {

    private AwsService awsAmazon;
    private final String file = "D:/Cacert";


    @Before
    public void setUp() throws AwsExcpetions {
        awsAmazon = FactoryS3.create(Credentials.ID, Credentials.KEY, Regions.SA_EAST_1);
    }

    @Test
    public void teste1EnviarArquivoTest() {
        try {
            boolean arquivoTeste = awsAmazon.enviarArquivo("arquivoTeste", "s3gedtest", new File(file));
            assertTrue(arquivoTeste);
        } catch (AwsExcpetions e) {
            fail(e.getMessage());
        }

    }


    @Test
    public void teste2ConsultaArquivoTest() {

        boolean arquivoTeste = awsAmazon.isExisteArquivo("arquivoTeste", "s3gedtest");
        assertTrue(arquivoTeste);


    }

    @Test
    public void teste3ExcluirArquivoTest() {
        try {
            boolean arquivoTeste = awsAmazon.exlcuirArquivo("arquivoTeste", "s3gedtest");
            assertTrue(arquivoTeste);
        } catch (AwsExcpetions e) {
            fail(e.getMessage());
        }

    }
}
