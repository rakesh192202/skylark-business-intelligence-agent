package com.skylark.business_agent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skylark.business_agent.model.Deal;
import com.skylark.business_agent.model.WorkOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MondayService {

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${monday.deals.board.id}")
    private Long dealsBoardId;

    @Value("${monday.workorders.board.id}")
    private Long workOrdersBoardId;

    public MondayService(
            @Value("${monday.api.url}") String apiUrl,
            @Value("${monday.api.token}") String apiToken) {

        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, apiToken)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String getBoards() {

        String query = """
        {
          boards {
            id
            name
          }
        }
        """;

        return executeQuery(query);
    }

    public String getDeals() {
        return fetchBoard(dealsBoardId);
    }

    public String getWorkOrders() {
        return fetchBoard(workOrdersBoardId);
    }

    public List<Deal> getDealsData() throws Exception {

        String json = getDeals();

        JsonNode board = mapper.readTree(json)
                .path("data")
                .path("boards")
                .get(0);

        Map<String, String> columnMap = buildColumnMap(board);

        JsonNode items = board.path("items_page").path("items");

        List<Deal> deals = new ArrayList<>();

        for (JsonNode item : items) {

            Deal deal = new Deal();

            deal.setId(item.path("id").asText());
            deal.setName(item.path("name").asText());

            for (JsonNode column : item.path("column_values")) {

                String columnId = column.path("id").asText();
                String columnTitle = columnMap.getOrDefault(columnId, columnId);

                deal.getFields().put(
                        columnTitle,
                        column.path("text").asText("")
                );
            }

            deals.add(deal);
        }

        return deals;
    }

    public List<WorkOrder> getWorkOrdersData() throws Exception {

        String json = getWorkOrders();

        JsonNode board = mapper.readTree(json)
                .path("data")
                .path("boards")
                .get(0);

        Map<String, String> columnMap = buildColumnMap(board);

        JsonNode items = board.path("items_page").path("items");

        List<WorkOrder> workOrders = new ArrayList<>();

        for (JsonNode item : items) {

            WorkOrder workOrder = new WorkOrder();

            workOrder.setId(item.path("id").asText());
            workOrder.setName(item.path("name").asText());

            for (JsonNode column : item.path("column_values")) {

                String columnId = column.path("id").asText();
                String columnTitle = columnMap.getOrDefault(columnId, columnId);

                workOrder.getFields().put(
                        columnTitle,
                        column.path("text").asText("")
                );
            }

            workOrders.add(workOrder);
        }

        return workOrders;
    }

    private Map<String, String> buildColumnMap(JsonNode board) {

        Map<String, String> map = new HashMap<>();

        for (JsonNode column : board.path("columns")) {

            map.put(
                    column.path("id").asText(),
                    column.path("title").asText()
            );
        }

        return map;
    }

    private String fetchBoard(Long boardId) {

        String query = """
        {
          boards(ids:%d){
            id
            name

            columns{
              id
              title
              type
            }

            items_page{
              items{
                id
                name

                column_values{
                  id
                  text
                  type
                  value
                }
              }
            }
          }
        }
        """.formatted(boardId);

        return executeQuery(query);
    }

    private String executeQuery(String query) {

        String body = """
        {
          "query":"%s"
        }
        """.formatted(
                query.replace("\"", "\\\"")
                        .replace("\n", " ")
        );

        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}