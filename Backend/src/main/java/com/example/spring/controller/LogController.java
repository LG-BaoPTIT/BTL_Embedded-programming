package com.example.spring.controller;

import com.example.spring.payload.request.DataSearch;
import com.example.spring.repository.LogRepositoryCustom;
import com.example.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LogController {
    @Autowired
    LogRepositoryCustom logRepositoryCustom;

    @Autowired
    private UserService userService;

    @PostMapping("/SearchLog")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getLogDoor(@RequestBody DataSearch dataSearch){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String homeId = userService.getHomeIdByUserName(authentication.getName());
        dataSearch.setHomeId(homeId);

        if(dataSearch.getType().equals("door")){
            return ResponseEntity.ok(logRepositoryCustom.getLogDoor(dataSearch));
        }
        if(dataSearch.getType().equals("dht")){
            return ResponseEntity.ok(logRepositoryCustom.getLogDht(dataSearch));

        }
        if(dataSearch.getType().equals("gas")) {
            return ResponseEntity.ok(logRepositoryCustom.getLogGas(dataSearch));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ERROR");
    }
}
