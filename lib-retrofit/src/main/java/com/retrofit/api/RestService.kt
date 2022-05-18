package com.retrofit.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 请求API接口
 */
interface RestService {
    /**
     * Get请求
     */
    @GET
    fun get(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * Delete请求
     */
    @DELETE
    fun delete(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * Post Json请求
     */
    @POST
    fun post(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * Put Json请求
     */
    @PUT
    fun put(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * Post 表单请求
     */
    @POST
    @FormUrlEncoded
    fun postForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * Post 表单请求
     */
    @PUT
    @FormUrlEncoded
    fun putForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * 下载
     */
    @GET
    @Streaming
    fun download(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Call<ResponseBody>

    /**
     * 上传
     */
    @POST
    fun upload(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: MultipartBody,
        @Tag tag: String
    ): Call<ResponseBody>
}