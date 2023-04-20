package com.store.storewithspring.controller;


import com.store.storewithspring.dto.UserDto;
import com.store.storewithspring.entity.Product;
import com.store.storewithspring.entity.User;
import com.store.storewithspring.repository.ProductRepository;
import com.store.storewithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/")
    public String homePage(Model model){
        List<Product> bestSell = new ArrayList<>();
        List<Product> fashion = new ArrayList<>();
        List<Product> other = new ArrayList<>();

        productRepository.findAll().forEach(item->{
            if(item.getCategory().getId() == 1){
                bestSell.add(item);
            }

            if(item.getCategory().getId() == 2){
                fashion.add(item);
            }

            if(item.getCategory().getId() == 3){
                other.add(item);
            }

        });

        model.addAttribute("productBestSell",bestSell);
        model.addAttribute("productFashion",fashion);
        model.addAttribute("productOther",other);


        return "/web/index";
    }

    @GetMapping("/admin/home")
    public String adminPage(Model model,HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        return "/admin/index";
    }


    @GetMapping("/details/{id}")
    public String details(Model model, @PathVariable Long id){
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product",product);
        return "/web/details";
    }

    @GetMapping("/cart")
    public String cart(HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/login";
        }
        return "/web/cart";
    }

    @GetMapping("/success")
    String success (){
        return "/web/success";
    }



}
