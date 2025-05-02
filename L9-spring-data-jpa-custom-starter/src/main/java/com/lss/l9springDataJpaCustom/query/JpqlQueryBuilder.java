package com.lss.l9springDataJpaCustom.query;

public class JpqlQueryBuilder {

    public static String buildFindByQuery(String entityName, String methodName) {
        String fieldsPart = methodName.substring(6);
        String[] parts = fieldsPart.split("And");
        StringBuilder jpql = new StringBuilder("SELECT e FROM " + entityName + " e WHERE ");
        for (int i = 0; i < parts.length; i++) {
            String field = Character.toLowerCase(parts[i].charAt(0)) + parts[i].substring(1);
            if (i > 0) {
                jpql.append(" AND ");
            }
            jpql.append("e.").append(field).append(" = :param").append(i);
        }
        return jpql.toString();
    }
}
