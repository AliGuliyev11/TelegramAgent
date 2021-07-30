package com.mycode.telegramagent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.enums.RolePriority;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.Role;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OfferRepo;
import com.mycode.telegramagent.repositories.RoleRepo;
import com.mycode.telegramagent.services.Impl.IAgentServiceImpl;
import com.mycode.telegramagent.services.Impl.OfferServiceImpl;
import com.mycode.telegramagent.services.Impl.OrderServiceImpl;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestingAgent {

    //Agent

    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private AgentDAO agentDAO;
    @Autowired
    private IAgentServiceImpl agentService;

    //Offer

    @Autowired
    private OfferServiceImpl offerService;
    @Autowired
    private OfferRepo offerRepo;
    @Autowired
    private OfferDAO dao;

    //Order

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private MockMvc mockMvc;

    //Role
    @Autowired
    private RoleRepo roleRepo;


    @Test
    @Order(1)
    void getAgentById() {
        com.mycode.telegramagent.models.Agent agent = new com.mycode.telegramagent.models.Agent();
        agent.setEmail("remindersazerbaijan1@gmail.com");
        agent.setIsVerified(true);
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent");
        agent.setCompanyName("TestCompany");
        agent.setPhoneNumber("124435345");
        agent.setHashCode(232435364);
        com.mycode.telegramagent.models.Agent savedAgent = agentRepo.save(agent);
        Assertions.assertEquals(savedAgent.getId(), agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com").getId());
    }

    @Test
    void signup() {
        AgentDto agent = new AgentDto();
        agent.setEmail("remindersazerbaijan@gmail.com");
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent");
        agent.setCompanyName("TestCompany");
        agent.setPhoneNumber("124435345");
        agent.setPassword("12345678");
        com.mycode.telegramagent.models.Agent savedAgent = agentDAO.signup(agent);
        Assertions.assertEquals(savedAgent.getId(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getId());
        Assertions.assertEquals(savedAgent.getEmail(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getEmail());
        Assertions.assertEquals(savedAgent.getVoen(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getVoen());
        Assertions.assertEquals(savedAgent.getAgencyName(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getAgencyName());
        Assertions.assertEquals(savedAgent.getCompanyName(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getCompanyName());
        Assertions.assertEquals(savedAgent.getPhoneNumber(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getPhoneNumber());
        Assertions.assertEquals(savedAgent.getPassword(), agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com").getPassword());
    }

    @Test
    void addRole() {
        Role role = new Role();
        role.setRolePriority(RolePriority.Standard);
        role.setName("ROLE_USER");
        roleRepo.save(role);
        Assertions.assertEquals("ROLE_USER", roleRepo.getRoleByRolePriority(RolePriority.Standard).getName());
    }

    @Test
    void getAgentByHashCode() {
        com.mycode.telegramagent.models.Agent agent = new com.mycode.telegramagent.models.Agent();
        agent.setId(1l);
        agent.setEmail("remindersazerbaijan1@gmail.com");
        agent.setIsVerified(true);
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent");
        agent.setCompanyName("TestCompany");
        agent.setPhoneNumber("124435345");
        agent.setHashCode(232435364);
        Role role = roleRepo.getRoleByRolePriority(RolePriority.Standard);
        agent.getRoles().add(role);
        Assertions.assertEquals(agent.getId(), agentRepo.getAgentByHashCode(232435364).getId());
        Assertions.assertEquals(agent.getEmail(), agentRepo.getAgentByHashCode(232435364).getEmail());
        Assertions.assertEquals(agent.getVoen(), agentRepo.getAgentByHashCode(232435364).getVoen());
        Assertions.assertEquals(agent.getAgencyName(), agentRepo.getAgentByHashCode(232435364).getAgencyName());
        Assertions.assertEquals(agent.getCompanyName(), agentRepo.getAgentByHashCode(232435364).getCompanyName());
        Assertions.assertEquals(agent.getPhoneNumber(), agentRepo.getAgentByHashCode(232435364).getPhoneNumber());
        Assertions.assertEquals(agent.getPassword(), agentRepo.getAgentByHashCode(232435364).getPassword());
    }


    @Test
    void checkAgencyName() {
        Assertions.assertEquals(true, agentDAO.checkAgencyName("TestAgent"));
    }


    @Test
    void checkEmail() {
        Assertions.assertEquals(true, agentDAO.checkEmail("remindersazerbaijan1@gmail.com"));
    }

    @Test
    void checkCompanyName() {
        Assertions.assertEquals(true, agentRepo.checkCompany("TestCompany"));
    }

    @SneakyThrows
    @Test
    void checkUniqueAgencyName() {
        AgentDto agent = new AgentDto();
        agent.setEmail("remindersazerbaijan2@gmail.com");
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent");
        agent.setCompanyName("TestCompany2");
        agent.setPhoneNumber("124435345");
        Assertions.assertThrows(AgencyExist.class, () -> agentService.checkUnique(agent));

    }

    @SneakyThrows
    @Test
    void checkUniqueCompanyName() {
        AgentDto agent = new AgentDto();
        agent.setEmail("remindersazerbaijan2@gmail.com");
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent2");
        agent.setCompanyName("TestCompany");
        agent.setPhoneNumber("124435345");

        Assertions.assertThrows(CompanyExist.class, () -> agentService.checkUnique(agent));

    }

    @SneakyThrows
    @Test
    void checkUniqueEmail() {
        AgentDto agent = new AgentDto();
        agent.setEmail("remindersazerbaijan@gmail.com");
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent2");
        agent.setCompanyName("TestCompany2");
        agent.setPhoneNumber("124435345");

        Assertions.assertThrows(EmailAlreadyExist.class, () -> agentService.checkUnique(agent));

    }

    @Test
    void forgotPassNullAgent() {

        String email = "remindergrtsazerbaijan@gmail.com";

        Assertions.assertThrows(EmailNotFound.class, () -> agentService.forgotPassword(email));
    }


    @Test
    @Order(5)
    void getAgentsOfferNotAnyOffer() {
        Assertions.assertThrows(NotAnyOffer.class, () ->
                offerService.getAgentOffers("remindersazerbaijan@gmail.com"));
    }

    @Test
    @Order(6)
    void saveOffer() {
        UserRequest userRequest = new UserRequest();
        Map<String, String> order = new HashMap<>();
        order.put("lang", "AZ");
        order.put("OrderTravel", "OrderTravel");
        order.put("Orderaddress1", "Orderaddress1");
        order.put("Orderaddress2", "Orderaddress2");
        order.put("Orderdate", "2021-07-30");
        order.put("Ordertravelle", "14");
        order.put("Orderbudget", "5000");
        order.put("Orderdateto", "5");

        Gson gson = new Gson();
        String json = gson.toJson(order);

        userRequest.setUserRequest(json);
        userRequest.setRequestStatus(RequestStatus.Active);
        userRequest.setExpiredDate(null);
        userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
        userRequest.setUserId("23243554");
        Agent agent = agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com");
        userRequest.setAgent(agent);
        orderDAO.saveUserRequest(userRequest);

        OfferDto offer = OfferDto.builder()
                .description("Testing desc")
                .note("Testing note")
                .price(300d)
                .startDate("2021-07-25")
                .endDate("2021-07-25")
                .build();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String email = "remindersazerbaijan1@gmail.com";
        Offer savedOffer = dao.saveOffer("23243554", agent.getEmail(), offer);
        Assertions.assertEquals(savedOffer.getId(), offerService.getOfferById(1l, email).getId());
        Assertions.assertEquals(savedOffer.getDescription(), offerService.getOfferById(1l, email).getDescription());
        Assertions.assertEquals(simpleDateFormat.format(savedOffer.getStartDate()), simpleDateFormat.format(offerService.getOfferById(1l, email).getStartDate()));
        Assertions.assertEquals(simpleDateFormat.format(savedOffer.getStartDate()), simpleDateFormat.format(offerService.getOfferById(1l, email).getStartDate()));
        Assertions.assertEquals(savedOffer.getNote(), offerService.getOfferById(1l, email).getNote());
        Assertions.assertEquals(savedOffer.getPrice(), offerService.getOfferById(1l, email).getPrice());
        Assertions.assertEquals(savedOffer.getAgent().getId(), offerService.getOfferById(1l, email).getAgent().getId());
        Assertions.assertEquals(savedOffer.getAgent().getEmail(), offerService.getOfferById(1l, email).getAgent().getEmail());
        Assertions.assertEquals(savedOffer.getAgent().getAgencyName(), offerService.getOfferById(1l, email).getAgent().getAgencyName());
        Assertions.assertEquals(savedOffer.getAgent().getCompanyName(), offerService.getOfferById(1l, email).getAgent().getCompanyName());
        Assertions.assertEquals(savedOffer.getAgent().getPhoneNumber(), offerService.getOfferById(1l, email).getAgent().getPhoneNumber());
        Assertions.assertEquals(savedOffer.getAgent().getHashCode(), offerService.getOfferById(1l, email).getAgent().getHashCode());
        Assertions.assertEquals(savedOffer.getAcceptedDate(), offerService.getOfferById(1l, email).getAcceptedDate());
        Assertions.assertEquals(savedOffer.getPhoneNumber(), offerService.getOfferById(1l, email).getPhoneNumber());

    }

    @SneakyThrows
    @Test
    void checkOfferDate() {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(1l, "remindersazerbaijan1@gmail.com");
        JSONObject jsonObject = new JSONObject(userRequest.getUserRequest());
        String orderDate = jsonObject.getString("Orderdate");
        String startDate = "2021-07-24";
        Assertions.assertThrows(CheckStartDate.class, () -> offerService.checkStartDate(startDate, orderDate, jsonObject.getLong("Orderdateto")));
    }

    @Test
    @Order(7)
    void getAgentsOffer() {

        List<Offer> offers = offerRepo.getOffersByAgentEmail("remindersazerbaijan1@gmail.com");
        Assertions.assertEquals(false, offers.isEmpty());
    }

    @Test
    void getAgentsOfferNull() {
        Assertions.assertThrows(NotAnyOffer.class, () -> offerService.getAgentOffers("ertet4t54@gmail.com"));
    }

    @Test
    @Order(8)
    void offerAcceptedTest() {
        ReplyToOffer replyToOffer = new ReplyToOffer();
        replyToOffer.setOfferId(1l);
        replyToOffer.setPhoneNumber("+994501234567");
        dao.offerAccepted(replyToOffer);

        Offer acceptedOffer = dao.getOfferById("remindersazerbaijan1@gmail.com", 1l);
        Assertions.assertEquals(AgentRequestStatus.Accepted, acceptedOffer.getUserRequest().getAgentRequestStatus());
    }

    @Test
    @Order(9)
    void getRequestByUUIDAndEmailTest() {
        UserRequest userRequest = orderDAO.getUserRequestByIdAndAgentEmail(1l, "remindersazerbaijan1@gmail.com");
        System.out.println(userRequest.getAgentRequestStatus());
        AgentRequestStatus agentRequestStatus = userRequest.getAgentRequestStatus();
        Assertions.assertEquals(AgentRequestStatus.Accepted, agentRequestStatus);
    }

    @Test
    @Order(10)
    void getAllRequests() {


        Assertions.assertEquals(false, orderDAO.getAllRequests("remindersazerbaijan1@gmail.com").isEmpty());
    }

    @Test
    @Order(11)
    void getRequestById() {

        UserRequest userRequest = orderDAO.getOrderById(1l);

        Assertions.assertEquals(userRequest.getAgent().getEmail(), userRequest.getAgent().getEmail());
    }


    @Test
    @Order(12)
    void getAllArchiveEmpty() {
        UserRequest userRequest = new UserRequest();
        Map<String, String> order = new HashMap<>();
        order.put("lang", "AZ");
        order.put("OrderTravel", "OrderTravel");
        order.put("Orderaddress1", "Orderaddress1");
        order.put("Orderaddress2", "Orderaddress2");
        order.put("Orderdate", "2021-07-30");
        order.put("Ordertravelle", "14");
        order.put("Orderbudget", "5000");
        order.put("Orderdateto", "5");

        Gson gson = new Gson();
        String json = gson.toJson(order);

        userRequest.setUserRequest(json);
        userRequest.setRequestStatus(RequestStatus.Active);
        userRequest.setExpiredDate(null);
        userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
        userRequest.setUserId("23243554");
        Agent agent = agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com");
        userRequest.setAgent(agent);
        orderDAO.saveUserRequest(userRequest);
        Assertions.assertThrows(NotAnyRequest.class, () -> orderService.getAllArchive("remindersazerbaijan1@gmail.com"));
    }

    @Test
    @Order(13)
    void addToArchive() {

        UserRequest archivedUserRequest = orderDAO.addToArchive("remindersazerbaijan1@gmail.com", 2l);
        UserRequest userRequest = orderDAO.getOrderById(2l);
        Assertions.assertEquals(userRequest.getAgentRequestStatus(), archivedUserRequest.getAgentRequestStatus());
    }

    @Test
    @Order(14)
    void getAllArchiveTest() {

        List<UserRequest> archive = orderDAO.getAllArchive("remindersazerbaijan1@gmail.com");

        Assertions.assertEquals(false, archive.isEmpty());
    }

    @Test
    void addToArchiveNull() {
        Assertions.assertThrows(RequestNotFound.class, () -> orderService.addToArchive("remindersazerbaijan1@gmail.com", 100l));
    }

    @Test
    void getAllRequestsNull() {
        Assertions.assertThrows(NotAnyRequest.class, () -> orderService.getAllRequests("weergri@gmail.com").isEmpty());
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Test
    @Order(15)
    void showOffersController() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/offer/show-offers").requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(offerService.getAgentOffers(agentDto.getEmail()))));
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Test
    @Order(16)
    void getAllRequestsController() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-all").requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllRequests(agentDto.getEmail()))));
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Transactional
    @Test
    @Order(17)
    void getAllNewRequestsNullController() {
        Assertions.assertThrows(NotAnyRequest.class, () -> orderService.getAllNewRequests("remindersazerbaijan1@gmail.com").isEmpty());
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Transactional
    @Test
    @Order(18)
    void getAllNewRequestsNotNullController() {
        UserRequest userRequest = new UserRequest();
        Map<String, String> order = new HashMap<>();
        order.put("lang", "AZ");
        order.put("OrderTravel", "OrderTravel");
        order.put("Orderaddress1", "Orderaddress1");
        order.put("Orderaddress2", "Orderaddress2");
        order.put("Orderdate", "2021-07-30");
        order.put("Ordertravelle", "14");
        order.put("Orderbudget", "5000");
        order.put("Orderdateto", "5");

        Gson gson = new Gson();
        String json = gson.toJson(order);

        userRequest.setUserRequest(json);
        userRequest.setRequestStatus(RequestStatus.Active);
        userRequest.setExpiredDate(null);
        userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
        userRequest.setUserId("23243554");
        Agent agent = agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com");
        userRequest.setAgent(agent);
        orderDAO.saveUserRequest(userRequest);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-new").requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllNewRequests(agentDto.getEmail()))));
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Transactional
    @Test
    @Order(19)
    void getAllAcceptedController() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-accepted").requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllAcceptedRequests(agentDto.getEmail()))));
    }
    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Transactional
    @Test
    @Order(20)
    void getAllOfferMadeNullController() {
        Assertions.assertThrows(NotAnyRequest.class, () -> orderService.getAllNewRequests("remindersazerbaijan1@gmail.com").isEmpty());
    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Transactional
    @Test
    @Order(21)
    void getAllOfferMadeController() {

        UserRequest userRequest=orderDAO.getUserRequestByIdAndAgentEmail(1l,"remindersazerbaijan1@gmail.com");
        userRequest.setAgentRequestStatus(AgentRequestStatus.Offer_Made);
        orderDAO.saveUserRequest(userRequest);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-offer-made").requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllOfferMadeRequests(agentDto.getEmail()))));
    }


    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Test
    @Order(22)
    void addToArchiveRequestController() {


        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/request/add-archive/{id}", 2l).requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.addToArchive(agentDto.getEmail(), 2l))));

    }

    @WithMockUser(username = "ROLE_USER")
    @SneakyThrows
    @Test
    @Order(23)
    void getAllArchiveRequestController() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-archive")
                .requestAttr("user", agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllArchive(agentDto.getEmail()))));
    }

//    @WithMockUser(username="ROLE_USER")
//    @SneakyThrows
//    @Test
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    void sendOffer() {
//
//        UserRequest userRequest = new UserRequest();
//        Map<String, String> order = new HashMap<>();
//        order.put("lang", "AZ");
//        order.put("OrderTravel", "OrderTravel");
//        order.put("Orderaddress1", "Orderaddress1");
//        order.put("Orderaddress2", "Orderaddress2");
//        order.put("Orderdate", "2021-07-30");
//        order.put("Ordertravelle", "14");
//        order.put("Orderbudget", "5000");
//        order.put("Orderdateto", "5");
//
//        Gson gson = new Gson();
//        userRequest.setUserRequest(gson.toJson(order));
//
//        userRequest.setRequestStatus(RequestStatus.Active);
//        userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
//        userRequest.setUserId("232435545");
//        Agent agent = agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com");
//        userRequest.setAgent(agent);
//        orderDAO.saveUserRequest(userRequest);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
//        df.setTimeZone(TimeZone.getTimeZone("GMT"));
//        mapper.setDateFormat(df);
//
//        ObjectMapper mapper2 = new ObjectMapper();
//        mapper2.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//
//        AgentDto agentDto = AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();
//
//        OfferDto offer = OfferDto.builder()
//                .description("Testing desc")
//                .note("Testing note")
//                .price(300d)
//                .startDate("2021-07-26")
//                .endDate("2021-07-26")
//                .build();
//
//        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/offer/{userId}", "232435545")
//                .requestAttr("user", agentDto)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper2.writeValueAsString(offer));
//        ResultActions result = mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(mapper.writeValueAsString(dao.getOfferById("remindersazerbaijan1@gmail.com", 2l))));
//    }

}
