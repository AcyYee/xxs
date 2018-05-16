package com.cwy.xxs.dvo;

import lombok.Data;

import java.util.List;

/**
 * @author acy19
 */
public class WxMessage implements Cloneable{

    private String toUser,templateId,page,data,fromId,emphasisKeyword;

    private List<KeyWord> keyWords;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getEmphasisKeyword() {
        return emphasisKeyword;
    }

    public void setEmphasisKeyword(String emphasisKeyword) {
        this.emphasisKeyword = emphasisKeyword;
    }

    public List<KeyWord> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<KeyWord> keyWords) {
        this.keyWords = keyWords;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder("");
        temp.append("{," + "\"touser\":\"").append(toUser).append("\"").append(",\"template_id\":\"").append(templateId).append("\"");
        if (page!=null){
            temp.append(",\"page\":\"").append(page).append("\"");
        }
        temp.append(",\"from_id\":\"").append(fromId).append("\"");
        temp.append(",\"data\":").append(setData());
        if (emphasisKeyword != null){
            temp.append(",\"emphasis_keyword\":\"").append(emphasisKeyword).append("\"");
        }
        temp.append('}');
        return temp.toString().replaceFirst(",","");
    }
    @Override
    public WxMessage clone() throws CloneNotSupportedException {
        return (WxMessage)super.clone();
    }
    private String setData(){
        StringBuilder builder = new StringBuilder("{");
        for (int i = 1;i<=keyWords.size();i++){
            builder.append(",\"keyword").append(i).append("\":{").append("\"value\":").append("\"").append(keyWords.get(i-1).getValue()).append("\",\"").append("value\":").append("\"").append(keyWords.get(i-1).getColor()).append("\"").append("}");
        }
        builder.append("}");
        return builder.toString().replaceFirst(",","");
    }
}
