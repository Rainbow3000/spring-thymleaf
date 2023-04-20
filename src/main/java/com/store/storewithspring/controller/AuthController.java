package com.store.storewithspring.controller;


import com.store.storewithspring.dto.UserDto;
import com.store.storewithspring.entity.User;
import com.store.storewithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String postRegister(UserDto userRequest, HttpSession session){

        if(userRequest.getUserName().trim().length() == 0 ||
                userRequest.getPassword().trim().length() == 0||
                userRequest.getRePassword().trim().length() == 0 ){
            session.setAttribute("msgErr","Vui lòng điền đẩy đủ thông tin !");
            return "redirect:/register";
        }

       if(userRequest.getUserName().length() < 5){
           session.setAttribute("msgErr","Tài khoản phải từ 5 kí tự trở lên !");
           return "redirect:/register";
       }

       if(userRequest.getPassword().length() < 8 || userRequest.getRePassword().length() < 8){
           session.setAttribute("msgErr","Mật khẩu phải từ 8 kí tự trở lên !");
           return "redirect:/register";
       }



        if(!userRequest.getPassword().equals(userRequest.getRePassword())){
            session.setAttribute("msgErr","Mật khẩu nhập không khớp !");
            return "redirect:/register";
        }
        String password = passwordEncoder.encode(userRequest.getPassword());
        User user = new User();
        user.setUsername(userRequest.getUserName());
        user.setPassword(password);
        user.setRole("USER");
        user.setDateCreate(java.time.LocalDate.now().toString());
        userRepository.save(user);

        session.setAttribute("msg","Đăng kí tài khoản thành công !");
        return "redirect:/register";
    }

    @GetMapping("/register")
    public String getRegister(HttpSession session, HttpServletRequest request){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser != null){
            return "redirect:/";
        }
        return "/web/register";
    }

    @PostMapping("/login")
    public String login(UserDto user,HttpSession session){
        if(user.getUserName().trim().length() == 0 || user.getPassword().trim().length() == 0){
            session.setAttribute("errorLogin","Vui lòng điền đẩy đủ thông tin !");
            return "redirect:/login";
        }

        if(user.getUserName().length() < 5 ){
            session.setAttribute("errorLogin","Tài khoản phải lớn hơn hoặc bằng 5 kí tự !");
            return "redirect:/login";
        }

        if(user.getPassword().length() < 8 ){
            session.setAttribute("errorLogin","Mật khẩu phải lớn hơn hoặc bằng 8 kí tự ! ");
            return "redirect:/login";
        }

        User userExist = userRepository.findByUsername(user.getUserName());
        if(userExist != null){
            boolean isPasswordMatch = passwordEncoder.matches(user.getPassword(), userExist.getPassword());
            if(isPasswordMatch){
                session.setAttribute("userId",userExist.getId());
                session.setAttribute("username",userExist.getUsername());

                if(userExist.getRole().equals("ADMIN")){
                    return "redirect:/admin/home";
                }

                return "redirect:/";
            }else{
                session.setAttribute("errorLogin","Mật khẩu không chính xác !");
                return "redirect:/login";
            }
        }else{
            session.setAttribute("errorLogin","Tài khoản không tồn tại !");
            return "redirect:/login";
        }
    }

    @GetMapping("/login")
    public String getLogin(User user,HttpSession session, HttpServletRequest request){
        Object sessionUser = session.getAttribute ("username");
        if(sessionUser != null){
            return "redirect:/";
        }
        return "/web/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/logout")
    public String adminLogout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin/user")
    public String getUserList(Model model){

        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(item->{
            if(!item.getRole().equals("ADMIN")){
                users.add(item);
            }
        });
       model.addAttribute("users",users);
        return "/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
        return "redirect:/admin/user";
    }

}
