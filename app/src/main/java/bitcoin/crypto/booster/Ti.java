package bitcoin.crypto.booster;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

import bitcoin.crypto.booster.mo.Ticker;

public class Ti {
    private static String TAG = "PREFS";

    public static List<Ticker> data(Context ctx){
        String json = ctx.getSharedPreferences(TAG,0).getString("data",null);
        if(json!=null){
            Type type = new TypeToken<List<Ticker>>(){}.getType();
            List<Ticker> data = new Gson().fromJson(json,type);
            if(data!=null) return data;
        }
        return null;
    }

    public static void save(Context ctx, List<Ticker> data){
        ctx.getSharedPreferences(TAG,0).edit().putString("data",new Gson().toJson(data)).apply();
    }

    public static boolean checkIp(Context ctx,String ip) {
        InputStream inputStream = ctx.getResources().openRawResource(R.raw.ips);
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            String wrongIp = scanner.nextLine();
            if (ip.equals(wrongIp))
                return false;
        }
        return true;
    }
}
