/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.trix.amazon.s3;

/**
 *
 * @author Renan
 */
public class AwsExcpetions extends Exception {

    /**
     * Creates a new instance of
     * <code>AlcancouLimiteDocumentosException</code> without detail message.
     */
    public AwsExcpetions() {
    }

    /**
     * Constructs an instance of
     * <code>AlcancouLimiteDocumentosException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public AwsExcpetions(String msg) {
        super(msg);
    }
}
