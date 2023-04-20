package com.store.storewithspring.controller;

import com.store.storewithspring.dto.ProductDto;
import com.store.storewithspring.entity.Category;
import com.store.storewithspring.entity.Product;
import com.store.storewithspring.repository.CategoryRepository;
import com.store.storewithspring.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/admin/product-list")
    public String getProduct(Model model,HttpSession session){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/";
        }
        List<Product> bestSell = new ArrayList<>();

        productRepository.findAll().forEach(item->{
            bestSell.add(item);
        });



        model.addAttribute("productBestSell",bestSell);

        return "/admin/product-list";
    }

    @GetMapping("/admin/product-create")
    public String getCreate(){
        return "/admin/product-create";
    }


    @PostMapping("/admin/product-create")
    public String postCreate(@RequestBody ProductDto productRequest,HttpSession session ){

        try{
            if(productRequest.getCategoryId() > 3){
                session.setAttribute("msgErr","Danh mục sản phẩm không hợp lệ !");
                return "/admin/product-list";
            }
            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
            Product product = new Product();
            product.setCategory(category);
            product.setProductName(productRequest.getProductName());
            product.setProductImage(productRequest.getProductImage());
            product.setProductDescription(productRequest.getProductDescription());
            product.setPriceNew(productRequest.getPriceNew());
            product.setPriceOld(productRequest.getPriceOld());
            productRepository.save(product);
            return "redirect:/admin/product-list";
        }catch (Exception ex){
            return "redirect:/admin/product-list";
        }
    }


    @PutMapping("/admin/product-update/{id}")
    public String putUpdateProduct (@PathVariable Long id, @RequestBody ProductDto productDto){
        Product product = productRepository.findById(id).orElse(null);
        product.setProductName(productDto.getProductName());
        product.setProductImage(productDto.getProductImage());
        product.setPriceOld(productDto.getPriceOld());
        product.setPriceNew(productDto.getPriceNew());
        product.setProductDescription(productDto.getProductDescription());
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        product.setCategory(category);
        productRepository.save(product);
        return "/admin/product-list";
    }

    @GetMapping("/admin/product-update/{id}")
    public  String getUpdateProduct(@PathVariable Long id, Model model){
        model.addAttribute("product",productRepository.findById(id).orElse(null));
        return "/admin/product-update";
    }

    @GetMapping("/admin/product-delete/{id}")
    public String delete(@PathVariable Long id){
        Product product = productRepository.findById(id).orElse(null);
        productRepository.delete(product);
        return "redirect:/admin/product-list";
    }

    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session){

        Object sessionUser = session.getAttribute ("username");
        if(sessionUser == null){
            return "redirect:/login";
        }

        Product product = productRepository.findById(id).orElse(null);
        ProductDto productDto = new ProductDto();
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductImage(product.getProductImage());
        productDto.setPriceNew(product.getPriceNew());
        productDto.setPriceOld(product.getPriceOld());
        productDto.setCategory(product.getCategory());
        productDto.setId(product.getId());
        int count = 0;
        if( session.getAttribute("count") == null){
            List<ProductDto> products = new ArrayList<>();
            productDto.setQuantity(1);
            productDto.setTotalPrice(productDto.getPriceNew() * productDto.getQuantity());
            products.add(productDto);
            count++;
            session.setAttribute("count",count);
            session.setAttribute("products",products);
        }else {
            String value = session.getAttribute("count").toString();
            int c = Integer.parseInt(value) + 1;
            List<ProductDto> products = (List<ProductDto>)session.getAttribute("products");
            int flag = 0;
            for(int i = 0; i < products.size() ; i++){
                if(products.get(i).getId() == id){
                    flag = 1;
                }
            }

            if(flag != 0){
                products.forEach(item->{
                    if(item.getId() == id){
                        item.setQuantity(item.getQuantity() + 1);
                        item.setTotalPrice(item.getPriceNew() * item.getQuantity());
                    }
                });
                return "redirect:/cart";
            }else {
                productDto.setQuantity(1);
                products.add(productDto);
                session.setAttribute("products",products);
                session.setAttribute("count",c);
            }
        }

        double totalAll = 0;
        List<ProductDto> products = (List<ProductDto>)session.getAttribute("products");

        for(int i = 0; i < products.size() ; i++){
              totalAll += products.get(i).getTotalPrice();
        }
        session.setAttribute("totalAll",totalAll);

        return "redirect:/cart";
    }

    @GetMapping("/cart-update-decrement/{id}")
    public String cartUpdateDecrement(@PathVariable Long id,HttpSession session){
        List<ProductDto> products = (List<ProductDto>)session.getAttribute("products");
        products.forEach(item->{
            if(item.getId() == id){
                if(item.getQuantity()  == 1){
                    return;
                }else {
                    String getCount = session.getAttribute("count").toString();
                    int count = Integer.parseInt(getCount) - 1;

                    session.setAttribute("count",count);

                    item.setQuantity(item.getQuantity() - 1);
                    item.setTotalPrice(item.getPriceNew() * item.getQuantity());
                }
            }
        });

        double totalAll = 0;


        for(int i = 0; i < products.size() ; i++){
            totalAll += products.get(i).getTotalPrice();
        }
        session.setAttribute("totalAll",totalAll);
        return "redirect:/cart";
    }

    @GetMapping("/cart-update-increment/{id}")
    public String cartUpdateIncrement(@PathVariable Long id,HttpSession session){
        List<ProductDto> products = (List<ProductDto>)session.getAttribute("products");
        products.forEach(item->{
            if(item.getId() == id){
                    item.setQuantity(item.getQuantity() + 1);
                    item.setTotalPrice(item.getPriceNew() * item.getQuantity());
                    String getCount = session.getAttribute("count").toString();
                    int count = Integer.parseInt(getCount) + 1;
                    session.setAttribute("count",count);
            }
        });
        double totalAll = 0;

        for(int i = 0; i < products.size() ; i++){
            totalAll += products.get(i).getTotalPrice();
        }
        session.setAttribute("totalAll",totalAll);
        return "redirect:/cart";
    }

    @GetMapping("/cart-delete/{id}")
    public String deleteCart(@PathVariable Long id,HttpSession session){
        List<ProductDto> productList = (List<ProductDto>)session.getAttribute("products");
        List<ProductDto> products = new ArrayList<>();
        productList.forEach(item->{
            if(item.getId() != id){
                products.add(item);
            }
        });
        String getCount = session.getAttribute("count").toString();
        int count = Integer.parseInt(getCount) - 1;
        session.setAttribute("count",count);
        session.setAttribute("products",products);
        return "redirect:/cart";
    }



}
