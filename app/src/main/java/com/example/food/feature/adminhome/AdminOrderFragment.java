package com.example.food.feature.adminhome;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food.Adapter.AdminOrderAdapter;
import com.example.food.BuildConfig;
import com.example.food.Domain.Order;
import com.example.food.Domain.Response.OrderDetail;
import com.example.food.R;
import com.example.food.databinding.FragmentAdminOrderBinding;
import com.example.food.feature.pdf.PdfUtil;
import com.example.food.firebase.CloudMessageBody;
import com.example.food.firebase.CloudMessageViewModel;
import com.example.food.firebase.Message;
import com.example.food.util.AppUtils;
import com.example.food.viewmodel.OrderViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;


public class AdminOrderFragment extends Fragment implements AdminOrderAdapter.ClickItem, OrderDetailsFragment.ClickButton {

    FragmentAdminOrderBinding binding;
    AdminOrderAdapter orderAdapter;
    OrderViewModel orderViewModel;
    CloudMessageViewModel cloudMessageViewModel;
    String datePattern = "dd-MM-yyyy";
    String[] orderTimeFilter;
    String[] orderStateFilter;
    String startDate;
    String endDate;
    String orderState;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab;
    Toolbar toolbar;
    AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setControls();
        setEvents();

        setData();
    }

    private void setData() {
        startDate = AppUtils.formatDate(new Date(), datePattern);
        endDate = AppUtils.formatDate(new Date(), datePattern);
        orderState = AppUtils.orderState[0];
        alertDialog.show();
        orderViewModel.callOrdersByStateAndCreateAtBetween(
                orderState,
                startDate,
                endDate);
        setObserver();

    }

    private void setObserver() {
        orderViewModel.getData().observe(requireActivity(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                orderAdapter.submitList(orders);
                alertDialog.dismiss();
            }
        });
        orderViewModel.getMessage().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        orderViewModel.getOrder().observe(requireActivity(),
                order -> sendMessageToCloud(order));
    }

    private void sendMessageToCloud(Order order) {
        Message message =  new Message("Đơn hàng " + order.getId(),
                "Đơn hàng của bạn đã được chấp nhận",
                order.getId(),
                order.getUser().getId());
        String toUser = order.getUser().getTokenFireBase();
        cloudMessageViewModel.callSendMessageToCloud(getString(R.string.authorization_cloud_message),
                new CloudMessageBody(message, toUser));
    }

    private void setEvents() {
        binding.chipGroupOrderTimeFilter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chip_today_order_time_filter:
                        startDate = AppUtils.formatDate(new Date(), datePattern);
                        endDate = AppUtils.formatDate(new Date(), datePattern);
                        break;
                    case R.id.chip_week_order_time_filter:
                        startDate = AppUtils.getFirstDayOfWeekNow();
                        endDate = AppUtils.getLastDayOfWeekNow();
                        break;
                    case R.id.chip_month_time_order_filter:
                        startDate = AppUtils.getFirstDayOfMonthNow();
                        endDate = AppUtils.getLastDayOfMonthNow();

                        break;

                }
