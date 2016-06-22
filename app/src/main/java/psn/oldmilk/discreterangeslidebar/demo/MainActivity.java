package psn.oldmilk.discreterangeslidebar.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import psn.oldmilk.seekbar.discreterangeslidebar.DiscreteRangeSlideBar;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class MainActivity extends AppCompatActivity {

    private DiscreteRangeSlideBar anotherSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anotherSlider = (DiscreteRangeSlideBar) findViewById( R.id.rsv_another);

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
        };
        anotherSlider.setOnSlideListener(listener);
    }
}
