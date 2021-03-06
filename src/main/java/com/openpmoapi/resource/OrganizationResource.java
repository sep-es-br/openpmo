package com.openpmoapi.resource;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.openpmoapi.event.FeatureCreatedEvent;
import com.openpmoapi.model.Organization;
import com.openpmoapi.repository.OrganizationRepository;
import com.openpmoapi.service.OrganizationService;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/api/organization")
@Api(value = "/api/organization",  tags = "Organization",description=" ")
public class OrganizationResource {
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	/**
	 * This is method delete one Organization
	 * 
	 * @param id
	 *			This is the id that will be deleted 
	 *        
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('write')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		organizationRepository.deleteById(id);
	}
	
	
	/**
	 * This is method update Organization
	 * @param id
	 * 			This is the id of the Organization
	 * 
	 * @param organization
	 * 			This is the collection of Organization 
	 * 
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('write')")
	public ResponseEntity<Organization> update(@PathVariable Long id, @Valid @RequestBody Organization organization) {
		Organization savedOrganization = organizationService.update(id, organization);
		return ResponseEntity.ok(savedOrganization);
	}
	
	
	/**
	 * 
	 * This is method save Organization
	 * 
	 * @param organization
	 * 			This is the collection of Organization
	 * 
	 * @param response
	 * 			This is the answer of the HttpServletResponse
	 * 
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('write')")
	public ResponseEntity<Organization> save(@Valid @RequestBody Organization organization, HttpServletResponse response) {
		Organization savedOrganization = organizationRepository.save(organization);
		publisher.publishEvent(new FeatureCreatedEvent(this, response, savedOrganization.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(organizationRepository.save(organization));
	}
	
	
	/**
	 * This is method find by all Organization
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('read')")
	public Iterable<Organization> findByAll() {
		 return organizationRepository.findAll(2);
	}
	
	
	/**
	 * 	This is method find by one Organization by the id
	 * 
	 *  @param id
	 *  		This is the id of the Organization you want to find
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('read')")
	public ResponseEntity<Organization> findById(@PathVariable Long id) {
		Optional<Organization> organization = organizationRepository.findById(id,2);
		return organization.isPresent() ? ResponseEntity.ok(organization.get()) : ResponseEntity.notFound().build();
	}
	
	  
	
	/**
	 * This is method find by name Organization
	 * 
	 * @param name
	 * 			This is the name of the organization
	 * 
	 */
	@GetMapping(path ="/like/{name}")
	@PreAuthorize("hasAuthority('ADMINISTRATOR') and #oauth2.hasScope('read')")
	public Collection<Organization> findByNameLike(@PathVariable("name") String name) {
		return organizationService.findByNameLike(name);
	 
	}
	

	
}
