package bitcoin.crypto.booster.mo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usd implements Parcelable {
    @Expose
    public Double price;
    @SerializedName("volume_24h")
    @Expose
    public Double volume24h;
    @SerializedName("market_cap")
    @Expose
    public Double marketCap;
    @SerializedName("percent_change_1h")
    @Expose
    public Double percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    public Double percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    public Double percentChange7d;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.price);
        dest.writeValue(this.volume24h);
        dest.writeValue(this.marketCap);
        dest.writeValue(this.percentChange1h);
        dest.writeValue(this.percentChange24h);
        dest.writeValue(this.percentChange7d);
    }

    public Usd() {
    }

    protected Usd(Parcel in) {
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.volume24h = (Double) in.readValue(Double.class.getClassLoader());
        this.marketCap = (Double) in.readValue(Double.class.getClassLoader());
        this.percentChange1h = (Double) in.readValue(Double.class.getClassLoader());
        this.percentChange24h = (Double) in.readValue(Double.class.getClassLoader());
        this.percentChange7d = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Usd> CREATOR = new Parcelable.Creator<Usd>() {
        @Override
        public Usd createFromParcel(Parcel source) {
            return new Usd(source);
        }

        @Override
        public Usd[] newArray(int size) {
            return new Usd[size];
        }
    };
}
