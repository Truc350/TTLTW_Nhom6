package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.admin;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ltw_nhom5.dao.ReportDAO;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ReportServlet", urlPatterns = {"/admin/ReportManagement", "/admin/report-data"})
public class ReportServlet extends HttpServlet {
    private ReportDAO reportDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        reportDAO = new ReportDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/admin/ReportManagement".equals(path)) {
            System.out.println("📄 Forwarding to report.jsp");
            request.getRequestDispatcher("/fontend/admin/report.jsp").forward(request, response);
            return;
        }

        if ("/admin/report-data".equals(path)) {
            System.out.println("📊 Handling report-data JSON request");
            handleReportData(request, response);
            return;
        }
    }


    private void handleReportData(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        String filter = request.getParameter("filter");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        LocalDate startDate;
        LocalDate endDate;

        if ("custom".equals(filter) && startDateStr != null && endDateStr != null) {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } else if ("week".equals(filter)) {
            startDate = LocalDate.now().minusDays(6);
            endDate = LocalDate.now();
        } else if ("month".equals(filter)) {
            startDate = LocalDate.now().minusDays(29);
            endDate = LocalDate.now();
        } else {

            startDate = LocalDate.now();
            endDate = LocalDate.now();
        }

        try {
            Map<String, Object> result = new HashMap<>();

            Map<String, Object> kpi = reportDAO.getOverviewStats(startDate, endDate);
            result.put("kpi", kpi);

            List<Map<String, Object>> topProducts = reportDAO.getTopSellingProducts(startDate, endDate, 3);
            result.put("topProducts", topProducts);

            List<Map<String, Object>> dailyData = reportDAO.getDailyRevenue(startDate, endDate);

            Map<String, Object> chartData = new HashMap<>();

            List<String> labels = new ArrayList<>();
            List<Double> revenueData = new ArrayList<>();
            List<Integer> ordersData = new ArrayList<>();
            List<Double> avgValueData = new ArrayList<>();

            for (Map<String, Object> day : dailyData) {
                String dateStr = day.get("date").toString();
                labels.add(dateStr);

                Double revenue = day.get("revenue") != null ?
                        ((Number) day.get("revenue")).doubleValue() : 0.0;
                Integer orderCount = day.get("order_count") != null ?
                        ((Number) day.get("order_count")).intValue() : 0;

                revenueData.add(revenue);
                ordersData.add(orderCount);
                avgValueData.add(orderCount > 0 ? revenue / orderCount : 0.0);

            }

            Map<String, Object> revenueChart = new HashMap<>();
            revenueChart.put("labels", labels);
            revenueChart.put("data", revenueData);

            Map<String, Object> ordersChart = new HashMap<>();
            ordersChart.put("labels", labels);
            ordersChart.put("data", ordersData);

            Map<String, Object> avgValueChart = new HashMap<>();
            avgValueChart.put("labels", labels);
            avgValueChart.put("data", avgValueData);

            chartData.put("revenue", revenueChart);
            chartData.put("orders", ordersChart);
            chartData.put("avgValue", avgValueChart);

            result.put("chartData", chartData);

            String jsonResponse = gson.toJson(result);

            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", true);
            errorResult.put("message", e.getMessage());

            response.getWriter().write(gson.toJson(errorResult));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}