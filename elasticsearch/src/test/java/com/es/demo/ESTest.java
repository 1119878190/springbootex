package com.es.demo;

import com.alibaba.fastjson.JSON;
import com.es.demo.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ESTest {


    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * 测试：创建索引
     */
    @Test
    void testCreateIndex() {
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
    void testExistIndex() {
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
    void deleteIndex() {
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
    void addDocument() {
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

    /**
     * 测试 文档是否存在
     */
    @Test
    void existDocument() {
        GetRequest request = new GetRequest("spring_es", "2");
        boolean exists = false;
        try {
            exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(exists);
    }

    /**
     * 获取文档
     *
     * @throws IOException
     */
    @Test
    void getDocument() throws IOException {
        GetRequest request = new GetRequest("spring_es", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println(response.getSourceAsString());
    }

    /**
     * 更新文档
     */
    @Test
    void updateDocument() {
        UpdateRequest request = new UpdateRequest("spring_es", "1");
        User user = new User("lafe", 23);
        request.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse response = null;
        try {
            response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);

    }

    /**
     * 删除文档
     *
     * @throws IOException
     */
    @Test
    void deleteDocument() throws IOException {
        DeleteRequest request = new DeleteRequest("spring_es", "1");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response);
    }

    /**
     * 批量插入
     */
    @Test
    void batchSave() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("10s");

        List<User> list = new ArrayList<>();
        list.add(new User("张三1", 1));
        list.add(new User("张三2", 2));
        list.add(new User("张三3", 3));
        list.add(new User("张三4", 4));
        list.add(new User("张三5", 5));
        list.add(new User("张三6", 6));
        list.add(new User("张三7", 7));

        for (int i = 0; i < list.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("spring_es")
                            .id("" + i)
                            .source(JSON.toJSONString(list.get(i)), XContentType.JSON)
            );
        }
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.hasFailures());
    }

    /**
     * 查询
     *
     * @throws IOException
     */
    @Test
    void search() throws IOException {
        SearchRequest request = new SearchRequest("spring_es");

        // 调价构造
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        sourceBuilder.from(1);
        sourceBuilder.size(20);

        // 查询
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword", "张三1");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "张三1");
        sourceBuilder.query(matchQueryBuilder);

        // 高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);

        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        request.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        System.out.println(response);
        System.out.println("===========================");
        for (SearchHit documentFields : response.getHits().getHits()) {

            Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Map<String, Object> sourceAsMap = documentFields.getSourceAsMap();// 原来的结果

            // 解析高亮字段，将原来的字段替换为我们高亮的字段即可
            if (title != null){
                Text[] fragments = title.fragments();
                String n_title = "";
                for (Text text : fragments) {
                    n_title += text;
                }
                sourceAsMap.put("title",n_title);
            }


            System.out.println(documentFields.getSourceAsMap());
        }
    }
}
