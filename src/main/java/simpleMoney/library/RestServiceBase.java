package simpleMoney.library;

import simpleMoney.library.exceptions.*;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.concurrent.*;

import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;
import static org.eclipse.jetty.http.HttpStatus.NOT_FOUND_404;
import static spark.Spark.*;
import static spark.Spark.exception;

public abstract class RestServiceBase {

    protected String Service_root;
    protected String Service_root_id;
    private ExecutorService executor;

    protected void initApi() {
        executor = new ThreadPoolExecutor(3, 3, 0L,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(15));

        Spark.port(8080);
        Spark.get("/status", (req, res) -> "API is up and running");
        Spark.post(String.format("/%s",Service_root), this::create);
        Spark.get(String.format("/%s/:%s", Service_root, Service_root_id), this::get);
        Spark.delete(String.format("/%s/:%s", Service_root, Service_root_id), this::delete);
    }

    protected abstract String create(Request request, Response response);

    protected abstract String get(Request request, Response response);

    protected abstract String delete(Request request, Response response);

    protected void initExceptionMapper() {
        exception(ApiParameterException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(NotFoundException.class, (exception, req, res) -> handleException(exception, res, NOT_FOUND_404));
        exception(MapperException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
        exception(AlreadyExistsException.class, (exception, req, res) -> handleException(exception, res, BAD_REQUEST_400));
    }

    protected String executeRequest(Callable<String> callable){
        String responseCode;
        try {
            Future<String> returnValue = executor.submit(callable);
            responseCode = returnValue.get();
        } catch (InterruptedException | ExecutionException e) {
            responseCode = ResponseCode.FAILURE.toString();
            executor.shutdown();
        }

        return responseCode;
    }

    private static <T extends BaseException> void handleException(T exception, Response res, int statusCode) {
        res.status(statusCode);
        res.body(Mapper.toJson(ResponseInfo.create(exception.getMessage(), exception.getResponseCode())));
    }
}
