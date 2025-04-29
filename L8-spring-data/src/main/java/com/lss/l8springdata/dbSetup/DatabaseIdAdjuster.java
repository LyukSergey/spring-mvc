package com.lss.l8springdata.dbSetup;

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
        Long studentMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM student", Long.class);
        Long teacherMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM teacher", Long.class);
        Long enrollmentMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM enrollment", Long.class);
        Long departmentMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM department", Long.class);
        Long courseMaxId = jdbcTemplate.queryForObject("SELECT MAX(id) FROM course", Long.class);
        if (studentMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE student ALTER COLUMN id RESTART WITH " + (studentMaxId + 1));
        }
        if (teacherMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE teacher ALTER COLUMN id RESTART WITH " + (teacherMaxId + 1));
        }
        if (enrollmentMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE enrollment ALTER COLUMN id RESTART WITH " + (enrollmentMaxId + 1));
        }
        if (departmentMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE department ALTER COLUMN id RESTART WITH " + (departmentMaxId + 1));
        }
        if (courseMaxId != null) {
            jdbcTemplate.execute("ALTER TABLE course ALTER COLUMN id RESTART WITH " + (courseMaxId + 1));
        }
    }
}
