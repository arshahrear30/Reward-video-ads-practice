package com.example.reward;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView tvdisplay;
    RewardedAd rewardedAd;
    String TAG = "RewardedAd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        tvdisplay = findViewById(R.id.tvdisplay);



        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();

        loadRewardAd();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedAd();
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}

    private void showRewardedAd(){

        if(rewardedAd !=null){

        rewardedAd.show(
                MainActivity.this,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        Log.d(TAG, "User earned the reward.");
                        // Handle the reward.
                        tvdisplay.append("\n User earned the reward.");
                    }
                });
    }else {
            Log.d(TAG,"The reward ad wasn't ready yet.");
            tvdisplay.append("\n The reward ad wasn't ready yet.");

        }
    }


    //}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}}

    //-------------------------

    private void loadRewardAd(){




        RewardedAd.load(
                this,
                "ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        Log.d(TAG, "Ad was loaded.");
                        MainActivity.this.rewardedAd = rewardedAd;
                        tvdisplay.setText("Ad was loaded.");
                        setRewardedAdCallBack();//+++++++++++++++++++++
                    }
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.getMessage());
                        rewardedAd = null;
                        tvdisplay.setText(loadAdError.toString());
                    }
                });

    }
    //------------------------------------------------

    //=================================================
    private void setRewardedAdCallBack(){

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d(TAG, "Ad was dismissed.");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        tvdisplay.append("\n Ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d(TAG, "Ad failed to show.");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null;
                        tvdisplay.append("\n Ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d(TAG, "Ad showed fullscreen content.");
                        tvdisplay.append("\n Ad showed fullscreen content.");
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(TAG, "Ad recorded an impression.");
                        tvdisplay.append("\n Ad recorded an impression.");
                    }

                    @Override
                    public void onAdClicked() {
                        // Called when an ad is clicked.
                        Log.d(TAG, "Ad was clicked.");

                        tvdisplay.append("\n Ad was clicked.");
                    }
                });

    }

    //==================================================

}
