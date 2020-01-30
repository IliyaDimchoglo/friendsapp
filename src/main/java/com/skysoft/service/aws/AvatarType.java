package com.skysoft.service.aws;

import java.util.Arrays;

public enum AvatarType {
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpeg"),
    UNSUPPORTED("UnsupportedContentType", "UnsupportedType");


    private String contentType;
    private String type;

    AvatarType(String contentType, String type) {
        this.contentType = contentType;
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public String getType() {
        return type;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static AvatarType getIconType(String contentType) {
        return Arrays.stream(values()).filter(type -> type.getContentType().equals(contentType)).findFirst().orElse(UNSUPPORTED);
    }
}
