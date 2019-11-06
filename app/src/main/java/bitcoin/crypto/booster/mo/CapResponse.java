package bitcoin.crypto.booster.mo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CapResponse {
    @SerializedName("data")
    @Expose
    public List<CapEntry> data = null;

}
