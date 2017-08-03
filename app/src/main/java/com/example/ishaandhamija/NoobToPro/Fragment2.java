package com.example.ishaandhamija.NoobToPro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by ishaandhamija on 22/04/17.
 */

class Fragment2 extends Fragment{

    BarChart barChart;
    ArrayList<String> crimeName;
    ArrayList<BarEntry> barEntries;

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment2,container,false);

        crimeName=new ArrayList<>();
        barEntries=new ArrayList<>();
        barChart=(BarChart) rootView.findViewById(R.id.bargraph);

        createArray();

        BarDataSet barDataSet = new BarDataSet(barEntries,"Crimes");
        BarData barData = new BarData(crimeName,barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setDescription("Crimes in India");


        return rootView;

    }

    public void createArray(){

        crimeName.add("Murder");
        barEntries.add(new BarEntry(281,0));
        crimeName.add("Kidnapping");
        barEntries.add(new BarEntry(207,1));
        crimeName.add("Robbery");
        barEntries.add(new BarEntry(160,2));
        crimeName.add("Burglary");
        barEntries.add(new BarEntry(792,3));
    }

}
