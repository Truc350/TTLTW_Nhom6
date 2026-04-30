package vn.edu.hcmuaf.fit.ttltw_nhom6.controller.admin.voucher;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.hcmuaf.fit.ttltw_nhom6.dao.VoucherDao;
import vn.edu.hcmuaf.fit.ttltw_nhom6.model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/sortVoucher")
public class SortVoucherServlet extends HttpServlet {

    private VoucherDao voucherDao;

    @Override
    public void init() {
        voucherDao = new VoucherDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        List<Voucher> list;

        if ("status".equals(sortBy)) {
            list = voucherDao.sortByStatus();
        } else if ("expiring".equals(sortBy)) {
            list = voucherDao.getExpiringSoon();
        } else {
            list = voucherDao.getAllSorted(sortBy, order);
        }

        request.setAttribute("allVouchers", list);
        request.getRequestDispatcher("/frontend/admin/Promotion.jsp")
                .forward(request, response);
    }
}
