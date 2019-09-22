package simpleMoney.library;

import simpleMoney.library.*;
import simpleMoney.library.exceptions.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static spark.Spark.*;
import static spark.Spark.exception;

public abstract class RestServiceBase {

    protected String Service_root;
    protected String Service_root_id;

    public void initApi() {
        port(8080);
        Spark.threadPool(10, 1, 10000);

        Spark.get("/status", (req, res) -> "API is up and running");

        Spark.post(String.format("/%s",Service_root), this::create);
        Spark.get(String.format("/%s/:%s", Service_root, Service_root_id), this::get);
        Spark.delete(String.format("/%s/:%s", Service_root, Service_root_id), this::delete);
    }

    protected abstract String create(Request request, Response response);

    protected abstract String get(Request request, Response response);

    protected abstract String delete(Request request, Response response);

    public void initExceptionMapper() {
        exception(ApiParameterException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(ClientNotFoundException.class, (exception, req, res) -> handleException(exception, res, NOT_FOUND_404));
        exception(MapperException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(AccountException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
    }

    private static <T extends BaseException> void handleException(T exception, Response res, int statusCode) {
        res.status(statusCode);
        res.body(Mapper.toJson(ResponseInfo.create(exception.getMessage(), exception.getResponseCode())));
    }
}
