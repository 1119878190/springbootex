package com.es.demo;

import com.alibaba.fastjson.JSON;
import com.es.demo.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ESTest {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 测试：创建索引
     */
    @Test
    void testCreateIndex(){
        // 1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest("spring_es");
        // 2.执行创建请求
        CreateIndexResponse response = null;
        try {
            response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

    /**
     * 测试索引是否存在
     */
    @Test
    void testExistIndex(){
        GetIndexRequest request = new GetIndexRequest("spring_es");
        boolean exists = false;
        try {
            exists = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(exists);
    }


    /**
     * 测试 删除索引
     */
    @Test
    void deleteIndex(){
        DeleteIndexRequest request = new DeleteIndexRequest("spring_es");
        AcknowledgedResponse response = null;
        try {
            response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.isAcknowledged());
    }

    /**
     * 添加文档
     */
    @Test
    void addDocument(){
        User user = new User("张三", 3);
        IndexRequest request = new IndexRequest("spring_es");
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        request.source(JSON.toJSONString(user), XContentType.JSON);

        IndexResponse response = null;
        try {
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.status());
    }

    @Test
    void existDocument(){
        GetRequest request = new GetRequest("spring_es", "2");
        boolean exists = false;
        try {
            exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(exists);
    }
}
