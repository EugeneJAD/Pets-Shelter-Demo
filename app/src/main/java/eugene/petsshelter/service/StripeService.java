package eugene.petsshelter.service;

import android.arch.lifecycle.LiveData;

import java.util.Map;

import eugene.petsshelter.data.models.ApiResponse;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface StripeService {

    @FormUrlEncoded
    @POST("donate")
    LiveData<ApiResponse<ResponseBody>> chargeDonation(@FieldMap Map<String,Object> fields);

}
