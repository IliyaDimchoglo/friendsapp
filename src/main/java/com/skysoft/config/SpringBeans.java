package com.skysoft.config;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class SpringBeans {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public AmazonS3 amazons3(){
        return AmazonS3Client.builder()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();
    }

}
