package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.VoucherDao;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/vouchersManagement")
public class VoucherDisplay extends HttpServlet {
    private VoucherDao voucherDao;

    @Override
    public void init() throws ServletException {
        voucherDao = new VoucherDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Voucher> allVouchers = voucherDao.getAllVouchers();
        request.setAttribute("allVouchers", allVouchers);
        request.getRequestDispatcher("/fontend/admin/promotion.jsp")
                .forward(request, response);
    }


}