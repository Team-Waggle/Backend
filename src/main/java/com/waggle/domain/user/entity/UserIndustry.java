package com.waggle.domain.user.entity;

import com.waggle.domain.reference.entity.Industry;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserIndustry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "industry_id")
    private Industry industry;
}
