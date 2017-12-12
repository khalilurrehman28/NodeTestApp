package com.dupleit.mapmarkers.nodeapptest;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

 /*   @GET("getpostonmap_request")
    Call<UsersMapsMarkers> getUserPostOnMap();

    @FormUrlEncoded
    @POST("getpostdatabyid_request")
    Call<UserPost> getUserDataByPost(@Field("POST_ID") String postID);

    @Multipart
    @POST("post_request")
    Call<UploadImageResponse> upload_post_request(
            @Part MultipartBody.Part file, @Part("USER_ID") int USER_ID,
            @Part("POST_DESCRIPTION") String POST_DESCRIPTION, @Part("POST_LATITUDE") double POST_LATITUDE,
            @Part("POST_LONGITUDE") double POST_LONGITUDE);
*/
    @FormUrlEncoded
    @POST("users")
    Call<UserLogin> getpostcomment_request(@Field("user_id") int userID);
/*

    @FormUrlEncoded
    @POST("addcommenttopost_request")
    Call<CommentResponse> addcommenttopost_request(@Field("POST_ID") int postID, @Field("USER_ID") int USER_ID, @Field("COMMENT") String Comment);
*/

   /* @FormUrlEncoded
    @POST("getpostonlatlang_request")
    Call<UsersMapsMarkers> getpostonlatlang_request(@Field("Userlatlang[]") List<LatLng> markerList);

    @FormUrlEncoded
    @POST("checkUserLogin_request")
    Call<UserLogin> checkUserLogin_request(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("registeruser_request")
    Call<UserLogin> registeruser_request(@Field("email") String email, @Field("password") String password, @Field("username") String username, @Field("mobile") String mobile);
*/
}