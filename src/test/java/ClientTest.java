import com.google.gson.Gson;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.elasticsearch.action.ListenableActionFuture;
import org.junit.Test;
import uk.co.telegraph.content.api.filter.Query;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.*;

import static org.asynchttpclient.Dsl.*;

public class ClientTest {

    private static final Gson gson = new Gson();

    @Test
    public void call() throws IOException{

        try(AsyncHttpClient asyncHttpClient = asyncHttpClient()){


            ListenableFuture<Response> whenResponse = asyncHttpClient.
                    preparePost("http://localhost:8080/content/v3/content/scroll").
                    setBody(gson.toJson(Query.all())).
                    setHeader("Content-type", "application/json").
                    execute();

            Runnable callback = () -> {
                try  {
                    Response response = whenResponse.get();
                    System.out.println(response);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            };

            Executor executor = Executors.newSingleThreadExecutor();
            whenResponse.addListener(callback, executor);


            Thread.sleep(500000);

        }catch(Exception e){
            e.printStackTrace();
        }



    }

}
