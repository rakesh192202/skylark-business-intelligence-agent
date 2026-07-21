package com.skylark.business_agent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MondayService {

    private final WebClient webClient;

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

    // Fetch all boards (optional)
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

    // Deals Board
    public String getDeals() {
        return fetchBoard(dealsBoardId);
    }

    // Work Orders Board
    public String getWorkOrders() {
        return fetchBoard(workOrdersBoardId);
    }

    // Common method for fetching any board
    private String fetchBoard(Long boardId) {

        String query = """
        {
          boards(ids:%d){
            id
            name
            items_page{
              items{
                id
                name
                column_values{
                  id
                  text
                }
              }
            }
          }
        }
        """.formatted(boardId);

        return executeQuery(query);
    }

    // Executes GraphQL query
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