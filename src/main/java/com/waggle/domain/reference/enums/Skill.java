package com.waggle.domain.reference.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Skill {
    JAVA("Java", "https://logo.clearbit.com/Java.com"),
    JAVASCRIPT("JavaScript", "https://logo.clearbit.com/JavaScript.com"),
    TYPESCRIPT("TypeScript", "https://logo.clearbit.com/TypeScriptlang.org"),
    NODE_JS("Node.js", "https://logo.clearbit.com/Node.js.org"),
    NEXT_JS("Next.js", "https://logo.clearbit.com/Next.js.org"),
    NEST_JS("Nest.js", "https://logo.clearbit.com/Nest.js.org"),
    SVELTE("Svelte", "https://logo.clearbit.com/Svelte.dev"),
    VUE("Vue", "https://logo.clearbit.com/Vuejs.org"),
    REACT("React", "https://logo.clearbit.com/Reactjs.org"),
    SPRING("Spring", "https://logo.clearbit.com/Spring.io"),
    GO("Go", "https://logo.clearbit.com/Go.dev"),
    KOTLIN(
        "Kotlin",
        "https://yt3.googleusercontent.com/QfykgcpEnn5dDj_yG8NwtcQiSiSSZuT091pzEACtBECGh7xuVydpV2l6rEw1IJB0kzLQaTKnfg=s160-c-k-c0x00ffffff-no-rj"
    ),
    EXPRESS("Express", "https://logo.clearbit.com/Express.com"),
    MYSQL("MySQL", "https://logo.clearbit.com/MySQL.com"),
    MONGODB("MongoDB", "https://logo.clearbit.com/MongoDB.com"),
    PYTHON("Python", "https://logo.clearbit.com/Python.org"),
    DJANGO("Django", "https://logo.clearbit.com/Djangoproject.com"),
    PHP("php", "https://logo.clearbit.com/php.net"),
    GRAPHQL("GraphQL", "https://logo.clearbit.com/GraphQL.com"),
    FIREBASE("Firebase", "https://logo.clearbit.com/Firebase.com"),
    FLUTTER("Flutter", "https://logo.clearbit.com/Flutter.dev"),
    SWIFT("Swift", "https://logo.clearbit.com/Swift.org"),
    REACT_NATIVE("ReactNative", "https://logo.clearbit.com/ReactNative.dev"),
    UNITY("Unity", "https://logo.clearbit.com/Unity.com"),
    AWS("AWS", "https://logo.clearbit.com/AWS.com"),
    KUBERNETES("Kubernetes", "https://logo.clearbit.com/Kubernetes.io"),
    DOCKER("Docker", "https://logo.clearbit.com/Docker.com"),
    GIT("Git", "https://logo.clearbit.com/Git-scm.com"),
    FIGMA("Figma", "https://logo.clearbit.com/Figma.com"),
    XD(
        "XD",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c2/Adobe_XD_CC_icon.svg/800px-Adobe_XD_CC_icon.svg.png"
    ),
    ZEPLIN("Zeplin", "https://logo.clearbit.com/Zeplin.com"),
    JEST("Jest", "https://logo.clearbit.com/Jestjs.io"),
    AXURE("Axure", "https://logo.clearbit.com/Axure.com"),
    MS_OFFICE("MS-Office", "https://logo.clearbit.com/office.com"),
    ILLUSTRATOR(
        "illustrator",
        "https://www.adobe.com/cc-shared/assets/img/product-icons/svg/illustrator-40.svg"
    ),
    PHOTOSHOP(
        "Photoshop",
        "https://www.adobe.com/cc-shared/assets/img/product-icons/svg/photoshop-40.svg"
    ),
    INDESIGN(
        "Indesign",
        "https://www.adobe.com/cc-shared/assets/img/product-icons/svg/indesign-40.svg"
    ),
    PREMIERE_PRO(
        "Premiere pro",
        "https://www.adobe.com/cc-shared/assets/img/product-icons/svg/premiere-pro.svg"
    ),
    AFTER_EFFECTS(
        "After Effects",
        "https://www.adobe.com/cc-shared/assets/img/product-icons/svg/after-effects.svg"
    ),
    MAX_3D(
        "3D max",
        "https://damassets.autodesk.net/content/dam/autodesk/www/product-imagery/badge-75x75/simplified-badges/3ds-max-2023-simplified-badge-75x75.png"
    ),
    BLENDER("Blender", "https://logo.clearbit.com/Blender.org"),
    CINEMA_4D(
        "Cinema 4D",
        "https://maxonassets.imgix.net/images/Products/Cinema-4D/C4D-Iso-logo.png?fm=webp&auto=format,compress&w=1920&h=1932&ar=16:9&fit=clip&crop=faces&q=80"
    );

    private final String displayName;
    private final String imageUrl;
}
