package com.cc_rental.user.apis.controllers;

import com.cc_rental.user.apis.containers.EmailFindResultContainer;
import com.cc_rental.user.apis.enums.EmailFindResult;
import com.cc_rental.user.apis.enums.UserRegisterResult;
import com.cc_rental.user.apis.vos.EmailFindVo;
import com.cc_rental.user.apis.vos.UserLoginVo;
import com.cc_rental.user.apis.enums.UserLoginResult;
import com.cc_rental.user.apis.services.UserService;
import com.cc_rental.user.apis.vos.UserRegisterVo;
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
    public String RegisterPost(HttpServletRequest request, HttpServletResponse response,
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
    public String FindEmailPost(HttpServletRequest request, HttpServletResponse response,
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
}