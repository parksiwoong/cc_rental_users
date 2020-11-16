package com.cc_rental.user.apis.daos;

import com.cc_rental.user.apis.vos.EmailFindVo;
import com.cc_rental.user.apis.vos.UserLoginVo;
import com.cc_rental.common.vos.UserVo;
import com.cc_rental.user.apis.vos.UserRegisterVo;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserDao {
    public UserVo selectUser(Connection connection, UserLoginVo userLoginVo)
            throws SQLException {
        UserVo userVo = null;
        String query = "" +
                "SELECT `user_index`              AS `userIndex`,\n" +
                "       `user_email`              AS `userEmail`,\n" +
                "       `user_password`           AS `userPassword`,\n" +
                "       `user_name`               AS `userName`,\n" +
                "       `user_contact`            AS `userContact`,\n" +
                "       `user_address`            AS `userAddress`,\n" +
                "       `user_license_type`       AS `userLtype`,\n" +
                "       `user_license_number`     AS `userLnumber`,\n" +
                "       `user_license_issue_date` AS `userLdate`,\n" +
                "       `user_level`              AS `userLevel`\n" +
                "FROM cc_rental.users\n" +
                "WHERE `user_email` = ?\n" +
                "  AND `user_password` = ?\n" +
                "LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userLoginVo.getEmail());
            preparedStatement.setString(2, userLoginVo.getHashedPassword());
            preparedStatement.executeQuery();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()) {
                    userVo = new UserVo(
                            resultSet.getInt("userIndex"),
                            resultSet.getString("userEmail"),
                            resultSet.getString("userPassword"),
                            resultSet.getString("userName"),
                            resultSet.getString("userContact"),
                            resultSet.getString("userAddress"),
                            resultSet.getString("userLtype"),
                            resultSet.getString("userLnumber"),
                            resultSet.getString("userLdate"),
                            resultSet.getInt("userLevel"));
                }
            }
        }
        return userVo;
    }

    public void insertUser(Connection connection, UserRegisterVo userRegisterVo)
            throws SQLException {
        UserVo userVo = null;
        String query = "" +
                "INSERT INTO`cc_rental`.`users`(user_email, \n" +
                "                               user_password, \n" +
                "                               user_name,\n" +
                "                               user_contact,\n" +
                "                               user_address,\n" +
                "                               user_license_type,\n" +
                "                               user_license_number,\n" +
                "                               user_license_issue_date)\n" +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userRegisterVo.getUser_email());
            preparedStatement.setString(2, userRegisterVo.getHashedPassword());
            preparedStatement.setString(3, userRegisterVo.getUser_name());
            preparedStatement.setString(4, userRegisterVo.getUser_contact());
            preparedStatement.setString(5, userRegisterVo.getUser_address());
            preparedStatement.setString(6, userRegisterVo.getUser_license_type());
            preparedStatement.setString(7, userRegisterVo.getUser_license_number());
            preparedStatement.setString(8, userRegisterVo.getUser_license_issue_date());
            preparedStatement.execute();
        }
    }

    public int selectEmailConut(Connection connection, String email)
            throws SQLException {
        int count;
        String query = "" +
                "SELECT COUNT(`user_index`) AS `count` FROM `cc_rental`.`users` WHERE `user_email` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.executeQuery();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                resultSet.next();
                count = resultSet.getInt("count");
            }
        }
        return count;
    }

    public int selectContactCount(Connection connection, String contact)
            throws SQLException {
        int count;
        String query = "" +
                "SELECT COUNT(`user_index`) AS `count` FROM `cc_rental`.`users` WHERE `user_contact` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, contact);
            preparedStatement.executeQuery();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                resultSet.next();
                count = resultSet.getInt("count");
            }
        }
        return count;

    }

    public String selectEmail(Connection connection, EmailFindVo emailFindVo)
            throws SQLException {
        String email = null;
        String query = "SELECT `user_email` AS `userEmail`\n" +
                "FROM `cc_rental`.`users`\n" +
                "WHERE `user_name` = ? AND `user_contact` = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, emailFindVo.getName());
            preparedStatement.setString(2, emailFindVo.getContact());
            preparedStatement.executeQuery();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                while (resultSet.next()){
                    email = resultSet.getString("userEmail");
                }
            }
        }
        return email;

    }
}