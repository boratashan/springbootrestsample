
package com.sprinbootsamples.restsample;

import com.sprinbootsamples.restsample.model.Book;
import com.sprinbootsamples.restsample.service.BookItemsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfig.class)
@WebAppConfiguration
public class BooksRestControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;




    @Autowired
    protected BookItemsService bookItemsService;

    private MockMvc mockMvc;

    private List<Book> books;

    @Before
    public void initBooks() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();



        books = new ArrayList<Book>();

        Book book = new Book();
        book.setId(1);
        book.setTitle("Sky Begins at Your Feet");
        book.setAuthor("Norine Dresser");
        book.setImage("http://test.org/image/9780200000000.png");
        book.setPrice(BigDecimal.valueOf(15.69));
        books.add(book);
        book.setId(2);
        book = new Book();
        book.setTitle("Hot Biscuits: Eighteen Stories by Women and Men of the Ranching West");
        book.setAuthor("Caryn Mirriam-Goldberg");
        book.setImage("http://test.org/image/9781890000000.png");
        book.setPrice(BigDecimal.valueOf(12.95));
        books.add(book);
    }

    @Test
    public void testGetBookSuccess() throws Exception {
        this.mockMvc.perform(get("/v1/items/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Sky Begins at Your Feet"));
    }


    @Test
    public void testGetBookItemNotFound() throws Exception {
        this.mockMvc.perform(get("/v1/items/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBookAllItems() throws Exception {
        this.mockMvc.perform(get("/v1/items/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }


    @Test
    public void testCreateBookSuccess() throws Exception {
        Book book = new Book();
        book.setTitle("TestTitle");
        book.setAuthor("TestAuthor");
        book.setPrice(BigDecimal.valueOf(1));
        book.setImage("http://testimg");
        ObjectMapper mapper = new ObjectMapper();
        String newAsJSon = mapper.writeValueAsString(book);
        this.mockMvc.perform(post("/v1/items/")
                .content(newAsJSon).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateBookFailed() throws Exception {
        Book book = new Book();
        book.setAuthor("TestAuthor");
        book.setPrice(BigDecimal.valueOf(1));
        book.setImage("http://testimg");
        ObjectMapper mapper = new ObjectMapper();
        String newAsJSon = mapper.writeValueAsString(book);
        this.mockMvc.perform(post("/v1/items/")
                .content(newAsJSon).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateBookSuccess() throws Exception {

        Book book = books.get(0);
        book.setTitle("Title UPDATED");
        ObjectMapper mapper = new ObjectMapper();
        String newAsJSON = mapper.writeValueAsString(book);
        this.mockMvc.perform(put(String.format("/v1/items/%d", books.get(0).getId()))
                .content(newAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(get(String.format("/v1/items/%d", books.get(0).getId()))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(books.get(0).getId()))
                .andExpect(jsonPath("$.title").value("Title UPDATED"));


    }


    @Test
    public void testUpdateBookFailed() throws Exception {
        Book book = books.get(0);
        book.setTitle("");
        ObjectMapper mapper = new ObjectMapper();
        String newAsJSON = mapper.writeValueAsString(book);
        this.mockMvc.perform(put(String.format("/v1/items/%d", books.get(0).getId()))
                .content(newAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBookSuccess() throws Exception {
        Book book = books.get(0);
        book.setTitle("");
        ObjectMapper mapper = new ObjectMapper();
        String newAsJSON = mapper.writeValueAsString(book);
        this.mockMvc.perform(delete(String.format("/v1/items/%d", books.get(0).getId()))
                .content(newAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBookFailed() throws Exception {
        this.mockMvc.perform(delete("/v1/items/-1")
                .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }


}
