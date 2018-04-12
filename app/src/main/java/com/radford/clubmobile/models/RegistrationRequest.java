package com.radford.clubmobile.models;

import android.net.Uri;
import com.radford.clubmobile.utils.FileUploadUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegistrationRequest {
    private final MultipartBody.Part file;
    private final Map<String, RequestBody> partMap;


    public RegistrationRequest(String email, String password, String firstName, String lastName, Uri fileUri) {
        this.file = (fileUri != null) ? FileUploadUtil.getFilePart(fileUri): null;

        this.partMap = new HashMap<>();
        this.partMap.put("email",  RequestBody.create(MediaType.parse("text/plain"), email));
        this.partMap.put("password",  RequestBody.create(MediaType.parse("text/plain"), password));
        this.partMap.put("firstName",  RequestBody.create(MediaType.parse("text/plain"), firstName));
        this.partMap.put("lastName",  RequestBody.create(MediaType.parse("text/plain"), lastName));
    }

    public Map<String, RequestBody> getPartMap() {
        return partMap;
    }

    public MultipartBody.Part getFile() {
        return file;
    }
}
