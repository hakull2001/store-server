package com.bookshop.controllers;

import com.bookshop.base.BaseController;
import com.bookshop.dao.PaymentUser;
import com.bookshop.dao.User;
import com.bookshop.dto.pagination.PaginateDTO;
import com.bookshop.exceptions.NotFoundException;
import com.bookshop.services.PaymentUserService;
import com.bookshop.specifications.GenericSpecification;
import com.bookshop.specifications.JoinCriteria;
import com.bookshop.specifications.SearchCriteria;
import com.bookshop.specifications.SearchOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.JoinType;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@SecurityRequirement(name = "Authorization")
public class PaymentController extends BaseController<PaymentUser> {
    @Autowired
    private PaymentUserService paymentUserService;

    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @GetMapping("/total")
    public ResponseEntity<?> getPaymentTotal(HttpServletRequest request){
        User user = (User) request.getAttribute("user");
        return new ResponseEntity<>(paymentUserService.totalPayment(user.getId()), HttpStatus.OK);
    }


    @PreAuthorize("@userAuthorizer.isMember(authentication)")
    @GetMapping
    public ResponseEntity<?> getPaymentByUser(HttpServletRequest request,
                                              @RequestParam(name = "page", required = false) Integer page,
                                              @RequestParam(name = "perPage", required = false) Integer perPage) {
        User user = (User) request.getAttribute("user");
        GenericSpecification<PaymentUser> specification = new GenericSpecification<PaymentUser>().getBasicQuery(request);
        specification.buildJoin(new JoinCriteria(SearchOperation.EQUAL, "user", "id",
                user.getId(), JoinType.INNER));
        PaginateDTO<PaymentUser> paginatePayments = paymentUserService.getListPayments(page, perPage, specification);
        return this.resPagination(paginatePayments);
    }

    @PreAuthorize("@userAuthorizer.isAdmin(authentication)")
    @GetMapping("/admin")
    public ResponseEntity<?> getAllPayment(HttpServletRequest request,
                                           @RequestParam(name = "fromDate", required = false) String fromDate,
                                           @RequestParam(name = "toDate", required = false) String toDate,
                                           @RequestParam(name = "page", required = false) Integer page,
                                           @RequestParam(name = "perPage", required = false) Integer perPage) {
        GenericSpecification<PaymentUser> specification = new GenericSpecification<PaymentUser>().getBasicQuery(request);
        if (fromDate != null && toDate == null) {
            specification.add(new SearchCriteria("createdAt", fromDate, SearchOperation.FROM_DATE));
        } else if (fromDate == null && toDate != null) {
            specification.add(new SearchCriteria("createdAt", toDate, SearchOperation.TO_DATE));
        } else if (fromDate != null) {
            specification.add(new SearchCriteria("createdAt", fromDate, SearchOperation.FROM_DATE));
            specification.add(new SearchCriteria("createdAt", toDate, SearchOperation.TO_DATE));
        }
        specification.add(new SearchCriteria("paymentStatus", 1L, SearchOperation.EQUAL));
        PaginateDTO<PaymentUser> paginatePayments = paymentUserService.getList(page, perPage, specification);
        return this.resPagination(paginatePayments);
    }
}
