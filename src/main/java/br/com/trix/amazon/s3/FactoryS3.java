package br.com.trix.amazon.s3;

import com.amazonaws.regions.Regions;

public class FactoryS3 {


    public static AwsService create(String id, String key, Regions region) throws AwsExcpetions {
        return new AwsAmazon(id, key, region);
    }
}
