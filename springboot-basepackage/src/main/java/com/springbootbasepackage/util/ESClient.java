package com.springbootbasepackage.util;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class ESClient {
    public static RestHighLevelClient getClient(){
        //建立java与ES的连接
        //创建ES服务器地址和端口对象；传递连接地址+端口；
        HttpHost httpHost = new HttpHost("127.0.0.1",9200);
        //创建builder对象,这是一个构建连接的对象；
        RestClientBuilder clientBuilder = RestClient.builder(httpHost);
        //创建一个客户端对象；
        RestHighLevelClient client = new RestHighLevelClient(clientBuilder);
        return client;
    }



}
