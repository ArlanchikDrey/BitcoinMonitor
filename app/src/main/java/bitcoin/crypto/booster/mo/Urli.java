package bitcoin.crypto.booster.mo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Urli implements Parcelable {
    @SerializedName("enabled")
    @Expose
    public boolean isOn;

    @SerializedName("web_view")
    @Expose
    public String link;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isOn ? (byte) 1 : (byte) 0);
        dest.writeString(this.link);
    }

    public Urli() {
    }

    protected Urli(Parcel in) {
        this.isOn = in.readByte() != 0;
        this.link = in.readString();
    }

    public static final Parcelable.Creator<Urli> CREATOR = new Parcelable.Creator<Urli>() {
        @Override
        public Urli createFromParcel(Parcel source) {
            return new Urli(source);
        }

        @Override
        public Urli[] newArray(int size) {
            return new Urli[size];
        }
    };
}
