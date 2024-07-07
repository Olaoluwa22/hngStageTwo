package com.hng.stagetwo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String orgId;
    @Column(nullable = false)
    private String name;
    private String description;
    @ManyToMany(mappedBy = "organizations")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Set<User> getUsers() {
        return users;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }
    public void addUser(User user) {
        users.add(user);
        user.getOrganizations().add(this);
    }
    public void removeUser(User user) {
        users.remove(user);
        user.getOrganizations().remove(this);
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", orgId='" + orgId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
