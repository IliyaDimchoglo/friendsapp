package com.skysoft.service.aws.api;

import com.skysoft.service.aws.AvatarType;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {

    String uploadAvatar(MultipartFile avatar, AvatarType avatarType);
}
