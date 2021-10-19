package com.lpastyle.elimsensorapp;

import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lpastyle.elimsensorapp.databinding.SensorListItemBinding;

import java.util.List;

public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.SensorViewHolder> {

    private RecyclerView.ViewHolder mViewHolder;
    private List<Sensor> sensors;
    private RVOnClick rvOnClick;

    public SensorListAdapter(List<Sensor> sensors, RVOnClick rvOnClick) {
        this.sensors = sensors;
        this.rvOnClick = rvOnClick;
    }

    @NonNull
    @Override
    public SensorListAdapter.SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SensorListItemBinding binding = SensorListItemBinding.inflate(layoutInflater, parent, false);
        return new SensorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorListAdapter.SensorViewHolder holder, int position) {
        Sensor sensor = sensors.get(position);
        holder.binding.setSensorItem(sensor);
        //holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }


    public class SensorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SensorListItemBinding binding;

        SensorViewHolder(@NonNull SensorListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            rvOnClick.onItemClick(getAdapterPosition());
        }
    }

    /*
     * REMINDER: The "old" way to proceed using hand made view holdler
     *
    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_list_item, parent, false);
        return new SensorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = sensors.get(position);
        holder.nameTv.setText(sensor.getName());
        holder.typeTv.setText(sensor.getStringType());
        holder.resolutionTv.setText(String.valueOf(sensor.getResolution()));
        holder.maxRangeTv.setText(String.valueOf(sensor.getMaximumRange()));
    }

    public static class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView typeTv;
        TextView resolutionTv;
        TextView maxRangeTv;

        public SensorViewHolder(View v) {
            super(v);
            nameTv = v.findViewById(R.id.nameTv);
            typeTv = v.findViewById(R.id.typeTv);
            resolutionTv = v.findViewById(R.id.resolutionTv);
            maxRangeTv = v.findViewById(R.id.maxRangeTv);

        }
    }
   */

}
