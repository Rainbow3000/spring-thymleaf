package com.store.storewithspring.controller;


import com.mysql.cj.Session;
import com.store.storewithspring.dto.OrderDto;
import com.store.storewithspring.dto.ProductDto;
import com.store.storewithspring.entity.Order;
import com.store.storewithspring.entity.OrderDetails;
import com.store.storewithspring.entity.Product;
import com.store.storewithspring.entity.User;
import com.store.storewithspring.repository.OrderDetailsRepository;
import com.store.storewithspring.repository.OrderRepository;
import com.store.storewithspring.repository.ProductRepository;
import com.store.storewithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @PostMapping("/order")
    public String createOrder(OrderDto orderDto, HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        Order order = new Order();
        order.setOrderDate(java.time.LocalDate.now().toString());
        User user = userRepository.findById(Long.parseLong(session.getAttribute("userId").toString())).orElse(null);
        order.setUserName(orderDto.getUserName());
        order.setPayStatus("CHỜ GIAO HÀNG");
        order.setUserAddress(orderDto.getUserAddress());
        order.setUserPhone(orderDto.getUserPhone());
        order.setTotalPrice(Double.parseDouble(session.getAttribute("totalAll").toString()));
        order.setUser(user);
        Order orderResponse = orderRepository.save(order);
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder(order);
        List<ProductDto> productList = (List<ProductDto>)session.getAttribute("products");
        productList.forEach(item->{
            Product product = productRepository.findById(item.getId()).orElse(null);
            orderDetails.setProduct(product);
            orderDetails.setProductName(product.getProductName());
            orderDetails.setTotal(item.getQuantity() * item.getPriceNew());
            orderDetails.setQuantity(item.getQuantity());
        });

        orderDetailsRepository.save(orderDetails);
        session.setAttribute("count",0);
        List<ProductDto> products = new ArrayList<>();
        session.setAttribute("products",products);
        return "redirect:/success";
    }

    @GetMapping("/admin/order")
    public String getOrder(Model model,HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        model.addAttribute("orders",orderRepository.findAll());
        return "/admin/order";
    }

    @GetMapping("/admin/order/details/{id}")
    public String getOrderDetails(Model model, @PathVariable Long id,HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        Order order = orderRepository.findById(id).orElseThrow(null);
        model.addAttribute("ordersDetails",order.getOrderDetails());
        return "/admin/order-details";
    }



    @GetMapping("/admin/order/confirm/{id}")
    public String confirmOrder(Model model, @PathVariable Long id,HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        Order order = orderRepository.findById(id).orElseThrow(null);
        order.setPayStatus("ĐÃ NHẬN HÀNG");
        orderRepository.save(order);
        return "redirect:/admin/order";
    }
}
