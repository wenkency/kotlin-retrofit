package com.lven.retrofit.api


import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface RxRestService {
    /**
     * GET 用于查询资源
     */
    @GET
    fun get(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * 用于删除服务端的资源
     */
    @DELETE
    fun delete(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Single<ResponseBody>


    /**
     * 表单形式
     * 用于创建、更新服务端的资源
     */
    @FormUrlEncoded
    @POST
    fun postForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * 表单形式
     * 用于更新服务端的资源
     */
    @FormUrlEncoded
    @PUT
    fun putForm(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @FieldMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * JSON形式
     * 用于创建、更新服务端的资源
     */
    @POST
    fun post(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * JSON形式
     * 用于更新服务端的资源
     */
    @PUT
    fun put(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body requestBody: RequestBody,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * 下载
     * 下载是直接到内存,所以需要 @Streaming
     */
    @Streaming
    @GET
    fun download(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @QueryMap params: MutableMap<String, Any>,
        @Tag tag: String
    ): Single<ResponseBody>

    /**
     * 上传
     */
    @POST
    fun upload(
        @Url url: String,
        @HeaderMap headers: MutableMap<String, String>,
        @Body multipartBody: MultipartBody,
        @Tag tag: String
    ): Single<ResponseBody>


}