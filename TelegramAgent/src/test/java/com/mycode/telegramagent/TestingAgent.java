package com.mycode.telegramagent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mycode.telegramagent.dao.Interface.AgentDAO;
import com.mycode.telegramagent.dao.Interface.OfferDAO;
import com.mycode.telegramagent.dao.Interface.OrderDAO;
import com.mycode.telegramagent.dto.AgentDto;
import com.mycode.telegramagent.dto.OfferDto;
import com.mycode.telegramagent.dto.ReplyToOffer;
import com.mycode.telegramagent.enums.AgentRequestStatus;
import com.mycode.telegramagent.enums.Languages;
import com.mycode.telegramagent.enums.RequestStatus;
import com.mycode.telegramagent.exceptions.*;
import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.models.Offer;
import com.mycode.telegramagent.models.UserRequest;
import com.mycode.telegramagent.repositories.AgentRepo;
import com.mycode.telegramagent.repositories.OfferRepo;
import com.mycode.telegramagent.repositories.OrderRepo;
import com.mycode.telegramagent.services.Impl.IAgentServiceImpl;
import com.mycode.telegramagent.services.Impl.OfferServiceImpl;
import com.mycode.telegramagent.services.Impl.OrderServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
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
        Assertions.assertEquals(savedAgent, agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com"));
    }

    @Test
    void signup() {
        AgentDto agent = new AgentDto();
        agent.setEmail("remindersazerbaijan@gmail.com");
        agent.setVoen("1243354645");
        agent.setAgencyName("TestAgent");
        agent.setCompanyName("TestCompany");
        agent.setPhoneNumber("124435345");
        com.mycode.telegramagent.models.Agent savedAgent = agentDAO.signup(agent);
        Assertions.assertEquals(savedAgent, agentRepo.getAgentByEmail("remindersazerbaijan@gmail.com"));
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
        Assertions.assertEquals(agent, agentRepo.getAgentByHashCode(232435364));
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

//    @Test
//    void checkOfferDate() {
//        Date orderDate = new Date();
//        String startDate = "2021-07-24";
//        Assertions.assertThrows(CheckStartDate.class, () -> offerService.checkStartDate(startDate, orderDate));
//    }


    @Test
    @Order(5)
    void getAgentsOfferNotAnyOffer() {
        Assertions.assertThrows(NotAnyOffer.class, () ->
                offerService.getAgentOffers("remindersazerbaijan@gmail.com"));
    }

//    @Test
//    @Order(6)
//    void saveOffer() {
//        UserRequest userRequest = new UserRequest();
//        userRequest.setLanguage(Languages.AZ);
//        userRequest.setOrdertravel("OrderTravel");
//        userRequest.setOrderaddress1("Orderaddress1");
//        userRequest.setOrderaddress2("Orderaddress2");
//        userRequest.setOrderdate(new Date());
//        userRequest.setOrdertraveller(14);
//        userRequest.setOrderbudget(5000);
//        userRequest.setOrderdateto(5);
//        userRequest.setRequestStatus(RequestStatus.Active);
//        userRequest.setAgentRequestStatus(AgentRequestStatus.New_Request);
//        userRequest.setUserId("23243554");
//        Agent agent = agentRepo.getAgentByEmail("remindersazerbaijan1@gmail.com");
//        userRequest.setAgent(agent);
//        orderDAO.saveUserRequest(userRequest);
//
//        OfferDto offer = OfferDto.builder()
//                .description("Testing desc")
//                .note("Testing note")
//                .price(300d)
//                .startDate("2021-07-25")
//                .endDate("2021-07-25")
//                .build();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//
//        Offer savedOffer = dao.saveOffer("23243554", agent.getEmail(), offer);
//        Assertions.assertEquals(savedOffer.getId(), offerService.getOfferById(1l).getId());
//        Assertions.assertEquals(savedOffer.getDescription(), offerService.getOfferById(1l).getDescription());
//        Assertions.assertEquals(simpleDateFormat.format(savedOffer.getStartDate()), simpleDateFormat.format(offerService.getOfferById(1l).getStartDate()));
//        Assertions.assertEquals(simpleDateFormat.format(savedOffer.getStartDate()), simpleDateFormat.format(offerService.getOfferById(1l).getStartDate()));
//        Assertions.assertEquals(savedOffer.getNote(), offerService.getOfferById(1l).getNote());
//        Assertions.assertEquals(savedOffer.getPrice(), offerService.getOfferById(1l).getPrice());
//        Assertions.assertEquals(savedOffer.getAgent(), offerService.getOfferById(1l).getAgent());
//        Assertions.assertEquals(savedOffer.getAcceptedDate(), offerService.getOfferById(1l).getAcceptedDate());
//        Assertions.assertEquals(savedOffer.getPhoneNumber(), offerService.getOfferById(1l).getPhoneNumber());
//
//    }

    @Test
    @Order(7)
    void getAgentsOffer() {

        List<Offer> offers = offerRepo.getOffersByAgentEmail("remindersazerbaijan1@gmail.com");
        Assertions.assertEquals(false, offers.isEmpty());
    }
    @Test
    void getAgentsOfferNull() {
        Assertions.assertThrows(NotAnyOffer.class, ()->offerService.getAgentOffers("ertet4t54@gmail.com"));
    }

    @Test
    @Order(8)
    void offerAcceptedTest() {
        ReplyToOffer replyToOffer = new ReplyToOffer();
        replyToOffer.setOfferId(1l);
        replyToOffer.setPhoneNumber("+994501234567");
        dao.offerAccepted(replyToOffer);

        Offer acceptedOffer = dao.getOfferById("remindersazerbaijan1@gmail.com",1l);
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

//    @Test
//    @Order(10)
//    void getAllRequests() {
//        com.mycode.telegramagent.dto.Order order = new com.mycode.telegramagent.dto.Order();
//        order.setLanguage(Languages.AZ);
//        order.setOrdertravel("OrderTravel");
//        order.setOrderaddress1("Orderaddress1");
//        order.setOrderaddress2("Orderaddress2");
//        order.setOrderdate(new Date());
//        order.setOrdertraveller(14);
//        order.setOrderbudget(5000);
//        order.setOrderdateto(5);
//        order.setUserId("23243554");
//
//        orderDAO.addOrder(order);
//
//        Assertions.assertEquals(false, orderDAO.getAllRequests("remindersazerbaijan1@gmail.com").isEmpty());
//    }

    @Test
    @Order(11)
    void getRequestById() {

        UserRequest userRequest = orderDAO.getOrderById(1l);

        Assertions.assertEquals(userRequest.getAgent().getEmail(), userRequest.getAgent().getEmail());
    }


    @Test
    @Order(12)
    void getAllArchiveEmpty() {
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

    @SneakyThrows
    @Test
    @Order(15)
    void showOffersController(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        AgentDto agentDto= AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/offer/show-offers").requestAttr("user",agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(offerService.getAgentOffers(agentDto.getEmail()))));
    }

    @SneakyThrows
    @Test
    @Order(16)
    void getAllRequestsController(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        AgentDto agentDto= AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-all").requestAttr("user",agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllRequests(agentDto.getEmail()))));
    }

    @SneakyThrows
    @Test
    @Order(17)
    void addToArchiveRequestController(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto= AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.put("/api/v1/request/add-archive/{id}",2l).requestAttr("user",agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.addToArchive(agentDto.getEmail(),2l))));
    }

    @SneakyThrows
    @Test
    @Order(18)
    void getAllArchiveRequestController(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        mapper.setDateFormat(df);
        mapper.registerModule(new JavaTimeModule());
        AgentDto agentDto= AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();

        RequestBuilder request = MockMvcRequestBuilders.get("/api/v1/request/show-archive")
                .requestAttr("user",agentDto);
        ResultActions result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(orderService.getAllArchive(agentDto.getEmail()))));
    }


//    @SneakyThrows
//    @Test
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    void sendOffer(){
//
//        UserRequest userRequest = new UserRequest();
//        userRequest.setLanguage(Languages.AZ);
//        userRequest.setOrdertravel("OrderTravele");
//        userRequest.setOrderaddress1("Orderaddresse");
//        userRequest.setOrderaddress2("Orderaddresse");
//        userRequest.setOrderdate(new Date());
//        userRequest.setOrdertraveller(14);
//        userRequest.setOrderbudget(5000);
//        userRequest.setOrderdateto(5);
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
//        AgentDto agentDto= AgentDto.builder().email("remindersazerbaijan1@gmail.com").build();
//
//        OfferDto offer = OfferDto.builder()
//                .description("Testing desc")
//                .note("Testing note")
//                .price(300d)
//                .startDate("2021-07-26")
//                .endDate("2021-07-26")
//                .build();
//
//        RequestBuilder request = MockMvcRequestBuilders.post("/api/v1/offer/{userId}","232435545")
//                .requestAttr("user",agentDto)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper2.writeValueAsString(offer));
//        ResultActions result = mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json(mapper.writeValueAsString(dao.getOfferById(2l))));
//    }

}
