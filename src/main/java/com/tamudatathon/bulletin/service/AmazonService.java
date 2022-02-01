package com.tamudatathon.bulletin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.annotation.PostConstruct;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AmazonService {

    private AmazonS3 s3client;

    @Value("${app.api.s3.endpointUrl}")
    private String endpointUrl;

    @Value("${app.api.s3.bucketName}")
    private String bucketName;

    @Value("${app.api.s3.accessKeyId}")
    private String accessKeyId;

    @Value("${app.api.s3.secretKey}")
    private String secretKey;

    @Value("${app.api.s3.region}")
    private String region;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials 
            = new BasicAWSCredentials(this.accessKeyId, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    }

    public URL uploadFile(MultipartFile multipartFile) throws IOException, URISyntaxException {
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);
        URI fileUrl;
        fileUrl = new URI(endpointUrl + "/" + bucketName + "/" + fileName);
        uploadFileTos3bucket(fileName, file);
        file.delete();
        return fileUrl.toURL();
    }

    public String deleteFileFromS3Bucket(URL url) {
        String fileUrl = url.toString();
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        this.s3client.deleteObject(this.bucketName, fileName);
        return "Successfully deleted";
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        this.s3client.putObject(this.bucketName, fileName, file);
    }

    private File convertMultiPartToFile(MultipartFile file) 
    throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}