//                Log.d("AAA","start date:" + startDate + "-endDate:" + endDate);
                alertDialog.show();
                orderViewModel.callOrdersByStateAndCreateAtBetween(
                        orderState,
                        startDate,
                        endDate);
            }
        });

        binding.chipGroupOrderStateFilter.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chip_unapproved_state_order:
                        orderState = AppUtils.orderState[0];// Đã giao
                        break;
                    case R.id.chip_approved_state_order:
                        orderState = AppUtils.orderState[1];// Đang giao
                        break;
                    case R.id.chip_shiped_state_order:
                        orderState = AppUtils.orderState[2];// Đã giao
                        break;

                }
                alertDialog.show();
                orderViewModel.callOrdersByStateAndCreateAtBetween(
                        orderState,
                        startDate,
                        endDate);
            }
        });

        binding.recyclerViewOrdersState.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    bottomAppBar.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                }else{
                    bottomAppBar.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setControls() {

        alertDialog = new SpotsDialog.Builder().setContext(requireContext()).setTheme(R.style.CustomProgressBarDialog).build();

        bottomAppBar = requireActivity().findViewById(R.id.bottom_bar);
        fab = requireActivity().findViewById(R.id.fab_cart);
        toolbar = requireActivity().findViewById(R.id.tool_bar_admin_main);
        toolbar.setTitle("");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_item_pdf_admin:
//                        Toast.makeText(requireContext(), "Click pdf", Toast.LENGTH_SHORT).show();
                        createPDF();
                        break;
                }
                return false;
            }
        });

        cloudMessageViewModel = new ViewModelProvider(this).get(CloudMessageViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderAdapter = new AdminOrderAdapter(Order.itemCallback, this);
        binding.recyclerViewOrdersState.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        RecyclerView.VERTICAL, false));
        binding.recyclerViewOrdersState.addItemDecoration(
                new DividerItemDecoration(binding.recyclerViewOrdersState.getContext(),
                        DividerItemDecoration.VERTICAL));
        binding.recyclerViewOrdersState.setAdapter(orderAdapter);

        // get filter
        orderTimeFilter = getResources().getStringArray(R.array.ORDER_TIME_FILTER);
        orderStateFilter = getResources().getStringArray(R.array.ORDER_STATE_FILTER);

        setChipFilter();


    }

    private void setChipFilter() {

    }

    private void getFilter(){

    }

    @Override
    public void showDetailsOrder(Order order) {
        OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(order, this);
        orderDetailsFragment.setCancelable(false);
        orderDetailsFragment.show(requireActivity().getSupportFragmentManager(), orderDetailsFragment.getTag());
    }


    @Override
    public void clickButtonAccept(int idOrder, String state) {
//        Toast.makeText(requireContext(), "Click accept button order", Toast.LENGTH_SHORT).show();
        if(state.equals(AppUtils.orderState[0])){// chưa duyệt
            orderViewModel.callUpdateStateOrder(idOrder, AppUtils.orderState[1]); // đang giao
        }

        // send message to user
    }

    @Override
    public void clickButtonAccept2(int idOrder, String state,int userId,  int rating, String comment) {

    }

    public void createPDF(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                showPdf(Common.getAppPath(getContext()) + "test_pdf.pdf");
                try {
                    createInvoice1(orderViewModel.getData().getValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission deny\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private void showPdf(String path) {
        PdfUtil pdf = new PdfUtil();

        if (new File(path).exists()) {
            new File(path).delete();
        }

        try {
            Document document = new Document();
            // save
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            document.setPageSize(PageSize.A4);
            document.setMargins(5f, 5f, 5f, 5f);
            document.addCreationDate();
            document.addAuthor("TNT_HIEN");
            document.addCreator("Hien Nguyen");

            Paragraph paragraph = new Paragraph("");
            pdf.addNewItem(document, "LIST OF ORDER", Element.ALIGN_CENTER, pdf.titleFont);
//            specify column widths
//            if (indexGrade == -1) {
//                return;
//            }
//
//            Grade grade = dropdownItemsGrade.get(indexGrade);
//            Teacher teacher = markViewModel.findTeacherById(grade.getTeacherId());
//            // title pdf
            List<Order> orders = orderViewModel.getData().getValue();
//            pdf.addNewItem(document, "Order ID:" + order.getId() + "           " + "GIÁO VIÊN:" +
//                    teacher.getTeacherName(), Element.ALIGN_CENTER, pdf.bf12);
//            pdf.addNewItem(document, "                              ", Element.ALIGN_CENTER, pdf.bf12);
//
//            //information student
//            List<Student> listStudent = markViewModel.getStudentsByGradeId(grade.getGradeId());
            int i=0;
            for (Order item : orders) {
                pdf.addNewItem(document, "NUMBER: " + (++i), Element.ALIGN_CENTER, pdf.bfBold12);
                // student information
                pdf.addNewItem(document,
                        "ORDER INFORMATION",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);

                pdf.addNewItem(document,
                        String.format("%10s %-50s %-30s", "",
                                "Id:" + item.getId(),
                                "State:" + (item.getState())),
                        Element.ALIGN_LEFT,
                        pdf.bf12);

                pdf.addNewItem(document,
                        String.format("%10s %-37s %-30s", "",
                                "Order user:" + item.getUser().getName(),
                                "Order date:" + AppUtils.formatDate(item.getCreateAt(), "dd-MM-yyyy hh:mm:ss")),
                        Element.ALIGN_LEFT,
                        pdf.bf12);
                // list mark of student

                pdf.addNewItem(document,
                        "ORDER DETAILS",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
                pdf.addNewItem(document,
                        "                ",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
                // order details
                pdf.addNewItem(document,
                        "Total: " + item.getTotalPriceOfProducts(),
                        Element.ALIGN_LEFT,
                        pdf.bf12);
                pdf.addNewItem(document,
                        "List of product:\n" + item.getProductsName(),
                        Element.ALIGN_LEFT,
                        pdf.bf12);
                pdf.addNewItem(document,
                        "Order address: " + item.getUser().getAddress(),
                        Element.ALIGN_LEFT,
                        pdf.bf12);

                pdf.addNewItem(document,
                        "-------------------------------------------------------------",
                        Element.ALIGN_CENTER,
                        pdf.bfBold12);
            }

            //print
            document.close();
            pdf.printPDF(requireContext());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void createInvoice1(List<Order> list) throws IOException {
        int pageWidth = 1200;
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.dp);
//        Bitmap bmpScale = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo myPageInfo;
        PdfDocument.Page myPage1;
        Canvas canvas = null;
        for (int i = 0; i < list.size(); i++) {
            Order t = list.get(i);
            myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, i + 1).create();
            myPage1 = myPdfDocument.startPage(myPageInfo);
            canvas = myPage1.getCanvas();

//        canvas.drawBitmap(bmpScale,0,0,paint);

            titlePaint.setTextAlign(Paint.Align.CENTER);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            titlePaint.setTextSize(90);
            canvas.drawText("THÔNG TIN ĐƠN HÀNG", pageWidth / 2, 270, titlePaint);
            canvas.drawText("Danh sách sản phẩm", pageWidth / 2, 650, titlePaint);



            titlePaint.setTextAlign(Paint.Align.LEFT);
            titlePaint.setColor(Color.GRAY);
            titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            titlePaint.setTextSize(35);
            canvas.drawText("Mã đơn hàng: " + t.getId(), 50, 350, titlePaint);
            canvas.drawText("Trạng thái: " + t.getState(), 700, 350, titlePaint);
            canvas.drawText("Người đặt: " + t.getUser().getName(), 50, 420, titlePaint);
            canvas.drawText("Ngày đặt: " + AppUtils.formatDate(t.getCreateAt(), "dd-MM-yyyy"), 700, 420, titlePaint);

//

            //main
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(35);


            canvas.drawRect(20, 760, pageWidth - 20, 860, paint);



            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("STT", 40, 830, paint);
            canvas.drawText("Mã SP", 130, 830, paint);
            canvas.drawText("Tên sản phầm", 290, 830, paint);
            canvas.drawText("Số lượng", 700, 830, paint);
            canvas.drawText("Đơn giá", 880, 830, paint);
            canvas.drawText("Giảm giá", 1030, 830, paint);

            canvas.drawLine(110, 790, 110, 840, paint);
            canvas.drawLine(260, 790, 260, 840, paint);
            canvas.drawLine(680, 790, 680, 840, paint);
            canvas.drawLine(840, 790, 840, 840, paint);
            canvas.drawLine(1020, 790, 1020, 840, paint);
            int offsetY = 950;
            int total = 0;
            List<OrderDetail> orderDetails = t.getOrderDetails();
            for (int j = 0; j < orderDetails.size(); j++) {
                OrderDetail orderDetail = orderDetails.get(j);
                int tt = j + 1;
                canvas.drawText(tt + "", 40, offsetY, paint);
                canvas.drawText(orderDetail.getProduct().getProductId() + "", 130, offsetY, paint);
                canvas.drawText(orderDetail.getProduct().getName(), 290, offsetY, paint);
                canvas.drawText(orderDetail.getQuantity()+"", 700, offsetY, paint);
                canvas.drawText(orderDetail.getPrice()+"", 880, offsetY, paint);
                canvas.drawText(orderDetail.getDiscount() + "", 1030, offsetY, paint);
                offsetY = offsetY + 60;
                total+=orderDetail.getQuantity();
            }




        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Tổng số lượng:", 700, offsetY + 150, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(total + "", pageWidth - 20, offsetY + 150, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Tổng tiền:", 700, offsetY + 220, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(AppUtils.formatCurrency(t.getTotalPriceOfProducts()), pageWidth - 20, offsetY + 220, paint);

        myPdfDocument.finishPage(myPage1);
    }

        String folder = Environment.getExternalStorageDirectory().getPath() + "/documents";
        File folderFile = new File(folder);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        String path = folder + "/Calcuradora_" + System.currentTimeMillis() + ".pdf";
        File myFile = new File(path);
        FileOutputStream fOut = new FileOutputStream(myFile);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myPdfDocument.writeTo(fOut);
        myPdfDocument.close();
        myOutWriter.close();
        fOut.close();
//        Toast.makeText(requireContext(), "File Saved on " + path, Toast.LENGTH_LONG).show();
        openPdfViewer(myFile);

    }

    private void openPdfViewer(File file) { //need to add provider in manifest and filepaths.xml
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(requireContext(),
                BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 101);
    }
}