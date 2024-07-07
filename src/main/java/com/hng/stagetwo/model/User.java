package com.hng.stagetwo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Table(name = "\"user\"")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userId;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String phone;
    private String role;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_organization",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    @JsonIgnore
    private Set<Organization> organizations = new HashSet<>();

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Set<Organization> getOrganizations() {
        return organizations;
    }
    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public List<String> getRoleAsList(){
        return Arrays.asList(this.role);
    }
    public void addOrganization(Organization organization) {
        organizations.add(organization);
        organization.getUsers().add(this);
    }
    public void removeOrganization(Organization organization) {
        organizations.remove(organization);
        organization.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
