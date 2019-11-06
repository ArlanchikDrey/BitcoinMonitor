package bitcoin.crypto.booster.mo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CapEntry {
    @SerializedName("priceUsd")
    @Expose
    public String priceUsd;
    @SerializedName("time")
    @Expose
    public Long time;
}
