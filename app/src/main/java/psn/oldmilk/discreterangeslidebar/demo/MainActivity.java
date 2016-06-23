package psn.oldmilk.discreterangeslidebar.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import psn.oldmilk.seekbar.discreterangeslidebar.DiscreteRangeSlideBar;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class MainActivity extends AppCompatActivity {

    private DiscreteRangeSlideBar anotherSlider;

    private Button mDrawRulerButton;
    private Button mStartIndexButton;
    private Button mRangeNumberButton;
    private Button mUnitButton;
    private Button mMultiplierButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anotherSlider = (DiscreteRangeSlideBar) findViewById( R.id.rsv_another);
        mDrawRulerButton = (Button) findViewById(R.id.button_drawruler);
        mStartIndexButton = (Button) findViewById(R.id.button_startindex);
        mRangeNumberButton = (Button) findViewById(R.id.button_numberrange);
        mUnitButton = (Button) findViewById(R.id.button_unit);
        mMultiplierButton = (Button) findViewById(R.id.button_multiplier);


        anotherSlider.setTypeface(TypefaceUtils.load(getAssets(), "fonts/MavenPro-Bold.ttf"));
        final DiscreteRangeSlideBar.OnSlideListener listener = new DiscreteRangeSlideBar.OnSlideListener() {
            @Override
            public void onSlide(int index) {
                Toast.makeText(
                        getApplicationContext(),
                        "Hi index: " + index,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onSliding() {

            }

            @Override
            public void onSlideUp() {

            }

            @Override
            public void onSlideDown() {

            }
        };
        anotherSlider.setOnSlideListener(listener);

        mDrawRulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anotherSlider.setIsDrawRuler(!anotherSlider.getIsDrawRuler());

            }
        });

        mStartIndexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anotherSlider.getStartIndex()==1) {
                    anotherSlider.setStartIndex(0);

                }else{
                    anotherSlider.setStartIndex(1);
                }

            }
        });

        mRangeNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anotherSlider.getRangeCount()==3) {
                    anotherSlider.setRangeCount(10);

                }else{
                    anotherSlider.setRangeCount(3);
                }

            }
        });

        mUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anotherSlider.getUnit().isEmpty()) {
                    anotherSlider.setUnit("%");

                }else{
                    anotherSlider.setUnit("");

                }

            }
        });

        mMultiplierButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anotherSlider.getMultiplier() == 1) {
                    anotherSlider.setMultiplier(10);
                }else{
                    anotherSlider.setMultiplier(1);
                }
            }
        });
    }
}
