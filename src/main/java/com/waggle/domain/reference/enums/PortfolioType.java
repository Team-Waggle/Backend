package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PortfolioType {
    GITHUB("GitHub", "https://logo.clearbit.com/GitHub.com"),
    NOTION("Notion", "https://logo.clearbit.com/Notion.com"),
    LINKEDIN("LinkedIn", "https://logo.clearbit.com/LinkedIn.com"),
    YOUTUBE("YouTube", "https://logo.clearbit.com/YouTube.com"),
    INSTAGRAM("Instagram", "https://logo.clearbit.com/Instagram.com"),
    BRUNCH("Brunch", "https://logo.clearbit.com/Brunch.co.kr"),
    TWITTER("Twitter", "https://logo.clearbit.com/Twitter.com"),
    DRIBBBLE("Dribbble", "https://logo.clearbit.com/Dribbble.com"),
    TRELLO("Trello", "https://logo.clearbit.com/Trello.com"),
    FIGMA("Figma", "https://logo.clearbit.com/Figma.com"),
    OTHER("기타", "");

    private final String displayName;
    private final String imageUrl;
}
