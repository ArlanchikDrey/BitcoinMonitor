package bitcoin.crypto.booster.mo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ticker implements Parcelable {
    @Expose
    public String name;
    @Expose
    public String symbol;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("total_supply")
    @Expose
    public Double totalSupply;
    @SerializedName("max_supply")
    @Expose
    public Double maxSupply;

    @SerializedName("quote")
    @Expose
    public Quotes quotes;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeString(this.slug);
        dest.writeValue(this.totalSupply);
        dest.writeValue(this.maxSupply);
        dest.writeParcelable(this.quotes, flags);
    }

    public Ticker() {
    }

    protected Ticker(Parcel in) {
        this.name = in.readString();
        this.symbol = in.readString();
        this.slug = in.readString();
        this.totalSupply = (Double) in.readValue(Double.class.getClassLoader());
        this.maxSupply = (Double) in.readValue(Double.class.getClassLoader());
        this.quotes = in.readParcelable(Quotes.class.getClassLoader());
    }

    public static final Creator<Ticker> CREATOR = new Creator<Ticker>() {
        @Override
        public Ticker createFromParcel(Parcel source) {
            return new Ticker(source);
        }

        @Override
        public Ticker[] newArray(int size) {
            return new Ticker[size];
        }
    };
}
