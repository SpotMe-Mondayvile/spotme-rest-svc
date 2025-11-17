package com.mts.spotmerest.models.friendships;

import com.mts.spotmerest.models.User;
import jakarta.persistence.*;

@Entity
@Table(name = "subscribe_users")
public class SubscribeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int targetUserId;
    private User user;

    public SubscribeUser(int targetUserId, User user) {
        this.targetUserId = targetUserId;
        this.user = user;
    }

    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
