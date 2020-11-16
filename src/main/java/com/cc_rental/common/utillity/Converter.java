package com.cc_rental.common.utillity;

import com.cc_rental.common.vos.UserVo;

import javax.servlet.http.HttpServletRequest;

public class Converter {
    private Converter(){}

    public static void setUserVo(HttpServletRequest request, UserVo userVo) {
        request.getSession().setAttribute("UserVo", userVo);
    }

//     converter.setUserVo(request, null);

    public static UserVo getUserVo(HttpServletRequest request) {
        Object userVoObject = request.getSession().getAttribute("UserVo");
        UserVo userVo = null;
        if (userVoObject instanceof UserVo) {
            userVo = (UserVo) userVoObject;
        }
        return userVo;
    }
}