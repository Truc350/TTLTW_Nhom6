package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.VoucherDao;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Voucher;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet("/admin/vouchers")
public class VoucherServlet extends HttpServlet {

    private VoucherDao voucherDao;

    public void init() throws ServletException {
        voucherDao  = new VoucherDao();

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        String target = request.getParameter("discount_target");
        String type = request.getParameter("discount_type");
        BigDecimal value = new BigDecimal(request.getParameter("discount_value"));
        BigDecimal min_order_amount = new BigDecimal(request.getParameter("min_order_amount"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String scope =   request.getParameter("apply_scope");
        boolean is_single_use =Boolean.parseBoolean(request.getParameter("is_single_use"));
        LocalDateTime start_date =LocalDateTime.parse(request.getParameter("start_date"));
        LocalDateTime end_date =LocalDateTime.parse(request.getParameter("end_date"));

        int used_count =0;

        Voucher voucher = new Voucher(code, type, used_count, value, target,  scope, quantity,
                                        start_date, end_date, min_order_amount,is_single_use);
        boolean isSuccess = voucherDao.addVoucher(voucher);
        if (isSuccess) {
            System.out.println("Successfully added voucher");
        }

        response.sendRedirect(request.getContextPath() + "/admin/vouchersManagement");
    }
}