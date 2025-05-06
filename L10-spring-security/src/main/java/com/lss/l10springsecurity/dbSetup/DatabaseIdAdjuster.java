package com.lss.l10springsecurity.dbSetup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseIdAdjuster implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseIdAdjuster(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Long usersMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM users", Long.class);
        Long postsMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM posts", Long.class);
        Long rolesMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM roles", Long.class);
        if (usersMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN id RESTART WITH " + (usersMaxId + 1));
        }
        if (postsMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE posts ALTER COLUMN id RESTART WITH " + (postsMaxId + 1));
        }
        if (rolesMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE roles ALTER COLUMN id RESTART WITH " + (rolesMaxId + 1));
        }
    }
}
