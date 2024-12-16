package com.mata.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtil {
    /**
     * 获取前50字内容
     */
    public static String getParagraphsUntil50Chars(String html) {
        Document doc = Jsoup.parse(html);
        Elements paragraphs = doc.select("p");
        StringBuilder result = new StringBuilder();
        int targetLength = 50;

        for (Element p : paragraphs) {
            // 添加当前段落的文本到结果中
            result.append(p.text().trim());

            // 如果结果长度已经达到或超过了目标长度，则停止添加
            if (result.length() >= targetLength) {
                break;
            }
        }

        // 确保返回的结果至少有50个字符
        if (result.length() < targetLength) {
            // 如果所有段落加起来都不足50个字符，则直接返回现有结果
            return result.toString();
        } else {
            // 如果结果超过了50个字符，只返回前50个字符
            return result.substring(0, Math.min(result.length(), targetLength));
        }
    }
}
