package com.retrofit.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * 协程请求API接口
 */
interface SuspendService {
    /**
     * Get请求
     */
    @GET
    suspend fun get(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * Delete请求
     */
    @DELETE
    suspend fun delete(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * Post Json请求
     */
    @POST
    suspend fun post(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * Put Json请求
     */
    @PUT
    suspend fun put(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * Post 表单请求
     */
    @POST
    @FormUrlEncoded
    suspend fun postForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * Post 表单请求
     */
    @PUT
    @FormUrlEncoded
    suspend fun putForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * 下载
     */
    @GET
    @Streaming
    suspend fun download(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Response<ResponseBody>

    /**
     * 上传
     */
    @POST
    suspend fun upload(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: MultipartBody,
        @Tag tag: String
    ): Response<ResponseBody>
}