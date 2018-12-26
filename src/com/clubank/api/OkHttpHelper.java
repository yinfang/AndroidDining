package com.clubank.api;

import com.clubank.util.MyRow;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by yeqing on 2016/2/1.
 * OkHttp的帮助类，使用单例模式
 */
public class OkHttpHelper {

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    private static OkHttpHelper sOkHttpHelper;
    private OkHttpClient client;

    //使用单例模式
    public static OkHttpHelper getInstance() {
        if (sOkHttpHelper == null) {
            sOkHttpHelper = new OkHttpHelper();
            sOkHttpHelper.client = new OkHttpClient();
        }
        return sOkHttpHelper;
    }

    /**
     * get方式
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * post 方式提交表单
     *
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public String post(String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * post 方式提交表单
     *
     * @param url
     * @param row
     * @return
     * @throws IOException
     */
    public String post(String url, MyRow row) throws IOException {

        FormBody.Builder builder = new FormBody.Builder();
        if (null != row) {
            for (String key : row.keySet()) {
                Object o = row.get(key);
                builder.add(key, o.toString());
            }
        }
        RequestBody reqBody = builder.build();

        return post(url, reqBody);
    }


    /**
     * 上传图片
     * @param url
     * @param fileName
     * @param method
     * @return
     * @throws Exception
     */
    public String postFile(String url, String fileName, String method) throws Exception {
        File file = new File(fileName);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                        //.addFormDataPart("title", "Square Logo")
                .addFormDataPart(method, file.getName(),
                        RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
