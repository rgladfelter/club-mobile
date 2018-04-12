package com.radford.clubmobile.utils;

import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUploadUtil {
    public static MultipartBody.Part getFilePart(Uri fileUri) {
        File file = new File(fileUri.getPath());
        return MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
    }
}
