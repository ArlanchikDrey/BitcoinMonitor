package bitcoin.crypto.booster.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import bitcoin.crypto.booster.R;
import im.delight.android.webview.AdvancedWebView;

public class UrliActivity extends AppCompatActivity implements AdvancedWebView.Listener{
    @BindView(R.id.week_view)
    AdvancedWebView week;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_urli);
        ButterKnife.bind(this);
        CookieManager.getInstance().setAcceptCookie(true);

        week.setCookiesEnabled(true);
        week.getSettings().setJavaScriptEnabled(true);
        week.getSettings().setDomStorageEnabled(true);
        week.getSettings().setAllowContentAccess(true);
        week.setListener(this, this);
        if (savedInstanceState != null) {
            week.restoreState(savedInstanceState);
        } else if(getIntent()!=null){
            week.loadUrl(getIntent().getStringExtra("week"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        week.saveState(outState);
    }



    @Override
    public void onBackPressed() {
        if (!week.onBackPressed()) {
            week.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if(url!=null) Log.i("WEEK",url);
    }

    @Override
    public void onPageFinished(String url) {
        week.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
        overridePendingTransition(0,0);

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        week.onActivityResult(requestCode,resultCode,data);
    }
}
