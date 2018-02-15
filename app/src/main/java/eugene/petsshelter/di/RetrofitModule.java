package eugene.petsshelter.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.BuildConfig;
import eugene.petsshelter.service.StripeService;
import eugene.petsshelter.utils.AppConstants;
import eugene.petsshelter.utils.LiveDataCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eugene on 08.02.2018.
 */

@Module
public class RetrofitModule {

    @Singleton @Provides
    OkHttpClient provideOkHttpClient(){

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }

        return httpClientBuilder.build();
    }

    @Singleton @Provides
    Retrofit provideHttpClient(OkHttpClient okHttpClient){
        return new Retrofit.Builder().baseUrl(AppConstants.GHARGE_SCRIPT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build();
    }

    @Provides
    StripeService provideStripeService(Retrofit retrofit) {
        return retrofit.create(StripeService.class);
    }

}
