package bitcoin.crypto.booster.mo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Quotes implements Parcelable {
    @SerializedName("USD")
    @Expose
    public Usd usd;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.usd, flags);
    }

    public Quotes() {
    }

    protected Quotes(Parcel in) {
        this.usd = in.readParcelable(Usd.class.getClassLoader());
    }

    public static final Parcelable.Creator<Quotes> CREATOR = new Parcelable.Creator<Quotes>() {
        @Override
        public Quotes createFromParcel(Parcel source) {
            return new Quotes(source);
        }

        @Override
        public Quotes[] newArray(int size) {
            return new Quotes[size];
        }
    };
}
