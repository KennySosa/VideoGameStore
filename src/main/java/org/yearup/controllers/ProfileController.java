package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;
import org.yearup.service.UserService;

import java.security.Principal;

//so profile controller now acts similar to Shoppingcartcontroller, where every method gets the logged in user id
// thru Principal then calls the service

@RestController
@RequestMapping("profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")// now both get/put require login
public class ProfileController
{
    private ProfileService profileService;
    private UserService userService;

    @Autowired
    public ProfileController(ProfileService profileService, UserService userService)
    {
        this.profileService = profileService;
        this.userService = userService;
    }

    @GetMapping
    public Profile getProfile(Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        Profile profile = profileService.getByUserId(userId);

        if (profile == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        } //this throws 404 error if profile doesnt exist instead of just sayin Null

        return profile;
    }

    @PutMapping
    public Profile updateProfile(@RequestBody Profile profile, Principal principal)
    {
        String userName = principal.getName();
        User user = userService.getByUserName(userName);
        int userId = user.getId();

        return profileService.update(userId, profile);
    }
}