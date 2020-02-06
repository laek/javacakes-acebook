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

    @Override
    public UUID createPost(String content) {
        String insertSql =
                "insert into posts(content) " +
                "values (:contentParam)";
        try (Connection conn = sql2o.open()) {
            UUID postId = UUID.randomUUID();
            conn.createQuery(insertSql)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return postId;
        }
    }

    @Override
    public List<Post> getAllPosts() {
        //TODO - implement this
        return null;
    }

}