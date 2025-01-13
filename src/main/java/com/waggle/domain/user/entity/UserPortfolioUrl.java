package com.waggle.domain.user.entity;

import com.waggle.domain.reference.entity.PortfolioUrl;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class UserPortfolioUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "portfolio_url_type_id")
    private PortfolioUrl portfolioUrl;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;
}
