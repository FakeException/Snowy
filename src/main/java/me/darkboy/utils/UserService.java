package me.darkboy.utils;

import me.darkboy.Snowy;
import me.darkboy.mysql.queries.InsertQuery;
import me.darkboy.mysql.queries.Query;
import me.darkboy.mysql.queries.SelectQuery;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public void createAccount(UserDetails user) throws SQLException {
        String sql = (new InsertQuery("accounts"))
                .value("username")
                .value("email")
                .value("password")
                .value("salt")
                .value("ip").build();
        Query query = new Query(Snowy.getMySQLConnection(), sql);
        query.setParameter(1, user.getUsername());

        String salt = PasswordUtils.getSalt(30);
        String password = PasswordUtils.generateSecurePassword(user.getPassword(), salt);

        query.setParameter(2, user.getEmail());
        query.setParameter(3, password);
        query.setParameter(4, salt);
        query.setParameter(5, getIp());
        query.executeUpdateAsync();
    }

//    public void login(UserDetails userDetails) throws SQLException {
//
//        if (isAccountExist(userDetails)) {
//            String newSecurePassword = PasswordUtils.generateSecurePassword(userDetails.getPassword(), Objects.requireNonNull(getSalt(userDetails)));
//            if (newSecurePassword.equals(getPassword(userDetails))) {
//
//            } else {
//                Notifications.showError("The given password is invalid!");
//            }
//        } else {
//            Notifications.showError("Account not found!");
//        }
//
//    }

    public static String getPassword(UserDetails userDetails) throws SQLException {
        String sql = (new SelectQuery("accounts")).column("password").where("username = '" + userDetails.getUsername() + "'").build();
        Query query = new Query(Snowy.getMySQLConnection(), sql);
        ResultSet rs = query.executeQuery();
        return rs.next() ? rs.getString("password") : null;
    }

    public String getIp() {
        String ipAddress;
        try {
            URL url = new URL("https://bot.whatismyipaddress.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = in.readLine();
        } catch (Exception ex) {
            return "ERROR";
        }
        return ipAddress;
    }

    public boolean isAccountExist(UserDetails user) throws SQLException {
        String selectQuery = (new SelectQuery("accounts")).column("*").where("username = '" + user.getUsername() + "'").build();
        Query query1 = new Query(Snowy.getMySQLConnection(), selectQuery);
        return query1.executeQuery().next();
    }
}
