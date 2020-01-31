package com.skysoft.service.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.skysoft.exception.BadRequestException;
import com.skysoft.exception.NotFoundException;
import com.skysoft.service.aws.api.AmazonS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3ServiceImpl implements AmazonS3Service {

    private final AmazonS3 amazonS3;

    @Value("${amazon-client.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadAvatar(MultipartFile avatar, AvatarType avatarType) {
        String uniqueAvatarName = getUniqueAvatarName();
        String avatarKey = generateNumberIconKey(uniqueAvatarName, avatarType.getType());
        saveAvatar(bucketName, avatar);
        return getUrl(bucketName, avatarKey).orElseThrow(() -> new NotFoundException("Avatar url not found"));
    }

    private void saveAvatar(String fileObjKeyName, MultipartFile avatar) {
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileObjKeyName, convertMultiPartToFile(avatar), objectMetadata(avatar))
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            log.info("Successfully saved avatar.");
        } catch (AmazonClientException e) {
            log.error("[x] Bad request for save avatar with message: {}.", e.getMessage());
            throw new BadRequestException("Bad request for save avatar.");
        }
    }

    private String generateNumberIconKey(String uniqueAvatarName, String contentType) {
        return uniqueAvatarName + "." + contentType;
    }

    private Optional<String> getUrl(String bucketName, String fileObjKeyName) throws BadRequestException {
        try {
            String avatarUrl = amazonS3.getUrl(bucketName, fileObjKeyName).toString();
            return Optional.of(avatarUrl);
        } catch (AmazonClientException e) {
            log.error("[x] Bad request for get url with message: {}.", e.getMessage());
            throw new BadRequestException("Bad request for get avatar url");
        }
    }

    private ObjectMetadata objectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String getUniqueAvatarName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private InputStream convertMultiPartToFile(MultipartFile file) throws BadRequestException {
        try {
            return file.getInputStream();
        } catch (IOException e) {
            log.error("[x] Error in converting multipart file to input stream");
            throw new BadRequestException("Cannot save icon for number.");
        }
    }
}
