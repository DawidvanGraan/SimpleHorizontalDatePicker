package za.co.dapps.simpledatepicker.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import za.co.dapps.simpledatepicker.R;
import za.co.dapps.simpledatepicker.utils.Formats;

public class DatePickerPagerAdapter extends RecycledPagerAdapter<DatePickerPagerAdapter.FooViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int entryCount = 0;
    private int selPos = 0;
    private float pageWidth = 1.0f;

    private boolean isMonthPicker = false; // Default to DAYS

    private DateTimeFormatter formatText1 = DateTimeFormat.forPattern(Formats.DATETIME_DAY_OF_WEEK_SHORT);
    private DateTimeFormatter formatText2 = DateTimeFormat.forPattern(Formats.DATETIME_DAY_OF_MONTH);

    interface OnDayPickerSelectorInterface {
        void onSelected(final int position, final int daysOffset);
    }

    public static class OnDayPickerSelector implements OnDayPickerSelectorInterface {

        @Override
        public void onSelected(final int position, final int daysOffset) {
            // Override me
        }
    }

    private OnDayPickerSelectorInterface onDayPickerSelector;

    public DatePickerPagerAdapter(Context context, final int count) {
        this(context, count, Formats.DATETIME_DAY_OF_WEEK_SHORT, Formats.DATETIME_DAY_OF_MONTH);
    }

    public DatePickerPagerAdapter(Context context, final int count,
                                 String format1, String format2) {
        super();
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.entryCount = count;
        this.selPos = Math.max(0, count - 4);

        this.formatText1 = format1 == null ? null : DateTimeFormat.forPattern(format1);
        this.formatText2 = format2 == null ? null : DateTimeFormat.forPattern(format2);
    }

    @Override
    public FooViewHolder onCreateViewHolder(ViewGroup parent) {
        // Inflate view
        View v = mLayoutInflater.inflate(R.layout.layout_date_pager_item, parent, false);

        final FooViewHolder viewHolder = new FooViewHolder(v);
        viewHolder.setOnClickListener(new OnDayPickerSelector() {
            @Override
            public void onSelected(final int position, final int offset) {

                // Cannot select into the future
                if (offset <= 0) {
                    selPos = position;
                }

                if (onDayPickerSelector != null) {
                    onDayPickerSelector.onSelected(position, offset);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FooViewHolder viewHolder, int position) {

        // Update the correct date to display
        DateTime dateTime = DateTime.now();
        final int offSet = position - (entryCount - 4);  // ENTRY_COUNT - 3 - 1
        if (this.isMonthPicker) {
            dateTime = dateTime.plusMonths(offSet);
        } else {
            dateTime = dateTime.plusDays(offSet);
        }

        // Show foo inside viewHolder
        viewHolder.show(position, formatDate(dateTime, formatText1), formatDate(dateTime, formatText2));
        viewHolder.setOffset(offSet);

        final int col = ContextCompat.getColor(mContext,
                offSet > 0 ? R.color.colorDarkGrey03 : position == selPos ? R.color.colorPrimary
                        : R.color.white);
        viewHolder.setBackgroundColor(col);

        final int txtColor1 = ContextCompat.getColor(mContext,
                offSet > 0 ? R.color.colorDarkGrey :
                        position == selPos ? R.color.white : R.color.charcoal);

        final int txtColor2 = ContextCompat.getColor(mContext,
                offSet > 0 ? R.color.colorPrimary :
                        position == selPos ? R.color.white : R.color.colorPrimaryDark);


        viewHolder.setTextColors(txtColor1, txtColor2);
    }

    @Override
    public float getPageWidth(int position) {
        return pageWidth;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return entryCount;
    }

    public void setPageWidth(float w) {
        this.pageWidth = w;
    }

    public void setIsMonthPicker(boolean value) {
        this.isMonthPicker = value;
    }

    public int getCurrentItem() {
        return selPos;
    }

    public void setSelectedItem(int position) {
        selPos = position;
        notifyDataSetChanged();
    }

    public void setOnDayPickerSelector(OnDayPickerSelector onDayPickerSelector) {
        this.onDayPickerSelector = onDayPickerSelector;
    }

    private String formatDate(DateTime dateTime, DateTimeFormatter format) {
        return format == null ? null : dateTime.toString(format);
    }

    public static class FooViewHolder extends RecycledPagerAdapter.ViewHolder {
        final TextView tvText1;
        final TextView tvText2;

        private int offSet = 0;
        private int position = 0;

        public FooViewHolder(final View view) {
            super(view);
            tvText1 = (TextView) view.findViewById(R.id.tvText1);
            tvText2 = (TextView) view.findViewById(R.id.tvText2);
        }

        public void show(final int position, final String text1, final String text2) {
            this.position = position;

            this.tvText1.setText(text1);
            this.tvText2.setText(text2);

            this.tvText1.setVisibility(text1 == null ? View.GONE : View.VISIBLE);
            this.tvText2.setVisibility(text2 == null ? View.GONE : View.VISIBLE);
        }

        public void setOffset(int offSet) {
            this.offSet = offSet;
        }

        public void setOnClickListener(final OnDayPickerSelectorInterface onClickListener) {
            if (onClickListener != null) {
                this.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListener.onSelected(position, offSet);
                    }
                });
            }
        }

        public void setBackgroundColor(final int color) {
            this.itemView.setBackgroundColor(color);
        }

        public void setTextColors(final int col1, final int col2) {
            this.tvText1.setTextColor(col1);
            this.tvText2.setTextColor(col2);
        }
    }
}

