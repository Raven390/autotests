package generator.app;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) throws SQLException {
        var connection = DriverManager.getConnection("jdbc:sqlite:generated-data.db");
        var objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
                .disable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        var handler = new RulesHandler(
                new RuleTypeOperations("helpers.data.rules"), new MetadataRepository(connection, objectMapper));

        Javalin.create(config -> {
                    config.router.apiBuilder(() -> path("/v1/rules", () -> {
                        post("/{name}", ctx -> handler.create(ctx.pathParam("name"))
                                .ifPresentOrElse(
                                        item -> ctx.status(HttpStatus.ACCEPTED),
                                        () -> ctx.status(HttpStatus.NOT_FOUND)));
                        delete("/{name}", ctx -> handler.delete(ctx.pathParam("name"))
                                .ifPresentOrElse(
                                        item -> ctx.status(HttpStatus.ACCEPTED),
                                        () -> ctx.status(HttpStatus.NOT_FOUND)));
                        get("/{name}", ctx -> handler.getOne(ctx.pathParam("name"))
                                .ifPresentOrElse(ctx::json, () -> ctx.status(HttpStatus.NOT_FOUND)));
                        get(ctx -> ctx.json(handler.getAll()));
                    }));
                    config.jsonMapper(new JavalinJackson(objectMapper, true));
                })
                .start(8080);
    }
}
