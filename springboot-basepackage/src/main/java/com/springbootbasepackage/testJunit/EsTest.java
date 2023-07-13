package com.springbootbasepackage.testJunit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbootbasepackage.entity.Person;
import com.springbootbasepackage.util.ESClient;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class EsTest {

    //详情参考文档地址：https://blog.51cto.com/u_13066/6357320
    String index = "person";
    RestHighLevelClient client = ESClient.getClient();

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testConnect() {
        RestHighLevelClient client1 = ESClient.getClient();
        log.info("client1返回:{}", client1);
    }



    @Test
    public void createIndex() throws IOException {
        // 1、创建setting
        Settings.Builder settings = Settings.builder()
                .put("number_of_shards", 3)
                .put("number_of_replicas", 1);

        // 2、设置mappings
        XContentBuilder mappings = JsonXContent.contentBuilder()
                .startObject()
                .startObject("properties")
                .startObject("name")
                .field("type", "text")
                .endObject()
                .startObject("age")
                .field("type", "integer")
                .endObject()
                .startObject("birthday")
                .field("type", "date")
                .field("format", "yyyy-MM-dd")
                .endObject()
                .endObject()
                .endObject();

        // 3、将settings和mappings封装到Request对象
        CreateIndexRequest request = new CreateIndexRequest(index).settings(settings).mapping(mappings);

        // 4、通过client对象连接ES并创建索引
        CreateIndexResponse resp = client.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(resp);
    }


    @Test
    public void existIndex() throws IOException {
        // 1、创建Request对象
        GetIndexRequest request = new GetIndexRequest(index);
        // 2、通过client对象执行
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        //3、打印返回结果
        System.out.println(exists);
    }


    @Test
    public void deleteIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        // 删除
        client.indices().delete(request, RequestOptions.DEFAULT);
    }





    //创建文档
    @Test
    public void createDoc() throws IOException {
        // 1、准备json数据

        Person person = new Person(1, "chb", 23, new Date());
        String json = mapper.writeValueAsString(person);

        // 准备request对象用于添加数据
        IndexRequest request = new IndexRequest(index);
        request.source(json, XContentType.JSON); // 添加数据

        // 3、通过client对象执行, 注意此处与操作Index的区别 client.indices().create
        IndexResponse resp = client.index(request, RequestOptions.DEFAULT);

        // 4、打印结果
        log.info("resp返回:{}", resp);

    }



    //修改文档
    @Test
    public void updateDoc() throws IOException {
        // 1、创建一个Map, 指定需要修改的内容
        Map<String, Object> doc = new HashMap<String, Object>();
        doc.put("name", "张三");
        String docId = "V2yiTokBhXfSW5bWof_r";

        // 2、创建request对象，封装数据
        UpdateRequest updateRequest = new UpdateRequest(index, docId);
        updateRequest.doc(doc);

        // 3、执行
        UpdateResponse resp = client.update(updateRequest, RequestOptions.DEFAULT);

        // 结果
        log.info("resp返回:{}", resp);
    }


    // 删除
    @Test
    public void deleteDoc() throws IOException {
        DeleteRequest request = new DeleteRequest(index, "N67mgXgB_tiW03WV73UZ");
        client.delete(request, RequestOptions.DEFAULT);
    }


    //批量插入
    @Test
    public void bulkCreateDoc() throws IOException {
        // 1、准备json数据
        Person person1 = new Person(3, "张三", 33, new Date());
        Person person2 = new Person(4, "李四", 44, new Date());
        Person person3 = new Person(5, "王五", 55, new Date());
        String json1 = mapper.writeValueAsString(person1);
        String json2 = mapper.writeValueAsString(person2);
        String json3 = mapper.writeValueAsString(person3);

        // 准备request对象用于添加数据
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest(index).id(person1.getId().toString()).source(json1, XContentType.JSON));
        bulkRequest.add(new IndexRequest(index).id(person2.getId().toString()).source(json2, XContentType.JSON));
        bulkRequest.add(new IndexRequest(index).id(person3.getId().toString()).source(json3, XContentType.JSON));

        // 3、通过client对象执行, 注意此处与操作Index的区别 client.indices().create
        BulkResponse resp = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        // 4、打印结果
        System.out.println(resp);
        log.info("resp返回:{}", resp);
    }


    //批量删除
    @Test
    public void bulkDeleteDoc() throws IOException {
        // DeleteRequest request = new DeleteRequest(index, "N67mgXgB_tiW03WV73UZ");
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new DeleteRequest(index, "3"));
        bulkRequest.add(new DeleteRequest(index, "4"));
        bulkRequest.add(new DeleteRequest(index, "5"));

        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

}
