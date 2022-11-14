package com.application.iserv.myapp.services;

import com.application.iserv.myapp.repositories.MyAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyAppService {

    private final MyAppRepository myAppRepository;

    @Autowired
    public MyAppService(MyAppRepository myAppRepository) {
        this.myAppRepository = myAppRepository;
    }

    public String getImage() {
        List<String> imageList = myAppRepository.getImage();
        return imageList.get(0);
    }


}
