package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Skill {
    JAVA("https://logo.clearbit.com/Java.com", "Java"),
    JAVASCRIPT("https://logo.clearbit.com/JavaScript.com", "JavaScript"),
    TYPESCRIPT("https://logo.clearbit.com/TypeScriptlang.org", "TypeScript"),
    NODE_JS("https://logo.clearbit.com/Node.js.org", "Node.js"),
    NEXT_JS("https://logo.clearbit.com/Next.js.org", "Next.js"),
    NEST_JS("https://logo.clearbit.com/Nest.js.org", "Nest.js"),
    SVELTE("https://logo.clearbit.com/Svelte.dev", "Svelte"),
    VUE("https://logo.clearbit.com/Vuejs.org", "Vue"),
    REACT("https://logo.clearbit.com/Reactjs.org", "React"),
    SPRING("https://logo.clearbit.com/Spring.io", "Spring"),
    GO("https://logo.clearbit.com/Go.dev", "Go"),
    KOTLIN(
        "https://yt3.googleusercontent.com/QfykgcpEnn5dDj_yG8NwtcQiSiSSZuT091pzEACtBECGh7xuVydpV2l6rEw1IJB0kzLQaTKnfg=s160-c-k-c0x00ffffff-no-rj",
        "Kotlin"),
    EXPRESS("https://logo.clearbit.com/Express.com", "Express"),
    MYSQL("https://logo.clearbit.com/MySQL.com", "MySQL"),
    MONGODB("https://logo.clearbit.com/MongoDB.com", "MongoDB"),
    PYTHON("https://logo.clearbit.com/Python.org", "Python"),
    DJANGO("https://logo.clearbit.com/Djangoproject.com", "Django"),
    PHP("https://logo.clearbit.com/php.net", "php"),
    GRAPHQL("https://logo.clearbit.com/GraphQL.com", "GraphQL"),
    FIREBASE("https://logo.clearbit.com/Firebase.com", "Firebase"),
    FLUTTER("https://logo.clearbit.com/Flutter.dev", "Flutter"),
    SWIFT("https://logo.clearbit.com/Swift.org", "Swift"),
    REACT_NATIVE("https://logo.clearbit.com/ReactNative.dev", "ReactNative"),
    UNITY("https://logo.clearbit.com/Unity.com", "Unity"),
    AWS("https://logo.clearbit.com/AWS.com", "AWS"),
    KUBERNETES("https://logo.clearbit.com/Kubernetes.io", "Kubernetes"),
    DOCKER("https://logo.clearbit.com/Docker.com", "Docker"),
    GIT("https://logo.clearbit.com/Git-scm.com", "Git"),
    FIGMA("https://logo.clearbit.com/Figma.com", "Figma"),
    XD("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Adobe_XD_CC_icon.svg/800px-Adobe_XD_CC_icon.svg.png",
        "XD"),
    ZEPLIN("https://logo.clearbit.com/Zeplin.com", "Zeplin"),
    JEST("https://logo.clearbit.com/Jestjs.io", "Jest"),
    AXURE("https://logo.clearbit.com/Axure.com", "Axure"),
    MS_OFFICE("https://logo.clearbit.com/office.com", "MS-Office"),
    ILLUSTRATOR("https://www.adobe.com/cc-shared/assets/img/product-icons/svg/illustrator-40.svg",
        "illustrator"),
    PHOTOSHOP("https://www.adobe.com/cc-shared/assets/img/product-icons/svg/photoshop-40.svg",
        "Photoshop"),
    INDESIGN("https://www.adobe.com/cc-shared/assets/img/product-icons/svg/indesign-40.svg",
        "Indesign"),
    PREMIERE_PRO("https://www.adobe.com/cc-shared/assets/img/product-icons/svg/premiere-pro.svg",
        "Premiere pro"),
    AFTER_EFFECTS("https://www.adobe.com/cc-shared/assets/img/product-icons/svg/after-effects.svg",
        "After Effects"),
    MAX_3D(
        "https://damassets.autodesk.net/content/dam/autodesk/www/product-imagery/badge-75x75/simplified-badges/3ds-max-2023-simplified-badge-75x75.png",
        "3D max"),
    BLENDER("https://logo.clearbit.com/Blender.org", "Blender"),
    CINEMA_4D(
        "https://maxonassets.imgix.net/images/Products/Cinema-4D/C4D-Iso-logo.png?fm=webp&auto=format,compress&w=1920&h=1932&ar=16:9&fit=clip&crop=faces&q=80",
        "Cinema 4D");

    private final String imageUrl;
    private final String name;
}
