package com.example.adwork.Tools;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adwork.R;

public class ApplicationAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LayoutInflater inflater;

    public ApplicationAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (cursor != null && !cursor.isClosed()) {
            return cursor.getCount();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            return cursor;
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            int appIdIndex = cursor.getColumnIndex("app_id");
            if (appIdIndex != -1) {
                return cursor.getLong(appIdIndex);
            }
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shenqin, parent, false);
            holder = new ViewHolder();
            holder.tvAppId = convertView.findViewById(R.id.tv_app_id);
            holder.tvReason = convertView.findViewById(R.id.tv_reason);
            holder.tvDateRange = convertView.findViewById(R.id.tv_date_range);
            holder.tvSubmitTime = convertView.findViewById(R.id.tv_submit_time);
            holder.tvStatus = convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (cursor != null && cursor.moveToPosition(position)) {
            int appId = cursor.getInt(cursor.getColumnIndexOrThrow("app_id"));
            String reason = cursor.getString(cursor.getColumnIndexOrThrow("reason"));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));
            String submitTime = cursor.getString(cursor.getColumnIndexOrThrow("submit_time"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            holder.tvAppId.setText("外出住宿申请 #" + appId);
            holder.tvReason.setText(reason);
            holder.tvDateRange.setText("时间: " + startDate + " 至 " + endDate);
            holder.tvSubmitTime.setText("提交于 " + submitTime);
            holder.tvStatus.setText(status);
        }

        return convertView;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvAppId;
        TextView tvReason;
        TextView tvDateRange;
        TextView tvSubmitTime;
        TextView tvStatus;
    }
}
