package bitcoin.crypto.booster.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import bitcoin.crypto.booster.BuildConfig;
import bitcoin.crypto.booster.R;

public class FragmentInfo extends Fragment {
    @BindView(R.id.title)
    TextView title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info, container, false);
        ButterKnife.bind(this, rootView);
        title.setText(title.getText().toString() + " " + BuildConfig.VERSION_NAME);
        return rootView;
    }

    @OnClick(R.id.policy)
    public void onViewClicked() {
        if (getActivity() != null) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(getString(R.string.alert_full))
                    .setTitle(getString(R.string.alert_title))
                    .setPositiveButton("OK", null)
                    .show();
        }
    }
}
