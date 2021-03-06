package muyinatech.myelasticsearch;

import com.google.gson.Gson;
import muyinatech.myelasticsearch.model.GuestPost;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;


public class SimpleElasticSearch {

    public static void main(String[] args) throws IOException {
        // on startup
        Node node = nodeBuilder().node();
        Client client = node.client();

        createDocument(client, "1", new GuestPost("Fred", "UK", "Hello my friend.", new Date()));
        createDocument(client, "2", new GuestPost("Sarah", "USA", "Hi nice to visit.", new Date()));
        createDocument(client, "3", new GuestPost("Billy", "France", "Bonjour!", new Date()));

       // readIndex(client, "twitter", "tweet", "3");
      //  searchDocument(client, "twitter", "tweet", "user", "kimchy");
       // deleteDocument(client, "twitter", "tweet", "3" );

        // on shutdown
        node.close();
    }

    private static void createDocument(Client client, String id, GuestPost guestPost)  throws IOException {

        client.prepareIndex("guestbook", "post", id)
                .setSource(new Gson().toJson(guestPost))
                .execute()
                .actionGet();
    }

    private static void readIndex(Client client, String index, String type, String id) {

        GetResponse getResponse = client.prepareGet(index, type, id).execute().actionGet();
        Map<String, Object> source = getResponse.getSource();
        System.out.println("------------------------------");
        System.out.println("Index: " + getResponse.getIndex());
        System.out.println("Type: " + getResponse.getType());
        System.out.println("Id: " + getResponse.getId());
        System.out.println("Version: " + getResponse.getVersion());
        System.out.println(source);
        System.out.println("------------------------------");
    }

    private static void searchDocument(Client client, String index, String type,
                                      String field, String value){
        QueryBuilder qb = termQuery(field, value);
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_AND_FETCH)
                .setQuery(qb)
                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

        SearchHit[] results = response.getHits().getHits();

        System.out.println("Current results: " + results.length);
        for (SearchHit hit : results) {
            System.out.println("------------------------------");
            Map<String,Object> result = hit.getSource();
            System.out.println(result);
        }

    }

    private static void deleteDocument(Client client, String index, String type, String id){

        DeleteResponse response = client.prepareDelete(index, type, id).execute().actionGet();
        System.out.println("Information on the deleted document:");
        System.out.println("Index: " + response.getIndex());
        System.out.println("Type: " + response.getType());
        System.out.println("Id: " + response.getId());
        System.out.println("Version: " + response.getVersion());
    }
}
