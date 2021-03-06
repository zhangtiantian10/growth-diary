package com.tw.controllers;

import com.tw.entities.Followed;
import com.tw.entities.User;
import com.tw.repositories.FollowedRepository;
import com.tw.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowedRepository followedRepository;

    @RequestMapping(value = "/api/user/{value}/{id}", method = RequestMethod.GET)
    public ResponseEntity findUsers(@PathVariable("value") String value, @PathVariable("id") Long id) {

        List<User> users = userRepository.findByNameContaining(value);
        List<Map> newUsers = new ArrayList<>();

        for (User user : users) {
            Followed followed = followedRepository.findByUserIdAndFollowedUserId(id, user.getId());
            boolean isFollowed = true;
            if (followed == null) {
                isFollowed = false;
            }

            Map<String, Object> u = new HashMap<>();
            u.put("id", user.getId());
            u.put("name", user.getName());
            u.put("isFollowed", isFollowed);
            newUsers.add(u);
        }

        return new ResponseEntity<>(newUsers, HttpStatus.OK);
    }
}
