package spring.mvc.lecture.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.mvc.lecture.entity.User;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM testdb.public.users_test";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            return user;
        });
    }

    public int createUser(User user) {
        String sql = "INSERT INTO testdb.public.users_test (name) VALUES (?)";
        return jdbcTemplate.update(sql, user.getName());
    }

    public int updateUser(long id, String name, String email) {
        String sql = "UPDATE testdb.public.users_test SET name = ?, email = ? WHERE id = ?";
        return jdbcTemplate.update(sql, name, email, id);
    }

    public int deleteUser(long id) {
        String sql = "DELETE FROM testdb.public.users_test WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<User> findById(long id) {
        String sql = "SELECT * FROM testdb.public.users_test WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            return user;
        });
    }

}
