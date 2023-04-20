package com.store.storewithspring.controller;

import com.store.storewithspring.entity.Category;
import com.store.storewithspring.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class CategoryController {

    public static String url = "http://localhost:8080";
    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("/admin/category-list")
    public String categoryList(Model model, HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        List<Category> categoryRepositoryList = categoryRepository.findAll();
        model.addAttribute("categoryRepositoryList",categoryRepositoryList);
        return "/admin/category-list";
    }

    @PostMapping("/admin/category-create")
    public String postCreate(Category category){

        categoryRepository.save(category);
        return "redirect:/admin/category-list";
    }


    @GetMapping("/admin/category-create")
    public String getCreate(Model model){
        return "/admin/category-create";
    }


    @GetMapping("/admin/category-update/{id}")
    public String getUpdate(@PathVariable Long id,Model model){
        Category category = categoryRepository.findById(id).orElse(null);
        model.addAttribute("category",category);
        return "/admin/category-update";

    }

    @PostMapping("/admin/category-update/{id}")
    public String postUpdate(Category categoryRequest,@PathVariable Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        category.setCategoryName(categoryRequest.getCategoryName());
        category.setStatus(categoryRequest.getStatus());
        categoryRepository.save(category);
        return "redirect:/admin/category-list";

    }

    @GetMapping("/admin/category-delete/{id}")
    public String delete(@PathVariable Long id){
            Category category = categoryRepository.findById(id).orElse(null);
            categoryRepository.delete(category);
            return "redirect:/admin/category-list";

    }

}
