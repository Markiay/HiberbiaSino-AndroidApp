package test;

import com.dao.UserDao;
import com.dao.UserDaoImpl;
import com.dto.User;
import com.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDaoTest {

    public static void main(String[] args){
        Connection conn = null;
        conn = ConnectionFactory.getInstance().makeConnection();
        try {
            conn.setAutoCommit(false);
            UserDao userDao = new UserDaoImpl();
            User newUser = new User();
            newUser.setUsername("q5");
            newUser.setPassword("1234567890");

            userDao.insert(conn, newUser);
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
