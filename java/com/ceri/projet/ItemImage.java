package com.ceri.projet;

public class ItemImage {
    private String description;
    private String imageUrl;

    ItemImage(String description, String imageUrl) {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
