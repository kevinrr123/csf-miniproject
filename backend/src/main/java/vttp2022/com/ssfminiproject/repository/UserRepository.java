package vttp2022.com.ssfminiproject.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.com.ssfminiproject.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public User getByUsername(String username) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from users where username = ?",username);
        User user = new User();
        if (rs.next()){
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));;
            return user;
        }
        return null;
    }

    public String getByEmail(String email) {

        SqlRowSet rs = jdbcTemplate.queryForRowSet("select * from contactinfo where email = ?",email);
        String existEmail = null;
        if (rs.next()){
            existEmail = rs.getString("email");
            return existEmail;
        }
        return null;
    }

    public boolean save(User nUser) {
        int saved = jdbcTemplate.update("insert into users (username, password) values (?, ?)", nUser.getUsername(), nUser.getPassword());
        SqlRowSet rs1 = jdbcTemplate.queryForRowSet("select userid from users where username = ?",nUser.getUsername());
        int userid = 0;
        if (rs1.next()){
            userid = rs1.getInt("userid");
        }
        saved += jdbcTemplate.update("insert into contactinfo (user_id, email) values (?, ?)", userid, nUser.getEmail());
        return saved > 1;
    }
}
