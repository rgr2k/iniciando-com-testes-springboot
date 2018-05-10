package com.schoolofnet.Testing;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.schoolofnet.Testing.controllers.PeopleController;
import com.schoolofnet.Testing.models.Person;
import com.schoolofnet.Testing.services.PeopleService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PeopleController.class)
public class PeopleTest {

	@Autowired
	private MockMvc mock;
	
	@MockBean
	private PeopleService peopleService;
	
	@Test
	public void findAll() throws Exception {
		Person leonan = new Person((long) 1, "Leonan", 22);
		List<Person> mockPeople = Arrays.asList(leonan);
		
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String mockPeopleJSON = ow.writeValueAsString(mockPeople);
		
		when(peopleService.findAll()).thenReturn(mockPeople);
		mock.perform(get("/people")
					.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is(200))
		.andExpect(content().json(mockPeopleJSON));
	}
	
	@Test
	public void createNewPerson() throws Exception {
		Person mockPerson = new Person((long) 10, "Erik", 28);
		
		when(peopleService.create(any(Person.class))).thenReturn(mockPerson);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String mockPersonJSON = ow.writeValueAsString(mockPerson);
		
		mock.perform(post("/people")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(mockPersonJSON))
		.andExpect(status().isOk())
		.andExpect(content().json(mockPersonJSON));
		verify(peopleService).create(any(Person.class));		
	}
	
	@Test
	public void removePerson() throws Exception {
		mock.perform(delete("/people" + "/{id}", new Long(1)))
			.andExpect(status().is(200));
	}
	
	@Test
	public void createNewPersonAndFail() throws Exception {
		Person mockPerson = new Person((long) 9, "", 23);
		
		when(peopleService.create(any(Person.class))).thenReturn(mockPerson);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = ow.writeValueAsString(mockPerson);
		
		mock.perform(post("/people")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
		.andExpect(status().is(400));
	}
}
