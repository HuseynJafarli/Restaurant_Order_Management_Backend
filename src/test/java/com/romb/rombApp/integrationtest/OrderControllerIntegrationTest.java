// package com.romb.rombApp.integrationtest;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.romb.rombApp.dto.OrderRequestDTO;
// import com.romb.rombApp.model.Order;
// import com.romb.rombApp.model.OrderStatus;
// import com.romb.rombApp.repository.OrderRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.hamcrest.Matchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// @WithMockUser(username = "testuser", roles = "WAITER")
// public class OrderControllerIntegrationTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private OrderRepository orderRepository;

//     @Autowired
//     private ObjectMapper objectMapper;

//     private Order testOrder;

//     @BeforeEach
//     void setUp() {
//         orderRepository.deleteAll();

//         Order order = new Order();
//         order.setStatus(OrderStatus.IN_PREPARATION);
//         testOrder = orderRepository.save(order);
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void createOrder_ShouldReturnCreatedOrder() throws Exception {
//         OrderRequestDTO dto = new OrderRequestDTO();
//         dto.setStatus(OrderStatus.IN_PREPARATION);

//         mockMvc.perform(post("/api/orders")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(dto)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.status", is("IN_PREPARATION")));
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void getOrderById_ShouldReturnOrder() throws Exception {
//         mockMvc.perform(get("/api/orders/{id}", testOrder.getId()))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id", is(testOrder.getId().intValue())))
//                 .andExpect(jsonPath("$.status", is("IN_PREPARATION")));
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void getOrderById_NotFound_ShouldReturn404() throws Exception {
//         mockMvc.perform(get("/api/orders/{id}", 9999L))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void updateOrder_ShouldReturnUpdatedOrder() throws Exception {
//         OrderRequestDTO dto = new OrderRequestDTO();
//         dto.setStatus(OrderStatus.IN_PREPARATION);

//         mockMvc.perform(put("/api/orders/{id}", testOrder.getId())
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(dto)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.status", is("IN_PREPARATION")));
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void deleteOrder_ShouldRemoveOrder() throws Exception {
//         mockMvc.perform(delete("/api/orders/{id}", testOrder.getId()))
//                 .andExpect(status().isOk());

//         mockMvc.perform(get("/api/orders/{id}", testOrder.getId()))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     @WithMockUser(username = "testuser", roles = "WAITER")
//     void getAllOrders_ShouldReturnList() throws Exception {
//         mockMvc.perform(get("/api/orders"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)));
//     }
// }

