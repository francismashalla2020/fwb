package com.thinkbold.fwb.Retrofit;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FWBApI {
    @FormUrlEncoded
    @POST("sendSMSApi.php")
    Observable<String> SendSMS(
            @Field("phone_number") String phone,
            @Field("message_body") String message_body
    );

    @FormUrlEncoded
    @POST("delete_spare.php")
    Observable<String> deleteSpare(@Field("id") String id,
                                   @Field("tmps") String tmps
    );

    @FormUrlEncoded
    @POST("admin/collectionForm.php")
    Observable<String> submitGarage (@Field("gname") String gname,
                                     @Field("phone") String phone,
                                     @Field("region") String region,
                                     @Field("location") String location,
                                     @Field("service") String service,
                                     @Field("cars") String cars
    );
    @FormUrlEncoded
    @POST("delete_car.php")
    Observable<String> deleteCar(@Field("id") String id,
                                 @Field("tmps") String tmps
    );

    @FormUrlEncoded
    @POST("newregistration.php")
    Observable<String> NewRegistration(@Field("name") String name,
                                       @Field("location") String location,
                                       @Field("usernumberverified") String usernumberverified,
                                       @Field("usertoken") String usertoken
    );

    @FormUrlEncoded
    @POST("insertNewOrder.php")
    Observable<String> submitOrder (@Field("name") String name,
                                    @Field("usernumberverified") String usernumberverified,
                                    @Field("location") String location,
                                    @Field("model") String model,
                                    @Field("transmission") String transmission,
                                    @Field("sfuel") String sfuel,
                                    @Field("year") String year,
                                    @Field("cLocation") String cLocation
    );

    @FormUrlEncoded
    @POST("insertSparepurchase.php")
    Observable<String> submitPurchase (@Field("model") String model,
                                       @Field("year") String year,
                                       @Field("sname") String sname,
                                       @Field("chassis") String chassis,
                                       @Field("newNo") String newNo
    );

    @FormUrlEncoded
    @POST("insertRating.php")
    Observable<String> submitRating (@Field("title") String title,
                                     @Field("scale") String scale,
                                     @Field("value") String value,
                                     @Field("ratingvalue") String ratingvalue
    );

    @FormUrlEncoded
    @POST("insertCustomServ.php")
    Observable<String> submitCustomserv (@Field("name") String name,
                                         @Field("usernumberverified") String usernumberverified,
                                         @Field("location") String location,
                                         @Field("other") String other,
                                         @Field("serviceTag") String serviceTag

    );

    @FormUrlEncoded
    @POST("towingserv.php")
    Observable<String> submitTowing (@Field("name") String name,
                                     @Field("usernumberverified") String usernumberverified,
                                     @Field("location") String location,
                                     @Field("model") String model,
                                     @Field("btype") String btype,
                                     @Field("ttype") String ttype,
                                     @Field("host") String host,
                                     @Field("destination") String destination,
                                     @Field("serviceTag") String serviceTag

    );
    @FormUrlEncoded
    @POST("bodyserv.php")
    Observable<String> submitBody (@Field("name") String name,
                                   @Field("usernumberverified") String usernumberverified,
                                   @Field("location") String location,
                                   @Field("model") String model,
                                   @Field("bodyservicetype") String bodyservicetype,
                                   @Field("cLocation") String cLocation,
                                   @Field("serviceTag") String serviceTag

    );
    @FormUrlEncoded
    @POST("washserv.php")
    Observable<String> submitWash (@Field("name") String name,
                                   @Field("usernumberverified") String usernumberverified,
                                   @Field("location") String location,
                                   @Field("model") String model,
                                   @Field("w_Service") String w_Service,
                                   @Field("cLocation") String cLocation,
                                   @Field("datedata") String datedata,
                                   @Field("timedata") String timedata,
                                   @Field("serviceTag") String serviceTag

    );

    //Monday codes
    @Multipart
    @POST("fileUpload.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image);

    @Multipart
    @POST("upload_product_img.php")
    Call<String> uploadProductFiles(@Part MultipartBody.Part file);


    //function for uploading image
    @Multipart
    @POST("uploadfile.php")
    Call<RequestBody>uploadImages(
            @Part MultipartBody.Part part,
            @Part("somedata") RequestBody requestBody
    );

    @Multipart
    @POST("sampleupload.php")
    Call<String> uploadMultiple(
            @Part("description") RequestBody description,
            @Part("size") RequestBody size,
            @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST ("pulp.php")
    Call<String> nuploadproduc(
            @Part MultipartBody.Part file,
            @Field("productName") String productName,
            @Field("imagePath") String imagePath
    );





    //newUpload Image
    @Multipart
    @POST("uploadImage.php")
    Call<Response>uploadNmage(
            @Part MultipartBody.Part image1
    );

    @FormUrlEncoded
    @POST("admin/add_product.php")
    Call<String> getImageData(
            @Field("name") String name,
            @Field("imgPath") String imgPath
    );

    @FormUrlEncoded
    @POST("exists.php")
    Observable<String> CheckUserExists(@Field("usernumberverified") String verified_phonenumber
    );



    //method for getting list PHp
    @GET("list.php")
    Call<String> getString();

    //method for sending sending feedback
    @FormUrlEncoded
    @POST("feedback.php")
    Call<ResponseBody>insertFeedback(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("comment") String comment
    );
    //method for sending rating
    @FormUrlEncoded
    @POST("rating.php")
    Observable<String>insertRating(
            @Field("value") String value,
            @Field("scale") String scale,
            @Field("ratingvalue") String ratingvalue,
            @Field("title") String title
    );

    @FormUrlEncoded
    @POST("add_proddetails.php")
    Observable<String> AddProductDetails(@Field("product_name") String product_name,
                                         @Field("price") String price,
                                         @Field("image1_url") String image1_url,
                                         @Field("image2_url") String image2_url,
                                         @Field("image3_url") String image3_url,
                                         @Field("product_type") String product_type,
                                         @Field("phonenumber") String 	phonenumber,
                                         @Field("status") String status,
                                         @Field("details") String 	details

    );

    @FormUrlEncoded
    @POST("fcm/newUserToken.php")
    Observable<String> newUserToken(@Field("token") String token
    );

    @FormUrlEncoded
    @POST("getYoutubedata.php")
    Observable<String> getYoutubedata(
            @Field("tmps") String tmps
    );

    //sending service history

    @FormUrlEncoded
    @POST("carServiceDetails.php")
    Observable<String> serviceHistory(@Field("phone") String phone,
                                      @Field("model") String model,
                                      @Field("date") String date,
                                      @Field("currentODO") String currentODO,
                                      @Field("nextValue") String nextValue,
                                      @Field("nextODO") String nextODO,
                                      @Field("mname") String mname,
                                      @Field("mphone") String mphone,
                                      @Field("gname") String gname,
                                      @Field("ebrand") String ebrand,
                                      @Field("eno") String eno,
                                      @Field("transf") String transf,
                                      @Field("remarks") String remarks,

                                      @Field("oilc") String oilc,
                                      @Field("filter") String filter,
                                      @Field("tranc") String tranc,
                                      @Field("diffc") String diffc,
                                      @Field("susp") String susp,
                                      @Field("brake") String brake,
                                      @Field("power") String power,
                                      @Field("spark") String spark,
                                      @Field("battery") String battery,
                                      @Field("rad") String rad,
                                      @Field("wind") String wind,
                                      @Field("plate") String plate
    );



    @FormUrlEncoded
    @POST("execution_email_usageproblem.php")
    Observable<String> sendEmailUsageProblem(
            @Field("description") String description,
            @Field("usernumberverified") String usernumberverified
    );

    @FormUrlEncoded
    @POST("execution_email_feedback.php")
    Observable<String> sendEmailFeedback(
            @Field("name") String name,
            @Field("email") String imgPath,
            @Field("feedback") String price
    );


    @Multipart
    @POST("upload_product_img.php")
    Call<String> uploadProductFile(@Part MultipartBody.Part file);


    @FormUrlEncoded
    @POST("sendFlightBookingEmail.php")
    Observable<String> sendFlightBookingEmail(@Field("name") String name,
                                              @Field("phonenumber") String phonenumber,
                                              @Field("email") String email,
                                              @Field("triptype") String triptype,
                                              @Field("trip_from") String trip_from,
                                              @Field("trip_to") String trip_to,
                                              @Field("depart_date") String depart_date,
                                              @Field("return_date") String return_date,
                                              @Field("adults") String adults,
                                              @Field("children") String children,
                                              @Field("infant") String infant,
                                              @Field("details") String details,
                                              @Field("uploaded_img_path") String uploaded_img_path
    );


    @FormUrlEncoded
    @POST("execution_emailTrainBooking.php")
    Observable<String> sendTrainBookingEmail(@Field("name") String name,
                                             @Field("phonenumber") String phonenumber,
                                             @Field("email") String email,
                                             @Field("triptype") String triptype,
                                             @Field("trip_from") String trip_from,
                                             @Field("trip_to") String trip_to,
                                             @Field("depart_date") String depart_date,
                                             @Field("return_date") String return_date,
                                             @Field("adults") String adults,
                                             @Field("children") String children,
                                             @Field("infant") String infant,
                                             @Field("details") String details,
                                             @Field("uploaded_img_path") String uploaded_img_path
    );

    @FormUrlEncoded
    @POST("newTrainBooking.php")
    Observable<String> newTrainBooking(@Field("name") String name,
                                       @Field("phonenumber") String phonenumber,
                                       @Field("email") String email,
                                       @Field("triptype") String triptype,
                                       @Field("trip_from") String trip_from,
                                       @Field("trip_to") String trip_to,
                                       @Field("depart_date") String depart_date,
                                       @Field("return_date") String return_date,
                                       @Field("adults") String adults,
                                       @Field("children") String children,
                                       @Field("infant") String infant,
                                       @Field("details") String details,
                                       @Field("uploaded_img_path") String uploaded_img_path
    );

    @FormUrlEncoded
    @POST("newFlightBooking.php")
    Observable<String> newFlightBooking(@Field("name") String name,
                                        @Field("phonenumber") String phonenumber,
                                        @Field("email") String email,
                                        @Field("triptype") String triptype,
                                        @Field("trip_from") String trip_from,
                                        @Field("trip_to") String trip_to,
                                        @Field("depart_date") String depart_date,
                                        @Field("return_date") String return_date,
                                        @Field("adults") String adults,
                                        @Field("children") String children,
                                        @Field("infant") String infant,
                                        @Field("details") String details,
                                        @Field("uploaded_img_path") String uploaded_img_path,
                                        @Field("flightType") String flightType
    );

    @FormUrlEncoded
    @POST("getflightsdest.php")
    Observable<String> getFlightsdest(
            @Field("tmps") String tmps,
            @Field("tripType") String tripType
    );

    @FormUrlEncoded
    @POST("gettraindest.php")
    Observable<String> getTraindest(
            @Field("tmps") String tmps
    );


    @FormUrlEncoded
    @POST("execution_email_booking.php")
    Observable<String> sendEmailBooking(
            @Field("name") String name,
            @Field("username") String username,
            @Field("usernumberverified") String usernumberverified,
            @Field("email") String email,
            @Field("country") String adults,
            @Field("details") String kids,
            @Field("itineraryName") String main_interest,
            @Field("booking_ref") String tour_date,
            @Field("pax") String details,
            @Field("totalAmount") String totalAmount
    );

    @FormUrlEncoded
    @POST("execution_email.php")
    Observable<String> sendEmailInquire(@Field("name") String name,
                                        @Field("phonenumber") String phonenumber,
                                        @Field("email") String email,
                                        @Field("adults") String adults,
                                        @Field("kids") String kids,
                                        @Field("main_interest") String main_interest,
                                        @Field("tour_date") String tour_date,
                                        @Field("details") String details,
                                        @Field("selectedBudgetRange") String selectedBudgetRange
    );


    @FormUrlEncoded
    @POST("newBooking.php")
    Observable<String> newBooking(@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("country") String adults,
                                  @Field("details") String kids,
                                  @Field("itineraryName") String main_interest,
                                  @Field("booking_ref") String tour_date,
                                  @Field("pax") String details,
                                  @Field("totalAmount") String totalAmount
    );

    @FormUrlEncoded
    @POST("getCountries.php")
    Observable<String> getCountries(
            @Field("tmps") String tmps
    );


    @FormUrlEncoded
    @POST("newGeneralInquire.php")
    Observable<String> newGeneralInquire(@Field("name") String name,
                                         @Field("phonenumber") String phonenumber,
                                         @Field("email") String email,
                                         @Field("adults") String adults,
                                         @Field("kids") String kids,
                                         @Field("main_interest") String main_interest,
                                         @Field("tour_date") String tour_date,
                                         @Field("details") String details,
                                         @Field("selectedBudgetRange") String selectedBudgetRange
    );



    @GET("checkAppVersion.php")
    Observable<String> checkAppVersion
            ();

    @FormUrlEncoded
    @POST("addadvertemail.php")
    Observable<String> SendAdvertEmail(@Field("advertEmail") String advertEmail
    );

    @FormUrlEncoded
    @POST("addwishlist.php")
    Observable<String> AddWishList(@Field("attractions_id") String attractions_id,
                                   @Field("attractions_name") String attractions_name,
                                   @Field("userphonenumber") String userphonenumber
    );

    @FormUrlEncoded
    @POST("removeWishlist.php")
    Observable<String> RemoveWishList(@Field("attractions_id") String attractions_id,
                                      @Field("userphonenumber") String verified_phonenumber
    );

    @FormUrlEncoded
    @POST("checkwishlist.php")
    Observable<String> checkIsWishlist(@Field("attractions_id") String attractions_id,
                                       @Field("verified_phonenumber") String verified_phonenumber
    );

    @FormUrlEncoded
    @POST("newcomment.php")
    Observable<String> NewUserComments(@Field("comment") String comment,
                                       @Field("comment_date") String comment_date,
                                       @Field("comment_id") String comment_id,
                                       @Field("post_id") String post_id,
                                       @Field("usernumberverified") String usernumberverified
    );


    @FormUrlEncoded
    @POST("completeRegistration.php")
    Observable<String> NewUserRegistration(@Field("verified_phonenumber") String verified_phonenumber,
                                           @Field("accountName") String accountName,
                                           @Field("userToken") String userToken,
                                           @Field("emailAddress") String emailAddress,
                                           @Field("newsLetterSubscription") String newsLetterSubscription
    );


    @FormUrlEncoded
    @POST("usageprobfeed.php")
    Observable<String> UsageProblem(@Field("description") String description,
                                    @Field("usernumberverified") String usernumberverified,
                                    @Field("tag") String tag
    );

    @FormUrlEncoded
    @POST("newpost.php")
    Observable<String> uploadPosts(@Field("uniqueId") String uniqueId,
                                   @Field("posturl") String posturl,
                                   @Field("thmbnailurl") String thmbnailurl,
                                   @Field("posttype") String posttype,
                                   @Field("description") String description,
                                   @Field("usernumberverified") String usernumberverified
    );


    @FormUrlEncoded
    @POST("addfeedback.php")
    Observable<String> AddFeedback(@Field("name") String name,
                                   @Field("email") String imgPath,
                                   @Field("feedback") String price
    );


    @FormUrlEncoded
    @POST("addphonenumber.php")
    Observable<String> RegisterNumber(@Field("verified_phonenumber") String verified_phonenumber
    );
}
