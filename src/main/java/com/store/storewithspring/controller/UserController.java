package com.store.storewithspring.controller;


import com.store.storewithspring.dto.ChartsDto;
import com.store.storewithspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/charts")
    public ResponseEntity<?> getCharts(){
        List<ChartsDto> listCharts = new ArrayList<>();

        ChartsDto month1 = new ChartsDto();
        month1.setMonth("Tháng 1");
        month1.setCount(0);
        ChartsDto month2 = new ChartsDto();
        month2.setMonth("Tháng 2");
        month2.setCount(0);
        ChartsDto month3 = new ChartsDto();
        month3.setMonth("Tháng 3");
        month3.setCount(0);
        ChartsDto month4 = new ChartsDto();
        month4.setMonth("Tháng 4");
        month4.setCount(0);
        ChartsDto month5 = new ChartsDto();
        month5.setMonth("Tháng 5");
        month5.setCount(0);
        ChartsDto month6 = new ChartsDto();
        month6.setMonth("Tháng 6");
        month6.setCount(0);
        ChartsDto month7 = new ChartsDto();
        month7.setMonth("Tháng 7");
        month7.setCount(0);
        ChartsDto month8 = new ChartsDto();
        month8.setMonth("Tháng 8");
        month8.setCount(0);
        ChartsDto month9 = new ChartsDto();
        month9.setMonth("Tháng 9");
        month9.setCount(0);
        ChartsDto month10 = new ChartsDto();
        month10.setMonth("Tháng 10");
        month10.setCount(0);
        ChartsDto month11 = new ChartsDto();
        month11.setMonth("Tháng 11");
        month11.setCount(0);
        ChartsDto month12 = new ChartsDto();
        month12.setMonth("Tháng 12");
        month12.setCount(0);

        userRepository.findAll().forEach(item->{
          String month = item.getDateCreate().split("-")[1];
          if(month.equals("01")){
              month1.setCount(month1.getCount() + 1);
          }
            if(month.equals("02")){
                month2.setCount(month2.getCount() + 1);
            }
            if(month.equals("03")){
                month3.setCount(month3.getCount() + 1);
            }
            if(month.equals("04")){
                month4.setCount(month4.getCount() + 1);
            }

            if(month.equals("05")){
                month5.setCount(month5.getCount() + 1);
            }
            if(month.equals("06")){
                month6.setCount(month6.getCount() + 1);
            }

            if(month.equals("07")){
                month7.setCount(month7.getCount() + 1);
            }
            if(month.equals("08")){
                month8.setCount(month8.getCount() + 1);
            }

            if(month.equals("09")){
                month9.setCount(month9.getCount() + 1);
            }
            if(month.equals("10")){
                month10.setCount(month10.getCount() + 1);
            }

            if(month.equals("11")){
                month11.setCount(month11.getCount() + 1);
            }

            if(month.equals("12")){
                month12.setCount(month12.getCount() + 1);
            }
        });

        listCharts.add(month1);
        listCharts.add(month2);
        listCharts.add(month3);
        listCharts.add(month4);
        listCharts.add(month5);
        listCharts.add(month6);
        listCharts.add(month7);
        listCharts.add(month8);
        listCharts.add(month9);
        listCharts.add(month10);
        listCharts.add(month11);
        listCharts.add(month12);


        return ResponseEntity.ok().body(listCharts);
    }
}

