package com.example.food.feature.adminhome;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Position;
import com.example.food.Domain.ProductReport;
import com.example.food.Domain.Response.ProductReportResponse;
import com.example.food.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.example.food.feature.product.ProductViewModel;
import com.example.food.util.AppUtils;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class HomeAdminFragment extends Fragment {



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SavedState saveState;
    private ListView listView;
    private View view;
    private ProductViewModel productViewModel;
    List<ProductReport> productReports;

    public HomeAdminFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_admin, container, false);

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        listView = view.findViewById(R.id.list_view);

        ArrayList<String> array = new ArrayList<>();
        for(int i=1; i<=30; i++){
            array.add("this is " + i);
        }

        ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, array);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String startDate = AppUtils.getFirstDayOfMonthNow();
        String endDate = AppUtils.getLastDayOfMonthNow();

        productViewModel.getProductRevenue(startDate,endDate, 5, 0 )
                .subscribe(new SingleObserver<Response<ProductReportResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<ProductReportResponse> productReportResponseResponse) {
                        if(productReportResponseResponse.code()==200){
                            productReports = productReportResponseResponse.body().getDate();
                            ColumnChart();
                        }else {
                            try {
                                Toast.makeText(requireContext(), productReportResponseResponse.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @SuppressLint("CheckResult")
    public void ColumnChart(){

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        if(productReports.size()>0){
            productReports.stream()
                    .forEach(productReport -> {
                        data.add(new ValueDataEntry(productReport.getName(), productReport.getRevenue()));
                    });
        }
//        data.add(new ValueDataEntry("1", 80540));
//        data.add(new ValueDataEntry("2", 94190));
//        data.add(new ValueDataEntry("3", 102610));
//        data.add(new ValueDataEntry("4", 110430));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(5d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Xếp hạng sản phẩm có doanh thu cao nhất");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Tên sản phẩm");
        cartesian.yAxis(0).title("Doanh thu(vnd)");

        anyChartView.setChart(cartesian);
    }



}