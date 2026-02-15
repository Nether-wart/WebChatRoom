package io.github.nether_wart.entity;

import jakarta.persistence.*;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long uid;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private boolean isDisable = false;

    @Column
    private String email;

    @Column(name = "created_at")
    private Long created;

    @ManyToMany(mappedBy = "users", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Room> accessibleRooms;

    public boolean canAccessRoom(Room room) {
        return accessibleRooms.stream()
                .mapToLong(Room::getRid)
                .anyMatch(value -> value == room.getRid());
    }

    public boolean canAccessRoom(Long rid) {
        return accessibleRooms.stream()
                .mapToLong(Room::getRid)
                .anyMatch(value -> value == rid);
    }

    @NonNull
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return null;
    }

    @NonNull
    @Override
    public String getUsername() {
        return name;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        isDisable = disable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Room> getAccessibleRooms() {
        return accessibleRooms;
    }

    public void setAccessibleRooms(Set<Room> accessibleRooms) {
        this.accessibleRooms = accessibleRooms;
    }
}
