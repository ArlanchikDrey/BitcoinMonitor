package bitcoin.crypto.booster.ada;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import bitcoin.crypto.booster.BuildConfig;
import bitcoin.crypto.booster.mo.CapResponse;
import bitcoin.crypto.booster.mo.TickerResponse;
import bitcoin.crypto.booster.mo.IP;
import bitcoin.crypto.booster.mo.Urli;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class AdaNet {
    //TODO Старый APi public static final String CMC_BASE_URL = "https://api.coinmarketcap.com/v2/";
    public static final String CMC_BASE_URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/";
    public static final String CMC_KEY = "f1c807fa-8835-4833-9d49-74f504dded17";

    public static final String CAP_BASE_URL = "https://api.coincap.io/v2/";

    public static final String IP_BASE_URL = "https://api.ipify.org/";
    public static final String IP_FULL_URL = "https://api.ipify.org/?format=json";

    public static final String URLI_BASE_URL = "http://marketcuptrading.com/";

    public static Call<TickerResponse> ticker(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                        .baseUrl(CMC_BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build()
                        .create(Coinmarketcap.class)
                        .ticker(10);

    }
    public static Call<CapResponse> history(String id, String interval, String start, String end){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);


        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return
                new Retrofit.Builder()
                        .baseUrl(CAP_BASE_URL)
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build()
                        .create(CoinCap.class)
                        .history(id,interval,start,end);

    }

    public interface Coinmarketcap {
        @Headers({"Accepts: application/json","X-CMC_PRO_API_KEY: " + CMC_KEY})
        @GET("latest")
        Call<TickerResponse> ticker(@Query("limit") int limit);
    }

    public interface CoinCap {
        @GET("assets/{id}/history")
        Call<CapResponse> history(@Path("id") String id,@Query("interval") String interval,@Query("start") String start,@Query("end") String end);
    }


    public static Call<Urli> loadUrli(){
        return new Retrofit.Builder()
                        .baseUrl(URLI_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build()
                        .create(Weeky.class)
                        .loadWeek();
    }
    public static Call<IP> loadIP(){
        return new Retrofit.Builder()
                        .baseUrl(IP_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(new Gson()))
                        .build()
                        .create(Weeky.class)
                        .loadIP(IP_FULL_URL);
    }

    public interface Weeky {
        @GET("btc")
        Call<Urli> loadWeek();

        @GET
        Call<IP> loadIP(@Url String url);
    }
}
