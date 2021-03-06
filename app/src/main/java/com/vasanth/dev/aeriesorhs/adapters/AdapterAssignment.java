package com.vasanth.dev.aeriesorhs.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vasanth.dev.aeriesorhs.R;
import com.vasanth.dev.aeriesorhs.activities.ClassActivity;
import com.vasanth.dev.aeriesorhs.helpers.DataStorageAndParsing;
import com.vasanth.dev.aeriesorhs.objects.Assignment;
import com.vasanth.dev.aeriesorhs.objects.Class;

import java.util.ArrayList;

import static android.graphics.Color.GRAY;

/**
 * Created by Admin on 11/27/2016.
 */

public class AdapterAssignment extends BaseAdapter {
    private Activity activity;
    private static LayoutInflater inflater = null;
    private int color;
    private String TAG = "AdapterAssignment";
    ArrayList<Class> tempClassesAsArrayList;
    public AdapterAssignment(Activity activity, int color) {
        try {
            this.tempClassesAsArrayList = DataStorageAndParsing.classesAsArrayList;
            this.activity = activity;
            this.color = color;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {
            Log.e("ERROR", "ERROR");
            e.printStackTrace();
        }
    }

    public int getCount() {
        return ((ClassActivity) activity).classMain.getAssignments().size();
    }

    @Override
    public Object getItem(int position) {
        return ((ClassActivity) activity).classMain.getAssignments().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView outOf;
        public TextView percentage;
        public TextView graded;
        public ImageButton add;
        public ImageButton subtract;
        public SeekBar seekBar;
        public ImageButton revertButton;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final AdapterAssignment.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.assignment_row, parent, false);
            holder = new AdapterAssignment.ViewHolder();
            holder.display_name = (TextView) convertView.findViewById(R.id.assignmentName);
            holder.outOf = (TextView) convertView.findViewById(R.id.assignmentOutOf);
            holder.percentage = (TextView) convertView.findViewById(R.id.assignmentPercentage);
            holder.graded = (TextView) convertView.findViewById(R.id.assignmentIsGraded);
            holder.add = (ImageButton) convertView.findViewById(R.id.addButton);
            holder.subtract = (ImageButton) convertView.findViewById(R.id.subtractButton);
            holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar);
            holder.revertButton = (ImageButton) convertView.findViewById(R.id.revertButton);
            convertView.setTag(holder);
        } else {
            holder = (AdapterAssignment.ViewHolder) convertView.getTag();
        }
        holder.seekBar.setProgress((int)(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage()*holder.seekBar.getMax()));
        convertView.setBackgroundColor(color);
        holder.display_name.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getName());
        holder.outOf.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + "/" + ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
        holder.percentage.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage() * 100 + "%");
        if(((ClassActivity) activity).classMain.getAssignments().get(position).getTotal()<1f){
            Log.v(TAG,((ClassActivity) activity).classMain.getAssignments().get(position).getTotal()+"" );
            holder.seekBar.setVisibility(View.INVISIBLE);
        }else{
            holder.seekBar.setVisibility(View.VISIBLE);
        }
        final View vi = convertView;
        holder.revertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassActivity) activity).classMain.getAssignments().get(position).revertGrades();
                holder.revertButton.setVisibility(View.INVISIBLE);
                holder.outOf.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + "/" + ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                holder.percentage.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage() * 100 + "%");
                holder.seekBar.setProgress((int)(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage()*holder.seekBar.getMax()));
                holder.graded.setText("No");
                vi.setBackgroundColor(GRAY);
                if(((ClassActivity) activity).classMain.getAssignments().get(position).isCounted()) {
                    ((ClassActivity) activity).classMain.getAssignments().get(position).setCounted(true);
                    holder.graded.setText("Yes");
                    vi.setAlpha(1f);
                    vi.setBackgroundColor(color);
                }

                ((TextView) ((ClassActivity) activity).findViewById(R.id.ClassGrade)).setText(Float.toString(((ClassActivity) v.getContext()).classMain.generateCalculatedGrade()));
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassActivity) activity).classMain.getAssignments().get(position).setWhatYouGot((((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + 1));
                ((ClassActivity) activity).classMain.getAssignments().get(position).setCounted(true);
                ((ClassActivity) activity).classMain.getAssignments().get(position).setPercentage(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() / ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                Log.v("AdapterAssignment", "New Percentage is " + (((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() / ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal() * 100f));
                ((TextView) ((ClassActivity) activity).findViewById(R.id.ClassGrade)).setText(Float.toString(((ClassActivity) v.getContext()).classMain.generateCalculatedGrade()));
                holder.outOf.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + "/" + ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                holder.percentage.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage() * 100 + "%");
                holder.graded.setText("Yes");
                holder.revertButton.setVisibility(View.VISIBLE);
                vi.setAlpha(1f);
                vi.setBackgroundColor(color);
            }
        });
        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ClassActivity) activity).classMain.getAssignments().get(position).setWhatYouGot((((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() - 1));
                ((ClassActivity) activity).classMain.getAssignments().get(position).setCounted(true);
                ((ClassActivity) activity).classMain.getAssignments().get(position).setPercentage(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() / ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                Log.v("AdapterAssignment", "New Percentage is " + (((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() / ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal() * 100f));
                ((TextView) ((ClassActivity) activity).findViewById(R.id.ClassGrade)).setText(Float.toString(((ClassActivity) v.getContext()).classMain.generateCalculatedGrade()));
                holder.outOf.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + "/" + ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                holder.percentage.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage() * 100 + "%");
                holder.graded.setText("Yes");
                vi.setAlpha(1f);
                vi.setBackgroundColor(color);
                holder.revertButton.setVisibility(View.VISIBLE);

            }
        });
        holder.seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // TODO Auto-generated method stub
                        if(!fromUser)
                            return;
                        holder.revertButton.setVisibility(View.VISIBLE);
                        float percentageProgress = ((float)progress)/((float)seekBar.getMax());
                        float newWhatYouGot =(int)(((ClassActivity) activity).classMain.getAssignments().get(position).getTotal()*percentageProgress);
                        Log.v(TAG, "newWhatYouGot: "+newWhatYouGot);
                        ((ClassActivity) activity).classMain.getAssignments().get(position).setWhatYouGot(newWhatYouGot);
                        ((ClassActivity) activity).classMain.getAssignments().get(position).setCounted(true);
                        ((ClassActivity) activity).classMain.getAssignments().get(position).setPercentage(percentageProgress);
                        ((TextView) ((ClassActivity) activity).findViewById(R.id.ClassGrade)).setText(Float.toString(((ClassActivity) activity).classMain.generateCalculatedGrade()));
                        holder.outOf.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getWhatYouGot() + "/" + ((ClassActivity) activity).classMain.getAssignments().get(position).getTotal());
                        holder.percentage.setText(((ClassActivity) activity).classMain.getAssignments().get(position).getPercentage() * 100 + "%");
                        holder.graded.setText("Yes");
                        vi.setAlpha(1f);
                        vi.setBackgroundColor(color);
                    }
                }
        );
        if (((ClassActivity) activity).classMain.getAssignments().get(position).isCounted()) {
            holder.graded.setText("Yes");
            convertView.setAlpha(1f);
        } else {
            holder.graded.setText("No");
            convertView.setBackgroundColor(GRAY);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(vi.getContext(), ClassActivity.class);
                //intent.putExtra("class_index", position);
                //vi.getContext().startActivity(intent);
            }
        });
        return convertView;
    }
}
