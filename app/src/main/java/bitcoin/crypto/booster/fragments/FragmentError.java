package bitcoin.crypto.booster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import bitcoin.crypto.booster.R;
import bitcoin.crypto.booster.activities.SplashActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentError extends Fragment {

    @BindView(R.id.retryBtn)
    Button retryBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.error, container, false);
        ButterKnife.bind(this, rootView);
        retryBtn.setOnClickListener(view -> {
            if(getActivity()!=null) {
                startActivity(new Intent(getActivity(), SplashActivity.class));
                getActivity().finish();
                getActivity().overridePendingTransition(0,0);
            }
        });
        return rootView;
    }
}
