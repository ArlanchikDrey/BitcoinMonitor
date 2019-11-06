package bitcoin.crypto.booster.mo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class TickerResponse implements Parcelable {
    @Expose
    public List<Ticker> data;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(data);
    }

    public TickerResponse() {
    }

    protected TickerResponse(Parcel in) {
        data = new ArrayList<Ticker>();
        in.readList(data,Ticker.class.getClassLoader());
    }

    public static final Parcelable.Creator<TickerResponse> CREATOR = new Parcelable.Creator<TickerResponse>() {
        @Override
        public TickerResponse createFromParcel(Parcel source) {
            return new TickerResponse(source);
        }

        @Override
        public TickerResponse[] newArray(int size) {
            return new TickerResponse[size];
        }
    };
}
