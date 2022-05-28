package com.alkemy.ong.auth.service;

import com.alkemy.ong.auth.model.vm.Asset;
import com.alkemy.ong.exception.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3ServiceImpl implements IS3Service {
    private final String BUCKET = "aimbucket";

    @Autowired
    private AmazonS3 s3Client;

    @Override
    public String putObject(MultipartFile file) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());


        if (file.isEmpty()) {
            throw new NullPointerException("La imagen esta vacia");
        }

        if (validFileExtensions(extension)) {

            String key = String.format("%s_%s", System.currentTimeMillis(), file.getOriginalFilename().replace(" ", ""));

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, key, file.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead); //Dar permiso de acceso publico para ver por url solo lectura

                s3Client.putObject(putObjectRequest);
                return key;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new NotFoundException("Formanto no valido");
        }

    }

    @Override
    public List<String> getObjectsFromS3() {
        ListObjectsV2Result result = s3Client.listObjectsV2(BUCKET);
        List<S3ObjectSummary> objects = result.getObjectSummaries();
        List<String> list = objects.stream().map(item -> {
            return item.getKey();
        }).collect(Collectors.toList());
        return list;
    }

    @Override
    public Asset getObject(String key) {
        S3Object s3Object = s3Client.getObject(BUCKET, key);
        ObjectMetadata metadata = s3Object.getObjectMetadata();
        try {
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            return new Asset(bytes, metadata.getContentType());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        S3Object object = s3Client.getObject(BUCKET, key);
        return object.getObjectContent();
    }


    @Override
    public void deleteObject(String key) {
        s3Client.deleteObject(BUCKET, key);
    }

    @Override
    public String getObjectUrl(String key) {
        return String.format("https://%s.s3.amazonaws.com/%s", BUCKET, key);
    }

    private boolean validFileExtensions(String extension) {
        String a[] = {"jpg", "jpeg", "bmp", "gif", "png"};
        return Arrays.asList(a).contains(extension);
    }

}
