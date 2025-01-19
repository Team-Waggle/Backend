package com.waggle.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.waggle.domain.reference.entity.PortfolioUrl;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPortfolioUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "portfolio_url_type_id")
    @JsonProperty("url_type")
    private PortfolioUrl portfolioUrl;

    @Column(name = "url", nullable = false, length = 1000)
    @JsonProperty("url")
    private String url;
}
