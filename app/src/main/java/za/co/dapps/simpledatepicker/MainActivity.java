package za.co.dapps.simpledatepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.dapps.simpledatepicker.ui.adapters.DatePickerPagerAdapter;
import za.co.dapps.simpledatepicker.ui.view.WrapContentHeightViewPager;
import za.co.dapps.simpledatepicker.utils.Formats;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.datePickerViewPager)
    WrapContentHeightViewPager datePickerViewPager;

    @BindView(R.id.tvSelectedDate)
    TextView tvSelectedDate;

    DatePickerPagerAdapter datePickerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Display a 1 Week, 7 Days
        datePickerPagerAdapter = new DatePickerPagerAdapter(this, 90); // 3 Months
        datePickerPagerAdapter.setPageWidth(0.1428f); // 1/7 width
        datePickerViewPager.setAdapter(datePickerPagerAdapter);

        datePickerViewPager.setOffscreenPageLimit(10);
        datePickerViewPager.setCurrentItem(datePickerPagerAdapter.getCount());
        datePickerPagerAdapter.setOnDayPickerSelector(new DatePickerPagerAdapter.OnDayPickerSelector() {
            @Override
            public void onSelected(final int position, final int daysOffset) {
                //
                // onDateSelected
                final DateTime dt = DateTime.now().plusDays(daysOffset).withTimeAtStartOfDay();
                if (!dt.isAfterNow()) {
                    final int centerPos = Math.max(0, position - 3);

                    //This notify that you need to recreate the views
                    datePickerPagerAdapter.notifyDataSetChanged();
                    datePickerViewPager.setCurrentItem(centerPos, true);

                    tvSelectedDate.setText(dt.toString(Formats.DATETIME_SHORT));
                }
            }
        });

    }
}
