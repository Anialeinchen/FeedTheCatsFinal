package com.example.anna.feedthecatsfinal.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anna.feedthecatsfinal.CatsFeedingEvent;
import com.example.anna.feedthecatsfinal.EventType;
import com.example.anna.feedthecatsfinal.R;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

    public void setEvents(List<CatsFeedingEvent> events) {
        this.events = events;
    }

    private List<CatsFeedingEvent> events = new ArrayList<>();

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        CatsFeedingEvent event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView morningShift;
        private TextView eveningShift;
        private TextView caregiverMorning;
        private TextView caregiverEvening;

        public EventViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.event_date);
            morningShift = itemView.findViewById(R.id.shift_morning);
            eveningShift = itemView.findViewById(R.id.shift_evening);
            caregiverMorning = itemView.findViewById(R.id.caregiver_morning);
            caregiverEvening = itemView.findViewById(R.id.caregiver_evening);
        }

        private void bind(CatsFeedingEvent event) {
            date.setText(String.valueOf(event.getDate()));
            caregiverMorning.setText(event.getCaregiverMorning());
            caregiverEvening.setText(event.getCaregiverEvening());

            switch (EventType.values()[event.getType()]) {
                case DAY:
                    morningShift.setBackgroundResource(R.color.active_label);
                    eveningShift.setBackgroundResource(R.color.active_label);
                    break;
                case MORNING:
                    morningShift.setBackgroundResource(R.color.active_label);
                    eveningShift.setBackgroundResource(R.color.inactive_label);
                    break;
                case EVENING:
                    morningShift.setBackgroundResource(R.color.inactive_label);
                    eveningShift.setBackgroundResource(R.color.active_label);
                    break;
            }
        }
    }
}
