package com.mata.enumPackage;

public enum CosFileMkdir {
    HeadImg("headImg"),
    GoodsImg("goodsImg"),
    ArticleHtmlImg("ArticleHtmlImg");
    private final String mkdirName;

    public String getMkdirName() {
        return mkdirName;
    }

    CosFileMkdir(String mkdirName) {
        this.mkdirName = mkdirName;
    }
}
