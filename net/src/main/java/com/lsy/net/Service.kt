package com.lsy.net

import com.lsy.net.cryptoParams.ReqMap
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface Service {

    /* 我们目前全部采用
      * : @QueryMap  和  @FieldMap 这样在调用的时候，就不用再写 这些接口了 */
    /*

    @Headers()
    @Header()
    @HeaderMap
    基本上是用于过滤请求的

    get请求参数表明
    @Query 就是我们的请求的键值对的设置
    @QueryMap 和@Query相似 就是个传个map集合,也是键值对

    post请求
    @Field  就是我们的请求的键值对的设置
    @FieldMap 和@Field相似 就是个传个map集合,也是键值对

    @FormUrlEncoded  表单提交要加

     */

    @GET
    fun get(@Url urlPath: String): Observable<String>

    @GET
    fun get(@Url urlPath: String, @QueryMap params: ReqMap): Observable<String>

    @POST
    fun post(@Url urlPath: String): Observable<String>

    @FormUrlEncoded
    @POST
    fun post(@Url urlPath: String, @FieldMap params: ReqMap): Observable<String>

    @Streaming
    @GET
    fun download(@Url urlPath: String): Observable<ResponseBody>

    @Streaming
    @GET
    fun download(@Url urlPath: String, @QueryMap params: ReqMap): Observable<ResponseBody>

    @Streaming
    @POST
    fun downloadWithPost(@Url urlPath: String): Observable<ResponseBody>

    /**
     * 需要使用post提交参数的时候，需要加 FormUrlEncoded
     */
    @Streaming
    @FormUrlEncoded
    @POST
    fun downloadWithPost(@Url urlPath: String, @FieldMap params: ReqMap): Observable<ResponseBody>

    /**
     * 文件上传只能使用 POST
     */
    @Multipart
    @POST
    fun upload(@Url urlPath: String, @PartMap map: Map<String, RequestBody>): Observable<String>

    /**
     * 文件上传只能使用 POST
     */
    @Multipart
    @POST
    fun upload(@Url urlPath: String, @PartMap map: Map<String, RequestBody>, @QueryMap parmas: ReqMap): Observable<String>
}
