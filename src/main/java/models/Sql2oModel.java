package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }

 //   UUID userId = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @Override
    public UUID createUser(String name, String email, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userId = UUID.randomUUID();
            conn.createQuery("insert into users(user_id, name, email, password) VALUES (:user_id, :name, :email, :password)")
                    .addParameter("user_id", userId)
                    .addParameter("name", name)
                    .addParameter("email", email)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
            return userId;
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Connection conn = sql2o.open()) {
            List<User> users = conn.createQuery("SELECT * FROM users")
                    .executeAndFetch(User.class);
            return users;
        }
    }

    @Override
    public boolean authenticate(String email, String password) {
        boolean correctLoginDetails = false;

        try (Connection conn = sql2o.open()) {
            List<User> authenticatedUser = conn.createQuery("SELECT * FROM users WHERE email = '" + email + "'")
                    .executeAndFetch(User.class);

            if (authenticatedUser == null) {
                return correctLoginDetails;
            } else if (authenticatedUser.toString().contains(password)) {
                correctLoginDetails = true;
            }
        }

        return correctLoginDetails;
    }

    @Override
    public String getName(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT name FROM users WHERE email = '" + email + "'")
                    .executeScalar(String.class);

        }
    }

    @Override
    public UUID getUserId(String email) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT user_id FROM users WHERE email = '" + email + "'")
                    .executeScalar(UUID.class);

        }
    }

    @Override
    public UUID createPost(String content, UUID userId) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postId = UUID.randomUUID();
            conn.createQuery("INSERT INTO posts (post_id, user_id, content) VALUES (:post_id, :user_id, :content)")
                    .addParameter("post_id", postId)
                    .addParameter("user_id", userId)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return postId;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (Connection conn = sql2o.open()) {
            List<Post> posts = conn.createQuery("SELECT * FROM posts ORDER BY time_stamp DESC")
                    .executeAndFetch(Post.class);
            return posts;
        }
    }
}

