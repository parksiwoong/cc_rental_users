package com.cc_rental.user.apis.controllers;

import com.cc_rental.user.apis.containers.EmailFindResultContainer;
import com.cc_rental.user.apis.enums.*;
import com.cc_rental.user.apis.vos.*;
import com.cc_rental.user.apis.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/apis/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String loginPost(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(name = "email", defaultValue = "") String email,
                            @RequestParam(name = "password", defaultValue = "") String password)
            throws SQLException {
        UserLoginVo userLoginVo = new UserLoginVo(email, password, request);
        UserLoginResult userLoginResult = this.userService.login(userLoginVo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", userLoginResult.name().toLowerCase());
        return jsonResponse.toString(4);
//        Converter.setUserVo(request, null);
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPost(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(name = "email", defaultValue = "") String email,
                               @RequestParam(name = "password", defaultValue = "") String password,
                               @RequestParam(name = "name", defaultValue = "") String name,
                               @RequestParam(name = "contact", defaultValue = "") String contatct,
                               @RequestParam(name = "address", defaultValue = "") String address,
                               @RequestParam(name = "license_type", defaultValue = "") String license_type,
                               @RequestParam(name = "license_number", defaultValue = "") String license_number,
                               @RequestParam(name = "license_issue_date", defaultValue = "") String license_issue_date)
            throws SQLException {
        UserRegisterVo userRegisterVo = new UserRegisterVo(email, password, name, contatct, address, license_type, license_number, license_issue_date);
        UserRegisterResult userRegisterResult = this.userService.Register(userRegisterVo);
        request.getSession().setAttribute("UserRegisterResult", userRegisterResult);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", userRegisterResult.name().toLowerCase());
        return jsonResponse.toString(4);

    }

    @RequestMapping(value = "/find_email",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String findEmailPost(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(name = "name", defaultValue = "") String name,
                                @RequestParam(name = "contact", defaultValue = "") String contact)
            throws SQLException {
        EmailFindVo emailFindVo = new EmailFindVo(name, contact);
        EmailFindResultContainer emailFindResultContainer = this.userService.findEmail(emailFindVo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", emailFindResultContainer.getFindEmailResult().name().toLowerCase());
        if (emailFindResultContainer.getFindEmailResult() == EmailFindResult.SUCCSES) {
            jsonResponse.put("email", emailFindResultContainer.getEmail());
        }
        return jsonResponse.toString(4);
    }
    @RequestMapping(value = "find_password", method = RequestMethod.GET)
    public String findPasswordGet(HttpServletRequest request, HttpServletResponse response) {
        return "user/find_password";
    }

    @RequestMapping(
            value = "find_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String findPasswordPost(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(name = "email", defaultValue = "") String email,
                                   @RequestParam(name = "name", defaultValue = "") String name,
                                   @RequestParam(name = "contact", defaultValue = "") String contact) throws
            SQLException {
        FindPasswordVo findPasswordVo = new FindPasswordVo(email, name, contact);
        PasswordFindResult passwordFindResult = this.userService.findPassword(findPasswordVo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", passwordFindResult.name().toLowerCase());
        return jsonResponse.toString(4);
    }

    @RequestMapping(value = "reset_password", method = RequestMethod.POST)
    public String resetPasswordPost(HttpServletRequest request, HttpServletResponse response,
                                    @RequestParam(name = "password", defaultValue = "") String password,
                                    @RequestParam(name = "key", defaultValue = "") String key) throws
            SQLException {
        ResetPasswordVo resetPasswordVo = new ResetPasswordVo(password, key);
        ResetPasswordResult resetPasswordResult = this.userService.resetPassword(resetPasswordVo);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("result", resetPasswordResult.name().toLowerCase());
        return jsonResponse.toString(4);
    }
}