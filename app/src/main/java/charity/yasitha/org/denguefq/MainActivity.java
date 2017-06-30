package charity.yasitha.org.denguefq;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private final static String ZERO_ML_STRING = "0ml";
    private final static String ZERO_ML_PH_STRING = "0ml/h";

    private EditText etWeight;
    private TextView tvFQ, tvFQHour;

    DecimalFormat df = new DecimalFormat("#,###,##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        etWeight = (EditText) findViewById(R.id.et_weight);
        tvFQ = (TextView) findViewById(R.id.tvFQ);
        tvFQHour = (TextView) findViewById(R.id.tvFQHour);

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    String s1 = s.toString();
                    if (!s1.isEmpty()) {
                        double weight = Double.parseDouble(s1);
                        if (weight > 2000) {
                            Toast.makeText(MainActivity.this, "Value is too large", Toast.LENGTH_SHORT).show();
                            tvFQ.setText(ZERO_ML_STRING);
                            tvFQHour.setText(ZERO_ML_PH_STRING);
                            return;
                        }
                        double fq = calculateFq(weight);
                        double fqPerHour = calculateFqPerHour(fq);
                        tvFQ.setText(String.format("%sml", df.format(fq)));
                        tvFQHour.setText(String.format("%sml/h", df.format(fqPerHour)));
                    } else {
                        tvFQ.setText(ZERO_ML_STRING);
                        tvFQHour.setText(ZERO_ML_PH_STRING);
                    }
                } catch (NumberFormatException ne) {
                    //Skipping exception
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Not available", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private double calculateFq(double weight) {
        double fluidFor48Hours = 0;
        double commonQuota = weight * 50;
        fluidFor48Hours = fluidFor48Hours + commonQuota;
        if (weight <= 10) {
            double additionalFqForFirst10 = 100 * weight;
            fluidFor48Hours = fluidFor48Hours + additionalFqForFirst10;
        } else if (weight <= 20) {
            double additionalFqForFirst10 = 1000;
            double additionalFqForSecond10 = 50 * (weight - 10);
            fluidFor48Hours = fluidFor48Hours + additionalFqForFirst10 + additionalFqForSecond10;
        } else if (weight > 20) {
            double additionalFqForFirst10 = 1000;
            double additionalFqForSecond10 = 500;
            double additionalFqForThird10 = 20 * (weight - 20);
            fluidFor48Hours = fluidFor48Hours + additionalFqForFirst10 + additionalFqForSecond10 + additionalFqForThird10;
        }
        return fluidFor48Hours;
    }

    private double calculateFqPerHour(double fq) {
        return fq / 48;
    }
}
