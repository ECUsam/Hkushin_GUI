import FileManager.ScriptReader;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import postfix.DataManger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Server {

    public static void main(String[] args) {
        try {
            ScriptReader scriptReader = new ScriptReader("E:\\download\\新约迫真战记―温暖神话ver0.561 完全汉化\\a_default\\script");
            scriptReader.readAll();

            int port = 11481;
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new MyHandler());
            server.start();
            System.out.println("Server is running on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String jsonResponse = "test";
            String queryString = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQuery(queryString);
            String method = queryParams.get("method");
            String arg = queryParams.get("arg");
            if(method.equals("type")){
                Gson gson = new Gson();
                HashMap<String, String> map = DataManger.getUniqueNameClassFromType(arg);
                jsonResponse = gson.toJson(map);
            }else if(method.equals("attr")){
                String arg2 = queryParams.get("arg2");
                jsonResponse = DataManger.searchFeatureFromClassName_one_to_fathers(arg, arg2);
            }else if(method.equals("attr_consti")){
                Gson gson = new Gson();
                var tmp_data = DataManger.getAttrMapFromCharaName(arg);
                if(tmp_data==null){
                    jsonResponse = "";
                }else
                    jsonResponse = gson.toJson(tmp_data);
            }else if(method.equals("attr_all")){
                String arg2 = queryParams.get("arg2");
                jsonResponse = Arrays.toString(DataManger.searchFeatureFromClassName_all(arg, arg2));
            }else if(method.equals("get_type")){
                jsonResponse = DataManger.getTypeOfName(arg);
            }

            // System.out.println(jsonResponse);
            if(jsonResponse==null){
                jsonResponse = "";
            }
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            // 发送响应内容
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

    private static Map<String, String> parseQuery(String queryString) {
        Map<String, String> queryParams = new java.util.HashMap<>();
        if (queryString != null) {
            String[] params = queryString.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

}
