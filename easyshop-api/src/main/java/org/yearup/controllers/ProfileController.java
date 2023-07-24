package org.yearup.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;


@CrossOrigin
@RestController
@RequestMapping("profile")
@PreAuthorize("hasRole('ROLE_USER')")
public class ProfileController
{
    private final ProfileDao profileDao;
    private final UserDao userDao;

    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping()
    public Profile getProfile(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return profileDao.getProfile(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }


    @PutMapping()
    public void updateProfile(@RequestBody Profile profile
                                , Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            profileDao.update(userId, profile);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... Our bad.");
        }
    }
}
