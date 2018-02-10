package eugene.petsshelter.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StripeService {

    @FormUrlEncoded
    @POST("donate")
    Call<Void> chargeDonation(@FieldMap Map<String,Object> fields);

}
