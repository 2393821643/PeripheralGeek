package com.mata.enumPackage;

public enum CosFileMkdir {
    HeadImg("headImg"),
    GoodsImg("goodsImg"),
    GoodsHtmlImg("goodsHtmlImg"),
    ArticleHtmlImg("ArticleHtmlImg"),
    ArticleImg("articleImg");
    private final String mkdirName;

    public String getMkdirName() {
        return mkdirName;
    }

    CosFileMkdir(String mkdirName) {
        this.mkdirName = mkdirName;
    }
}
