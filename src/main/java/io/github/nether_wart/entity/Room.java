package io.github.nether_wart.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue
    private Long rid;

    @Column
    private boolean isPublic = true;

    @Column
    private boolean isTraceless = false;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "room_users",
            joinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid"),
            inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid")
    )
    private Set<User> users;

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isTraceless() {
        return isTraceless;
    }

    public void setTraceless(boolean traceless) {
        isTraceless = traceless;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
