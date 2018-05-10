package com.schoolofnet.Testing.services;

import java.util.List;

import com.schoolofnet.Testing.models.Person;

public interface PeopleService {

	public Person create(Person person);
	public List<Person> findAll();
	public void remove(Long id);
}
