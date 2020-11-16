package com.cc_rental.user.apis.services;

import com.cc_rental.common.utillity.Converter;
import com.cc_rental.user.apis.containers.EmailFindResultContainer;
import com.cc_rental.user.apis.enums.EmailFindResult;
import com.cc_rental.user.apis.enums.UserRegisterResult;
import com.cc_rental.user.apis.vos.EmailFindVo;
import com.cc_rental.user.apis.vos.UserLoginVo;
import com.cc_rental.common.vos.UserVo;
import com.cc_rental.user.apis.daos.UserDao;
import com.cc_rental.user.apis.enums.UserLoginResult;
import com.cc_rental.user.apis.vos.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class UserService {
    private static final String EMAIL_REGEX = "^(?=.{4,100}.$)([0-9a-zA-Z][0-9a-zA-Z\\-_.]*[0-9a-zA-Z])@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
    private static final String PASSWORD_REGEX = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{4,100})$";

    private final DataSource dataSource;
    private final UserDao userDao;

    @Autowired
    public UserService(DataSource dataSource, UserDao userDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
    }

    public UserLoginResult login(UserLoginVo userLoginVo)
            throws SQLException {
        UserLoginResult userLoginResult;
        if (!userLoginVo.getEmail().matches(EMAIL_REGEX) ||
                !userLoginVo.getPassword().matches(PASSWORD_REGEX)) {
            userLoginResult = UserLoginResult.FAILURE;
        } else {
            try (Connection connection = this.dataSource.getConnection()) {
                UserVo userVo = this.userDao.selectUser(connection, userLoginVo);
                if (userVo == null) {
                    userLoginResult = UserLoginResult.FAILURE;
                } else {
                    Converter.setUserVo(userLoginVo.getRequest(), userVo);
                    userLoginResult = UserLoginResult.SUCCESS;
                }
            }
        }
        return userLoginResult;
    }

    public UserRegisterResult Register(UserRegisterVo userRegisterVo)
            throws SQLException {
        UserRegisterResult userRegisterResult;
        try (Connection connection = this.dataSource.getConnection()) {
            if (this.userDao.selectEmailConut(connection, userRegisterVo.getUser_email()) > 0) {
                userRegisterResult = UserRegisterResult.EMAIL_DUPLICATE;
            } else if (this.userDao.selectContactCount(connection, userRegisterVo.getUser_contact()) > 0) {
                userRegisterResult = UserRegisterResult.CONTACT_DUPLICATE;
            } else {
                this.userDao.insertUser(connection, userRegisterVo);
                if (this.userDao.selectEmailConut(connection, userRegisterVo.getUser_email()) > 0) {
                    userRegisterResult = UserRegisterResult.SUCCESS;
                } else {
                    userRegisterResult = UserRegisterResult.FAILURE;
                }
            }
        }
        return userRegisterResult;
    }


    public EmailFindResultContainer findEmail(EmailFindVo emailFindVo)
            throws SQLException {
        if (emailFindVo.getName().equals("") || emailFindVo.getContact().equals("")) {
            return new EmailFindResultContainer(EmailFindResult.NORMALIZATION_FAILURE);
        } else {
            try (Connection connection = this.dataSource.getConnection()) {
                String email = this.userDao.selectEmail(connection, emailFindVo);
                if (email == null) {
                    return new EmailFindResultContainer(EmailFindResult.FAILURE);
                } else {
                    return new EmailFindResultContainer(EmailFindResult.SUCCSES, email);
                }
            }
        }
    }
}


