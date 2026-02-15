package io.github.nether_wart.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_password")
public class UserPassword {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "uid", nullable = false, unique = true)
    long uid;

    @Column(name = "salt", length = 32)
    String salt;

    @Column(name = "password", length = 32)
    String password;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
